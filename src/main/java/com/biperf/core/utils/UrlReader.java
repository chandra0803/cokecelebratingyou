
package com.biperf.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class UrlReader
{
  private final Logger log = Logger.getLogger( UrlReader.class );

  protected void configureProxy( HttpClient client )
  {
    if ( !Environment.buildProxy().equals( Proxy.NO_PROXY ) ) // some envs may not use a proxy, AWS
                                                              // for example
    {
      ProxyHost proxy = new ProxyHost( Environment.getProxy(), Environment.getProxyPort() );
      client.getHostConfiguration().setProxyHost( proxy );
    }
  }

  protected String getProtocol( String url )
  {
    String protocol = null;
    URL urlObject = null;
    try
    {
      urlObject = new URL( url );
      protocol = urlObject.getProtocol().trim().toLowerCase();
    }
    catch( MalformedURLException e )
    {
      log.error( "\n\n********\nERROR in BaseUrlReader.getProtocol -- MalformedURLException: " + url + "\n\n*******" );
      throw new RuntimeException( "MalformedURLException: " + url );
    }

    return protocol;
  }

  public boolean useProxy( String url )
  {
    // figure out the proxy stuff
    String protocol = getProtocol( url );
    boolean useProxy = true;
    if ( protocol.equalsIgnoreCase( "http" ) || protocol.equalsIgnoreCase( "https" ) )
    {
      String host = RequestUtil.getEnvironmentAgnosticHostFrom( url );
      if ( host.toLowerCase().indexOf( "localhost" ) > -1 )
      {
        // if localhost, don't use proxy
        useProxy = false;
      }
    }
    return useProxy;
  }

  protected HttpClient createHttpClient( String url )
  {
    HttpClient client = new HttpClient();
    if ( useProxy( url ) )
    {
      configureProxy( client );
    }

    return client;
  }

  protected GetMethod executeGetMethod( String url, AuthenticationCredentials... authenticationCredentials ) throws Exception
  {
    HttpClient client = createHttpClient( url );
    GetMethod method = new GetMethod( url );
    return executeMethod( client, method );
  }

  protected GetMethod executeGetMethod( String url, NameValuePair[] pairs ) throws Exception
  {
    HttpClient client = createHttpClient( url );
    GetMethod method = new GetMethod( url );
    method.setQueryString( pairs );
    return executeMethod( client, method );
  }

  protected PostMethod executePostMethod( String url, Map<String, String> params ) throws Exception
  {
    HttpClient client = createHttpClient( url );
    PostMethod method = new PostMethod( url );
    method.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
    if ( params != null )
    {
      for ( String key : params.keySet() )
      {
        method.addParameter( key, params.get( key ) );
      }
    }
    return executeMethod( client, method );
  }

  protected PostMethod executePostMethod( String url, Map<String, String> params, String oauthToken ) throws Exception
  {
    HttpClient client = createHttpClient( url );
    PostMethod method = new PostMethod( url );
    method.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
    method.setRequestHeader( "Authorization", "OAuth " + URLDecoder.decode( oauthToken, "utf-8" ) );
    if ( params != null )
    {
      for ( String key : params.keySet() )
      {
        method.addParameter( key, params.get( key ) );
      }
    }
    return executeMethod( client, method );
  }

  protected <T extends HttpMethod> T executeMethod( HttpClient client, T method, AuthenticationCredentials... authenticationCredentials ) throws Exception
  {
    // auth
    if ( authenticationCredentials != null && authenticationCredentials.length > 0 )
    {
      AuthenticationCredentials auth = authenticationCredentials[0];
      client.getParams().setAuthenticationPreemptive( true );
      client.getState().setCredentials( new AuthScope( null, AuthScope.ANY_PORT ), new UsernamePasswordCredentials( auth.getUsername(), auth.getPassword() ) );
    }

    int status = 0;

    try
    {
      method.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler( 3, false ) );

      status = client.executeMethod( method );
    }
    catch( HttpException e )
    {
      throw new Exception( "\n\nERROR in UrlReader#executeMethod for GetMethod#toString: " + method.toString(), e );
    }
    catch( IOException ex )
    {
      throw new Exception( "\n\nERROR in UrlReader#executeMethod for GetMethod#toString: " + method.toString(), ex );
    }

    if ( status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED )
    {
      throw new Exception( "\n\nERROR in UrlReader#executeMethod, status not HttpStatus.SC_OK or HttpStatus.SC_CREATED; status was: " + status + " ; status text: " + method.getStatusText() );
    }

    return method;
  }

  public String asString( String url, AuthenticationCredentials... authenticationCredentials ) throws Exception
  {
    GetMethod method = executeGetMethod( url, authenticationCredentials );
    return getResponseAsString( method );
  }

  public String asString( String url, Map<String, String> queryParams ) throws Exception
  {
    NameValuePair[] pairs = new NameValuePair[] {};
    if ( queryParams != null )
    {
      pairs = new NameValuePair[queryParams.size()];
      int i = 0;
      for ( Map.Entry<String, String> entry : queryParams.entrySet() )
      {
        pairs[i] = new NameValuePair( entry.getKey(), entry.getValue() );
        i++;
      }
    }
    GetMethod method = executeGetMethod( url, pairs );
    return getResponseAsString( method );
  }

  public String postAndReturnString( String url, Map<String, String> params ) throws Exception
  {
    PostMethod method = executePostMethod( url, params );
    return getResponseAsString( method );
  }

  public String postAndReturnString( String url, Map<String, String> params, String oauthToken ) throws Exception
  {
    PostMethod method = executePostMethod( url, params, oauthToken );
    return getResponseAsString( method );
  }

  public byte[] asBytes( String url, Map<String, String> queryParams ) throws Exception
  {
    NameValuePair[] pairs = new NameValuePair[] {};
    if ( queryParams != null )
    {
      pairs = new NameValuePair[queryParams.size()];
      int i = 0;
      for ( Map.Entry<String, String> entry : queryParams.entrySet() )
      {
        pairs[i] = new NameValuePair( entry.getKey(), entry.getValue() );
        i++;
      }
    }
    GetMethod method = executeGetMethod( url, pairs );
    byte[] responseBody = method.getResponseBody();
    method.releaseConnection();
    return responseBody;
  }

  public byte[] asBytes( String url ) throws Exception
  {
    GetMethod method = executeGetMethod( url );
    byte[] responseBody = method.getResponseBody();
    method.releaseConnection();
    return responseBody;
  }

  public Properties asProperties( String url, Map<String, String> queryParams ) throws Exception
  {
    NameValuePair[] pairs = new NameValuePair[] {};
    if ( queryParams != null )
    {
      pairs = new NameValuePair[queryParams.size()];
      int i = 0;
      for ( Map.Entry<String, String> entry : queryParams.entrySet() )
      {
        pairs[i] = new NameValuePair( entry.getKey(), entry.getValue() );
        i++;
      }
    }
    GetMethod method = executeGetMethod( url, pairs );
    InputStream responseBody = method.getResponseBodyAsStream();
    Properties prop = new Properties();
    prop.load( responseBody );
    method.releaseConnection();
    return prop;
  }

  protected String getResponseAsString( HttpMethod method ) throws IOException
  {
    String responseBody = method.getResponseBodyAsString();
    method.releaseConnection();
    return responseBody;
  }

  public static class AuthenticationCredentials
  {
    private String username;
    private String password;

    public AuthenticationCredentials( String username, String password )
    {
      this.username = username;
      this.password = password;
    }

    public String getPassword()
    {
      return password;
    }

    public String getUsername()
    {
      return username;
    }
  }

}
