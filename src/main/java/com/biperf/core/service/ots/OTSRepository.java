
package com.biperf.core.service.ots;

import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.value.ots.v1.reedeem.CardInfo;
import com.biperf.core.value.ots.v1.reedeem.RedeemRequest;
import com.biperf.core.value.ots.v1.reedeem.RedeemResponse;

public interface OTSRepository
{
  public Program getProgramInfo( String otsProgamNumber ) throws HttpStatusCodeException, ServiceErrorException;

  public <T> boolean updateBatchDetails( T batchDetails ) throws HttpStatusCodeException, ServiceErrorException;

  public CardInfo getCardInfo( String cardNumber ) throws HttpStatusCodeException, ServiceErrorException;

  public RedeemResponse redeem( RedeemRequest redeemRequest ) throws HttpStatusCodeException, ServiceErrorException;
}
