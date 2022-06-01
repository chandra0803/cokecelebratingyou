
package com.biperf.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileExtractUtils
{

  /**
   * Creates a directory at the specified path if it does not exist already
   * @param path - path to directory
   */
  public static void createDirIfNeeded( String path )
  {
    if ( path != null && !new File( path ).isDirectory() )
    {
      // Create a directory; all non-existent ancestor directories are automatically created
      new File( path ).mkdirs();
    }
  }
  
  public static String getVersion() throws IOException
  {
    Properties prop = new Properties();
    InputStream input = null;
    input = FileExtractUtils.class.getClassLoader().getResourceAsStream( "build.properties" );
    prop.load( input );
    return prop.getProperty( "gVersion" );
  }

}
