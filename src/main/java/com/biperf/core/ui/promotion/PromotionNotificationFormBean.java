/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionNotificationFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.promotion.Survey;

/**
 * PromotionNotificationFormBean.
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
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionNotificationFormBean implements Serializable, Comparable
{
  private Long promotionNotificationId;
  private String promotionNotificationType;
  private String notificationType;
  private String notificationTypeName;
  private long notificationMessageId;
  private Long claimFormStepEmailId;
  private String numberOfDays;
  private String promotionNotificationFrequencyType;
  private String promotionNotificationFrequencyTypeName;
  private String dayOfWeekType;
  private String dayOfWeekTypeName;
  private String descriminator = PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE;
  private String everyDaysAfterIssuance = "";
  private String numberOfDaysAfterPromoEnd;

  private String createdBy;
  private long dateCreated;
  private Long version = null;
  private int sequenceNbr;
  private int dayOfMonth;
  private boolean inactiveAlert;

  private Set<Survey> surveyLibrarySet = new LinkedHashSet<Survey>();

  // SSI Promotion Notification Params
  private String daysBeforeContestEnd;
  private String daysAfterContestCreated;
  private String frequencyContestCreated;
  private String daysClaimsPending;
  private String frequencyClaimsPending;
  private String daysAfterContestEnded;

  public PromotionNotificationFormBean()
  {
    // empty constructor
  }

  public Long getClaimFormStepEmailId()
  {
    return claimFormStepEmailId;
  }

  public void setClaimFormStepEmailId( Long claimFormStepEmailId )
  {
    this.claimFormStepEmailId = claimFormStepEmailId;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public long getNotificationMessageId()
  {
    return notificationMessageId;
  }

  public void setNotificationMessageId( long notificationMessageId )
  {
    this.notificationMessageId = notificationMessageId;
  }

  public String getNotificationType()
  {
    return notificationType;
  }

  public void setNotificationType( String notificationType )
  {
    this.notificationType = notificationType;
  }

  public String getNumberOfDays()
  {
    return numberOfDays;
  }

  public void setNumberOfDays( String numberOfDays )
  {
    this.numberOfDays = numberOfDays;
  }

  public Long getPromotionNotificationId()
  {
    return promotionNotificationId;
  }

  public void setPromotionNotificationId( Long promotionNotificationId )
  {
    this.promotionNotificationId = promotionNotificationId;
  }

  public String getPromotionNotificationType()
  {
    return promotionNotificationType;
  }

  public void setPromotionNotificationType( String promotionNotificationType )
  {
    this.promotionNotificationType = promotionNotificationType;
  }

  public int getSequenceNbr()
  {
    return sequenceNbr;
  }

  public void setSequenceNbr( int sequenceNbr )
  {
    this.sequenceNbr = sequenceNbr;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getNotificationTypeName()
  {
    return notificationTypeName;
  }

  public void setNotificationTypeName( String notificationTypeName )
  {
    this.notificationTypeName = notificationTypeName;
  }

  public String getDayOfWeekType()
  {
    return dayOfWeekType;
  }

  public void setDayOfWeekType( String dayOfWeekType )
  {
    this.dayOfWeekType = dayOfWeekType;
  }

  public String getDayOfWeekTypeName()
  {
    return dayOfWeekTypeName;
  }

  public void setDayOfWeekTypeName( String dayOfWeekTypeName )
  {
    this.dayOfWeekTypeName = dayOfWeekTypeName;
  }

  public String getPromotionNotificationFrequencyType()
  {
    return promotionNotificationFrequencyType;
  }

  public void setPromotionNotificationFrequencyType( String promotionNotificationFrequencyType )
  {
    this.promotionNotificationFrequencyType = promotionNotificationFrequencyType;
  }

  public String getPromotionNotificationFrequencyTypeName()
  {
    return promotionNotificationFrequencyTypeName;
  }

  public void setPromotionNotificationFrequencyTypeName( String promotionNotificationFrequencyTypeName )
  {
    this.promotionNotificationFrequencyTypeName = promotionNotificationFrequencyTypeName;
  }

  public String getDescriminator()
  {
    return descriminator;
  }

  public void setDescriminator( String descriminator )
  {
    this.descriminator = descriminator;
  }

  public String getEveryDaysAfterIssuance()
  {
    return everyDaysAfterIssuance;
  }

  public void setEveryDaysAfterIssuance( String everyDaysAfterIssuance )
  {
    this.everyDaysAfterIssuance = everyDaysAfterIssuance;
  }

  public String getNumberOfDaysAfterPromoEnd()
  {
    return numberOfDaysAfterPromoEnd;
  }

  public void setNumberOfDaysAfterPromoEnd( String numberOfDaysAfterPromoEnd )
  {
    this.numberOfDaysAfterPromoEnd = numberOfDaysAfterPromoEnd;
  }

  public int getDayOfMonth()
  {
    return dayOfMonth;
  }

  public void setDayOfMonth( int dayOfMonth )
  {
    this.dayOfMonth = dayOfMonth;
  }

  public boolean isInactiveAlert()
  {
    return inactiveAlert;
  }

  public void setInactiveAlert( boolean inactiveAlert )
  {
    this.inactiveAlert = inactiveAlert;
  }

  public String getDaysBeforeContestEnd()
  {
    return daysBeforeContestEnd;
  }

  public void setDaysBeforeContestEnd( String daysBeforeContestEnd )
  {
    this.daysBeforeContestEnd = daysBeforeContestEnd;
  }

  public String getDaysAfterContestEnded()
  {
    return daysAfterContestEnded;
  }

  public void setDaysAfterContestEnded( String daysAfterContestEnded )
  {
    this.daysAfterContestEnded = daysAfterContestEnded;
  }

  public String getDaysAfterContestCreated()
  {
    return daysAfterContestCreated;
  }

  public void setDaysAfterContestCreated( String daysAfterContestCreated )
  {
    this.daysAfterContestCreated = daysAfterContestCreated;
  }

  public String getFrequencyContestCreated()
  {
    return frequencyContestCreated;
  }

  public void setFrequencyContestCreated( String frequencyContestCreated )
  {
    this.frequencyContestCreated = frequencyContestCreated;
  }

  public String getDaysClaimsPending()
  {
    return daysClaimsPending;
  }

  public void setDaysClaimsPending( String daysClaimsPending )
  {
    this.daysClaimsPending = daysClaimsPending;
  }

  public String getFrequencyClaimsPending()
  {
    return frequencyClaimsPending;
  }

  public void setFrequencyClaimsPending( String frequencyClaimsPending )
  {
    this.frequencyClaimsPending = frequencyClaimsPending;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof PromotionNotificationFormBean ) )
    {
      throw new ClassCastException( "A PromotionNotificationFormBean was expected." );
    }
    PromotionNotificationFormBean promoNotification = (PromotionNotificationFormBean)object;
    Integer currentObjInactive = this.isInactiveAlert() ? 1 : 0;
    Integer newObjInactive = promoNotification.isInactiveAlert() ? 1 : 0;
    return currentObjInactive.compareTo( newObjInactive );

  }

  public Set<Survey> getSurveyLibrarySet()
  {
    return surveyLibrarySet;
  }

  public void setSurveyLibrarySet( Set<Survey> surveyLibrarySet )
  {
    this.surveyLibrarySet = surveyLibrarySet;
  }
}
