
package com.biperf.core.ui.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.struts.upload.MultipartRequestWrapper;

public final class HttpServletRequestWrapper extends MultipartRequestWrapper
{

  private boolean customStream;
  private byte[] rawData;
  private HttpServletRequest request;
  private ResetServletInputStream servletStream;

  public HttpServletRequestWrapper( HttpServletRequest servletRequest, boolean customStream )
  {
    super( servletRequest );
    this.customStream = customStream;
    if ( customStream )
    {
      this.request = servletRequest;
      this.servletStream = new ResetServletInputStream();
    }
  }

  public String getParameter( String parameter )
  {
    String result = super.getParameter( parameter );
    if ( result == null )
    {
      return null;
    }

    String trimResult = null;
    String requestUri = super.getRequestURI();

    if ( parameter.equals( "alternateReturnUrl" ) )
    {
      if ( result != null && ( result.contains( "/stackRankListDisplay.do" ) || result.contains( "/expiredStackRankListDisplay.do" ) ) )
      {
        trimResult = result;
      }
      else
      {
        trimResult = "";
      }

      return trimResult;
    }

    if ( requestUri.contains( "/nomination/submitNomination.do" ) && ( parameter.endsWith( "drawingData" ) || parameter.endsWith( "cardData" ) )
        || requestUri.contains( "/admin/systemVariableEdit.do" ) || requestUri.contains( "/reports/exportToPDF.do" ) )
    {
      trimResult = result;
    }
    else if ( requestUri.contains( "/recognitionWizard/validate.do" ) )
    {
      if ( parameter.endsWith( "drawingData" ) || parameter.endsWith( "cardData" ) )
      {
        return result;
      }
      trimResult = StripXSSUtil.stripXSSPattern( result.trim() );
    }
    else
    {
      trimResult = StripXSSUtil.stripXSS( result.trim() );
    }

    if ( result.startsWith( "$" ) )
    {
      trimResult = " ";
    }

    return trimResult;
  }

  public String[] getParameterValues( String parameter )
  {
    String[] results = super.getParameterValues( parameter );
    if ( results == null )
    {
      return null;
    }
    int count = results.length;

    String[] trimResults = new String[count];
    String requestUri = super.getRequestURI();

    if ( requestUri.contains( "/nomination/submitNomination.do" ) && ( parameter.endsWith( "drawingData" ) || parameter.endsWith( "cardData" ) )
        || requestUri.contains( "/admin/systemVariableEdit.do" ) || requestUri.contains( "/reports/exportToPDF.do" )
        || requestUri.contains( "process/processDetailDisplay.do" ) && parameter.equalsIgnoreCase( "program" ) )
    {
      trimResults = results;
    }
    else if ( requestUri.contains( "/recognitionWizard/validate.do" ) )
    {
      for ( int i = 0; i < count; i++ )
      {
        if ( results[i].startsWith( "$" ) )
        {
          trimResults[i] = " ";
        }
        else if ( parameter.endsWith( "drawingData" ) || parameter.endsWith( "cardData" ) )
        {
          return results;
        }
        else
        {
          trimResults[i] = StripXSSUtil.stripXSSPattern( results[i].trim() );
        }
      }
    }
    else
    {
      for ( int i = 0; i < count; i++ )
      {
        if ( results[i].startsWith( "$" ) )
        {
          trimResults[i] = " ";
        }
        else
        {
          trimResults[i] = StripXSSUtil.stripXSS( results[i].trim() );
        }
      }
    }

    return trimResults;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException
  {
    if ( customStream )
    {
      if ( rawData == null )
      {
        rawData = IOUtils.toByteArray( this.request.getReader() );
        servletStream.stream = new ByteArrayInputStream( rawData );
      }
      return servletStream;
    }
    else
    {
      return super.getInputStream();
    }
  }

  @Override
  public BufferedReader getReader() throws IOException
  {
    if ( customStream )
    {
      if ( rawData == null )
      {
        rawData = IOUtils.toByteArray( this.request.getReader() );
        servletStream.stream = new ByteArrayInputStream( rawData );
      }
      return new BufferedReader( new InputStreamReader( servletStream ) );
    }
    else
    {
      return super.getReader();
    }
  }

  public void resetInputStream( byte[] newRawData )
  {
    servletStream.stream = new ByteArrayInputStream( newRawData );
  }

  /* Inner Class */
  private class ResetServletInputStream extends ServletInputStream
  {

    private InputStream stream;

    @Override
    public int read() throws IOException
    {
      return stream.read();
    }

    @Override
    public boolean isFinished()
    {
      return this.isFinished();
    }

    @Override
    public boolean isReady()
    {
      return this.isReady();
    }

    @Override
    public void setReadListener( ReadListener listener )
    {

    }
  }

}
