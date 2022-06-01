
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * 
 * SSIContestApprovalATNView.
 * 
 * @author aptel
 * @since Feb 26, 2015
 * @version 1.0
 */

public class SSIContestApprovalATNView
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