
package com.biperf.core.value;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StandingsUrlBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String standingsUrl = null;

  @JsonProperty( "standingsUrl" )
  public String getStandingsUrl()
  {
    return standingsUrl;
  }

  public void setStandingsUrl( String standingsUrl )
  {
    this.standingsUrl = standingsUrl;
  }
}
