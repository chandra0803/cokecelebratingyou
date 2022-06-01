
package com.biperf.core.mobileapp.recognition.service;

import java.io.IOException;

import com.biperf.core.google.android.gcm.server.Message;
import com.biperf.core.google.android.gcm.server.Result;
import com.biperf.core.google.android.gcm.server.Sender;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.service.system.SystemVariableService;

public class GoogleCloudMessagingServiceImpl implements GoogleCloudMessagingService
{
  private SystemVariableService systemVariableService;

  @Override
  public Result sendNoRetry( Message message, UserDevice device ) throws IOException
  {
    return getSender().sendNoRetry( message, device.getRegistrationId() );
  }

      /* default */ Sender getSender()
  {
    String serverId = systemVariableService.getPropertyByName( SystemVariableService.GOOGLE_CLOUD_MESSAGING_SERVER_ID ).getStringVal();

    return new BeaconSender( serverId );
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

}