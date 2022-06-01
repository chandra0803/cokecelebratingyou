package com.biperf.core.utils.jms;

import java.io.Serializable;

import javax.jms.JMSException;

/**
 * Implement this interface for JMS messages being sent as JSON text rather than through Java serialization
 */
public interface JsonJmsMessage extends Serializable
{
  
  /** Called when the message is received */
  void processMessage() throws JMSException;

}
