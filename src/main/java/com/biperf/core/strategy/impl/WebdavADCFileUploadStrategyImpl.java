
package com.biperf.core.strategy.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.WebdavResource;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;

public class WebdavADCFileUploadStrategyImpl extends BaseStrategy implements FileUploadStrategy
{
  private static final Log logger = LogFactory.getLog( WebdavADCFileUploadStrategyImpl.class );

  private boolean setUp( String path ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    try
    {
      String webdavHref = getUrlLocationPrefix() + "/" + getSubfolder();
      resource = new WebdavResource( buildHttpURL( webdavHref ) );
      logger.error( "Connection to webdav ADC resource [" + webdavHref + "] successfully established" );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav ADC resource [" + webdavHref + "] successfully established" );
      }

      if ( resource.exists() )
      {
        String href = webdavHref + path;
        boolean success = resource.mkcolMethod( href );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Path [" + href + "] successfully setup : " + success );
        }

        return success;
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Error setup webdav resource", e );
    }
    finally
    {
      try
      {
        if ( null != resource )
        {
          resource.close();
        }
      }
      catch( Throwable t )
      {
      }
    }
    return false;
  }

  private boolean validatePath( String path, boolean split ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    String webdavHref = null;
    try
    {
      webdavHref = getUrlLocationPrefix() + path;
      logger.error( "webdavHref: " + webdavHref );
      resource = new WebdavResource( buildHttpURL( webdavHref ) );
      logger.error( "webdavHref connection established " );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
      }

      return true;
    }
    catch( Exception e )
    {
      logger.error( "Error connecting to webdav adc: " + e );
      if ( split )
      {
        String[] pathTokens = path.split( "/" );
        String tokenPath = "";
        for ( int i = 0; i < pathTokens.length; ++i )
        {
          tokenPath = tokenPath + pathTokens[i] + '/';
          if ( !validatePath( tokenPath, false ) )
          {
            return false;
          }
        }

        return true;
      }
      else
      {
        return setUp( path );
      }
    }
    finally
    {
      try
      {
        if ( null != resource )
        {
          resource.close();
        }
      }
      catch( Throwable t )
      {
      }
    }
  }

  // filePath =/werrnomm/incoming/SSIProgress_03182015.xls
  public boolean uploadFileData( String filePath, byte[] data ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    try
    {
      // pathToValidate=/werrnomm/incoming/
      String pathToValidate = ImageUtils.convertToUrlPath( ImageUtils.getPath( filePath ) );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Validating path [" + pathToValidate + "]" );
      }

      if ( validatePath( pathToValidate, true ) )
      {
        String webdavHref = getUrlLocationPrefix() + pathToValidate;
        resource = new WebdavResource( buildHttpURL( webdavHref ) );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
        }

        if ( resource.exists() )
        {
          // href = http://balsam.biperf.com/qa/abxe/werrnomm/incoming/SSIProgress_03182015.xls
          String href = ImageUtils.convertToUrlPath( getUrlLocationPrefix() + filePath );
          boolean success = resource.putMethod( href, data );
          logger.error( "Upload to webdav adc with file location: " + href + " was successful" );
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "**Data to webdav resource [" + href + "] successfully uploaded : " + success );
          }

          return true;
        }
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Error uploading file", e );
    }
    finally
    {
      try
      {
        if ( null != resource )
        {
          resource.close();
        }
      }
      catch( Throwable t )
      {
      }
    }

    return false;
  }

  public byte[] getFileData( String filePath ) throws ServiceErrorException
  {
    String urlLocation = null;
    DataInputStream reader = null;
    InputStream iStream = null;
    HttpURLConnection conn = null;
    byte[] data = null;
    try
    {
      urlLocation = getUrlLocationPrefix() + ImageUtils.convertToUrlPath( filePath );

      URL url = new URL( urlLocation );
      conn = (HttpURLConnection)url.openConnection( getProxyConfig() );
      reader = new DataInputStream( conn.getInputStream() );
      data = IOUtils.toByteArray( reader );
    }
    catch( IOException e )
    {
      logger.error( "Failed to invoke URL " + urlLocation, e );
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
      }
      try
      {
        if ( null != reader )
        {
          reader.close();
        }
      }
      catch( Throwable t )
      {
      }
    }

    return data;
  }

  private Proxy getProxyConfig()
  {
    if ( !Environment.isCtech() )
    {
      return Proxy.NO_PROXY;
    }

    String proxyHost = Environment.getProxy();
    int proxyPort = Environment.getProxyPort();
    return new Proxy( Proxy.Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
  }

  public boolean delete( String filePath ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    try
    {
      String webdavHref = getUrlLocationPrefix() + "/" + getSubfolder();
      resource = new WebdavResource( buildHttpURL( webdavHref ) );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
      }

      if ( resource.exists() )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Submitted for Delete" );
        }

        String href = ImageUtils.convertToUrlPath( filePath );
        boolean success = resource.deleteMethod( href );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Data from webdav resource [" + href + "] successfully deleted : " + success );
        }

        return success;
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Error deleting file", e );
    }
    finally
    {
      try
      {
        if ( null != resource )
        {
          resource.close();
        }
      }
      catch( Throwable t )
      {
      }
    }
    return false;
  }

  public boolean uploadFileData( String filePath, InputStream inputStream ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    try
    {
      String pathToValidate = ImageUtils.convertToUrlPath( ImageUtils.getPath( filePath ) );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Validating path [" + pathToValidate + "]" );
      }

      if ( validatePath( pathToValidate, true ) )
      {
        String webdavHref = getUrlLocationPrefix() + "/" + getSubfolder();
        resource = new WebdavResource( buildHttpURL( webdavHref ) );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
        }

        if ( resource.exists() )
        {
          String href = ImageUtils.convertToUrlPath( filePath );
          StringBuilder strData = new StringBuilder();
          byte[] bdata = new byte[1024];
          while ( inputStream.read( bdata ) > 0 )
          {
            strData.append( new String( bdata ) );
          }
          inputStream.close();
          boolean success = resource.putMethod( href, strData.toString().getBytes() );
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "Data to webdav resource [" + href + "] successfully uploaded : " + success );
          }

          return true;
        }
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Error uploading file", e );
    }
    finally
    {
      try
      {
        if ( null != resource )
        {
          resource.close();
        }
      }
      catch( Throwable t )
      {
      }
    }

    return false;
  }

  public String getUrlLocationPrefix()
  {
    return getFileInputUrl( getPrefix() );
  }

  private String getPrefix()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_PREFIX ).getStringVal();
  }

  private String getSubfolder()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_SUBFOLDER ).getStringVal();
  }

  private String getFileInputUrl( String prefix )
  {
    StringBuilder builder = new StringBuilder();
    builder.append( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.GLOBAL_FILE_PROCESSING_WEBDAV ).getStringVal() );
    builder.append( '/' );
    builder.append( prefix );
    String urlLocation = builder.toString();

    return urlLocation;
  }

  protected HttpURL buildHttpURL( String webdavHref ) throws URIException
  {
    if ( AwsUtils.isAws() )
    {
      String username = getWedDavUsername();
      String password = getWedDavPassword();
      if ( webdavHref.startsWith( "https" ) )
      {
        return new HttpsURL( username, password, webdavHref );
      }
      else
      {
        return new HttpURL( username, password, webdavHref );
      }
    }
    else
    {
      if ( webdavHref.startsWith( "https" ) )
      {
        return new HttpsURL( webdavHref );
      }
      else
      {
        return new HttpURL( webdavHref );
      }
    }
  }

//Client customization for WIP #57733 starts
  
 public byte[] getFileDataforPushProcess( String filePath ) throws ServiceErrorException
 {
   final String PROTOCOL_HTTP = "http";
   final String PROTOCOL_HTTPS = "https";

   String urlLocation = null;
   DataInputStream reader = null;
   InputStream iStream = null;
   HttpURLConnection conn = null;
   try
   {
     urlLocation =  ImageUtils.convertToUrlPath( filePath );

     // Security Certificates are not available when using Server instance name in the URL, to
     // communicate over HTTPS
     // Therefore we need to use HTTP protocol for this URL call.
     if ( urlLocation.startsWith( PROTOCOL_HTTPS ) )
     {
       urlLocation = urlLocation.replace( PROTOCOL_HTTPS, PROTOCOL_HTTP );
     }
     URL url = new URL( urlLocation );
     conn = (HttpURLConnection)url.openConnection();
     reader = new DataInputStream( conn.getInputStream() );

     return IOUtils.toByteArray(reader);
   }
   catch( IOException e )
   {
     logger.error( "Failed to invoke URL " + urlLocation, e );
   }
   finally
   {
     if ( conn != null )
       conn.disconnect();
     try
     {
       if ( null != iStream )
         iStream.close();
     }
     catch( Throwable t )
     {
     }
     try
     {
       if ( null != reader )
         reader.close();
     }
     catch( Throwable t )
     {
     }
   }

   return null;
 }

 // Client customization for WIP #57733 ends

  protected String getWedDavUsername()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.AWS_GLOBAL_FILE_PROCESSING_WEBDAV_USERNAME ).getStringVal();
  }

  protected String getWedDavPassword()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.AWS_GLOBAL_FILE_PROCESSING_WEBDAV_PASSWORD ).getStringVal();
  }
}
