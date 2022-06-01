
package com.biperf.core.service.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.BeanLocator;

/**
 * PromotionNotificationsUpdateAssociation is used to update promotions post-save on the Notification page.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Bala</td>
 * <td>May 03, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionNotificationsUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * @param detachedPromotion a detached {@link Promotion} object.
   */
  public PromotionNotificationsUpdateAssociation( Promotion detachedPromotion )
  {
    super( detachedPromotion );
  }

  /**
    * Use the detached {@link Promotion} object to update the attached {@link Promotion} object.
    * 
    * @param attachedDomain the attached version of the {@link Promotion} object.
    */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion detachedPromotion = (Promotion)getDetachedDomain();
    Promotion attachedPromotion = (Promotion)attachedDomain;

    if ( attachedPromotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion attachedGoalQuestPromo = (GoalQuestPromotion)attachedPromotion;
      GoalQuestPromotion detachedGoalQuestPromo = (GoalQuestPromotion)detachedPromotion;

      if ( detachedGoalQuestPromo.getPromotionGoalQuestSurveys() != null && detachedGoalQuestPromo.getPromotionGoalQuestSurveys().size() > 0 )
      {
        // Set of PromotionGoalQuestSurvey
        Set detachedPromotionGQSurvey = detachedGoalQuestPromo.getPromotionGoalQuestSurveys();
        Set promotionGQSurveyToAdd = new HashSet();
        for ( Iterator iterator = detachedGoalQuestPromo.getPromotionGoalQuestSurveys().iterator(); iterator.hasNext(); )
        {
          PromotionGoalQuestSurvey promotionGoalQuestSurvey = (PromotionGoalQuestSurvey)iterator.next();
          promotionGQSurveyToAdd.add( promotionGoalQuestSurvey );
        }
        // remove all of the PromotionGoalQuestSurvey that have been unchecked.
        Set attachPromotionGQSurvey = attachedGoalQuestPromo.getPromotionGoalQuestSurveys();
        Iterator attachedPromotionGQSurvey = attachPromotionGQSurvey.iterator();
        while ( attachedPromotionGQSurvey.hasNext() )
        {
          PromotionGoalQuestSurvey promotionGoalQuestSurvey = (PromotionGoalQuestSurvey)attachedPromotionGQSurvey.next();
          if ( !promotionGQSurveyToAdd.contains( promotionGoalQuestSurvey ) )
          {
            attachedPromotionGQSurvey.remove();
          }
        }

        // now add in the new promotionGoalQuestSurvey
        Iterator promotionGQSurveyToAddIterator = promotionGQSurveyToAdd.iterator();
        while ( promotionGQSurveyToAddIterator.hasNext() )
        {
          PromotionGoalQuestSurvey promotionGoalQuestSurvey = (PromotionGoalQuestSurvey)promotionGQSurveyToAddIterator.next();
          if ( !attachPromotionGQSurvey.contains( promotionGoalQuestSurvey ) )
          {
            attachedGoalQuestPromo.addPromotionGoalQuestSurvey( promotionGoalQuestSurvey );
          }
        }
      }
      else
      {
        // remove all of the PromotionGoalQuestSurvey.
        Set attachPromotionGQSurvey = attachedGoalQuestPromo.getPromotionGoalQuestSurveys();
        Iterator attachedPromotionGQSurvey = attachPromotionGQSurvey.iterator();
        while ( attachedPromotionGQSurvey.hasNext() )
        {
          PromotionGoalQuestSurvey promotionGoalQuestSurvey = (PromotionGoalQuestSurvey)attachedPromotionGQSurvey.next();
          attachedPromotionGQSurvey.remove();
        }
      }
    }

    if ( attachedPromotion instanceof ProductClaimPromotion )
    {
      if ( !attachedPromotion.hasParent() )// product claim child promotion notifications are not
                                           // editable.
      {
        this.executePromotion( attachedPromotion, detachedPromotion );
      }
    }
    else
    {
      this.executePromotion( attachedPromotion, detachedPromotion );
    }
  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedPromo
   * @param detachedPromo
   */
  private void executePromotion( Promotion attachedPromo, Promotion detachedPromo )
  {
    if ( attachedPromo.getPromotionNotifications() != null && attachedPromo.getPromotionNotifications().size() > 0 )
    {
      // if there are notifications, iterate over them to see if any need to be deleted.
      Iterator attachedPromoNotifications = attachedPromo.getPromotionNotifications().iterator();
      while ( attachedPromoNotifications.hasNext() )
      {
        // If the attached promo notification does not exist in the detached promotion
        // notification list, then it has been deleted and should be removed.
        PromotionNotification attachedPromoNotification = (PromotionNotification)attachedPromoNotifications.next();
        if ( !detachedPromo.getPromotionNotifications().contains( attachedPromoNotification ) )
        {
          attachedPromoNotifications.remove();
        }
      }

    }

    if ( detachedPromo.getPromotionNotifications() != null && detachedPromo.getPromotionNotifications().size() > 0 )
    {
      Iterator detachedPromoNotifications = detachedPromo.getPromotionNotifications().iterator();
      while ( detachedPromoNotifications.hasNext() )
      {
        PromotionNotification detachedPromoNotification = (PromotionNotification)detachedPromoNotifications.next();
        if ( detachedPromoNotification.getId() != null && detachedPromoNotification instanceof ClaimFormNotificationType )
        {
          ClaimFormDAO claimFormDAO = getClaimFormDAO();
          ( (ClaimFormNotificationType)detachedPromoNotification ).setClaimFormStepEmailNotification( claimFormDAO
              .getClaimFormStepEmailNotificationById( ( (ClaimFormNotificationType)detachedPromoNotification ).getClaimFormStepEmailNotification().getId() ) );
        }
        // If the attachedPromo already contains the notification option, we still need to iterate
        // through and
        // add any new notifications. Otherwise, we can just add the whole notification option.
        if ( attachedPromo.getPromotionNotifications().contains( detachedPromoNotification ) )
        {
          Iterator attachedPromoNotifications = attachedPromo.getPromotionNotifications().iterator();
          while ( attachedPromoNotifications.hasNext() )
          {
            PromotionNotification attachedPromoNotification = (PromotionNotification)attachedPromoNotifications.next();
            if ( attachedPromoNotification instanceof ClaimFormNotificationType && detachedPromoNotification instanceof ClaimFormNotificationType )
            {
              ClaimFormNotificationType attachedClaimFormNotification = (ClaimFormNotificationType)attachedPromoNotification;
              ClaimFormNotificationType detachedClaimFormNotification = (ClaimFormNotificationType)detachedPromoNotification;
              if ( attachedClaimFormNotification.equals( detachedClaimFormNotification ) )
              {
                attachedClaimFormNotification.setNotificationMessageId( detachedClaimFormNotification.getNotificationMessageId() );
              }
            }
            if ( attachedPromoNotification instanceof PromotionNotificationType && detachedPromoNotification instanceof PromotionNotificationType )
            {
              PromotionNotificationType attachedPromoNotificationType = (PromotionNotificationType)attachedPromoNotification;
              PromotionNotificationType detachedPromoNotificationType = (PromotionNotificationType)detachedPromoNotification;
              if ( attachedPromoNotificationType.equals( detachedPromoNotificationType ) )
              {
                attachedPromoNotificationType.setNotificationMessageId( detachedPromoNotificationType.getNotificationMessageId() );
                attachedPromoNotificationType.setNumberOfDays( detachedPromoNotificationType.getNumberOfDays() );
                attachedPromoNotificationType.setPromotionNotificationFrequencyType( detachedPromoNotificationType.getPromotionNotificationFrequencyType() );
                attachedPromoNotificationType.setDayOfWeekType( detachedPromoNotificationType.getDayOfWeekType() );
                attachedPromoNotificationType.setDescriminator( detachedPromoNotificationType.getDescriminator() );
                attachedPromoNotificationType.setDayOfMonth( detachedPromoNotificationType.getDayOfMonth() );
              }

            }

          }
        }
        else
        {
          attachedPromo.addPromotionNotification( detachedPromoNotification );
        }
      }
    }

  }

  /**
   * Returns a reference to the Audience DAO service.
   * 
   * @return a reference to the Audience DAO service.
   */
  private ClaimFormDAO getClaimFormDAO()
  {
    return (ClaimFormDAO)BeanLocator.getBean( ClaimFormDAO.BEAN_NAME );
  }
}
