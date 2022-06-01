
package com.biperf.core.service.ecard.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ecard.ECardsRepository;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.ecard.ECardMigrateResponse;
import com.biperf.core.value.ecard.ECardMigrateView;
import com.biperf.core.value.ecard.ECardRequest;
import com.biperf.core.value.ecard.ECardResponse;

@Service( "eCardsRepositoryImpl" )
public class ECardsRepositoryImpl implements ECardsRepository
{

  private static final Log logger = LogFactory.getLog( ECardsRepositoryImpl.class );

  @Override
  public ResponseEntity<ECardResponse[]> getECards( ECardRequest ecardRequest ) throws ServiceErrorException, Exception
  {
    ResponseEntity<ECardResponse[]> eCardresponse = null;
    try
    {
      eCardresponse = MeshServicesUtil.getRestWebClient()
          .exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/ecards/" + ecardRequest.getCompanyId() + "/" + ecardRequest.getVisibility(),
                     HttpMethod.GET,
                     new HttpEntity<>( MeshServicesUtil.getGenericHeaders() ),
                     ECardResponse[].class );
    }
    catch( RestClientException ex )
    {
      logger.error( "Exception while getting ecards : " + ex.getMessage() );
      throw new ServiceErrorException( ex.getMessage() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while getting ecards : " + exception.getMessage() );
      throw new ServiceErrorException( exception.getMessage() );
    }
    return eCardresponse;
  }

  @Override
  public List<ECardMigrateView> getMigrateECards( String ecardMigrateRequest ) throws ServiceErrorException
  {
    ResponseEntity<ECardMigrateResponse> eCardresponse = null;
    List<ECardMigrateView> ecardList = null;

    try
    {
      eCardresponse = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/ecards",
                                                                    HttpMethod.POST,
                                                                    new HttpEntity<String>( ecardMigrateRequest, MeshServicesUtil.getJsonHeaders() ),
                                                                    ECardMigrateResponse.class );

    }
    catch( RestClientException ex )
    {
      logger.error( "Exception while getting migrate ecards : " + ex.getMessage() );
      throw new ServiceErrorException( ex.getMessage() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while getting migrate ecards : " + exception.getMessage() );
      throw new ServiceErrorException( exception.getMessage() );
    }

    if ( eCardresponse != null && null != eCardresponse.getBody() )
    {
      ecardList = eCardresponse.getBody().getResponse();
    }
    return ecardList;
  }
}
