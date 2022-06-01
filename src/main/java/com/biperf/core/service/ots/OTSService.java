
package com.biperf.core.service.ots;

import org.json.JSONException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.value.ots.v1.reedeem.RedeemResponse;

public interface OTSService extends SAO
{
  public static final String BEAN_NAME = "otsService";

  public Program getOTSProgramInfo( String otsProgamNumber ) throws ServiceErrorException, JSONException;

  public boolean updateOTSBatchDetails( Program batchDetails ) throws ServiceErrorException;

  public RedeemResponse redeem( String cardNumber, Long userId ) throws ServiceErrorException, JSONException;

}