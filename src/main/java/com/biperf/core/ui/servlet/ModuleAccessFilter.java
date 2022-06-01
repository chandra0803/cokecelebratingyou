/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/ModuleAccessFilter.java,v $
 */

package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;

/*
 * ModuleAccessFilter <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Dec
 * 22, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

// TODO: Whenever BI adds a new module, add support for the new module to this class.
public class ModuleAccessFilter implements Filter
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The names of the modules whose installation is optional.
   */
  private static final String IDEAS_MODULE = "ideas";
  private static final String NOMINATIONS_MODULE = "nominations";
  private static final String PRODUCT_CLAIM_MODULE = "productclaim";
  private static final String QUIZ_MODULE = "quiz";
  private static final String RECOGNITION_MODULE = "recognition";
  private static final String SIX_SIGMA_MODULE = "sixsigma";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * Maps module names to system variable names.
   */
  private static final Map systemVariableMap = new HashMap();
  static
  {
    systemVariableMap.put( NOMINATIONS_MODULE, SystemVariableService.INSTALL_NOMINATIONS );
    systemVariableMap.put( PRODUCT_CLAIM_MODULE, SystemVariableService.INSTALL_PRODUCTCLAIMS );
    systemVariableMap.put( QUIZ_MODULE, SystemVariableService.INSTALL_QUIZZES );
    systemVariableMap.put( RECOGNITION_MODULE, SystemVariableService.INSTALL_RECOGNITION );
  }

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Called by the container to indicate to a filter that it is being taken out of service.
   */
  public void destroy()
  {
    // nop
  }

  /**
   * When the application receives an HTTP request, this method filters out requests for resources
   * in modules that are not installed.
   * 
   * @param request the servlet request to be processed.
   * @param response the response to the servlet request.
   * @param filterChain used to pass the request/response to the next filter in the chain.
   */
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain ) throws IOException, ServletException
  {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpServletResponse httpResponse = (HttpServletResponse)response;

    RequestWrapper requestWrapper = new RequestWrapper( httpRequest );

    String moduleName = requestWrapper.getModuleName();
    if ( isModuleInstalled( moduleName ) )
    {
      // Pass the request up the filter chain.
      filterChain.doFilter( request, response );
    }
    else
    {
      // Redirect the user to an error page.
      String url = new StringBuffer().append( requestWrapper.getContextPath() ).append( "/errors/moduleNotInstalled.jsp?moduleName=" ).append( moduleName ).toString();
      httpResponse.sendRedirect( url );
    }
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

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the system variable service.
   * 
   * @return a reference to the system variable service.
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  /**
   * Returns true if the specified module is installed; returns false otherwise.
   * 
   * @param moduleName the name of the module whose installation state this method checks.
   * @return true if the specified module is installed; returns false otherwise.
   */
  private boolean isModuleInstalled( String moduleName )
  {
    boolean isModuleInstalled = true;

    String propertyName = (String)systemVariableMap.get( moduleName );
    if ( propertyName != null )
    {
      PropertySetItem propertySetItem = getSystemVariableService().getPropertyByName( propertyName );
      isModuleInstalled = propertySetItem.getBooleanVal();
    }

    return isModuleInstalled;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adds module filter-specific behavior to an <code>HttpServletRequest</code> object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /**
     * The grammar for each of the components of a servlet path for this application, as a regular
     * expression.
     */
    private static final String ACTION_PATH = "\\w+\\.do";
    private static final String MODULE_NAME = "\\w+";
    private static final String SLASH = "/";

    /**
     * The grammar for servlet paths for this application, as a regular expression.
     */
    private static final String SERVLET_PATH_PATTERN = "^(?:" + SLASH + "(" + MODULE_NAME + "))?" + SLASH + ACTION_PATH + "$";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    private static final Log log = LogFactory.getLog( RequestWrapper.class );

    /**
     * A compile pattern that specifies all valid servlet paths for this application.
     */
    private static final Pattern servletPathPattern = Pattern.compile( SERVLET_PATH_PATTERN );

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    /**
     * Constructs a <code>RequestWrapper</code> object.
     * 
     * @param request the HTTP request to be wrapped.
     */
    RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    /**
     * Returns the name of the module whose resource this HTTP request requests. Returns the empty
     * string if the core module contains the requested resource.
     * 
     * @return the name of the module whose resource this HTTP request requests.
     */
    public String getModuleName()
    {
      String moduleName = null;

      Matcher matcher = servletPathPattern.matcher( getServletPath() );
      if ( matcher.matches() )
      {
        moduleName = matcher.group( 1 );
      }
      else
      {
        log.error( "Unrecognized servlet path: unable to get module name." );
      }

      return moduleName;
    }
  }
}
