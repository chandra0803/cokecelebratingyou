/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/ClientStateUtils.java,v $
 */

package com.biperf.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClientStateUtils.
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
 * <td>zahler</td>
 * <td>Jan 3, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClientStateUtils
{
  private static final Log logger = LogFactory.getLog( ClientStateUtils.class );

  public static String generateEncodedLink( String siteURLPrefix, String pagePath, Map clientStateParameterMap, boolean doubleEncode )
  {
    return generateEncodedLink( siteURLPrefix, pagePath, clientStateParameterMap, doubleEncode, "clientState" );
  }

  /**
   * @param siteURLPrefix eg. http://localhost:7001/beacon
   * @param pagePath eg. /claim/viewClaim.do
   * @param clientStateParameterMap map of parameters that will be serialized into clientState
   * @param doubleEncode if true will encode the clientState twice to allow it to be used in
   *                        javascript calls. 
   * @return encoded String link
   */
  public static String generateEncodedLink( String siteURLPrefix, String pagePath, Map clientStateParameterMap, boolean doubleEncode, String paramName )
  {
    Map paramMap = null;

    // see if the pagePath already contains a clientState
    int index = pagePath.indexOf( paramName );
    int endIndex = pagePath.length();
    if ( index > 0 )
    {
      endIndex = pagePath.indexOf( "&", index + 12 );
      String temp = pagePath.substring( index + 12, endIndex );
      try
      {
        temp = URLDecoder.decode( temp, "UTF-8" );
        // Password to be used to serialize parameter via ClientState Utilities
        paramMap = ClientStateSerializer.deserialize( temp, ClientStatePasswordManager.getGlobalPassword() );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage(), e );
      }
    }

    if ( paramMap == null )
    {
      paramMap = clientStateParameterMap;
    }
    else
    {
      // iterate over passed params and add to map (which will overwrite existing parameters by the
      // same name
      for ( Iterator iter = clientStateParameterMap.keySet().iterator(); iter.hasNext(); )
      {
        String key = (String)iter.next();
        paramMap.put( key, clientStateParameterMap.get( key ) );
      }
    }
    // Password to be used to serialize parameter via ClientState Utilities
    String clientState = ClientStateSerializer.serialize( paramMap, ClientStatePasswordManager.getGlobalPassword() );

    try
    {
      clientState = URLEncoder.encode( clientState, "UTF-8" );
      if ( doubleEncode )
      {
        clientState = URLEncoder.encode( clientState, "UTF-8" );
      }
    }
    catch( UnsupportedEncodingException e )
    {
      logger.error( e.getMessage(), e );
    }

    // Check to see if there are already parameters on the pagePath
    char paramOperator = '?';
    if ( index > 0 )
    {
      paramOperator = pagePath.charAt( index - 1 );
    }
    else
    {
      if ( pagePath.indexOf( "?" ) > 0 )
      {
        paramOperator = '&';
      }
    }

    if ( index > 0 )
    {
      return siteURLPrefix + pagePath.substring( 0, index - 1 ) + paramOperator + paramName + "=" + clientState + pagePath.substring( endIndex );
    }
    else
    {
      return siteURLPrefix + pagePath + paramOperator + paramName + "=" + clientState + "&cryptoPass=1";
    }
  }

  /**
   * @param siteURLPrefix eg. http://localhost:7001/beacon
   * @param pagePath eg. /claim/viewClaim.do
   * @param clientStateParameterMap map of parameters that will be serialized into clientState
   * @return encoded String link
   */
  public static String generateEncodedLink( String siteURLPrefix, String pagePath, Map clientStateParameterMap )
  {
    return generateEncodedLink( siteURLPrefix, pagePath, clientStateParameterMap, false );
  }

  public static Map getClientStateMap( HttpServletRequest request )
  {
    return getClientStateMap( request, "clientState" );
  }

  public static Map getClientStateMap( HttpServletRequest request, String paramName )
  {
    if ( request.getParameter( paramName ) != null )
    {
      try
      {
        // Deserialize the client state.
        // If cryptoPass = "1", use the password from the crypto classes,
        // otherwise use the password generated on the session.
        return ClientStateSerializer.deserialize( request.getParameter( paramName ),
                                                  request.getParameter( "cryptoPass" ) != null && request.getParameter( "cryptoPass" ).equals( "1" )
                                                      ? ClientStatePasswordManager.getGlobalPassword()
                                                      : ClientStatePasswordManager.getPassword() );
      }
      catch( InvalidClientStateException e )
      {
        // log the error and return empty Map
        logger.error( e.getMessage(), e );
      }
    }
    return new HashMap();
  }

  /**
   * @param clientStateParameterMap map of parameters that will be serialized into clientState
   * @return encoded String clientState
   */
  public static String generateEncodedParamMap( Map clientStateParameterMap )
  {
    try
    {
      // Password to be used to serialize parameter via ClientState Utilities
      return URLEncoder.encode( ClientStateSerializer.serialize( clientStateParameterMap, ClientStatePasswordManager.getGlobalPassword() ), "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      logger.error( e.getMessage(), e );
    }
    return "";
  }

  public static String getParameterValue( HttpServletRequest request, Map clientStateMap, String key )
  {
    if ( StringUtils.isEmpty( request.getParameter( key ) ) )
    {
      if ( clientStateMap.get( key ) instanceof String )
      {
        return (String)clientStateMap.get( key );
      }
      else if ( clientStateMap.get( key ) instanceof Long )
      {
        return ( (Long)clientStateMap.get( key ) ).toString();
      }
    }
    return request.getParameter( key );
  }

  public static Object getParameterValueAsObject( HttpServletRequest request, Map clientStateMap, String key )
  {
    if ( StringUtils.isEmpty( request.getParameter( key ) ) )
    {
      return clientStateMap.get( key );
    }
    return request.getParameter( key );
  }

  /**
   * Get a parameter value from the client state map as a Long object. 
   * @param clientState Parameters
   * @param key Parameter name
   * @return Long value
   */
  public static Long getParameterAsLong( Map<String, Object> clientState, String key )
  {
    if ( clientState.get( key ) instanceof Long )
    {
      return (Long)clientState.get( key );
    }
    else if ( clientState.get( key ) instanceof String )
    {
      return new Long( (String)clientState.get( key ) );
    }
    return null;
  }

}
