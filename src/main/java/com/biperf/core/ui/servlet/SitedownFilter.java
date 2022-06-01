/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/SitedownFilter.java,v $
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

import com.biperf.core.exception.BeaconConnectionException;
import com.biperf.core.utils.UserManager;

/**
 * Prepares ThreadLocal with the required information for processing.
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
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class SitedownFilter implements Filter
{

  private static final Log log = LogFactory.getLog( SitedownFilter.class );

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    // empty
  }

  /**
   * Add the necessary paramenters for use in the Service/DAO layer.
   *
   * @param request ServletRequest
   * @param response ServletResponse
   * @param filterChain FilterChain
   * @throws IOException
   * @throws ServletException
   */
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain ) throws IOException, ServletException
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "com.biperf.core.ui.servlet.SitedownFilter  doFilter start" );
    }

    HttpServletResponse httpResponse = (HttpServletResponse)response;
    HttpServletRequest httpRequest = (HttpServletRequest)request;

    try
    {
      filterChain.doFilter( request, response );
    }
    catch( BeaconConnectionException bce )
    {
      httpResponse.sendRedirect( httpRequest.getContextPath() + "/sitedown.html" );
    }
    finally
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "com.biperf.core.ui.servlet.SitedownFilter clean up code" );
      }
      // we need to do this always...
      UserManager.removeUser();
    }
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // empty
  }

}
