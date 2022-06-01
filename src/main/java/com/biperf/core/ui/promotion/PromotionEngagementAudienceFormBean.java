
package com.biperf.core.ui.promotion;

/**
 * PromotionEngagementAudienceFormBean.
 * 
 * @author kandhi
 * @since Jul 31, 2014
 * @version 1.0
 */
public class PromotionEngagementAudienceFormBean
{
  private String audienceKey;
  private Long eligiblePromotionId;
  private String audienceType;
  private String audienceName;
  private String promotionName;

  public String getAudienceKey()
  {
    return audienceKey;
  }

  public void setAudienceKey( String audienceKey )
  {
    this.audienceKey = audienceKey;
  }

  public Long getEligiblePromotionId()
  {
    return eligiblePromotionId;
  }

  public void setEligiblePromotionId( Long eligiblePromotionId )
  {
    this.eligiblePromotionId = eligiblePromotionId;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public String getAudienceName()
  {
    return audienceName;
  }

  public void setAudienceName( String audienceName )
  {
    this.audienceName = audienceName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

}
