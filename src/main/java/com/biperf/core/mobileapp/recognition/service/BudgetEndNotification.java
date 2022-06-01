
package com.biperf.core.mobileapp.recognition.service;

public class BudgetEndNotification
{

  private Long userId;

  private String localeCode;

  private String budgetRemaining;

  private String awardMedia;

  private String promotionName;

  private String endDate;

  public BudgetEndNotification()
  {
    super();
  }

  public BudgetEndNotification( Long userId, String localeCode, String budgetRemaining, String awardMedia, String promotionName, String endDate )
  {
    super();
    this.userId = userId;
    this.localeCode = localeCode;
    this.budgetRemaining = budgetRemaining;
    this.awardMedia = awardMedia;
    this.promotionName = promotionName;
    this.endDate = endDate;
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

  public String getBudgetRemaining()
  {
    return budgetRemaining;
  }

  public void setBudgetRemaining( String budgetRemaining )
  {
    this.budgetRemaining = budgetRemaining;
  }

  public String getAwardMedia()
  {
    return awardMedia;
  }

  public void setAwardMedia( String awardMedia )
  {
    this.awardMedia = awardMedia;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

}
