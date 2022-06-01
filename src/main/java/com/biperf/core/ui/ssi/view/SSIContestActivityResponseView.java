
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.ssi.SSIContestActivity;

/**
 * 
 * SSIContestActivityResponseView.
 * 
 * @author kandhi
 * @since Dec 31, 2014
 * @version 1.0
 */
public class SSIContestActivityResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIContestActivityView activity;

  public SSIContestActivityResponseView( SSIContestActivity activity )
  {
    this.activity = new SSIContestActivityView();
    this.activity.setId( activity.getId() );
  }

  public SSIContestActivityView getActivity()
  {
    return activity;
  }

  public void setActivity( SSIContestActivityView activity )
  {
    this.activity = activity;
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
