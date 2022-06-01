
package com.biperf.core.service.ots.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ots.OTSRepository;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.value.ots.v1.reedeem.CardInfo;
import com.biperf.core.value.ots.v1.reedeem.RedeemRequest;
import com.biperf.core.value.ots.v1.reedeem.RedeemResponse;

@Service( "OTSRepositoryImpl" )
public class OTSRepositoryImpl implements OTSRepository
{

  // To Fetch The OTS Program Details
  @Override
  public Program getProgramInfo( String otsProgamNumber ) throws HttpStatusCodeException, ServiceErrorException
  {
    ResponseEntity<Program> programOpValue = null;

    programOpValue = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/ots/programs/{id}",
                                                                   HttpMethod.GET,
                                                                   new HttpEntity<String>( MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken() ),
                                                                   Program.class,
                                                                   otsProgamNumber );

    return (Program)programOpValue.getBody();
  }

  @Override
  // Update The Batch Details
  public <T> boolean updateBatchDetails( T batchDetails ) throws HttpStatusCodeException, ServiceErrorException
  {
    ResponseEntity<Program> batchOpValue = null;
    Program batchDetailsLocal = (Program)batchDetails;
    batchOpValue = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/ots/programs/" + batchDetailsLocal.getProgramNumber(),
                                                                 HttpMethod.POST,
                                                                 new HttpEntity<T>( batchDetails, (HttpHeaders)MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken() ),
                                                                 Program.class );

    return batchOpValue.getStatusCode() == HttpStatus.OK ? true : false;
  }

  @Override
  public CardInfo getCardInfo( String cardNumber ) throws HttpStatusCodeException, ServiceErrorException
  {

    ResponseEntity<CardInfo> programOpValue = null;

    programOpValue = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/ots/cards/{cardNumber}",
                                                                   HttpMethod.GET,
                                                                   new HttpEntity<String>( MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken() ),
                                                                   CardInfo.class,
                                                                   cardNumber );

    return programOpValue.getBody();
  }

  @Override
  public RedeemResponse redeem( RedeemRequest redeemRequest ) throws HttpStatusCodeException, ServiceErrorException
  {

    ResponseEntity<RedeemResponse> depositResponse = MeshServicesUtil.getRestWebClient()
        .exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/ots/redeem",
                   HttpMethod.POST,
                   new HttpEntity<>( redeemRequest, (HttpHeaders)MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken() ),
                   RedeemResponse.class );
    return depositResponse.getBody();
  }

}
