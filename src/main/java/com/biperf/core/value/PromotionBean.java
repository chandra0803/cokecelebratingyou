
package com.biperf.core.value;

/**
 * Alerts Performance Tuning
 * 
 * @author dudam
 * @since Jan 2, 2018
 * @version 1.0
 */
public class PromotionBean implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private Long promotionId;
  private String promotionType;

  public PromotionBean()
  {

  }

  public PromotionBean( Long promotionId, String promotionType )
  {
    this.promotionId = promotionId;
    this.promotionType = promotionType;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

}
