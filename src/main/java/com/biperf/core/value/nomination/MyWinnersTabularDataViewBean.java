
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

public class MyWinnersTabularDataViewBean
{
  private MyWinnersTabularDataMetaViewBean meta = new MyWinnersTabularDataMetaViewBean();
  private List<MyWinnersTabularDataResultsViewBean> results = new ArrayList<MyWinnersTabularDataResultsViewBean>();

  public List<MyWinnersTabularDataResultsViewBean> getResults()
  {
    return results;
  }

  public void setResults( List<MyWinnersTabularDataResultsViewBean> results )
  {
    this.results = results;
  }

  public MyWinnersTabularDataMetaViewBean getMeta()
  {
    return meta;
  }

  public void setMeta( MyWinnersTabularDataMetaViewBean meta )
  {
    this.meta = meta;
  }
}
