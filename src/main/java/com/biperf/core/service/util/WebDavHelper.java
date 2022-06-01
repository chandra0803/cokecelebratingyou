/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/util/WebDavHelper.java,v $
 */

package com.biperf.core.service.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.MoveMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;

/**
 * WebDavHelper.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>babu</td>
 * <td>Nov 10, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class WebDavHelper
{

  public static void move( WebDavConfiguration configuration, String sourceUrl, String destinationUrl, String incomingFilename, String outgoingFilename, String prefix ) throws Exception
  {
    MoveMethod method = null;
    try
    {
      String fullSourceUrl = configuration.getSubFolderPath( prefix ) + sourceUrl + incomingFilename;
      String fullDestinationUrl = configuration.getSubFolderPath( prefix ) + destinationUrl + outgoingFilename;
      if ( isFileExist( configuration, destinationUrl + outgoingFilename, prefix ) )
      {
        delete( configuration, destinationUrl + outgoingFilename, prefix, false );
      }

      method = new MoveMethod( fullSourceUrl, fullDestinationUrl, true );
      int status = configuration.getHttpClient().executeMethod( method );
      if ( status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED )
      {
        throw new Exception( "MOVE method expectged OK or CREATED" );
      }
    }
    catch( IOException e )
    {
      throw new Exception( "MOVE method failed", e );
    }
    finally
    {
      if ( method != null )
      {
        method.releaseConnection();
      }
    }
  }

  public static boolean isFileExist( WebDavConfiguration configuration, String url, String prefix ) throws Exception
  {
    boolean result = false;
    PropFindMethod method = null;
    try
    {
      method = new PropFindMethod( configuration.getSubFolderPath( prefix ) + url, DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_1 );
      int status = configuration.getHttpClient().executeMethod( method );
      if ( status != HttpStatus.SC_OK && status != HttpStatus.SC_MULTI_STATUS )
      {
        result = false;
      }
      else
      {
        result = true;
      }
    }
    catch( IOException e )
    {
      throw new Exception( "PropFind method failed", e );
    }
    finally
    {
      if ( method != null )
      {
        method.releaseConnection();
      }
    }

    return result;
  }

  public static void delete( WebDavConfiguration configuration, String url, String prefix, boolean isOriginal ) throws Exception
  {
    DeleteMethod method = null;
    try
    {
      if ( isOriginal )
      {
        method = new DeleteMethod( configuration.getRootUrl() );
      }
      else
      {
        method = new DeleteMethod( configuration.getSubFolderPath( prefix ) + url );
      }
      int status = configuration.getHttpClient().executeMethod( method );
      if ( status != HttpStatus.SC_OK && status != HttpStatus.SC_NO_CONTENT )
      {
        throw new Exception( "DELETE method expected OK or NO_CONTENT for file: " + method.getPath() );
      }
    }
    catch( IOException e )
    {
      throw new Exception( "DELETE method failed" + method.getPath(), e );
    }
    finally
    {
      if ( method != null )
      {
        method.releaseConnection();
      }
    }
  }

  public static int mkcol( WebDavConfiguration configuration, String url, String prefix ) throws Exception
  {
    int status = -1;
    MkColMethod method = null;
    try
    {
      method = new MkColMethod( configuration.getSubFolderPath( prefix ) );
      status = configuration.getHttpClient().executeMethod( method );

      method = new MkColMethod( configuration.getSubFolderPath( prefix ) + url );
      status = configuration.getHttpClient().executeMethod( method );
      if ( status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED )
      {
        throw new Exception( "MKCOL method expected OK or CREATED" );
      }
    }
    catch( IOException e )
    {
      throw new Exception( "MKCOL method failed", e );
    }
    finally
    {
      if ( method != null )
      {
        method.releaseConnection();
      }
    }
    return status;
  }

  public static String put( WebDavConfiguration configuration, String url, File file, String prefix ) throws Exception
  {
    PutMethod method = null;
    String endpath = null;
    try
    {

      // Create the valid or invalid folder if it doesn't exist.
      if ( !isFileExist( configuration, url, prefix ) )
      {
        mkcol( configuration, url, prefix );
      }
      try
      {
        delete( configuration, url + file.getName(), prefix, false );
      }
      catch( Exception e )
      {
        // e.printStackTrace();
      }
      endpath = configuration.getSubFolderPath( prefix ) + url + file.getName();
      method = new PutMethod( configuration.getSubFolderPath( prefix ) + url + file.getName() );
      method.setRequestEntity( new FileRequestEntity( file, FileExtensionContentTypeMapper.getContentType( file ) ) );
      int status = configuration.getHttpClient().executeMethod( method );
      if ( status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED )
      {
        throw new Exception( "PUT method expected OK or CREATED : " + method.getResponseBodyAsString() );
      }
    }
    catch( HttpException e )
    {
      e.printStackTrace();
      throw new Exception( "PUT method failed", e );
    }
    catch( IOException e )
    {
      e.printStackTrace();
      throw new Exception( "PUT method failed", e );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      throw new Exception( "PUT method failed", e );
    }
    finally
    {
      if ( method != null )
      {
        method.releaseConnection();
      }
    }
    return endpath;
  }
}
