
package com.biperf.core.ui.user;

import java.io.Serializable;

import com.biperf.core.exception.ExceptionView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class ParticipantActivationLinkDestination extends ExceptionView implements Serializable
{
  private Long contactId;
  private String contactType;

  public Long getContactId()
  {
    return contactId;
  }

  public void setContactId( Long contactId )
  {
    this.contactId = contactId;
  }

  public String getContactType()
  {
    return contactType;
  }

  public void setContactType( String contactType )
  {
    this.contactType = contactType;
  }

}
