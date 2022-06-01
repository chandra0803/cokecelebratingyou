
package com.biperf.core.ui.ssi.view;

/**
 * 
 * SSIContestAwardThemNowResponseView.
 * 
 * @author kandhi
 * @since Feb 12, 2015
 * @version 1.0
 */
public class SSIContestAwardThemNowResponseView
{
  private String forwardUrl;

  public SSIContestAwardThemNowResponseView()
  {

  }

  public SSIContestAwardThemNowResponseView( String forwardUrl )
  {
    this.setForwardUrl( forwardUrl );
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
