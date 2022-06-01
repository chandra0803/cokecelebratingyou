/*
 * (c) 2018 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.domain.company.Company;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.resttemplateclientfactory.RestTemplateClientFactory;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.ids.IDSRepository;
import com.biperf.core.service.system.SystemVariableService;

@Component
public class MeshServicesUtil
{
  private static final Log log = LogFactory.getLog( MeshServicesUtil.class );

  private static RestTemplateClientFactory restTemplateClientFactory = new RestTemplateClientFactory();

  public static RestTemplate getRestWebClient()
  {
    return restTemplateClientFactory.getRestWebClient();

  }

  public static HttpHeaders getAuthorizationHeadersWithCompanyIDAndJWTToken( String personId ) throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = getHeaders();
    headers.add( "authorization", "Bearer " + getIDSRepository().getKongGateWayTokenByPersonId( personId ) );

    return headers;
  }

  public static HttpHeaders getAuthorizationHeadersWithCompanyIDAndJWTToken() throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = getHeaders();
    headers.add( "authorization", "Bearer " + getIDSRepository().getKongGateWayToken() );

    return headers;
  }

  public static HttpHeaders getAuthorizationHeadersForClientCredentials() throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = getHeaders();
    headers.add( "authorization", "Bearer " + getIDSRepository().getKongGateWayForClientCredentials() );

    return headers;
  }

  public static String getAuthorizationTokenForClientCredentials() throws HttpStatusCodeException, ServiceErrorException
  {
    return getIDSRepository().getKongGateWayForClientCredentials();
  }

  public static HttpHeaders getAuthorizationHeadersWithCompanyIDAndJWTTokenForMultiPart() throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = getMultiPartHeaders();
    headers.add( "authorization", "Bearer " + getIDSRepository().getKongGateWayToken() );

    return headers;
  }

  public static HttpHeaders getHeadersWithAuthorization() throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = new HttpHeaders();
    headers.add( "content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE );
    headers.add( "authorization", "Bearer " + getIDSRepository().getKongGateWayToken() );
    headers.add( "correlation-id", getCorrelationId() );

    return headers;
  }

  public static HttpHeaders getGenericHeaders()
  {
    HttpHeaders headers = new HttpHeaders();
    headers.add( "content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE );
    headers.add( "correlation-id", getCorrelationId() );

    return headers;
  }

  public static HttpHeaders getJsonHeaders()
  {
    HttpHeaders headers = new HttpHeaders();
    headers.add( "content-type", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "correlation-id", getCorrelationId() );

    return headers;
  }

  public static String getNackleMeshServicesBaseEndPoint()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_HOST_BASE_URL ).getStringVal();
  }

  public static String getCompanyId()
  {
    Company company = getCompanyService().getCompanyDetail();
    return Objects.nonNull( company ) ? company.getCompanyId().toString() : null;
  }

  public static HttpHeaders getHeaders() throws HttpStatusCodeException, ServiceErrorException
  {
    HttpHeaders headers = new HttpHeaders();

    try
    {
      String companyID = getCompanyId();

      if ( Objects.nonNull( companyID ) )
      {
        headers.add( "company-id", companyID );

      }

    }
    catch( Exception ex )
    {
      log.error( "Company ID is  Null : " + ex.getMessage() );
    }

    headers.add( "content-type", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "accept", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "correlation-id", getCorrelationId() );

    return headers;
  }

  private static HttpHeaders getMultiPartHeaders()
  {
    HttpHeaders headers = new HttpHeaders();

    try
    {
      String companyID = getCompanyId();

      if ( Objects.nonNull( companyID ) )
      {
        headers.add( "company-id", companyID );
        log.info( "company ID" + companyID );
      }

    }
    catch( Exception ex )
    {
      log.error( "Company ID is  Null : " + ex.getMessage() );
    }

    headers.add( "content-type", MediaType.MULTIPART_FORM_DATA_VALUE );
    headers.add( "accept", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "correlation-id", getCorrelationId() );

    return headers;

  }

  private static String getCorrelationId()
  {
    String uuID = UUID.randomUUID().toString();
    return uuID;
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private static CompanyService getCompanyService()
  {
    return (CompanyService)BeanLocator.getBean( CompanyService.BEAN_NAME );
  }

  private static IDSRepository getIDSRepository()
  {
    return (IDSRepository)BeanLocator.getBean( IDSRepository.BEAN_NAME );
  }

}
