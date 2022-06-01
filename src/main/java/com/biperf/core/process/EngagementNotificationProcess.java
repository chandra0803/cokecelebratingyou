
package com.biperf.core.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.engagement.EngagementEligManager;
import com.biperf.core.domain.engagement.EngagementEligUser;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.EngagementNotificationValueBean;
import com.objectpartners.cms.util.CmsUtil;

/**
 * Process to create RPM notifications for the Participant and Manager.
 * EngagementNotificationProcess.
 * 
 * @author kandhi
 * @since Sep 12, 2014
 * @version 1.0
 */
public class EngagementNotificationProcess extends BaseProcessImpl
{
  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "engagementNotificationProcess";
  public static final String PROCESS_NAME = "Engagement Notification Process";

  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";

  private static final Log log = LogFactory.getLog( EngagementRefreshScoresProcess.class );

  private EngagementService engagementService;
  private PromotionService promotionService;
  protected PromotionNotificationService promotionNotificationService;

  int managerSuccessCount = 0;
  int paxSuccessCount = 0;
  int totalEligManagers = 0;
  int totalEligPax = 0;

  protected void onExecute()
  {
    try
    {
      Map<String, Object> queryParams = new HashMap<String, Object>();
      queryParams.put( "startDate", DateUtils.getFirstDayOfPreviousMonth() );
      queryParams.put( "endDate", DateUtils.getLastDayOfPreviousMonth() );
      // Adding 1 to month as end month in procedure needs month+1
      queryParams.put( "endMonth", DateUtils.getMonthFromDate( DateUtils.getFirstDayOfPreviousMonth() ) + 1 );
      queryParams.put( "endYear", DateUtils.getYearFromDate( DateUtils.getFirstDayOfPreviousMonth() ) );
      // Notifications for manager
      queryParams.put( "mode", "team" );

      PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
      promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.ENGAGEMENT ) } );
      promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
      List promotions = promotionService.getPromotionList( promoQueryConstraint );
      Promotion promotion = (Promotion)promotions.get( 0 );
      PromotionNotificationType promotionManagerNotificationType = null;
      PromotionNotificationType promotionParticipantNotificationType = null;
      ArrayList notifications = (ArrayList)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() );

      promotionManagerNotificationType = fetchNotification( notifications, PromotionEmailNotificationType.KPM_MANAGERS_UPDATE );
      if ( promotionManagerNotificationType != null )
      {
        List<EngagementEligManager> eligManagers = getEngagementService().getAllEligibleManagers();
        if ( eligManagers != null )
        {
          totalEligManagers = eligManagers.size();
          for ( EngagementEligManager eligManager : eligManagers )
          {
            try
            {
              boolean isSuccess = process( queryParams, eligManager.getId(), managerSuccessCount );
              if ( isSuccess )
              {
                managerSuccessCount++;
              }
            }
            catch( Exception e )
            {
              log.error( "An exception occurred in " + PROCESS_NAME + " while retrieving the notifications data for manager " + eligManager.getId() + "-" + "(process invocation ID = "
                  + getProcessInvocationId() + ")", e );
            }
          }
        }
      }

      // Notifications for Participants
      promotionParticipantNotificationType = fetchNotification( notifications, PromotionEmailNotificationType.KPM_PARTICIPANT_UPDATE );

      queryParams.put( "mode", "user" );
      if ( promotionParticipantNotificationType != null )
      {
        List<EngagementEligUser> eligUsers = getEngagementService().getAllEligibleParticipants();
        if ( eligUsers != null )
        {
          totalEligPax = eligUsers.size();
          for ( EngagementEligUser eligUser : eligUsers )
          {
            try
            {

              boolean isSuccess = process( queryParams, eligUser.getId(), paxSuccessCount );
              if ( isSuccess )
              {
                paxSuccessCount++;
              }
            }
            catch( Exception e )
            {
              log.error( "An exception occurred in " + PROCESS_NAME + " while retrieving the notifications data for participant " + eligUser.getId() + "-" + "(process invocation ID = "
                  + getProcessInvocationId() + ")", e );
            }
          }
        }
      }
    }
    catch( Exception e )
    {
      log.error( "An exception occurred in " + PROCESS_NAME + " while retrieving the notifications data. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred in " + PROCESS_NAME + " while retrieving the notifications data." + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
    sendSummaryMessage();
  }

  private PromotionNotificationType fetchNotification( List notifications, String notificationCode )
  {
    // For each Promotion Notification
    for ( Iterator notificationsIter = notifications.iterator(); notificationsIter.hasNext(); )
    {
      PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationsIter.next();

      long messageId = promotionNotificationType.getNotificationMessageId();

      // Process only when a notification has been set up on the promotion
      if ( messageId > 0 )
      {
        String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
        log.debug( "--------------------------------------------------------------------------------" );
        log.debug( "process :" + BEAN_NAME );
        log.debug( "notification type:" + notificationTypeCode );

        /* Promotion Program Launch Alert - send to Pax X days prior to the promotion starts */

        // Notification Type : Promotion Program Launch Alert
        // Promotion Status : Complete
        if ( notificationTypeCode.equals( notificationCode ) )
        {
          return promotionNotificationType;
        }
      }
    }
    return null;
  }

  protected boolean process( Map<String, Object> queryParams, Long userId, int successCount )
  {
    queryParams.put( "userId", userId );
    // Get the notification Data for each user
    Map<String, Object> returnMap = getEngagementService().getNotificationsData( queryParams );
    if ( BAD_OUTPUT.equals( returnMap.get( OUTPUT_RETURN_CODE ) ) )
    {
      log.error( "Oracle returned bad out put for " + PROCESS_NAME + " while retrieving the notifications data. " + "User ID = " + userId + ")" );
    }
    else
    {
      // Send the e-mail.
      return sendMessage( returnMap, queryParams, successCount );
    }
    return false;
  }

  protected void sendSummaryMessage()
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", PROCESS_NAME );
    objectMap.put( "managerSuccessCount", managerSuccessCount );
    objectMap.put( "paxSuccessCount", new Long( paxSuccessCount ) );
    objectMap.put( "totalEligManagers", totalEligManagers );
    objectMap.put( "totalEligPax", new Long( totalEligPax ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Message m = messageService.getMessageByCMAssetCode( MessageService.ENGAGEMENT_NOTIFICATION_SUMMARY );
    Mailing mailing = composeMail( m.getId(), MailingType.PROMOTION );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "number of recipient managers: " + managerSuccessCount );
      log.debug( "number of recipient participants: " + paxSuccessCount );
      log.debug( "total number of managers: " + totalEligManagers );
      log.debug( "total number of participants: " + totalEligPax );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "process: " + BEAN_NAME + " has been sent to the process scheduler " + recipientUser.getFirstName() + " " + recipientUser.getLastName() + " Number of recipient managers: "
          + managerSuccessCount + " Number of recipient participants: " + paxSuccessCount + " Total number of managers: " + totalEligManagers + " Total number of participants: " + totalEligPax );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  private boolean sendMessage( Map<String, Object> results, Map<String, Object> queryParams, int successCount )
  {
    boolean isSuccess = false;
    EngagementNotificationValueBean engagementNotificationValueBean = (EngagementNotificationValueBean)results.get( "p_out_result_set1" );

    if ( engagementNotificationValueBean != null )
    {
      if ( !StringUtil.isNullOrEmpty( engagementNotificationValueBean.getEmailAddress() ) )
      {
        // Compose the mailing.
        Mailing mailing = null;
        boolean isTeam = false;
        if ( "team".equals( queryParams.get( "mode" ) ) )
        {
          isTeam = true;
          mailing = composeMail( MessageService.RPM_NOTIFICATION_EMAIL_MESSAGE_FOR_TEAM_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
        }
        else
        {
          mailing = composeMail( MessageService.RPM_NOTIFICATION_EMAIL_MESSAGE_FOR_USER_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
        }

        // Collect e-mail message parameters.
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put( "isTeam", isTeam );
        objectMap.put( "firstName", engagementNotificationValueBean.getFirstName() );
        objectMap.put( "startDate", DateUtils.toDisplayString( (Date)queryParams.get( "startDate" ), CmsUtil.getLocale( engagementNotificationValueBean.getUserLocale() ) ) );
        objectMap.put( "endDate", DateUtils.toDisplayString( (Date)queryParams.get( "endDate" ), CmsUtil.getLocale( engagementNotificationValueBean.getUserLocale() ) ) );
        objectMap.put( "isScoreActive", engagementNotificationValueBean.isScoreActive() );
        objectMap.put( "companyGoal", engagementNotificationValueBean.getCompanyGoal() );
        objectMap.put( "score", engagementNotificationValueBean.getScore() );
        objectMap.put( "companyAverage", engagementNotificationValueBean.getCompanyAvg() );
        objectMap.put( "sentTarget", engagementNotificationValueBean.getSentTarget() );
        objectMap.put( "receivedTarget", engagementNotificationValueBean.getReceivedTarget() );
        objectMap.put( "connectedToTarget", engagementNotificationValueBean.getConnectedToTarget() );
        objectMap.put( "connectedByTarget", engagementNotificationValueBean.getConnectedByTarget() );
        objectMap.put( "loginTarget", engagementNotificationValueBean.getLoginActivityTarget() );
        objectMap.put( "totalTeamMemCnt", engagementNotificationValueBean.getTotalMembersAvailable() );
        objectMap.put( "achievedCnt", engagementNotificationValueBean.getAchievedCnt() );
        objectMap.put( "notAchievedCnt", engagementNotificationValueBean.getTotalMembersAvailable() - engagementNotificationValueBean.getAchievedCnt() );
        objectMap.put( "orgLevel", engagementNotificationValueBean.getOrgLevel() );
        objectMap.put( "sentCnt", engagementNotificationValueBean.getSentCnt() );
        objectMap.put( "receivedCnt", engagementNotificationValueBean.getReceivedCnt() );
        objectMap.put( "connectedToCnt", engagementNotificationValueBean.getConnectedToCnt() );
        objectMap.put( "connectedByCnt", engagementNotificationValueBean.getConnectedByCnt() );
        objectMap.put( "loginCnt", engagementNotificationValueBean.getLoginActivityCnt() );
        objectMap.put( "sentDiff", engagementNotificationValueBean.getSentCnt() - engagementNotificationValueBean.getSentTarget() );
        objectMap.put( "receivedDiff", engagementNotificationValueBean.getReceivedCnt() - engagementNotificationValueBean.getReceivedTarget() );
        objectMap.put( "connectedToDiff", engagementNotificationValueBean.getConnectedToCnt() - engagementNotificationValueBean.getConnectedToTarget() );
        objectMap.put( "connectedByDiff", engagementNotificationValueBean.getConnectedByCnt() - engagementNotificationValueBean.getConnectedByTarget() );
        objectMap.put( "loginDiff", engagementNotificationValueBean.getLoginActivityCnt() - engagementNotificationValueBean.getLoginActivityTarget() );
        objectMap.put( "recvCompanyAvg", engagementNotificationValueBean.getRecvCompanyAvg() );
        objectMap.put( "sentCompanyAvg", engagementNotificationValueBean.getSentCompanyAvg() );
        objectMap.put( "connByCompanyAvg", engagementNotificationValueBean.getConnFromCompanyAvg() );
        objectMap.put( "connToCompanyAvg", engagementNotificationValueBean.getConnToCompanyAvg() );
        objectMap.put( "loginCompanyAvg", engagementNotificationValueBean.getLoginCompanyAvg() );
        objectMap.put( "recvTeamAvg", engagementNotificationValueBean.getRecvTeamAvg() );
        objectMap.put( "sentTeamAvg", engagementNotificationValueBean.getSentTeamAvg() );
        objectMap.put( "connByTeamAvg", engagementNotificationValueBean.getConnFromTeamAvg() );
        objectMap.put( "connToTeamAvg", engagementNotificationValueBean.getConnToTeamAvg() );
        objectMap.put( "loginTeamAvg", engagementNotificationValueBean.getLoginTeamAvg() );
        objectMap.put( "url", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

        // Add the recipient.
        MailingRecipient mailingRecipient = addRecipient( engagementNotificationValueBean.getEmailAddress() );
        mailingRecipient.setLocale( engagementNotificationValueBean.getUserLocale() );
        mailingRecipient.setUser( getUserService().getUserById( (long)queryParams.get( "userId" ) ) );
        mailing.addMailingRecipient( mailingRecipient );

        // Send the e-mail message.
        mailingService.submitMailing( mailing, objectMap );
        isSuccess = true;
        log.debug( "------------------------------------------------------------------------------" );
        log.debug( "Process " + BEAN_NAME + " completed for user " + engagementNotificationValueBean.getFirstName() + "." ); // Manager-name
        log.debug( "------------------------------------------------------------------------------" );
      }
    }
    return isSuccess;
  }

  public EngagementService getEngagementService()
  {
    return engagementService;
  }

  public void setEngagementService( EngagementService engagementService )
  {
    this.engagementService = engagementService;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public PromotionNotificationService getPromotionNotificationService()
  {
    return promotionNotificationService;
  }

  public void setPromotionNotificationService( PromotionNotificationService promotionNotificationService )
  {
    this.promotionNotificationService = promotionNotificationService;
  }

}
