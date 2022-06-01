/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/PreliminaryProactiveEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;

public class PreliminaryProactiveEmailProcess extends ProactiveEmailProcess
{
  private static final Log log = LogFactory.getLog( PreliminaryProactiveEmailProcess.class );

  public static final String BEAN_NAME = "preliminaryProactiveEmailProcess";

  private String promotionId;

  private String[] promotionNotificationTypes;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    log.debug( "--------------------------------------------------------------------------------" );
    log.debug( "Running process : " + BEAN_NAME + " with PromotionID = " + promotionId );
    Promotion promotion = promotionService.getPromotionById( new Long( promotionId ) );
    List notificationTypes = new ArrayList();
    PromotionNotificationType promotionNotificationType = null;
    if ( promotionNotificationTypes != null )
    {
      notificationTypes = Arrays.asList( promotionNotificationTypes );
    }

    if ( promotion.isChallengePointPromotion() )
    {
      log.debug( "Promotion selected is challengepoint promotion " );
      ArrayList notifications = (ArrayList)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() );

      if ( notificationTypes.contains( PromotionEmailNotificationType.PROGRAM_LAUNCH + "_cp" ) && promotion.isComplete() )
      {
        promotionNotificationType = fetchNotification( notifications, PromotionEmailNotificationType.PROGRAM_LAUNCH );
        if ( promotionNotificationType != null )
        {
          long messageId = promotionNotificationType.getNotificationMessageId();
          int promoLaunchPaxCount = 0;
          Set paxs = new HashSet();
          // Retrieves a list of primary audience participants who are eligible for the given
          // promotion.
          paxs.addAll( getParticipantService().getAllEligiblePaxForPromotion( promotion.getId(), true ) );
          // Send Program Launch Email to paxss
          promoLaunchPaxCount = sendChallengepointLaunchMessage( paxs, messageId, (ChallengePointPromotion)promotion );
          // Add Comment to process invocation
          addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Program Launch Alert: " + promoLaunchPaxCount );
          log.debug( "Number of recipients attempted for Program Launch Alert: " + promoLaunchPaxCount );

        }
      }

      if ( notificationTypes.contains( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED ) && promotion.isLive() )
      {
        promotionNotificationType = fetchNotification( notifications, PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED );
        if ( promotionNotificationType != null )
        {
          long messageId = promotionNotificationType.getNotificationMessageId();
          int challengepointNotSelectedCount = 0;
          ChallengePointPromotion challengepointPromotion = (ChallengePointPromotion)promotion;
          if ( challengepointPromotion.getGoalCollectionEndDate() != null )
          {
            // Send Goal Not Selected Email to paxs
            challengepointNotSelectedCount = challengepointNotSelected( messageId, promotion );
            // Add Comment to process invocation
            addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Goal Not Selected Email: " + challengepointNotSelectedCount );
            log.debug( "Number of recipients attempted for Goal Not Selected Alert: " + challengepointNotSelectedCount );
          }
        }
      }

    }
    else if ( promotion.isGoalQuestPromotion() )
    {
      log.debug( "Promotion selected is goalquest promotion " );
      // Get Promotion Notifications
      ArrayList notifications = (ArrayList)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() );

      if ( notificationTypes.contains( PromotionEmailNotificationType.PROGRAM_LAUNCH + "_gq" ) && promotion.isComplete() )
      {
        promotionNotificationType = fetchNotification( notifications, PromotionEmailNotificationType.PROGRAM_LAUNCH );
        if ( promotionNotificationType != null )
        {
          long messageId = promotionNotificationType.getNotificationMessageId();
          int promoLaunchPaxCount = 0;
          Set paxs = new HashSet();
          // Retrieves a list of primary audience participants who are eligible for the given
          // promotion.
          paxs.addAll( getParticipantService().getAllEligiblePaxForPromotion( promotion.getId(), true ) );
          // Send Program Launch Email to paxss
          promoLaunchPaxCount = sendGoalquestLaunchMessage( paxs, messageId, (GoalQuestPromotion)promotion );
          // send partner Welcome email message
          long partnerMessageId = getGoalquestPartnerEmailMessageId();
          if ( ( (GoalQuestPromotion)promotion ).getPartnerAudienceType() != null && partnerMessageId > 0 )
          {
            long launchPartnerRecipientCount = sendPartnerLaunchMessage( partnerMessageId, (GoalQuestPromotion)promotion );
            promoLaunchPaxCount += launchPartnerRecipientCount;
            // Log Comment to process invocation
            String message = "Promotion: " + promotion.getName() + ". Number of Partner recipients attempted for Goalquest Partner Welcome Email : " + launchPartnerRecipientCount;
            log.debug( message );
          }
          // Add Comment to process invocation
          addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Program Launch Alert: " + promoLaunchPaxCount );
          log.debug( "Number of recipients attempted for Program Launch Alert: " + promoLaunchPaxCount );
        }
      }

      if ( notificationTypes.contains( PromotionEmailNotificationType.GOAL_NOT_SELECTED ) && promotion.isLive() )
      {
        promotionNotificationType = fetchNotification( notifications, PromotionEmailNotificationType.GOAL_NOT_SELECTED );
        if ( promotionNotificationType != null )
        {
          long messageId = promotionNotificationType.getNotificationMessageId();
          int goalNotSelectedPaxCount = 0;
          GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
          // Goal Selection End Date should be specified
          if ( goalQuestPromotion.getGoalCollectionEndDate() != null )
          {
            // Send Goal Not Selected Email to paxs
            goalNotSelectedPaxCount = goalNotSelected( messageId, promotion );
            // Add Comment to process invocation
            addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Goal Not Selected Email: " + goalNotSelectedPaxCount );
            log.debug( "Number of recipients attempted for Goal Not Selected Alert: " + goalNotSelectedPaxCount );
          }
        }
      }

    }
    else
    {
      log.warn( "Promotion selected is not goalquest promotion or challengepoint promotion " );
    }

    log.debug( "Completed process : " + BEAN_NAME + " with PromotionID = " + promotionId );
    log.debug( "--------------------------------------------------------------------------------" );

  } // end method onExecute()

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

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setPromotionNotificationTypes( String[] promotionNotificationTypes )
  {
    this.promotionNotificationTypes = promotionNotificationTypes;
  }

  public boolean isNotice()
  {
    return true;
  }

}
