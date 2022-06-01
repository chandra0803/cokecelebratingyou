
package com.biperf.core.value.serviceanniversary;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class GiftCodeSiteView implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean success;

  private String giftCodeSiteUrl;

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

  public String getGiftCodeSiteUrl()
  {
    return giftCodeSiteUrl;
  }

  public void setGiftCodeSiteUrl( String giftCodeSiteUrl )
  {
    this.giftCodeSiteUrl = giftCodeSiteUrl;
  }

  @Override
  public String toString()
  {
    return "GiftCodeSiteView [giftCodeSiteUrl=" + giftCodeSiteUrl + "]";
  }

}
