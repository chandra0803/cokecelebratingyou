
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;

import com.biperf.core.utils.ResourceManager;

public class TrimmingFilter implements Filter
{
  private static final Log logger = LogFactory.getLog( TrimmingFilter.class );

  // private FilterConfig filterConfig;

  public void init( FilterConfig filterConfig ) throws ServletException
  {
    logger.debug( "Filter initialized" );
    // this.filterConfig = filterConfig;
  }

  public void destroy()
  {
    logger.debug( "Filter destroyed" );
    // this.filterConfig = null;
  }

  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {

    String contentType = request.getContentType();
    String uri = ( (HttpServletRequest)request ).getRequestURI();
    try
    {
      if ( request.getParameter( "cryptoPass" ) != null )
      {
        ResourceManager.setResource( "cryptoPass", "1" );
      }

      if ( ( contentType != null && contentType.contains( MediaType.APPLICATION_JSON_VALUE ) ) || uri.contains( "jazzySpellCheck.do" ) )
      {
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper( (HttpServletRequest)request, true );
        String body = StripXSSUtil.stripXSS( IOUtils.toString( wrappedRequest.getReader() ) );
        wrappedRequest.resetInputStream( body.getBytes() );

        chain.doFilter( wrappedRequest, response );
      }
      else
      {
        chain.doFilter( new HttpServletRequestWrapper( (HttpServletRequest)request, false ), response );
      }
    }
    finally
    {
      ResourceManager.removeResource( "cryptoPass" );
    }
  }
}
