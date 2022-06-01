
package com.biperf.core.service.email;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class EmailCredentials implements Serializable
{
  private String username;
  private String password;

  public EmailCredentials( String username, String password )
  {
    this.username = username;
    this.password = password;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername( String username )
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }
}
