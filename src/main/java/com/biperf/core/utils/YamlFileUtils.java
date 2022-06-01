
package com.biperf.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;
import org.yaml.snakeyaml.Yaml;

@Component
public class YamlFileUtils
{
  private static final Logger log = LoggerFactory.getLogger( YamlFileUtils.class );

  private static final String DATA_DIR_PROPERTY = "appsecuredatadir";

  /**
   * Deserialize an object from a Yaml file
   * @param pathName Full path to file
   */
  @SuppressWarnings( "unchecked" )
  public <T> T readYamlFile( String pathName ) throws FileNotFoundException
  {
    FileInputStream fileInputStream = new FileInputStream( new File( pathName ) );
    T data = (T)new Yaml().load( fileInputStream );
    try
    {
      fileInputStream.close();
    }
    catch( IOException e )
    {
      log.error( "Error closing yaml file", e );
    }
    return data;
  }

  /**
   * Serialize an object to a Yaml file
   * @param pathName Full path to file
   */
  public void writeYamlFile( String pathName, Object data ) throws IOException
  {
    FileWriter fileWriter = new FileWriter( pathName );
    new Yaml().dump( data, fileWriter );
    fileWriter.close();
  }

  /** Read honeycomb properties yaml file from secure directory */
  public Map<String, Map<String, Object>> readAppSecureDir( String fileName ) throws FileNotFoundException
  {
    return readYamlFile( HtmlUtils.htmlEscape( System.getProperty( DATA_DIR_PROPERTY ) ) + File.separatorChar + fileName );
  }

  /**
   * Writes a yaml file to the app secure directory. Overwrites an existing file, or creates a new file.
   * @throws IOException If file could not be created / opened / written to, etc
   */
  public void writeAppSecureDir( String fileName, Object data ) throws IOException
  {
    writeYamlFile( System.getProperty( DATA_DIR_PROPERTY ) + File.separatorChar + fileName, data );
  }

}
