
package com.biperf.core.indexing;

import com.biperf.core.value.indexing.BIElasticSearchCriteria;
import com.biperf.core.value.indexing.ESResultWrapper;

public interface BIElasticSearchSearcher
{
  <T extends BIElasticSearchCriteria> ESResultWrapper search( T biSearchCriteria, BIIndexType indexType ) throws Exception;

}
