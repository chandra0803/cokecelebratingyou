/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ImageUrlResult
{
  private String url;
  private String proxyUrl;

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getProxyUrl()
  {
    return proxyUrl;
  }

  public void setProxyUrl( String proxyUrl )
  {
    this.proxyUrl = proxyUrl;
  }

}
