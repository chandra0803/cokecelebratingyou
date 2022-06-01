
package com.biperf.core.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAwardThemNowService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankTeamValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIPaxDTGTActivityProgressValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestPayoutDepositProcess extends SSIContestBaseProcess
{
  private static final Log log = LogFactory.getLog( SSIContestPayoutDepositProcess.class );

  public static final String PROCESS_NAME = "SSI Contest Payout Deposit Process";
  public static final String BEAN_NAME = "ssiContestPayoutDepositProcess";

  private static final int CREATOR_AUDIENCE_TYPE = 1;
  private static final int MANAGER_AUDIENCE_TYPE = 2;
  private static final int PAX_AUDIENCE_TYPE = 3;

  private SSIContestAwardThemNowService ssiContestAwardThemNowService;
  private JournalService journalService;
  private NodeService nodeService;

  private Long contestId;
  private Short awardIssuanceNumber;

  @SuppressWarnings( "unchecked" )
  protected void onExecute()
  {
    try
    {
      log.debug( "starting SSIContestPayoutDepositProcess onExecute....with contestId: " + contestId + ", awardIssuanceNumber:" + awardIssuanceNumber );
      AssociationRequestCollection assocReqColl = new AssociationRequestCollection();
      assocReqColl.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_PARTICIPANTS ) );
      assocReqColl.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_MANAGERS ) );
      SSIContest ssiContest = ssiContestService.getContestByIdWithAssociations( contestId, assocReqColl );

      Participant creator = getContestCreator( ssiContest );
      List<PromotionNotificationType> promotionNotifications = getPromotionNotificationsByPromotionId( ssiContest );
      log.debug( "promotionNotifications size...." + promotionNotifications.size() );
      SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = getContestUniqueCheckValueBean( ssiContest );

      List<DepositProcessBean> userJournalList = ssiContestService.getContestUserJournalList( contestId, awardIssuanceNumber );
      log.debug( "userJournalList size...." + userJournalList.size() );

      int payOutDepositCount = 0;
      int paxEmailCount = 0;
      int mgrEmailCount = 0;
      int creatorEmailCount = 0;
      boolean payOutDeposited = false;
      boolean nonWinner = false;
      Long participantId = null;
      PromotionNotificationType paxFinalResultNotificiation = getFinalResultNotification( ssiContest, PAX_AUDIENCE_TYPE, promotionNotifications );
      Set<Participant> eligibleManagers = new HashSet<Participant>();
      List<SSIContestParticipant> ssiContestParticipantList = null;

      if ( ssiContest.getContestType().isAwardThemNow() )
      {
        ssiContestParticipantList = ssiContestService.getSSIContestATNParticipants( ssiContest.getId(), awardIssuanceNumber );
      }
      else
      {
        ssiContestParticipantList = ssiContestService.getSSIContestParticipants( ssiContest.getId() );
      }

      for ( SSIContestParticipant ssiContestParticipant : ssiContestParticipantList )
      {
        participantId = ssiContestParticipant.getParticipant().getId();
        DepositProcessBean entry = participantInWinnersList( participantId, userJournalList );
        if ( entry != null )
        {
          Long journalId = entry.getJournalId();
          Long userId = entry.getParticipantId();
          payOutDeposited = false;

          // payout deposit
          if ( ssiContest.getPayoutType().isPoints() )
          {
            if ( journalId != null )
            {
              if ( depositPayout( journalId ) )
              {
                payOutDeposited = true;
                payOutDepositCount++;
              }
              else
              {
                log.error( "Deposit Failed for userId: " + userId + ", journalId: " + journalId );
                logErrorMessage( new Exception( "Deposit Failed for userId: " + userId + ", journalId: " + journalId ) );
              }
            }
            else
            {
              log.error( "No journal Entry Exists for userId: " + userId );
              logErrorMessage( new Exception( "No journal Entry Exists for userId: " + userId ) );
            }
          }
        }
        else
        {
          nonWinner = true;
        }
        // send pax notification
        if ( ( ssiContest.getPayoutType().isOther() || payOutDeposited || nonWinner ) && paxFinalResultNotificiation != null )
        {
          // RELOAD contest for contest change
          if ( !ssiContest.getStatus().isFinalizeResults() )
          {
            ssiContest = ssiContestService.getContestByIdWithAssociations( contestId, assocReqColl );
          }
          if ( ssiContestParticipant != null )
          {
            if ( ssiContest.getContestType().isAwardThemNow() )
            {
              createAwardIssuanceNotificationForParticipants( ssiContest, ssiContestParticipant, paxFinalResultNotificiation, creator );
            }
            else
            {
              createContestFinalResultNotificationForParticipants( ssiContest, ssiContestParticipant, paxFinalResultNotificiation, creator );
            }
            paxEmailCount++;
          }
        }
        if ( ssiContestParticipant != null )
        {
          buildManagers( ssiContest, ssiContestParticipant, eligibleManagers );
        }
      }

      if ( ssiContest.getPayoutType().isOther() || payOutDepositCount == userJournalList.size() )
      {
        // RELOAD contest
        ssiContest = ssiContestService.getContestByIdWithAssociations( contestId, assocReqColl );

        PromotionNotificationType ceatorFinalResultNoticiation = getFinalResultNotification( ssiContest, CREATOR_AUDIENCE_TYPE, promotionNotifications );
        PromotionNotificationType managerFinalResultNoticiation = getFinalResultNotification( ssiContest, MANAGER_AUDIENCE_TYPE, promotionNotifications );
        // send creator notification
        if ( ceatorFinalResultNoticiation != null )
        {
          if ( ssiContest.getContestType().isAwardThemNow() )
          {
            createAwardIssuanceNotificationForCreator( ssiContest, ceatorFinalResultNoticiation, creator );
          }
          else
          {
            createContestFinalResultNotificationForCreator( ssiContest, ceatorFinalResultNoticiation, creator, contestUniqueCheckValueBean );
          }
          creatorEmailCount++;
        }
        // send manager notification
        if ( managerFinalResultNoticiation != null )
        {
          {
            if ( ssiContest.getContestType().isAwardThemNow() )
            {
              mgrEmailCount = createAwardIssuanceNotificationForManagers( ssiContest, managerFinalResultNoticiation, creator );
            }
            else
            {
              mgrEmailCount = createContestFinalResultNotificationForManagers( ssiContest, managerFinalResultNoticiation, creator, contestUniqueCheckValueBean, eligibleManagers );
            }
          }
        }
      }
      addComment( "SSIContestPayoutDepositProcess completed successfully for contestId: " + contestId + " ,Total deposit processed: " + payOutDepositCount + " ,Total Participants received email: "
          + paxEmailCount + " ,Total Manager emails were sent: " + mgrEmailCount + " ,Total Creator emails were sent: " + creatorEmailCount );
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      log.error( " exception in SSIContestPayoutDepositProcess onExecute: " + e );
      e.printStackTrace( System.out );
      log.error( e.getMessage(), e );
      addComment( "An exception occurred while depositing contest payouts, contestId: " + contestId + " awardIssuanceNumber: " + awardIssuanceNumber );
    }
  }

  private DepositProcessBean participantInWinnersList( Long participantId, List<DepositProcessBean> userJournalList )
  {
    DepositProcessBean validWinner = null;
    for ( DepositProcessBean entry : userJournalList )
    {
      if ( entry.getParticipantId().equals( participantId ) )
      {
        validWinner = entry;
      }
    }
    return validWinner;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private void buildManagers( SSIContest ssiContest, SSIContestParticipant ssiParticipant, Set eligibleManagers ) throws ServiceErrorException
  {
    Participant participant = ssiParticipant.getParticipant();
    Map<String, Object> paxManagers = ssiContestService.getContestUserManagersForSelectedPax( ssiContest.getId(), participant.getId() );
    if ( paxManagers != null )
    {
      List<Long> managerIdList = (List<Long>)paxManagers.get( "managerList" );
      if ( managerIdList != null )
      {
        for ( Long managerId : managerIdList )
        {
          eligibleManagers.add( participantService.getParticipantById( managerId ) );
        }
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  private boolean depositPayout( Long journalId ) throws ServiceErrorException
  {
    AssociationRequestCollection assocs = new AssociationRequestCollection();
    assocs.add( new JournalAssociationRequest( JournalAssociationRequest.ALL ) );
    Journal journal = journalService.getJournalById( journalId, assocs );
    return journal != null && journalService.depositJournalEntry( journal, false );
  }

  private PromotionNotificationType getFinalResultNotification( SSIContest contest, final int audienceType, List<PromotionNotificationType> promotionNotifications )
  {

    SSIContestType contestType = contest.getContestType();
    String promotionEmailNotificationType = null;
    switch ( audienceType )
    {
      case CREATOR_AUDIENCE_TYPE:
        if ( contestType.isObjectives() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES;
        }
        else if ( contestType.isStepItUp() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP;
        }
        else if ( contestType.isDoThisGetThat() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT;
        }
        else if ( contestType.isStackRank() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK;
        }
        else if ( contestType.isAwardThemNow() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW;
        }
        break;
      case MANAGER_AUDIENCE_TYPE:
        if ( contestType.isObjectives() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES;
        }
        else if ( contestType.isStepItUp() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP;
        }
        else if ( contestType.isDoThisGetThat() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT;
        }
        else if ( contestType.isStackRank() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK;
        }
        else if ( contestType.isAwardThemNow() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW;
        }
        break;
      case PAX_AUDIENCE_TYPE:
        if ( contestType.isObjectives() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES;
        }
        else if ( contestType.isStepItUp() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP;
        }
        else if ( contestType.isDoThisGetThat() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT;
        }
        else if ( contestType.isStackRank() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK;
        }
        else if ( contestType.isAwardThemNow() )
        {
          promotionEmailNotificationType = PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW;
        }
        break;
      default:
        promotionEmailNotificationType = "";
        break;
    }

    for ( PromotionNotificationType promotionNotification : promotionNotifications )// getPromotionNotificationsByPromotionId(
                                                                                    // contest ) )
    {
      if ( promotionNotification.getPromotionEmailNotificationType().getCode().equals( promotionEmailNotificationType ) && promotionNotification.getNotificationMessageId() > 0 )
      {
        return promotionNotification;
      }
    }

    return null;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  private boolean createContestFinalResultNotificationForCreator( SSIContest contest,
                                                                  PromotionNotificationType promotionNotificationType,
                                                                  Participant creator,
                                                                  SSIContestUniqueCheckValueBean contestUniqueCheckValueBean )
  {
    boolean success = false;
    try
    {
      if ( SSIContestStatus.FINALIZE_RESULTS.equals( contest.getStatus().getCode() ) )
      {
        log.debug( "SSI Contest Final Result Notification for Contest Creator" );

        Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
        MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

        Map<String, Object> dataMap = null;
        if ( contest.getContestType().isObjectives()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), null );
          dataMap = setFinalResultsObjectivesContestDataForCreator( contest, contestUniqueCheckValueBean, contestProgressData );
        }
        else if ( contest.getContestType().isDoThisGetThat()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), null );
          Map<String, Object> localDataMap = new HashMap<String, Object>();
          // localDataMap.put("locale", getLocale(creator));
          dataMap = mailingService.setCreatorMgrDoThisGetThatData( contest, localDataMap, contestProgressData, getPrecision( contest ) );
        }
        else if ( contest.getContestType().isStackRank()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          List<SSIContestProgressValueBean> ssiContestProgressValueBeanList = ssiContestParticipantService.getContestProgress( contest.getId(), null );
          List<SSIContestStackRankTeamValueBean> ssiContestStackRankTeamValueBeanList = null;
          if ( ssiContestProgressValueBeanList.size() > 0 )
          {
            ssiContestStackRankTeamValueBeanList = ssiContestProgressValueBeanList.get( 0 ).getStackRankParticipants();
          }

          dataMap = setContestFinalResultProgressStackRankData( creator, getPrecision( contest ), ssiContestStackRankTeamValueBeanList, contest );
          List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), null );
          dataMap = setFinalResultsStackRankContestDataForCreator( dataMap, contest, contestProgressData );
        }
        else if ( contest.getContestType().isStepItUp()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), null );
          if ( contest.isIncludeStackRank() )
          {
            List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList = ssiContestParticipantService
                .getContestStackRank( contest.getId(), null, null, STACK_RANK_CURRENT_PAGE, TOTAL_STACK_RANK_RECORDS_PER_PAGE, false, true );
            dataMap = setContestProgressStackRankData( creator, getPrecision( contest ), ssiContestStackRankPaxValueBeanList, contest );
          }
          dataMap = setFinalResultsStepItUpContestDataForCreator( contest, contestProgressData, getPrecision( contest ), dataMap );
        }
        if ( !contest.getContestType().isAwardThemNow() && dataMap != null )
        {
          dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
          submitMailing( contest, creator, dataMap, message, mailingType );
          success = true;
        }
      }
    }
    catch( Exception e )
    {
      logErrorMessage( new Exception( "Error during SSI Contest Final Result Notification for Contest Creator: ssiContest" + contest.getId() + ", userId" + creator.getId(), e ) );
    }
    return success;
  }

  @SuppressWarnings( "rawtypes" )
  private int createContestFinalResultNotificationForManagers( SSIContest contest,
                                                               PromotionNotificationType promotionNotificationType,
                                                               Participant creator,
                                                               SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                               Set eligibleManagers )
  {
    int mgrEmailCount = 0;
    if ( SSIContestStatus.FINALIZE_RESULTS.equals( contest.getStatus().getCode() ) )
    {
      log.debug( "SSI Contest Final Result Notification for Contest Managers" );

      Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
      MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

      // eligibleManagers = getEligibleManagersFiltered( contest, eligibleManagers );

      for ( Object eligibleManager : eligibleManagers )
      {
        Participant manager = (Participant)eligibleManager;
        try
        {
          Map<String, Object> dataMap;
          if ( contest.getContestType().isObjectives()
              && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), manager.getId() );
            dataMap = setFinalResultsObjectivesContestDataForManager( contest, contestUniqueCheckValueBean, contestProgressData );
            dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), manager.getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType, manager );
            mgrEmailCount++;
          }
          else if ( contest.getContestType().isDoThisGetThat()
              && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), manager.getId() );
            Map<String, Object> localDataMap = new HashMap<String, Object>();
            // localDataMap.put("locale", getLocale(manager));
            dataMap = mailingService.setCreatorMgrDoThisGetThatData( contest, localDataMap, contestProgressData, getPrecision( contest ) );
            dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), manager.getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType, manager );
            mgrEmailCount++;
          }
          else if ( contest.getContestType().isStackRank()
              && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            dataMap = new HashMap<String, Object>();
            int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
            List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), manager.getId() );
            String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
            dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
            dataMap.put( KEY_FINAL_ACTIVITY_RESULTS, SSIContestUtil.getFormattedValue( contestProgressData.get( 0 ).getProgress(), precision ) + getActivitySuffix( contest ) );
            dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), manager.getId() ) );
            dataMap.put( KEY_PROGRAM_NAME, programName );
            submitMailing( contest, creator, dataMap, message, mailingType, manager );
            mgrEmailCount++;
          }
          else if ( contest.getContestType().isStepItUp()
              && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), manager.getId() );
            dataMap = setFinalResultsStepItUpContestDataForManager( contest, contestProgressData, getPrecision( contest ), new HashMap<String, Object>() );
            dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), manager.getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType, manager );
            mgrEmailCount++;
          }
        }
        catch( Exception e )
        {
          logErrorMessage( new Exception( "Error during SSI Contest Final Result Notification for Contest Managers: ssiContest" + contest.getId() + ", userId" + manager.getId(), e ) );
        }
      }
    }
    return mgrEmailCount;
  }

  private boolean createContestFinalResultNotificationForParticipants( SSIContest contest,
                                                                       SSIContestParticipant contestParticipant,
                                                                       PromotionNotificationType promotionNotificationType,
                                                                       Participant creator )
  {
    boolean success = false;
    try
    {
      if ( SSIContestStatus.FINALIZE_RESULTS.equals( contest.getStatus().getCode() ) )
      {
        log.debug( "SSI Contest Final Result Notification for Contest Participants" );
        Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
        MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );
        Map<String, Object> dataMap;
        if ( contest.getContestType().isObjectives()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          dataMap = setFinalResultsObjectivesContestDataForParticipants( contestParticipant, contest );
          dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
          submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
          success = true;
        }
        else if ( contest.getContestType().isDoThisGetThat()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          SSIContestPaxProgressDetailValueBean paxProgressValueBean = ssiContestParticipantService.getContestParticipantProgress( contest.getId(), contestParticipant.getParticipant().getId() );
          paxProgressValueBean.setLocale( getLocale( contestParticipant.getParticipant() ) );
          dataMap = setFinalResultsDoThisGetThatDataContestParticipant( contest, paxProgressValueBean );
          dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
          submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
          success = true;
        }
        else if ( contest.getContestType().isStackRank()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          SSIContestPaxProgressDetailValueBean paxProgressValueBean = ssiContestParticipantService.getContestParticipantProgress( contest.getId(), contestParticipant.getParticipant().getId() );
          dataMap = setFinalResultsStackRankContestDataForParticipants( contest, paxProgressValueBean );
          dataMap.put( KEY_CONTEST_END_DAYS_REMAING, DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
          dataMap.put( KEY_PROGRAM_NAME, systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
          dataMap.put( KEY_INDIVIDUAL_STACK_RANK, "#" + paxProgressValueBean.getStackRank() );
          dataMap.put( KEY_PARTICIPANT_COUNT, paxProgressValueBean.getTotalPax() );

          if ( contest.getPayoutType().isPoints() )
          {
            dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.TRUE ) );
            dataMap.put( KEY_AWARD_AMOUNT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getPayoutAmount(), 0 ) );
          }
          else if ( contest.getPayoutType().isOther() )
          {
            dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.FALSE ) );
            dataMap.put( KEY_PAYOUT_DESCRIPTION, paxProgressValueBean.getPayoutDescription() );
          }
          String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
          for ( SSIContestStackRankPayoutValueBean contestStackRankPayoutValueBean : paxProgressValueBean.getPayouts() )
          {
            if ( paxProgressValueBean.getStackRank() != null && contestStackRankPayoutValueBean.getRank() != null
                && paxProgressValueBean.getStackRank() == contestStackRankPayoutValueBean.getRank().intValue() )
            {
              dataMap.put( KEY_BADGE_IMAGE, siteUrlPrefix + contestStackRankPayoutValueBean.getBadge().getImg() );
            }
          }
          submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
          success = true;
        }
        else if ( contest.getContestType().isStepItUp()
            && PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          dataMap = setStepItUpContestDataParticipant( contestParticipant.getParticipant(), contest );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
          dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
          submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
          success = true;
        }
      }
    }
    catch( Exception e )
    {
      logErrorMessage( new Exception( "Error during SSI Contest Final Result Notification for Contest Participants: ssiContest" + contest.getId() + ", userId"
          + contestParticipant.getParticipant().getId(), e ) );
    }
    return success;
  }

  private void createAwardIssuanceNotificationForCreator( SSIContest contest, PromotionNotificationType promotionNotificationType, Participant creator ) throws Exception
  {
    try
    {
      SSIContestAwardThemNow awardThemNowContest = this.ssiContestAwardThemNowService.getContestAwardThemNowByIdAndIssunace( contest.getId(), awardIssuanceNumber );
      Map<String, Object> dataMap = setAwardIssuanceDataForCreator( awardThemNowContest );
      submitMailing( awardThemNowContest, promotionNotificationType, creator, dataMap, creator );
    }
    catch( Exception e )
    {
      logErrorMessage( new Exception( "Error During Award Issuance Notification For Creator,  Contest ID" + contest.getId() + ", User ID" + creator.getId(), e ) );
    }
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private int createAwardIssuanceNotificationForManagers( SSIContest contest, PromotionNotificationType promotionNotificationType, Participant creator )
  {
    SSIContestAwardThemNow awardThemNowContest = this.ssiContestAwardThemNowService.getContestAwardThemNowByIdAndIssunace( contest.getId(), awardIssuanceNumber );
    List<SSIContestParticipant> contestParticipants = getContestParticipants( awardThemNowContest );
    Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
    MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );
    int mgrEmailCount = 0;
    for ( SSIContestParticipant contestParticipant : contestParticipants )
    {
      try
      {
        Set mailingRecipients = new HashSet();
        Set managers = nodeService.getNodeManagersForUser( contestParticipant.getParticipant(), contestParticipant.getParticipant().getPrimaryUserNode().getNode() );
        for ( Object manager1 : managers )
        {
          User manager = (User)manager1;
          MailingRecipient managerRecipient = getMailingRecipient( manager );
          Map<String, Object> dataMap = setAwardIssuanceDataForManager( awardThemNowContest, contestParticipant, manager );
          dataMap = getContestGeneralInfo( awardThemNowContest, managerRecipient.getLocale(), dataMap, creator, contestParticipant.getParticipant() );
          setAwardIssuanceDataForManager( awardThemNowContest, contestParticipant, manager );
          managerRecipient.addMailingRecipientDataFromMap( dataMap );
          mailingRecipients.add( managerRecipient );
          mgrEmailCount++;
        }
        Mailing mailing = composeMail();
        mailing.setMessage( message );
        mailing.setMailingType( mailingType );
        mailing.addMailingRecipients( mailingRecipients );
        mailingService.submitMailing( mailing, null );
      }
      catch( Exception e )
      {
        logErrorMessage( new Exception( "Error During Award Issuance Notification For Managers, Contest ID" + contest.getId() + ", Contest Participant User ID" + creator.getId(), e ) );
      }
    }
    return mgrEmailCount;
  }

  private void createAwardIssuanceNotificationForParticipants( SSIContest contest, SSIContestParticipant contestParticipant, PromotionNotificationType promotionNotificationType, Participant creator )
  {
    try
    {
      SSIContestAwardThemNow awardThemNowContest = this.ssiContestAwardThemNowService.getContestAwardThemNowByIdAndIssunace( contest.getId(), awardIssuanceNumber );
      Map<String, Object> dataMap = setAwardIssuanceDataForParticipant( awardThemNowContest, contestParticipant );
      submitMailing( awardThemNowContest, promotionNotificationType, creator, dataMap, contestParticipant.getParticipant() );
    }
    catch( Exception e )
    {
      logErrorMessage( new Exception( "Error During Award Issuance Notification For Participants, Contest ID: " + contest.getId() + ", User ID" + contestParticipant.getParticipant().getId(), e ) );
    }
  }

  private List<SSIContestParticipant> getContestParticipants( SSIContestAwardThemNow awardThemNowContest )
  {
    int participantCount = this.ssiContestAwardThemNowService.getContestParticipantsCount( awardThemNowContest.getContest().getId(), awardThemNowContest.getIssuanceNumber() );
    Long contestId = awardThemNowContest.getContest().getId();
    return this.ssiContestAwardThemNowService.getContestParticipants( contestId, awardThemNowContest.getIssuanceNumber(), 1, participantCount );
  }

  private Map<String, Object> setAwardIssuanceDataForCreator( SSIContestAwardThemNow awardThemNowContest ) throws Exception
  {
    int precision = getPrecision( awardThemNowContest.getContest() );
    int participantCount = this.ssiContestAwardThemNowService.getContestParticipantsCount( awardThemNowContest.getContest().getId(), awardThemNowContest.getIssuanceNumber() );
    SSIContestPayoutObjectivesTotalsValueBean payoutObjectivesTotalsValueBean = this.ssiContestAwardThemNowService.calculatePayoutObjectivesTotals( awardThemNowContest.getContest().getId(),
                                                                                                                                                    awardThemNowContest.getIssuanceNumber() );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_DATE, DateUtils.toDisplayString( awardThemNowContest.getIssuanceDate() ) );
    dataMap.put( KEY_PARTICIPANT_COUNT, String.valueOf( participantCount ) );
    String activityAmount = SSIContestUtil.getFormattedValue( payoutObjectivesTotalsValueBean.getObjectiveAmountTotal(), precision ) + getActivityAmountSuffix( awardThemNowContest );
    dataMap.put( KEY_ACTIVITY_AMOUNT, activityAmount );

    if ( awardThemNowContest.getContest().getPayoutType().isPoints() )
    {
      dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.TRUE ) );
    }
    else if ( awardThemNowContest.getContest().getPayoutType().isOther() )
    {
      dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.FALSE ) );
      SSIContestUniqueCheckValueBean valueBean = this.ssiContestParticipantService.performUniqueCheck( awardThemNowContest.getContest().getId(), awardThemNowContest.getIssuanceNumber() );
      if ( valueBean.isPayoutDescSame() )
      {
        dataMap.put( KEY_IS_UNIQUE_DESCRIPTION, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_PAYOUT_DESCRIPTION, valueBean.getPayoutDesc() );
      }
      else
      {
        dataMap.put( KEY_IS_UNIQUE_DESCRIPTION, String.valueOf( Boolean.FALSE ) );
        dataMap.put( KEY_PAYOUT_DESCRIPTION, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.VARIES_BY_PARTICIPANT" ) );
      }
    }
    dataMap.put( KEY_AWARD_AMOUNT, SSIContestUtil.getFormattedValue( payoutObjectivesTotalsValueBean.getObjectivePayoutTotal(), precision ) );
    String notificationUnformatted = awardThemNowContest.getNotificationMessageText();
    if ( notificationUnformatted != null )
    {
      dataMap.put( KEY_IS_AWARD_NOTIFICATION_MESSAGE, String.valueOf( Boolean.TRUE ) );
      dataMap.put( KEY_AWARD_NOTIFICATION_MESSAGE, notificationUnformatted.replaceAll( "\\$", "&#36;" ) );
    }
    String descriptionUnformatted = this.cmAssetService.getString( awardThemNowContest.getContest().getCmAssetCode(), SSIContest.CONTEST_CMASSET_DESCRIPTION, UserManager.getLocale(), true );
    dataMap.put( KEY_CONTEST_DESCRIPTION, descriptionUnformatted.replaceAll( "\\$", "&#36;" ) );
    return dataMap;
  }

  private Map<String, Object> setAwardIssuanceDataForManager( SSIContestAwardThemNow awardThemNowContest, SSIContestParticipant contestParticipant, User manager )
  {
    Map<String, Object> dataMap = setAwardIssuanceDataForParticipant( awardThemNowContest, contestParticipant );
    dataMap.put( KEY_MANAGER_FIRST_NAME, manager.getFirstName() );
    return dataMap;
  }

  private Map<String, Object> setAwardIssuanceDataForParticipant( SSIContestAwardThemNow awardThemNowContest, SSIContestParticipant contestParticipant )
  {
    int precision = getPrecision( awardThemNowContest.getContest() );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_ACTIVITY_AMOUNT, SSIContestUtil.getFormattedValue( contestParticipant.getObjectiveAmount(), precision ) );
    dataMap.put( KEY_ACTIVITY_DESCRIPTION, contestParticipant.getActivityDescription() );

    if ( awardThemNowContest.getContest().getPayoutType().isPoints() )
    {
      dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.TRUE ) );
      dataMap.put( KEY_AWARD_AMOUNT, SSIContestUtil.getFormattedValue( contestParticipant.getObjectivePayout(), 0 ) );
    }
    else if ( awardThemNowContest.getContest().getPayoutType().isOther() )
    {
      dataMap.put( KEY_AWARD_VALUE, SSIContestUtil.getFormattedValue( contestParticipant.getObjectiveAmount(), precision ) );
      dataMap.put( KEY_PAYOUT_DESCRIPTION, contestParticipant.getObjectivePayoutDescription() );
    }
    String notificationUnformatted = awardThemNowContest.getNotificationMessageText();
    if ( notificationUnformatted != null )
    {
      dataMap.put( KEY_IS_AWARD_NOTIFICATION_MESSAGE, String.valueOf( Boolean.TRUE ) );
      dataMap.put( KEY_AWARD_NOTIFICATION_MESSAGE, notificationUnformatted.replaceAll( "\\$", "&#36;" ) );
    }
    String descriptionUnformatted = this.cmAssetService.getString( awardThemNowContest.getContest().getCmAssetCode(), SSIContest.CONTEST_CMASSET_DESCRIPTION, UserManager.getLocale(), true );
    dataMap.put( KEY_CONTEST_DESCRIPTION, descriptionUnformatted.replaceAll( "\\$", "&#36;" ) );
    return dataMap;
  }

  protected Map<String, Object> setFinalResultsObjectivesContestDataForCreator( SSIContest contest,
                                                                                SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                                List<SSIContestProgressValueBean> contestProgressValueBeanList )
      throws ServiceErrorException
  {
    return objectivesFinalResultsNotificationData( contest, contestUniqueCheckValueBean, contestProgressValueBeanList );
  }

  private Map<String, Object> objectivesFinalResultsNotificationData( SSIContest contest,
                                                                      SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                      List<SSIContestProgressValueBean> contestProgressValueBeanList )
      throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if ( contestUniqueCheckValueBean.isActivityDescSame() )
    {
      dataMap.put( KEY_IS_OBJECTIVES_SAME, String.valueOf( Boolean.TRUE ) );
    }
    if ( contestProgressValueBeanList != null && contestProgressValueBeanList.size() > 0 )
    {
      SSIContestProgressValueBean contestProgressValueBean = contestProgressValueBeanList.get( 0 );
      if ( contestUniqueCheckValueBean.isActivityDescSame() )
      {
        dataMap.put( KEY_OBJECTIVES_TOTAL, SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), precision ) + getActivitySuffix( contest ) );
        dataMap.put( KEY_OBJECTIVE_AMOUNT, SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), precision ) + getActivitySuffix( contest ) );
        dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( contestProgressValueBean.getTeamActivity(), precision ) + getActivitySuffix( contest ) );
        dataMap.put( KEY_TOTAL_AMOUNT_TO_GO,
                     SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity(), precision ) + getActivitySuffix( contest ) );
        Long percentageToObjective = (long)Math.floor( contestProgressValueBean.getTeamActivity() / contestProgressValueBean.getGoal() * 100 );
        dataMap.put( KEY_PERCENTAGE_TO_OBJECTIVE, String.valueOf( percentageToObjective ) + "%" );
      }
      if ( contest.getSameObjectiveDescription() )
      {
        dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
      }
      else if ( contestUniqueCheckValueBean.isActivityDescSame() )
      {
        dataMap.put( KEY_ACTIVITY_DESCRIPTION, contestProgressValueBean.getActivityDescription() );
      }
      else
      {
        dataMap.put( KEY_ACTIVITY_DESCRIPTION, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.VARIES_BY_PARTICIPANT" ) );
      }

      if ( contest.getPayoutType().isPoints() )
      {
        dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      }
      dataMap.put( KEY_PAYOUT_QUANTITY, contestProgressValueBean.getPayoutQuantity() );
      if ( contest.getPayoutType().isOther() )
      {
        dataMap.put( KEY_IS_OTHER, String.valueOf( Boolean.TRUE ) );
      }
      dataMap.put( KEY_PAYOUT_DESCRIPTION, contestUniqueCheckValueBean.getPayoutDesc() );
      dataMap.put( KEY_TOTAL_TEAM_MEM_CNT, contestProgressValueBean.getTotalParticipant() );
      dataMap.put( KEY_TEAM_MEM_ACHIEVED_CNT, contestProgressValueBean.getParticipantAchieved() );
    }
    return dataMap;
  }

  private Map<String, Object> setFinalResultsStackRankContestDataForCreator( Map<String, Object> dataMap, SSIContest contest, List<SSIContestProgressValueBean> contestProgressData )
  {
    SSIContestProgressValueBean valueBean = contestProgressData.get( 0 );
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
    dataMap.put( KEY_FINAL_ACTIVITY_RESULTS, SSIContestUtil.getFormattedValue( valueBean.getProgress(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_ACTIVITY_GOAL_AMOUNT, SSIContestUtil.getFormattedValue( valueBean.getGoal(), precision ) + getActivitySuffix( contest ) );
    String percentage = SSIContestUtil.getFormattedValue( (long)Math.floor( valueBean.getProgress() / valueBean.getGoal() * 100 ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + "%";
    dataMap.put( KEY_PROGRESS_TO_DATE, percentage );
    dataMap.put( KEY_FINAL_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( valueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    return dataMap;
  }

  private void submitMailing( SSIContestAwardThemNow contest, PromotionNotificationType promotionNotificationType, Participant creator, Map<String, Object> dataMap, Participant recipient )
  {
    Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
    MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );
    Mailing mailing = composeMail();
    mailing.setMessage( message );
    mailing.setMailingType( mailingType );
    MailingRecipient mailingRecipient = getMailingRecipient( recipient );
    dataMap = getContestGeneralInfo( contest, mailingRecipient.getLocale(), dataMap, creator, recipient );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailing.addMailingRecipient( mailingRecipient );
    mailingService.submitMailing( mailing, dataMap );
  }

  /**
   * Method to populate the objectMap with contest general data that applies to all contests
   * @param contest SSIContestAwardThemNow
   * @param locale String
   * @param creator Participant
   * @return Map
   */
  private Map<String, Object> getContestGeneralInfo( SSIContestAwardThemNow contest, String locale, Map<String, Object> dataMap, Participant creator, Participant recipient )
  {
    dataMap.put( KEY_FIRST_NAME, recipient.getFirstName() );
    dataMap.put( KEY_CONTEST_NAME, getContestName( contest.getContest(), locale ) );
    dataMap.put( KEY_CONTEST_CREATOR_NAME, creator.getNameFLNoComma() );
    dataMap.put( KEY_CONTEST_START_DATE, DateUtils.toDisplayDateString( contest.getContest().getStartDate(), getLocale( locale ) ) );
    dataMap.put( KEY_CONTEST_END_DATE, DateUtils.toDisplayDateString( contest.getContest().getEndDate(), getLocale( locale ) ) );
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    dataMap.put( KEY_PROGRAM_NAME, programName );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( KEY_PROGRAM_URL, programUrl );
    return dataMap;
  }

  private Map<String, Object> setFinalResultsObjectivesContestDataForManager( SSIContest contest,
                                                                              SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                              List<SSIContestProgressValueBean> contestProgressValueBeanList )
      throws ServiceErrorException
  {
    return objectivesFinalResultsNotificationData( contest, contestUniqueCheckValueBean, contestProgressValueBeanList );
  }

  // Participant, Objectives Contest Type
  private Map<String, Object> setFinalResultsObjectivesContestDataForParticipants( SSIContestParticipant contestParticipant, SSIContest contest ) throws ServiceErrorException
  {
    return objectivesEndAndFinalResultsForParticipants( contestParticipant, contest );
  }

  private Map<String, Object> setFinalResultsStackRankContestDataForParticipants( SSIContest contest, SSIContestPaxProgressDetailValueBean paxProgressValueBean )
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_FINAL_ACTIVITY_RESULTS,
                 SSIContestUtil.getFormattedValue( paxProgressValueBean.getActivityAmount() != null ? paxProgressValueBean.getActivityAmount() : new Double( 0 ), precision )
                     + getActivitySuffix( contest ) );
    dataMap.put( KEY_INDIVIDUAL_STACK_RANK, "#" + paxProgressValueBean.getStackRank() );
    dataMap.put( KEY_AMOUNT_FOR_CURRENT_LEVEL, SSIContestUtil.getFormattedValue( paxProgressValueBean.getPayoutAmount(), precision ) + getPayoutSuffix( contest ) );
    dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
    dataMap.put( KEY_AWARD_CURRENCY, SSIContestUtil.getFormattedValue( paxProgressValueBean.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    return dataMap;
  }

  private Map<String, Object> setFinalResultsDoThisGetThatDataContestParticipant( SSIContest contest, SSIContestPaxProgressDetailValueBean paxProgressValueBean )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    int counter = 0;
    if ( contest.getLastProgressUpdateDate() != null && contest.isIncludeStackRank() )
    {
      dataMap.put( "includeStackRank", String.valueOf( true ) );
    }
    for ( SSIPaxDTGTActivityProgressValueBean dtgtActivity : paxProgressValueBean.getActivities() )
    {
      String activityDescValue = dtgtActivity.getActivityDescription();
      String minimumQualifierValue = SSIContestUtil.getFormattedValue( dtgtActivity.getMinQualifier(), precision ) + getActivitySuffix( contest );
      String finalResultsValue = SSIContestUtil.getFormattedValue( dtgtActivity.getProgress() != null ? dtgtActivity.getProgress() : new Double( 0 ), precision ) + getActivitySuffix( contest );
      String awardsEarnedValue = SSIContestUtil.getFormattedValue( dtgtActivity.getPayout() != null ? dtgtActivity.getPayout() : new Long( 0 ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
          + getPayoutSuffix( contest );
      String rank = dtgtActivity.getStackRank() != null && dtgtActivity.getStackRank().getRank() != null ? String.valueOf( dtgtActivity.getStackRank().getRank() ) : "";

      // html
      dataMap.put( "description[" + counter + "]", activityDescValue );
      dataMap.put( "minQualifier[" + counter + "]", minimumQualifierValue );
      dataMap.put( "finalResultsValue[" + counter + "]", finalResultsValue );
      dataMap.put( "awardsEarnedValue[" + counter + "]", awardsEarnedValue );

      if ( contest.getLastProgressUpdateDate() != null && contest.isIncludeStackRank() )
      {
        dataMap.put( "rank[" + counter + "]", rank );
      }
    }
    return dataMap;
  }

  private Map<String, Object> setFinalResultsStepItUpContestDataForManager( SSIContest ssiContest,
                                                                            List<SSIContestProgressValueBean> contestProgressData,
                                                                            int decimalPrecision,
                                                                            Map<String, Object> dataMap )
  {
    return stepItUpFinalResultsNotificationData( ssiContest, contestProgressData, decimalPrecision, dataMap );
  }

  private Map<String, Object> stepItUpFinalResultsNotificationData( SSIContest ssiContest, List<SSIContestProgressValueBean> contestProgressData, int precision, Map<String, Object> dataMap )
  {
    if ( dataMap == null )
    {
      dataMap = new HashMap<String, Object>();
    }
    if ( contestProgressData != null && contestProgressData.size() > 0 )
    {
      SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, ssiContest.getActivityDescription() );
      if ( contestProgressValueBean.getActivity() != null )
      {
        dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( contestProgressValueBean.getActivity(), precision ) + getActivitySuffix( ssiContest ) );
        Long percentProgress = (long)Math.floor( contestProgressValueBean.getActivity() / contestProgressValueBean.getContestGoal() * 100 );
        dataMap.put( KEY_ACTIVITY_GOAL_AMOUNT, SSIContestUtil.getFormattedValue( contestProgressValueBean.getContestGoal(), precision ) + getActivitySuffix( ssiContest ) );
        dataMap.put( KEY_PERCENTAGE_TO_GOAL, percentProgress + "%" );
        dataMap.put( KEY_TO_GO_GOAL,
                     SSIContestUtil.getFormattedValue( contestProgressValueBean.getContestGoal() - contestProgressValueBean.getActivity(), precision ) + getActivitySuffix( ssiContest ) );
        dataMap.put( KEY_FINAL_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( contestProgressValueBean.getTotalPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( ssiContest ) );
      }
    }
    return dataMap;
  }

  private Map<String, Object> setFinalResultsStepItUpContestDataForCreator( SSIContest ssiContest,
                                                                            List<SSIContestProgressValueBean> contestProgressData,
                                                                            int decimalPrecision,
                                                                            Map<String, Object> dataMap )
  {
    return stepItUpFinalResultsNotificationData( ssiContest, contestProgressData, decimalPrecision, dataMap );
  }

  public Short getAwardIssuanceNumber()
  {
    return awardIssuanceNumber;
  }

  public void setAwardIssuanceNumber( Short awardIssuanceNumber )
  {
    this.awardIssuanceNumber = awardIssuanceNumber;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setSsiContestAwardThemNowService( SSIContestAwardThemNowService ssiContestAwardThemNowService )
  {
    this.ssiContestAwardThemNowService = ssiContestAwardThemNowService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

}
