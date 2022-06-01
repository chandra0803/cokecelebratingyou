/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/ClaimFormNotificationType.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;

/**
 * ClaimFormNotificationType.
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
public class ClaimFormNotificationType extends PromotionNotification
{
  private ClaimFormStepEmailNotification claimFormStepEmailNotification;
  private ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType;

  public ClaimFormNotificationType()
  {
    // empty constructor
  }

  public ClaimFormNotificationType( ClaimFormNotificationType promotionNotificationToCopy )
  {
    this.setNotificationMessageId( promotionNotificationToCopy.getNotificationMessageId() );
    this.claimFormStepEmailNotification = promotionNotificationToCopy.getClaimFormStepEmailNotification();
    this.claimFormStepEmailNotificationType = promotionNotificationToCopy.getClaimFormStepEmailNotificationType();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof ClaimFormNotificationType ) )
    {
      return false;
    }

    final ClaimFormNotificationType claimFormNotificationType = (ClaimFormNotificationType)o;

    if ( getPromotion() != null )
    {
      if ( !getPromotion().equals( claimFormNotificationType.getPromotion() ) )
      {
        return false;
      }
    }
    if ( getClaimFormStepEmailNotification() != null )
    {
      if ( !getClaimFormStepEmailNotification().equals( claimFormNotificationType.getClaimFormStepEmailNotification() ) )
      {
        return false;
      }
    }
    if ( getClaimFormStepEmailNotificationType() != null )
    {
      if ( !getClaimFormStepEmailNotificationType().equals( claimFormNotificationType.getClaimFormStepEmailNotificationType() ) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = this.getPromotion() != null ? this.getPromotion().hashCode() * 13 : 0;
    result += this.getClaimFormStepEmailNotification() != null ? this.getClaimFormStepEmailNotification().hashCode() : 0;
    result += this.getClaimFormStepEmailNotificationType() != null ? this.getClaimFormStepEmailNotificationType().hashCode() : 0;
    return result;
  }

  public ClaimFormStepEmailNotification getClaimFormStepEmailNotification()
  {
    return claimFormStepEmailNotification;
  }

  public void setClaimFormStepEmailNotification( ClaimFormStepEmailNotification claimFormStepEmailNotification )
  {
    this.claimFormStepEmailNotification = claimFormStepEmailNotification;
  }

  public ClaimFormStepEmailNotificationType getClaimFormStepEmailNotificationType()
  {
    return claimFormStepEmailNotificationType;
  }

  public void setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType )
  {
    this.claimFormStepEmailNotificationType = claimFormStepEmailNotificationType;
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
    ClaimFormNotificationType promotionNotification = (ClaimFormNotificationType)super.clone();

    return promotionNotification;
  }

}
