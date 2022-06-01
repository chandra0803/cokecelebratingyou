
package com.biperf.core.ui.user;

import java.io.Serializable;

import com.biperf.core.exception.ExceptionView;

@SuppressWarnings( "serial" )
public class UserContactActivationView extends ExceptionView implements Serializable
{
  private String tokenValidation = null;
  private String contactEmail = null;
  private String contactPhone = null;
  private Long countryId = null;
  private String userName = null;
  private boolean finalLogin = false;

  public String getContactEmail()
  {
    return contactEmail;
  }

  public void setContactEmail( String contactEmail )
  {
    this.contactEmail = contactEmail;
  }

  public String getContactPhone()
  {
    return contactPhone;
  }

  public void setContactPhone( String contactPhone )
  {
    this.contactPhone = contactPhone;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }
  
  public String getTokenValidation()
  {
    return tokenValidation;
  }

  public void setTokenValidation( String tokenValidation )
  {
    this.tokenValidation = tokenValidation;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( contactEmail == null ) ? 0 : contactEmail.hashCode() );
    result = prime * result + ( ( contactPhone == null ) ? 0 : contactPhone.hashCode() );
    result = prime * result + ( ( countryId == null ) ? 0 : countryId.hashCode() );
    result = prime * result + ( ( userName == null ) ? 0 : userName.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    UserContactActivationView other = (UserContactActivationView)obj;
    if ( contactEmail == null )
    {
      if ( other.contactEmail != null )
      {
        return false;
      }
    }
    else if ( !contactEmail.equals( other.contactEmail ) )
    {
      return false;
    }
    if ( contactPhone == null )
    {
      if ( other.contactPhone != null )
      {
        return false;
      }
    }
    else if ( !contactPhone.equals( other.contactPhone ) )
    {
      return false;
    }
    if ( countryId == null )
    {
      if ( other.countryId != null )
      {
        return false;
      }
    }
    else if ( !countryId.equals( other.countryId ) )
    {
      return false;
    }
    if ( userName == null )
    {
      if ( other.userName != null )
      {
        return false;
      }
    }
    else if ( !userName.equals( other.userName ) )
    {
      return false;
    }
    return true;
  }

  public boolean isFinalLogin()
  {
    return finalLogin;
  }

  public void setFinalLogin( boolean finalLogin )
  {
    this.finalLogin = finalLogin;
  }

  @Override
  public String toString()
  {
    return "UserContactActivationView [contactEmail=" + contactEmail + ", contactPhone=" + contactPhone + ", countryId=" + countryId + ", userName=" + userName + "]";
  }

}
