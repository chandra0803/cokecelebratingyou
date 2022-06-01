/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/util/WebDavConfiguration.java,v $
 */

package com.biperf.core.service.util;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * WebDavConfiguration.
 *
 * TODO move this to ContentLibrary object
 *
 * @author repko
 * @since Sep 9, 2008
 * @version 1.0
 */
/**
 * WebDavConfiguration.
 * 
 * @author repko
 * @since Sep 9, 2008
 * @version 1.0
 */
public class WebDavConfiguration implements Cloneable, Serializable
{
  private String host;

  private Integer port;

  private String path;

  private String protocol;

  private String rootPath;

  private String username;

  private String password;

  public WebDavConfiguration()
  {
    // default is a nop
  }

  public WebDavConfiguration( String originalUrl )
  {
    try
    {
      HttpURL httpsURL = new HttpsURL( originalUrl );
      host = httpsURL.getHost();
      port = new Integer( httpsURL.getPort() );
      path = httpsURL.getPath();
      protocol = originalUrl.substring( 0, originalUrl.indexOf( ":" ) );
    }
    catch( URIException urie )
    {
      // TODO: Catch and log
    }
  }

  public WebDavConfiguration( String path, String username, String password )
  {
    this( path );
    this.username = username;
    this.password = password;
  }

  /**
   * Used for testing only
   * 
   * @param props
   */
  public WebDavConfiguration( Properties props )
  {
    boolean enabled = Boolean.parseBoolean( props.getProperty( "enabled", "false" ) );
    if ( enabled )
    {
      this.host = props.getProperty( "host" );
      this.port = Integer.parseInt( props.getProperty( "port" ) );
      this.path = props.getProperty( "path" );
      this.username = props.getProperty( "username" );
      this.password = props.getProperty( "password" );
      this.protocol = props.getProperty( "protocol" );
    }
  }

  /**
   * Assumes the path to be /work/wip/../prefix/subfoldername
   * 
   * @param prefix
   * @return
   */
  public String getSubfolderName( String prefix )
  {
    if ( path != null && path.length() > 0 )
    {
      int prefixIndex = path.toUpperCase().indexOf( prefix.toUpperCase() );
      int afterPrefixIndex = prefixIndex + prefix.length() + 1;
      String subfolderURI = path.substring( afterPrefixIndex, path.indexOf( "/", afterPrefixIndex ) + 1 );
      return subfolderURI;
    }
    return null;
  }

  /**
   * Assumes the path to be /work/wip/../prefix/subfoldername
   * 
   * @param prefix
   * @return
   */
  public String getIncomingFolderName( String prefix )
  {
    if ( path != null && path.length() > 0 )
    {
      int prefixIndex = path.toUpperCase().indexOf( prefix.toUpperCase() );
      int afterPrefixIndex = prefixIndex + prefix.length() + 1;
      int afterSubfolderIndex = path.indexOf( "/", afterPrefixIndex ) + 1;
      String incomingFoldername = path.substring( afterSubfolderIndex, path.indexOf( "/", afterSubfolderIndex ) + 1 );
      return incomingFoldername;
    }
    return null;
  }

  /**
   * Assumes the path to be /work/wip/../prefix/subfoldername
   * 
   * @param prefix
   * @return
   */
  public String getPrefixPath( String prefix )
  {
    if ( path != null && path.length() > 0 )
    {
      int prefixIndex = path.toUpperCase().indexOf( prefix.toUpperCase() );
      // Add 1 for adding /
      int afterPrefixIndex = prefixIndex + prefix.length() + 1;
      String subfolderURI = path.substring( 0, afterPrefixIndex );
      return subfolderURI;
    }
    return null;
  }

  /**
   * Assumes the path to be /work/wip/../prefix/subfoldername/incoming/filename
   * 
   * @param prefix
   * @return
   */
  public String getSubFolderPath( String prefix )
  {
    if ( prefix != null && prefix.length() > 0 )
    {
      return new StringBuilder( getProtocol() ).append( "://" ).append( getHost() ).append( ':' ).append( getPort() ).append( getPrefixPath( prefix ) ).append( getSubfolderName( prefix ) ).toString();
    }
    return null;
  }

  public String getHostNameURL()
  {
    return new StringBuilder( getProtocol() ).append( "://" ).append( getHost() ).append( ':' ).append( getPort() ).toString();
  }

  public boolean isEnabled()
  {
    return !StringUtils.isBlank( host );
  }

  public String getRootUrl()
  {
    return new StringBuilder( getProtocol() ).append( "://" ).append( getHost() ).append( ':' ).append( getPort() ).append( getPath() ).toString();
  }

  public String getHost()
  {
    return host;
  }

  public void setHost( String host )
  {
    this.host = host;
  }

  public Integer getPort()
  {
    return port;
  }

  public void setPort( Integer port )
  {
    this.port = port;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath( String path )
  {
    this.path = path;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername( String username )
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this, ToStringStyle.SHORT_PREFIX_STYLE );
    builder.append( "host", host );
    builder.append( "port", port );
    builder.append( "path", path );
    builder.append( "username", username );
    builder.append( "password", password );
    builder.append( "rootUrl", getRootUrl() );
    return builder.toString();
  }

  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public HttpClient getHttpClient()
  {

    HostConfiguration hostConfig = new HostConfiguration();
    hostConfig.setHost( getHostNameURL() );

    HttpConnectionManager connMgr = new SimpleHttpConnectionManager( true );
    HttpConnectionManagerParams params = new HttpConnectionManagerParams();
    params.setMaxConnectionsPerHost( hostConfig, 1 );
    connMgr.setParams( params );
    HttpClient httpClient = new HttpClient();
    httpClient.setHostConfiguration( hostConfig );
    httpClient.setHttpConnectionManager( connMgr );
    if ( username != null && password != null )
    {
      httpClient.getParams().setAuthenticationPreemptive( true );
      httpClient.getState().setCredentials( new AuthScope( host, port, AuthScope.ANY_REALM ), new UsernamePasswordCredentials( username, password ) );
    }
    return httpClient;
  }

  public String getProtocol()
  {
    return protocol;
  }

  public void setProtocol( String protocol )
  {
    this.protocol = protocol;
  }

}
