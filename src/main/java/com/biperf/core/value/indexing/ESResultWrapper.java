
package com.biperf.core.value.indexing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.biperf.core.indexing.BIIndexType;

import io.searchbox.core.SearchResult;

public class ESResultWrapper
{
  private SearchResult jestResult;
  private BIIndexType indexType;

  public ESResultWrapper( SearchResult thatJestResult, BIIndexType thatIndexType )
  {
    this.jestResult = thatJestResult;
    this.indexType = thatIndexType;
  }

  public List<Searchable> getSearchRecords()
  {
    List<Searchable> records = new ArrayList<Searchable>();

    jestResult.getHits( indexType.getClassType() ).stream().filter( h -> Objects.nonNull( h ) ).forEach( h -> records.add( h.source ) );

    return records;
  }

  public long getHits()
  {
    return jestResult.getTotal();
  }

  public SearchResult getJestResult()
  {
    return jestResult;
  }

  public BIIndexType getIndexType()
  {
    return indexType;
  }

  public long getExecutionTime()
  {
    if ( null != jestResult && null != jestResult.getValue( "took" ) )
    {
      return ( (Double)jestResult.getValue( "took" ) ).longValue();
    }
    return -0L;
  }
}
