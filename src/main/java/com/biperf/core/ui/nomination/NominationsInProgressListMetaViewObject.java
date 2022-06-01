
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationsInProgressListMetaViewObject
{

  private List<NominationsInProgressListColumnViewObject> columns = new ArrayList<NominationsInProgressListColumnViewObject>();

  @JsonProperty( "columns" )
  public List<NominationsInProgressListColumnViewObject> getColumns()
  {
    return columns;
  }

  public void setColumns( List<NominationsInProgressListColumnViewObject> columns )
  {
    this.columns = columns;
  }

}
