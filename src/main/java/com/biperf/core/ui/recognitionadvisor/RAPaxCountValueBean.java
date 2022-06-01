
package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_EMPTY )
public class RAPaxCountValueBean implements Serializable
{
  @JsonProperty( "raNewHireTotalcount" )
  private Integer newHireTotalcount;
  @JsonProperty( "raOverDueTotalcount" )
  private Integer overDueTotalcount;

  public Integer getNewHireTotalcount()
  {
    return newHireTotalcount;
  }

  public void setNewHireTotalcount( Integer newHireTotalcount )
  {
    this.newHireTotalcount = newHireTotalcount;
  }

  public Integer getOverDueTotalcount()
  {
    return overDueTotalcount;
  }

  public void setOverDueTotalcount( Integer overDueTotalcount )
  {
    this.overDueTotalcount = overDueTotalcount;
  }
}
