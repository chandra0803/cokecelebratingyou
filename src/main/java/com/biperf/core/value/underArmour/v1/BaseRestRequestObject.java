
package com.biperf.core.value.underArmour.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BaseRestRequestObject extends BaseRestObject implements SecureToken
{

  private String appCode;
  private long clientPaxId;
  private String securityToken;

  public BaseRestRequestObject()
  {

  }

  public BaseRestRequestObject( String appCode )
  {
    super();
    this.appCode = appCode;
  }

  public BaseRestRequestObject( String appCode, long clientPaxId )
  {
    super();
    this.appCode = appCode;
    this.clientPaxId = clientPaxId;
  }

  public BaseRestRequestObject( String appCode, long clientPaxId, String securityToken )
  {
    super();
    this.appCode = appCode;
    this.clientPaxId = clientPaxId;
    this.securityToken = securityToken;
  }

  public String getSecurityToken()
  {
    return securityToken;
  }

  public void setSecurityToken( String securityToken )
  {
    this.securityToken = securityToken;
  }

  public String getAppCode()
  {
    return appCode;
  }

  public void setAppCode( String appCode )
  {
    this.appCode = appCode;
  }

  public long getClientPaxId()
  {
    return clientPaxId;
  }

  public void setClientPaxId( long clientPaxId )
  {
    this.clientPaxId = clientPaxId;
  }

  @Override
  public String getHashableAttribute()
  {
    String identifier = this.getAppCode();
    return this.getClientPaxId() > 0 ? identifier + "-" + this.getClientPaxId() : identifier;
  }

}