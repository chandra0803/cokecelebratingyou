/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.rosterproxy.impl;

import java.util.Objects;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.biperf.core.service.rosterproxy.RosterProxyRepository;
import com.biperf.core.service.rosterproxy.V1.RosterProxyRequest;
import com.biperf.core.service.rosterproxy.V1.RosterProxyView;
import com.biperf.core.service.rosterproxy.V1.RosterPutRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.MeshServicesUtil;

@Service( "RosterProxyRepositoryImpl" )
public class RosterProxyRepositoryImpl implements RosterProxyRepository
{
  private static final Log logger = LogFactory.getLog( RosterProxyRepositoryImpl.class );

  protected @Autowired SystemVariableService systemVariableService;
  private final static String NACKLE_URL = "/nkl-api/services/v1.0";

  @Override
  public RosterProxyView resideRosterProxyInfo()
  {
    RosterProxyView view = getRosterProxyDetails();

    if ( Objects.nonNull( view ) )
    {
      if ( view.getResponseCode() == 200 )
      {
        String apiSecret = RandomStringUtils.randomAlphanumeric( 15 );

        RosterPutRequest request = new RosterPutRequest();
        request.setEndpoint( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + NACKLE_URL );
        request.setApiKey( getSystemVariableService().getContextName() );
        request.setApiSecret( apiSecret );

        view = putRosterProxyDetails( request );
        view.setDeveloperMessage( apiSecret );
      }
      else
      {
        String apiSecret = RandomStringUtils.randomAlphanumeric( 15 );

        RosterProxyRequest request = new RosterProxyRequest();
        request.setCompanyId( MeshServicesUtil.getCompanyId() );
        request.setEndpoint( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + NACKLE_URL );
        request.setApiKey( getSystemVariableService().getContextName() );
        request.setApiSecret( apiSecret );

        view = postRosterProxyDetails( request );
        view.setDeveloperMessage( apiSecret );

      }
    }

    return view;
  }

  @Override
  public RosterProxyView getRosterProxyInfo()
  {
    return getRosterProxyDetails();
  }

  @Override
  public RosterProxyView postRosterProxyInfo( RosterProxyRequest request )
  {
    return postRosterProxyDetails( request );
  }

  @Override
  public RosterProxyView putRosterProxyInfo( RosterPutRequest request )
  {
    return putRosterProxyDetails( request );
  }

  private RosterProxyView getRosterProxyDetails()
  {
    ResponseEntity<RosterProxyView> rosterProxyView = null;

    try
    {

      rosterProxyView = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/roster-proxy-admin/companyroster/{companyId}",
                                                                      HttpMethod.GET,
                                                                      new HttpEntity<String>( MeshServicesUtil.getAuthorizationHeadersForClientCredentials() ),
                                                                      RosterProxyView.class,
                                                                      MeshServicesUtil.getCompanyId() );

    }
    catch( Exception e )
    {
      logger.error( "Message from roster proxy service :: " + e );
      RosterProxyView view = new RosterProxyView();
      view.setResponseCode( 404 );
      return view;

    }
    return rosterProxyView.getBody();
  }

  private RosterProxyView postRosterProxyDetails( RosterProxyRequest request )
  {
    RosterProxyView rosterProxyView = null;

    try
    {
      rosterProxyView = MeshServicesUtil.getRestWebClient().postForObject( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/roster-proxy-admin/companyroster",
                                                                           new HttpEntity<RosterProxyRequest>( request, MeshServicesUtil.getAuthorizationHeadersForClientCredentials() ),
                                                                           RosterProxyView.class );
    }
    catch( Exception e )
    {
      logger.error( "Message from roster proxy service :: " + e );
      rosterProxyView.setResponseCode( 404 );
      return rosterProxyView;
    }
    return rosterProxyView;

  }

  private RosterProxyView putRosterProxyDetails( RosterPutRequest request )
  {
    ResponseEntity<RosterProxyView> rosterProxyView = null;

    try
    {
      rosterProxyView = MeshServicesUtil.getRestWebClient()
          .exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/roster-proxy-admin/companyroster/" + MeshServicesUtil.getCompanyId(),
                     HttpMethod.PUT,
                     new HttpEntity<RosterPutRequest>( request, MeshServicesUtil.getAuthorizationHeadersForClientCredentials() ),
                     RosterProxyView.class );
    }
    catch( Exception e )
    {
      logger.error( "Message from roster proxy service :: " + e );
      RosterProxyView view = new RosterProxyView();
      view.setResponseCode( 404 );
      return view;
    }
    return rosterProxyView.getBody();

  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
