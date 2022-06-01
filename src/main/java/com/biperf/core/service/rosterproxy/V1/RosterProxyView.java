/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.rosterproxy.V1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RosterProxyView implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "responseCode" )
  private int responseCode;

  @JsonProperty( "responseMessage" )
  private String responseMessage;

  @JsonProperty( "developerMessage" )
  private String developerMessage;

  @JsonProperty( "success" )
  private boolean success;

  @JsonProperty( "errorCode" )
  private String errorCode;

  @JsonProperty( "companyId" )
  private String companyId;

  @JsonProperty( "endpoint" )
  private String endpoint;

  @JsonProperty( "apiKey" )
  private String apiKey;

  public int getResponseCode()
  {
    return responseCode;
  }

  public void setResponseCode( int responseCode )
  {
    this.responseCode = responseCode;
  }

  public String getResponseMessage()
  {
    return responseMessage;
  }

  public void setResponseMessage( String responseMessage )
  {
    this.responseMessage = responseMessage;
  }

  public String getDeveloperMessage()
  {
    return developerMessage;
  }

  public void setDeveloperMessage( String developerMessage )
  {
    this.developerMessage = developerMessage;
  }

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

  public String getErrorCode()
  {
    return errorCode;
  }

  public void setErrorCode( String errorCode )
  {
    this.errorCode = errorCode;
  }

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

}
