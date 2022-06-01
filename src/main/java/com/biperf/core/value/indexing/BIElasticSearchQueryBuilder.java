
package com.biperf.core.value.indexing;

import java.util.Collection;

import com.biperf.core.domain.enums.SortByType;

import io.searchbox.core.search.sort.Sort;
import io.searchbox.core.search.sort.Sort.Sorting;

public interface BIElasticSearchQueryBuilder
{
  default Sort.Sorting getSorting( SortByType sortBy )
  {
    if ( "ASC".equalsIgnoreCase( sortBy.getSortBy() ) )
    {
      return Sorting.ASC;
    }
    else if ( "DESC".equalsIgnoreCase( sortBy.getSortBy() ) )
    {
      return Sorting.DESC;
    }

    return Sorting.ASC;
  }

  public <T extends BIElasticSearchCriteria> String buildJsonQuery( T criteria );

  public <T extends BIElasticSearchCriteria> Collection<Sort> buildSort( T criteria );

  public <T extends Searchable> String buildDeleteQuery( T indexData );

}
