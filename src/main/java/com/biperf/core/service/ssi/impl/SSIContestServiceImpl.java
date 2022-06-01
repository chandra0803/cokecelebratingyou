
package com.biperf.core.service.ssi.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxPayout;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.SSIContestPayoutDepositProcess;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.managertoolkit.AlertMessageService;
import com.biperf.core.service.managertoolkit.ParticipantAlertService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestBillCodesUpdateAssociation;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIContestStackRankPayoutsUpdateAssociation;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.service.util.ServiceUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestBillCodeBean;
import com.biperf.core.value.ssi.SSIContestContentValueBean;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestDocumentValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestRankValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestSummaryValueBean;
import com.biperf.core.value.ssi.SSIContestTranslationsCountValueBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Contest Service handles Contest maintenance(contest life cycle) related services 
 * @author dudam
 * @since Nov 5, 2014
 * @version 1.0
 */
public class SSIContestServiceImpl implements SSIContestService
{
  protected static final Log log = LogFactory.getLog( SSIContestServiceImpl.class );

  private static final String CREATOR_ROLE = "creator";
  private static final String MANAGER_ROLE = "manager";
  private static final String APPROVER_ROLE = "approver"; // This might not be an actual role, but
                                                          // it makes the creator list page display
                                                          // how we want

  protected static final String APPROVE = "approve";
  protected static final String DENY = "deny";
  protected static final Integer APPROVAL_LEVEL_ONE = 0;
  protected static final Integer APPROVAL_LEVEL_TWO = 1;
  protected static final Integer LEVEL_ONE_APPROVED = 1;
  protected static final Integer LEVEL_TWO_APPROVED = 2;
  protected static final Integer CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL = 1;
  protected static final Integer CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL = 2;
  private static final long BADGE_RULE_NOT_AVAILABLE = 0L;

  protected SSIContestDAO ssiContestDAO;
  protected CMAssetService cmAssetService;
  protected GamificationDAO gamificationDAO;
  protected SSIContestParticipantDAO ssiContestParticipantDAO;
  protected AlertMessageService alertMessageService;
  protected ParticipantAlertService participantAlertService;
  private MailingService mailingService;
  private ParticipantDAO participantDAO;
  private SystemVariableService systemVariableService;
  private UserService userService;

  @Override
  public SSIContest getContestById( Long contestId )
  {
    return this.ssiContestDAO.getContestById( contestId );
  }

  @Override
  public SSIContestParticipant getContestParticipantByContestIdAndPaxId( Long contestId, Long participantId )
  {
    return this.ssiContestDAO.getContestParticipantByContestIdAndPaxId( contestId, participantId );
  }

  @Override
  public SSIContest saveContest( SSIContest detachedContest, List<UpdateAssociationRequest> updateAssociationRequests ) throws ServiceErrorException
  {
    SSIContest attachedContest = null;

    if ( detachedContest.getId() != null && detachedContest.getId() > 0 )
    {
      // Look up the existing contest to update
      attachedContest = this.getContestById( detachedContest.getId() );

      attachedContest.setClaimSubmissionLastDate( detachedContest.getClaimSubmissionLastDate() );
      attachedContest.setClaimApprovalNeeded( detachedContest.getClaimApprovalNeeded() );
      attachedContest.setDataCollectionType( detachedContest.getDataCollectionType() );
      updateContestAssociations( updateAssociationRequests, attachedContest );
    }
    else
    {
      attachedContest = this.ssiContestDAO.saveContest( detachedContest );
    }
    return attachedContest;
  }

  @Override
  public SSIContest saveContest( SSIContest detachedContest, SSIContestContentValueBean valueBean, List<UpdateAssociationRequest> updateAssociationRequests ) throws ServiceErrorException
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
      // attachedContest.setStatus( detachedContest.getStatus() );
      attachedContest.setStartDate( detachedContest.getStartDate() );
      attachedContest.setEndDate( detachedContest.getEndDate() );
      attachedContest.setDisplayStartDate( detachedContest.getDisplayStartDate() );
      attachedContest.setIncludePersonalMessage( detachedContest.getIncludePersonalMessage() );
      updateContestAssociations( updateAssociationRequests, attachedContest );
    }
    else
    {
      attachedContest = this.ssiContestDAO.saveContest( detachedContest );
    }
    return attachedContest;
  }

  public int getRequireContentApprovalProcess( Long ssiContestId )
  {
    return ssiContestDAO.getRequireContentApproval( ssiContestId );
  }

  public void sendContestUpdateNotificationToApprovers( Long contestId )
  {
    SSIContest contest = ssiContestDAO.getContestById( contestId );
    if ( isEligibleForUpdateNotificationToApprovers( contest ) )
    {
      String creatorName = participantDAO.getLNameFNameByPaxId( contest.getCreatorId() );

      Participant level1Approver = participantDAO.getParticipantById( contest.getApprovedByLevel1() );
      Mailing level1Mailing = mailingService.buildSSIContestUpdatedAfterApprovalNotification( contest, level1Approver, creatorName, contest.getDateApprovedLevel1() );
      if ( level1Mailing != null )
      {
        mailingService.submitMailing( level1Mailing, null );
      }

      if ( contest.getApprovedByLevel2() != null && contest.getDateApprovedLevel2() != null )
      {
        Participant level2Approver = participantDAO.getParticipantById( contest.getApprovedByLevel2() );
        Mailing level2Mailing = mailingService.buildSSIContestUpdatedAfterApprovalNotification( contest, level2Approver, creatorName, contest.getDateApprovedLevel2() );
        if ( level2Mailing != null )
        {
          mailingService.submitMailing( level2Mailing, null );
        }
      }
    }
  }

  private boolean isEligibleForUpdateNotificationToApprovers( SSIContest contest )
  {
    return ( contest.getStatus().isLive() || contest.getStatus().isPending() ) && contest.getPromotion().getRequireContestApproval() && contest.getApprovedByLevel1() != null
        && contest.getDateApprovedLevel1() != null;
  }

  protected void updateContestAssociations( List<UpdateAssociationRequest> updateAssociationRequests, SSIContest attachedContest )
  {
    if ( updateAssociationRequests != null && updateAssociationRequests.size() > 0 )
    {
      for ( int i = 0; i < updateAssociationRequests.size(); i++ )
      {
        UpdateAssociationRequest request = (UpdateAssociationRequest)updateAssociationRequests.get( i );
        request.execute( attachedContest );
      }
    }
  }

  public void updateContestParticipant( List<SSIContestParticipantValueBean> detachedParticipants )
  {
    List<Long> paxIds = lookupDetachedParticipants( detachedParticipants );

    if ( paxIds.size() > 0 )
    {
      List<SSIContestParticipant> attachedContestParticipants = getAttachedParticipants( paxIds );
      mergeContestParticipants( attachedContestParticipants, detachedParticipants );
    }
  }

  protected void createContestCMAsset( SSIContestContentValueBean valueBean, SSIContest detachedContest ) throws ServiceErrorException
  {
    String locale = null;
    String nameValue = "";
    String descriptionValue = "";
    String messageValue = "";
    String documentNameValue = "";
    String documentOriginalNameValue = "";
    String documentUrlValue = "";
    for ( SSIContestNameValueBean name : valueBean.getNames() )
    {
      locale = name.getLanguage();
      nameValue = name.getText();
    }
    for ( SSIContestDescriptionValueBean description : valueBean.getDescriptions() )
    {
      locale = description.getLanguage();
      descriptionValue = description.getText();
    }
    for ( SSIContestMessageValueBean message : valueBean.getMessages() )
    {
      locale = message.getLanguage();
      messageValue = message.getText();
    }
    for ( SSIContestDocumentValueBean document : valueBean.getDocuments() )
    {
      locale = document.getLanguage();
      documentNameValue = document.getName();
      documentOriginalNameValue = document.getOriginalFilename();
      documentUrlValue = document.getUrl();
    }
    if ( !StringUtil.isNullOrEmpty( locale ) )
    {
      if ( StringUtil.isNullOrEmpty( detachedContest.getCmAssetCode() ) )
      {
        // Create and set asset to contest
        String assetName = cmAssetService.getUniqueAssetCode( SSIContest.CONTEST_CMASSET_PREFIX );
        detachedContest.setCmAssetCode( assetName );
      }
      CMDataElement cmDataName = new CMDataElement( SSIContest.CONTEST_CMASSET_CONTEST, SSIContest.CONTEST_CMASSET_NAME, nameValue, false, DataTypeEnum.STRING );
      CMDataElement cmDataDescription = new CMDataElement( SSIContest.CONTEST_CMASSET_CONTEST, SSIContest.CONTEST_CMASSET_DESCRIPTION, descriptionValue, false, DataTypeEnum.HTML );
      CMDataElement cmDataMessage = new CMDataElement( SSIContest.CONTEST_CMASSET_CONTEST, SSIContest.CONTEST_CMASSET_MESSAGE, messageValue, false, DataTypeEnum.HTML );
      CMDataElement cmDataDocumentName = new CMDataElement( SSIContest.CONTEST_CMASSET_CONTEST, SSIContest.CONTEST_CMASSET_DOCUMENT_DISPLAY_NAME, documentNameValue, false, DataTypeEnum.HTML );
      CMDataElement cmDataDocumentOriginalName = new CMDataElement( SSIContest.CONTEST_CMASSET_CONTEST,
                                                                    SSIContest.CONTEST_CMASSET_DOCUMENT_ORIGINAL_NAME,
                                                                    documentOriginalNameValue,
                                                                    false,
                                                                    DataTypeEnum.HTML );
      CMDataElement cmDataDocumentUrl = new CMDataElement( SSIContest.CONTEST_CMASSET_CONTEST, SSIContest.CONTEST_CMASSET_DOCUMENT_URL, documentUrlValue, false, DataTypeEnum.STRING );

      List<CMDataElement> elements = new ArrayList<CMDataElement>();
      elements.add( cmDataName );
      elements.add( cmDataDescription );
      elements.add( cmDataMessage );
      elements.add( cmDataDocumentName );
      elements.add( cmDataDocumentOriginalName );
      elements.add( cmDataDocumentUrl );

      cmAssetService.createOrUpdateAsset( SSIContest.CONTEST_SECTION_CODE,
                                          SSIContest.CONTEST_CMASSET_TYPE_NAME,
                                          SSIContest.CONTEST_CMASSET_CONTEST,
                                          detachedContest.getCmAssetCode(),
                                          elements,
                                          CmsUtil.getLocale( locale ),
                                          null );
    }
  }

  @Override
  public SSIContest getContestByIdWithAssociations( Long contestId, AssociationRequestCollection associationRequestCollection )
  {
    return this.ssiContestDAO.getContestByIdWithAssociations( contestId, associationRequestCollection );
  }

  @Override
  public List<SSIContestApprover> getContestApproversByIdAndApproverType( Long contestId, SSIApproverType ssiApproverType )
  {
    return this.ssiContestDAO.getContestApproversByIdAndApproverType( contestId, ssiApproverType );
  }

  @Override
  public List<SSIContestParticipant> getAllContestParticipantsByContestId( Long contestId )
  {
    return this.ssiContestDAO.getAllContestParticipantsByContestId( contestId );
  }

  @Override
  public List<SSIContestTranslationsCountValueBean> getContestTranslationsCount( String assetCode )
  {
    return this.ssiContestDAO.getContestTranslationsCount( assetCode );
  }

  @Override
  public List<SSIContestManager> getAllContestManagersByContestId( Long contestId )
  {
    return this.ssiContestDAO.getAllContestManagersByContestId( contestId );
  }

  public boolean isContestNameUnique( String contestName, Long currentContestId, String locale )
  {
    return ssiContestDAO.isContestNameUnique( contestName, currentContestId, locale );
  }

  @Override
  public void saveContestParticipants( Long contestId, Long[] participantIds )
  {
    List<Long> existingParticipantIds = ssiContestDAO.getExistingContestParticipantIds( participantIds, contestId );
    for ( Long existingParticipantId : existingParticipantIds )
    {
      participantIds = (Long[])ArrayUtils.removeElement( participantIds, existingParticipantId.longValue() );
    }
    if ( participantIds.length > 0 )
    {
      this.ssiContestDAO.saveContestParticipants( contestId, participantIds );
    }
  }

  @Override
  public void deleteContestParticipant( Long contestId, Long participantId )
  {
    this.ssiContestDAO.deleteContestParticipant( contestId, participantId );
  }

  @Override
  public void saveContestManagers( Long contestId, Long[] managerIds ) throws ServiceErrorException
  {
    try
    {
      List<Long> existingmanagerIds = ssiContestDAO.getExistingContestManagerIds( managerIds, contestId );
      for ( Long existingManagerId : existingmanagerIds )
      {
        managerIds = (Long[])ArrayUtils.removeElement( managerIds, existingManagerId.longValue() );
      }
      if ( managerIds.length > 0 )
      {
        this.ssiContestDAO.saveContestManagers( contestId, managerIds );
      }
    }
    catch( Exception e )
    {
      log.error( "Error while adding managers: " + e );
      throw new ServiceErrorException( "ssi_contest.pax.manager.ADD_MANAGER_ERROR" );
    }
  }

  @Override
  public void saveContestSuperViewers( Long contestId, Long[] superViewerIds ) throws ServiceErrorException
  {
    try
    {
      List<Long> existingSuperViewerIds = ssiContestDAO.getExistingContestSuperViewerIds( superViewerIds, contestId );
      for ( Long existingSuperViewerId : existingSuperViewerIds )
      {
        superViewerIds = (Long[])ArrayUtils.removeElement( superViewerIds, existingSuperViewerId.longValue() );
      }
      if ( superViewerIds.length > 0 )
      {
        this.ssiContestDAO.saveContestSuperViewers( contestId, superViewerIds );
      }
    }
    catch( Exception e )
    {
      log.error( "Error while adding super viewers: " + e );
      throw new ServiceErrorException( "ssi_contest.pax.manager.ADD_SUPERVIEWER_ERROR" );
    }
  }

  @Override
  public void deleteContestManager( Long contestId, Long managerId )
  {
    this.ssiContestDAO.deleteContestManager( contestId, managerId );
  }

  @Override
  public void deleteContestSuperViewer( Long contestId, Long superViewerId )
  {
    this.ssiContestDAO.deleteContestSuperViewer( contestId, superViewerId );
  }

  @Override
  public Map<String, Set<Participant>> getSelectedContestApprovers( Long contestId )
  {
    Set<Participant> level1Approvers = new HashSet<Participant>();
    Set<Participant> level2Approvers = new HashSet<Participant>();
    if ( contestId != null && contestId > 0 )
    {
      List<SSIContestApprover> selectedContestApprovers = this.getContestApproversByIdAndApproverType( contestId, null );
      if ( selectedContestApprovers != null )
      {
        for ( Iterator iter = selectedContestApprovers.iterator(); iter.hasNext(); )
        {
          SSIContestApprover contestApprover = (SSIContestApprover)iter.next();
          if ( contestApprover != null && contestApprover.getApproverType() != null && contestApprover.getApproverType().equals( SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL1_APPROVER ) ) )
          {
            Participant approver = contestApprover.getApprover();
            level1Approvers.add( approver );
          }
          else if ( contestApprover != null && contestApprover.getApproverType() != null
              && contestApprover.getApproverType().equals( SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL2_APPROVER ) ) )
          {
            Participant approver = contestApprover.getApprover();
            level2Approvers.add( approver );
          }
        }
      }
    }
    Map<String, Set<Participant>> selectedApprovers = new HashMap<String, Set<Participant>>();
    selectedApprovers.put( "selected_contest_approver_level_1", level1Approvers );
    selectedApprovers.put( "selected_contest_approver_level_2", level2Approvers );
    return selectedApprovers;
  }

  public List<SSIContestNameValueBean> getTranslatedContestNames( String assetCode, String key )
  {
    return ssiContestDAO.getTranslatedContestNames( assetCode, key );
  }

  public List<SSIContestDescriptionValueBean> getTranslatedContestDescriptions( String assetCode, String key )
  {
    return ssiContestDAO.getTranslatedContestDescriptions( assetCode, key );
  }

  public List<SSIContestMessageValueBean> getTranslatedContestMessages( String assetCode, String key )
  {
    return ssiContestDAO.getTranslatedContestMessages( assetCode, key );
  }

  public List<SSIContestDocumentValueBean> getTranslatedContestDocuments( String assetCode )
  {
    List<SSIContestDocumentValueBean> documents = new ArrayList<SSIContestDocumentValueBean>();
    List localeItems = cmAssetService.getSupportedLocales( false );
    for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      if ( content != null )
      {
        String localeCode = (String)content.getContentDataMap().get( "CODE" );
        Locale locale = CmsUtil.getLocale( localeCode );
        String documentDisplayName = cmAssetService.getString( SSIContest.CONTEST_CMASSET_DOCUMENT_DISPLAY_NAME, assetCode, locale, true );
        String documentOriginalName = cmAssetService.getString( SSIContest.CONTEST_CMASSET_DOCUMENT_ORIGINAL_NAME, assetCode, locale, true );
        String documentUrl = cmAssetService.getString( SSIContest.CONTEST_CMASSET_DOCUMENT_URL, assetCode, locale, true );
        String documentId = documentUrl;
        SSIContestDocumentValueBean nameVB = new SSIContestDocumentValueBean( localeCode, documentId, documentDisplayName, documentUrl, documentOriginalName );
        if ( StringUtils.isNotEmpty( documentId ) )
        {
          documents.add( nameVB );
        }
      }
    }
    return documents;
  }

  @Override
  public Map<String, Object> getContestManagersForSelectedPax( Long contestId, String locale, String sortedOn, String sortedBy ) throws ServiceErrorException
  {
    Map<String, Object> paxManagers = ssiContestDAO.getSSIContestManagerList( contestId, locale, getManagerSortColumn( sortedOn ), getManagerSortOrder( sortedBy ) );
    int managersCount = (Integer)paxManagers.get( "managerCount" );
    if ( managersCount == 0 )
    {
      log.error( "Procedure returned 0 managers" );
      throw new ServiceErrorException( "ssi_contest.pax.manager.NO_MANAGERS_FOUND" );
    }
    return paxManagers;
  }

  private String getManagerSortColumn( String sortedOn )
  {
    String sortCol = "last_name";
    if ( !StringUtil.isNullOrEmpty( sortedOn ) )
    {
      if ( "lastName".equals( sortedOn ) )
      {
        sortCol = "last_name";
      }
      else if ( "orgName".equals( sortedOn ) )
      {
        sortCol = "name";
      }
      else if ( "orgType".equals( sortedOn ) )
      {
        sortCol = "node_type";
      }
      else if ( "departmentName".equals( sortedOn ) )
      {
        sortCol = "department";
      }
      else if ( "jobName".equals( sortedOn ) )
      {
        sortCol = "job_position";
      }
    }
    return sortCol;
  }

  private String getManagerSortOrder( String sortedBy )
  {
    String sortOrder = "asc";
    if ( !StringUtil.isNullOrEmpty( sortedBy ) )
    {
      sortOrder = sortedBy;
    }
    return sortOrder;
  }

  public List<SSIContestListValueBean> getManagerArchivedContests( Long managerId )
  {
    List<SSIContestListValueBean> contestList = ssiContestDAO.getManagerArchivedContests( managerId );
    populateMgrRoleAndUrl( contestList, MANAGER_ROLE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.MANAGER" ), managerId );
    return translateAssetCode( contestList );
  }

  public List<SSIContestListValueBean> getManagerLiveContests( Long managerId )
  {
    List<SSIContestListValueBean> contestList = ssiContestDAO.getManagerLiveContests( managerId );
    contestList = translateAssetCode( contestList );
    populateMgrRoleAndUrl( contestList, MANAGER_ROLE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.MANAGER" ), managerId );
    return contestList;
  }

  protected List<SSIContestListValueBean> translateAssetCode( List<SSIContestListValueBean> contestList )
  {
    // Translate the asset code
    for ( SSIContestListValueBean contest : contestList )
    {
      contest.setName( cmAssetService.getString( contest.getName(), SSIContest.CONTEST_CMASSET_NAME, getUserLocale(), true ) );
    }
    return contestList;
  }

  /**
   * populate statusLabel for all value beans in the contestList
   * @param contestList
   * @return contestList with statusLabelPopulated
   */
  protected void populateStatusLabel( List<SSIContestListValueBean> contestList )
  {
    List<SSIContestStatus> contestStatusesFromPcikList = (List<SSIContestStatus>)SSIContestStatus.getList();
    Map<String, String> statusCodeDescriptionMap = new HashMap<String, String>();
    for ( SSIContestStatus contestStatus : contestStatusesFromPcikList )
    {
      String description = contestStatus.getDescription();
      if ( SSIContestStatus.FINALIZE_RESULTS.equals( contestStatus.getCode() ) )
      {
        description = ContentReaderManager.getText( "ssi_contest.creator", "FINAL" );
      }
      statusCodeDescriptionMap.put( contestStatus.getCode(), description );
    }
    // populate statusLabel with the description of pick list item
    for ( SSIContestListValueBean valueBean : contestList )
    {
      valueBean.setStatusLabel( statusCodeDescriptionMap.get( valueBean.getStatus() ) );
    }
  }

  private void populateMgrRoleAndUrl( List<SSIContestListValueBean> contestList, String role, String roleLabel, Long participantId )
  {
    String siteUrl = getSysUrl();
    for ( SSIContestListValueBean valueBean : contestList )
    {
      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put( SSIContestUtil.CONTEST_ID, valueBean.getContestId().toString() );
      paramMap.put( SSIContestUtil.USER_ID, participantId.toString() );
      String url = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_MGR_DETAIL_URL, paramMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );

      valueBean.setRole( role );
      valueBean.setRoleLabel( roleLabel );
      valueBean.setDetailPageUrl( url );
    }
  }

  protected void populateCreatorRoleAndUrl( List<SSIContestListValueBean> contestList, String role, String roleLabel, Long participantId )
  {
    String siteUrl = getSysUrl();
    for ( SSIContestListValueBean valueBean : contestList )
    {
      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put( SSIContestUtil.CONTEST_ID, valueBean.getContestId().toString() );
      paramMap.put( SSIContestUtil.USER_ID, participantId.toString() );
      String url = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_CREATOR_DETAIL_URL, paramMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );

      valueBean.setRole( role );
      valueBean.setRoleLabel( roleLabel );
      valueBean.setDetailPageUrl( url );
    }
  }

  private void populateApproverRoleAndUrl( List<SSIContestListValueBean> contestList, String role, String roleLabel, Long participantId )
  {
    String siteUrl = getSysUrl();
    for ( SSIContestListValueBean valueBean : contestList )
    {
      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put( SSIContestUtil.CONTEST_ID, valueBean.getContestId().toString() );
      if ( SSIContestType.AWARD_THEM_NOW.equals( valueBean.getContestType() ) )
      {
        valueBean.setDetailPageUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_ISSUANCE_APPROVAL_SUMMARY, paramMap ) );
        valueBean.setReadOnlyUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_ISSUANCE_APPROVAL_SUMMARY, paramMap ) );
      }
      else
      {
        valueBean.setDetailPageUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_APPROVAL_SUMMARY, paramMap ) );
        valueBean.setReadOnlyUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_APPROVAL_SUMMARY, paramMap ) );
      }

      valueBean.setRole( role );
      valueBean.setRoleLabel( roleLabel );
    }
  }

  public List<SSIContestParticipant> updatePayout( SSIContest detachSSIContest, Long contestId )
  {
    SSIContest attachSSIContest = this.updatePayoutForContest( detachSSIContest, contestId );
    updatePayoutForParticipants( attachSSIContest, detachSSIContest, contestId );
    return getFirstPageParticipants( detachSSIContest );
  }

  private SSIContest updatePayoutForContest( SSIContest detachSSIContest, Long contestId )
  {
    SSIContest attachContest = this.getContestById( contestId );
    if ( attachContest.getSameObjectiveDescription() != null && attachContest.getSameObjectiveDescription() && detachSSIContest.getSameObjectiveDescription() != null
        && !detachSSIContest.getSameObjectiveDescription() )
    {
      attachContest.setActivityDescription( null );
    }
    return attachContest;
  }

  private void updatePayoutForParticipants( SSIContest attachSSIContest, SSIContest detachSSIContest, Long contestId )
  {
    boolean resetObjectiveAmount = false;
    boolean resetObjectivePayoutDescription = false;
    boolean resetBonusFields = false;
    boolean resetActivityDescription = false;
    boolean resetPayout = false;

    if ( detachSSIContest.getActivityMeasureType() != null && attachSSIContest.getActivityMeasureType() != null
        && !detachSSIContest.getActivityMeasureType().getCode().equalsIgnoreCase( attachSSIContest.getActivityMeasureType().getCode() ) )
    {
      resetObjectiveAmount = true;
    }
    if ( detachSSIContest.getPayoutType() != null && attachSSIContest.getPayoutType() != null && detachSSIContest.getPayoutType().isOther() && attachSSIContest.getPayoutType().isPoints() )
    {
      resetObjectivePayoutDescription = true;
      resetPayout = true;
    }
    if ( !detachSSIContest.isIncludeBonus() && attachSSIContest.isIncludeBonus()
        || detachSSIContest.getPayoutType() != null && detachSSIContest.getPayoutType().isOther() && attachSSIContest.getPayoutType() != null && attachSSIContest.getPayoutType().isPoints() )
    {
      resetBonusFields = true;
    }
    if ( detachSSIContest.getSameObjectiveDescription() != null && detachSSIContest.getSameObjectiveDescription() && attachSSIContest.getSameObjectiveDescription() != null
        && !attachSSIContest.getSameObjectiveDescription() )
    {
      resetActivityDescription = true;
    }
    if ( resetObjectiveAmount || resetObjectivePayoutDescription || resetBonusFields || resetActivityDescription || resetPayout )
    {
      this.ssiContestDAO.updatePayout( resetObjectiveAmount, resetObjectivePayoutDescription, resetBonusFields, resetActivityDescription, resetPayout, contestId );
    }
    attachSSIContest.setSameObjectiveDescription( detachSSIContest.getSameObjectiveDescription() );
  }

  /**
   * Will save the current page results and call the stored procedure to validate if all the participant information is filled or not.
   * {@inheritDoc}
   */
  @Override
  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId ) throws ServiceErrorException
  {
    return this.ssiContestDAO.calculatePayoutObjectivesTotals( contestId );
  }

  @Override
  public Long calculatePayoutDoThisGetThatTotals( Long contestId )
  {
    return this.ssiContestDAO.calculatePayoutDoThisGetThatTotals( contestId );
  }

  /**
   * Method to save the contest and participant pay outs - Step 3 of Objectives contest create
   * {@inheritDoc}
   * @throws ServiceErrorException 
   */
  @Override
  public SSIContest savePayoutObjectives( SSIContest detachedContest, Long badgeRuleId, List<SSIContestParticipantValueBean> detachedParticipants ) throws ServiceErrorException
  {
    // Look up the SSIContest
    Long contestId = detachedContest.getId();
    SSIContest attachedContest = this.getContestById( contestId );

    // Merge the SSIContest from the page
    mergeContest( detachedContest, badgeRuleId, attachedContest );

    updateContestParticipant( detachedParticipants );

    SSIContestBillCodesUpdateAssociation billCodeAssociation = buildBillCodesUpdateAssociation( detachedContest );
    billCodeAssociation.execute( attachedContest );

    // update contest goal for live contest
    if ( attachedContest.getStatus().isLive() )
    {
      flushHibernateSession();
      updateContestGoalPercentage( contestId );
    }
    return attachedContest;
  }

  /**
   * Method to save the contest and participant pay outs - Step 3 of Step It Up contest create
   * {@inheritDoc}
   * @throws ServiceErrorException 
   */
  @Override
  public SSIContest savePayoutStepItUp( SSIContest detachedContest, List<SSIContestParticipantValueBean> detachedParticipants ) throws ServiceErrorException
  {
    Long contestId = detachedContest.getId();

    SSIContest attachedContest = this.getContestById( contestId );

    mergeContest( detachedContest, BADGE_RULE_NOT_AVAILABLE, attachedContest );

    updateContestParticipant( detachedParticipants );

    SSIContestBillCodesUpdateAssociation billCodeAssociation = buildBillCodesUpdateAssociation( detachedContest );
    billCodeAssociation.execute( attachedContest );

    // update contest goal for live contest
    if ( attachedContest.getStatus().isLive() )
    {
      flushHibernateSession();
      updateContestGoalPercentage( contestId );
    }
    return attachedContest;
  }

  protected void flushHibernateSession()
  {
    HibernateSessionManager.getSession().flush();
  }

  // The update association clears the hibernate cache.
  // Doing this so we can override it and avoid the static-state hibernate call.
  protected SSIContestBillCodesUpdateAssociation buildBillCodesUpdateAssociation( SSIContest detachedContest )
  {
    return new SSIContestBillCodesUpdateAssociation( detachedContest );
  }

  public SSIContest savePayoutStackRank( SSIContest detachedContest, List<SSIContestRankValueBean> detachedRanks, List<UpdateAssociationRequest> updateAssociation ) throws ServiceErrorException
  {
    Long contestId = detachedContest.getId();
    SSIContest attachedContest = this.getContestById( contestId );

    mergeContest( detachedContest, BADGE_RULE_NOT_AVAILABLE, attachedContest );

    for ( SSIContestRankValueBean valueBean : detachedRanks )
    {
      SSIContestStackRankPayout rank = new SSIContestStackRankPayout();
      rank.setBadgeRule( getBadgeRule( valueBean.getBadgeId() ) );
      rank.setId( valueBean.getId() );
      rank.setPayoutAmount( valueBean.getPayoutAmount() );
      rank.setPayoutDescription( valueBean.getPayoutDescription() );
      rank.setRankPosition( valueBean.getRank() );
      detachedContest.addStackRankPayout( rank );
    }
    SSIContestStackRankPayoutsUpdateAssociation association = new SSIContestStackRankPayoutsUpdateAssociation( detachedContest );
    association.execute( attachedContest );

    SSIContestBillCodesUpdateAssociation billCodeAssociation = new SSIContestBillCodesUpdateAssociation( detachedContest );
    billCodeAssociation.execute( attachedContest );

    // update contest goal for live contest
    if ( attachedContest.getStatus().isLive() )
    {
      updateContestGoalPercentage( contestId );
    }
    return attachedContest;
  }

  public SSIContestPayoutStackRankTotalsValueBean getStackRankTotals( Long contestId )
  {
    return this.ssiContestDAO.getStackRankTotals( contestId );
  }

  private List<SSIContestParticipant> getAttachedParticipants( List<Long> paxIds )
  {
    return this.ssiContestDAO.getContestParticipants( paxIds );
  }

  protected List<Long> lookupDetachedParticipants( List<SSIContestParticipantValueBean> detachedParticipants )
  {
    List<Long> paxIds = new ArrayList<Long>();
    for ( SSIContestParticipantValueBean contestParticipant : ServiceUtil.emptyIfNull( detachedParticipants ) )
    {
      paxIds.add( contestParticipant.getId() );
    }
    return paxIds;
  }

  /**
   * Method to save the contest and activity payouts  - Step 3 of Do This Get That contest create
   * {@inheritDoc}
   * @throws ServiceErrorException 
   */
  @Override
  public SSIContest savePayoutDoThisGetThat( SSIContest detachedContest ) throws ServiceErrorException
  {

    Long contestId = detachedContest.getId();
    SSIContest attachedContest = this.getContestById( contestId );

    mergeContest( detachedContest, BADGE_RULE_NOT_AVAILABLE, attachedContest );

    SSIContestBillCodesUpdateAssociation billCodeAssociation = buildBillCodesUpdateAssociation( detachedContest );
    billCodeAssociation.execute( attachedContest );

    // update contest goal for live contest
    if ( attachedContest.getStatus().isLive() )
    {
      updateContestGoalPercentage( contestId );
    }

    return attachedContest;
  }

  @Override
  public SSIContestActivity saveContestActivity( Long contestId, SSIContestActivity ssiContestActivity )
  {

    ssiContestActivity.setSequenceNumber( getNextSequenceNumber( contestId ) );

    SSIContest contest = this.getContestById( contestId );
    ssiContestActivity.setContest( contest );
    ssiContestActivity = this.ssiContestDAO.saveContestActivity( ssiContestActivity );
    contest.getContestActivities().add( ssiContestActivity );
    return ssiContestActivity;
  }

  private int getNextSequenceNumber( Long contestId )
  {
    return this.ssiContestDAO.getNextSequenceNum( contestId );
  }

  private int getNextLevelSequenceNumber( Long contestId )
  {
    return this.ssiContestDAO.getNextLevelSequenceNum( contestId );
  }

  @Override
  public SSIContestActivity updateContestActivity( SSIContestActivity detachedContestActivity )
  {
    SSIContestActivity attachedContestActivity = this.getContestActivityById( detachedContestActivity.getId() );
    attachedContestActivity.setDescription( detachedContestActivity.getDescription() );
    attachedContestActivity.setGoalAmount( detachedContestActivity.getGoalAmount() );
    attachedContestActivity.setIncrementAmount( detachedContestActivity.getIncrementAmount() );
    attachedContestActivity.setMinQualifier( detachedContestActivity.getMinQualifier() );
    attachedContestActivity.setPayoutAmount( detachedContestActivity.getPayoutAmount() );
    attachedContestActivity.setPayoutCapAmount( detachedContestActivity.getPayoutCapAmount() );
    attachedContestActivity.setPayoutDescription( detachedContestActivity.getPayoutDescription() );
    return attachedContestActivity;
  }

  @Override
  public List<SSIContestListValueBean> getContestsWithTodayTileStartDate()
  {
    return this.ssiContestDAO.getContestsWithTodayTileStartDate();
  }

  @Override
  public void deleteContestActivity( Long contestId, Long contestActivityId, Long userId ) throws ServiceErrorException
  {
    this.ssiContestDAO.deleteContestActivity( contestId, contestActivityId, userId );
  }

  @Override
  public List<SSIContestParticipant> saveContestAndFetchNextPageResults( SSIContest detachedContest,
                                                                         Long badgeRuleId,
                                                                         List<SSIContestParticipantValueBean> detachedParticipants,
                                                                         String sortedBy,
                                                                         String sortedOn,
                                                                         int pageNumber )
      throws ServiceErrorException
  {
    // Save the current page
    savePayoutObjectives( detachedContest, badgeRuleId, detachedParticipants );

    // Lookup existing participant data for the specified page
    List<SSIContestParticipant> nextPageParticipants = getNextPageContestParticipants( detachedContest, sortedBy, sortedOn, pageNumber );
    return nextPageParticipants;
  }

  protected List<SSIContestParticipant> getNextPageContestParticipants( SSIContest detachedContest, String sortedBy, String sortColumnName, int pageNumber )
  {
    return ssiContestParticipantDAO.getContestParticipants( detachedContest.getId(), pageNumber, SSIContestUtil.PAX_RECORDS_PER_PAGE, sortColumnName, sortedBy );
  }

  /**
   * Save the current page and load the participants for the next page
   * {@inheritDoc}
   * @throws ServiceErrorException 
   */
  @Override
  public List<SSIContestParticipant> saveContestAndFetchNextPageResults( SSIContest detachedContest,
                                                                         List<SSIContestParticipantValueBean> detachedParticipants,
                                                                         String sortedBy,
                                                                         String sortedOn,
                                                                         int pageNumber )
      throws ServiceErrorException
  {
    // Save the current page
    savePayoutStepItUp( detachedContest, detachedParticipants );

    // Lookup existing participant data for the specified page
    return getNextPageContestParticipants( detachedContest, sortedBy, sortedOn, pageNumber );
  }

  /**
   * Merge the detached contest and attached contest
   * @param detachedContest
   * @param badgeRuleId
   * @param attachedContest
   */
  private void mergeContest( SSIContest detachedContest, Long badgeRuleId, SSIContest attachedContest )
  {
    // Update the badge rule
    attachedContest.setBadgeRule( getBadgeRule( badgeRuleId ) );
    attachedContest.setActivityMeasureType( detachedContest.getActivityMeasureType() );
    attachedContest.setActivityMeasureCurrencyCode( detachedContest.getActivityMeasureCurrencyCode() );
    attachedContest.setPayoutType( detachedContest.getPayoutType() );
    attachedContest.setPayoutOtherCurrencyCode( detachedContest.getPayoutOtherCurrencyCode() );
    attachedContest.setIncludeBonus( detachedContest.isIncludeBonus() );
    attachedContest.setSameObjectiveDescription( detachedContest.getSameObjectiveDescription() );
    attachedContest.setStackRankOrder( detachedContest.getStackRankOrder() );
    attachedContest.setIncludeStackRank( detachedContest.isIncludeStackRank() );
    attachedContest.setContestGoal( detachedContest.getContestGoal() );
    attachedContest.setActivityDescription( detachedContest.getActivityDescription() );
    attachedContest.setIndividualBaselineType( detachedContest.getIndividualBaselineType() );
    attachedContest.setStepItUpBonusCap( detachedContest.getStepItUpBonusCap() );
    attachedContest.setStepItUpBonusIncrement( detachedContest.getStepItUpBonusIncrement() );
    attachedContest.setStepItUpBonusPayout( detachedContest.getStepItUpBonusPayout() );

    // stack rank contest properties
    attachedContest.setIncludeStackRankQualifier( detachedContest.getIncludeStackRankQualifier() );
    attachedContest.setStackRankOrder( detachedContest.getStackRankOrder() );
    attachedContest.setStackRankQualifierAmount( detachedContest.getStackRankQualifierAmount() );

    // bill code properties
    attachedContest.setBillPayoutCodeType( detachedContest.getBillPayoutCodeType() );
    /*
     * attachedContest.setBillPayoutCode1( detachedContest.getBillPayoutCode1() );
     * attachedContest.setBillPayoutCode2( detachedContest.getBillPayoutCode2() );
     */
  }

  protected BadgeRule getBadgeRule( Long badgeRuleId )
  {
    BadgeRule badgeRule = null;
    if ( badgeRuleId != null && badgeRuleId > 0 )
    {
      badgeRule = this.gamificationDAO.getBadgeRuleById( badgeRuleId );
    }
    return badgeRule;
  }

  /**
   * Merge the detached contest participants and attached contest participants
   * @param attachedContestParticipants
   * @param detachedContestParticipants
   */
  protected void mergeContestParticipants( List<SSIContestParticipant> attachedContestParticipants, List<SSIContestParticipantValueBean> detachedContestParticipants )
  {
    if ( attachedContestParticipants != null && attachedContestParticipants.size() > 0 && detachedContestParticipants != null && detachedContestParticipants.size() > 0 )
    {
      for ( SSIContestParticipant attachSSIContestParticipant : ServiceUtil.emptyIfNull( attachedContestParticipants ) )
      {
        for ( SSIContestParticipantValueBean detachSSIContestParticipant : ServiceUtil.emptyIfNull( detachedContestParticipants ) )
        {
          if ( attachSSIContestParticipant.getId().equals( detachSSIContestParticipant.getId() ) )
          {
            attachSSIContestParticipant.setActivityDescription( detachSSIContestParticipant.getActivityDescription() );
            attachSSIContestParticipant
                .setObjectiveAmount( !StringUtil.isNullOrEmpty( detachSSIContestParticipant.getObjectiveAmount() ) ? Double.parseDouble( detachSSIContestParticipant.getObjectiveAmount() ) : null );
            attachSSIContestParticipant
                .setObjectiveBonusCap( !StringUtil.isNullOrEmpty( detachSSIContestParticipant.getBonusPayoutCap() ) ? Long.parseLong( detachSSIContestParticipant.getBonusPayoutCap() ) : null );
            attachSSIContestParticipant.setObjectiveBonusIncrement( !StringUtil.isNullOrEmpty( detachSSIContestParticipant.getBonusForEvery() )
                ? Double.parseDouble( detachSSIContestParticipant.getBonusForEvery() )
                : null );
            attachSSIContestParticipant
                .setObjectiveBonusPayout( !StringUtil.isNullOrEmpty( detachSSIContestParticipant.getBonusPayout() ) ? Long.parseLong( detachSSIContestParticipant.getBonusPayout() ) : null );
            attachSSIContestParticipant
                .setObjectivePayout( !StringUtil.isNullOrEmpty( detachSSIContestParticipant.getObjectivePayout() ) ? Long.parseLong( detachSSIContestParticipant.getObjectivePayout() ) : null );
            attachSSIContestParticipant.setObjectivePayoutDescription( detachSSIContestParticipant.getObjectivePayoutDescription() );
            attachSSIContestParticipant.setStepItUpBaselineAmount( !StringUtil.isNullOrEmpty( detachSSIContestParticipant.getBaselineAmount() )
                ? Double.parseDouble( detachSSIContestParticipant.getBaselineAmount() )
                : null );
          }
        }
      }
    }
  }

  @Override
  public SSIContestActivity getContestActivityById( Long contestActivityId )
  {
    return ssiContestDAO.getContestActivityById( contestActivityId );
  }

  @Override
  public List<SSIContestActivity> getContestActivitiesByContestId( Long contestId )
  {
    return ssiContestDAO.getContestActivitiesByContestId( contestId );
  }

  public List<SSIContestListValueBean> getContestListByCreator( Long creatorId )
  {
    List<SSIContestListValueBean> contestList = ssiContestDAO.getContestListByCreatorSuperViewer( creatorId );
    List<SSIContestListValueBean> atnContestList = ssiContestDAO.getAwardThemNowContestSuperViewer( creatorId );
    contestList = translateAssetCode( contestList );
    atnContestList = translateAssetCode( atnContestList );
    populateStatusLabel( contestList );
    populateCreatorRoleAndUrl( contestList, CREATOR_ROLE, getCreatotRoleLabel(), creatorId );
    Collections.sort( contestList );
    contestList.addAll( atnContestList );
    return contestList;
  }

  public List<SSIContestListValueBean> getApprovalsForListPageByUserId( Long userId )
  {
    List<SSIContestListValueBean> contestList = ssiContestDAO.getContestWaitingForApprovalByUserId( userId );
    contestList = translateAssetCode( contestList );
    populateStatusLabel( contestList );
    populateApproverRoleAndUrl( contestList, APPROVER_ROLE, getApproverRoleLabel(), userId );
    populateContestTypeName( contestList );
    contestList.forEach( value -> value.setCanShowActionLinks( true ) );
    return contestList;
  }

  private void populateContestTypeName( List<SSIContestListValueBean> contestList )
  {
    contestList.forEach( value -> value.setContestTypeName( SSIContestType.lookup( value.getContestType() ).getName() ) );
  }

  public List<SSIContestListValueBean> getArchivedContestListByCreator( Long creatorId )
  {
    List<SSIContestListValueBean> contestList = ssiContestDAO.getArchivedContestListByCreator( creatorId );
    List<SSIContestListValueBean> superViewerContests = ssiContestDAO.getArchivedContestListBySuperViewer( creatorId );
    List<SSIContestListValueBean> superViewerDistinctContests = new ArrayList<SSIContestListValueBean>();
    for ( SSIContestListValueBean ssiContest : superViewerContests )
    {
      if ( !contestList.contains( ssiContest ) )
      {
        superViewerDistinctContests.add( ssiContest );
      }
    }
    contestList.addAll( superViewerDistinctContests );

    populateCreatorRoleAndUrl( contestList, CREATOR_ROLE, getCreatotRoleLabel(), creatorId );
    Collections.sort( translateAssetCode( contestList ) );
    return contestList;
  }

  public List<SSIContestListValueBean> getDeniedContestListByCreator( Long creatorId )
  {
    List<SSIContestListValueBean> contestList = ssiContestDAO.getDeniedContestListByCreator( creatorId );
    List<SSIContestListValueBean> superViewerContests = ssiContestDAO.getDeniedContestListBySuperViewer( creatorId );
    List<SSIContestListValueBean> superViewerDistinctContests = new ArrayList<SSIContestListValueBean>();
    for ( SSIContestListValueBean ssiContest : superViewerContests )
    {
      if ( !contestList.contains( ssiContest ) )
      {
        superViewerDistinctContests.add( ssiContest );
      }
    }
    contestList.addAll( superViewerDistinctContests );

    Collections.sort( translateAssetCode( contestList ) );
    return contestList;
  }

  public List<SSIContest> getCreatorLiveContests( Long creatorId )
  {
    List<SSIContest> creatorContests = ssiContestDAO.getCreatorLiveContests( creatorId );
    List<SSIContest> superViewerContests = ssiContestDAO.getSuperViewerLiveContests( creatorId );
    List<SSIContest> superViewerDistinctContests = new ArrayList<SSIContest>();
    for ( SSIContest ssiContest : superViewerContests )
    {
      if ( !creatorContests.contains( ssiContest ) )
      {
        superViewerDistinctContests.add( ssiContest );
      }
    }
    creatorContests.addAll( superViewerDistinctContests );
    return creatorContests;
  }

  public void deleteContest( Long contestId ) throws ServiceErrorException
  {

    SSIContest ssiContest = ssiContestDAO.getContestById( contestId );
    String contestNameCmAssetCode = ssiContest.getCmAssetCode();
    if ( ssiContest != null )
    {
      if ( ssiContest.isDeleteable() )
      {

        ssiContestDAO.deleteContest( ssiContest );
        // delete contest name from cm asset
        // TODO; the following implementation is not available
        cmAssetService.deleteCMAsset( contestNameCmAssetCode );
      }
      else
      {
        // The contest is at a status that we cannot delete
        throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_DELETE_STATUS_ERR );
      }
    }
  }

  @Override
  public SSIContestLevel getContestLevelById( Long contestLevelId )
  {
    return ssiContestDAO.getContestLevelById( contestLevelId );
  }

  @Override
  public List<SSIContestLevel> getContestLevelsByContestId( Long contestId )
  {
    return ssiContestDAO.getContestLevelsByContestId( contestId );
  }

  public Long copyContest( Long sourceContestId, String destinationContestName, String locale ) throws ServiceErrorException
  {
    return ssiContestDAO.copyContest( sourceContestId, destinationContestName, locale );
  }

  // public void setSsiContestParticipantService( SSIContestParticipantService
  // ssiContestParticipantService )
  // {
  // this.ssiContestParticipantService = ssiContestParticipantService;
  // }

  public void approveContest( Long contestId, Long userId, Integer approvalLevel ) throws ServiceErrorException
  {
    updateApprovalStatus( contestId, userId, approvalLevel, APPROVE, "" );
  }

  public void denyContest( Long contestId, Long userId, Integer approvalLevel, String denialReason ) throws ServiceErrorException
  {
    updateApprovalStatus( contestId, userId, approvalLevel, DENY, denialReason );

  }

  private void updateApprovalStatus( Long contestId, Long userId, int approvalLevel, String approvalType, String denialReason ) throws ServiceErrorException
  {
    validateUserForApprovalDecision( contestId, userId );
    AssociationRequestCollection ssiContestAssociationRequest = new AssociationRequestCollection();
    ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_LEVEL2_APPROVERS ) );
    SSIContest attachSSIContest = getContestByIdWithAssociations( contestId, ssiContestAssociationRequest );
    validateContestStatusForAppovalDecision( attachSSIContest );
    validateUserOperationForContestApprovalDecision( userId, attachSSIContest );
    Date today = new Date();
    if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL && APPROVAL_LEVEL_ONE == approvalLevel && APPROVE.equalsIgnoreCase( approvalType ) )
    {
      setLiveOrPendingStatus( attachSSIContest, today );
      setLevelOneApprovedByAndDate( userId, attachSSIContest );
      sendLevel2ApprovalAlert( attachSSIContest, false, userId, null );
    }
    else if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL && APPROVAL_LEVEL_ONE == approvalLevel && DENY.equalsIgnoreCase( approvalType ) )
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.DENIED ) );
      attachSSIContest.setDenialReason( denialReason );
      setLevelOneApprovedByAndDate( userId, attachSSIContest );
      sendLevel2ApprovalAlert( attachSSIContest, false, userId, null );
    }
    else if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL && APPROVAL_LEVEL_ONE == approvalLevel && APPROVE.equalsIgnoreCase( approvalType ) )
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.WAITING_FOR_APPROVAL ) );
      setLevelOneApprovedByAndDate( userId, attachSSIContest );
      sendLevel2ApprovalAlert( attachSSIContest, true, userId, null );
    }
    else if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL && APPROVAL_LEVEL_ONE == approvalLevel && DENY.equalsIgnoreCase( approvalType ) )
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.DENIED ) );
      attachSSIContest.setDenialReason( denialReason );
      setLevelOneApprovedByAndDate( userId, attachSSIContest );
      sendLevel2ApprovalAlert( attachSSIContest, false, userId, null );
    }
    else if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL && APPROVAL_LEVEL_TWO == approvalLevel && APPROVE.equalsIgnoreCase( approvalType ) )
    {
      setLiveOrPendingStatus( attachSSIContest, today );
      setLevelTwoApprovedByAndDate( userId, attachSSIContest );
      sendLevel2ApprovalAlert( attachSSIContest, false, userId, null );
    }
    else if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL && APPROVAL_LEVEL_TWO == approvalLevel && DENY.equalsIgnoreCase( approvalType ) )
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.DENIED ) );
      attachSSIContest.setDenialReason( denialReason );
      setLevelTwoApprovedByAndDate( userId, attachSSIContest );
      sendLevel2ApprovalAlert( attachSSIContest, false, userId, null );
    }
    // Track SSI Admin Action
    if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
    {
      if ( APPROVE.equalsIgnoreCase( approvalType ) )
      {
        saveAdminAction( contestId,
                         UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                         SSIAdminContestActions.APPROVE_CONTEST,
                         "Approved Contest for Contest ID: " + contestId + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );
      }
      else if ( DENY.equalsIgnoreCase( approvalType ) )
      {
        saveAdminAction( contestId,
                         UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                         SSIAdminContestActions.DENY_CONTEST,
                         "Denied Contest for Contest ID: " + contestId + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );
      }
    }
  }

  private void validateUserOperationForContestApprovalDecision( Long userId, SSIContest attachSSSIContest ) throws ServiceErrorException
  {
    if ( attachSSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL && attachSSSIContest.getApprovedByLevel1() != null
        && attachSSSIContest.getApprovedByLevel1().equals( userId ) )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_APPPROVAL_DENY_ERROR );
    }
  }

  private void validateContestStatusForAppovalDecision( SSIContest attachSSSIContest ) throws ServiceErrorException
  {
    if ( !attachSSSIContest.getStatus().isWaitingForApproval() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR );
    }
  }

  /**
   * Making sure that only contest approvers are approving the contest
   * @param contestId
   * @param userId
   * @throws ServiceErrorException
   */
  protected void validateUserForApprovalDecision( Long contestId, Long userId ) throws ServiceErrorException
  {
    if ( !ssiContestDAO.checkUserBelongToContestApproversGroup( contestId, userId ) )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.INVALID_USER_FOR_CONTEST_APPROVER_ERROR );
    }
  }

  private void setLevelOneApprovedByAndDate( Long userId, SSIContest attachSSSIContest ) throws ServiceErrorException
  {
    if ( attachSSSIContest.getApprovedByLevel1() != null && attachSSSIContest.getDateApprovedLevel1() != null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR );
    }
    else
    {
      attachSSSIContest.setApprovedByLevel1( userId );
      attachSSSIContest.setLevelApproved( LEVEL_ONE_APPROVED );
      attachSSSIContest.setDateApprovedLevel1( DateUtils.getCurrentDateTrimmed() );
    }
  }

  private void setLevelTwoApprovedByAndDate( Long userId, SSIContest attachSSSIContest ) throws ServiceErrorException
  {
    if ( attachSSSIContest.getApprovedByLevel1() == null || attachSSSIContest.getApprovedByLevel2() != null && attachSSSIContest.getDateApprovedLevel2() != null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR );
    }
    else
    {
      attachSSSIContest.setApprovedByLevel2( userId );
      attachSSSIContest.setLevelApproved( LEVEL_TWO_APPROVED );
      attachSSSIContest.setDateApprovedLevel2( DateUtils.getCurrentDateTrimmed() );
    }
  }

  @Override
  public void updateContestStatus( Long contestId )
  {
    AssociationRequestCollection ssiContestAssociationRequest = new AssociationRequestCollection();
    ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_LEVEL1_APPROVERS ) );
    SSIContest attachSSIContest = this.ssiContestDAO.getContestByIdWithAssociations( contestId, ssiContestAssociationRequest );
    if ( attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL
        || attachSSIContest.getPromotion().getContestApprovalLevels() == CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL )
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.WAITING_FOR_APPROVAL ) );
      sendLevel1ApprovalAlert( attachSSIContest );
    }
    else
    {
      setLiveOrPendingStatus( attachSSIContest, new Date() );
    }
  }

  // based on contest start date set status live or pending
  private void setLiveOrPendingStatus( SSIContest attachSSIContest, Date today )
  {
    if ( org.apache.commons.lang3.time.DateUtils.isSameDay( today, attachSSIContest.getStartDate() ) || attachSSIContest.getStartDate().before( today ) )
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
      attachSSIContest.setDateLaunched( today );
    }
    else
    {
      attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.PENDING ) );
    }
  }

  protected void sendLevel1ApprovalAlert( SSIContest contest )
  {
    AlertMessage alertMessage = new AlertMessage();
    alertMessage.setSubject( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_SUBJECT" ) );
    alertMessage.setExpiryDate( contest.getEndDate() );
    alertMessage.setMessage( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_MESG" ) );
    alertMessage.setContestId( contest.getId() );
    alertMessage.setSsiAlertType( SSIContest.CONTEST_ROLE_APPROVER );
    for ( SSIContestApprover ssiContestApprover : contest.getContestLevel1Approvers() )
    {
      alertMessage.setMessageTo( String.valueOf( ssiContestApprover.getApprover().getId() ) );
      ParticipantAlert participantAlert = new ParticipantAlert();
      participantAlert.setUser( ssiContestApprover.getApprover() );
      participantAlert.setAlertMessage( alertMessage );
      this.participantAlertService.saveParticipantAlert( participantAlert );
    }

    // send level 1 approval notification
    Participant creator = this.participantDAO.getParticipantById( contest.getCreatorId() );
    Mailing level1ApprovalMailing = this.mailingService.buildSSIContestApprovalNotification( contest, contest.getContestLevel1Approvers(), creator, true, contest.getApprovedByLevel1() );
    if ( level1ApprovalMailing != null )
    {
      this.mailingService.submitMailing( level1ApprovalMailing, null );
    }
  }

  protected void sendLevel2ApprovalAlert( SSIContest contest, boolean sendLevel2Alerts, Long level1ApproverId, SSIContestAwardThemNow contestAwardThemNow )
  {
    // Update level1 alert to set expire date to yesterday.
    // Now Level1 approvers no longer will see alert
    List<AlertMessage> alertMessages = this.alertMessageService.getAlertMessageByContestId( contest.getId() );
    if ( alertMessages != null )
    {
      for ( AlertMessage alertMessage : alertMessages )
      {
        alertMessage.setExpiryDate( org.apache.commons.lang3.time.DateUtils.addDays( new Date(), -1 ) );
      }
    }

    // send contest status notification to creator(approved/denied)
    Participant creator = this.participantDAO.getParticipantById( contest.getCreatorId() );
    String level1ApproverFullName = this.participantDAO.getLNameFNameByPaxId( level1ApproverId );

    Mailing approverStatusMailing = this.mailingService.buildSSIContestApprovalStatusNotification( contest, creator, level1ApproverFullName, contestAwardThemNow );

    if ( approverStatusMailing != null )
    {
      this.mailingService.submitMailing( approverStatusMailing, null );
    }

    if ( sendLevel2Alerts )
    {
      // Send level2 approvals and ignore level2 approver if
      // level1 approver is same as level 2 approver
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setSubject( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_SUBJECT" ) );
      alertMessage.setExpiryDate( contest.getEndDate() );
      alertMessage.setMessage( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_MESG" ) );
      alertMessage.setContestId( contest.getId() );
      alertMessage.setSsiAlertType( SSIContest.CONTEST_ROLE_APPROVER );
      for ( SSIContestApprover contestApprover : contest.getContestLevel2Approvers() )
      {
        // exclude the level1 approver for the alerts
        if ( !contestApprover.getApprover().getId().equals( level1ApproverId ) )
        {
          alertMessage.setMessageTo( String.valueOf( contestApprover.getApprover().getId() ) );
          ParticipantAlert participantAlert = new ParticipantAlert();
          participantAlert.setUser( contestApprover.getApprover() );
          participantAlert.setAlertMessage( alertMessage );
          this.participantAlertService.saveParticipantAlert( participantAlert );
        }
      }

      // send level 2 approval notification
      Mailing level2ApprovalMailing = this.mailingService.buildSSIContestApprovalNotification( contest, contest.getContestLevel2Approvers(), creator, false, level1ApproverId );
      if ( level2ApprovalMailing != null )
      {
        this.mailingService.submitMailing( level2ApprovalMailing, null );
      }
    }
  }

  @Override
  public List<SSIContest> getAllContestsByStatus( List<SSIContestStatus> contestStatuses )
  {
    return ssiContestDAO.getAllContestsByStatus( contestStatuses );
  }

  public List<SSIContestListValueBean> getContestWaitingForApprovalByUserId( Long userId )
  {
    return this.ssiContestDAO.getContestWaitingForApprovalByUserId( userId );
  }

  public List<NameIdBean> getContestNames( Set<Long> contestIds, String locale )
  {
    return this.ssiContestDAO.getContestNames( contestIds, locale );
  }

  public Map<String, Object> downloadContestData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    return this.ssiContestDAO.downloadContestData( inParameters );
  }

  public Map<String, Object> downloadContestPayoutData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    return this.ssiContestDAO.downloadContestPayoutData( inParameters );
  }

  public Map<String, Object> extractContestClaimData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    return this.ssiContestDAO.extractContestClaimData( inParameters );
  }

  @Override
  public Map<String, Object> downloadContestCreatorExtractData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    return this.ssiContestDAO.downloadContestCreatorExtractData( inParameters );
  }

  @Override
  public SSIContestLevel saveContestLevel( Long contestId, SSIContestLevel ssiContestLevel, Long badgeRuleId )
  {

    SSIContest contest = this.getContestById( contestId );
    ssiContestLevel.setBadgeRule( getBadgeRule( badgeRuleId ) );
    ssiContestLevel.setContest( contest );
    ssiContestLevel.setSequenceNumber( getNextLevelSequenceNumber( contestId ) );
    ssiContestLevel.setGoalAmount( ssiContestLevel.getGoalAmount() );
    ssiContestLevel.setPayoutAmount( ssiContestLevel.getPayoutAmount() );
    ssiContestLevel.setPayoutDesc( ssiContestLevel.getPayoutDesc() );

    ssiContestLevel = this.ssiContestDAO.saveContestLevel( ssiContestLevel );
    contest.getContestLevels().add( ssiContestLevel );
    return ssiContestLevel;
  }

  @Override
  public void deleteContestLevel( Long contestid, Long contestLevelId, Long userId ) throws ServiceErrorException
  {
    this.ssiContestDAO.deleteContestLevel( contestid, contestLevelId, userId );
  }

  public List<SSIContestStackRankPayout> getStackRankPayoutsByContestId( Long contestId )
  {

    return this.ssiContestDAO.getStackRankPayoutsByContestId( contestId );
  }

  @Override
  public SSIContestLevel updateContestLevel( SSIContestLevel ssiContestLevel, Long badgeRuleId )
  {
    SSIContestLevel attachedContestLevel = this.getContestLevelById( ssiContestLevel.getId() );
    ssiContestLevel.setBadgeRule( getBadgeRule( badgeRuleId ) );
    attachedContestLevel.setGoalAmount( ssiContestLevel.getGoalAmount() );
    attachedContestLevel.setBadgeRule( ssiContestLevel.getBadgeRule() );
    attachedContestLevel.setId( ssiContestLevel.getId() );
    attachedContestLevel.setPayoutAmount( ssiContestLevel.getPayoutAmount() );
    attachedContestLevel.setPayoutDesc( ssiContestLevel.getPayoutDesc() );
    attachedContestLevel.setSequenceNumber( ssiContestLevel.getSequenceNumber() );

    return attachedContestLevel;
  }

  public SSIContestSummaryValueBean getParticipantsSummaryData( Long contestId, Long userId, String sortBy, String sortColumnName, int pageNumber, int perPage ) throws ServiceErrorException
  {
    return this.ssiContestDAO.getParticipantsSummaryData( contestId, userId, sortBy, sortColumnName, pageNumber, perPage );
  }

  @Override
  public List<SSIContestParticipant> resetAllParticipantsBaseLineAmount( String individualBaselineType, Long contestId ) throws ServiceErrorException
  {
    try
    {
      if ( SSIIndividualBaselineType.NO_BASELINE_CODE.equals( individualBaselineType ) )
      {
        this.ssiContestDAO.resetAllParticipantsBaseLineAmount( contestId );
      }
    }
    catch( SQLException sqlException )
    {
      log.error( "SQL Error occurred while resetting all pax baseline amts " + sqlException );
      throw new ServiceErrorException( "ssi_contest.payout_stepitup.RESET_BASELINE_ERROR" );
    }
    return this.ssiContestParticipantDAO
        .getContestParticipants( contestId, SSIContestUtil.FIRST_PAGE_NUMBER, SSIContestUtil.PAX_RECORDS_PER_PAGE, SSIContestUtil.SORT_BY_LAST_NAME, SSIContestUtil.DEFAULT_SORT_BY );
  }

  @Override
  public SSIContestBaseLineTotalsValueBean calculateBaseLineTotalsForStepItUp( Long contestId )
  {
    return ssiContestDAO.calculateBaseLineTotalsForStepItUp( contestId );
  }

  @Override
  public List<SSIContestParticipant> updateLevelPayout( SSIContest detachSSIContest, Long contestId )
  {
    List<SSIContestParticipant> firstPageParticipants = null;
    SSIContest attachContest = this.getContestById( contestId );
    if ( attachContest.getPayoutType() != null && detachSSIContest.getPayoutType() != null && attachContest.getPayoutType().isOther() && detachSSIContest.getPayoutType().isPoints() )
    {
      List<SSIContestLevel> contestlevels = ssiContestDAO.getContestLevelsByContestId( contestId );
      for ( SSIContestLevel contestlevel : contestlevels )
      {
        contestlevel.setPayoutDesc( null );
      }

    }
    if ( attachContest.getActivityMeasureType() != null && detachSSIContest.getActivityMeasureType() != null
        && ( attachContest.getActivityMeasureType().isCurrency() && detachSSIContest.getActivityMeasureType().isUnit()
            || attachContest.getActivityMeasureType().isUnit() && detachSSIContest.getActivityMeasureType().isCurrency() ) )
    {
      List<SSIContestLevel> contestlevels = ssiContestDAO.getContestLevelsByContestId( contestId );
      for ( SSIContestLevel contestlevel : contestlevels )
      {
        contestlevel.setGoalAmount( null );
      }
    }

    mergeContest( detachSSIContest, BADGE_RULE_NOT_AVAILABLE, attachContest );

    if ( detachSSIContest.getIndividualBaselineType() != null && !detachSSIContest.getIndividualBaselineType().isNo() )
    {
      firstPageParticipants = getFirstPageParticipants( detachSSIContest );
    }
    return firstPageParticipants;
  }

  private List<SSIContestParticipant> getFirstPageParticipants( SSIContest detachSSIContest )
  {
    List<SSIContestParticipant> firstPageParticipants = this.ssiContestParticipantDAO
        .getContestParticipants( detachSSIContest.getId(), SSIContestUtil.FIRST_PAGE_NUMBER, SSIContestUtil.PAX_RECORDS_PER_PAGE, SSIContestUtil.SORT_BY_LAST_NAME, SSIContestUtil.DEFAULT_SORT_BY );
    return firstPageParticipants;
  }

  public SSIContestPayoutsValueBean getContestPayouts( Long contestId, Long contestActivityId, String sortColumnName, String sortBy, Integer rowNumStart, Integer rowNumEnd )
      throws ServiceErrorException
  {
    try
    {
      return ssiContestDAO.getContestPayouts( contestId, contestActivityId, sortColumnName, sortBy, rowNumStart, rowNumEnd );
    }
    catch( Exception e )
    {
      log.error( "<<Error>>" + e );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_PAYOUTS_DATA_ERR );
    }
  }

  public SSIContestPayoutsValueBean saveContestPaxPayouts( Long contestId,
                                                           String userIds,
                                                           String payoutAmounts,
                                                           String payoutDesc,
                                                           String sortColumnName,
                                                           String sortBy,
                                                           Integer rowNumStart,
                                                           Integer rowNumEnd )
      throws ServiceErrorException
  {
    try
    {
      return ssiContestDAO.saveContestPaxPayouts( contestId, userIds, payoutAmounts, payoutDesc, sortColumnName, sortBy, rowNumStart, rowNumEnd );
    }
    catch( Exception e )
    {
      log.error( "<<Error>>" + e );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_PAYOUTS_DATA_ERR );
    }
  }

  private void updateContestGoalPercentage( Long contestId ) throws ServiceErrorException
  {
    this.ssiContestDAO.updateContestGoalPercentage( contestId );
  }

  public void updateContestStackRank( Long contestId ) throws ServiceErrorException
  {
    this.ssiContestDAO.updateContestStackRank( contestId );
  }

  public boolean approveContestPayouts( Long contestId, Short awardIssuanceNumber, String csvUserIds, String csvPayoutAmounts ) throws ServiceErrorException
  {
    Long userId = UserManager.getUserId();
    boolean isApprovalPayoutSucceded = Boolean.FALSE;
    try
    {

      SSIContest contest = this.getContestById( contestId );
      String contestType = contest.getContestType().getCode();
      if ( SSIContestType.OBJECTIVES.equals( contestType ) || SSIContestType.DO_THIS_GET_THAT.equals( contestType ) || SSIContestType.STEP_IT_UP.equals( contestType ) )
      {
        isApprovalPayoutSucceded = callApproveContestPayouts( contest, userId, null, null, null );
      }
      else if ( SSIContestType.STACK_RANK.equals( contestType ) )
      {
        isApprovalPayoutSucceded = callApproveContestPayouts( contest, userId, null, csvUserIds, csvPayoutAmounts );
      }
      else if ( SSIContestType.AWARD_THEM_NOW.equals( contestType ) )
      {
        isApprovalPayoutSucceded = callApproveContestPayouts( contest, userId, awardIssuanceNumber, null, null );
      }
      // launch payout deposit process
      // launchContestPayoutsDepositProcess( contestId, userId, awardIssuanceNumber );
    }
    catch( ServiceErrorException e )
    {
      log.error( "Data Access Error, Stored procedure returned error" + "\n" );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_APPROVE_PAYOUT_ERR );
    }
    return isApprovalPayoutSucceded;

  }

  private boolean callApproveContestPayouts( SSIContest ssiContest, Long userId, Short awardIssuanceNumber, String csvUserIds, String csvPayoutAmounts ) throws ServiceErrorException
  {
    return this.ssiContestDAO.approveContestPayouts( ssiContest, userId, awardIssuanceNumber, csvUserIds, csvPayoutAmounts );
  }

  public void launchContestPayoutsDepositProcess( Long contestId, Short awardIssuanceNumber ) throws ServiceErrorException
  {
    Long userId = UserManager.getUserId();

    LinkedHashMap<String, String[]> parameterValueMap = new LinkedHashMap<String, String[]>();
    parameterValueMap.put( "contestId", new String[] { contestId.toString() } );
    parameterValueMap.put( "userId", new String[] { userId.toString() } );
    if ( awardIssuanceNumber != null )
    {
      parameterValueMap.put( "awardIssuanceNumber", new String[] { awardIssuanceNumber.toString() } );
    }
    parameterValueMap.put( "isSSIAdmin", new String[] { String.valueOf( UserManager.getUser().isSSIAdmin() ) } );
    Process process = getProcessService().createOrLoadSystemProcess( SSIContestPayoutDepositProcess.BEAN_NAME, SSIContestPayoutDepositProcess.BEAN_NAME );
    getProcessService().launchProcess( process, parameterValueMap, userId );
  }

  public List<SSIContest> getAllContestsToLaunch( SSIContestStatus contestStatus )
  {
    return ssiContestDAO.getAllContestsToLaunch( contestStatus );
  }

  public SSIContest saveContest( SSIContest contest )
  {
    return ssiContestDAO.saveContest( contest );
  }

  public void setUploadInProgress( Long contestId, boolean isUploadInProgress, String saveAndSendProgressUpdate )
  {
    SSIContest ssiContest = getContestById( contestId );
    ssiContest.setUploadInProgress( isUploadInProgress );
    ssiContest.setSaveAndSendProgressUpdate( saveAndSendProgressUpdate );
    saveContest( ssiContest );
  }

  public Map<String, Integer> getOpenContestCount( Long promotionId, List<String> contestTypes )
  {
    Map<String, Integer> openContests = new HashMap<String, Integer>();
    for ( String contestType : contestTypes )
    {
      openContests.put( contestType, ssiContestDAO.getOpenContestCount( promotionId, contestType ) );
    }
    return openContests;
  }

  @Override
  public Map<String, Double> getContestActivityTotals( Long contestId )
  {
    return ssiContestDAO.getContestActivityTotals( contestId );
  }

  @Override
  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy )
  {
    return this.ssiContestDAO.getContestManagers( contestId, pageNumber, SSIContestUtil.PAX_RECORDS_PER_PAGE, sortedOn, sortedBy );
  }

  @Override
  public List<SSIContestSuperViewer> getContestSuperViewers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy )
  {
    return this.ssiContestDAO.getContestSuperViewers( contestId, pageNumber, SSIContestUtil.PAX_RECORDS_PER_PAGE, sortedOn, sortedBy );
  }

  public Long getHighestLevelPayout( Long contestId )
  {
    return this.ssiContestDAO.getHighestLevelPayout( contestId );
  }

  @Override
  public List<SSIContestManager> getContestManagersWithAssociations( Long contestId, Integer currentPage, String sortedOn, String sortedBy, AssociationRequestCollection associationRequestCollection )
  {
    List<SSIContestManager> contestManagers = this.getContestManagers( contestId, currentPage, sortedOn, sortedBy );
    if ( associationRequestCollection != null )
    {
      for ( SSIContestManager contestManager : contestManagers )
      {
        for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = iterator.next();
          req.execute( contestManager.getManager() );
        }
      }
    }
    return contestManagers;
  }

  @Override
  public List<SSIContestSuperViewer> getContestSuperViewersWithAssociations( Long contestId,
                                                                             Integer currentPage,
                                                                             String sortedOn,
                                                                             String sortedBy,
                                                                             AssociationRequestCollection associationRequestCollection )
  {
    List<SSIContestSuperViewer> contestSuperViewers = this.getContestSuperViewers( contestId, currentPage, sortedOn, sortedBy );
    if ( associationRequestCollection != null )
    {
      for ( SSIContestSuperViewer contestSuperViewer : contestSuperViewers )
      {
        for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = iterator.next();
          req.execute( contestSuperViewer.getSuperViewer() );
        }
      }
    }
    return contestSuperViewers;
  }

  @Override
  public List<DepositProcessBean> getContestUserJournalList( Long ssiContestId, Short awardIssuanceNumber )
  {
    return ssiContestDAO.getContestUserJournalList( ssiContestId, awardIssuanceNumber );
  }

  public int getBadgeCountInSsiContest( Long promotionId, Long badgeRuleId )
  {
    return ssiContestDAO.getBadgeCountInSsiContest( promotionId, badgeRuleId );
  }

  public void finalizeContest( Long contestId )
  {
    SSIContest contest = ssiContestDAO.getContestById( contestId );
    contest.setStatus( SSIContestStatus.lookup( SSIContestStatus.FINALIZE_RESULTS ) );
    ssiContestDAO.saveContest( contest );
  }

  @Override
  public Map<String, Object> getContestUserManagersForSelectedPax( Long contestId, Long paxId ) throws ServiceErrorException
  {
    Map<String, Object> paxManagers = ssiContestDAO.getSSIContestUserManagerList( contestId, paxId );
    /*
     * int managersCount = (Integer)paxManagers.get( "managerCount" ); if ( managersCount > 300 ) {
     * log.error( "Procedure returned more than 300 managers" ); throw new ServiceErrorException(
     * "ssi_contest.pax.manager.MORE_MANAGERS_FOUND" ); }
     */
    return paxManagers;
  }

  public SSIContestClaimField getContestClaimFieldById( Long contestClaimFieldId )
  {
    return ssiContestDAO.getContestClaimFieldById( contestClaimFieldId );
  }

  @Override
  public List<SSIContestClaimField> getContestClaimFieldsByContestId( Long contestId )
  {
    return ssiContestDAO.getContestClaimFieldsByContestId( contestId );
  }

  public void setSsiContestDAO( SSIContestDAO ssiContestDAO )
  {
    this.ssiContestDAO = ssiContestDAO;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setGamificationDAO( GamificationDAO gamificationDAO )
  {
    this.gamificationDAO = gamificationDAO;
  }

  public void setSsiContestParticipantDAO( SSIContestParticipantDAO ssiContestParticipantDAO )
  {
    this.ssiContestParticipantDAO = ssiContestParticipantDAO;
  }

  public void setAlertMessageService( AlertMessageService alertMessageService )
  {
    this.alertMessageService = alertMessageService;
  }

  public void setParticipantAlertService( ParticipantAlertService participantAlertService )
  {
    this.participantAlertService = participantAlertService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  protected ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }

  @Override
  public int launchContestArchivalProcess( SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    int totalContestArchived = 0;
    try
    { // archive non- atn Contest
      int nonAtnContestArchievedCount = launchNonAtnContestArchivalProcess( getArchivalDate( ssiPromotion.getDaysToArchive() ) );
      // archive ATN Contest with/without approver
      int atnContestArchievedCount = launchAtnContestArchivalProcess( ssiPromotion );
      totalContestArchived = nonAtnContestArchievedCount + atnContestArchievedCount;
    }
    catch( SQLException e )
    {
      log.error( "launchContestArchivalProcess error" + e );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_ARCHIVAL_PROCESS_ERR );
    }
    return totalContestArchived;
  }

  private int launchAtnContestArchivalProcess( SSIPromotion ssiPromotion ) throws SQLException
  {
    int lastDayOfProcessExecutionDate = 1;// archived contest which is older than today.
    int totalAtnContestArchievedCount = 0;
    if ( ssiPromotion.getContestApprovalLevels() != 0 )
    {
      List<SSIContest> ssiContests = this.ssiContestDAO.getExpiredAwardThemNowContest( getArchivalDate( lastDayOfProcessExecutionDate ) );

      if ( ssiContests != null && !ssiContests.isEmpty() )
      {
        for ( SSIContest ssiContest : ssiContests )
        {
          int issuanceWaitingforApprovalCount = this.ssiContestDAO.getWaitingforApprovalAwardThemNowIssuancesCount( ssiContest.getId() );
          if ( issuanceWaitingforApprovalCount == 0 )
          {
            int atnContestarchivedWithApproval = this.ssiContestDAO.launchATNContestArchivalProcessWithApprover( ssiContest.getId() );
            totalAtnContestArchievedCount = totalAtnContestArchievedCount + atnContestarchivedWithApproval;
          }

        }
      }
    }
    else
    {
      int atnContestarchivedWithoutApproval = this.ssiContestDAO.launchATNContestArchivalProcessWithoutApprover( getArchivalDate( lastDayOfProcessExecutionDate ) );
      totalAtnContestArchievedCount = totalAtnContestArchievedCount + atnContestarchivedWithoutApproval;
    }
    return totalAtnContestArchievedCount;
  }

  private int launchNonAtnContestArchivalProcess( java.sql.Date archivalDate ) throws ServiceErrorException
  {
    try
    {
      return this.ssiContestDAO.launchNonATNContestArchivalProcess( archivalDate );
    }
    catch( SQLException sqlException )
    {
      log.error( "Sql error while launching non atn contest archival process: " + sqlException );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_ARCHIVAL_PROCESS_ERR );
    }
  }

  private java.sql.Date getArchivalDate( int archivalPeriod )
  {
    Calendar calendar = (Calendar)Calendar.getInstance().clone();
    calendar.setTime( new Date() );
    calendar.add( Calendar.DAY_OF_YEAR, -archivalPeriod );
    return new java.sql.Date( calendar.getTime().getTime() );
  }

  public List<SSIContestStackRankPayoutValueBean> getContestSRPayoutList( Long contestId ) throws ServiceErrorException
  {
    try
    {
      List<SSIContestStackRankPayoutValueBean> contestProgressSrPayouts = new ArrayList<SSIContestStackRankPayoutValueBean>();
      Map<String, Object> output = this.ssiContestDAO.getContestSRPayoutList( contestId );
      contestProgressSrPayouts = (List<SSIContestStackRankPayoutValueBean>)output.get( "contestSrPayoutsData" );
      return contestProgressSrPayouts;
    }
    catch( Exception e )
    {
      log.error( "<<Error>>" + e );
      throw new ServiceErrorException( ServiceErrorMessageKeys.SSI_CONTEST_PROGRESS_DATA_ERR );
    }
  }

  @Override
  public void resetUpdateInProgressFlag( Long contestId, boolean isUpdateInProgress )
  {
    SSIContest ssiContest = getContestById( contestId );
    ssiContest.setUpdateInProgress( isUpdateInProgress );
    saveContest( ssiContest );

  }

  protected String getSysUrl()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public SSIContestPaxPayout getPaxPayout( Long contestId, Long userId )
  {
    return ssiContestDAO.getPaxPayout( contestId, userId );
  }

  private String getCreatotRoleLabel()
  {
    return cmAssetService.getString( "ssi_contest.creator", "CREATOR", getUserLocale(), true );
  }

  private String getApproverRoleLabel()
  {
    return cmAssetService.getString( "claims.product.approval", "APPROVER", getUserLocale(), true );
  }

  protected Locale getUserLocale()
  {
    return UserManager.getLocale();
  }

  public List<SSIContest> getContestSearchByAdmin( List<String> assetCodes, List<Long> userIDs, String ssiContestStatus, Date startDate, Date endDate )
  {
    return ssiContestDAO.getContestSearchByAdmin( assetCodes, userIDs, ssiContestStatus, startDate, endDate );
  }

  @Override
  public void saveAdminAction( Long contestID, Long userID, String action, String desc )
  {
    SSIAdminContestActions ssiAdminContestActions = new SSIAdminContestActions();
    ssiAdminContestActions.setUserID( userID );
    ssiAdminContestActions.setContestID( contestID );
    ssiAdminContestActions.setAction( action );
    ssiAdminContestActions.setDescription( desc );
    ssiContestDAO.saveAdminAction( ssiAdminContestActions );
    // Send mail to Original Creator
    if ( action.equals( SSIAdminContestActions.EDIT_CONTEST ) )
    {
      Mailing createMailing = mailingService.buildSSIContestEditNotification( getContestById( contestID ), null );
      mailingService.submitMailing( createMailing, null );
    }
  }

  @Override
  public int getWaitingforApprovalAwardThemNowIssuancesCount( Long ssiID )
  {
    return ssiContestDAO.getWaitingforApprovalAwardThemNowIssuancesCount( ssiID );
  }

  @Override
  public SSIAdminContestActions getAdminActionByEditCreator( Long contestID )
  {
    return ssiContestDAO.getAdminActionByEditCreator( contestID );
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  @Override
  public Map getContsetPaxManagerSVExtract( Map<String, Object> reportParameters, String ssiType )
  {
    return ssiContestDAO.getContsetPaxManagerSVExtract( reportParameters, ssiType );
  }

  @Override
  public Map getContsetErrorExtract( Map<String, Object> reportParameters )
  {
    return ssiContestDAO.getContsetErrorExtract( reportParameters );
  }

  @Override
  public List<NameableBean> getSSIContestList( String contestType )
  {
    return ssiContestDAO.getSSIContestList( contestType );
  }

  @Override
  public Map<String, Object> verifyImportFile( Long importFileId, Long contestId, String contestType )
  {
    return ssiContestDAO.verifyImportFile( importFileId, "V", contestId, contestType );
  }

  @Override
  public Map<String, Object> importImportFile( Long importFileId, Long contestId, String contestType )
  {
    return ssiContestDAO.verifyImportFile( importFileId, "L", contestId, contestType );
  }

  @Override
  public List<SSIContestBillCodeBean> getContestBillCodesByContestId( Long contestId )
  {

    Map<String, SSIContestBillCodeBean> contestBillCodeMap = new HashMap<String, SSIContestBillCodeBean>();
    SSIContest contest = getContestById( contestId );

    List<SSIContestBillCodeBean> contestBillCodesList = ssiContestDAO.getContestBillCodesByContestId( contest.getId() );
    List<SSIContestBillCodeBean> promoBillCodesList = getBillCodesByPromoId( contest.getPromotion().getId() );
    contestBillCodesList.stream().forEach( e -> contestBillCodeMap.put( e.getSortOrder() + "_" + e.getBillCode(), e ) );

    for ( SSIContestBillCodeBean promoBill : promoBillCodesList )
    {
      SSIContestBillCodeBean contestBillCode = contestBillCodeMap.get( promoBill.getSortOrder() + "_" + promoBill.getBillCode() );
      promoBill.setPromoBillCodeOrder( promoBill.getSortOrder() );
      promoBill.setSortOrder( null );
      if ( Objects.nonNull( contestBillCode ) )
      {
        if ( !StringUtil.isNullOrEmpty( contestBillCode.getCustomValue() ) )
        {
          promoBill.setCustomValue( contestBillCode.getCustomValue() );
        }
        promoBill.setSortOrder( contestBillCode.getSortOrder() );
      }
    }
    return promoBillCodesList;
  }

  @Override
  public List<SSIContestBillCodeBean> getBillCodesByPromoId( Long promoId )
  {
    return ssiContestDAO.getBillCodesByPromoId( promoId );
  }

  @Override
  public boolean isContestCreator( Long userId )
  {
    return ssiContestDAO.fetchContestCreatorCount( userId );
  }

  @Override
  public void updateContestParticipants( SSIContestParticipant contestParticipant )
  {
    ssiContestDAO.updateContestParticipants( contestParticipant );
  }

  @Override
  public void updateContestManagers( SSIContestManager contestManager )
  {
    ssiContestDAO.updateContestManagers( contestManager );
  }

  public List<SSIContestParticipant> getSSIContestATNParticipants( Long ssiContestId, Short awardIssuanceNumber )
  {
    return ssiContestDAO.getSSIContestATNParticipants( ssiContestId, awardIssuanceNumber );
  }

  public List<SSIContestParticipant> getSSIContestParticipants( Long ssiContestId )
  {
    return ssiContestDAO.getSSIContestParticipants( ssiContestId );
  }
  
}
