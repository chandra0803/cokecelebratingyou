
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationsInProgressListTabularDataViewObject
{
  private NominationsInProgressListMetaViewObject meta = new NominationsInProgressListMetaViewObject();
  private List<NominationsInProgressViewObject> results = new ArrayList<NominationsInProgressViewObject>();

  @JsonProperty( "meta" )
  public NominationsInProgressListMetaViewObject getMeta()
  {
    return meta;
  }

  public void setMeta( NominationsInProgressListMetaViewObject meta )
  {
    this.meta = meta;
  }

  @JsonProperty( "results" )
  public List<NominationsInProgressViewObject> getResults()
  {
    return results;
  }

  public void setResults( List<NominationsInProgressViewObject> results )
  {
    this.results = results;
  }
}
