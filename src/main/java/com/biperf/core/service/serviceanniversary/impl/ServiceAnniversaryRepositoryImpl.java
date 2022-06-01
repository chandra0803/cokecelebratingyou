
package com.biperf.core.service.serviceanniversary.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryRepository;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.serviceanniversary.GiftCodeSiteView;
import com.biperf.core.value.serviceanniversary.ServiceAwardSiteView;

@Service( "serviceAnniversaryRepositoryImpl" )
public class ServiceAnniversaryRepositoryImpl implements ServiceAnniversaryRepository
{
  private static final Log logger = LogFactory.getLog( ServiceAnniversaryRepositoryImpl.class );

  @Override
  public String getContributePageUrl( String invitationId, String uuid ) throws Exception
  {
    Map<String, Object> param = new HashMap<>();
    param.put( "invitationId", invitationId );
    ResponseEntity<ServiceAwardSiteView> saContributeDtlsVO;

    HttpHeaders httpHeaders = null;
    if ( StringUtil.isNullOrEmpty( uuid ) )
    {
      httpHeaders = MeshServicesUtil.getHeadersWithAuthorization();
    }
    else
    {
      httpHeaders = MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken( uuid );
    }

    try
    {
      saContributeDtlsVO = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/service-awards/celebration/{invitationId}/sites/service-awards",
                                                                         HttpMethod.POST,
                                                                         new HttpEntity<String>( httpHeaders ),
                                                                         ServiceAwardSiteView.class,
                                                                         param );
      if ( saContributeDtlsVO != null )
      {
        logger.info( saContributeDtlsVO.toString() );
      }
    }
    catch( RestClientException ex )
    {
      logger.error( "RestClientException while fetching the Contribute details in getContributePageUrl : " + ex.getMessage() + " celebration Id :" + invitationId );
      throw new ServiceErrorException( ex.getMessage() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while fetching the Contribute details in getContributePageUrl : " + exception.getMessage() + " celebration Id :" + invitationId );
      throw new ServiceErrorException( exception.getMessage() );
    }

    if ( saContributeDtlsVO != null && saContributeDtlsVO.getBody() != null && saContributeDtlsVO.getBody().getServiceAwardsSiteUrl() != null )
    {
      String url = saContributeDtlsVO.getBody().getServiceAwardsSiteUrl().replaceAll( "^\"|\"$", "" );
      return url.endsWith( "/" ) ? url : url.concat( "/" );
    }
    else
    {
      return null;
    }
  }

  @Override
  public String getGiftCodePageUrl( String celebrationId ) throws Exception
  {
    Map<String, Object> param = new HashMap<>();
    param.put( "celebrationId", celebrationId );
    ResponseEntity<GiftCodeSiteView> giftCodeSiteView;

    try
    {
      giftCodeSiteView = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/service-awards/celebration/{celebrationId}/sites/gift-code",
                                                                       HttpMethod.POST,
                                                                       new HttpEntity<String>( MeshServicesUtil.getHeadersWithAuthorization() ),
                                                                       GiftCodeSiteView.class,
                                                                       param );
      if ( giftCodeSiteView != null )
      {
        logger.info( giftCodeSiteView.toString() );
      }
    }
    catch( RestClientException ex )
    {
      logger.error( "RestClientException while fetching the Gift code link in getGiftCodePageUrl : " + ex.getMessage() + " celebration Id :" + celebrationId );
      throw new ServiceErrorException( ex.getMessage() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while fetching the Gift code link details in getGiftCodePageUrl : " + exception.getMessage() + " celebration Id :" + celebrationId );
      throw new ServiceErrorException( exception.getMessage() );
    }

    if ( giftCodeSiteView != null && giftCodeSiteView.getBody() != null && giftCodeSiteView.getBody().getGiftCodeSiteUrl() != null )
    {
      String url = giftCodeSiteView.getBody().getGiftCodeSiteUrl().replaceAll( "^\"|\"$", "" );
      return url.endsWith( "/" ) ? url : url.concat( "/" );
    }
    else
    {
      return null;
    }
  }
}
