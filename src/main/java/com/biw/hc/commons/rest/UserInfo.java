
package com.biw.hc.commons.rest;

import java.io.Serializable;

/**
 * Taken from hc-commons project.
 * Currently infeasible to take the hc-commons dependency, as it has a _lot_ of stuff we do _not_ want
 */
public class UserInfo implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Long userId;
  private String firstName;
  private String lastName;
  private String middleName;
  private String suffix;
  private String userName;
  private String loginToken;
  private String clientCode;
  private Boolean firstTimeSetupDone;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean credentialsNonExpired;
  private Boolean accountNonLocked;
  private Long clientId;
  private String locale;
  private String emailAddress;
  private UserInfo adminUser;
  private String countryName;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix( String suffix )
  {
    this.suffix = suffix;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getLoginToken()
  {
    return loginToken;
  }

  public void setLoginToken( String loginToken )
  {
    this.loginToken = loginToken;
  }

  public String getClientCode()
  {
    return clientCode;
  }

  public void setClientCode( String clientCode )
  {
    this.clientCode = clientCode;
  }

  public Boolean getFirstTimeSetupDone()
  {
    return firstTimeSetupDone;
  }

  public void setFirstTimeSetupDone( Boolean firstTimeSetupDone )
  {
    this.firstTimeSetupDone = firstTimeSetupDone;
  }

  public Boolean getEnabled()
  {
    return enabled;
  }

  public void setEnabled( Boolean enabled )
  {
    this.enabled = enabled;
  }

  public Boolean getAccountNonExpired()
  {
    return accountNonExpired;
  }

  public void setAccountNonExpired( Boolean accountNonExpired )
  {
    this.accountNonExpired = accountNonExpired;
  }

  public Boolean getCredentialsNonExpired()
  {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired( Boolean credentialsNonExpired )
  {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public Boolean getAccountNonLocked()
  {
    return accountNonLocked;
  }

  public void setAccountNonLocked( Boolean accountNonLocked )
  {
    this.accountNonLocked = accountNonLocked;
  }

  public Long getClientId()
  {
    return clientId;
  }

  public void setClientId( Long clientId )
  {
    this.clientId = clientId;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public UserInfo getAdminUser()
  {
    return adminUser;
  }

  public void setAdminUser( UserInfo adminUser )
  {
    this.adminUser = adminUser;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

}
