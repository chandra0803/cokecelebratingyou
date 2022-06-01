
package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * 
 * DIYQuizFileUploadView.
 * 
 * @author kandhi
 * @since Jul 18, 2013
 * @version 1.0
 */
public class DIYQuizFileUploadView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private DIYQuizFileUploadPropertiesView properties;

  public DIYQuizFileUploadPropertiesView getProperties()
  {
    return properties;
  }

  public void setProperties( DIYQuizFileUploadPropertiesView properties )
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
