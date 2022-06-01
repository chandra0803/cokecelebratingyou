
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.IPAddressUtils;

public class SSOTestFilter implements Filter
{

  private static final Log logger = LogFactory.getLog( SSOTestFilter.class );

  public void init( FilterConfig filterConfig ) throws ServletException
  {

  }

  /**
   * @param servletRequest ServletRequest
   * @param servletResponse ServletResponse
   * @param filterChain FilterChain
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   */
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

    if ( IPAddressUtils.isAllowAccess( SystemVariableService.ADMIN_IP_RESTRICTIONS, getRemoteAddress( httpServletRequest ) ) )
    {
      logger.debug( "SSOTestFilter - Access Allowed - continuing down chain " );
      filterChain.doFilter( servletRequest, servletResponse );
      logger.debug( "SSOTestFilter - coming back from chain " );
    }
    else
    {
      logger.debug( "SSOTestFilter - Access not allowed for IPAddress:" + getRemoteAddress( httpServletRequest ) );
      httpServletResponse.sendRedirect( httpServletRequest.getContextPath() + "/errors/accessDenied.html" );
    }
  }

  /**
   * getRemoteAddress
   * @param httpServletRequest
   * @return String - remoteAddress
   */
  private String getRemoteAddress( HttpServletRequest httpServletRequest )
  {
    String remoteAddress = httpServletRequest.getHeader( "X-FORWARDED-FOR" );
    if ( remoteAddress == null )
    {
      remoteAddress = httpServletRequest.getRemoteAddr();
    }
    if ( remoteAddress.indexOf( "," ) >= 0 )
    {
      remoteAddress = remoteAddress.substring( 0, remoteAddress.indexOf( "," ) );
    }
    logger.debug( "Remote address:" + remoteAddress + ", Request url:" + httpServletRequest.getRequestURL() );
    return remoteAddress;
  }

  public void destroy()
  {

  }

}