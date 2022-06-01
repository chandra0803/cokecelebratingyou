
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * 
 * SSIContestManagerResponseView.
 * 
 * @author kandhi
 * @since Nov 21, 2014
 * @version 1.0
 */
public class SSIContestManagerResponseView
{
  private List<WebErrorMessage> messages;

  public SSIContestManagerResponseView()
  {

  }

  public SSIContestManagerResponseView( WebErrorMessage message )
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
