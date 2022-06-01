/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/IPAccessFilter.java,v $
 */

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

/**
 * Checks the remote ip and disallows access, based on system variable configurations.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Aug 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class IPAccessFilter implements Filter
{

  private static final Log log = LogFactory.getLog( IPAccessFilter.class );

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    // empty
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
    HttpServletRequest request = (HttpServletRequest)servletRequest;
    HttpServletResponse response = (HttpServletResponse)servletResponse;

    String remoteAddress = request.getHeader( "X-FORWARDED-FOR" );
    if ( remoteAddress == null )
    {
      remoteAddress = request.getRemoteAddr();
    }
    if ( remoteAddress.indexOf( "," ) >= 0 )
    {
      remoteAddress = remoteAddress.substring( 0, remoteAddress.indexOf( "," ) );
    }

    if ( log.isDebugEnabled() )
    {
      log.debug( "IPAccessFilter.doFilter begin " + request.getRequestURL() + " remote address is " + remoteAddress );
    }

    if ( isAllowAccess( remoteAddress ) )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "IPAccessFilter.doFilter continuing down chain " );
      }

      filterChain.doFilter( servletRequest, servletResponse );

      if ( log.isDebugEnabled() )
      {
        log.debug( "IPAccessFilter.doFilter coming back from chain " );
      }
    }
    else
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "IPAccessFilter.doFilter disallowing access " );
      }

      response.sendRedirect( request.getContextPath() + "/errors/serviceUnavailable.html" );
    }

  }

  // determines is the remoteAddress is allowed Access to the application or not. Takes into
  // consideration sytem properties for whether or not we are currently restricting access
  protected boolean isAllowAccess( String remoteAddress )
  {
    if ( IPAddressUtils.isAccessRestricted( SystemVariableService.SHOULD_RESTRICT_ACCESS_BY_IP ) )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Access is restricted" );
      }

      return IPAddressUtils.isAllowAccess( SystemVariableService.IPS_TO_ALLOW_CSV_REGEX, remoteAddress );
    }
    return true;
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // empty
  }

}
