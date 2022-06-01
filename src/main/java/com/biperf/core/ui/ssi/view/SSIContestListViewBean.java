
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.ssi.SSIContestListValueBean;

public class SSIContestListViewBean
{
  private List<SSIContestListValueBean> activeContestsList;
  private List<WebErrorMessage> messages;

  public SSIContestListViewBean()
  {

  }

  public SSIContestListViewBean( List<SSIContestListValueBean> contestList )
  {
    this.activeContestsList = contestList;
  }

  public List<SSIContestListValueBean> getActiveContestsList()
  {
    return activeContestsList;
  }

  public void setActiveContestsList( List<SSIContestListValueBean> activeContestsList )
  {
    this.activeContestsList = activeContestsList;
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
