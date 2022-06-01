
package com.biperf.core.process;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.managertoolkit.ParticipantAlertService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestProgressLoadNotificationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( SSIContestProgressLoadNotificationProcess.class );

  public static final String PROCESS_NAME = "SSI Contest Progress Load Notification Process";
  public static final String BEAN_NAME = "ssiContestProgressLoadNotificationProcess";

  private static final int STACK_RANK_CURRENT_PAGE = 1;
  private static final int TOTAL_STACK_RANK_RECORDS_PER_PAGE = 3;

  private SSIContestParticipantService ssiContestParticipantService;
  private SSIContestService ssiContestService;
  private GamificationService gamificationService;
  private ParticipantAlertService participantAlertService;
  private MailingService mailingService;
  private CMAssetService cmAssetService;
  private ParticipantService participantService;
  private PromotionService promotionService;

  private Long contestId;
  private Long importFileId;
  private int totalRecords;
  private int totalProcessedRecords;
  private String isSSIAdmin = "false";

  protected void onExecute()
  {
    try
    {
      if ( isFileLoad() )
      {
        addComment( "SSI Contest Progress Load - data loading completed for contestId: " + contestId + " importFileId: " + importFileId + " totalRecords: " + totalRecords + " totalProcessedRecords: "
            + totalProcessedRecords );
      }
      addComment( "Progress Load notification process is starting. " );

      sendContestProgressLoadNotifications( contestId, importFileId, totalProcessedRecords );

      addComment( "Progress Load notification process  is completed. Progress Load completed successfully. " );
    }
    catch( ServiceErrorException e )
    {
      logErrorMessage( e );
      log.error( e.getMessage(), e );
      addComment( "An exception occurred while uploading contest progress for contestId: " + contestId + " importFileId: " + importFileId );
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      log.error( e.getMessage(), e );
      addComment( "An exception occurred while uploading contest progress for contestId: " + contestId + " importFileId: " + importFileId );
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  private void sendContestProgressLoadNotifications( Long contestId, Long importFileId, int totalProcessedRecords ) throws ServiceErrorException
  {
    SSIContest ssiContest = getContest();
    SSIPromotion ssiPromotion = getPromtoion( ssiContest );

    // participant notifications
    sendParticipantNotifications( ssiContest, ssiPromotion );
    SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = ssiContestParticipantService.performUniqueCheck( ssiContest.getId() );
    // manager notifications
    sendManagerNotifications( ssiContest, contestUniqueCheckValueBean, ssiPromotion );
    // creator notifications
    sendCreatorNotifications( ssiContest, contestUniqueCheckValueBean, ssiPromotion );
  }

  private boolean isFileLoad()
  {
    return importFileId != null ? true : false;
  }

  private SSIContest getContest()
  {
    AssociationRequestCollection ssiContestAssociationRequest = new AssociationRequestCollection();
    ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_MANAGERS ) );
    ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_PARTICIPANTS ) );
    SSIContest ssiContest = ssiContestService.getContestByIdWithAssociations( contestId, ssiContestAssociationRequest );
    return ssiContest;
  }

  private SSIPromotion getPromtoion( SSIContest ssiContest )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    SSIPromotion ssiPromotion = (SSIPromotion)promotionService.getPromotionByIdWithAssociations( ssiContest.getPromotion().getId(), associationRequestCollection );
    return ssiPromotion;
  }

  private void sendParticipantNotifications( SSIContest ssiContest, SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    BadgeRule badgeRule = ssiContest.getBadgeRule();
    String badgeImageUrlSuffix = "";
    String badgeName = "";
    if ( badgeRule != null )
    {
      badgeImageUrlSuffix = gamificationService.getBadgeImageUrlSuffix( badgeRule );
      badgeName = badgeRule.getBadgeNameTextFromCM();
    }
    if ( isFileLoad() )
    {
      sendFileLoadParticipantNotifications( ssiContest, badgeName, badgeImageUrlSuffix, ssiPromotion );
    }
    else
    {
      sendOnlineParticipantNotifications( ssiContest, badgeName, badgeImageUrlSuffix, ssiPromotion );
    }
  }

  private void sendFileLoadParticipantNotifications( SSIContest ssiContest, String badgeName, String badgeImageUrlSuffix, SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    List<Long> paxIds = ssiContestParticipantService.getContestProgressLoadParticipantIdsByImportFileId( contestId, importFileId );
    int paxEmailCount = 0;
    for ( Iterator<Long> iter = paxIds.iterator(); iter.hasNext(); )
    {
      Long paxId = (Long)iter.next();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
      Participant participant = (Participant)participantService.getParticipantByIdWithAssociations( paxId, associationRequestCollection );
      if ( participant.getPrimaryEmailAddress() != null )
      {
        sendContestProgressLoadCompleteParticipantEmail( ssiContest, participant, badgeName, badgeImageUrlSuffix, ssiPromotion );
        paxEmailCount++;
      }
      sendProgressLoadAlert( ssiContest, participant, SSIContest.CONTEST_ROLE_PAX );
    }
    log.debug( "Contest Progress Load Notification, participant emails sent count: " + paxEmailCount + " for contest id: " + contestId + " ,importFileId: " + importFileId );
    addComment( "Contest Progress Load Notification, participant emails sent count: " + paxEmailCount + " for contest id: " + contestId + " ,importFileId: " + importFileId );
  }

  private void sendOnlineParticipantNotifications( SSIContest ssiContest, String badgeName, String badgeImageUrlSuffix, SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    Set<SSIContestParticipant> contestParticipants = ssiContest.getContestParticipants();
    int paxEmailCount = 0;
    for ( Iterator<SSIContestParticipant> iter = contestParticipants.iterator(); iter.hasNext(); )
    {
      SSIContestParticipant contestParticipant = (SSIContestParticipant)iter.next();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
      Participant participant = (Participant)participantService.getParticipantByIdWithAssociations( contestParticipant.getParticipant().getId(), associationRequestCollection );
      if ( participant.getPrimaryEmailAddress() != null )
      {
        sendContestProgressLoadCompleteParticipantEmail( ssiContest, participant, badgeName, badgeImageUrlSuffix, ssiPromotion );
        paxEmailCount++;
      }
      sendProgressLoadAlert( ssiContest, participant, SSIContest.CONTEST_ROLE_PAX );
    }
    log.debug( "Contest Progress Load Notification, participant emails sent count: " + paxEmailCount + " for contest id: " + contestId );
    addComment( "Contest Progress Load Notification, participant emails sent count: " + paxEmailCount + " for contest id: " + contestId );
  }

  private void sendManagerNotifications( SSIContest ssiContest, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    int mgrEmailCount = 0;
    for ( Iterator<SSIContestManager> iter = ssiContest.getContestManagers().iterator(); iter.hasNext(); )
    {
      SSIContestManager contestManager = (SSIContestManager)iter.next();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
      Participant manager = (Participant)participantService.getParticipantByIdWithAssociations( contestManager.getManager().getId(), associationRequestCollection );
      if ( manager.getPrimaryEmailAddress() != null )
      {
        sendProgressLoadManagerEmail( ssiContest, manager, contestUniqueCheckValueBean, ssiPromotion );
        mgrEmailCount++;
      }
      sendProgressLoadAlert( ssiContest, manager, SSIContest.CONTEST_ROLE_MGR );
    }
    if ( isFileLoad() )
    {
      log.debug( "Contest Progress Load Notification, manager emails sent count: " + mgrEmailCount + " for contest id: " + contestId + " ,importFileId: " + importFileId );
      addComment( "Contest Progress Load Notification, manager emails sent count: " + mgrEmailCount + " for contest id: " + contestId + " ,importFileId: " + importFileId );
    }
    else
    {
      log.debug( "Contest Progress Load Notification, manager emails sent count: " + mgrEmailCount + " for contest id: " + contestId );
      addComment( "Contest Progress Load Notification, manager emails sent count: " + mgrEmailCount + " for contest id: " + contestId );
    }
  }

  private void sendCreatorNotifications( SSIContest ssiContest, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    if ( isFileLoad() )
    {
      // creator notifications
      Participant creator = participantService.getParticipantById( ssiContest.getCreatorId() );
      sendProgressLoadCreatorEmailAndAlert( ssiContest, creator, contestUniqueCheckValueBean, ssiPromotion );
      log.debug( "Contest Progress Load Notification, creator emails sent for contest id: " + contestId + " ,importFileId: " + importFileId );
      addComment( "Contest Progress Load Notification, creator emails sent for contest id: " + contestId + " ,importFileId: " + importFileId );

      sendContestProgressLoadCompleteUpdateNotification( ssiContest, creator, importFileId, null, totalRecords, totalProcessedRecords, ssiPromotion );
      log.debug( "Contest Progress Load Notification, creator load complete notification udpate email sent for contest id: " + contestId + " ,importFileId: " + importFileId + " ,totalRecords: "
          + totalRecords );
      // send load complete notification to creator
      addComment( "Contest Progress Load Notification, creator load complete notification udpate email sent for contest id: " + contestId + " ,importFileId: " + importFileId
          + " ,totalProcessedRecords: " + totalProcessedRecords );
      // update status
      updateContestData( ssiContest );
    }
  }

  // SSI Progress Load
  private void sendProgressLoadAlert( SSIContest ssiContest, Participant participant, String role ) throws ServiceErrorException
  {
    AlertMessage alertMessage = new AlertMessage();
    alertMessage.setSubject( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_PROGRESS_LOAD_SUBJECT" ) );
    // alertMessage.setExpiryDate( ssiContest.getEndDate() );
    alertMessage.setExpiryDate( DateUtils.addDays( new Date(), 1 ) );
    String contestName = cmAssetService.getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, Locale.US, true );
    alertMessage.setMessage( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_PROGRESS_LOAD_MESG" ), new Object[] { contestName } ) );
    alertMessage.setContestId( ssiContest.getId() );
    alertMessage.setSsiAlertType( role );

    alertMessage.setMessageTo( String.valueOf( participant.getId() ) );
    ParticipantAlert participantAlert = new ParticipantAlert();
    participantAlert.setUser( participant );
    participantAlert.setAlertMessage( alertMessage );
    this.participantAlertService.saveParticipantAlert( participantAlert );
  }

  private void sendProgressLoadCreatorEmailAndAlert( SSIContest ssiContest, Participant creator, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIPromotion ssiPromotion )
      throws ServiceErrorException
  {
    sendProgressLoadCreatorEmail( ssiContest, creator, contestUniqueCheckValueBean, ssiPromotion, isSSIAdmin );
    sendProgressLoadAlert( ssiContest, creator, SSIContest.CONTEST_ROLE_CREATOR );
  }

  /**
   * 
   * @param ssiContest
   * @param participant
   * @param importFileId
   * @param contestUniqueCheckValueBean
   */
  private void sendProgressLoadManagerEmail( SSIContest ssiContest, Participant manager, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIPromotion ssiPromotion )
      throws ServiceErrorException
  {
    List<SSIContestProgressValueBean> contestProgressData = null;
    List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList = new ArrayList<SSIContestStackRankPaxValueBean>();
    Long managerId = manager.getId();
    try
    {
      contestProgressData = ssiContestParticipantService.getContestProgress( ssiContest.getId(), managerId );

      if ( contestProgressData != null && contestProgressData.size() > 0 )
      {
        Mailing mailing = mailingService.buildContestProgressLoadManagerMailing( ssiContest,
                                                                                 manager,
                                                                                 contestUniqueCheckValueBean,
                                                                                 contestProgressData,
                                                                                 ssiContestStackRankPaxValueBeanList,
                                                                                 ssiPromotion );
        if ( mailing != null )
        {
          mailingService.submitMailing( mailing, null );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.error( "<<Error>> Error sending ssi contest creator and manager email " + e.getMessage(), e );
    }
  }

  /**
  * 
  * @param ssiContest
  * @param participant
  * @param importFileId
  * @param contestUniqueCheckValueBean
  */
  private void sendProgressLoadCreatorEmail( SSIContest ssiContest, Participant creator, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIPromotion ssiPromotion, String isSSIAdmin )
      throws ServiceErrorException
  {
    List<SSIContestProgressValueBean> contestProgressData = null;
    List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList = new ArrayList<SSIContestStackRankPaxValueBean>();

    try
    {
      contestProgressData = ssiContestParticipantService.getContestProgress( ssiContest.getId(), null );
      if ( SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) )
      {
        ssiContestStackRankPaxValueBeanList = ssiContestParticipantService.getContestStackRank( ssiContest.getId(),
                                                                                                null,
                                                                                                null,
                                                                                                STACK_RANK_CURRENT_PAGE,
                                                                                                TOTAL_STACK_RANK_RECORDS_PER_PAGE,
                                                                                                false,
                                                                                                true );
      }

      if ( contestProgressData != null && contestProgressData.size() > 0 )
      {
        Mailing mailing = mailingService
            .buildContestProgressLoadCreatorMailing( ssiContest, creator, contestUniqueCheckValueBean, contestProgressData, ssiContestStackRankPaxValueBeanList, ssiPromotion, isSSIAdmin );
        if ( mailing != null )
        {
          mailingService.submitMailing( mailing, null );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.error( "<<Error>> Error sending ssi contest creator and manager email " + e.getMessage(), e );
    }
  }

  /**
   * 
   * @param ssiContest
   * @param participant
   * @param badgeName
   * @param badgeImageUrlSuffix
   */
  private void sendContestProgressLoadCompleteParticipantEmail( SSIContest ssiContest, Participant participant, String badgeName, String badgeImageUrlSuffix, SSIPromotion ssiPromotion )
      throws ServiceErrorException
  {
    SSIContestPaxProgressDetailValueBean paxProgressValueBean = null;
    try
    {
      paxProgressValueBean = ssiContestParticipantService.getContestParticipantProgress( ssiContest.getId(), participant.getId() );
      if ( paxProgressValueBean != null )
      {
        Mailing mailing = mailingService.buildContestProgressLoadParticipantMailing( ssiContest, participant, paxProgressValueBean, badgeImageUrlSuffix, ssiPromotion );
        if ( mailing != null )
        {
          mailingService.submitMailing( mailing, null );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.error( "<<Error>> Error sending ssi contest participant email " + e.getMessage(), e );
    }
  }

  private void updateContestData( SSIContest ssiContest ) throws ServiceErrorException
  {
    // Setting activity date in procedure for fileload since we updating same in online update
    // ssiContest.setLastProgressUpdateDate( com.biperf.core.utils.DateUtils.getCurrentDate() );
    ssiContest.setUploadInProgress( Boolean.FALSE );
    ssiContestService.saveContest( ssiContest );
  }

  private void sendContestProgressLoadCompleteUpdateNotification( SSIContest ssiContest,
                                                                  Participant creator,
                                                                  Long importFileId,
                                                                  String fileName,
                                                                  int totalRecords,
                                                                  int totalProcessedRecords,
                                                                  SSIPromotion ssiPromotion )
      throws ServiceErrorException
  {
    Mailing mailing = mailingService.buildContestProgressLoadCompleteCreatorMailing( ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, ssiPromotion, isSSIAdmin );
    if ( mailing != null )
    {
      mailingService.submitMailing( mailing, null );
    }
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Long getImportFileId()
  {
    return importFileId;
  }

  public void setImportFileId( Long importFileId )
  {
    this.importFileId = importFileId;
  }

  public int getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( int totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public int getTotalProcessedRecords()
  {
    return totalProcessedRecords;
  }

  public void setTotalProcessedRecords( int totalProcessedRecords )
  {
    this.totalProcessedRecords = totalProcessedRecords;
  }

  public void setSsiContestParticipantService( SSIContestParticipantService ssiContestParticipantService )
  {
    this.ssiContestParticipantService = ssiContestParticipantService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantAlertService( ParticipantAlertService participantAlertService )
  {
    this.participantAlertService = participantAlertService;
  }

  public String getIsSSIAdmin()
  {
    return isSSIAdmin;
  }

  public void setIsSSIAdmin( String isSSIAdmin )
  {
    this.isSSIAdmin = isSSIAdmin;
  }
}
