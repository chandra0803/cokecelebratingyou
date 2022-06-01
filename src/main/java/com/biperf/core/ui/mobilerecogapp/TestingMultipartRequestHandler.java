
package com.biperf.core.ui.mobilerecogapp;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.CommonsMultipartRequestHandler;

public class TestingMultipartRequestHandler extends CommonsMultipartRequestHandler
{

  @Override
  public void handleRequest( HttpServletRequest request ) throws ServletException
  {

    StringBuilder h = new StringBuilder();
    h.append( "\n*\n*************************************************" + "\n headers...." + "\n*************************************************\n" );

    Enumeration<String> headerNames = request.getHeaderNames();
    while ( headerNames.hasMoreElements() )
    {
      String headerName = headerNames.nextElement();
      Enumeration<String> headerValues = request.getHeaders( headerName );
      h.append( "\nheader: " ).append( headerName );
      while ( headerValues.hasMoreElements() )
      {
        String headerValue = headerValues.nextElement();
        h.append( "\n --> " ).append( headerValue );
      }
    }

    h.append( "\n*************************************************" + "\n end headers" + "\n*************************************************\n\n" );

    if ( log.isDebugEnabled() )
    {
      log.debug( h.toString() );
    }

    // try
    // {
    // StringWriter writer = new StringWriter();
    // IOUtils.copy(request.getInputStream(), writer);
    //
    // StringBuilder sb = new StringBuilder();
    // sb.append("\n*\n*************************************************"
    // + "\n request input stream...."
    // + "\n*************************************************\n");
    //
    // sb.append(writer.toString());
    //
    // sb.append("\n*************************************************"
    // + "\n end request input stream"
    // + "\n*************************************************\n\n");
    //
    // System.out.println(sb);
    // }
    // catch(Throwable t)
    // {
    // System.out.println("\n*\nERROR reading request input stream: " + t.getMessage());
    // }

    StringBuilder sb = new StringBuilder();
    sb.append( "\n*\n*************************************************" + "\n parameter names...." + "\n*************************************************" );
    Enumeration<String> params = request.getParameterNames();
    while ( params.hasMoreElements() )
    {
      String param = params.nextElement();
      sb.append( "\n  --> " ).append( param );
    }

    sb.append( "\n*************************************************" + "\n end parameter names" + "\n*************************************************\n\n" );

    if ( log.isDebugEnabled() )
    {
      log.debug( sb );
    }

    super.handleRequest( request );
  }

  @Override
  protected String getRepositoryPath( ModuleConfig mc )
  {
    String path = super.getRepositoryPath( mc ); // To change body of generated methods, choose
                                                 // Tools | Templates.

    System.out.println( "\n*\n***********************************************" + "\nrepository path: " + path + "\n***********************************************\n\n" );

    return path;
  }

}
