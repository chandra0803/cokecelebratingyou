
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SSIDeleteContestSuccessResponseView
{
  private List<SSIDeleteContestSuccessMessage> messages = new ArrayList<SSIDeleteContestSuccessMessage>();

  public SSIDeleteContestSuccessResponseView()
  {
    messages.add( new SSIDeleteContestSuccessMessage() );
  }

  public List<SSIDeleteContestSuccessMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIDeleteContestSuccessMessage> messages )
  {
    this.messages = messages;
  }

  @JsonInclude( value = Include.NON_EMPTY )
  private class SSIDeleteContestSuccessMessage
  {

    private String type = "success";
    private Boolean success = Boolean.TRUE;

    public SSIDeleteContestSuccessMessage()
    {
    }

    public String getType()
    {
      return type;
    }

    public void setType( String type )
    {
      this.type = type;
    }

    @JsonProperty( "isSuccess" )
    public Boolean getSuccess()
    {
      return success;
    }

    public void setSuccess( Boolean success )
    {
      this.success = success;
    }
  }

}
