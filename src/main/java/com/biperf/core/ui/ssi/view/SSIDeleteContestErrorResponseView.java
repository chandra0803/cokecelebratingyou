
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SSIDeleteContestErrorResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public SSIDeleteContestErrorResponseView()
  {

  }

  public SSIDeleteContestErrorResponseView( WebErrorMessage message )
  {
    this.messages.add( message );
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}
