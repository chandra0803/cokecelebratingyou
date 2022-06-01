
package com.biperf.core.ui.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author dudam
 * @since Dec 20, 2012
 * @version 1.0
 */
public class ProfileAlertsView
{

  private List<ProfileAlertView> messages = new ArrayList<ProfileAlertView>();

  public List<ProfileAlertView> getMessages()
  {
    return messages;
  }

  public void setMessages( List<ProfileAlertView> messages )
  {
    this.messages = messages;
  }

}
