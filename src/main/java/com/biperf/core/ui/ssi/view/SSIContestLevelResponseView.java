
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.ssi.SSIContestLevel;

/**
 * 
 * SSIContestLevelResponseView.
 * 
 * @author Prabhu Patel
 * @since Jan 16, 2015
 * @version 1.0
 */
public class SSIContestLevelResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIContestLevelView level;

  public SSIContestLevelResponseView( SSIContestLevel ssiContestLevel )
  {
    this.level = new SSIContestLevelView();
    this.level.setId( ssiContestLevel.getId() );
  }

  public SSIContestLevelView getLevel()
  {
    return level;
  }

  public void setLevel( SSIContestLevelView level )
  {
    this.level = level;
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
