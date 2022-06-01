
package com.biperf.core.indexing.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.indexing.BIElasticSearchAdminService;
import com.biperf.core.indexing.BIElasticSearchIndexerService;
import com.biperf.core.indexing.BIIndex;
import com.biperf.core.indexing.BIIndexType;
import com.biperf.core.indexing.EsIndexStatus;
import com.biperf.core.indexing.IndexExistance;
import com.biperf.core.service.jms.GJavaMessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.jms.ResetElasticSearchClientFactoryMessage;
import com.biperf.core.value.indexing.ESIndexMappingBuilder;
import com.biperf.core.value.indexing.Searchable;
import com.biperf.core.value.participant.PaxIndexData;
import com.google.gson.JsonObject;
import com.objectpartners.cms.util.BeanLocator;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.indices.CloseIndex;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.OpenIndex;
import io.searchbox.indices.Stats;
import io.searchbox.indices.mapping.PutMapping;

@Service( "biElasticSearchIndexerService" )
public class BIElasticSearchIndexerServiceImpl implements BIElasticSearchIndexerService, BIElasticSearchAdminService
{
  private static final Log logger = LogFactory.getLog( BIElasticSearchIndexerServiceImpl.class );

  private @Autowired BIIndex biIndex;
  private @Autowired ESClientFactory factory;
  private @Autowired SystemVariableService systemVariableService;

  @Override
  public boolean index( List<? extends Searchable> data, BIIndexType indexType ) throws Exception
  {
    if ( !indexExists() )
    {
      createIndex();
    }

    ESIndexMappingBuilder mappingBuilder = indexType.getIndexBuilder();
    PutMapping.Builder putMapping = new PutMapping.Builder( biIndex.getName(), indexType.getType(), mappingBuilder.buildJsonSchema().string() );

    JestResult result = factory.getInstance().execute( putMapping.build() );
    if ( !result.isSucceeded() )
    {
      logger.error( result.getErrorMessage() );
    }

    Builder bulkRequestBuilder = new Bulk.Builder().defaultIndex( biIndex.getName() ).defaultType( indexType.getType() );

    for ( Searchable searchable : data )
    {

      PaxIndexData pax = (PaxIndexData)searchable;

      if ( pax.isActive() )
      {
        XContentBuilder buildJson = mappingBuilder.buildJsonData( searchable );
        String id = mappingBuilder.buildId( searchable ).toString();
        io.searchbox.core.Index index = new Index.Builder( buildJson.string() ).index( biIndex.getName() ).type( indexType.getType() ).id( id ).build();
        bulkRequestBuilder.addAction( index );
      }
      else
      {
        bulkRequestBuilder.addAction( buildDelete( indexType, pax.getUserId() ) );
      }

    }
    BulkResult bulkResult = factory.getInstance().execute( bulkRequestBuilder.build() );
    return bulkResult.isSucceeded();
  }

  private Delete buildDelete( BIIndexType indexType, Long paxId ) throws Exception
  {
    return new Delete.Builder( String.valueOf( paxId ) ).index( biIndex.getName() ).type( indexType.getType() ).build();
  }

  @Override
  public boolean deleteByIds( BIIndexType indexType, List<Long> paxIds ) throws Exception
  {
    Builder bulkRequestBuilder = new Bulk.Builder().defaultIndex( biIndex.getName() ).defaultType( indexType.getType() );

    for ( Long paxId : paxIds )
    {
      Delete delete = new Delete.Builder( String.valueOf( paxId ) ).index( biIndex.getName() ).type( indexType.getType() ).build();
      bulkRequestBuilder.addAction( delete );

    }

    return factory.getInstance().execute( bulkRequestBuilder.build() ).isSucceeded();
  }

  @Override
  public boolean createIndex() throws Exception
  {
    CreateIndex index = new CreateIndex.Builder( biIndex.getName() ).settings( BIIndex.ANALYSIS_SETTINGS ).build();
    JestResult result = factory.getInstance().execute( index );

    if ( !result.isSucceeded() )
    {
      logger.warn( "Failed to create Index with name '" + biIndex.getName() + "': " + buildResponseError( result ) );
    }
    return result.isSucceeded();
  }

  @Override
  public boolean deleteIndex() throws Exception
  {
    JestResult result = factory.getInstance().execute( new DeleteIndex.Builder( biIndex.getName() ).build() );
    if ( !result.isSucceeded() )
    {
      logger.warn( "Failed to delete Index with name '" + biIndex.getName() + "': " + buildResponseError( result ) );
    }
    return result.isSucceeded();
  }

  @Override
  public boolean indexExists() throws Exception
  {
    JestResult result = factory.getInstance().execute( new IndicesExists.Builder( biIndex.getName() ).build() );

    if ( !result.isSucceeded() )
    {
      logger.info( String.format( "Failed Verifying existence of index \"%s\" :" + buildResponseError( result ), biIndex.getName() ) );
    }
    return result.isSucceeded();
  }

  private String buildResponseError( JestResult result )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "ResponseCode: " + result.getResponseCode() + " Message: " + result.getErrorMessage() );
    return sb.toString();
  }

  @Override
  public boolean close() throws Exception
  {
    JestResult result = factory.getInstance().execute( new CloseIndex.Builder( biIndex.getName() ).build() );
    if ( !result.isSucceeded() )
    {
      logger.debug( "Failed to close Index with name '" + biIndex.getName() + "': " + buildResponseError( result ) );
    }
    return result.isSucceeded();

  }

  @Override
  public void sendClientResetMessage()
  {
    getGJavaMessageService().sendToJmsTopic( createResetMessage() );
  }

  @Override
  public void resetClientFactory()
  {
    factory.reset();
  }

  @Override
  public boolean open() throws Exception
  {
    JestResult result = factory.getInstance().execute( new OpenIndex.Builder( biIndex.getName() ).build() );
    if ( !result.isSucceeded() )
    {
      logger.debug( "Failed to Open index name :" + biIndex.getName() + " : " + buildResponseError( result ) );
    }
    return result.isSucceeded();
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public EsIndexStatus getIndexStatus()
  {
    EsIndexStatus status = new EsIndexStatus();

    try
    {
      if ( indexExists() )
      {
        // IndexStatusAction statsRequest = new IndexStatusAction( biIndex.getName() );
        // IndexStatusResult result = (IndexStatusResult)factory.getInstance().execute(
        // statsRequest);
        Stats stats = new Stats.Builder().addIndex( biIndex.getName() ).build();
        JestResult result = factory.getInstance().execute( stats );
        JsonObject response = result.getJsonObject().getAsJsonObject();
        if ( result.isSucceeded() )
        {
          JsonObject primaries = response.get( "_all" ).getAsJsonObject().get( "primaries" ).getAsJsonObject();
          JsonObject docObj = primaries.get( "docs" ).getAsJsonObject();
          // docs
          status.setDocumentCount( docObj.get( "count" ).getAsInt() );
          status.setDocumentsDeleted( docObj.get( "deleted" ).getAsInt() );
          // search metrics
          JsonObject searchObj = primaries.get( "search" ).getAsJsonObject();
          status.setQueryTimeInMillis( searchObj.get( "query_time_in_millis" ).getAsLong() );
          status.setSearchCount( searchObj.get( "query_total" ).getAsInt() );
          status.setFetchTimeInMillis( searchObj.get( "fetch_time_in_millis" ).getAsLong() );
          // store metrics
          JsonObject storeObj = primaries.get( "store" ).getAsJsonObject();
          status.setStoreSize( storeObj.get( "size_in_bytes" ).getAsFloat() );
        }
        status.setExists( IndexExistance.EXISTS );
      }
    }
    catch( Exception e )
    {
      // check for security issues..
      if ( isSecurityException( e ) )
      {
        status.setSecurityCorrect( false );
      }
      else
      {
        logger.error( e.getMessage(), e );
      }
    }
    finally
    {
      status.setName( biIndex.getName() );
      status.setEndPoint( getEndPoint() );
    }

    return status;
  }

  private boolean isSecurityException( Exception e )
  {
    String msg = e.getMessage();
    if ( null != msg )
    {
      return msg.contains( "security_exception" );
    }
    return false;
  }

  private String getEndPoint()
  {
    PropertySetItem prop = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.AUTOCOMPLETE_ES_URL );
    return null != prop ? prop.getStringVal() : null;
  }

  public GJavaMessageService getGJavaMessageService()
  {
    return (GJavaMessageService)BeanLocator.getBean( GJavaMessageService.BEAN_NAME );
  }

  protected ResetElasticSearchClientFactoryMessage createResetMessage()
  {
    return new ResetElasticSearchClientFactoryMessage();
  }

}
