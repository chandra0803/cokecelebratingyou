
package com.biperf.core.service.ids.impl;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ids.IDSRepository;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.vo.ids.IDServiceResVO;

@Service( "IDSRepositoryImpl" )
public class IDSRepositoryImpl implements IDSRepository
{
  private static final Log logger = LogFactory.getLog( IDSRepositoryImpl.class );

  @Override
  public String getKongGateWayToken() throws HttpStatusCodeException, ServiceErrorException
  {
    return getKongToken( getIDSParameters() );
  }

  @Override
  public String getKongGateWayTokenByPersonId( String personId ) throws HttpStatusCodeException, ServiceErrorException
  {
    return getKongToken( getIDSParametersByPersonId( personId ) );
  }

  @Override
  public String getKongGateWayForClientCredentials() throws HttpStatusCodeException, ServiceErrorException
  {
    return getKongToken( getIDSClientCredentialsParameters() );
  }

  @Override
  public String getIDSSSOEndpoint() throws HttpStatusCodeException, ServiceErrorException
  {
    return getSSOEndpoint( getIDSParameters() );
  }

  private HttpEntity<MultiValueMap<String, String>> getIDSParameters()
  {
    MultiValueMap<String, String> parameters = getParameters();

    if ( Objects.nonNull( UserManager.getUserId() ) )
    {
      parameters.add( "person_id", UserManager.getRosterUserId().toString() );
    }
    else
    {
      parameters.add( "person_id", UUID.randomUUID().toString() );
    }

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>( parameters, MeshServicesUtil.getGenericHeaders() );

    return requestEntity;
  }

  private HttpEntity<MultiValueMap<String, String>> getIDSParametersByPersonId( String personId )
  {
    MultiValueMap<String, String> parameters = getParameters();
    // The person id should be in UUID format.
    parameters.add( "person_id", personId );

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>( parameters, MeshServicesUtil.getGenericHeaders() );

    return requestEntity;
  }

  private HttpEntity<MultiValueMap<String, String>> getIDSClientCredentialsParameters()
  {
    MultiValueMap<String, String> parameters = getClientCredentialsParameters();
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>( parameters, MeshServicesUtil.getGenericHeaders() );

    return requestEntity;

  }

  private MultiValueMap<String, String> getParameters()
  {

    MultiValueMap<String, String> parameters = getCommonParameters();
    parameters.add( "grant_type", "daymaker" );

    return parameters;

  }

  private MultiValueMap<String, String> getClientCredentialsParameters()
  {
    MultiValueMap<String, String> parameters = getCommonParameters();
    parameters.add( "grant_type", "client_credentials" );

    return parameters;

  }

  private MultiValueMap<String, String> getCommonParameters()
  {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    String clientID = getClinetID();

    parameters.add( "client_id", clientID );
    parameters.add( "client_secret", getClientSecret() );
    parameters.add( "company_identifier", getContextName() );

    return parameters;

  }

  private String getKongToken( HttpEntity<MultiValueMap<String, String>> parameters ) throws HttpStatusCodeException, ServiceErrorException
  {
    return getKongTokenWithSSOEndpoint( parameters ).getAccessToken();
  }

  private String getSSOEndpoint( HttpEntity<MultiValueMap<String, String>> parameters ) throws HttpStatusCodeException, ServiceErrorException
  {
    return getKongTokenWithSSOEndpoint( parameters ).getSsoEndpoint();

  }

  private IDServiceResVO getKongTokenWithSSOEndpoint( HttpEntity<MultiValueMap<String, String>> parameters ) throws HttpStatusCodeException, ServiceErrorException
  {

    ResponseEntity<IDServiceResVO> idServiceResVO = null;

    try
    {
      idServiceResVO = MeshServicesUtil.getRestWebClient().postForEntity( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/id/token", parameters, IDServiceResVO.class );

      if ( idServiceResVO.getStatusCode() != HttpStatus.OK )
      {
        throw new ServiceErrorException( "Exception while getting token from ID service : " + idServiceResVO.getBody().toString() );
      }
    }
    catch( Exception resException )
    {
      logger.error( "Exception while getting token from ID service : " + resException.getMessage() );
      throw new ServiceErrorException( idServiceResVO.getBody().toString() );
    }

    return idServiceResVO.getBody();

  }

  private String getContextName()
  {
    return getSystemVariableService().getContextName();
  }

  private String getClientSecret()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
  }

  private String getClinetID()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
