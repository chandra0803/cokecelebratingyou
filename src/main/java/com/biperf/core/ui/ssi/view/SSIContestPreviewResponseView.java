
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.utils.WebResponseConstants;

public class SSIContestPreviewResponseView
{
  private List<WebErrorMessage> messages;
  private String forwardUrl = "";

  public SSIContestPreviewResponseView()
  {

  }

  public SSIContestPreviewResponseView( String text, String name )
  {
    messages = new ArrayList<WebErrorMessage>();
    messages.add( new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD, false, text, WebResponseConstants.RESPONSE_COMMAND_MODAL, name ) );
  }

  public SSIContestPreviewResponseView( String sysUrl )
  {
    this.setForwardUrl( sysUrl + "/ssi/creatorContestList.do?method=display" );
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public String getForwardUrl()
  {
    return forwardUrl;
  }

  public void setForwardUrl( String forwardUrl )
  {
    this.forwardUrl = forwardUrl;
  }
}
