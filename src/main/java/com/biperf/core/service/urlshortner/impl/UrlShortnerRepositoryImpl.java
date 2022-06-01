/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.urlshortner.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.urlshortner.UrlShortnerRepository;
import com.biperf.core.service.urlshortner.v1.UrlShortnerRes;
import com.biperf.core.utils.MeshServicesUtil;

@Service( "UrlShortnerRepositoryImpl" )
public class UrlShortnerRepositoryImpl implements UrlShortnerRepository
{
  private static final Log log = LogFactory.getLog( UrlShortnerRepositoryImpl.class );

  @Override
  public String getShortUrl( String title, String url ) throws HttpStatusCodeException, ServiceErrorException
  {
    ResponseEntity<UrlShortnerRes> urlShortnerRes = null;

    String baseUrl = MeshServicesUtil.getNackleMeshServicesBaseEndPoint().replace( "v1", "" ).trim();
    String endPoint = baseUrl + "url-shortener/v1/shorten/latest";

    MultiValueMap<String, String> formparams = new LinkedMultiValueMap<String, String>();
    formparams.add( "title", title );
    formparams.add( "url", url );

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>( formparams, getHeaders() );

    try
    {
      urlShortnerRes = MeshServicesUtil.getRestWebClient().postForEntity( endPoint, requestEntity, UrlShortnerRes.class );
    }
    catch( RestClientException ex )
    {
      log.error( "Exception while posting url to url shortner service : " + ex );
    }
    catch( Exception exception )
    {
      log.error( "Exception while posting url : " + exception );
      throw new ServiceErrorException( exception.getMessage() );
    }

    if ( urlShortnerRes.getStatusCode() != HttpStatus.OK )
    {
      log.error( "The url shortner service response code : " + urlShortnerRes.getStatusCode() );
      throw new ServiceErrorException( "The url shortner service throwing the response code : " + urlShortnerRes.getStatusCode() );
    }

    return urlShortnerRes.getBody().getShorturl();
  }

  public HttpHeaders getHeaders() throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = new HttpHeaders();
    headers.add( "authorization", "Bearer " + MeshServicesUtil.getAuthorizationTokenForClientCredentials() );
    headers.add( "content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE );
    headers.add( "accept", MediaType.APPLICATION_JSON_VALUE );

    return headers;
  }

}
