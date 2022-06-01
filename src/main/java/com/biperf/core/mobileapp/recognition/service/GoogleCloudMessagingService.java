
package com.biperf.core.mobileapp.recognition.service;

import java.io.IOException;

import com.biperf.core.google.android.gcm.server.Message;
import com.biperf.core.google.android.gcm.server.Result;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;

public interface GoogleCloudMessagingService
{
  public Result sendNoRetry( Message message, UserDevice device ) throws IOException;
}