
package com.biperf.core.process;

import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.value.hc.ParticipantSyncResponse;
import com.biw.hc.core.service.HCServices;

public class HoneycombParticipantSyncProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( HoneycombParticipantSyncProcess.class );

  public static final String BEAN_NAME = "honeycombParticipantSyncProcess";

  private HCServices hcServices = null;

  @Override
  protected void onExecute()
  {
    try
    {
      addComment( "Starting Honeycomb participant account sync" );
      Future<ParticipantSyncResponse> asyncResult = hcServices.syncAllParticipantDetails();
      
      // Calling get() will block, making the async call moot here.  
      // This is probably not a bad thing for the process - processes are already 'asynchronous' and the process will sit in 'executing' status intuitively
      ParticipantSyncResponse response = asyncResult.get();
      addComment( response.getSuccessfulUsernames().size() + " participants were synched.  " 
        + response.getMissingParticipants().size() + " participants were missing in honeycomb and not synched" );
    }
    catch( Exception e )
    {
      log.error( e );
      addComment( "An Exception occurred. " + "\n" + e.toString() + "\n" + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public HCServices getHcServices()
  {
    return hcServices;
  }

  public void setHcServices( HCServices hcServices )
  {
    this.hcServices = hcServices;
  }

}
