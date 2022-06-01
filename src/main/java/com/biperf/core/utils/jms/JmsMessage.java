
package com.biperf.core.utils.jms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

@SuppressWarnings( "serial" )
public abstract class JmsMessage implements Serializable
{
  private Map<Object, Object> parameters = new HashMap<Object, Object>();

  public void addParameter( Object key, Object value )
  {
    parameters.put( key, value );
  }

  public Object getParameterValue( Object key )
  {
    return parameters.get( key );
  }

  public abstract void processMessage() throws JMSException;

}
