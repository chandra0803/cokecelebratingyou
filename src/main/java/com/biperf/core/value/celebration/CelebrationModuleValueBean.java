/**
 * 
 */

package com.biperf.core.value.celebration;

/**
 * @author poddutur
 *
 */
public class CelebrationModuleValueBean
{
  private Long claimId;
  private String promotionName;
  private String eCardUrl;
  private Integer anniversaryNumberOfYears;
  private Integer anniversaryNumberOfDays;
  private String celebrationPageUrl;
  private String customFormElementsInfo;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String geteCardUrl()
  {
    return eCardUrl;
  }

  public void seteCardUrl( String eCardUrl )
  {
    this.eCardUrl = eCardUrl;
  }

  public Integer getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( Integer anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public Integer getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( Integer anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public String getCelebrationPageUrl()
  {
    return celebrationPageUrl;
  }

  public void setCelebrationPageUrl( String celebrationPageUrl )
  {
    this.celebrationPageUrl = celebrationPageUrl;
  }

  public String getCustomFormElementsInfo()
  {
    return customFormElementsInfo;
  }

  public void setCustomFormElementsInfo( String customFormElementsInfo )
  {
    this.customFormElementsInfo = customFormElementsInfo;
  }

}
