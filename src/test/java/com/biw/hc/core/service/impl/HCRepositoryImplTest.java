
package com.biw.hc.core.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.ui.UnitTest;
import com.biperf.core.value.hc.HCRequestDetails;

public class HCRepositoryImplTest extends UnitTest
{
  RestTemplate mockRestTemplate;

  HCRepositoryImpl hcRepository;

  public void setUp()
  {
    mockRestTemplate = createMock( RestTemplate.class );

    hcRepository = new HCRepositoryImpl()
    {
      RestTemplate getRestTemplate()
      {
        return mockRestTemplate;
      }
    };
  }

  @SuppressWarnings( "unchecked" )
  @Test
  public <T> void testExecute() throws Exception
  {
    T employerRecords = (T)new ArrayList<HashMap<String, Object>>();
    HCRequestDetails hcRequestDetails = new HCRequestDetails();
    HttpHeaders headers = new HttpHeaders();

    hcRequestDetails.setUrl( "http://localhost:8001/hc-gateway/services/1.0" );
    hcRequestDetails.setHttpMethod( HttpMethod.POST );
    hcRequestDetails.setClientCode( "g" );
    hcRequestDetails.setEncyrptedSaltKey( "encrypted salt key" );
    hcRequestDetails.setProduct( "G6" );
    hcRequestDetails.setVersion( "6.3" );

    headers.add( "accept", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "content-type", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "clientcode", hcRequestDetails.getClientCode() );
    headers.add( "signature", hcRequestDetails.getEncyrptedSaltKey() );
    headers.add( "product", hcRequestDetails.getProduct() );
    headers.add( "version", hcRequestDetails.getVersion() );

    ResponseEntity<Void> response = new ResponseEntity<>( HttpStatus.OK );

    expect( mockRestTemplate.exchange( eq( hcRequestDetails.getUrl() ), eq( hcRequestDetails.getHttpMethod() ), (HttpEntity<?>)anyObject(), eq( Void.class ) ) ).andReturn( response );

    hcRepository.execute( hcRequestDetails, employerRecords, Void.class );

  }

}
