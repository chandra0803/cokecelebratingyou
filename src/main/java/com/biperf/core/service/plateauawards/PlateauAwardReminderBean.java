
package com.biperf.core.service.plateauawards;

import java.util.Date;

import com.biperf.core.domain.participant.Participant;

public class PlateauAwardReminderBean
{
  private Long merchOrderId;
  private Participant participant;
  private String promotionName;
  private Date dateIssued;
  private Date dateReminded;
  private String awardLevel;

  public Long getMerchOrderId()
  {
    return merchOrderId;
  }

  public void setMerchOrderId( Long merchOrderId )
  {
    this.merchOrderId = merchOrderId;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getDateIssued()
  {
    return dateIssued;
  }

  public void setDateIssued( Date dateIssued )
  {
    this.dateIssued = dateIssued;
  }

  public Date getDateReminded()
  {
    return dateReminded;
  }

  public void setDateReminded( Date dateReminded )
  {
    this.dateReminded = dateReminded;
  }

  public String getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( String awardLevel )
  {
    this.awardLevel = awardLevel;
  }
}
