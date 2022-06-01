/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/AuthProcessingFilterEntryPoint.java,v $
 */

package com.biperf.core.ui.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * Overrides acegi default AuthenticationProcessingFilter to allow for JSON requests which have been denied access
 * to be redirected to a separate login URL than normal posts.
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
 * <td>drahn</td>
 * <td>Oct 24, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
@SuppressWarnings( "deprecation" )
public class AuthProcessingFilterEntryPoint extends LoginUrlAuthenticationEntryPoint
{
  private String ajaxLoginUrl;
  private String ajaxHtmlLoginUrl;

  /**
   * Allows subclasses to modify the login form URL that should be applicable
   * for a given request.
   *
   * @param request the request
   * @param response the response
   * @param exception the exception
   * @return the URL (cannot be null or empty; defaults to
   * {@link #getLoginFormUrl()})
   */
  @Override
  protected String determineUrlToUseForThisRequest( HttpServletRequest request, HttpServletResponse response, AuthenticationException exception )
  {
    String postType = request.getHeader( "post-type" );
    if ( "ajax".equals( postType ) )
    {
      String responseType = request.getParameter( "responseType" );
      if ( "html".equalsIgnoreCase( responseType ) )
      {
        return getAjaxHtmlLoginUrl();
      }
      else
      {
        return getAjaxLoginUrl();
      }
    }
    return getLoginFormUrl();

  }

  /**
   * The redirect URL where the <code>AuthProcessingFilterEntryPoint</code> login
   * for Ajax requests can be found. Should be relative to the web-app context path, and
   * include a leading <code>/</code>
   *
   * @param loginFormUrl
   */
  public void setAjaxLoginUrl( String ajaxLoginUrl )
  {
    this.ajaxLoginUrl = ajaxLoginUrl;
  }

  public String getAjaxLoginUrl()
  {
    return ajaxLoginUrl;
  }

  /**
   * Bug 52848
   * The redirect url for ajax requests with html response.
   * @return
   */
  public String getAjaxHtmlLoginUrl()
  {
    return ajaxHtmlLoginUrl;
  }

  public void setAjaxHtmlLoginUrl( String ajaxHtmlLoginUrl )
  {
    this.ajaxHtmlLoginUrl = ajaxHtmlLoginUrl;
  }

}
