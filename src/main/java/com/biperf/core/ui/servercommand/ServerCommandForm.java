
package com.biperf.core.ui.servercommand;

import com.biperf.core.ui.BaseForm;

public class ServerCommandForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private String targetUrl;

  public void setTargetUrl( String targetUrl )
  {
    this.targetUrl = targetUrl;
  }

  public String getTargetUrl()
  {
    return targetUrl;
  }

}
