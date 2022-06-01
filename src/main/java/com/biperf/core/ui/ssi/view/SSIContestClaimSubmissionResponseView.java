
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * SSIContestClaimSubmissionResponseView.
 * 
 * @author dudam
 * @since May 27, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestClaimSubmissionResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private String forwardUrl;

  public SSIContestClaimSubmissionResponseView()
  {

  }

  public SSIContestClaimSubmissionResponseView( String forwardUrl )
  {
    this.forwardUrl = forwardUrl;
  }

  public SSIContestClaimSubmissionResponseView( WebErrorMessage message )
  {
    this.messages.add( message );
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
