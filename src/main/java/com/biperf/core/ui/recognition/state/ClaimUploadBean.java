
package com.biperf.core.ui.recognition.state;

import java.io.Serializable;

/**
 * ClaimUploadBean.java
 * This class is created as part of WIP #39189
 * @author dudam
 * @since Nov 14, 2017
 * @version 1.0
 */
@SuppressWarnings( "serial" )
public class ClaimUploadBean implements Serializable
{
  private String url;
  private String description;

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

}
