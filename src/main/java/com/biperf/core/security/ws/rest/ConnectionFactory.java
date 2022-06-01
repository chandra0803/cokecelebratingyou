
package com.biperf.core.security.ws.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;

public class ConnectionFactory implements HttpURLConnectionFactory
{

  // Proxy proxy;

  /*
   * private void initializeProxy() { System.getProperties().put("bi.http.proxyHost",
   * "inproxy.biin.bi.corp"); System.getProperties().put("bi.http.proxyPort", "8080"); proxy =
   * Environment.buildProxy() ; }
   */

  public HttpURLConnection getHttpURLConnection( URL url ) throws IOException
  {
    // initializeProxy();
    return (HttpURLConnection)url.openConnection();
  }
}
