
package com.biperf.core.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.IPAddressUtils;

/**
 * 
 * NotAuthTimeoutRedirectFilter.
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
 * <td>robinsra</td>
 * <td>June 30, 2016</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>

 */
public class NotAuthTimeoutRedirectFilter implements Filter
{
  public static final String LOGIN = "login";
  public static final String STATIC_PAGE = "static";

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
    String remoteAddress = request.getRemoteAddr();
    String environment = Environment.getEnvironment();
    String redirectUrl = "";
    if ( IPAddressUtils.isAllowAccess( SystemVariableService.IPS_TO_ALLOW_CSV_REGEX, remoteAddress ) || Environment.ENV_DEV.equalsIgnoreCase( environment ) )
    {
      // inside BI
      redirectUrl = getRedirectUrl( SystemVariableService.NOT_AUTH_TIMEOUT_REDIRECT_INTERNAL_URL );
    }
    else
    {
      // outside BI
      redirectUrl = getRedirectUrl( SystemVariableService.NOT_AUTH_TIMEOUT_REDIRECT_URL );
    }
    HttpServletResponse response = (HttpServletResponse)servletResponse;
    response.sendRedirect( response.encodeRedirectURL( redirectUrl ) );
  }

  /**
   * getRedirectUrl
   * @param sysVarName
   * @return String redirectUrl
   */
  private String getRedirectUrl( String sysVarName )
  {
    String redirectUrl = "";
    String redirectTo = getSystemVariableService().getPropertyByName( sysVarName ).getStringVal();
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    if ( LOGIN.equalsIgnoreCase( redirectTo ) )
    {
      redirectUrl = siteUrlPrefix + "/login.do";
    }
    else if ( STATIC_PAGE.equalsIgnoreCase( redirectTo ) )
    {
      redirectUrl = siteUrlPrefix + "/notAuthTimeout.do";
    }
    else if ( redirectTo.startsWith( "http" ) )
    {
      redirectUrl = redirectTo;
    }
    else
    {
      redirectUrl = siteUrlPrefix + "/login.do";
    }

    return redirectUrl;
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // empty
  }

}
