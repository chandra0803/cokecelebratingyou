
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class RosterResponseFilter implements Filter
{

  public void init( FilterConfig filterConfig ) throws ServletException
  {
  }

  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {
    HttpServletResponse httpResponse = (HttpServletResponse)response;
    RosterResponseWrapper responseWrapper = new RosterResponseWrapper( httpResponse );
    chain.doFilter( request, responseWrapper );
  }

  public void destroy()
  {
  }

}