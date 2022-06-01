
package com.biperf.core.service.ssi.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.ssi.SSIContestAwardThemNowDAO;
import com.biperf.core.domain.enums.SSIContestIssuanceStatusType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAwardThemNowService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestContentValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;

/**
 * 
 * SSIContestAwardThemNowServiceImpl.
 * 
 * @author kandhi
 * @since Feb 5, 2015
 * @version 1.0
 */
public class SSIContestAwardThemNowServiceImpl extends SSIContestServiceImpl implements SSIContestAwardThemNowService
{

  private SSIContestAwardThemNowDAO ssiContestAwardThemNowDAO;
  protected static final Log log = LogFactory.getLog( SSIContestAwardThemNowServiceImpl.class );

  public void setSsiContestAwardThemNowDAO( SSIContestAwardThemNowDAO ssiContestAwardThemNowDAO )
  {
    this.ssiContestAwardThemNowDAO = ssiContestAwardThemNowDAO;
  }

  /**
   * Save the contest participants with the award issuance number
   * {@inheritDoc}
   */
  @Override
  public void saveContestParticipants( Long contestId, Long[] participantIds, Short awardIssuanceNumber )
  {
    // Save the contest participants with the award issuance number as calculated above
    awardIssuanceNumber = getAwardIssuanceNumber( contestId, awardIssuanceNumber );

    List<Long> existingParticipantIds = ssiContestAwardThemNowDAO.getExistingContestParticipantIds( participantIds, contestId, awardIssuanceNumber );

    for ( Long existingParticipantId : existingParticipantIds )
    {
      participantIds = (Long[])ArrayUtils.removeElement( participantIds, existingParticipantId.longValue() );
    }
    if ( participantIds.length > 0 )
    {
      ssiContestAwardThemNowDAO.saveContestParticipants( contestId, participantIds, awardIssuanceNumber );
    }
  }

  @Override
  public int getContestParticipantsCount( Long contestId, Short awardIssuanceNumber )
  {
    awardIssuanceNumber = getAwardIssuanceNumber( contestId, awardIssuanceNumber );
    return ssiContestAwardThemNowDAO.getContestParticipantsCount( contestId, awardIssuanceNumber );
  }

  public List<SSIContestParticipant> getContestParticipants( Long contestId, Short awardIssuanceNumber, Integer pageNumber, Integer pageSize )
  {
    return ssiContestAwardThemNowDAO.getContestParticipants( contestId, awardIssuanceNumber, pageNumber, pageSize, null, null );
  }

  @Override
  public List<SSIContestParticipant> getContestParticipants( Long contestId,
                                                             Short awardIssuanceNumber,
                                                             Integer pageNumber,
                                                             String sortedOn,
                                                             String sortedBy,
                                                             AssociationRequestCollection associationRequestCollection )
  {
    List<SSIContestParticipant> contestParticipants = ssiContestAwardThemNowDAO.getContestParticipants( contestId,
                                                                                                        awardIssuanceNumber,
                                                                                                        pageNumber,
                                                                                                        SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                                                        sortedOn,
                                                                                                        sortedBy );
    if ( associationRequestCollection != null )
    {
      for ( SSIContestParticipant contestParticipant : contestParticipants )
      {
        for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = iterator.next();
          req.execute( contestParticipant.getParticipant() );
        }
      }
    }
    return contestParticipants;
  }

  @Override
  public SSIContest updateContestAwardIssuanceNumber( Long contestId )
  {
    SSIContest contest = this.getContestById( contestId );
    Short awardIssuanceNumber = getAwardIssuanceNumber( contestId, null );
    contest.setAwardIssuanceNumber( awardIssuanceNumber );
    return contest;
  }

  @Override
  public SSIContestAwardThemNow approveContest( Long contestId, Long userId, int approvalLevel, Short awardIssuanceNumber ) throws ServiceErrorException
  {
    return updateApprovalStatus( contestId, userId, approvalLevel, APPROVE, "", awardIssuanceNumber );
  }

  @Override
  public void denyContest( Long contestId, Long userId, int approvalLevel, String denialReson, Short awardIssuanceNumber ) throws ServiceErrorException
  {
    updateApprovalStatus( contestId, userId, approvalLevel, DENY, denialReson, awardIssuanceNumber );

  }

  private SSIContestAwardThemNow updateApprovalStatus( Long contestId, Long userId, int approvalLevel, String approvalType, String denialReson, Short awardIssuanceNumber ) throws ServiceErrorException
  {
    // Making sure that only contest approvers are approving the contest
    validateUserForApprovalDecision( contestId, userId );

    // Get the contest
    SSIContest attachedSSIContest = getContestById( contestId );

    // Get the Award Them Now issuance
    SSIContestAwardThemNow contestAwardThemNow = ssiContestAwardThemNowDAO.getContestAwardThemNow( contestId, awardIssuanceNumber );

    // Make sure contest is in live status and issunace is non approved
    validateContestStatusForAppovalDecision( attachedSSIContest, contestAwardThemNow );

    boolean sendAlerts = false;
    String issuanceStatus = null;

    if ( attachedSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL )
    {
      if ( APPROVE.equalsIgnoreCase( approvalType ) )
      {
        issuanceStatus = SSIContestIssuanceStatusType.APPROVED;
      }
      else if ( DENY.equalsIgnoreCase( approvalType ) )
      {
        issuanceStatus = SSIContestIssuanceStatusType.DENIED;
      }
    }
    else if ( attachedSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL )
    {
      // Make sure Level 1 approver is not approving level 2 as well
      validateIfLevel1ApproverIsApprovingLevel2( userId, contestAwardThemNow );

      if ( APPROVAL_LEVEL_ONE == approvalLevel )
      {
        if ( APPROVE.equalsIgnoreCase( approvalType ) )
        {
          issuanceStatus = SSIContestIssuanceStatusType.WAITING_FOR_APPROVAL;
          // Send alerts to 2nd level approvers
          sendAlerts = true;
        }
        else if ( DENY.equalsIgnoreCase( approvalType ) )
        {
          issuanceStatus = SSIContestIssuanceStatusType.DENIED;
        }
      }
      else if ( APPROVAL_LEVEL_TWO == approvalLevel )
      {
        if ( APPROVE.equalsIgnoreCase( approvalType ) )
        {
          issuanceStatus = SSIContestIssuanceStatusType.APPROVED;
        }
        else if ( DENY.equalsIgnoreCase( approvalType ) )
        {
          issuanceStatus = SSIContestIssuanceStatusType.DENIED;
        }
      }
    }
    if ( !StringUtil.isNullOrEmpty( issuanceStatus ) )
    {
      setApprovalOrDenial( userId, contestAwardThemNow, issuanceStatus, denialReson, approvalLevel );
      sendLevel2ApprovalAlert( attachedSSIContest, sendAlerts, contestAwardThemNow.getApprovedByLevel1(), contestAwardThemNow );
    }
    return contestAwardThemNow;
  }

  /**
   * This will set the contest approver details or denial reason 
   * @param userId
   * @param contestAwardThemNow
   * @param approvalOrDenial
   * @param denialReason
   * @param levelOfApproval
   * @throws ServiceErrorException
   */
  private void setApprovalOrDenial( Long userId, SSIContestAwardThemNow contestAwardThemNow, String approvalOrDenial, String denialReason, Integer levelOfApproval ) throws ServiceErrorException
  {
    if ( APPROVAL_LEVEL_ONE == levelOfApproval && contestAwardThemNow.getApprovedByLevel1() != null && contestAwardThemNow.getDateApprovedLevel1() != null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR );
    }
    else if ( APPROVAL_LEVEL_TWO == levelOfApproval && contestAwardThemNow.getApprovedByLevel2() != null && contestAwardThemNow.getDateApprovedLevel2() != null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR );
    }
    else
    {
      contestAwardThemNow.setIssuanceStatusType( SSIContestIssuanceStatusType.lookup( approvalOrDenial ) );

      if ( APPROVAL_LEVEL_ONE == levelOfApproval )
      {
        contestAwardThemNow.setApprovedByLevel1( userId );
        contestAwardThemNow.setLevelApproved( LEVEL_ONE_APPROVED );
        contestAwardThemNow.setDateApprovedLevel1( DateUtils.getCurrentDateTrimmed() );
      }
      else if ( APPROVAL_LEVEL_TWO == levelOfApproval )
      {
        contestAwardThemNow.setApprovedByLevel2( userId );
        contestAwardThemNow.setLevelApproved( LEVEL_TWO_APPROVED );
        contestAwardThemNow.setDateApprovedLevel2( DateUtils.getCurrentDateTrimmed() );
      }
      if ( !StringUtil.isNullOrEmpty( denialReason ) && SSIContestIssuanceStatusType.DENIED.equals( approvalOrDenial ) )
      {
        contestAwardThemNow.setDenialReason( denialReason );
      }
    }
  }

  /**
   * Should not allow same approver for both the levels
   * @param userId
   * @param attachSSSIContest
   * @param contestAwardThemNow
   * @throws ServiceErrorException
   */
  private void validateIfLevel1ApproverIsApprovingLevel2( Long userId, SSIContestAwardThemNow contestAwardThemNow ) throws ServiceErrorException
  {
    if ( contestAwardThemNow.getApprovedByLevel1() != null && contestAwardThemNow.getApprovedByLevel1().equals( userId ) )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_APPPROVAL_DENY_ERROR );
    }
  }

  private void validateContestStatusForAppovalDecision( SSIContest attachSSSIContest, SSIContestAwardThemNow contestAwardThemNow ) throws ServiceErrorException
  {
    if ( contestAwardThemNow.getIssuanceStatusType().isApproved() && !attachSSSIContest.getStatus().isLive() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR );
    }
  }

  private Short getAwardIssuanceNumber( Long contestId, Short awardIssuanceNumber )
  {
    // If there is no awardIssuance number in the client state then get the max value from data base
    if ( awardIssuanceNumber == null || awardIssuanceNumber < 1 )
    {
      Short dbValue = ssiContestAwardThemNowDAO.getMaxAwardIssuanceNumber( contestId );
      // If the max value from database is null then default it to 1
      if ( dbValue == null )
      {
        awardIssuanceNumber = 1;
      }
      else
      {
        // If found then increment the value by 1
        awardIssuanceNumber = (short) ( dbValue + 1 );
      }
    }
    return awardIssuanceNumber;
  }

  @Override
  public void deleteContestParticipant( Long contestId, Long paxId, Short awardIssuanceNumber )
  {
    ssiContestAwardThemNowDAO.deleteContestParticipant( contestId, paxId, awardIssuanceNumber );
  }

  @Override
  public SSIContestAwardThemNow saveContest( SSIContest detachedContest,
                                             SSIContestContentValueBean valueBean,
                                             List<UpdateAssociationRequest> updateAssociationRequests,
                                             Long badgeRuleId,
                                             short awardIssuanceNumber )
      throws ServiceErrorException
  {
    SSIContest attachedContest = saveContest( detachedContest, valueBean, updateAssociationRequests, badgeRuleId );
    SSIContestAwardThemNow contestAtn = saveAwardThemNowContest( awardIssuanceNumber, attachedContest );
    return contestAtn;
  }

  public SSIContestAwardThemNow saveAwardThemNowContest( short awardIssuanceNumber, SSIContest attachedContest )
  {
    SSIContestAwardThemNow contestAtn = getContestAwardThemNowByIdAndIssunace( attachedContest.getId(), awardIssuanceNumber );
    if ( contestAtn == null )
    {
      contestAtn = new SSIContestAwardThemNow();
      contestAtn.setIssuanceStatusType( SSIContestIssuanceStatusType.lookup( SSIContestIssuanceStatusType.IN_PROGRESS ) );
      contestAtn.setContest( attachedContest );
      contestAtn.setIssuanceNumber( awardIssuanceNumber );
      attachedContest.setAwardIssuanceNumber( awardIssuanceNumber );
      contestAtn = ssiContestAwardThemNowDAO.saveContestAtn( contestAtn );
    }
    return contestAtn;
  }

  @Override
  public SSIContest saveContest( SSIContest detachedContest, SSIContestContentValueBean valueBean, List<UpdateAssociationRequest> updateAssociationRequests ) throws ServiceErrorException
  {
    SSIContest attachedContest = this.getContestById( detachedContest.getId() );

    // Create the CM asset for the contest
    createContestCMAsset( valueBean, detachedContest );

    // Merge the incoming changes to the attached
    attachedContest.setCmAssetCode( detachedContest.getCmAssetCode() );
    attachedContest.setEndDate( detachedContest.getEndDate() );
    updateContestAssociations( updateAssociationRequests, attachedContest );

    return attachedContest;
  }

  @Override
  public SSIContest saveContest( SSIContest detachedContest, SSIContestContentValueBean valueBean, List<UpdateAssociationRequest> updateAssociationRequests, Long badgeRuleId )
      throws ServiceErrorException
  {
    SSIContest attachedContest = null;

    // Create the CM asset for the contest
    createContestCMAsset( valueBean, detachedContest );

    if ( detachedContest.getId() != null && detachedContest.getId() > 0 )
    {
      // Look up the existing contest to update
      attachedContest = this.getContestById( detachedContest.getId() );

      // Merge the incoming changes to the attached
      attachedContest.setCmAssetCode( detachedContest.getCmAssetCode() );
      attachedContest.setStatus( detachedContest.getStatus() );
      attachedContest.setStartDate( detachedContest.getStartDate() );
      attachedContest.setEndDate( detachedContest.getEndDate() );
      attachedContest.setDisplayStartDate( detachedContest.getDisplayStartDate() );
      attachedContest.setBillPayoutCodeType( detachedContest.getBillPayoutCodeType() );

      attachedContest.setActivityMeasureType( detachedContest.getActivityMeasureType() );
      attachedContest.setActivityMeasureCurrencyCode( detachedContest.getActivityMeasureCurrencyCode() );
      attachedContest.setPayoutType( detachedContest.getPayoutType() );
      attachedContest.setPayoutOtherCurrencyCode( detachedContest.getPayoutOtherCurrencyCode() );

      BadgeRule badgeRule = null;
      if ( badgeRuleId != null && badgeRuleId > 0 )
      {
        badgeRule = getBadgeRule( badgeRuleId );
      }
      attachedContest.setBadgeRule( badgeRule );

      updateContestAssociations( updateAssociationRequests, attachedContest );
    }
    else
    {
      BadgeRule badgeRule = null;
      if ( badgeRuleId != null && badgeRuleId > 0 )
      {
        badgeRule = getBadgeRule( badgeRuleId );
      }
      detachedContest.setBadgeRule( badgeRule );
      attachedContest = ssiContestAwardThemNowDAO.saveContest( detachedContest );
    }
    return attachedContest;
  }

  @Override
  public void deleteContestIssuance( Long contestId, Short awardIssuanceNumber ) throws ServiceErrorException
  {
    ssiContestAwardThemNowDAO.updateAwardThemNowContestStatus( contestId, awardIssuanceNumber, SSIContestIssuanceStatusType.CANCELLED );
  }

  /****
   * 
   * savePayoutAwardThemNow method will get the attached ATN objected , will merge the detach ATN object and update the participants information
   * {@inheritDoc}
   */
  @Override
  public void savePayoutAwardThemNow( SSIContestAwardThemNow detachedATNContest, List<SSIContestParticipantValueBean> detachedParticipants )
  {
    updateAwardThemNowMessage( detachedATNContest );
    updateContestParticipant( detachedParticipants );
  }

  private void updateAwardThemNowMessage( SSIContestAwardThemNow detachedATNContest )
  {
    ssiContestAwardThemNowDAO.updateAwadThemNowMessage( detachedATNContest.getNotificationMessageText(), detachedATNContest.getContest().getId(), detachedATNContest.getIssuanceNumber() );
  }

  @Override
  public SSIContestAwardThemNow getContestAwardThemNowByIdAndIssunace( Long contestId, short issuanceNumber )
  {
    return ssiContestAwardThemNowDAO.getContestAwardThemNowByIdAndIssunace( contestId, issuanceNumber );
  }

  @Override
  public SSIContestAwardHistoryTotalsValueBean getContestAwardTotals( Long contestId )
  {
    return ssiContestAwardThemNowDAO.getContestAwardTotals( contestId );
  }

  @Override
  public Map<String, Object> getAllIssuancesForContest( Long contestId, int pageNumber, int recordsPerpage, String sortedOn, String sortedBy ) throws ServiceErrorException
  {
    return ssiContestAwardThemNowDAO.getAllIssuancesForContest( contestId, pageNumber, recordsPerpage, sortedOn, sortedBy );
  }

  public void updateAwardThemNowContestStatus( Long contestId, short issuanceNumber ) throws ServiceErrorException
  {
    AssociationRequestCollection ssiContestAssociationRequest = new AssociationRequestCollection();
    ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_LEVEL1_APPROVERS ) );
    SSIContest attachSSIContest = this.ssiContestDAO.getContestByIdWithAssociations( contestId, ssiContestAssociationRequest );
    attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );

    if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL
        || attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL )
    {
      ssiContestAwardThemNowDAO.updateAwardThemNowContestStatus( contestId, issuanceNumber, SSIContestIssuanceStatusType.WAITING_FOR_APPROVAL );
      sendLevel1ApprovalAlert( attachSSIContest );
    }
    else
    {
      ssiContestAwardThemNowDAO.updateAwardThemNowContestStatus( contestId, issuanceNumber, SSIContestIssuanceStatusType.APPROVED );
      if ( approveContestPayouts( contestId, issuanceNumber, null, null ) )
      {
        launchContestPayoutsDepositProcess( contestId, issuanceNumber );
      }
    }
  }

  /**
   * Save the current page and load the participants for the next page
   * {@inheritDoc}
   * @throws ServiceErrorException 
   */
  @Override
  public List<SSIContestParticipant> saveParticipantsAndFetchNextPageResults( SSIContest detachedContest,
                                                                              short awardIssuanceNumber,
                                                                              List<SSIContestParticipantValueBean> detachedParticipants,
                                                                              String sortedBy,
                                                                              String sortedOn,
                                                                              int pageNumber )
      throws ServiceErrorException
  {
    updateContestParticipant( detachedParticipants, awardIssuanceNumber );
    return getContestParticipants( detachedContest.getId(), awardIssuanceNumber, pageNumber, sortedOn, sortedBy, null );
  }

  @Override
  public void updateContestParticipant( List<SSIContestParticipantValueBean> detachedParticipants, short issuanceNumber )
  {
    List<Long> paxIds = lookupDetachedParticipants( detachedParticipants );

    if ( paxIds.size() > 0 )
    {
      List<SSIContestParticipant> attachedContestParticipants = getAttachedParticipants( paxIds, issuanceNumber );
      mergeContestParticipants( attachedContestParticipants, detachedParticipants );
    }
  }

  public List<SSIContestParticipant> getAttachedParticipants( List<Long> paxIds, short issuanceNumber )
  {
    return ssiContestAwardThemNowDAO.getContestParticipants( paxIds, issuanceNumber );
  }

  @Override
  public void updateSameValueForAllPax( Long contestId, short issuanceNumber, String key, SSIContestParticipantValueBean participant )
  {
    ssiContestAwardThemNowDAO.updateSameValueForAllPax( contestId, issuanceNumber, key, participant );
  }

  /**
   * Will save the current page results and call the stored procedure to validate if all the participant information is filled or not.
   * {@inheritDoc}
   */
  @Override
  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId, Short issuanceNumber ) throws ServiceErrorException
  {
    return this.ssiContestAwardThemNowDAO.calculatePayoutObjectivesTotals( contestId, issuanceNumber );
  }

}
