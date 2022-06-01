
package com.biperf.core.ui.engagement;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * 
 * EngagementRecognizedView.
 * 
 * @author kandhi
 * @since Jun 23, 2014
 * @version 1.0
 */
public class EngagementRecognizedView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private EngagementRecognizedTreeView tree;

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public EngagementRecognizedTreeView getTree()
  {
    return tree;
  }

  public void setTree( EngagementRecognizedTreeView tree )
  {
    this.tree = tree;
  }

}
