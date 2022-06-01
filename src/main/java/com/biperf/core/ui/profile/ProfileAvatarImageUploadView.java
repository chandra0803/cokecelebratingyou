
package com.biperf.core.ui.profile;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class ProfileAvatarImageUploadView
{

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private ProfileAvatarImageUploadPropertiesView properties;

  public ProfileAvatarImageUploadPropertiesView getProperties()
  {
    return properties;
  }

  public void setProperties( ProfileAvatarImageUploadPropertiesView properties )
  {
    this.properties = properties;
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
