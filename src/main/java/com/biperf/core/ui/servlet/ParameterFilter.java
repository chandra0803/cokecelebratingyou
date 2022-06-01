/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/ParameterFilter.java,v $
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
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.IPAddressUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

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
 *
 */
public class ParameterFilter implements Filter
{

  private static final Log log = LogFactory.getLog( ParameterFilter.class );
  private SystemVariableService systemVariableService = null;

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
   * @param servletRequest ServletRequest
   * @param servletResponse ServletResponse
   * @param filterChain FilterChain
   * @throws IOException
   * @throws ServletException
   */
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "com.biperf.core.ui.servlet.ParameterFilter  doFilter start" );
    }
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // MD5Hash.setDefaultUpperCase(!getSystemVariableService().getPropertyByName(SystemVariableService.PASSWORD_SHOULD_USE_REGEX).getBooleanVal());
    SHA256Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    Monitor monitor = null;
    try
    {
      HttpServletRequest request = (HttpServletRequest)servletRequest;
      HttpSession httpSession = request.getSession( true );
      HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;

      String remoteAddress = request.getHeader( "X-FORWARDED-FOR" );
      if ( remoteAddress == null )
      {
        remoteAddress = request.getRemoteAddr();
      }
      if ( remoteAddress.indexOf( "," ) >= 0 )
      {
        remoteAddress = remoteAddress.substring( 0, remoteAddress.indexOf( "," ) );
      }

      // Save the original URL for displaytag
      request.setAttribute( RequestUtils.ORIGINAL_URI_ATTRIBUTE, request.getRequestURI() );

      // Start a monitor for the page
      monitor = MonitorFactory.start( "page " + request.getRequestURI() );

      // Get the username from the Acegi security context and set it on thread local
      SecurityContext acegiContext = (SecurityContext)httpSession.getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
      if ( acegiContext != null )
      {
        AuthenticatedUser user = null;
        Authentication auth = acegiContext.getAuthentication();
        Object obj = null;
        if ( auth != null )
        {
          obj = auth.getPrincipal();
        }
        if ( obj != null )
        {
          if ( obj instanceof AuthenticatedUser )
          {
            user = (AuthenticatedUser)obj;
            UserManager.setUser( user );
          }
        }
      }

      if ( !Environment.getEnvironment().equals( Environment.ENV_DEV ) && UserManager.getUser() != null
          && UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) )
          && !IPAddressUtils.isAllowAccess( SystemVariableService.IPS_TO_ALLOW_CSV_REGEX, remoteAddress ) )
      {
        httpResponse.sendRedirect( request.getContextPath() + "/errors/serviceUnavailable.html" );
      }
      if ( log.isDebugEnabled() )
      {
        log.debug( "com.biperf.core.ui.servlet.ParameterFilter continuing down filter chain" );
      }
      filterChain.doFilter( servletRequest, servletResponse );
      if ( log.isDebugEnabled() )
      {
        log.debug( "com.biperf.core.ui.servlet.ParameterFilter coming back from filter chain" );
      }
    }
    finally
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "com.biperf.core.ui.servlet.ParameterFilter clean up code" );
      }
      if ( monitor != null )
      {
        monitor.stop();
      }
      // we need to do this always...
      UserManager.removeUser();
      /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      // MD5Hash.removeDefaultUpperCase();
      SHA256Hash.removeDefaultUpperCase();
      /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    }
  }

  private SystemVariableService getSystemVariableService()
  {
    if ( systemVariableService == null )
    {
      systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // empty
  }

}
