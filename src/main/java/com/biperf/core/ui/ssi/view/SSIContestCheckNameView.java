
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * 
 * SSIContestCheckNameView.
 * 
 * @author chowdhur
 * @since Nov 7, 2014
 */
public class SSIContestCheckNameView
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
