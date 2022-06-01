/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionNotification.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

/**
 * PromotionNotification.
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
 * <td>sedey</td>
 * <td>Aug 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class PromotionNotification extends BaseDomain implements Cloneable
{
  private Promotion promotion;
  private long notificationMessageId;

  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionNotification [" );
    sb.append( "{promotion=" + this.getPromotion().getName() + "}, " );
    sb.append( "{notificationMessageId=" + this.getNotificationMessageId() + "}, " );
    sb.append( "]" );

    return sb.toString();

  }

  public long getNotificationMessageId()
  {
    return notificationMessageId;
  }

  public void setNotificationMessageId( long notificationMessageId )
  {
    this.notificationMessageId = notificationMessageId;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isClaimFormNotificationType()
  {
    return this instanceof ClaimFormNotificationType;
  }

  public boolean isPromotionNotificationType()
  {
    return this instanceof PromotionNotificationType;
  }

  /**
   * Clone this object and remove the promotion. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionNotification promotionNotification = (PromotionNotification)super.clone();
    promotionNotification.setPromotion( null );

    promotionNotification.resetBaseDomain();

    return promotionNotification;

  }

}
