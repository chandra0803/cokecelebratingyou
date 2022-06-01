
package com.biperf.core.indexing;

import static com.biperf.core.utils.Environment.ENV_DEV;
import static com.biperf.core.utils.Environment.getEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.biperf.core.utils.Environment;

@Component
public class BIIndex
{
  private static final Log log = LogFactory.getLog( BIIndex.class );

  private String name;

  public String getName()
  {
    StringBuilder sb = new StringBuilder();
    // for local environments, use the machine name so we can
    // co-locate indices with other developers
    if ( ENV_DEV.equalsIgnoreCase( getEnvironment() ) )
    {
      sb.append( name );
      sb.append( "-" );
      sb.append( Environment.ENV_PRE );
    }
    else
    {
      sb.append( name );
      sb.append( "-" );
      sb.append( getEnvironment() );
    }

    String indexName = sb.toString().toLowerCase();// index names can only be lowercase

    if ( indexName.length() > 30 ) // max length for an index name is 30 characters
    {
      String suffix = indexName.substring( indexName.lastIndexOf( "-", indexName.length() ) );
      int suffixLength = suffix.length();
      String prefix = indexName.substring( 0, indexName.lastIndexOf( "-" ) );
      prefix = prefix.substring( 0, 30 - suffixLength );
      return prefix + suffix;
    }

    return indexName;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  private String buildLocalIndexName()
  {
    String localName = "";

    Map<String, String> env = System.getenv();
    if ( env.containsKey( "COMPUTERNAME" ) )
    {
      localName = env.get( "COMPUTERNAME" );
    }
    else if ( env.containsKey( "HOSTNAME" ) )
    {
      localName = env.get( "HOSTNAME" );
    }
    else
    {
      try
      {
        localName = InetAddress.getLocalHost().getHostName();
      }
      catch( UnknownHostException e )
      {
        log.error( e.getMessage(), e );
      }
    }

    return localName;
  }

  public static String ANALYSIS_SETTINGS;
  static
  {
    InputStream schemaAsStream = null;
    try
    {
      schemaAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "/es-model/index-settings.json" );
      StringWriter writer = new StringWriter();
      IOUtils.copy( schemaAsStream, writer );
      ANALYSIS_SETTINGS = writer.toString();
    }
    catch( Exception e )
    {
      log.error( e );
    }
    finally
    {
      try
      {
        if ( Objects.nonNull( schemaAsStream ) )
        {
          schemaAsStream.close();
        }
      }
      catch( IOException e )
      {
        // closing quietly
      }
    }
  }

}
