
package com.biperf.core.process;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.StringUtil;

public class InactivityAlertsProcess extends ProactiveEmailProcess
{
  private static final Log log = LogFactory.getLog( InactivityAlertsProcess.class );

  public static final String PROCESS_NAME = "Inactivity Alerts Process";
  public static final String BEAN_NAME = "inactivityAlertsProcess";

  private PromotionService promotionService;

  private String promotionId;
  private String messageId;

  public void onExecute()
  {
    PromotionNotification promoNotification = null;

    if ( !StringUtil.isEmpty( promotionId ) )
    {
      try
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
        Promotion currentPromotion = promotionService.getPromotionByIdWithAssociations( Long.valueOf( promotionId ), associationRequestCollection );
        if ( currentPromotion.getPromotionNotifications() != null && currentPromotion.getPromotionNotifications().size() > 0 )
        {
          Iterator notItr = currentPromotion.getPromotionNotifications().iterator();

          while ( notItr.hasNext() )
          {
            promoNotification = (PromotionNotification)notItr.next();
            if ( promoNotification.getNotificationMessageId() == Long.valueOf( messageId ) )
            {
              break;
            }
          }
          // ProactiveEmailProcess proactiveEmail=(ProactiveEmailProcess)BeanLocator.getBean(
          // "proactiveEmailProcessTarget" );
          int numberOfPaxes = processProActive( currentPromotion, (PromotionNotificationType)promoNotification, true, true );

        }

      }

      catch( Exception e )
      {
        log.error( "Cannot process inactivity alerts:" + e );
      }
    }
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

}
