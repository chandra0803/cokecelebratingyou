
package com.biperf.core.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils
{

  private static final ObjectMapper mapper = new ObjectMapper();

  private JsonUtils()
  {
  }

  public static String getStringValueFrom( JSONObject json, String name )
  {
    try
    {
      return json.getString( name );
    }
    catch( Throwable t )
    {
      return "";
    }
  }

  public static boolean validJSON( String jsonString )
  {
    try
    {
      mapper.readTree( jsonString );
      return true;
    }
    catch( IOException e )
    {
      return false;
    }

  }

  public static String toJsonStringFromObject( Object bean )
  {
    Writer writer = new StringWriter();
    try
    {

      mapper.writeValue( writer, bean );
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
    return writer.toString();
  }

  public static <T> T toObjectFromJsonString( String json, Class<T> clazz )
  {
    try
    {
      return mapper.readValue( json, clazz );
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
    return null;
  }

}
