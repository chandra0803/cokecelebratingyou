/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/mobileapp/pushNotifications/ios/TestIOSPushNotification.java,v $
 */

package com.biperf.core.mobileapp.pushNotifications.ios;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.mobileapp.recognition.service.RecognitionNotification.ParameterKeys;

import javapns.Push;
import javapns.communication.ProxyManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import junit.framework.TestCase;

public class TestIOSPushNotification extends TestCase
{
  private static final String PROXY_HOST_VALUE = "appproxy.biperf.com";
  private static final String PROXY_PORT_VALUE = "8080";

  public static void testIOSPushNotification()
  {
    try
    {
      ProxyManager.setProxy( PROXY_HOST_VALUE, PROXY_PORT_VALUE );

      PushNotificationPayload pl1 = new PushNotificationPayload();
      pl1.addCustomAlertBody( "You got recognized!" );
      pl1.addBadge( 1 );
      pl1.addSound( "default" );

      pl1.addCustomDictionary( ParameterKeys.RECOGNITION_ID.toString(), "12345" );
      pl1.addCustomDictionary( ParameterKeys.LANDING_SCREEN.toString(), "RECOGNITION_DETAIL" );

      // String json = "{\"aps\":{\"sound\":\"default\",\"alert\":\"Test Push
      // Notifications\",\"badge\":1}}";
      // Payload pl1 = new Payload( json )
      // {

      // };

      // String json2 =
      // "{\"aps\":{\"sound\":\"default\",\"content-available\":\"1\",\"alert\":\"Test Push
      // Notifications\",\"badge\":1}}";
      // Payload pl2 = new Payload( json2 )
      // {

      // };

      // List<PayloadPerDevice> payloadDevicePairs = new ArrayList();
      // PayloadPerDevice ppd1 = new PayloadPerDevice(pl2,
      // "2e7c15d1d4b45df2c0d72e9d4347b05f92e984e5921e0f63c9b8218c69e935d7");
      // payloadDevicePairs.add( ppd1 );

      InputStream input = new FileInputStream( new File( "env\\pushcertificateProd.p12" ) );

      // byte[] bytes = new
      // UrlReader().asBytes("http://beaconpprd.biworldwide.com/recognitionApp_redirect/certs/iosPushcertificateProd.p12");
      // InputStream input = new ByteArrayInputStream(bytes);

      List<String> deviceTokenList = new ArrayList();

      // deviceTokenList.add( "ce20ee67474a34c689ac9760ee06ac110961de0d444c450c237500b71d480c37" );
      // deviceTokenList.add( "91414abd19736259a44d1c1ddd4f388df68ee3d47008e552b07c807fd707c2d1"
      // );//jason dev
      deviceTokenList.add( "1534315ff0dab9410640d3980f7a0836ea84f585ed85f106f25d662dd6ffb6b6" );// jason
                                                                                                // prod

      // List<PushedNotification> notifications = Push.alert( "Hello World!", input, "password",
      // false, "5f90e2a9c5ab25128121b29452d01d6a1fdacad3c9fe49e2ec001178dd17efc3" );
      PushedNotifications notification1 = Push.payload( pl1, input, "password", true, deviceTokenList );
      // PushedNotifications notification1 = Push.payloads( input, "password", false,
      // payloadDevicePairs );
      for ( PushedNotification notification : notification1 )
      {
        if ( notification.isSuccessful() )
        {
          System.out.println( "Push notification sent successfully to: " + notification.getDevice().getToken() );
        }
        else
        {
          String invalidToken = notification.getDevice().getToken();
          /* Add code here to remove invalidToken from your database */

          /* Find out more about what the problem was */
          Exception theProblem = notification.getException();
          theProblem.printStackTrace();

          /* If the problem was an error-response packet returned by Apple, get it */
          ResponsePacket theErrorResponse = notification.getResponse();
          if ( theErrorResponse != null )
          {
            System.out.println( theErrorResponse.getMessage() );
          }
        }
      }
    }
    catch( Exception ex )
    {
      System.out.println( "There was a problem " + ex );
    }

  }

}