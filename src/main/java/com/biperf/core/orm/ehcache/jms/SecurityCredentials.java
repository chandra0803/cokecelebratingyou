
package com.biperf.core.orm.ehcache.jms;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class SecurityCredentials implements Serializable
{
  String securityPrincipalName = null;
  String securityCredentialsPassword = null;
  String userName = null;
  String password = null;

  public String getSecurityPrincipalName()
  {
    return securityPrincipalName;
  }

  public void setSecurityPrincipalName( String securityPrincipalName )
  {
    this.securityPrincipalName = securityPrincipalName;
  }

  public String getSecurityCredentialsPassword()
  {
    return securityCredentialsPassword;
  }

  public void setSecurityCredentialsPassword( String securityCredentialsPassword )
  {
    this.securityCredentialsPassword = securityCredentialsPassword;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
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
