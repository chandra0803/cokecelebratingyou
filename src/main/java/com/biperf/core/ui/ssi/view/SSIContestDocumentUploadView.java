
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * 
 * SSIContestDocumentUploadValueBean.
 * 
 * @author chowdhur
 * @since Nov 12, 2014
 */
public class SSIContestDocumentUploadView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIContestFileUploadPropertiesView properties;

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public SSIContestFileUploadPropertiesView getProperties()
  {
    return properties;
  }

  public void setProperties( SSIContestFileUploadPropertiesView properties )
  {
    this.properties = properties;
  }

}
