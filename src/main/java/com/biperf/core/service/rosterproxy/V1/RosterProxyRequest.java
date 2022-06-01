/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.rosterproxy.V1;

import java.io.Serializable;

public class RosterProxyRequest implements Serializable
{
  private String companyId;
  private String endpoint;
  private String apiKey;
  private String apiSecret;

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public String getEndpoint()
  {
    return endpoint;
  }

  public void setEndpoint( String endpoint )
  {
    this.endpoint = endpoint;
  }

  public String getApiKey()
  {
    return apiKey;
  }

  public void setApiKey( String apiKey )
  {
    this.apiKey = apiKey;
  }

  public String getApiSecret()
  {
    return apiSecret;
  }

  public void setApiSecret( String apiSecret )
  {
    this.apiSecret = apiSecret;
  }

}
