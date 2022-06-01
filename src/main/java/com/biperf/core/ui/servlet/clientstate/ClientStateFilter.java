/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/servlet/clientstate/ClientStateFilter.java,v $
 */

package com.biperf.core.ui.servlet.clientstate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.biperf.core.utils.ClientStateConstants;
import com.biperf.core.utils.ClientStatePasswordManager;

/**
 * ClientStateFilter
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
 * <td>Thomas Eaton</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class ClientStateFilter implements Filter
{
  /**
   * Called by the container to indicate to a filter that it is being taken out of service.
   */
  public void destroy()
  {
    // nop
  }

  /**
   * When the application receives an HTTP request, this method copies the client state password
   * from HTTP-session scope into resource-manager scope.
   * 
   * @param request the servlet request to be processed.
   * @param response the response to the servlet request.
   * @param filterChain used to pass the request/response to the next filter in the chain.
   */
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain ) throws IOException, ServletException
  {
    HttpSession httpSession = ( (HttpServletRequest)request ).getSession();

    // Copy the client state password from HTTP-session scope to resource-manager scope.
    String password = (String)httpSession.getAttribute( ClientStateConstants.CLIENT_STATE_PASSWORD_KEY );
    ClientStatePasswordManager.setPassword( password );

    // Pass the request up the filter chain.
    filterChain.doFilter( request, response );
  }

  /**
   * Called by the container to indicate to a filter that it is being placed into service.
   * 
   * @param filterConfig used to pass information to a filter during initialization.
   */
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    // nop
  }
}
