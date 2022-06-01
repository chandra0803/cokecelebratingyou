
package com.biperf.core.mobileapp.recognition.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.biperf.core.google.android.gcm.server.Sender;
import com.biperf.core.utils.Environment;

public class BeaconSender extends Sender
{
  public BeaconSender( String key )
  {
    super( key );
  }

  @Override
  protected HttpURLConnection getConnection( String url ) throws IOException
  {
    HttpURLConnection conn = (HttpURLConnection)new URL( url ).openConnection( Environment.buildProxy() );
    return conn;
  }
}