
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpAllowMethodsFilter implements Filter
{
  public void init( FilterConfig filterConfig ) throws ServletException
  {
  }

  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {
    HttpServletRequest httpServletRequest = (HttpServletRequest)request;

    request.setCharacterEncoding( "utf-8" );
    response.setCharacterEncoding( "utf-8" );
    if ( isAllowedMethod( httpServletRequest ) )
    {
      chain.doFilter( request, response );
    }
    else
    {
      HttpServletResponse httpResponse = (HttpServletResponse)response;
      httpResponse.setStatus( HttpServletResponse.SC_METHOD_NOT_ALLOWED );
    }
  }

  public void destroy()
  {
  }

  private boolean isAllowedMethod( HttpServletRequest request )
  {
    if ( request.getHeader( "X-HTTP-Method" ) != null || request.getHeader( "X-Method-Override" ) != null || request.getHeader( "X-HTTP-Method-Override" ) != null )
    {
      return false;
    }

    else if ( request.getParameter( "_method" ) != null )
    {
      String methodParam = request.getParameter( "_method" );

      if ( methodParam.equalsIgnoreCase( "put" ) || methodParam.equalsIgnoreCase( "delete" ) || methodParam.equalsIgnoreCase( "trace" ) || methodParam.equalsIgnoreCase( "connect" )
          || methodParam.equalsIgnoreCase( "head" ) )
      {
        return false;
      }
    }
    return "GET".equals( request.getMethod() ) || "POST".equals( request.getMethod() );
  }
}