/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/RequestUtils.java,v $
 */

package com.biperf.core.ui.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.tags.TableTagParameters;
import org.springframework.web.util.WebUtils;

import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.utils.AdvancedListPageInfo;
import com.biperf.util.StringUtils;

/**
 * Library of static helper methods useful in Servlet action classes.
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
 * <td>Mar 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RequestUtils extends WebUtils
{
  public static final String ORIGINAL_URI_ATTRIBUTE = "com.biperf.core.ui.original_uri";

  // == URL Tools
  /**
   * Gets an object from the request attributes and returns it. Returns null if it isn't there.
   * 
   * @param request
   * @param paramName
   * @return Object
   */
  public static Object getAttribute( HttpServletRequest request, String paramName )
  {
    return request.getAttribute( paramName );
  }

  /**
   * Gets an object from the request parameter and returns it. Returns null if it isn't there.
   * 
   * @param request
   * @param paramName
   * @return Object
   */
  public static Object getParameter( HttpServletRequest request, String paramName )
  {
    return request.getAttribute( paramName );
  }

  /**
   * @param request
   * @param link
   * @return String
   */
  public static String getResourceUrl( HttpServletRequest request, String link )
  {
    if ( null != link && link.startsWith( "http://" ) )
    {
      return link;
    }
    return getBaseURI( request ) + link;
  }

  /**
   * Build the full request URL, including the query string.
   * 
   * @param request
   * @return String
   */
  public static String getRequestUrl( HttpServletRequest request )
  {

    // Servlet API 2.3 version
    StringBuffer reqUrl = request.getRequestURL();

    if ( request.getQueryString() != null )
    {
      reqUrl.append( "?" ).append( request.getQueryString() );
    }

    return reqUrl.toString();

  } // getRequestUrl

  /**
   * Returns the original URI - saved by the ParameterFilter
   * 
   * @param request
   * @return String saved as a request attribute
   */
  public static String getOriginalRequestURI( HttpServletRequest request )
  {
    return (String)request.getAttribute( ORIGINAL_URI_ATTRIBUTE );
  }

  /**
   * Returns the original URI - saved by the ParameterFilter
   * 
   * @param request
   * @return String saved as a request attribute
   */
  public static String getOriginalRequestURIWithQueryString( HttpServletRequest request )
  {
    StringBuffer requestURI = new StringBuffer();
    requestURI.append( (String)request.getAttribute( ORIGINAL_URI_ATTRIBUTE ) );
    if ( request.getQueryString() != null )
    {
      requestURI.append( "?" ).append( request.getQueryString() );
    }
    return requestURI.toString();
  }

  /**
   * Attempt to get a request parameter as a String value.
   * 
   * @param request
   * @param name
   * @return String
   * @throws IllegalArgumentException
   */
  public static String getRequiredParamString( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );

    if ( param == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    return param;

  } // getParamString

  /**
   * Attempt to get a request parameter and return it as an object.
   * 
   * @param request
   * @param name
   * @return String
   * @throws IllegalArgumentException
   */
  public static Object getRequiredParam( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    Object object = request.getParameter( name );

    if ( object == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    return object;

  } // getRequiredParam

  /**
   * Attempt to get a required request attribute and return it as an object.
   * 
   * @param request
   * @param name
   * @return String
   * @throws IllegalArgumentException
   */
  public static Object getRequiredAttribute( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    Object object = request.getAttribute( name );

    if ( object == null )
    {
      throw new IllegalArgumentException( "request attribute " + name + " was missing" );
    }

    return object;

  } // getRequiredAttribute

  /**
   * Attempt to get a required request attribute as a long.
   * 
   * @param request
   * @param name
   * @return String
   * @throws IllegalArgumentException
   */
  public static Long getRequiredAttributeLong( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    Long longAttribute = (Long)request.getAttribute( name );

    if ( longAttribute == null )
    {
      throw new IllegalArgumentException( "request attribute " + name + " was missing" );
    }

    return longAttribute;

  } // getRequiredAttributeLong

  /**
   * Attempt to get a optional request attribute as a long.
   * 
   * @param request
   * @param name
   * @return String
   * @throws IllegalArgumentException
   */
  public static Long getOptionalAttributeLong( HttpServletRequest request, String name )
  {

    Long longAttribute = new Long( 0 );

    try
    {
      longAttribute = RequestUtils.getRequiredAttributeLong( request, name );
    }
    catch( IllegalArgumentException iae )
    {
      // Do nothing as this is an optional param
    }

    return longAttribute;

  } // getRequiredAttributeLong

  /**
   * Determines if the request contains a paramater with the given name.
   * 
   * @param request
   * @param name
   * @return boolean
   * @throws IllegalArgumentException
   */
  public static boolean containsParam( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    boolean containsParam = true;

    if ( request.getParameter( name ) == null )
    {
      containsParam = false;
    }

    return containsParam;

  } // containsParam

  /**
   * Determines if the request contains an attribute with the given name.
   * 
   * @param request
   * @param name
   * @return boolean
   * @throws IllegalArgumentException
   */
  public static boolean containsAttribute( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    boolean containsAttribute = true;

    if ( request.getAttribute( name ) == null )
    {
      containsAttribute = false;
    }

    return containsAttribute;

  } // containsParam

  /**
   * Checks if the request contains a submit parameter.
   * 
   * @param request
   * @param name
   * @return boolean
   * @throws IllegalArgumentException
   */
  public static boolean containsSubmitParam( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    return RequestUtils.containsParam( request, name ) || RequestUtils.containsParam( request, name + ".x" ); // image
                                                                                                              // submits

  } // containsSubmitAction

  /**
   * Get a request parameter as a String value.
   * 
   * @param request
   * @param name
   * @return String
   */
  public static String getOptionalParamString( HttpServletRequest request, String name )
  {

    String param = ""; // default

    try
    {
      param = getRequiredParamString( request, name );
    }
    catch( IllegalArgumentException e )
    {
      // Do nothing as the parameter isn't required.
    }

    return param;
  } // getOptionalParamString

  /**
   * Attempt to get a request parameter as an int value.
   * 
   * @param request
   * @param name
   * @return int
   * @throws IllegalArgumentException
   */
  public static int getRequiredParamInt( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );

    if ( param == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    try
    {
      return Integer.parseInt( param );
    }
    catch( NumberFormatException e )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was not an int" );
    }

  } // getRequiredParamInt

  /**
   * Get a request parameter as an int value. Default is zero.
   * 
   * @param request
   * @param name
   * @return int
   */
  public static int getOptionalParamInt( HttpServletRequest request, String name )
  {

    int param = 0; // default

    try
    {
      param = getRequiredParamInt( request, name );
    }
    catch( IllegalArgumentException e )
    {
      // Do nothing as the parameter isn't required.
    }

    return param;

  } // getParamInt

  /**
   * Attempt to get a request parameter as a long value.
   * 
   * @param request
   * @param name
   * @return long
   * @throws IllegalArgumentException
   */
  public static long getRequiredParamLong( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );

    if ( param == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    try
    {
      return Long.parseLong( param );
    }
    catch( NumberFormatException e )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was not an long" );
    }

  } // getRequiredParamLong

  /**
   * Attempt to get a request parameter as a Long value.
   * 
   * @param request
   * @param name
   * @return long
   * @throws IllegalArgumentException
   */
  // public static Long getRequiredParamLongClass( HttpServletRequest request, String name ) throws
  // IllegalArgumentException
  // {
  //
  // String param = request.getParameter( name );
  //
  // if ( param == null )
  // throw new IllegalArgumentException( "request parameter " + name + " was missing" );
  //
  // try
  // {
  // return new Long( param );
  // }
  // catch( NumberFormatException e )
  // {
  // throw new IllegalArgumentException( "request parameter " + name + " was not an long" );
  // }
  //
  // } // getRequiredParamLong

  /**
   * Get a request parameter as an long value. Default is zero.
   * 
   * @param request
   * @param name
   * @return long
   */
  public static long getOptionalParamLong( HttpServletRequest request, String name )
  {

    long param = 0; // default

    try
    {
      param = getRequiredParamLong( request, name );
    }
    catch( IllegalArgumentException e )
    {
      // Do nothing as the parameter isn't required.
    }

    return param;
  } // getOptionalParamLong

  /**
   * Get a request parameter as an Long value. Default is null.
   * 
   * @param request
   * @param name
   * @return long
   */
  // public static Long getOptionalParamLongClass( HttpServletRequest request, String name )
  // {
  //
  // Long param = null; // default
  //
  // try
  // {
  // param = getRequiredParamLongClass( request, name );
  // }
  // catch( IllegalArgumentException e )
  // {
  // // Do nothing as the parameter isn't required.
  // }
  //
  // return param;
  // } // getOptionalParamLong

  /**
   * Attempt to get a request parameter as a double value.
   * 
   * @param request
   * @param name
   * @return double
   * @throws IllegalArgumentException
   */
  public static double getRequiredParamDouble( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );

    if ( param == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    try
    {
      return Double.parseDouble( param );
    }
    catch( NumberFormatException e )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was not an long" );
    }

  } // getRequiredParamLong

  /**
   * Get a request parameter as a double value. Default is zero.
   * 
   * @param request
   * @param name
   * @return double
   */
  public static double getOptionalParamDouble( HttpServletRequest request, String name )
  {

    double param = 0; // default

    try
    {
      param = getRequiredParamDouble( request, name );
    }
    catch( IllegalArgumentException e )
    {
      // Do nothing as the parameter isn't required.
    }

    return param;
  } // getOptionalParamLong

  /**
   * Attempt to get a request parameter as a date value in mm/dd/yyyy format.
   * 
   * @param request
   * @param name
   * @return Date
   * @throws IllegalArgumentException
   */
  public static Date getParamDate( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );

    if ( param == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    try
    {
      DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT );
      return df.parse( param );
    }
    catch( ParseException e )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was not a Date in the default locale - " + e.getMessage() );
    }

  } // getParamDate

  /**
   * Gets an array of values from the paramMap for an optionalValue;
   * 
   * @param request
   * @param name
   * @return String[]
   */
  public static String[] getOptionalParamArray( HttpServletRequest request, String name )
  {

    String[] params = new String[] {};

    try
    {
      params = RequestUtils.getRequiredParamArray( request, name );
    }
    catch( IllegalArgumentException e )
    {
      // Do nothing, it isn't required the values be present.
    }

    return params;
  }

  /**
   * Gets an array of values from the paramMap.
   * 
   * @param request
   * @param name
   * @return String[]
   * @throws IllegalArgumentException
   */
  public static String[] getRequiredParamArray( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String[] params = request.getParameterValues( name );

    if ( params == null )
    {
      throw new IllegalArgumentException( "request parameter " + name + " was missing" );
    }

    return params;

  } // End getParamArray

  /**
   * Creates a Map populated with single value name=value pairs from the request being passed in
   * with the parameter names being used as the hash key.
   * 
   * @param request
   * @return Map
   */
  public static Map getParamMap( HttpServletRequest request )
  {
    String paramName;
    Map paramMap = new HashMap();
    Enumeration names = request.getParameterNames();

    while ( names.hasMoreElements() )
    {
      paramName = (String)names.nextElement();
      paramMap.put( paramName, RequestUtils.getRequiredParamString( request, paramName ) );
    } // while

    return paramMap;
  } // End getParamMap

  /**
   * Creates a hashMap populated with name=value[] pairs from the request being passed in with the
   * parameter names being used as the hash key.
   * 
   * @param request
   * @return Hashmap
   */
  public static Map getMultipleParamMap( HttpServletRequest request )
  {

    // get names of parameters
    Enumeration names = request.getParameterNames();

    // instantiate map and placeholders
    Map paramMap = new HashMap();
    String currentName = null;
    String[] allValues = null;

    // Build the map
    while ( names.hasMoreElements() )
    {

      currentName = (String)names.nextElement();
      allValues = request.getParameterValues( currentName );
      paramMap.put( currentName, allValues );

    } // End while

    return paramMap;

  } // End getParamMap

  /**
   * Attempts to access a request parameter with a given name and convert its to an object of class
   * Boolean. If the value of the parameter is not null and, ignoring case, is equal to "true", will
   * return a Boolean "TRUE", otherwise returns "FALSE".
   * 
   * @param request
   * @param name
   * @return Boolean
   * @throws IllegalArgumentException
   */
  public static Boolean getParamBoolean( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );
    if ( param == null )
    {
      throw new IllegalArgumentException( "ServletUtil.getParamBoolean: Request parameter " + name + " was missing" );
    }

    return Boolean.valueOf( param );

  } // End getParamBoolean()

  /**
   * Attempts to access a request parameter with a given name and convert its to an object of class
   * Boolean. If the value of the parameter is not null and, ignoring case, is equal to "true", will
   * return a Boolean "TRUE", otherwise returns "FALSE".
   * 
   * @param request
   * @param name
   * @return Boolean
   * @throws IllegalArgumentException
   */
  public static Boolean getOptionalParamBoolean( HttpServletRequest request, String name ) throws IllegalArgumentException
  {

    String param = request.getParameter( name );

    if ( param == null )
    {
      return Boolean.valueOf( false );
    }

    return Boolean.valueOf( param );
  } // End getParamBoolean()

  /**
   * Get the base URI for the application
   * 
   * @param pRequest
   * @return String
   */
  public static String getBaseURI( HttpServletRequest pRequest )
  {
    return pRequest.getContextPath();
  }

  /**
   * Get a required attribute from the request as a String.
   * 
   * @param request
   * @param string
   * @return String
   */
  public static String getRequiredAttributeString( HttpServletRequest request, String string )
  {

    String stringAttribute = (String)request.getAttribute( string );

    if ( null == stringAttribute || "".equals( stringAttribute ) )
    {
      throw new IllegalArgumentException( "request attribute " + string + " was missing" );
    }

    return stringAttribute;
  }

  public static void setAttribute( HttpServletRequest request, String string, Object object )
  {
    request.setAttribute( string, object );
  }

  /**
   * Returns the original URI - saved by the ParameterFilter
   * 
   * @param request
   * @return String saved as a request attribute
   */
  public static String getActionMappingRequestURI( HttpServletRequest request )
  {
    String actionMapping = (String)request.getAttribute( ORIGINAL_URI_ATTRIBUTE );
    if ( !StringUtils.isEmpty( actionMapping ) )
    {
      actionMapping = actionMapping.substring( actionMapping.lastIndexOf( "/" ) + 1, actionMapping.length() );
    }
    return actionMapping;
  }

  /**
   * Returns the protocol associated with the request URL
   * 
   * @param request
   * @return String protocol associated with the request URL, default is 'http'
   */
  public static String getProtocol( HttpServletRequest request )
  {
    String protocol = "http";

    try
    {
      if ( null != request && null != request.getRequestURL() )
      {
        URL url = new URL( request.getRequestURL().toString() );
        protocol = url.getProtocol();
      }
    }
    catch( MalformedURLException e )
    {
      // return the default 'http' protocol
    }

    return protocol;
  }

  public static String getHomePageFilterToken( HttpServletRequest request )
  {
    String filter = (String)request.getSession().getAttribute( "filterName" );
    if ( filter == null || filter.trim().length() == 0 )
    {
      return "";
    }
    return "#launch/" + filter.trim();
  }

  public static String getFilterToken( HttpServletRequest request, String code )
  {
    String filterPageName = FilterSetupType.lookup( code ).getCode().trim();
    request.getSession().setAttribute( "filterName", filterPageName );

    return "#launch/" + filterPageName;
  }

  /**
   * Helper for displaytag table paging/sorting. Translate displaytag parameters on the request into
   * a ListPageInfo object.
   *
   * @param request
   * @return
   */
  public static AdvancedListPageInfo getListPageInfo( HttpServletRequest request )
  {
    AdvancedListPageInfo pageInfo = new AdvancedListPageInfo();
    String par;

    par = request.getParameter( AdvancedListPageInfo.PARAM_PAGE_NUMBER );
    pageInfo.setPageNumber( par != null ? Integer.parseInt( par ) : 1 );

    par = request.getParameter( "dir" );
    pageInfo.setSortAscending( !"desc".equals( par ) );

    par = request.getParameter( "sort" );
    pageInfo.setSortCriterion( par );

    // when exporting, always fetch all records
    par = request.getParameter( TableTagParameters.PARAMETER_EXPORTING );
    pageInfo.setExporting( par != null );
    if ( pageInfo.isExporting() )
    {
      pageInfo.setPageNumber( 1 );
      pageInfo.setObjectsPerPage( Integer.MAX_VALUE );
    }

    return pageInfo;
  }

  @SuppressWarnings( "unchecked" )
  public static <T> AdvancedListPageInfo<T> getListPageInfo( HttpServletRequest request, Class<T> type )
  {
    return getListPageInfo( request );
  }

} // class RequestUtil
