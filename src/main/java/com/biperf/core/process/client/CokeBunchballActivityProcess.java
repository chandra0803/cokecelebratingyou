package com.biperf.core.process.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.CokeProcessService;

public class CokeBunchballActivityProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cokeBunchballActivityProcess";
  public static final String MESSAGE_NAME = "Coke Bunchball Activity Process";

  private static final Log log = LogFactory.getLog( CokeBunchballActivityProcess.class );

  private CokeProcessService cokeProcessService;

  @Override
  protected void onExecute()
  {
    addComment( "Starting process" );
    try
    {
      int resultCode = cokeProcessService.generateBunchballActivityData();
      addComment( "Process completed. Return code from stored proc-" + resultCode );
    }
    catch( Exception e )
    {
      addComment( "Process failed. Please look in logs for error details." );
      e.printStackTrace();
    }

  }

  public CokeProcessService getCokeProcessService()
  {
    return cokeProcessService;
  }

  public void setCokeProcessService( CokeProcessService cokeProcessService )
  {
    this.cokeProcessService = cokeProcessService;
  }

}
