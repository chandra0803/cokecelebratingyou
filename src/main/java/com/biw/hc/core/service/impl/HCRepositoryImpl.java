
package com.biw.hc.core.service.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ExceptionView;
import com.biperf.core.exception.HoneycombException;
import com.biperf.core.value.hc.HCRequestDetails;
import com.biw.hc.core.service.HCRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service( "HCRepositoryImpl" )
public class HCRepositoryImpl implements HCRepository
{
  private static final Logger log = LoggerFactory.getLogger( HCRepositoryImpl.class );

  private RestTemplate restTemplate = null;

  RestTemplate getRestTemplate()
  {
    if ( restTemplate != null )
    {
      return restTemplate;
    }

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    restTemplate = new RestTemplate( requestFactory );
    return restTemplate;
  }

  @Override
  public <T, V> ResponseEntity<V> execute( HCRequestDetails hcRequestDetails, T data, Class<V> response ) throws HoneycombException
  {
    HttpHeaders headers = new HttpHeaders();

    // PASSING ALL HEADERS FOR NOW EVEN IF WE USE IT OR NOT
    headers.add( HCHeaderEnum.ACCEPT.getHeader(), MediaType.APPLICATION_JSON_VALUE );
    headers.add( HCHeaderEnum.CONTENT_TYPE.getHeader(), MediaType.APPLICATION_JSON_VALUE );
    headers.add( HCHeaderEnum.CLIENT_CODE.getHeader(), hcRequestDetails.getClientCode() );
    headers.add( HCHeaderEnum.SIGNATURE.getHeader(), hcRequestDetails.getEncyrptedSaltKey() );
    headers.add( HCHeaderEnum.PRODUCT.getHeader(), hcRequestDetails.getProduct() );
    headers.add( HCHeaderEnum.VERSION.getHeader(), hcRequestDetails.getVersion() );
    headers.add( HCHeaderEnum.LOCALE.getHeader(), hcRequestDetails.getLocale() );
    headers.add( HCHeaderEnum.USER_NAME.getHeader(), hcRequestDetails.getUsername() );
    headers.add( HCHeaderEnum.USER_ID.getHeader(), hcRequestDetails.getUserId() );

    // Don't send client id if we don't have it - HC code will try to parse and throw exception
    if ( StringUtils.isNotBlank( hcRequestDetails.getClientId() ) )
    {
      headers.add( HCHeaderEnum.CLIENT_ID.getHeader(), hcRequestDetails.getClientId() );
    }

    try
    {
      return getRestTemplate().exchange( hcRequestDetails.getUrl(), hcRequestDetails.getHttpMethod(), new HttpEntity<T>( data, headers ), response );
    }
    catch( HttpStatusCodeException httpException )
    {
      ObjectMapper mapper = new ObjectMapper();
      try
      {
        // Maybe over-logging honeycomb exceptions, but they can be hard to recreate and diagnose so this is okay
        log.error( "Honeycomb HTTP Exception. Response Body: " + httpException.getResponseBodyAsString() );
        ExceptionView view = mapper.readValue( httpException.getResponseBodyAsString(), ExceptionView.class );
        HoneycombException honeycombException = buildHoneycombException( view );
        throw honeycombException;
      }
      catch( IOException mappingException )
      {
        log.error( "Unable to deserialize exception view", mappingException );
        throw new BeaconRuntimeException( "Unable to deserialize exception view", mappingException );
      }
    }
  }

  private HoneycombException buildHoneycombException( ExceptionView view )
  {
    HoneycombException exception = new HoneycombException();
    exception.setDeveloperMessage( view.getDeveloperMessage() );
    exception.setErrorCode( view.getErrorCode() );
    exception.setResponseCode( view.getResponseCode() );
    exception.setResponseMessage( view.getResponseMessage() );
    return exception;
  }
}