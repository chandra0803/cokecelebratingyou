
package com.biperf.core.ui.api.ping;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Version
{
  @JsonProperty
  private String version;

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

}
