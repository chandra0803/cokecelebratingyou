/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/PromotionNotificationType.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionNotificationFrequencyType;

/**
 * PromotionNotificationType.
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
public class PromotionNotificationType extends PromotionNotification
{
  private PromotionEmailNotificationType promotionEmailNotificationType;
  private Integer numberOfDays;
  private PromotionNotificationFrequencyType promotionNotificationFrequencyType;
  private DayOfWeekType dayOfWeekType;
  private String descriminator;
  private int dayOfMonth;

  public PromotionNotificationType()
  {
    // empty constructor
  }

  public PromotionNotificationType( PromotionNotificationType promotionNotificationToCopy )
  {
    this.setNotificationMessageId( promotionNotificationToCopy.getNotificationMessageId() );
    this.promotionEmailNotificationType = promotionNotificationToCopy.getPromotionEmailNotificationType();
    this.numberOfDays = promotionNotificationToCopy.getNumberOfDays();
    this.descriminator = promotionNotificationToCopy.getDescriminator();
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
    if ( ! ( o instanceof PromotionNotificationType ) )
    {
      return false;
    }

    final PromotionNotificationType promotionNotificationType = (PromotionNotificationType)o;

    if ( getPromotion() != null )
    {
      if ( !getPromotion().equals( promotionNotificationType.getPromotion() ) )
      {
        return false;
      }
    }
    if ( getPromotionEmailNotificationType() != null )
    {
      if ( !getPromotionEmailNotificationType().equals( promotionNotificationType.getPromotionEmailNotificationType() ) )
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
    result += this.getPromotionEmailNotificationType() != null ? this.getPromotionEmailNotificationType().hashCode() : 0;

    return result;
  }

  public Integer getNumberOfDays()
  {
    return numberOfDays;
  }

  public void setNumberOfDays( Integer numberOfDays )
  {
    this.numberOfDays = numberOfDays;
  }

  public PromotionEmailNotificationType getPromotionEmailNotificationType()
  {
    return promotionEmailNotificationType;
  }

  public void setPromotionEmailNotificationType( PromotionEmailNotificationType promotionEmailNotificationType )
  {
    this.promotionEmailNotificationType = promotionEmailNotificationType;
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
    PromotionNotificationType promotionNotification = (PromotionNotificationType)super.clone();

    return promotionNotification;
  }

  public DayOfWeekType getDayOfWeekType()
  {
    return dayOfWeekType;
  }

  public void setDayOfWeekType( DayOfWeekType dayOfWeekType )
  {
    this.dayOfWeekType = dayOfWeekType;
  }

  public PromotionNotificationFrequencyType getPromotionNotificationFrequencyType()
  {
    return promotionNotificationFrequencyType;
  }

  public void setPromotionNotificationFrequencyType( PromotionNotificationFrequencyType promotionNotificationFrequencyType )
  {
    this.promotionNotificationFrequencyType = promotionNotificationFrequencyType;
  }

  public String getDescriminator()
  {
    return descriminator;
  }

  public void setDescriminator( String descriminator )
  {
    this.descriminator = descriminator;
  }

  public int getDayOfMonth()
  {
    return dayOfMonth;
  }

  public void setDayOfMonth( int dayOfMonth )
  {
    this.dayOfMonth = dayOfMonth;
  }
}
