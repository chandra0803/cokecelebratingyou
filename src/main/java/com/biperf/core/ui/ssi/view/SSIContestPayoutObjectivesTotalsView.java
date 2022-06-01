
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;

/**
 * 
 * SSIContestPayoutObjectivesTotalsView.
 * 
 * @author kandhi
 * @since Dec 5, 2014
 * @version 1.0
 */
public class SSIContestPayoutObjectivesTotalsView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIContestPayoutObjectivesTotalsValueBean contestJson;

  public SSIContestPayoutObjectivesTotalsView( SSIContestPayoutObjectivesTotalsValueBean contestJson )
  {
    super();
    this.contestJson = contestJson;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public SSIContestPayoutObjectivesTotalsValueBean getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestPayoutObjectivesTotalsValueBean contestJson )
  {
    this.contestJson = contestJson;
  }

}
