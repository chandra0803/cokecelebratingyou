
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.value.ssi.SSIContestMessageValueBean;

/**
 * 
 * SSIContestMessagesView.
 * 
 * @author chowdhur
 * @since Nov 18, 2014
 */
public class SSIContestMessagesView
{
  private List<SSIContestMessageValueBean> messages;

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

}
