/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/plateauawards/PlateauAwardsRedeemBean.java,v $
 */

package com.biperf.core.domain.plateauawards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author poddutur
 * @since Oct 9, 2015
 */
public class PlateauAwardsRedeemBean
{
  private String promotionName;
  private Long endDate;
  private String catalogUrl;

  @JsonProperty( "name" )
  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Long endDate )
  {
    this.endDate = endDate;
  }

  public String getCatalogUrl()
  {
    return catalogUrl;
  }

  public void setCatalogUrl( String catalogUrl )
  {
    this.catalogUrl = catalogUrl;
  }

}
