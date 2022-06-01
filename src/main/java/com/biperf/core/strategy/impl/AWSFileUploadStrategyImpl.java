
package com.biperf.core.strategy.impl;

import static com.biperf.core.utils.Environment.getEnvironment;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.ImageUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.biperf.core.service.system.SystemVariableService;
import org.apache.commons.io.IOUtils;

public class AWSFileUploadStrategyImpl extends BaseStrategy implements FileUploadStrategy
{
  private static final Log log = LogFactory.getLog( AWSFileUploadStrategyImpl.class );

  private AmazonClientFactory amazonClientFactory;

  @Override
  public boolean uploadFileData( String filePath, byte[] data ) throws ServiceErrorException
  {
    return uploadFileData( filePath, new ByteArrayInputStream( data ) );
  }

  @Override
  public byte[] getFileData( String filePath ) throws ServiceErrorException
  {
    AmazonS3 s3client = null;
    String urlKey = null;
    byte[] data = null;
    try
    {
      s3client = amazonClientFactory.getClient();
      urlKey = getUrlLocationPrefix() + ImageUtils.convertToUrlPath( filePath );
      S3Object object = s3client.getObject( new GetObjectRequest( getBucketName(), urlKey ) );
      data = new byte[(int)object.getObjectMetadata().getContentLength()];
      DataInputStream dataIs = new DataInputStream( object.getObjectContent() );
      dataIs.readFully( data );
      dataIs.close();
      object.close();
    }
    catch( AmazonServiceException ase )
    {
      String msg = buildAmazonLog( ase );
      log.error( msg, ase );
      throw new ServiceErrorException( buildAmazonLog( ase ), ase );
    }
    catch( Throwable t )
    {
      log.error( t.getMessage(), t );
      throw new ServiceErrorException( t.getMessage(), t );
    }
    finally
    {
      if ( s3client != null )
      {
        s3client.shutdown();
      }
    }
    return data;
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
     log.error( "Failed to invoke URL " + urlLocation, e );
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


  @Override
  public boolean delete( String filePath ) throws ServiceErrorException
  {
    AmazonS3 s3client = null;
    try
    {
      s3client = amazonClientFactory.getClient();
      s3client.deleteObject( new DeleteObjectRequest( getBucketName(), getUrlLocationPrefix() + ImageUtils.convertToUrlPath( filePath ) ) );
    }
    catch( AmazonServiceException ase )
    {
      String msg = buildAmazonLog( ase );
      log.error( msg, ase );
      throw new ServiceErrorException( buildAmazonLog( ase ), ase );
    }
    catch( Throwable t )
    {
      log.error( t.getMessage(), t );
      throw new ServiceErrorException( t.getMessage(), t );
    }
    finally
    {
      if ( s3client != null )
      {
        s3client.shutdown();
      }
    }

    return true;
  }

  @Override
  public boolean uploadFileData( String filePath, InputStream inputStream ) throws ServiceErrorException
  {
    AmazonS3 s3client = null;
    String path = null;
    try
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Upload file to AWS with file path: " + filePath );
      }
      /*
       * NOTE: due to the fact that the filePath is being passed in by mulitple clients- we need to
       * make sure that they DO NOT start with a slash. Doing this here rather than sprinkling the
       * logic every else
       */
      if ( filePath.startsWith( "/" ) )
      {
        filePath = filePath.substring( 1 );
        if ( log.isDebugEnabled() )
        {
          log.debug( "Upload file to AWS with modified file path: " + filePath );
        }
      }
      log.error( "***** filePath " + filePath );
      s3client = amazonClientFactory.getClient();
      path = getUrlLocationPrefix() + ImageUtils.convertToUrlPath( filePath );
      if ( log.isDebugEnabled() )
      {
        log.debug( "Generated upload filepath for image PUT: " + path );
      }
      log.error( "***** path " + path );
      PutObjectRequest putRequest = new PutObjectRequest( getBucketName(), path, inputStream, new ObjectMetadata() );
      PutObjectResult result = s3client.putObject( putRequest );
    }
    catch( AmazonServiceException ase )
    {
      String msg = buildAmazonLog( ase );
      log.error( msg, ase );
      throw new ServiceErrorException( msg, ase );
    }
    catch( Throwable t )
    {
      log.error( t.getMessage(), t );
      throw new ServiceErrorException( t.getMessage(), t );
    }
    finally
    {
      if ( s3client != null )
      {
        s3client.shutdown();
      }
    }
    return true;
  }

  protected String getBucketName()
  {
    return getSystemVariableService().getContextName() + "-" + getEnvironment() + "-user-content";
  }

  @Override
  public String getUrlLocationPrefix()
  {
    return getSystemVariableService().getContextName() + "-cm/cm3dam/";
  }

  public AmazonClientFactory getAmazonClientFactory()
  {
    return amazonClientFactory;
  }

  public void setAmazonClientFactory( AmazonClientFactory amazonClientFactory )
  {
    this.amazonClientFactory = amazonClientFactory;
  }

  private String buildAmazonLog( AmazonServiceException ase )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason. " + ase.getMessage() );
    sb.append( "\nError Message:    " + ase.getMessage() );
    sb.append( "\nHTTP Status Code: " + ase.getStatusCode() );
    sb.append( "\nAWS Error Code:   " + ase.getErrorCode() );
    sb.append( "\nError Type:       " + ase.getErrorType() );
    sb.append( "\nRequest ID:       " + ase.getRequestId() );
    return sb.toString();
  }
}
