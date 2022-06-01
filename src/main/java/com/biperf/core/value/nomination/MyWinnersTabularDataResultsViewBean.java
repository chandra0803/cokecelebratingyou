
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyWinnersTabularDataResultsViewBean
{
  private String promotionName;
  private String dateWon;
  private String detailURL;

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getDateWon()
  {
    return dateWon;
  }

  public void setDateWon( String dateWon )
  {
    this.dateWon = dateWon;
  }

  @JsonProperty( "detailUrl" )
  public String getDetailURL()
  {
    return detailURL;
  }

  public void setDetailURL( String detailURL )
  {
    this.detailURL = detailURL;
  }

}
