
package com.biperf.core.ui.homepage;

import java.util.List;

import com.biperf.core.value.DailyTipValueBean;

public class DailyTipView
{

  private String[] messages = {};
  private List<DailyTipValueBean> tipCollections;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<DailyTipValueBean> getTipCollections()
  {
    return tipCollections;
  }

  public void setTipCollections( List<DailyTipValueBean> tipCollections )
  {
    this.tipCollections = tipCollections;
  }

}
