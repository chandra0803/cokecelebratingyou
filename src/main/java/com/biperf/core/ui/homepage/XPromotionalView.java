
package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.List;

public class XPromotionalView
{
  private String[] messages = {};
  private List<XPromotionalObject> xpromos = new ArrayList<XPromotionalObject>();

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<XPromotionalObject> getXpromos()
  {
    return xpromos;
  }

  public void setXpromos( List<XPromotionalObject> xpromos )
  {
    this.xpromos = xpromos;
  }
}
