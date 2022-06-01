
package com.biperf.core.ui.search;

import java.util.List;

import com.biperf.core.domain.participant.NameIdBean;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterView
{

  @JsonProperty( "filters" )
  private List<NameIdBean> filters;

  public FilterView( List<NameIdBean> filters )
  {
    this.filters = filters;
  }

}
