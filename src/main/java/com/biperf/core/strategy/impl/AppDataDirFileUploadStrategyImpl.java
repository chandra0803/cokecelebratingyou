
package com.biperf.core.strategy.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.ImageUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.biperf.core.service.system.SystemVariableService;

public class AppDataDirFileUploadStrategyImpl extends BaseStrategy implements FileUploadStrategy
{
  private static final Log logger = LogFactory.getLog( AppDataDirFileUploadStrategyImpl.class );

  private String clientPrefix;

  public boolean uploadFileData( String filePath, InputStream inputStream ) throws ServiceErrorException
  {
    FileOutputStream writer = null;
    try
    {
      String path = ImageUtils.convertPath( getParentPath() + ImageUtils.getPath( filePath ) );
      String fileName = ImageUtils.getFilename( filePath );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Request to upload file[" + fileName + "] to path[" + path + "]" );
      }

      if ( path != null && !new File( path ).exists() )
      {
        new File( path ).mkdirs();
      }

      File extractFile = new File( path, fileName );
      boolean exists = extractFile.exists() || extractFile.createNewFile();
      if ( exists )
      {
        writer = new FileOutputStream( extractFile );
        byte[] data = new byte[1024];
        while ( inputStream.read( data ) > 0 )
        {
          writer.write( data );
        }
        inputStream.close();
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "File[" + fileName + "] to [" + path + "] successfully uploaded" );
        }

        return true;
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
        if ( null != writer )
        {
          writer.close();
        }
      }
      catch( Throwable t )
      {
      }
    }

    return false;
  }

  public boolean uploadFileData( String filePath, byte[] data ) throws ServiceErrorException
  {
    FileOutputStream writer = null;
    try
    {
      String path = ImageUtils.convertPath( getParentPath() + ImageUtils.getPath( filePath ) );
      String fileName = ImageUtils.getFilename( filePath );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Request to upload file[" + fileName + "] to path[" + path + "]" );
      }

      if ( path != null && !new File( path ).exists() )
      {
        new File( path ).mkdirs();
      }

      File extractFile = new File( path, fileName );
      boolean exists = extractFile.exists() || extractFile.createNewFile();
      if ( exists )
      {
        writer = new FileOutputStream( extractFile );
        writer.write( data );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "File[" + fileName + "] to [" + path + "] successfully uploaded" );
        }

        return true;
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
        if ( null != writer )
        {
          writer.close();
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
    DataInputStream reader = null;
    InputStream iStream = null;
    try
    {
      String path = ImageUtils.convertPath( getParentPath() + filePath );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Request to read data from file path : " + path );
      }

      iStream = new FileInputStream( path );
      reader = new DataInputStream( iStream );

      return IOUtils.toByteArray( reader );

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
    }
  }

  public boolean delete( String filePath ) throws ServiceErrorException
  {
    String path = ImageUtils.convertPath( getParentPath() + ImageUtils.getPath( filePath ) );
    String fileName = ImageUtils.getFilename( filePath );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Request to delete file[" + fileName + "] from path[" + path + "]" );
    }

    File extractFile = new File( path, fileName );
    if ( extractFile.exists() )
    {
      return extractFile.delete();
    }
    return false;
  }

  @Override
  public String getUrlLocationPrefix()
  {
    return getParentPath();
  }

  private String getParentPath()
  {
    return System.getProperty( "appdatadir" ) + '/' + clientPrefix + '/';
  }

  public void setClientPrefix( String clientPrefix )
  {
    this.clientPrefix = clientPrefix;
  }
	// Client customization for WIP #57733 starts
  
  public byte[] getFileDataforPushProcess( String filePath ) throws ServiceErrorException
  {
    String urlLocation = null;
    //DataInputStream reader = null;
    InputStream iStream = null;
    HttpURLConnection conn = null;
    try
    {
      urlLocation = getUrlReportLocationPrefix() + filePath ;

      URL url = new URL( urlLocation );
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestProperty("Accept-Charset", "UTF-8");
      byte[] data = IOUtils.toByteArray( conn.getInputStream() ) ;//new byte[0];
      
      if ( logger.isDebugEnabled() )
        logger.debug( "Successfully hit URL: " + urlLocation );

      return data;
    }
    catch( IOException e )
    {
      logger.error( "Failed to invoke URL " + urlLocation, e );
    }
    catch( Throwable t )
    {
      logger.error( "------------FILE DOWNLOAD ISSUE------ "+t.getMessage(), t);
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
        logger.error( "------------STREAM CLOSE ISSUE------ "+t.getMessage(), t);
      }
     }

    return null;
  }

 // Client customization for WIP #57733 ends
  
  public String getUrlReportLocationPrefix()
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
