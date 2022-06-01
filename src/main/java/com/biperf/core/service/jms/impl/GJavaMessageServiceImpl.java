
package com.biperf.core.service.jms.impl;

import org.apache.log4j.Logger;

import com.biperf.core.service.jms.GJavaMessageService;
import com.biperf.core.utils.jms.JmsMessage;
import com.biperf.core.utils.jms.JmsQueueMessageListener;
import com.biperf.core.utils.jms.JmsTopicMessageListener;
import com.biperf.core.utils.jms.JsonJmsMessage;

public class GJavaMessageServiceImpl implements GJavaMessageService
{
  private static final Logger log = Logger.getLogger( GJavaMessageServiceImpl.class );

  private JmsTopicMessageListener jmsTopicMessageListener;
  private JmsQueueMessageListener jmsQueueMessageListener;

  public void setJmsTopicMessageListener( JmsTopicMessageListener jmsTopicMessageListener )
  {
    this.jmsTopicMessageListener = jmsTopicMessageListener;
  }

  public void setJmsQueueMessageListener( JmsQueueMessageListener jmsQueueMessageListener )
  {
    this.jmsQueueMessageListener = jmsQueueMessageListener;
  }

  public void sendToJmsQueue( JmsMessage message )
  {
    // JMS sender
    if ( jmsQueueMessageListener != null )
    {
      jmsQueueMessageListener.sendNotification( message );
    }
    else
    {
      log.warn( "Send To JMS Queue invoked but not turned on" );
    }
  }

  public void sendToJmsTopic( final JmsMessage message )
  {
    if ( jmsTopicMessageListener != null )
    {
      // JMS sender
      jmsTopicMessageListener.sendNotification( message );
    }
    else
    {
      log.warn( "Send To JMS Topic invoked but not turned on" );
    }
  }
  
  @Override
  public void sendToJmsTopic( final JsonJmsMessage message )
  {
    if ( jmsTopicMessageListener != null )
    {
      // JMS sender
      jmsTopicMessageListener.sendNotification( message );
    }
    else
    {
      log.warn( "Send To JMS Topic invoked but not turned on" );
    }
  }

}
