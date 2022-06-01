/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

public class PromoRecPicUpdatedData
{
  private Long promotionId;
  private String migratedUrl;
  private String contentResource;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getMigratedUrl()
  {
    return migratedUrl;
  }

  public void setMigratedUrl( String migratedUrl )
  {
    this.migratedUrl = migratedUrl;
  }
  
  public String getContentResource()
  {
    return contentResource;
  }

  public void setContentResource( String contentResource )
  {
    this.contentResource = contentResource;
  }

}
