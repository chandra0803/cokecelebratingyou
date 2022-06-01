/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

public class PromoRecPictureData
{
  private Long promotionId;
  private String contentResource;
  private String pictureUrl;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getContentResource()
  {
    return contentResource;
  }

  public void setContentResource( String contentResource )
  {
    this.contentResource = contentResource;
  }

  public String getPictureUrl()
  {
    return pictureUrl;
  }

  public void setPictureUrl( String pictureUrl )
  {
    this.pictureUrl = pictureUrl;
  }

}
