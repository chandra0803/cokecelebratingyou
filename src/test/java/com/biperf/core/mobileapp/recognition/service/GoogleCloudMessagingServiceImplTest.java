
package com.biperf.core.mobileapp.recognition.service;

import static com.biperf.core.mobileapp.recognition.domain.DeviceType.ANDROID;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.biperf.core.google.android.gcm.server.Message;
import com.biperf.core.google.android.gcm.server.Result;
import com.biperf.core.google.android.gcm.server.Sender;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;

public class GoogleCloudMessagingServiceImplTest
{

  private GoogleCloudMessagingServiceImpl service;

  @Before
  public void setUp()
  {
    // use a subclass so we don't need to rely on the systemVariableService
    service = new GoogleCloudMessagingServiceImpl()
    {
      @Override
      Sender getSender()
      {
        return new Sender( "AIzaSyCFeTYAG35pv8gBqeS038h-Yv8HDc3qUxs" );
      }
    };
  }

  @Test
  public void doSomething()
  {
    assertTrue( true );
  }

  // @Test
  public void testSendToGoogleCloudMessaging() throws IOException
  {
    Message message = new Message.Builder().addData( "TheMessage", "Hello World!!!" ).build();

    UserDevice userDevice = new UserDevice( null,
                                            ANDROID,
                                            "APA91bFFkQU-vKaVEyxks_9AxZWdYTj085Oo2UM6hIN1E9p0IKnSv6dj0nBn1-rfxQF-v86yFZyUa6n3bPEqtPZfA5lIq7CSNGPk0nQ6azNSEK6hI1qiSsu41tg-R2G8HS7SbMZsHSgDXeHJ9TWgReM-L0U8i4WN7G2W8G8T0h7hNw5yVHh0NAjt1zUkCS3mqjg76COeLzhk",
                                            false );

    Result result = service.sendNoRetry( message, userDevice );

    System.out.println( "\n******************************************************" + "\nGCM RESULT" + "\n******************************************************" + "\nMessage ID: "
        + result.getMessageId() + "\nCanonical Reg ID: " + result.getCanonicalRegistrationId() + "\nError code: " + result.getErrorCodeName()
        + "\n******************************************************" + "\nEND RESULT" + "\n******************************************************" );

  }

}
