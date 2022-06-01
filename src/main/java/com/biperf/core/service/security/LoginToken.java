
package com.biperf.core.service.security;

public class LoginToken
{
  private final String username;
  private final String token;

  public LoginToken( String username, String token )
  {
    this.username = username;
    this.token = token;
  }

  public String getUsername()
  {
    return username;
  }

  public String getToken()
  {
    return token;
  }
}
