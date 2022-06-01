
package com.biperf.core.indexing.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.indexing.EsIndexStatus;
import com.biperf.core.indexing.IndexHealth;
import com.biperf.core.indexing.IndexStatus;
import com.biperf.util.StringUtils;
import com.google.gson.Gson;

import io.searchbox.action.AbstractAction;
import io.searchbox.client.JestResult;

@SuppressWarnings( "rawtypes" )
public class IndexStatusAction extends AbstractAction
{
  public static String CHARSET = "utf-8";

  private static final Log logger = LogFactory.getLog( IndexStatusAction.class );

  @SuppressWarnings( "unchecked" )
  public IndexStatusAction( String indexName )
  {
    super();
    this.indexName = indexName;
    setURI( buildURI() );
  }

  public String getRestMethodName()
  {
    return "GET";
  }

  @Override
  public JestResult createNewElasticSearchResult( String raw, int responseCode, String statusMsg, Gson arg3 )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( raw + " RESPONSE CODE: " + responseCode + " STATUS MSG: " + statusMsg );
    }
    JestResult result = new IndexStatusResult( null, raw );
    return result;
  }

  @Override
  protected String buildURI()
  {
    return "/_cat/indices/" + indexName;
  }

  public class IndexStatusResult extends JestResult
  {
    private String values = null;

    public IndexStatusResult( Gson gson, String values )
    {
      super( gson );
      this.values = values;
    }

    /*
     * //response format from elasticsearch health status index pri rep docs.count docs.deleted
     * store.size pri.store.size yellow open gyoda-siemback-10am 5 1 0 0 795b 795b yellow open
     * g6bb8-siemback-10am 5 1 1212 4 396.6kb 396.6kb
     */

    public EsIndexStatus getStatus()
    {
      EsIndexStatus status = new EsIndexStatus();

      if ( null != values )
      {
        String[] valueArray = values.split( " " );
        /*
         * status.setDocumentCount( buildInteger( valueArray[5] ) ); status.setDocumentsDeleted(
         * buildInteger( valueArray[6] ) ); status.setHealth( buildIndexHealth( valueArray[0] ) );
         * status.setName( valueArray[2] ); status.setPrimaryStoreSize( valueArray[8] );
         * status.setStatus( buildIndexStatus( valueArray[1] ) ); status.setStoreSize( valueArray[7]
         * );
         */
      }

      return status;
    }

    private IndexStatus buildIndexStatus( String value )
    {
      if ( StringUtils.isEmpty( value ) )
      {
        return IndexStatus.UNKNOWN;
      }

      if ( value.equalsIgnoreCase( "open" ) )
      {
        return IndexStatus.OPEN;
      }

      if ( value.equalsIgnoreCase( "close" ) )
      {
        return IndexStatus.CLOSED;
      }

      return IndexStatus.UNKNOWN;
    }

    private IndexHealth buildIndexHealth( String value )
    {
      if ( StringUtils.isEmpty( value ) )
      {
        return IndexHealth.UNKNOWN;
      }

      if ( value.equalsIgnoreCase( "green" ) )
      {
        return IndexHealth.GREEN;
      }
      if ( value.equalsIgnoreCase( "yellow" ) )
      {
        return IndexHealth.YELLOW;
      }
      if ( value.equalsIgnoreCase( "red" ) )
      {
        return IndexHealth.RED;
      }

      return IndexHealth.UNKNOWN;
    }

    private int buildInteger( String value )
    {
      if ( StringUtils.isValidNumeric( value ) )
      {
        return new Integer( value );
      }
      return -99;
    }

  }

}
