
package com.biperf.core.value.serviceanniversary;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ServiceAwardSiteView implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean success;

  private String serviceAwardsSiteUrl;

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

  public String getServiceAwardsSiteUrl()
  {
    return serviceAwardsSiteUrl;
  }

  public void setServiceAwardsSiteUrl( String serviceAwardsSiteUrl )
  {
    this.serviceAwardsSiteUrl = serviceAwardsSiteUrl;
  }

  @Override
  public String toString()
  {
    return "ServiceAwardSiteView [serviceAwardsSiteUrl=" + serviceAwardsSiteUrl + "]";
  }

}
