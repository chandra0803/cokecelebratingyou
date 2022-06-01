
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SSIContestSuperViewerResponseView
{
  private List<WebErrorMessage> messages;

  public SSIContestSuperViewerResponseView()
  {

  }

  public SSIContestSuperViewerResponseView( WebErrorMessage message )
  {
    this.messages = new ArrayList<WebErrorMessage>();
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
