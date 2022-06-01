
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

public class MyWinnersTabularDataMetaViewBean
{
  List<MyWinnersTabularDataColumnsViewBean> columns = new ArrayList<MyWinnersTabularDataColumnsViewBean>();

  public List<MyWinnersTabularDataColumnsViewBean> getColumns()
  {
    return columns;
  }

  public void setColumns( List<MyWinnersTabularDataColumnsViewBean> columns )
  {
    this.columns = columns;
  }
}
