
package com.biperf.core.ui.hc;

public class SecureClientView
{
  private String clientCode;
  private String token;
  private String salt;
  private String farm;

  public String getClientCode()
  {
    return clientCode;
  }

  public void setClientCode( String clientCode )
  {
    this.clientCode = clientCode;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken( String token )
  {
    this.token = token;
  }

  public String getSalt()
  {
    return salt;
  }

  public void setSalt( String salt )
  {
    this.salt = salt;
  }

  public String getFarm()
  {
    return farm;
  }

  public void setFarm( String farm )
  {
    this.farm = farm;
  }

}