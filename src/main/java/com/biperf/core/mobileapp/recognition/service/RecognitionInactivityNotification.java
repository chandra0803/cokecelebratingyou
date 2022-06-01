
package com.biperf.core.mobileapp.recognition.service;

public class RecognitionInactivityNotification
{

  private Long userId;

  private String localeCode;

  private String promotionName;

  private String deadline;

  public RecognitionInactivityNotification()
  {
    super();
  }

  public RecognitionInactivityNotification( Long userId, String localeCode, String promotionName, String deadline )
  {
    super();
    this.userId = userId;
    this.localeCode = localeCode;
    this.promotionName = promotionName;
    this.deadline = deadline;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getLocaleCode()
  {
    return localeCode;
  }

  public void setLocaleCode( String localeCode )
  {
    this.localeCode = localeCode;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getDeadline()
  {
    return deadline;
  }

  public void setDeadline( String deadline )
  {
    this.deadline = deadline;
  }

}
