
package com.biperf.core.utils.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JmsTopicMessageListener implements MessageListener
{
  private static Logger log = Logger.getLogger( JmsTopicMessageListener.class );

  private static final String APPLICATION_NAME_PARAM = "applicationName";
  private static final String CLASS_NAME_PARAM = "className";
  
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private JmsTemplate jmsTemplate;
  private Topic topic;
  private String applicationName;

  public void setJmsTemplate( JmsTemplate jmsTemplate )
  {
    this.jmsTemplate = jmsTemplate;
  }

  public void setTopic( Topic topic )
  {
    this.topic = topic;
  }

  public void setApplicationName( String applicationName )
  {
    this.applicationName = applicationName;
  }

  public JmsTopicMessageListener()
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
      String incomingApplicationName = message.getStringProperty( APPLICATION_NAME_PARAM );
      if ( log.isDebugEnabled() )
      {
        log.debug( "MessageListener-onMessage - incomingApplicationName=" + incomingApplicationName + ", JMSMessageID=" + message.getJMSMessageID() );
      }

      // Handle requests from the same application
      if ( incomingApplicationName.equals( applicationName ) )
      {
        if ( message instanceof ObjectMessage )
        {
          handleObjectMessage( message );
        }
        else if ( message instanceof TextMessage )
        {
          handleTextMessage( message );
        }
        else
        {
          log.error( "Cannot handle message of type (class=" + message.getClass().getName() + "). Notification ignored." );
          return;
        }
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
  
  private void handleObjectMessage( Message message ) throws JMSException
  {
    ObjectMessage objectMessage = (ObjectMessage)message;

    // check the message content
    if ( ! ( objectMessage.getObject() instanceof JmsMessage ) )
    {
      log.error( "An unknown cluster notification message received (class=" + objectMessage.getObject().getClass().getName() + "). Notification ignored." );
      return;
    }

    // now handle the message
    if ( log.isDebugEnabled() )
    {
      log.debug( "Handling objectMessage.getObject = " + objectMessage.getObject() + ", JMSMessageID=" + objectMessage.getJMSMessageID() );
    }
    JmsMessage jmsMessage = (JmsMessage)objectMessage.getObject();
    jmsMessage.processMessage();
  }

  private void handleTextMessage( Message message ) throws JMSException, IOException, JsonParseException, JsonMappingException
  {
    TextMessage textMessage = (TextMessage)message;

    // Need to know class name and have that class available to deserialize
    String className = textMessage.getStringProperty( CLASS_NAME_PARAM );
    if ( className == null )
    {
      log.error( "Text JMS message does not contain class name parameter" );
      return;
    }
    Class<?> clazz;
    try
    {
      clazz = Class.forName( className );
    }
    catch( Exception e )
    {
      log.error( "Text JMS message class could not be loaded. className=" + className );
      return;
    }

    if ( log.isDebugEnabled() )
    {
      log.debug( "Handling TextMessage. text=" + textMessage.getText() + ", JMSMessageID=" + textMessage.getJMSMessageID() );
    }
    JsonJmsMessage jsonJmsMessage = (JsonJmsMessage)objectMapper.readValue( textMessage.getText(), clazz );
    jsonJmsMessage.processMessage();
  }

  public void sendNotification( final JsonJmsMessage message )
  {
    if ( jmsTemplate != null )
    {
      // JMS sender
      jmsTemplate.send( topic, new MessageCreator()
      {
        public Message createMessage( Session session ) throws JMSException
        {
          try
          {
            String json = objectMapper.writeValueAsString( message );
            TextMessage textMessage = session.createTextMessage( json );
            textMessage.setStringProperty( CLASS_NAME_PARAM, message.getClass().getName() );
            textMessage.setStringProperty( APPLICATION_NAME_PARAM, applicationName );

            if ( log.isDebugEnabled() )
            {
              log.debug( "CreateMessage - textMessage=" + textMessage.getText() );
            }
            return textMessage;
          }
          catch( JsonProcessingException e )
          {
            log.error( "Exception sending JMS message as JSON", e );
            throw new JMSException( "Exception sending JMS message as JSON" );
          }
        }
      } );
    }
    else
    {
      log.warn( "Send To JMS Topic invoked but not turned on" );
    }
  }

  public void sendNotification( final JmsMessage message )
  {
    if ( jmsTemplate != null )
    {
      // JMS sender
      jmsTemplate.send( topic, new MessageCreator()
      {
        public Message createMessage( Session session ) throws JMSException
        {
          ObjectMessage objectMessage = session.createObjectMessage();
          objectMessage.setObject( message );
          objectMessage.setStringProperty( APPLICATION_NAME_PARAM, applicationName );
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
      log.warn( "Send To JMS Topic invoked but not turned on" );
    }
  }

}
