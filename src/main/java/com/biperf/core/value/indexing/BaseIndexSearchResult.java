
package com.biperf.core.value.indexing;

import java.util.List;

/**
 *   Abstract Index Search result data holder
 */
public abstract class BaseIndexSearchResult<T extends Searchable>
{
  public long totalExecutionTime;
  public long hitCount;

  public abstract List<T> getSearchResults();

  public BaseIndexSearchResult()
  {
  }

  public long getTotalExecutionTime()
  {
    return totalExecutionTime;
  }

  public void setTotalExecutionTime( long totalExecutionTime )
  {
    this.totalExecutionTime = totalExecutionTime;
  }

  public long getHitCount()
  {
    return hitCount;
  }

  public void setHitCount( long hitCount )
  {
    this.hitCount = hitCount;
  }
}
