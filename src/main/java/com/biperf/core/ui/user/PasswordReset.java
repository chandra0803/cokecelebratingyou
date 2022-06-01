
package com.biperf.core.ui.user;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class PasswordReset implements Serializable
{
  @JsonProperty( "email" )
  private String email;

  @JsonProperty( "contactType" )
  private String contactType;

  @JsonProperty( "contactId" )
  private Long contactId;

  @JsonProperty( "paxIDs" )
  private List<Long> paxIDs;

  @JsonProperty( "userName" )
  private String userName;

  @JsonProperty( "token" )
  private String token;

  @JsonProperty( "password" )
  private String password;

  @JsonProperty( "emailOrPhone" )
  private String emailOrPhone;

  @JsonProperty( "initialQuery" )
  private String initialQuery;

  @JsonProperty( "sendMessage" )
  private boolean sendMessage;

  @JsonProperty( "fromEmail" )
  private boolean fromEmail;

  @JsonProperty( "activation" )
  private boolean activation;

  public String getEmail()
  {
    return email;
  }

  public void setEmail( String email )
  {
    this.email = email;
  }

  public String getContactType()
  {
    return contactType;
  }

  public void setContactType( String contactType )
  {
    this.contactType = contactType;
  }

  public Long getContactId()
  {
    return contactId;
  }

  public void setContactId( Long contactId )
  {
    this.contactId = contactId;
  }

  public List<Long> getPaxIDs()
  {
    return paxIDs;
  }

  public void setPaxIDs( List<Long> paxIDs )
  {
    this.paxIDs = paxIDs;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken( String token )
  {
    this.token = token;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public String getEmailOrPhone()
  {
    return emailOrPhone;
  }

  public void setEmailOrPhone( String emailOrPhone )
  {
    this.emailOrPhone = emailOrPhone;
  }

  public String getInitialQuery()
  {
    return initialQuery;
  }

  public void setInitialQuery( String initialQuery )
  {
    this.initialQuery = initialQuery;
  }

  public boolean isSendMessage()
  {
    return sendMessage;
  }

  public void setSendMessage( boolean sendMessage )
  {
    this.sendMessage = sendMessage;
  }

  public boolean isFromEmail()
  {
    return fromEmail;
  }

  public void setFromEmail( boolean fromEmail )
  {
    this.fromEmail = fromEmail;
  }

  public boolean isActivation()
  {
    return activation;
  }

  public void setActivation( boolean activation )
  {
    this.activation = activation;
  }

}
