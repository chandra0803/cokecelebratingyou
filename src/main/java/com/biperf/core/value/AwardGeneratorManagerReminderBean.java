/**
 * 
 */

package com.biperf.core.value;

import java.util.Date;

/**
 * @author poddutur
 *
 */
public class AwardGeneratorManagerReminderBean
{
  private Long promotionId;
  private Long participantId;
  private Date createdDate;
  private Date expirationDate;
  private boolean notifyManager;
  private int numberOfDaysForAlert;
  private Long batchId;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Date getCreatedDate()
  {
    return createdDate;
  }

  public void setCreatedDate( Date createdDate )
  {
    this.createdDate = createdDate;
  }

  public Date getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( Date expirationDate )
  {
    this.expirationDate = expirationDate;
  }

  public boolean isNotifyManager()
  {
    return notifyManager;
  }

  public void setNotifyManager( boolean notifyManager )
  {
    this.notifyManager = notifyManager;
  }

  public int getNumberOfDaysForAlert()
  {
    return numberOfDaysForAlert;
  }

  public void setNumberOfDaysForAlert( int numberOfDaysForAlert )
  {
    this.numberOfDaysForAlert = numberOfDaysForAlert;
  }

  public Long getBatchId()
  {
    return batchId;
  }

  public void setBatchId( Long batchId )
  {
    this.batchId = batchId;
  }

}
