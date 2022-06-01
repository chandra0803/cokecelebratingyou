
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

public class NominationsInProgressListMetaValueObject
{

  private List<NominationsInProgressListColumnValueObject> columns = new ArrayList<NominationsInProgressListColumnValueObject>();

  public List<NominationsInProgressListColumnValueObject> getColumns()
  {
    return columns;
  }

  public void setColumns( List<NominationsInProgressListColumnValueObject> columns )
  {
    this.columns = columns;
  }

}
