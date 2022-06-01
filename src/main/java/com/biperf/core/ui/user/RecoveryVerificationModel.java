package com.biperf.core.ui.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class RecoveryVerificationModel implements Serializable
{
  
  private String contactType;
  private String token;
  private String emailOrPhone;

  public String getContactType()
  {
    return contactType;
  }

  public void setContactType( String contactType )
  {
    this.contactType = contactType;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken( String token )
  {
    this.token = token;
  }

  public String getEmailOrPhone()
  {
    return emailOrPhone;
  }

  public void setEmailOrPhone( String emailOrPhone )
  {
    this.emailOrPhone = emailOrPhone;
  }

}
