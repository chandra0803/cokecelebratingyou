
package com.biperf.core.strategy.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.WebdavResource;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;

public class WebdavFileUploadStrategyImpl extends BaseStrategy implements FileUploadStrategy
{
  private static final Log logger = LogFactory.getLog( WebdavFileUploadStrategyImpl.class );

  // to isolate the changes, this class delegates to and Amazon impl
  private FileUploadStrategy awsFileUploadStrategy;
  private Properties webDavProperties;

  private boolean setUp( String path ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    try
    {
      String webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
      logger.error( "Connecting to : " + webdavHref );
      HttpURL httpURL = new HttpURL( webdavHref );
      resource = new WebdavResource( httpURL );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
      }

      if ( resource.exists() )
      {
        String href = getWebDavProperty( "collectionName" ) + path;
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
      e.printStackTrace();
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
      webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" ) + path;
      resource = new WebdavResource( new HttpURL( webdavHref ) );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
      }

      return true;
    }
    catch( Exception e )
    {

      e.printStackTrace();
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

  @Override
  public boolean uploadFileData( String filePath, byte[] data ) throws ServiceErrorException
  {
    if ( AwsUtils.isAws() )
    {
      return this.awsFileUploadStrategy.uploadFileData( filePath, data );
    }

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
        String webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
        resource = new WebdavResource( new HttpURL( webdavHref ) );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
        }

        if ( resource.exists() )
        {
          String href = getWebDavProperty( "collectionName" ) + ImageUtils.convertToUrlPath( filePath );
          boolean success = resource.putMethod( href, data );
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "Data to webdav resource [" + href + "] successfully uploaded : " + success );
          }

          cgiVirusScan( ImageUtils.convertToUrlPath( filePath ) );
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "Submitted for AV Scan" );
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
  
//Client customization for WIP #57733 starts
  
 public boolean uploadReportFileData( String filePath, byte[] data ) throws ServiceErrorException
 {
   WebdavResource resource = null;
   try
   {
     String pathToValidate = ImageUtils.convertToUrlPath( ImageUtils.getPath( filePath ) );
     if ( logger.isDebugEnabled() )
       logger.debug( "Validating path [" + pathToValidate + "]" );

     if ( validatePath( pathToValidate, true ) )
     {
       String webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
       resource = new WebdavResource( new HttpURL( webdavHref ) );
       if ( logger.isDebugEnabled() )
         logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );

       if ( resource.exists() )
       {
         String href = getWebDavProperty( "collectionName" ) + ImageUtils.convertToUrlPath( filePath );
         boolean success = resource.putMethod( href, data );
         if ( logger.isDebugEnabled() )
           logger.debug( "Data to webdav resource [" + href + "] successfully uploaded : " + success );

         cgiVirusScan( ImageUtils.convertToUrlPath( filePath ) );
         if ( logger.isDebugEnabled() )
           logger.debug( "Submitted for AV Scan" );

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
         resource.close();
     }
     catch( Throwable t )
     {
     }
   }

   return false;
 }

//Client customization for WIP #57733 ends


  protected byte[] getFileDataDeprecated( String filePath ) throws ServiceErrorException
  {
    WebdavResource resource = null;
    DataInputStream reader = null;
    InputStream iStream = null;
    try
    {
      String webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" ) + ImageUtils.convertToUrlPath( ImageUtils.getPath( filePath ) );
      resource = new WebdavResource( new HttpURL( webdavHref ) );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
      }

      if ( resource.exists() )
      {
        String href = getWebDavProperty( "collectionName" ) + ImageUtils.convertToUrlPath( filePath );
        iStream = resource.getMethodData( href );
        reader = new DataInputStream( iStream );

        byte[] data = new byte[reader.available()];
        reader.read( data );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Reading data from webdav resource [" + href + "] successfully completed" );
        }

        return data;
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "Error reading file", e );
    }
    finally
    {
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

    return null;
  }

  @Override
  public byte[] getFileData( String filePath ) throws ServiceErrorException
  {
    if ( AwsUtils.isAws() )
    {
      return this.awsFileUploadStrategy.getFileData( filePath );
    }

    final String PROTOCOL_HTTP = "http";
    final String PROTOCOL_HTTPS = "https";

    String urlLocation = null;
    DataInputStream reader = null;
    InputStream iStream = null;
    HttpURLConnection conn = null;
    byte[] data = null;
    try
    {
      urlLocation = getUrlLocationPrefix() + ImageUtils.convertToUrlPath( filePath );

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

  // Client customization for WIP #57733 starts
  
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

  private Proxy getProxyConfig()
  {
    if ( !Environment.isCtech() )
    {
      return Proxy.NO_PROXY;
    }

    return Environment.buildProxy();
  }

  @Override
  public boolean delete( String filePath ) throws ServiceErrorException
  {
    if ( AwsUtils.isAws() )
    {
      return this.awsFileUploadStrategy.delete( filePath );
    }

    WebdavResource resource = null;
    try
    {
      String webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
      resource = new WebdavResource( new HttpURL( webdavHref ) );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
      }

      if ( resource.exists() )
      {
        // The delete CGI script looks for the file in WEBDAV
        // Therefore invoke CGI delete first and then the WEBDAV delete
        cgiDelete( ImageUtils.convertToUrlPath( filePath ) );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Submitted for Delete" );
        }

        String href = getWebDavProperty( "collectionName" ) + ImageUtils.convertToUrlPath( filePath );
        resource.deleteMethod( href );

        // returning success by default after deleting
        boolean success = true;
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

  private void cgiDelete( String filePath ) throws IOException
  {
    // Request the new on demand daemon process to delete
    String collectionName = getWebDavProperty( "collectionName" );
    StringBuffer scanLocation = new StringBuffer();
    scanLocation.append( getWebDavProperty( "webDavHost" ) );
    scanLocation.append( getWebDavProperty( "deleteScript" ) );
    scanLocation.append( "&collectionname=" );
    scanLocation.append( collectionName.substring( 0, collectionName.length() - 1 ) );
    scanLocation.append( "&filename=" );
    scanLocation.append( filePath );

    postCgiScript( scanLocation.toString() );
  }

  private void cgiVirusScan( String filePath ) throws IOException
  {
    // Request the new on demand daemon process to AV scan
    String collectionName = getWebDavProperty( "collectionName" );
    StringBuffer scanLocation = new StringBuffer();
    scanLocation.append( getWebDavProperty( "webDavHost" ) );
    scanLocation.append( getWebDavProperty( "virusScanScript" ) );
    scanLocation.append( "?collectionname=" );
    scanLocation.append( collectionName.substring( 0, collectionName.length() - 1 ) );
    scanLocation.append( "&filename=" );
    scanLocation.append( filePath );

    postCgiScript( scanLocation.toString() );
  }

  private void postCgiScript( String cgiScriptPath ) throws IOException
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Post CGI script URL : " + cgiScriptPath );
    }

    // Use Timeout to make the request asynchronous
    HttpClientParams params = new HttpClientParams();
    int timeoutInterval = new Integer( getWebDavProperty( "scriptTimeoutIntervalInSec" ) ).intValue();
    params.setSoTimeout( (int)DateUtils.MILLIS_PER_SECOND * timeoutInterval );

    HttpClient client = new HttpClient( params );

    // HttpMethod GET
    HttpMethodBase method = new GetMethod( new URL( cgiScriptPath ).toString() );

    // We do not have to use proxy settings, but might have to in the future.
    String proxyHost = null;
    int proxyPort = 0;

    // Use Proxy settings if provided
    HostConfiguration config = client.getHostConfiguration();
    if ( null != proxyHost )
    {
      config.setProxyHost( new ProxyHost( proxyHost, proxyPort ) );
    }

    try
    {
      // Execute the method
      int statusCode = client.executeMethod( method );

      // Check for successful STATUS
      if ( statusCode != HttpStatus.SC_OK )
      {
        throw new IOException( "Operation failed with Connection Status Code(" + statusCode + ") : " + method.getStatusLine() );
      }
    }
    catch( SocketTimeoutException e )
    {
      // Timeout exception is expected to make the request asynchronous
    }
    finally
    {
      // Release the connection
      method.releaseConnection();
    }
  }

  @Override
  public boolean uploadFileData( String filePath, InputStream inputStream ) throws ServiceErrorException
  {
    if ( AwsUtils.isAws() )
    {
      return this.awsFileUploadStrategy.uploadFileData( filePath, inputStream );
    }

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
        String webdavHref = getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
        resource = new WebdavResource( new HttpURL( webdavHref ) );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Connection to webdav resource [" + webdavHref + "] successfully established" );
        }

        if ( resource.exists() )
        {
          String href = getWebDavProperty( "collectionName" ) + ImageUtils.convertToUrlPath( filePath );
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

          cgiVirusScan( ImageUtils.convertToUrlPath( filePath ) );
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "Submitted for AV Scan" );
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

  @Override
  public String getUrlLocationPrefix()
  {
    if ( AwsUtils.isAws() )
    {
      return this.awsFileUploadStrategy.getUrlLocationPrefix();
    }
    return getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
  }

  private String getWebDavProperty( String propertyName )
  {
    return webDavProperties.getProperty( propertyName );
  }

  public void setWebDavProperties( Properties webDavProperties )
  {
    this.webDavProperties = webDavProperties;
  }

  public FileUploadStrategy getAwsFileUploadStrategy()
  {
    return awsFileUploadStrategy;
  }

  public void setAwsFileUploadStrategy( FileUploadStrategy awsFileUploadStrategy )
  {
    this.awsFileUploadStrategy = awsFileUploadStrategy;
  }
}
