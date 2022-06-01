
package com.biperf.core.utils.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsQueueMessageListener implements MessageListener
{
  private static Logger log = Logger.getLogger( JmsQueueMessageListener.class );

  private static final String NODE_NAME_PARAM = "nodeName";

  private JmsTemplate jmsTemplate;
  private Queue queue;
  private String nodeName;

  public void setJmsTemplate( JmsTemplate jmsTemplate )
  {
    this.jmsTemplate = jmsTemplate;
  }

  public void setQueue( Queue queue )
  {
    this.queue = queue;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public JmsQueueMessageListener()
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "Constructor for " + getClass() );
    }
  }

  public void onMessage( Message message )
  {
    try
    {
      // check the message type
      ObjectMessage objectMessage = null;

      if ( ! ( message instanceof ObjectMessage ) )
      {
        log.error( "Cannot handle message of type (class=" + message.getClass().getName() + "). Notification ignored." );
        return;
      }

      objectMessage = (ObjectMessage)message;

      // check the message content
      if ( ! ( objectMessage.getObject() instanceof JmsMessage ) )
      {
        log.error( "An unknown cluster notification message received (class=" + objectMessage.getObject().getClass().getName() + "). Notification ignored." );
        return;
      }

      if ( log.isDebugEnabled() )
      {
        log.debug( "objectMessage.getObject = " + objectMessage.getObject() + ", JMSMessageID=" + objectMessage.getJMSMessageID() );
      }

      String incomingNodeName = objectMessage.getStringProperty( NODE_NAME_PARAM );
      if ( log.isDebugEnabled() )
      {
        log.debug( "MessageListener-onMessage - incomingNodeName=" + incomingNodeName + ", JMSMessageID=" + objectMessage.getJMSMessageID() );
      }

      // Handle requests from the same application
      if ( !incomingNodeName.equals( nodeName ) )
      {
        // now handle the message
        if ( log.isDebugEnabled() )
        {
          log.debug( "Handling the message. objectMessage-JMSMessageID=" + objectMessage.getJMSMessageID() );
        }
        JmsMessage jmsMessage = (JmsMessage)objectMessage.getObject();
        jmsMessage.processMessage();
      }
    }
    catch( NullPointerException npe )
    {
      // ignore this exception. If this is a flushGroup and the group is not defined - we get a
      // NullPointerException. Do not log anything in this case.
    }
    catch( Exception jmsEx )
    {
      log.error( "Cannot handle cluster Notification", jmsEx );
    }
  }

  public void sendNotification( final JmsMessage message )
  {
    if ( jmsTemplate != null )
    {
      // JMS sender
      jmsTemplate.send( queue, new MessageCreator()
      {
        public Message createMessage( Session session ) throws JMSException
        {
          ObjectMessage objectMessage = session.createObjectMessage();
          objectMessage.setObject( message );
          objectMessage.setStringProperty( NODE_NAME_PARAM, nodeName );
          if ( log.isDebugEnabled() )
          {
            log.debug( "CreateMessage - objectMessage=" + objectMessage.getObject() );
          }
          return objectMessage;
        }
      } );
    }
    else
    {
      log.warn( "Send To JMS Queue invoked but not turned on" );
    }
  }

}
