
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SSICopyContestErrorResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public SSICopyContestErrorResponseView()
  {

  }

  public SSICopyContestErrorResponseView( WebErrorMessage message )
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
