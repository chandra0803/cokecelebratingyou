/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/util/FileExtensionContentTypeMapper.java,v $
 */

package com.biperf.core.service.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * FileExtensionContentTypeMapper.
 * FIXME move data to WebDavFileType (do lookup and call getContentType)
 *
 * @author repko
 * @since Sep 11, 2008
 * @version 1.0
 */
public class FileExtensionContentTypeMapper
{
  private static Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>();

  static
  {
    CONTENT_TYPE_MAP.put( "avi", "video/avi" );
    CONTENT_TYPE_MAP.put( "doc", "application/vnd.msword" );
    CONTENT_TYPE_MAP.put( "gif", "image/gif" );
    CONTENT_TYPE_MAP.put( "jpg", "image/jpeg" );
    CONTENT_TYPE_MAP.put( "mpg", "video/mpeg" );
    CONTENT_TYPE_MAP.put( "pdf", "application/pdf" );
    CONTENT_TYPE_MAP.put( "ppt", "application/vnd.ms-powerpoint" );
    CONTENT_TYPE_MAP.put( "png", "image/x-png" );
    CONTENT_TYPE_MAP.put( "ram", "application/vnd.rn-realaudio" );
    CONTENT_TYPE_MAP.put( "wmv", "video/x-ms-wmv" );
    CONTENT_TYPE_MAP.put( "xls", "application/vnd.ms-excel" );
    CONTENT_TYPE_MAP.put( "xml", "text/xml" );
    CONTENT_TYPE_MAP.put( "txt", "text/plain" );
    CONTENT_TYPE_MAP.put( "csv", "text/plain" );
  }

  public static String getContentType( File file )
  {
    String fileName = file.getName();
    String extension = fileName.substring( fileName.lastIndexOf( '.' ) + 1 );
    return CONTENT_TYPE_MAP.get( extension ) == null ? "text/plain" : CONTENT_TYPE_MAP.get( extension );
  }
}
