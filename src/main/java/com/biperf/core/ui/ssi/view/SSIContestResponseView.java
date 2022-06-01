
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * SSIContestResponseView.
 * 
 * @author kandhi
 * @since Feb 20, 2015
 * @version 1.0
 */
public class SSIContestResponseView
{
  private List<WebErrorMessage> messages;

  public SSIContestResponseView()
  {

  }

  public SSIContestResponseView( WebErrorMessage message )
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
