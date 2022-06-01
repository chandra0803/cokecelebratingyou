/*
 * (c) 2018 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * 
 * @since Mar 8, 2018
 * 
 */

@JsonInclude( value = Include.NON_EMPTY )
public class RAEnableBean implements Serializable
{

  private static final long serialVersionUID = -3467046330361172113L;

  @JsonProperty( "raEnabled" )
  private boolean raEnabled;

  public boolean isRaEnabled()
  {
    return raEnabled;
  }

  public void setRaEnabled( boolean raEnabled )
  {
    this.raEnabled = raEnabled;
  }

}
