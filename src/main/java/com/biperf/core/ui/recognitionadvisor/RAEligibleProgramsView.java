
package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_EMPTY )
public class RAEligibleProgramsView implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "raEnabled" )
  private boolean raEnabled;

  @JsonProperty( "eligiblePrograms" )
  List<EligibleProgramBean> eligiblePrograms = new ArrayList<EligibleProgramBean>();

  public boolean isRaEnabled()
  {
    return raEnabled;
  }

  public List<EligibleProgramBean> getEligiblePrograms()
  {
    return eligiblePrograms;
  }

  public void setEligiblePrograms( List<EligibleProgramBean> eligiblePrograms )
  {
    this.eligiblePrograms = eligiblePrograms;
  }

  public void setRaEnabled( boolean raEnabled )
  {
    this.raEnabled = raEnabled;
  }

}
