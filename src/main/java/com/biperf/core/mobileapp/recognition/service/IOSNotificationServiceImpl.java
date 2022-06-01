
package com.biperf.core.mobileapp.recognition.service;

import java.util.List;

import com.biperf.core.utils.Environment;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.Devices;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.Payload;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;

public class IOSNotificationServiceImpl implements IOSNotificationService
{

  @Override
  public PushedNotifications payload( Payload payload, Object keystore, String password, boolean production, Object devices ) throws CommunicationException, KeystoreException
  {
    PushedNotifications notifications = new PushedNotifications();
    if ( payload == null )
    {
      return notifications;
    }
    PushNotificationManager pushManager = new PushNotificationManager();
    try
    {
      AppleNotificationServer server = new G5AppleNotificationServer( keystore, password, production );
      pushManager.initializeConnection( server );
      List<Device> deviceList = Devices.asDevices( devices );
      notifications.setMaxRetained( deviceList.size() );
      for ( Device device : deviceList )
      {
        try
        {
          BasicDevice.validateTokenFormat( device.getToken() );
          PushedNotification notification = pushManager.sendNotification( device, payload, false );
          notifications.add( notification );
        }
        catch( InvalidDeviceTokenFormatException e )
        {
          notifications.add( new PushedNotification( device, payload, e ) );
        }
      }
    }
    finally
    {
      try
      {
        pushManager.stopConnection();
      }
      catch( Exception e )
      {
      }
    }
    return notifications;
  }

  private static class G5AppleNotificationServer extends AppleNotificationServerBasicImpl
  {

    public G5AppleNotificationServer( Object keystore, String password, String type, boolean production ) throws KeystoreException
    {
      super( keystore, password, type, production );
    }

    private G5AppleNotificationServer( Object keystore, String password, boolean production ) throws KeystoreException
    {
      super( keystore, password, production );
    }

    @Override
    public int getProxyPort()
    {
      return Environment.getProxyPort();
    }

    @Override
    public String getProxyHost()
    {
      return Environment.getProxy();
    }
  }
}
