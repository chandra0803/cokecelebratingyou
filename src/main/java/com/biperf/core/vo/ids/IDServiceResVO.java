
package com.biperf.core.vo.ids;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class IDServiceResVO implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "access_token" )
  private String accessToken;

  @JsonProperty( "token_type" )
  private String tokenType;

  @JsonProperty( "sso_endpoint" )
  private String ssoEndpoint;

  @JsonProperty( "message" )
  private String message;

  public String getAccessToken()
  {
    return accessToken;
  }

  public void setAccessToken( String accessToken )
  {
    this.accessToken = accessToken;
  }

  public String getTokenType()
  {
    return tokenType;
  }

  public void setTokenType( String tokenType )
  {
    this.tokenType = tokenType;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getSsoEndpoint()
  {
    return ssoEndpoint;
  }

  public void setSsoEndpoint( String ssoEndpoint )
  {
    this.ssoEndpoint = ssoEndpoint;
  }

}
