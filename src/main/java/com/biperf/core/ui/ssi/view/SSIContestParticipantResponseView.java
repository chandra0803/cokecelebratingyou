
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * @author dudam
 * @since Nov 21, 2014
 * @version 1.0
 */
public class SSIContestParticipantResponseView
{
  private List<WebErrorMessage> messages;

  public SSIContestParticipantResponseView()
  {

  }

  public SSIContestParticipantResponseView( List<WebErrorMessage> messages )
  {
    this.messages = messages;
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
