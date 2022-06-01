
package com.biperf.core.service.underarmour.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.underarmour.UARepository;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.JsonUtils;
import com.biperf.core.value.underArmour.v1.BaseRestResponseObject;
import com.biperf.core.value.underArmour.v1.actigraphy.ActigraphyRequest;
import com.biperf.core.value.underArmour.v1.actigraphy.response.ActigraphyResponse;
import com.biperf.core.value.underArmour.v1.application.ApplicationRequest;
import com.biperf.core.value.underArmour.v1.participant.ParticipantRequest;
import com.biperf.core.value.underArmour.v1.participant.PaxAuthStatusResponse;

@Service( "UARepositoryImpl" )
public class UARepositoryImpl implements UARepository
{

  private static final Log logger = LogFactory.getLog( UARepositoryImpl.class );

  @Autowired
  private SystemVariableService systemVariableService;

  private RestTemplate restTemplate = null;

  private RestTemplate getRestTemplate()
  {
    if ( restTemplate != null )
    {
      return restTemplate;
    }
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setProxy( Environment.buildProxy() );
    restTemplate = new RestTemplate();
    restTemplate.setRequestFactory( factory );
    return restTemplate;
  }

  public BaseRestResponseObject insertApplication( ApplicationRequest applicationRequest )
  {
    BaseRestResponseObject baseResponseObject = getRestTemplate().postForEntity( getUnderArmourUrl( "/ua-record/v1/application/createApplication" ), applicationRequest, BaseRestResponseObject.class )
        .getBody();
    uaLoggers( applicationRequest, baseResponseObject );
    return baseResponseObject;
  }

  public BaseRestResponseObject updateApplication( ApplicationRequest applicationRequest )
  {
    BaseRestResponseObject baseResponseObject = getRestTemplate().postForEntity( getUnderArmourUrl( "/ua-record/v1/application/updateApplication" ), applicationRequest, BaseRestResponseObject.class )
        .getBody();
    uaLoggers( applicationRequest, baseResponseObject );
    return baseResponseObject;
  }

  public BaseRestResponseObject authorizeParticipant( ParticipantRequest participantRequest )
  {
    BaseRestResponseObject baseResponseObject = getRestTemplate()
        .postForEntity( getUnderArmourUrl( "/ua-record/v1/participant/authorizeParticipant" ), participantRequest, BaseRestResponseObject.class ).getBody();
    uaLoggers( participantRequest, baseResponseObject );
    return baseResponseObject;
  }

  public BaseRestResponseObject deAuthorizeParticipant( ParticipantRequest participantRequest )
  {
    BaseRestResponseObject baseResponseObject = getRestTemplate()
        .postForEntity( getUnderArmourUrl( "/ua-record/v1/participant/deauthorizeParticipant" ), participantRequest, BaseRestResponseObject.class ).getBody();
    uaLoggers( participantRequest, baseResponseObject );
    return baseResponseObject;
  }

  public PaxAuthStatusResponse isAuthorizeParticipant( ParticipantRequest participantRequest )
  {
    PaxAuthStatusResponse paxAuthStatusResponse = getRestTemplate()
        .postForEntity( getUnderArmourUrl( "/ua-record/v1/participant/isParticipantAuthorized" ), participantRequest, PaxAuthStatusResponse.class ).getBody();
    uaLoggers( participantRequest, paxAuthStatusResponse );
    return paxAuthStatusResponse;
  }

  public ActigraphyResponse getActigraphy( ActigraphyRequest actigraphyRequest )
  {
    ActigraphyResponse actigraphyResponse = getRestTemplate().postForEntity( getUnderArmourUrl( "/ua-record/v1/actigraphy/getActigraphy" ), actigraphyRequest, ActigraphyResponse.class ).getBody();
    uaLoggers( actigraphyRequest, actigraphyResponse );
    return actigraphyResponse;
  }

  private String getUnderArmourUrl( String uaServiceUrl )
  {
    String url = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.UA_WEBSERVICES_URL_PREFIX ).getStringVal() + uaServiceUrl + ".biws";
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Under Armour URL: " + url );
    }
    return url;
  }

  private void uaLoggers( Object request, Object response )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Request: " + JsonUtils.toJsonStringFromObject( request ) );
      logger.debug( "Response: " + JsonUtils.toJsonStringFromObject( response ) );
    }
  }

}
