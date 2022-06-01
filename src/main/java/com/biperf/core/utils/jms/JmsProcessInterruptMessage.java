
package com.biperf.core.utils.jms;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import com.biperf.core.service.process.ProcessService;
import com.biperf.core.utils.BeanLocator;

@SuppressWarnings( "serial" )
public class JmsProcessInterruptMessage extends JmsMessage
{
  private static final Logger logger = Logger.getLogger( JmsProcessInterruptMessage.class );

  private static final String PROCESS_ID = "PROCESS_ID";
  private static final String ALL_PROCESSES = "ALL_PROCESSES";

  public void processMessage() throws JMSException
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Entering processMessage()" );
    }
    if ( isInterruptAllProcesses() )
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Interrupting ALL scheduled processes" );
      }
      getProcessService().interruptAllProcesses();
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Successfully interrupted ALL scheduled processes" );
      }
    }
    else
    {
      Long processId = getProcessId();
      if ( null != processId )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Interrupting process with ID : " + processId );
        }
        getProcessService().interruptProcess( processId );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Successfully interrupted process with ID : " + processId );
        }
      }
    }
    // temp logging
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Leaving processMessage()" );
    }
  }

  private boolean isInterruptAllProcesses()
  {
    Boolean value = (Boolean)getParameterValue( ALL_PROCESSES );
    return null != value && value.booleanValue();
  }

  private Long getProcessId()
  {
    return (Long)getParameterValue( PROCESS_ID );
  }

  public void setInterruptAllProcesses( Boolean value )
  {
    addParameter( ALL_PROCESSES, value );
  }

  public void setProcessId( Long processId )
  {
    addParameter( PROCESS_ID, processId );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }
}
