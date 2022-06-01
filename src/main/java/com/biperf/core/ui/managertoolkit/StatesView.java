
package com.biperf.core.ui.managertoolkit;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.participant.NameIdBean;

public class StatesView
{

  private String[] messages = {};
  private List<NameIdBean> locations = new ArrayList<NameIdBean>();

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<NameIdBean> getLocations()
  {
    return locations;
  }

  public void setLocations( List<NameIdBean> locations )
  {
    this.locations = locations;
  }

}
