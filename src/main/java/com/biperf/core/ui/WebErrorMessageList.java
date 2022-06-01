
package com.biperf.core.ui;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class WebErrorMessageList
{

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public List<WebErrorMessage> getMessages()
  {

    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}
