
package com.biperf.core.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil
{
  /**
   * Get the base URI for the application, e.g. "http://domain.com/rave"
   * 
   * @param request
   * @return StringBuffer
   */
  public static StringBuffer getBaseURI( HttpServletRequest request )
  {
    String contextPath = request.getContextPath();
    StringBuffer baseURI = request.getRequestURL();
    int pos = baseURI.indexOf( contextPath );
    if ( pos >= 0 )
    {
      baseURI.delete( pos + contextPath.length(), baseURI.length() );
    }

    return baseURI;
  }

  public static String getEnvironmentAgnosticHostFrom( HttpServletRequest request )
  {
    return getEnvironmentAgnosticHostFrom( request.getRequestURL().toString() );
  }

  /**
   * Return the host of the given request, removing any environment tokens such
   * as "qa" or "pprd."  For example, using "http://clientnameqa.rave.com/rave"
   * should return "clientname.rave.com" .
   * @param request
   * @return
   */
  public static String getEnvironmentAgnosticHostFrom( String requestUrl )
  {
    String host = "";
    try
    {
      URL url = new URL( requestUrl );
      host = url.getHost();
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( "Unable to determine host in RequestUtils#getHostFrom" );
    }

    // remove any qa or pprd tokens from the subdomain.
    // for example, clientnameqa.rave.com should be clientname.rave.com
    String[] tokens = host.split( "\\." );

    if ( tokens != null && tokens.length > 1 )
    {
      // it's in the form clientname.somedomain.com, so remove qa and pprd
      String subdomain = tokens[0];
      if ( subdomain.endsWith( "qa" ) )
      {
        subdomain = subdomain.replaceAll( "qa", "" );
      }
      else if ( subdomain.endsWith( "pprd" ) )
      {
        subdomain = subdomain.replaceAll( "pprd", "" );
      }

      // reconstruct the host from the subdomain and the tokens
      host = subdomain.trim();
      for ( int i = 1; i < tokens.length; i++ )
      {
        host = host + "." + tokens[i];
      }
    }

    return host;
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
  }

  public static boolean isTaconiteRequest( HttpServletRequest request )
  {
    Map params = request.getParameterMap();
    if ( params != null && params.containsKey( "ts" ) )
    {
      return true;
    }
    return false;
  }

  public static boolean isSecureRequest( HttpServletRequest request )
  {
    return request.isSecure();
  }

  public static final String getWebappRootUrl( HttpServletRequest request )
  {
    String port = request.getServerPort() == 443 || request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
    return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
  }

  public static List<String> getPreferredLanguageListFrom( HttpServletRequest request )
  {
    String headerValue = request.getHeader( "Accept-Language" );

    List<String> languageList = new ArrayList<String>();

    if ( headerValue == null || headerValue.trim().length() == 0 )
    {
      return languageList;
    }

    // lanuageList is in the form of: da, en-gb;q=0.8, en;q=0.7
    // first split by comma
    String[] langs = headerValue.split( "," );

    // for each token, remove everything from the semi-colon and after
    for ( int i = 0; i < langs.length; i++ )
    {
      String token = langs[i];

      // first remove the weighting (q value) if it exists
      if ( token.indexOf( ";" ) > 0 )
      {
        token = token.substring( 0, token.indexOf( ";" ) );
      }

      // remove the language variant ("gb" in en-gb) if it exists
      if ( token.indexOf( "-" ) > 0 )
      {
        token = token.substring( 0, token.indexOf( "-" ) );
      }

      languageList.add( token.trim() );
    }

    return languageList;
  }
}
