/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.resttemplateclientfactory;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class RestReqAndResLoggingInterceptor implements ClientHttpRequestInterceptor
{
  private static final Log log = LogFactory.getLog( RestReqAndResLoggingInterceptor.class );

  @Override
  public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution ) throws IOException
  {
    logHttpRequest( request, body );
    ClientHttpResponse response = execution.execute( request, body );
    logHttpResponse( response );
    return response;
  }

  private void logHttpRequest( HttpRequest request, byte[] body ) throws IOException
  {
    if ( log.isInfoEnabled() )
    {
      log.info( "============================HTTP Request Begin============================================" );
      log.info( "URI          : " + request.getURI() );
      log.info( "Method       : " + request.getMethod() );
      log.info( "Headers      : " + request.getHeaders() );
      log.info( "Request body : " + new String( body, "UTF-8" ) );
      log.info( "============================HTTP Request End==============================================" );
    }
  }

  private void logHttpResponse( ClientHttpResponse response ) throws IOException
  {
    if ( log.isInfoEnabled() )
    {
      log.info( "============================HTTP Response Begin=============================================" );
      log.info( "Status code   : " + response.getStatusCode() );
      log.info( "Status text   : " + response.getStatusText() );
      log.info( "Headers       : " + response.getHeaders() );
      log.info( "Response body : " + StreamUtils.copyToString( response.getBody(), Charset.defaultCharset() ) );
      log.info( "============================HTTP Response End=============================================" );
    }

  }
}