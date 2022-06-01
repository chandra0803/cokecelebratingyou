
package com.biperf.core.indexing.impl;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.indexing.BIElasticSearchSearcher;
import com.biperf.core.indexing.BIIndex;
import com.biperf.core.indexing.BIIndexType;
import com.biperf.core.value.indexing.BIElasticSearchCriteria;
import com.biperf.core.value.indexing.BIElasticSearchQueryBuilder;
import com.biperf.core.value.indexing.ESResultWrapper;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Service
public class BIElasticSearchSearcherImpl implements BIElasticSearchSearcher
{
  private static final Log log = LogFactory.getLog( BIElasticSearchSearcherImpl.class );

  private @Autowired BIIndex biIndex;
  private @Autowired ESClientFactory factory;

  @Override
  public <T extends BIElasticSearchCriteria> ESResultWrapper search( T biSearchCriteria, BIIndexType indexType ) throws Exception
  {
    BIElasticSearchQueryBuilder queryBuilder = indexType.getQueryBuilder();
    String queryJson = queryBuilder.buildJsonQuery( biSearchCriteria );

    if ( log.isTraceEnabled() )
    {
      log.trace( "Searching index '" + biIndex.getName() + "': " + biSearchCriteria );
      log.trace( "JSON Query: " + queryJson );
    }

    Search search = new Search.Builder( queryJson ).addIndex( biIndex.getName() ).addType( indexType.getType() ).build();
    SearchResult jestResult = factory.getInstance().execute( search );

    if ( isFalse( jestResult.isSucceeded() ) )
    {
      throw new BeaconRuntimeException( jestResult.getErrorMessage() );
    }

    return new ESResultWrapper( jestResult, indexType );
  }

}
