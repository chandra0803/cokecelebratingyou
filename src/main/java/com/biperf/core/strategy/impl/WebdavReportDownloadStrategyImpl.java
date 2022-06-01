
package com.biperf.core.strategy.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.ReportDownloadStrategy;

public class WebdavReportDownloadStrategyImpl extends BaseStrategy implements ReportDownloadStrategy
{
  private static final Log logger = LogFactory.getLog( WebdavReportDownloadStrategyImpl.class );

  public byte[] getFileData( String filePath ) throws ServiceErrorException
  {
    String urlLocation = null;
    // DataInputStream reader = null;
    InputStream iStream = null;
    HttpURLConnection conn = null;
    byte[] data = null;
    try
    {
      urlLocation = getUrlLocationPrefix() + filePath;

      URL url = new URL( urlLocation );
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestProperty( "Accept-Charset", "UTF-8" );
      data = IOUtils.toByteArray( conn.getInputStream() );// new byte[0];

      // IOUtils.getInputStreamBytes( conn.getInputStream() ) ;//new byte[0];

      /*
       * URL url = new URL( urlLocation ); conn = (HttpURLConnection)url.openConnection(); reader =
       * new DataInputStream( conn.getInputStream() ); byte[] data = new byte[reader.available()];
       * reader.read( data );
       */
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Successfully hit URL: " + urlLocation );
      }
    }
    catch( IOException e )
    {
      logger.error( "Failed to invoke URL " + urlLocation, e );
    }
    catch( Throwable t )
    {
      logger.error( "------------FILE DOWNLOAD ISSUE------ " + t.getMessage(), t );
    }
    finally
    {
      if ( conn != null )
      {
        conn.disconnect();
      }
      try
      {
        if ( null != iStream )
        {
          iStream.close();
        }
      }
      catch( Throwable t )
      {
        logger.error( "------------STREAM CLOSE ISSUE------ " + t.getMessage(), t );
      }
      /*
       * try { if ( null != reader ) reader.close(); } catch( Throwable t ) { logger.error(
       * "------------READER CLOSE ISSUE------ "+t.getMessage(), t); }
       */
    }

    return data;
  }

  public void writeFileData( String filePath, final HttpServletResponse response ) throws ServiceErrorException
  {
    String urlLocation = null;
    HttpURLConnection conn = null;
    try
    {
      urlLocation = getUrlLocationPrefix() + filePath;
      logger.error( "urlLocation: " + urlLocation );

      URL url = new URL( urlLocation );
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestProperty( "Accept-Charset", "UTF-8" );
      byte[] buffer = new byte[8192];
      int len;
      while ( ( len = conn.getInputStream().read( buffer ) ) != -1 )
      {
        response.getOutputStream().write( buffer, 0, len );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Successfully hit URL: " + urlLocation );
      }

    }
    catch( IOException e )
    {
      logger.error( "Failed to invoke URL " + urlLocation, e );
    }
    catch( Throwable t )
    {
      logger.error( "------------FILE DOWNLOAD ISSUE------ " + t.getMessage(), t );
    }
    finally
    {
      if ( conn != null )
      {
        conn.disconnect();
      }
    }
  }

  public String getUrlLocationPrefix()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.GLOBAL_FILE_PROCESSING_WEBDAV ).getStringVal() );
    builder.append( '/' );
    builder.append( getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_PREFIX ).getStringVal() );
    builder.append( "/reports/" );
    String urlLocation = builder.toString();

    return urlLocation;
  }

}
