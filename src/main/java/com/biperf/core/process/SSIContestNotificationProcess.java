
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionNotificationFrequencyType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestNotificationProcess.
 * 
 * @author kandhi
 * @since Jan 6, 2015
 * @version 1.0
 */
public class SSIContestNotificationProcess extends SSIContestBaseProcess
{
  public static final String BEAN_NAME = "ssiContestNotificationProcess";

  // general keys
  private static final String KEY_CLIENT_NAME = "clientName";

  private static final String KEY_IS_INCLUDE_BONUS = "isIncludeBonus";

  private static final String KEY_BONUS_PAYOUT = "bonusPayout";
  private static final String KEY_FOR_EVERY = "forEvery";
  //
  private static final String KEY_PROMOTION_NAME = "promotionName";

  private static final String KEY_CONTEST_GOAL = "contestGoal";
  private static final String KEY_IS_BONUS_SAME = "isBonusSame";
  //
  // // Step It Up
  private static final String KEY_IS_INCLUDE_BASELINE = "isIncludeBaseline";
  private static final String KEY_PAX_BASE_LINE_AMOUNT = "paxBaseLineAmount";
  private static final String KEY_MAX_PAYOUT = "maxPayoutAmt";

  private static final String PARTICIPANT = "participant";
  private static final String MANAGER = "manager";

  private static final Log log = LogFactory.getLog( SSIContestNotificationProcess.class );

  Date now = new Date();

  @Override
  protected void onExecute()
  {
    SSIPromotion promotion = ssiPromotionService.getLiveSSIPromotion();
    try
    {
      if ( promotion != null )
      {
        launchNotifications( promotion );
      }
    }
    catch( Exception ex )
    {
      log.error( "An exception occurred while sending notifications for SSI: " + getPromotionName( promotion.getCmAssetCode(), systemVariableService.getDefaultLanguage().getStringVal() )
          + " (process invocation ID = " + getProcessInvocationId() + ")", ex );
      addComment( "An exception occurred while sending notifications for SSI: " + getPromotionName( promotion.getCmAssetCode(), systemVariableService.getDefaultLanguage().getStringVal() )
          + " (process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  private void launchNotifications( SSIPromotion promotion )
  {
    // Get all the live, pending & waiting for approval contests without associations
    List<SSIContest> allContests = ssiContestService.getAllContestsByStatus( Arrays.asList( SSIContestStatus.lookup( SSIContestStatus.LIVE ),
                                                                                            SSIContestStatus.lookup( SSIContestStatus.PENDING ),
                                                                                            SSIContestStatus.lookup( SSIContestStatus.WAITING_FOR_APPROVAL ),
                                                                                            SSIContestStatus.lookup( SSIContestStatus.FINALIZE_RESULTS ) ) );

    // Fetch the notifications for each promotion that are associated with the contests
    Map<Long, List<PromotionNotificationType>> contestPromotionNotificationsMap = new HashMap<Long, List<PromotionNotificationType>>();
    // Set<Long> distinctPromotionIds = new HashSet<Long>();
    for ( SSIContest contest : allContests )
    {
      // Fetch only once for each promotion
      if ( !contestPromotionNotificationsMap.containsKey( contest.getPromotion().getId() ) )
      {
        List<PromotionNotificationType> notifications = getPromotionNotificationsByPromotionId( contest );
        contestPromotionNotificationsMap.put( contest.getPromotion().getId(), notifications );
      }
    }

    if ( contestPromotionNotificationsMap.isEmpty() || !contestPromotionNotificationsMap.containsKey( promotion.getId() ) )
    {
      contestPromotionNotificationsMap.put( promotion.getId(), (ArrayList<PromotionNotificationType>)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() ) );
    }

    // Send the launch notification for current live promotion if any
    if ( promotion != null )
    {
      sendProgramLaunchNotification( promotion, contestPromotionNotificationsMap.get( promotion.getId() ) );
    }

    for ( SSIContest contest : allContests )
    {
      // For each contest get all the associations
      AssociationRequestCollection assocReqColl = new AssociationRequestCollection();
      assocReqColl.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.ALL ) );
      contest = ssiContestService.getContestByIdWithAssociations( contest.getId(), assocReqColl );

      Participant creator = getContestCreator( contest );

      try
      {
        boolean isLaunchSuccessful = false;
        boolean hasLaunchNotifications = false;

        SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = getContestUniqueCheckValueBean( contest );

        // Get the notifications for the promotion that this contest is tied to
        List<PromotionNotificationType> notifications = contestPromotionNotificationsMap.get( contest.getPromotion().getId() );
        if ( notifications != null )
        {
          for ( PromotionNotificationType promotionNotificationType : notifications )
          {
            if ( promotionNotificationType.getNotificationMessageId() > 0 )
            {
              String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
              if ( ( contest.getContestType().isObjectives() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES.equals( notificationTypeCode )
                  || contest.getContestType().isDoThisGetThat() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT.equals( notificationTypeCode )
                  || contest.getContestType().isStepItUp() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP.equals( notificationTypeCode )
                  || contest.getContestType().isStackRank() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK.equals( notificationTypeCode ) )
                  && !contest.getContestType().isAwardThemNow() )
              {
                boolean success = createContestLaunchNotificationForCreator( contest, promotionNotificationType, creator, contestUniqueCheckValueBean );
                isLaunchSuccessful = success;
                if ( isLaunchSuccessful )
                {
                  hasLaunchNotifications = true;
                }
              }
              else if ( ( contest.getContestType().isObjectives() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES.equals( notificationTypeCode )
                  || contest.getContestType().isDoThisGetThat() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT.equals( notificationTypeCode )
                  || contest.getContestType().isStepItUp() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP.equals( notificationTypeCode )
                  || contest.getContestType().isStackRank() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK.equals( notificationTypeCode ) )
                  && !contest.getContestType().isAwardThemNow() )
              {
                boolean success = createContestLaunchNotificationForManagers( contest, promotionNotificationType, creator, contestUniqueCheckValueBean );
                isLaunchSuccessful = success;
                if ( isLaunchSuccessful )
                {
                  isLaunchSuccessful = setLaunchSuccessful( contest );
                  hasLaunchNotifications = true;
                }
              }
              else if ( ( contest.getContestType().isObjectives() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES.equals( notificationTypeCode )
                  || contest.getContestType().isDoThisGetThat() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT.equals( notificationTypeCode )
                  || contest.getContestType().isStepItUp() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP.equals( notificationTypeCode )
                  || contest.getContestType().isStackRank() && PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK.equals( notificationTypeCode ) )
                  && !contest.getContestType().isAwardThemNow() )
              {
                boolean success = createContestLaunchNotificationForParticipants( contest, promotionNotificationType, creator );
                isLaunchSuccessful = success;
                if ( isLaunchSuccessful )
                {
                  isLaunchSuccessful = setLaunchSuccessful( contest );
                  hasLaunchNotifications = true;
                }
              }
              else if ( contest.getContestType().isObjectives() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES.equals( notificationTypeCode )
                  || contest.getContestType().isDoThisGetThat() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT.equals( notificationTypeCode )
                  || contest.getContestType().isStepItUp() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP.equals( notificationTypeCode )
                  || contest.getContestType().isStackRank() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK.equals( notificationTypeCode ) )
              {
                createContestEndNotificationForCreator( contest, promotionNotificationType, creator, contestUniqueCheckValueBean );
              }
              else if ( contest.getContestType().isObjectives() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES.equals( notificationTypeCode )
                  || contest.getContestType().isDoThisGetThat() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT.equals( notificationTypeCode )
                  || contest.getContestType().isStepItUp() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP.equals( notificationTypeCode )
                  || contest.getContestType().isStackRank() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK.equals( notificationTypeCode ) )
              {
                createContestEndNotificationForManagers( contest, promotionNotificationType, creator, contestUniqueCheckValueBean );
              }
              else if ( contest.getContestType().isObjectives() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES.equals( notificationTypeCode )
                  || contest.getContestType().isDoThisGetThat() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT.equals( notificationTypeCode )
                  || contest.getContestType().isStepItUp() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP.equals( notificationTypeCode )
                  || contest.getContestType().isStackRank() && PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK.equals( notificationTypeCode ) )
              {
                createContestEndNotificationForParticipants( contest, promotionNotificationType, creator );
              }
              else if ( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER.equals( notificationTypeCode ) )
              {
                createContestApproverReminder( promotion, contest, promotionNotificationType, creator );
              }
              else if ( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR.equals( notificationTypeCode ) )
              {
                createContestFinalResultsReminder( promotion, contest, promotionNotificationType, creator );
              }
            }
          }
        }

        if ( isLaunchSuccessful && hasLaunchNotifications )
        {
          contest.setLaunchNotificationSent( true );
          Set<SSIContestParticipant> contestParticipants = contest.getContestParticipants();
          contestParticipants.parallelStream().forEach( p -> ssiContestService.updateContestParticipants( p ) );
          Set<SSIContestManager> contestManagers = contest.getContestManagers();
          contestManagers.parallelStream().forEach( p -> ssiContestService.updateContestManagers( p ) );
          ssiContestService.saveContest( contest );
        }
        addComment( "Launch notifications for contest '" + getContestName( contest, systemVariableService.getDefaultLanguage().getStringVal() ) + "' has been processed." );
      }
      catch( Exception ex )
      {
        log.error( "An exception occurred while sending notifications for contest: " + getContestName( contest, systemVariableService.getDefaultLanguage().getStringVal() )
            + " (process invocation ID = " + getProcessInvocationId() + ")", ex );
        addComment( "An exception occurred while sending notifications for contest: " + getContestName( contest, systemVariableService.getDefaultLanguage().getStringVal() )
            + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }

    }

  }

  /**
   * Notification - Contest Launch notification Contest Creator 
   * @param contest SSIContest
   * @param promotionNotificationType PromotionNotificationType
   * @param creator Participant
   * @return boolean
   * @throws ServiceErrorException
   */
  private boolean createContestLaunchNotificationForCreator( SSIContest contest,
                                                             PromotionNotificationType promotionNotificationType,
                                                             Participant creator,
                                                             SSIContestUniqueCheckValueBean contestUniqueCheckValueBean )
      throws ServiceErrorException
  {
    boolean success = false;
    if ( !contest.isLaunchNotificationSent() )
    {
      if ( ( contest.getStatus().isLive() || contest.getStatus().isPending() ) && org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), contest.getDisplayStartDate() ) )
      {
        if ( contest.getEndDate().after( now ) )
        {
          log.debug( "SSI Contest Launch Notification for Contest Creator" );

          Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
          MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

          if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setLaunchObjectivesContestDataForCreator( contest, contestUniqueCheckValueBean );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType );
            success = true;
          }
          else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setLaunchDoThisGetThatContestDataForCreator( contest, getLocale( creator ) );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType );
            success = true;
          }
          else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setLaunchStackRankContestDataForCreator( contest );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
            dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
            submitMailing( contest, creator, dataMap, message, mailingType );
            success = true;
          }
          else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setLaunchStepItUpContestDataForCreator( contest );
            String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
            dataMap.put( KEY_PROGRAM_URL, programUrl );
            dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
            submitMailing( contest, creator, dataMap, message, mailingType );
            success = true;
          }
        }
      }
    }
    return success;
  }

  /**
   * Notification - Contest Launch notification Contest Managers
   * @param contest SSIContest
   * @param promotionNotificationType PromotionNotificationType
   * @param creator Participant
   * @return boolean
   * @throws ServiceErrorException
   */
  private boolean createContestLaunchNotificationForManagers( SSIContest contest,
                                                              PromotionNotificationType promotionNotificationType,
                                                              Participant creator,
                                                              SSIContestUniqueCheckValueBean contestUniqueCheckValueBean )
      throws ServiceErrorException
  {
    boolean success = false;
    boolean isLaunchNotificationSendForAllPax = isLaunchNotificationSendForAllPax( contest, MANAGER );
    if ( !contest.isLaunchNotificationSent() || isLaunchNotificationSendForAllPax )
    {
      if ( ( contest.getStatus().isLive() || contest.getStatus().isPending() ) && org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), contest.getDisplayStartDate() ) )
      {
        if ( contest.getEndDate().after( now ) )
        {
          log.debug( "SSI Contest Launch Notification for Contest Managers" );
          Set<SSIContestManager> contestManagers = contest.getContestManagers();

          if ( contestManagers.size() > 0 )
          {
            Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
            MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

            for ( SSIContestManager contestManager : contestManagers )
            {
              if ( !contestManager.isLaunchNotificationSent() )
              {
                if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
                {
                  Map<String, Object> dataMap = setLaunchObjectivesContestDataForManager( contest, contestUniqueCheckValueBean );
                  dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
                  submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
                  success = true;
                }
                else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
                {
                  Map<String, Object> dataMap = setLaunchDoThisGetThatContestDataForManager( contest, getLocale( creator ) );
                  dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
                  submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
                  success = true;
                }
                else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
                {
                  Map<String, Object> dataMap = new HashMap<String, Object>();
                  dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
                  dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
                  submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
                  success = true;
                }
                else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
                {
                  Map<String, Object> dataMap = new HashMap<String, Object>();
                  dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
                  dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
                  submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
                  success = true;
                }
              }
            }
          }
          else
          {
            success = true; // no contest managers
          }
        }
      }
    }
    return success;
  }

  /**
   * Notification - Contest Launch notification Contest Participants
   * @param contest SSIContest
   * @param promotionNotificationType PromotionNotificationType
   * @param creator Participant
   * @return boolean
   * @throws ServiceErrorException 
   */
  private boolean createContestLaunchNotificationForParticipants( SSIContest contest, PromotionNotificationType promotionNotificationType, Participant creator ) throws ServiceErrorException
  {
    boolean success = false;
    boolean isLaunchNotificationSendForAllPax = isLaunchNotificationSendForAllPax( contest, PARTICIPANT );
    if ( !contest.isLaunchNotificationSent() || isLaunchNotificationSendForAllPax )
    {
      if ( ( contest.getStatus().isLive() || contest.getStatus().isPending() ) && org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), contest.getDisplayStartDate() ) )
      {
        if ( contest.getEndDate().after( now ) )
        {
          log.debug( "SSI Contest Launch Notification for Contest Participants" );
          Set<SSIContestParticipant> contestParticipants = contest.getContestParticipants();
          Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
          MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );
          String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
          for ( SSIContestParticipant contestParticipant : contestParticipants )
          {
            if ( !contestParticipant.isLaunchNotificationSent() )
            {
              if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
              {
                Map<String, Object> dataMap = setLaunchObjectivesContestDataForParticipant( contestParticipant, contest );
                dataMap.put( KEY_PROGRAM_URL, programUrl );
                submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
                success = true;
              }
              else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
              {
                Map<String, Object> dataMap = setLaunchDoThisGetThatContestDataForParticipant( contest, getLocale( contestParticipant.getParticipant() ) );
                dataMap.put( KEY_PROGRAM_URL, programUrl );
                submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
                success = true;
              }
              else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
              {
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put( KEY_PROGRAM_URL, programUrl );
                dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
                submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
                success = true;
              }
              else if ( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
              {
                Map<String, Object> dataMap = setLaunchStepItUpContestDataForParticipant( contestParticipant, contest );
                dataMap.put( KEY_PROGRAM_URL, programUrl );
                dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
                submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
                success = true;
              }
            }
          }
        }
      }
    }
    return success;
  }

  private boolean createContestEndNotificationForCreator( SSIContest contest,
                                                          PromotionNotificationType promotionNotificationType,
                                                          Participant creator,
                                                          SSIContestUniqueCheckValueBean contestUniqueCheckValueBean )
      throws ServiceErrorException
  {
    boolean success = false;
    if ( SSIContestStatus.LIVE.equals( contest.getStatus().getCode() ) )
    {
      Date notificationDate = new Date( contest.getEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );

      // Send Notification if current date is the notification date
      if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), notificationDate ) )
      {
        log.debug( "SSI Contest Launch Notification for Contest Creator" );

        Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
        MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

        List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), null );

        if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          Map<String, Object> dataMap = setEndObjectivesContestDataForCreator( contest, contestUniqueCheckValueBean, contestProgressData );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
          submitMailing( contest, creator, dataMap, message, mailingType );
          success = true;
        }
        else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          Map<String, Object> dataMap = new HashMap<String, Object>();
          // dataMap.put("locale", getLocale(creator));
          dataMap = mailingService.setCreatorMgrDoThisGetThatData( contest, dataMap, contestProgressData, getPrecision( contest ) );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
          submitMailing( contest, creator, dataMap, message, mailingType );
          success = true;
        }
        else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          Map<String, Object> dataMap = setEndStackRankContestDataForCreator( contest, creator );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
          dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
          submitMailing( contest, creator, dataMap, message, mailingType );
          success = true;
        }
        else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
        {
          Map<String, Object> dataMap = setEndStepItUpContestDataForCreator( creator, contest, true );
          dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
          dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
          submitMailing( contest, creator, dataMap, message, mailingType );
          success = true;
        }
      }
    }
    return success;
  }

  /**
   * Notification - Contest End notification Contest Managers
   * @param contest SSIContest
   * @param promotionNotificationType PromotionNotificationType
   * @param creator Participant
   * @return boolean
   * @throws ServiceErrorException
   */
  private boolean createContestEndNotificationForManagers( SSIContest contest,
                                                           PromotionNotificationType promotionNotificationType,
                                                           Participant creator,
                                                           SSIContestUniqueCheckValueBean contestUniqueCheckValueBean )
      throws ServiceErrorException
  {
    boolean success = false;
    if ( SSIContestStatus.LIVE.equals( contest.getStatus().getCode() ) )
    {
      Date notificationDate = new Date( contest.getEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );

      // Send Notification if current date is the notification date
      if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), notificationDate ) )
      {
        log.debug( "SSI Contest End Notification for Contest Managers" );

        Set<SSIContestManager> contestManagers = contest.getContestManagers();

        if ( contestManagers.size() > 0 )
        {
          Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
          MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

          for ( SSIContestManager contestManager : contestManagers )
          {
            List<SSIContestProgressValueBean> contestProgressData = getProgressData( contest.getId(), contestManager.getManager().getId() );

            if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
            {
              Map<String, Object> dataMap = setEndObjectivesContestDataForManager( contest, contestUniqueCheckValueBean, contestProgressData );
              dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
              submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
              success = true;
            }
            else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
            {
              Map<String, Object> dataMap = new HashMap<String, Object>();
              // dataMap.put("locale", getLocale(contestManager.getManager()));
              dataMap = mailingService.setCreatorMgrDoThisGetThatData( contest, dataMap, contestProgressData, getPrecision( contest ) );
              dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
              submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
              success = true;
            }
            else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
            {
              Map<String, Object> dataMap = setEndStackRankContestDataForManagers( contestManager.getManager(), contest );
              dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
              dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
              submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
              success = true;
            }
            else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
            {
              Map<String, Object> dataMap = setEndStepItUpContestDataForManager( contestManager.getManager(), contest, false );
              dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateManagerDetailPageUrl( contest.getId(), contestManager.getManager().getId() ) );
              dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
              submitMailing( contest, creator, dataMap, message, mailingType, contestManager.getManager() );
              success = true;
            }
          }
        }
        else
        {
          success = true; // no managers
        }
      }
    }
    return success;
  }

  /**
   * Notification - Contest End notification Contest Participants
   * @param contest SSIContest
   * @param promotionNotificationType PromotionNotificationType
   * @param creator Participant
   * @return boolean
   * @throws ServiceErrorException 
   */
  private boolean createContestEndNotificationForParticipants( SSIContest contest, PromotionNotificationType promotionNotificationType, Participant creator ) throws ServiceErrorException
  {
    boolean success = false;
    if ( SSIContestStatus.LIVE.equals( contest.getStatus().getCode() ) )
    {
      Date notificationDate = new Date( contest.getEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );

      // Send Notification if current date is the notification date
      if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), notificationDate ) )
      {
        log.debug( "SSI Contest End Notification for Contest Participants" );

        Set<SSIContestParticipant> contestParticipants = contest.getContestParticipants();

        Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
        MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

        for ( SSIContestParticipant contestParticipant : contestParticipants )
        {
          SSIContestPaxProgressDetailValueBean paxProgressValueBean = ssiContestParticipantService.getContestParticipantProgress( contest.getId(), contestParticipant.getParticipant().getId() );

          if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setEndObjectivesContestDataForParticipants( contestParticipant, contest );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
            success = true;
          }
          else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap = mailingService.setContestProgressDoThisGetThatData( contest, dataMap, paxProgressValueBean, getLocale( contestParticipant.getParticipant() ) );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
            submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
            success = true;
          }
          else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setEndStackRankContestDataForParticipants( contest, paxProgressValueBean );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
            dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
            submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
            success = true;
          }
          else if ( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP.equals( promotionNotificationType.getPromotionEmailNotificationType().getCode() ) )
          {
            Map<String, Object> dataMap = setStepItUpContestDataParticipant( contestParticipant.getParticipant(), contest );
            dataMap.put( KEY_PROGRAM_URL, SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestParticipant.getParticipant().getId() ) );
            dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
            submitMailing( contest, creator, dataMap, message, mailingType, contestParticipant.getParticipant() );
            success = true;
          }
        }
      }
    }
    return success;
  }

  /**
   * Send the contest approver reminder
   * @param promotion SSIPromotion
   * @param contest SSIContest
   * @param promotionNotificationType PromotionNotificationType
   * @param creator Participant
   */
  private void createContestApproverReminder( SSIPromotion promotion, SSIContest contest, PromotionNotificationType promotionNotificationType, Participant creator )
  {
    if ( contest.getStatus().isWaitingForApproval() )
    {
      // Using last updated date since it will be the last update to the contest (ie. changed the
      // contest status to waiting for approval from create)
      Date notificationDate = new Date( contest.getAuditUpdateInfo().getDateModified().getTime()
          + promotionNotificationType.getNumberOfDays().longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
      // Current date is beyond the notification date and the notification frequency matches current
      // day and contest requires approval(redundant check)
      if ( DateUtils.getCurrentDate().compareTo( notificationDate ) >= 0 && isEligibleforNotification( promotionNotificationType ) && promotion.getRequireContestApproval() )
      {
        Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
        MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( KEY_PROGRAM_URL, systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
        // level 1 is not approved yet
        if ( contest.getDateApprovedLevel1() == null )
        {
          Set<SSIContestApprover> levelApprovers = contest.getContestLevel1Approvers();
          for ( SSIContestApprover contestApprover : levelApprovers )
          {
            submitMailing( promotion, contest, message, mailingType, contestApprover.getApprover(), creator.getNameFLNoComma(), dataMap );
          }
        }
        else if ( promotion.getContestApprovalLevels() == 2 && contest.getDateApprovedLevel2() == null )
        { // Has 2 levels of approvals and level 2 is not approved yet
          Set<SSIContestApprover> levelApprovers = contest.getContestLevel2Approvers();
          for ( SSIContestApprover contestApprover : levelApprovers )
          {
            // Level 1 approver cannot be same as level2 approver
            if ( !contest.getApprovedByLevel1().equals( contest.getApprovedByLevel2() ) )
            {
              // submit the mailing
              submitMailing( promotion, contest, message, mailingType, contestApprover.getApprover(), creator.getNameFLNoComma(), dataMap );
            }
          }
        }
      }
    }
  }

  /** Send the final results reminder notification for the contest creator
  * @param contest SSIContest
  * @param promotionNotificationType PromotionNotificationType
  * @param creator Participant
  */
  private void createContestFinalResultsReminder( SSIPromotion promotion, SSIContest contest, PromotionNotificationType promotionNotificationType, Participant creator )
  {
    if ( SSIContestStatus.LIVE.equals( contest.getStatus().getCode() ) && contest.getEndDate().before( DateUtils.getCurrentDate() ) )
    {
      Date notificationDate = new Date( contest.getEndDate().getTime() + promotionNotificationType.getNumberOfDays().longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
      // Current date is beyond the notification date and the notification frequency matches
      if ( DateUtils.getCurrentDate().compareTo( notificationDate ) >= 0 && isEligibleforNotification( promotionNotificationType ) )
      {
        Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
        MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( KEY_PROGRAM_URL, systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
        submitMailing( promotion, contest, message, mailingType, creator, creator.getNameFLNoComma(), dataMap );
      }
    }
  }

  // Objectives Contest Data Set Starts
  // Creator, Objectives Contest Type
  private Map<String, Object> setLaunchObjectivesContestDataForCreator( SSIContest contest, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean ) throws ServiceErrorException
  {
    return objectivesLaunchNotificationData( contest, contestUniqueCheckValueBean );
  }

  // Manager, Objectives Contest Type
  private Map<String, Object> setLaunchObjectivesContestDataForManager( SSIContest contest, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean ) throws ServiceErrorException
  {
    return objectivesLaunchNotificationData( contest, contestUniqueCheckValueBean );
  }

  // Participant, Objectives Contest Type
  private Map<String, Object> setLaunchObjectivesContestDataForParticipant( SSIContestParticipant contestParticipant, SSIContest contest )
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_FIRST_NAME, contestParticipant.getParticipant().getFirstName() );
    if ( contest.getPayoutType().isPoints() )
    {
      dataMap.put( KEY_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( contestParticipant.getObjectivePayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    }
    else if ( contest.getPayoutType().isOther() )
    {
      dataMap.put( KEY_PAYOUT_AMOUNT, contestParticipant.getObjectivePayoutDescription() );
    }
    dataMap.put( KEY_OBJECTIVES_TOTAL, SSIContestUtil.getFormattedValue( contestParticipant.getObjectiveAmount(), precision ) + getActivitySuffix( contest ) );
    if ( contest.isIncludeBonus() )
    {
      dataMap.put( KEY_IS_INCLUDE_BONUS, String.valueOf( Boolean.TRUE ) );
      if ( contest.getActivityMeasureType().isUnit() )
      {
        dataMap.put( KEY_FOR_EVERY, StringUtils.stripEnd( SSIContestUtil.getFormattedValue( contestParticipant.getObjectiveBonusIncrement(), precision ), null ) );
      }
      else
      {
        dataMap.put( KEY_FOR_EVERY, StringUtils.stripEnd( SSIContestUtil.getFormattedValue( contestParticipant.getObjectiveBonusIncrement(), precision ) + " " + getActivitySuffix( contest ), null ) );
      }
      dataMap.put( KEY_BONUS_PAYOUT, SSIContestUtil.getFormattedValue( contestParticipant.getObjectiveBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    }
    if ( contest.getSameObjectiveDescription() )
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
    }
    else
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, contestParticipant.getActivityDescription() );
    }
    return dataMap;
  }

  private Map<String, Object> objectivesLaunchNotificationData( SSIContest contest, SSIContestUniqueCheckValueBean contestUniqueCheckValueBean ) throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getTotalPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    if ( contestUniqueCheckValueBean.isActivityDescSame() )
    {
      dataMap.put( KEY_IS_OBJECTIVES_SAME, String.valueOf( Boolean.TRUE ) );
      dataMap.put( KEY_OBJECTIVES_TOTAL, SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getTotalAmount(), precision ) + getActivitySuffix( contest ) );
      if ( contest.isIncludeBonus() )
      {
        if ( contestUniqueCheckValueBean.isBonusSame() )
        {
          dataMap.put( KEY_IS_BONUS_SAME, String.valueOf( Boolean.TRUE ) );
          dataMap.put( KEY_BONUS_PAYOUT, SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
          if ( contest.getActivityMeasureType().isUnit() )
          {
            dataMap.put( KEY_FOR_EVERY, SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusIncrement(), precision ) );
          }
          else
          {
            dataMap.put( KEY_FOR_EVERY, SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusIncrement(), precision ) + " " + getActivitySuffix( contest ) );
          }

        }
      }
    }
    if ( contest.getSameObjectiveDescription() )
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
    }
    else if ( contestUniqueCheckValueBean.isActivityDescSame() )
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, contestUniqueCheckValueBean.getActivityDesc() );
    }
    else
    {
      dataMap.put( KEY_ACTIVITY_DESCRIPTION, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.VARIES_BY_PARTICIPANT" ) );
    }
    return dataMap;
  }

  private Map<String, Object> setEndObjectivesContestDataForCreator( SSIContest contest,
                                                                     SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                     List<SSIContestProgressValueBean> contestProgressValueBeanList )
      throws ServiceErrorException
  {
    return objectivesEndNotificationData( contest, contestUniqueCheckValueBean, contestProgressValueBeanList );
  }

  private Map<String, Object> setEndObjectivesContestDataForManager( SSIContest contest,
                                                                     SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                     List<SSIContestProgressValueBean> contestProgressValueBeanList )
      throws ServiceErrorException
  {
    return objectivesEndNotificationData( contest, contestUniqueCheckValueBean, contestProgressValueBeanList );
  }

  private Map<String, Object> objectivesEndNotificationData( SSIContest contest,
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
        dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( contestProgressValueBean.getTeamActivity(), precision ) + getActivitySuffix( contest ) );
        double remaining = 0;
        if ( contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity() > (double)0 )
        {
          remaining = contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity();
        }
        dataMap.put( KEY_TOTAL_AMOUNT_TO_GO, SSIContestUtil.getFormattedValue( remaining, precision ) + getActivitySuffix( contest ) );
        Long percentageToObjective = (long)Math.floor( contestProgressValueBean.getTeamActivity() / contestProgressValueBean.getGoal() * 100 );
        dataMap.put( KEY_PERCENTAGE_TO_OBJECTIVE, String.valueOf( percentageToObjective ) );
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
      dataMap.put( KEY_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
      dataMap.put( KEY_PAYOUT_DESCRIPTION, contestUniqueCheckValueBean.getPayoutDesc() );
      if ( contestProgressValueBean.getTotalParticipant() != null )
      {
        dataMap.put( KEY_TOTAL_TEAM_MEM_CNT, String.valueOf( contestProgressValueBean.getTotalParticipant() ) );
      }
      else
      {
        dataMap.put( KEY_TOTAL_TEAM_MEM_CNT, contestProgressValueBean.getTotalParticipant() );
      }
      if ( contestProgressValueBean.getParticipantAchieved() != null )
      {
        dataMap.put( KEY_TEAM_MEM_ACHIEVED_CNT, String.valueOf( contestProgressValueBean.getParticipantAchieved() ) );
      }
      else
      {
        dataMap.put( KEY_TEAM_MEM_ACHIEVED_CNT, contestProgressValueBean.getParticipantAchieved() );
      }
    }
    return dataMap;
  }

  private Map<String, Object> setEndObjectivesContestDataForParticipants( SSIContestParticipant contestParticipant, SSIContest contest ) throws ServiceErrorException
  {
    return objectivesEndAndFinalResultsForParticipants( contestParticipant, contest );
  }

  private Map<String, Object> setLaunchDoThisGetThatContestDataForCreator( SSIContest contest, Locale locale )
  {
    return doThisGetThatLaunchNotificationData( contest, false, false, locale );
  }

  private Map<String, Object> setLaunchDoThisGetThatContestDataForManager( SSIContest contest, Locale locale )
  {
    return doThisGetThatLaunchNotificationData( contest, false, true, locale );
  }

  private Map<String, Object> setLaunchDoThisGetThatContestDataForParticipant( SSIContest contest, Locale locale )
  {
    return doThisGetThatLaunchNotificationData( contest, true, false, locale );
  }

  private Map<String, Object> doThisGetThatLaunchNotificationData( SSIContest contest, boolean isParticipant, boolean isManager, Locale locale )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    int precision = getPrecision( contest );
    int paxCount = this.ssiContestParticipantService.getContestParticipantsCount( contest.getId() );
    if ( !isParticipant )
    {
      dataMap.put( "isNotParticipant", String.valueOf( true ) );
      if ( !isManager )
      {
        dataMap.put( "isNotManager", String.valueOf( true ) );
      }
    }

    dataMap.put( "contestActivitySize", Integer.toString( contest.getContestActivities().size() ) );
    int counter = 0;
    for ( SSIContestActivity contestActivity : contest.getContestActivities() )
    {
      counter++;
      dataMap.put( "description[" + counter + "]", contestActivity.getDescription() );

      if ( contest.getActivityMeasureType().isUnit() )
      {
        dataMap.put( "incrementAmount[" + counter + "]", SSIContestUtil.getFormattedValue( contestActivity.getIncrementAmount(), precision ) );
      }
      else
      {
        dataMap.put( "incrementAmount[" + counter + "]", SSIContestUtil.getFormattedValue( contestActivity.getIncrementAmount(), precision ) + " " + getActivitySuffix( contest ) );
      }

      dataMap.put( "payoutAmount[" + counter + "]", SSIContestUtil.getFormattedValue( contestActivity.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + " " + getPayoutSuffix( contest ) );

      if ( !isParticipant )
      {
        dataMap.put( "goalAmount[" + counter + "]", SSIContestUtil.getFormattedValue( contestActivity.getGoalAmount(), precision ) + " " + getActivitySuffix( contest ) );
      }

      if ( !isManager )
      {
        dataMap.put( "payoutCapAmount[" + counter + "]",
                     SSIContestUtil.getFormattedValue( contestActivity.getPayoutCapAmount() * paxCount, SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + " " + getPayoutSuffix( contest ) );
      }
      dataMap.put( "minQualifier[" + counter + "]", SSIContestUtil.getFormattedValue( contestActivity.getMinQualifier(), precision ) + " " + getActivitySuffix( contest ) );
    }
    return dataMap;
  }

  private Map<String, Object> setLaunchStepItUpContestDataForCreator( SSIContest contest )
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_CONTEST_GOAL, SSIContestUtil.getFormattedValue( contest.getContestGoal(), precision ) + getActivitySuffix( contest ) );
    Long maximumPayout = calculateMaximumPayout( contest );
    dataMap.put( KEY_MAX_PAYOUT, SSIContestUtil.getFormattedValue( maximumPayout, SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    dataMap.put( KEY_ACTIVITY_DESCRIPTION, contest.getActivityDescription() );
    return dataMap;
  }

  private Long calculateMaximumPayout( SSIContest contest )
  {
    Long highestPayout = SSIContestUtil.getHighestLevelPayoutAmount( new ArrayList<SSIContestLevel>( contest.getContestLevels() ) );
    int totalPax = contest.getContestParticipants().size();
    Long maximumPayoutCalculated = totalPax * highestPayout;
    Long bonusPayout = 0L;
    if ( contest.isIncludeBonus() )
    {
      bonusPayout = totalPax * contest.getStepItUpBonusCap();
    }
    return maximumPayoutCalculated + bonusPayout;
  }

  private Map<String, Object> setLaunchStepItUpContestDataForParticipant( SSIContestParticipant contestParticipant, SSIContest contest )
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_PAX_BASE_LINE_AMOUNT, SSIContestUtil.getFormattedValue( contestParticipant.getStepItUpBaselineAmount(), precision ) + getActivitySuffix( contest ) );
    if ( !contest.getIndividualBaselineType().isNo() )
    {
      dataMap.put( KEY_IS_INCLUDE_BASELINE, String.valueOf( Boolean.TRUE ) );
    }
    return dataMap;
  }

  private Map<String, Object> setEndStepItUpContestDataForCreator( Participant creator, SSIContest contest, boolean isCreator ) throws ServiceErrorException
  {
    return stepItUpEndNotificationData( creator, contest, isCreator );
  }

  private Map<String, Object> setEndStepItUpContestDataForManager( Participant manager, SSIContest contest, boolean isCreator ) throws ServiceErrorException
  {
    return stepItUpEndNotificationData( manager, contest, isCreator );
  }

  private Map<String, Object> stepItUpEndNotificationData( Participant user, SSIContest contest, boolean isCreator ) throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap;
    if ( isCreator && contest.isIncludeStackRank() )
    {
      List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList = ssiContestParticipantService
          .getContestStackRank( contest.getId(), null, null, STACK_RANK_CURRENT_PAGE, TOTAL_STACK_RANK_RECORDS_PER_PAGE, false, true );
      dataMap = setContestProgressStackRankData( user, getPrecision( contest ), ssiContestStackRankPaxValueBeanList, contest );
    }
    else
    {
      dataMap = new HashMap<String, Object>();
    }
    List<SSIContestProgressValueBean> contestProgress = ssiContestParticipantService.getContestProgress( contest.getId(), isCreator ? null : user.getId() );
    SSIContestProgressValueBean valueBean = contestProgress.get( 0 );
    dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( valueBean.getActivity(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_ACTIVITY_GOAL_AMOUNT, SSIContestUtil.getFormattedValue( valueBean.getContestGoal(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_PERCENTAGE_TO_GOAL, SSIContestUtil.getFormattedValue( valueBean.getPercPayout(), 0 ) + "%" );
    dataMap.put( KEY_TO_GO_GOAL, SSIContestUtil.getFormattedValue( valueBean.getTogo(), precision ) + getActivitySuffix( contest ) );
    return dataMap;
  }

  private Map<String, Object> setEndStackRankContestDataForCreator( SSIContest contest, Participant creator ) throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    List<SSIContestProgressValueBean> valueBeans = this.ssiContestParticipantService.getContestProgress( contest.getId(), null );
    SSIContestProgressValueBean valueBean = valueBeans.get( 0 );
    List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList = ssiContestParticipantService
        .getContestStackRank( contest.getId(), null, null, STACK_RANK_CURRENT_PAGE, TOTAL_STACK_RANK_RECORDS_PER_PAGE, false, true );
    Map<String, Object> dataMap = setContestProgressStackRankData( creator, getPrecision( contest ), ssiContestStackRankPaxValueBeanList, contest );
    dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( valueBean.getProgress(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_ACTIVITY_GOAL_AMOUNT, SSIContestUtil.getFormattedValue( valueBean.getGoal(), precision ) + getActivitySuffix( contest ) );
    Double goalPercentage = (double)0;
    if ( valueBean.getProgress() != null && valueBean.getGoal() != null && !valueBean.getGoal().equals( (double)0 ) && !valueBean.getGoal().equals( 0.0 ) )
    {
      goalPercentage = valueBean.getProgress() / valueBean.getGoal() * 100;
    }
    dataMap.put( KEY_PERCENTAGE_TO_GOAL, SSIContestUtil.getFormattedValue( goalPercentage, 0 ) + "%" );
    dataMap.put( KEY_TO_GO_GOAL, SSIContestUtil.getFormattedValue( valueBean.getTogo(), precision ) + getActivitySuffix( contest ) );
    return dataMap;
  }

  private Map<String, Object> setEndStackRankContestDataForManagers( Participant manager, SSIContest contest ) throws ServiceErrorException
  {
    int precision = getPrecision( contest );
    List<SSIContestProgressValueBean> valueBeans = this.ssiContestParticipantService.getContestProgress( contest.getId(), manager.getId() );
    SSIContestProgressValueBean valueBean = valueBeans.get( 0 );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_PROGRESS_TO_DATE, SSIContestUtil.getFormattedValue( valueBean.getProgress(), precision ) + getActivitySuffix( contest ) );
    return dataMap;
  }

  private Map<String, Object> setEndStackRankContestDataForParticipants( SSIContest contest, SSIContestPaxProgressDetailValueBean paxProgressValueBean )
  {
    int precision = getPrecision( contest );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( KEY_PROGRESS_TO_DATE,
                 paxProgressValueBean.getActivityAmount() == null
                     ? String.valueOf( 0 )
                     : SSIContestUtil.getFormattedValue( paxProgressValueBean.getActivityAmount(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_INDIVIDUAL_STACK_RANK, paxProgressValueBean.getStackRank() );
    dataMap.put( KEY_PARTICIPANT_COUNT, paxProgressValueBean.getTotalPax() );
    dataMap.put( KEY_AWARD_AMOUNT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    return dataMap;
  }

  private void submitMailing( SSIPromotion promotion, SSIContest contest, Message message, MailingType mailingType, Participant participant, String creatorName, Map<String, Object> objectMap )
  {
    Mailing mailing = composeMail();
    mailing.setMessage( message );
    mailing.setMailingType( mailingType );
    MailingRecipient mailingRecipient = getMailingRecipient( participant );

    objectMap.put( KEY_CONTEST_NAME, getContestName( contest, mailingRecipient.getLocale() ) );
    objectMap.put( KEY_CONTEST_START_DATE, DateUtils.toDisplayDateString( contest.getStartDate(), getLocale( mailingRecipient.getLocale() ) ) );
    objectMap.put( KEY_CONTEST_END_DATE, DateUtils.toDisplayDateString( contest.getEndDate(), getLocale( mailingRecipient.getLocale() ) ) );
    objectMap.put( KEY_FIRST_NAME, participant.getFirstName() );
    objectMap.put( KEY_CONTEST_CREATOR_NAME, creatorName );
    objectMap.put( KEY_PROMOTION_NAME, getPromotionName( promotion.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
    String clientName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    objectMap.put( KEY_CLIENT_NAME, clientName );
    objectMap.put( KEY_PROGRAM_NAME, clientName );
    // Set the recipients on the mailing
    mailing.addMailingRecipient( mailingRecipient );
    if ( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR.equals( message.getCmAssetCode() ) )
    {
      MailingRecipient mailingSSIAdminRecipient = null;
      if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ) != null )
      {
        mailingSSIAdminRecipient = getMailingRecipient( participant );
        mailingSSIAdminRecipient.setUser( null );
        mailingSSIAdminRecipient.addMailingRecipientDataFromMap( objectMap );
        mailingSSIAdminRecipient
            .setPreviewEmailAddress( getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ).getUserID() ).getEmailAddr() );
        mailing.addMailingRecipient( mailingSSIAdminRecipient );
      }
    }
    mailingService.submitMailing( mailing, objectMap );
  }

  private Map<String, Object> setLaunchStackRankContestDataForCreator( SSIContest contest ) throws ServiceErrorException
  {
    List<SSIContestProgressValueBean> contestProgressData = this.ssiContestParticipantService.getContestProgress( contest.getId(), null );
    SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
    Map<String, Object> dataMap = new HashMap<String, Object>();
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    dataMap.put( KEY_ACTIVITY_GOAL_AMOUNT, SSIContestUtil.getFormattedValue( contest.getContestGoal(), precision ) + getActivitySuffix( contest ) );
    dataMap.put( KEY_MAX_PAYOUT, SSIContestUtil.getFormattedValue( contestProgressValueBean.getPayoutCap(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
    return dataMap;
  }

  private void sendProgramLaunchNotification( SSIPromotion promotion, List<PromotionNotificationType> notifications )
  {
    for ( PromotionNotificationType promotionNotificationType : notifications )
    {
      // Process only when a notification has been set up on the promotion
      if ( promotionNotificationType.getNotificationMessageId() > 0 )
      {
        String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
        if ( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR.equals( notificationTypeCode ) )
        {
          try
          {
            createProgramLaunchNotification( promotion, promotionNotificationType );
          }
          catch( Exception ex )
          {
            log.error( "An exception occurred while sending notifications for promotion: " + getPromotionName( promotion.getCmAssetCode(), systemVariableService.getDefaultLanguage().getStringVal() )
                + " (process invocation ID = " + getProcessInvocationId() + ")", ex );
            addComment( "An exception occurred while sending notifications for contest: " + getPromotionName( promotion.getCmAssetCode(), systemVariableService.getDefaultLanguage().getStringVal() )
                + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
          }
          break;
        }
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  private void createProgramLaunchNotification( SSIPromotion promotion, PromotionNotificationType promotionNotificationType )
  {
    if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), promotion.getSubmissionStartDate() ) )
    {
      Set paxs = new HashSet();
      log.debug( "SSI Contest Set Up Launch Notification" );

      // Retrieve contest creator audiences for the promotion.
      paxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );

      List<Participant> contestCreators = getRecipients( paxs );

      Message message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
      MailingType mailingType = MailingType.lookup( MailingType.PROMOTION );

      for ( Participant contestCreator : contestCreators )
      {
        Mailing mailing = composeMail();
        mailing.setMessage( message );
        mailing.setMailingType( mailingType );
        MailingRecipient mailingRecipient = getMailingRecipient( contestCreator );
        Map<String, Object> objectMap = new HashMap<String, Object>();
        setLaunchPromotionData( objectMap, contestCreator, promotion, mailingRecipient.getLocale() );
        // Set the recipients on the mailing
        mailing.addMailingRecipient( mailingRecipient );
        mailingService.submitMailing( mailing, objectMap );
      }
    }
  }

  private void setLaunchPromotionData( Map<String, Object> objectMap, Participant contestCreator, Promotion promo, String locale )
  {
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    objectMap.put( KEY_FIRST_NAME, contestCreator.getFirstName() );
    objectMap.put( KEY_PROMOTION_NAME, getPromotionName( promo.getPromoNameAssetCode(), locale ) );
    objectMap.put( KEY_PROGRAM_URL, siteLink );
    objectMap.put( KEY_PROGRAM_NAME, programName );
  }

  private String getPromotionName( String promoNameAssetCode, String userLocale )
  {
    Locale locale = getLocale( userLocale );
    return cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
  }

  // assuming number of days check already done before calling this method
  private boolean isEligibleforNotification( PromotionNotificationType promoNotification )
  {
    PromotionNotificationFrequencyType frequency = promoNotification.getPromotionNotificationFrequencyType();

    // null check
    if ( null == frequency )
    {
      return false;
    }
    else if ( frequency.isDaily() )
    {
      return true;
    }
    else if ( frequency.isWeekly() )
    {
      // Is today the day of the week to send?
      Calendar cal = new GregorianCalendar();
      int todayOfWeek = cal.get( Calendar.DAY_OF_WEEK ); // 1=Sunday, 2=Monday, ...
      if ( promoNotification.getDayOfWeekType().getCode().equals( Integer.toString( todayOfWeek ) ) )
      {
        return true;
      }
    }
    else if ( frequency.isMonthly() )
    {
      // Is today the day of the week to send?
      Calendar cal = new GregorianCalendar();
      int todayOfMonth = cal.get( Calendar.DAY_OF_MONTH );
      if ( promoNotification.getDayOfMonth() == todayOfMonth )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isLaunchNotificationSendForAllPax( SSIContest contest, String paxRole )
  {
    if ( paxRole.equalsIgnoreCase( PARTICIPANT ) )
    {
      Set<SSIContestParticipant> contestParticipants = contest.getContestParticipants();
      return contestParticipants.stream().anyMatch( p -> !p.isLaunchNotificationSent() );

    }
    else if ( paxRole.equalsIgnoreCase( MANAGER ) )
    {
      Set<SSIContestManager> contestManagers = contest.getContestManagers();
      return contestManagers.stream().anyMatch( p -> !p.isLaunchNotificationSent() );
    }
    return false;
  }

  private boolean setLaunchSuccessful( SSIContest contest )
  {
    boolean isLaunchNotificationSendForManager = isLaunchNotificationSendForAllPax( contest, MANAGER );
    boolean isLaunchNotificationSendForPax = isLaunchNotificationSendForAllPax( contest, PARTICIPANT );
    if ( isLaunchNotificationSendForManager || isLaunchNotificationSendForPax )
    {
      return true;
    }
    return false;
  }
}
