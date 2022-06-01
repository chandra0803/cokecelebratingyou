
package com.biperf.core.domain.user;

import com.biperf.core.domain.BaseDomain;

public class StrongMailUser extends BaseDomain
{
  private String firstName;
  private String lastName;
  private String userName;
  private String password;
  private String emailAddr;
  private String emailStatus;
  private String company;
  private String websiteUrl;
  private String contactUrl;
  private String languageId;
  private String userTokenUrl;

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

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public String getEmailStatus()
  {
    return emailStatus;
  }

  public void setEmailStatus( String emailStatus )
  {
    this.emailStatus = emailStatus;
  }

  public String getCompany()
  {
    return company;
  }

  public void setCompany( String company )
  {
    this.company = company;
  }

  public String getWebsiteUrl()
  {
    return websiteUrl;
  }

  public void setWebsiteUrl( String websiteUrl )
  {
    this.websiteUrl = websiteUrl;
  }

  public String getContactUrl()
  {
    return contactUrl;
  }

  public void setContactUrl( String contactUrl )
  {
    this.contactUrl = contactUrl;
  }

  public String getLanguageId()
  {
    return languageId;
  }

  public void setLanguageId( String languageId )
  {
    this.languageId = languageId;
  }

  public String getUserTokenUrl()
  {
    return userTokenUrl;
  }

  public void setUserTokenUrl( String userTokenUrl )
  {
    this.userTokenUrl = userTokenUrl;
  }

  @Override
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof StrongMailUser ) )
    {
      return false;
    }

    final StrongMailUser user = (StrongMailUser)o;

    if ( getUserName() != null ? !getUserName().equals( user.getUserName() ) : user.getUserName() != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return getUserName() != null ? getUserName().hashCode() : 0;
  }

}
