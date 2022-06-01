
package com.biperf.core;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseJsonView
{

  @JsonProperty( "messages" )
  public List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}
