
package com.biperf.core.indexing;

import java.util.List;

import com.biperf.core.value.indexing.Searchable;

public interface BIElasticSearchIndexerService
{

  public boolean index( List<? extends Searchable> data, BIIndexType indexType ) throws Exception;

  boolean deleteByIds( BIIndexType indexType, List<Long> paxIds ) throws Exception;

}
