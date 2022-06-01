/**
 * 
 */

package com.biperf.core.domain.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class ForumRedirectToEditView
{
  private Message message = new Message();

  @JsonProperty( "message" )
  public Message getMessage()
  {
    return message;
  }

  public void setMessage( Message message )
  {
    this.message = message;
  }

  public static class Message
  {
    @JsonProperty( "redirectUrl" )
    String redirectUrl;

    public String getRedirectUrl()
    {
      return redirectUrl;
    }

    public void setRedirectUrl( String redirectUrl )
    {
      this.redirectUrl = redirectUrl;
    }
  }

}
