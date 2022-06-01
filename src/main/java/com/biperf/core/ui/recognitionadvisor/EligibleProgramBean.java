
package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_EMPTY )
public class EligibleProgramBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "programName" )
  private String programName;

  @JsonProperty( "programId" )
  private Long programId;

  public String getProgramName()
  {
    return programName;
  }

  public void setProgramName( String programName )
  {
    this.programName = programName;
  }

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId( Long programId )
  {
    this.programId = programId;
  }

}
