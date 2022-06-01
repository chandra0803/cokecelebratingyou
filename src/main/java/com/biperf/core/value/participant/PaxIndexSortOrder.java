
package com.biperf.core.value.participant;

import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.value.indexing.PaxIndexMappingEnum;

/**
 *  PAX indexed data  sort order
 */
public class PaxIndexSortOrder
{
  private SortByType sortByType;
  private PaxIndexMappingEnum indexedField;

  public PaxIndexSortOrder( SortByType sortByType, PaxIndexMappingEnum indexedField )
  {
    this.sortByType = sortByType;
    this.indexedField = indexedField;
  }

  public SortByType getSortByType()
  {
    return sortByType;
  }

  public PaxIndexMappingEnum getIndexedField()
  {
    return indexedField;
  }

  @Override
  public String toString()
  {
    return "PaxIndexSortOrder [sortByType=" + sortByType + ", indexedField=" + indexedField + "]";
  }
}
