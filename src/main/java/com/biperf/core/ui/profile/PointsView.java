
package com.biperf.core.ui.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PointsView implements Serializable
{

  private List<ProfilePointsView> messages = new ArrayList<ProfilePointsView>();

  public List<ProfilePointsView> getMessages()
  {
    return messages;
  }

  public void setMessages( List<ProfilePointsView> messages )
  {
    this.messages = messages;
  }

}
