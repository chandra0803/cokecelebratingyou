
package com.biperf.core.ui.api.mobile.shop;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MobileShopView
{
  @JsonProperty
  private boolean showShopLink;
  @JsonProperty
  private boolean showTravelLink;
  @JsonProperty
  private Long balance;
  @JsonProperty
  private String mediaType;
  @JsonProperty
  private Long returnCode;
  @JsonProperty
  private String returnMessage;
  @JsonProperty
  private String supplierType;

  public String getSupplierType()
  {
    return supplierType;
  }

  public void setSupplierType( String supplierType )
  {
    this.supplierType = supplierType;
  }

  public boolean isShowShopLink()
  {
    return showShopLink;
  }

  public void setShowShopLink( boolean showShopLink )
  {
    this.showShopLink = showShopLink;
  }

  public boolean isShowTravelLink()
  {
    return showTravelLink;
  }

  public void setShowTravelLink( boolean showTravelLink )
  {
    this.showTravelLink = showTravelLink;
  }

  public Long getBalance()
  {
    return balance;
  }

  public void setBalance( Long balance )
  {
    this.balance = balance;
  }

  public String getMediaType()
  {
    return mediaType;
  }

  public void setMediaType( String mediaType )
  {
    this.mediaType = mediaType;
  }

  public Long getReturnCode()
  {
    return returnCode;
  }

  public void setReturnCode( Long returnCode )
  {
    this.returnCode = returnCode;
  }

  public String getReturnMessage()
  {
    return returnMessage;
  }

  public void setReturnMessage( String returnMessage )
  {
    this.returnMessage = returnMessage;
  }


}
