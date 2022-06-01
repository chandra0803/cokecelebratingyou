
package com.biperf.core.service.ssi.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestPaxClaimDAO;
import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.SSIContestClaimApproveProcess;
import com.biperf.core.process.SSIContestClaimsUpdateProcess;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.managertoolkit.ParticipantAlertService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.SSIFileUpload;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestPaginationValueBean;
import com.biperf.core.value.ssi.SSIContestPaxClaimCountValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPaxClaimServiceImpl.
 * 
 * @author kancherl
 * @since May 20, 2015
 * @version 1.0
 */

public class SSIContestPaxClaimServiceImpl implements SSIContestPaxClaimService
{
  protected static final Log log = LogFactory.getLog( SSIContestPaxClaimServiceImpl.class );

  protected SSIContestPaxClaimDAO ssiContestPaxClaimDAO;
  protected CMAssetService cmAssetService;
  private ParticipantDAO participantDAO;
  protected SSIContestDAO ssiContestDAO;
  protected MailingService mailingService;
  protected ParticipantAlertService participantAlertService;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private ProcessService processService;

  @Override
  public SSIContestPaxClaim getPaxClaimById( Long paxClaimId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimById( paxClaimId );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public SSIContestPaxClaim getPaxClaimByIdWithAssociations( Long paxClaimId, AssociationRequestCollection associationRequestCollection )
  {
    SSIContestPaxClaim paxClaim = ssiContestPaxClaimDAO.getPaxClaimById( paxClaimId );
    for ( Iterator iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
    {
      AssociationRequest req = (AssociationRequest)iterator.next();
      req.execute( paxClaim );
    }
    return paxClaim;
  }

  @Override
  public SSIFileUpload uploadClaimDocument( SSIFileUpload data ) throws ServiceErrorException
  {
    // upload file
    data.setFull( ImageUtils.getSSIContestClaimsPath( data.getType(), data.getId(), data.getName() ) );
    appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );

    // move to web dev
    byte[] media = appDataDirFileUploadStrategy.getFileData( data.getFull() );
    webdavFileUploadStrategy.uploadFileData( data.getFull(), media );
    appDataDirFileUploadStrategy.delete( data.getFull() );
    return data;
  }

  @Override
  public SSIContestPaxClaim savePaxClaim( SSIContestPaxClaim paxClaim ) throws ServiceErrorException
  {
    try
    {
      paxClaim.setStatus( SSIClaimStatus.lookup( SSIClaimStatus.WAITING_FOR_APPROVAL ) );
      if ( paxClaim.getId() == null || StringUtils.isEmpty( paxClaim.getClaimNumber() ) )
      {
        paxClaim.setClaimNumber( generateClaimNumber() );
      }
      ssiContestPaxClaimDAO.savePaxClaim( paxClaim );

      // notify contest claim approver
      SSIContest contest = this.ssiContestDAO.getContestById( paxClaim.getContestId() );
      Participant approver = this.participantDAO.getParticipantById( contest.getContestOwnerId() );

      createClaimApprovalAlert( contest, approver );
      Mailing mailing = this.mailingService.buildContestClaimApprovalNotification( contest, approver );
      if ( mailing != null )
      {
        mailingService.submitMailing( mailing, null );
      }
    }
    catch( SQLException sqlException )
    {
      log.error( "SQL Exception while Saving PAX claim for Pax: " + paxClaim.getSubmitterId() + sqlException.getMessage() );
      throw new ServiceErrorException( "ssi_contest.claims.SUBMIT_CLAIM_ERR" );
    }
    return paxClaim;
  }

  private void createClaimApprovalAlert( SSIContest contest, Participant approver )
  {
    // check for existing alert, create if not available
    ParticipantAlert participantAlert = this.participantAlertService.getAlertByPaxIdContestIdAndType( approver.getId(), contest.getId(), SSIContest.CONTEST_ROLE_CLAIM_APPROVER );
    Date expiryDate = DateUtils.getDateAfterNumberOfDays( contest.getClaimSubmissionLastDate(), contest.getPromotion().getDaysToApproveClaim() );
    if ( participantAlert != null )
    {
      Date today = new Date();
      Date existingExpiryDate = participantAlert.getAlertMessage().getExpiryDate();
      // check for expire date of the alert and update if expires
      if ( existingExpiryDate.before( today ) || org.apache.commons.lang3.time.DateUtils.isSameDay( existingExpiryDate, today ) )
      {
        participantAlert.getAlertMessage().setExpiryDate( expiryDate );
        this.participantAlertService.saveParticipantAlert( participantAlert );
      }
    }
    else
    {
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setSubject( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_SUBJECT" ) );
      alertMessage.setExpiryDate( expiryDate );
      alertMessage.setMessage( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_MESG" ) );
      alertMessage.setContestId( contest.getId() );
      alertMessage.setSsiAlertType( SSIContest.CONTEST_ROLE_CLAIM_APPROVER );
      alertMessage.setMessageTo( String.valueOf( approver.getId() ) );
      participantAlert = new ParticipantAlert();
      participantAlert.setUser( approver );
      participantAlert.setAlertMessage( alertMessage );
      this.participantAlertService.saveParticipantAlert( participantAlert );
    }
  }

  @Override
  public void approvePaxClaim( Long paxClaimId, Long approverId ) throws ServiceErrorException
  {
    try
    {
      // approve claim
      ssiContestPaxClaimDAO.approvePaxClaim( SSIClaimStatus.APPROVED, approverId, paxClaimId );
    }
    catch( SQLException sqlException )
    {
      log.error( "SQL exception while Approving the PAX claim " + paxClaimId + sqlException );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_CLAIM_APPROVAL_ERR );
    }
  }

  @Override
  public Date approveAllPaxClaims( Long contestId, Long approverId ) throws ServiceErrorException
  {
    Date approveDenyDate = null;
    try
    {
      // approve all claims
      approveDenyDate = ssiContestPaxClaimDAO.approveAllPaxClaims( contestId, approverId );
    }
    catch( SQLException sqlException )
    {
      log.error( "SQL exception while Approving all PAX claims " + contestId + sqlException );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_CLAIM_APPROVAL_ERR );
    }
    return approveDenyDate;
  }

  @Override
  public void approveAllPaxClaimsAndNotify( Long contestId, Long approverId ) throws ServiceErrorException
  {
    try
    {
      // approve all claims & notify all submitters
      launchContestClaimApproveProcess( contestId, approverId );

      // expire claim approval alert
      expireClaimApprovalAlert( approverId, contestId );
    }
    catch( ServiceErrorException see )
    {
      log.error( "Exception while Approving all the PAX claims " + contestId + see );
      throw see;
    }
  }

  private void launchContestClaimApproveProcess( Long contestId, Long approverId ) throws ServiceErrorException
  {
    LinkedHashMap<String, String[]> parameterValueMap = getParameterValueMap( contestId, approverId );
    Process process = processService.createOrLoadSystemProcess( SSIContestClaimApproveProcess.BEAN_NAME, SSIContestClaimApproveProcess.BEAN_NAME );
    processService.launchProcess( process, parameterValueMap, approverId );
  }

  @Override
  public void denyPaxClaim( Long paxClaimId, Long approverId, String deniedReason ) throws ServiceErrorException
  {
    try
    {
      // deny claim
      ssiContestPaxClaimDAO.denyPaxClaim( SSIClaimStatus.DENIED, deniedReason, approverId, paxClaimId );
    }
    catch( SQLException sqlException )
    {
      log.error( "SQL exception while Denying the PAX claim " + paxClaimId + sqlException );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_CLAIM_APPROVAL_ERR );
    }
  }

  public void notifyClaimSubmitterAndExpireClaimApprovalAlert( Long paxClaimId, Long approverId )
  {
    String approverName = this.participantDAO.getLNameFNameByPaxId( approverId );
    SSIContestPaxClaim paxClaim = this.getPaxClaimById( paxClaimId );
    AssociationRequestCollection contestAssociationRequestCollection = new AssociationRequestCollection();
    contestAssociationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
    SSIContest contest = this.ssiContestDAO.getContestByIdWithAssociations( paxClaim.getContestId(), contestAssociationRequestCollection );
    Participant submitter = this.participantDAO.getParticipantById( paxClaim.getSubmitterId() );

    SSIContestParticipant contestParticipant = null;
    if ( contest.getContestType().isObjectives() && !contest.getSameObjectiveDescription() )
    {
      contestParticipant = this.ssiContestDAO.getContestParticipantByContestIdAndPaxId( contest.getId(), submitter.getId() );
    }
    Mailing mailing = this.mailingService
        .buildContestClaimApprovalUpdateStatusNotification( paxClaim, approverName, contest, submitter, SSIContestUtil.getActivityDescription( paxClaim, contestParticipant, contest ) );
    if ( mailing != null )
    {
      this.mailingService.submitMailing( mailing, null );
    }
    // update approver alert if no more pending claims
    SSIContestPaxClaimCountValueBean valueBean = ssiContestPaxClaimDAO.getPaxClaimsCountByApproverId( paxClaim.getContestId() );
    if ( valueBean.getClaimsWaitingForApprovalCount() == 0 )
    {
      expireClaimApprovalAlert( approverId, paxClaim.getContestId() );
    }
  }

  private void expireClaimApprovalAlert( Long approverId, Long contestId )
  {
    ParticipantAlert participantAlert = this.participantAlertService.getAlertByPaxIdContestIdAndType( approverId, contestId, SSIContest.CONTEST_ROLE_CLAIM_APPROVER );
    if ( participantAlert != null )
    {
      participantAlert.getAlertMessage().setExpiryDate( new Date() );
      this.participantAlertService.saveParticipantAlert( participantAlert );
    }
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByContestId( Long contestId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsByContestId( contestId );
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsBySubmitterId( Long contestId, Long submitterId, SSIContestPaginationValueBean paginationParams )
  {
    List<SSIContestPaxClaim> paxClaims = ssiContestPaxClaimDAO.getPaxClaimsBySubmitterId( contestId, submitterId, paginationParams );
    if ( paxClaims != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new SSIContestPaxClaimAssociationRequest( SSIContestPaxClaimAssociationRequest.CLAIM_FIELDS ) );
      for ( SSIContestPaxClaim paxClaim : paxClaims )
      {
        for ( Iterator iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = (AssociationRequest)iterator.next();
          req.execute( paxClaim );
        }
      }
    }
    return paxClaims;
  }

  @Override
  public int getPaxClaimsCountBySubmitterId( Long contestId, Long submitterId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsCountBySubmitterId( contestId, submitterId );
  }

  @Override
  public Double getClaimsActivityAmount( Long contestId, Long submitterId )
  {
    return ssiContestPaxClaimDAO.getClaimsActivityAmount( contestId, submitterId );
  }

  @Override
  public Map<String, Object> getPaxClaimsByContestIdAndStatus( Long contestId, String claimStatus, int pageNumber, int recordsPerPage, String sortedOn, String SortedBy ) throws ServiceErrorException
  {

    String claimStatusList = null;
    if ( claimStatus == null || claimStatus.equalsIgnoreCase( "all" ) )
    {
      claimStatusList = SSIClaimStatus.APPROVED + "," + SSIClaimStatus.WAITING_FOR_APPROVAL + "," + SSIClaimStatus.DENIED;
    }
    else
    {
      claimStatusList = claimStatus;

    }
    return ssiContestPaxClaimDAO.getPaxClaimsByContestIdAndStatus( contestId, claimStatusList, pageNumber, recordsPerPage, sortedOn, SortedBy );
  }

  @Override
  public SSIContestPaxClaimCountValueBean getPaxClaimsCountByApproverId( Long contestId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsCountByApproverId( contestId );
  }

  @Override
  public SSIContestPaxClaim getPaxClaimByClaimNumber( String claimNumber )
  {
    return ssiContestPaxClaimDAO.getPaxClaimByClaimNumber( claimNumber );
  }

  @Override
  public SSIContestPaxClaim getPaxClaimByClaimNumberWithAssociations( String claimNumber, AssociationRequestCollection associationRequestCollection )
  {
    SSIContestPaxClaim paxClaim = ssiContestPaxClaimDAO.getPaxClaimByClaimNumber( claimNumber );
    for ( Iterator iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
    {
      AssociationRequest req = (AssociationRequest)iterator.next();
      req.execute( paxClaim );
    }
    return paxClaim;
  }

  @Override
  public boolean isClaimNumberExists( String claimNumber )
  {
    SSIContestPaxClaim paxClaim = ssiContestPaxClaimDAO.getPaxClaimByClaimNumber( claimNumber );
    if ( paxClaim != null )
    {
      return true;
    }
    return false;
  }

  @Override
  public void updateAllPaxClaimsAndStackRank( Long contestId, Long approverId ) throws ServiceErrorException
  {
    // update the PAX claim totals
    launchContestClaimsUpdateProcess( contestId, approverId );
  }

  private void launchContestClaimsUpdateProcess( Long contestId, Long approverId )
  {
    LinkedHashMap<String, String[]> parameterValueMap = getParameterValueMap( contestId, approverId );
    Process process = processService.createOrLoadSystemProcess( SSIContestClaimsUpdateProcess.BEAN_NAME, SSIContestClaimsUpdateProcess.BEAN_NAME );
    processService.launchProcess( process, parameterValueMap, approverId );

  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndApproveDenyDate( Long contestId, Date approveDenyDate )
  {
    List<SSIContestPaxClaim> paxClaims = ssiContestPaxClaimDAO.getPaxClaimsByContestIdAndApproveDenyDate( contestId, approveDenyDate );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestPaxClaimAssociationRequest( SSIContestPaxClaimAssociationRequest.CLAIM_FIELDS ) );
    for ( SSIContestPaxClaim paxClaim : paxClaims )
    {
      for ( Iterator iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = (AssociationRequest)iterator.next();
        req.execute( paxClaim );
      }
    }
    return paxClaims;
  }

  private LinkedHashMap<String, String[]> getParameterValueMap( Long contestId, Long approverId )
  {
    LinkedHashMap<String, String[]> parameterValueMap = new LinkedHashMap<String, String[]>();
    parameterValueMap.put( "contestId", new String[] { Long.toString( contestId ) } );
    parameterValueMap.put( "userId", new String[] { Long.toString( approverId ) } );
    return parameterValueMap;
  }

  public String generateClaimNumber()
  {
    String claimNumber = RandomStringUtils.randomNumeric( 16 );
    if ( isClaimNumberExists( claimNumber ) )
    {
      log.info( "Claim Number generated exists in the database. Recreating the Claim Number : " + claimNumber );
      return this.generateClaimNumber();
    }
    else
    {
      return claimNumber;
    }
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndStatus( Long contestId, List<SSIClaimStatus> claimStatuses )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsByContestIdAndStatus( contestId, claimStatuses );
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByApproverId( Long approverId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsByApproverId( approverId );
  }

  @Override
  public List<SSIContestListValueBean> getPaxClaimsWaitingForApprovalByApproverId( Long approverId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsWaitingForApprovalByApproverId( approverId );
  }

  @Override
  public List<SSIContestListValueBean> getPaxClaimsViewAllByApproverId( Long approverId )
  {
    return ssiContestPaxClaimDAO.getPaxClaimsViewAllByApproverId( approverId );
  }

  @Override
  public int updateContestClaims( Long contestId ) throws ServiceErrorException
  {
    try
    {
      return this.ssiContestPaxClaimDAO.updateContestClaims( contestId );
    }
    catch( SQLException e )
    {
      log.error( "SQL Exception while updating contest claims: " + e );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UPD_STACKRANK_ERR );
    }
  }

  @Override
  public Set<Long> getPaxClaimsForApprovalByContestId( List<ParticipantAlert> alertMessageList )
  {
    List<Long> paxContestIds = new ArrayList<Long>();
    List<SSIContestPaxClaim> waitingForApprovalClaims = null;
    alertMessageList.forEach( mockParticipantAlert -> paxContestIds.add( mockParticipantAlert.getAlertMessage().getContestId() ) );
    if ( null != paxContestIds && !paxContestIds.isEmpty() )
    {
      waitingForApprovalClaims = ssiContestPaxClaimDAO.getPaxClaimsForApprovalByContestId( paxContestIds );
    }
    Set<Long> waitingForApprovercontestIds = new HashSet<Long>();
    if ( waitingForApprovalClaims != null )
    {
      waitingForApprovalClaims.forEach( e -> waitingForApprovercontestIds.add( e.getContestId() ) );
    }
    return waitingForApprovercontestIds;
  }

  public void setSsiContestPaxClaimDAO( SSIContestPaxClaimDAO ssiContestPaxClaimDAO )
  {
    this.ssiContestPaxClaimDAO = ssiContestPaxClaimDAO;
  }

  public CMAssetService getCmAssetService()
  {
    return cmAssetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public ParticipantDAO getParticipantDAO()
  {
    return participantDAO;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  public void setSsiContestDAO( SSIContestDAO ssiContestDAO )
  {
    this.ssiContestDAO = ssiContestDAO;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setParticipantAlertService( ParticipantAlertService participantAlertService )
  {
    this.participantAlertService = participantAlertService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
