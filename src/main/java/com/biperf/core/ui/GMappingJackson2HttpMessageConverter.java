
package com.biperf.core.ui;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.biperf.core.ui.roster.RosterResponse;
import com.biperf.core.ui.servlet.RosterOutputStream;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter
{

  private String jsonPrefix;

  @Override
  protected void writeInternal( Object object, HttpOutputMessage outputMessage ) throws IOException, HttpMessageNotWritableException
  {

    JsonEncoding encoding = getJsonEncoding( outputMessage.getHeaders().getContentType() );
    OutputStream body = outputMessage.getBody();
    if ( body instanceof RosterOutputStream )
    {
      // error handler is changing content type to HTML when container output stream is not passed.
      body = ( (RosterOutputStream)body ).getOriginalOutputStream();
      object = new RosterResponse( false, object );
    }
    JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createGenerator( body, encoding );

    if ( this.getObjectMapper().isEnabled( SerializationFeature.INDENT_OUTPUT ) )
    {
      jsonGenerator.useDefaultPrettyPrinter();
    }

    try
    {
      if ( this.jsonPrefix != null )
      {
        jsonGenerator.writeRaw( this.jsonPrefix );
      }
      this.getObjectMapper().writeValue( jsonGenerator, object );
    }
    catch( JsonProcessingException ex )
    {
      throw new HttpMessageNotWritableException( "Could not write JSON: " + ex.getMessage(), ex );
    }
  }

  public void setJsonPrefix( String jsonPrefix )
  {
    this.jsonPrefix = jsonPrefix;
  }

  public void setPrefixJson( boolean prefixJson )
  {
    this.jsonPrefix = prefixJson ? "{} && " : null;
  }

}
