
package com.biperf.core.value.indexing;

import java.util.List;

public class IndexSearchResult<T extends Searchable> extends BaseIndexSearchResult<T>
{
  private List<Searchable> data;

  public IndexSearchResult( List<Searchable> searchable, long hits, long executionTime )
  {
    this.data = searchable;
    this.hitCount = hits;
    this.totalExecutionTime = executionTime;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<T> getSearchResults()
  {
    return (List<T>)data;
  }

}
