
package com.biperf.core.ui;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.biperf.core.ui.roster.RosterResponse;
import com.biperf.core.ui.servlet.RosterOutputStream;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class GMappingJackson2JsonView extends MappingJackson2JsonView
{

  private String jsonPrefix;

  private boolean updateContentLength = false;

  @Override
  protected void renderMergedOutputModel( Map<String, Object> model, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    OutputStream stream = this.updateContentLength ? createTemporaryOutputStream() : response.getOutputStream();
    Object value = null;
    if ( stream instanceof RosterOutputStream )
    {
      this.getObjectMapper().registerModule( new JacksonConfig() );
      this.getObjectMapper().registerModule( new JavaTimeModule() );
      this.getObjectMapper().disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );

      Set<String> renderedAttributes = !CollectionUtils.isEmpty( getModelKeys() ) ? getModelKeys() : model.keySet();
      for ( Map.Entry<String, Object> entry : model.entrySet() )
      {
        if ( ! ( entry.getValue() instanceof BindingResult ) && renderedAttributes.contains( entry.getKey() ) )
        {
          value = entry.getValue();
          break;
        }
      }
      value = new RosterResponse( true, value );
    }
    else
    {
      value = filterModel( model );
    }

    writeContent( stream, value, this.jsonPrefix );
    if ( this.updateContentLength )
    {
      writeToResponse( response, (ByteArrayOutputStream)stream );
    }
  }

  public void setUpdateContentLength( boolean updateContentLength )
  {
    this.updateContentLength = updateContentLength;
  }

  public void setJsonPrefix( String jsonPrefix )
  {
    this.jsonPrefix = jsonPrefix;
  }

}
