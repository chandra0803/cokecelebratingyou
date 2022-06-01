/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/servlet/LoggingFilter.java,v $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

/**
 * LoggingFilter.
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
 * <td>kumars</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoggingFilter implements Filter
{

  private static final Log LOG = LogFactory.getLog( LoggingFilter.class );
  private static final String MDC_KEY = "MDC_KEY";

  /**
   * Inspects session for a consultant object. If present, grabs consultant id and the first 10
   * characters of the session id and places the concatonated string in MDC (see log4j javadoc). If
   * consultant not present in session, creates a new session, grabs first 10 digits of session if
   * and apends 10 zeros to it and places this in MDC.
   * 
   * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest,
   *      javax.servlet.ServletResponse) Overridden from
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
   *      javax.servlet.FilterChain)
   * @param request
   * @param response
   * @param chain
   * @throws IOException
   * @throws ServletException
   */
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {

    LOG.debug( ">>>doFilter" );

    String keySuffix = "0000000000"; // value only used if consultant is not in session (first time
    // to site)
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    StringBuffer message = new StringBuffer( httpRequest.getSession( true ).getId().substring( 0, 10 ) );

    message.append( " " );
    message.append( keySuffix );

    MDC.put( MDC_KEY, message );
    chain.doFilter( request, response );
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy()
  {
    // nop
  }

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init( FilterConfig config ) throws ServletException
  {
    // nop
  }

}
