/**
 * 
 */

package com.biperf.core.value.recognition;

import java.util.Date;

public class PurlRecipientValue implements java.io.Serializable
{

  private static final long serialVersionUID = 1L;

  private Long purlRecipientId;
  private Long userId;
  private Long promotionId;
  private String anniversary;
  private Date awardDate;

  /**
   * @param userId
   * @param promotionId
   */
  public PurlRecipientValue( Long purlRecipientId, Long userId, Long promotionId, String anniversary, Date awardDate )
  {
    super();
    this.purlRecipientId = purlRecipientId;
    this.userId = userId;
    this.promotionId = promotionId;
    this.anniversary = anniversary;
    this.awardDate = awardDate;
  }

  public PurlRecipientValue()
  {
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public String getAnniversary()
  {
    return anniversary;
  }

  public void setAnniversary( String anniversary )
  {
    this.anniversary = anniversary;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

}
