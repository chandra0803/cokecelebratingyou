
package com.biperf.core.ui.productclaim;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class ProductPreviewRedirectView
{
  private List<Message> messages = new ArrayList<Message>();

  @JsonProperty( "messages" )
  public List<Message> getMessages()
  {
    return messages;
  }

  public void setMessages( List<Message> messages )
  {
    this.messages = messages;
  }

  public static class Message
  {
    @JsonProperty( "type" )
    String type;
    @JsonProperty( "command" )
    String command;
    @JsonProperty( "url" )
    String redirectUrl;

    public String getRedirectUrl()
    {
      return redirectUrl;
    }

    public void setRedirectUrl( String redirectUrl )
    {
      this.redirectUrl = redirectUrl;
    }

    public String getType()
    {
      return type;
    }

    public void setType( String type )
    {
      this.type = type;
    }

    public String getCommand()
    {
      return command;
    }

    public void setCommand( String command )
    {
      this.command = command;
    }
  }

}
