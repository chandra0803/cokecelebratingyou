/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.hc;

import org.springframework.http.HttpMethod;

/**
 * TODO Javadoc for HCRequestDetails.
 * 
 * @author
 * @since Jun 19, 2017
 * @version 1.0
 */
public class HCRequestDetails
{
  private String product;
  private String version;
  private String clientCode;
  private String clientId;
  private String url;
  private HttpMethod httpMethod;
  private String encyrptedSaltKey;
  private String locale;
  
  private String userId;
  private String username;

  public String getClientCode()
  {
    return clientCode;
  }

  public void setClientCode( String clientCode )
  {
    this.clientCode = clientCode;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public HttpMethod getHttpMethod()
  {
    return httpMethod;
  }

  public void setHttpMethod( HttpMethod httpMethod )
  {
    this.httpMethod = httpMethod;
  }

  public String getEncyrptedSaltKey()
  {
    return encyrptedSaltKey;
  }

  public void setEncyrptedSaltKey( String encyrptedSaltKey )
  {
    this.encyrptedSaltKey = encyrptedSaltKey;
  }

  public String getProduct()
  {
    return product;
  }

  public void setProduct( String product )
  {
    this.product = product;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public String getClientId()
  {
    return clientId;
  }

  public void setClientId( String clientId )
  {
    this.clientId = clientId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername( String username )
  {
    this.username = username;
  }

}
