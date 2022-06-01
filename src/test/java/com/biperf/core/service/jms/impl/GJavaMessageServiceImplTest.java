
package com.biperf.core.service.jms.impl;

import org.easymock.classextension.EasyMock;

import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.utils.jms.HoneycombInitializationMessage;
import com.biperf.core.utils.jms.JmsTopicMessageListener;

public class GJavaMessageServiceImplTest extends BaseServiceTest
{

  private GJavaMessageServiceImpl gJavaMessageService;

  /** Mocks */
  private JmsTopicMessageListener jmsTopicMessageListenerMock;

  /**
   * Constructor to take the name of the test.
   */
  public GJavaMessageServiceImplTest( String test )
  {
    super( test );
  }

  protected void setUp() throws Exception
  {
    super.setUp();
    gJavaMessageService = new GJavaMessageServiceImpl();
    jmsTopicMessageListenerMock = EasyMock.createMock( JmsTopicMessageListener.class );
    gJavaMessageService.setJmsTopicMessageListener( jmsTopicMessageListenerMock );
  }

  public void testSendJsonNotification()
  {
    HoneycombInitializationMessage message = new HoneycombInitializationMessage();
    jmsTopicMessageListenerMock.sendNotification( message );
    EasyMock.expectLastCall().once();
    EasyMock.replay( jmsTopicMessageListenerMock );
    gJavaMessageService.sendToJmsTopic( message );
    EasyMock.verify( jmsTopicMessageListenerMock );
  }

}
