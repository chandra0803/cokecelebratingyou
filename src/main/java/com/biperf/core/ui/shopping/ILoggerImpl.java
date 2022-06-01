/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/shopping/ILoggerImpl.java,v $
 */

package com.biperf.core.ui.shopping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.util.log.ILogger;

/**
 * ILoggerImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>robinsra</td>
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ILoggerImpl implements ILogger
{

  private static final Log logger = LogFactory.getLog( ILoggerImpl.class );

  public ILoggerImpl()
  {
    super();
  }

  /**
   * Log a message object with the {@link org.apache.log4j.Priority#FATAL FATAL} priority.
   * <p>
   * This method first checks if this logger is <code>FATAL</code> enabled by comparing the
   * priority of this logger with the {@link org.apache.log4j.Priority#FATAL FATAL} priority. If
   * this logger is <code>ERROR</code> enabled, then it converts the message object (passed as
   * parameter) to a string by invoking the appropriate {@link org.apache.log4j.or.ObjectRenderer}.
   * It then proceeds to call all the registered appenders in this logger and also higher in the
   * hierarchy depending on the value of the additivity flag.
   * <p>
   * <b>WARNING</b> Note that passing a {@link Throwable} to this method will only print the stack
   * trace if the the priority of this logger is set at the
   * {@link org.apache.log4j.Priority#DEBUG DEBUG} priority.
   * 
   * @param message the message object to log.
   * @param t the exception to log, including its stack trace.
   */
  public void fatal( Object message, Throwable t )
  {
    logger.fatal( message, t );
  }

  /**
   * Log a message object with the {@link org.apache.log4j.Priority#ERROR ERROR} priority.
   * <p>
   * This method first checks if this logger is <code>ERROR</code> enabled by comparing the
   * priority of this logger with the {@link org.apache.log4j.Priority#ERROR ERROR} priority. If
   * this logger is <code>ERROR</code> enabled, then it converts the message object (passed as
   * parameter) to a string by invoking the appropriate {@link org.apache.log4j.or.ObjectRenderer}.
   * It then proceeds to call all the registered appenders in this logger and also higher in the
   * hierarchy depending on the value of the additivity flag.
   * <p>
   * <b>WARNING</b> Note that passing a {@link Throwable} to this method will only print the stack
   * trace if the the priority of this logger is set at the
   * {@link org.apache.log4j.Priority#DEBUG DEBUG} priority.
   * 
   * @param message the message object to log.
   * @param t the exception to log, including its stack trace.
   */
  public void error( Object message, Throwable t )
  {
    logger.error( message, t );
  }

  public void error( Object message )
  {
    logger.error( message );
  }

  /**
   * Log a message object with the {@link org.apache.log4j.Priority#WARN WARN} priority.
   * <p>
   * This method first checks if this logger is <code>WARN</code> enabled by comparing the
   * priority of this logger with the {@link org.apache.log4j.Priority#WARN WARN} priority. If this
   * logger is <code>WARN</code> enabled, then it converts the message object (passed as
   * parameter) to a string by invoking the appropriate {@link org.apache.log4j.or.ObjectRenderer}.
   * It then proceeds to call all the registered appenders in this logger and also higher in the
   * hierarchy depending on the value of the additivity flag.
   * <p>
   * <b>WARNING</b> Note that passing a {@link Throwable} to this method will print the name of the
   * <code>Throwable</code> but no stack trace. To print a stack trace use the
   * {@link #warn(Object, Throwable)} form instead.
   * 
   * @param message the message object to log.
   */
  public void warn( Object message )
  {
    logger.warn( message );
  }

  /**
   * Log a message object with the <code>WARN</code> priority including the stack trace of the
   * {@link Throwable} <code>t</code> passed as parameter.
   * <p>
   * See {@link #warn(Object)} form for more detailed information.
   * 
   * @param message the message object to log.
   * @param t the exception to log, including its stack trace.
   */
  public void warn( Object message, Throwable t )
  {
    logger.warn( message, t );
  }

  /**
   * Log a message object with the {@link org.apache.log4j.Priority#INFO INFO} priority.
   * <p>
   * This method first checks if this logger is <code>INFO</code> enabled by comparing the
   * priority of this logger with the {@link org.apache.log4j.Priority#INFO INFO} priority. If this
   * logger is <code>INFO</code> enabled, then it converts the message object (passed as
   * parameter) to a string by invoking the appropriate {@link org.apache.log4j.or.ObjectRenderer}.
   * It then proceeds to call all the registered appenders in this logger and also higher in the
   * hierarchy depending on the value of the additivity flag.
   * <p>
   * <b>WARNING</b> Note that passing a {@link Throwable} to this method will print the name of the
   * <code>Throwable</code> but no stack trace. To print a stack trace use the
   * {@link #info(Object, Throwable)} form instead.
   * 
   * @param message the message object to log.
   */
  public void info( Object message )
  {
    logger.info( message );
  }

  /**
   * Log a message object with the <code>INFO</code> priority including the stack trace of the
   * {@link Throwable} <code>t</code> passed as parameter.
   * <p>
   * See {@link #info(Object)} form for more detailed information.
   * 
   * @param message the message object to log.
   * @param t the exception to log, including its stack trace.
   */
  public void info( Object message, Throwable t )
  {
    logger.error( message, t );
  }

  public boolean isDebugEnabled()
  {
    return true;
  }

  public boolean isInfoEnabled()
  {
    return true;
  }

  public String getName()
  {
    return "Beacon";
  }

  /**
   * Log a message object with the {@link org.apache.log4j.Priority#DEBUG DEBUG} priority.
   * <p>
   * This method first checks if this logger is <code>DEBUG</code> enabled by comparing the
   * priority of this logger with the {@link org.apache.log4j.Priority#DEBUG DEBUG} priority. If
   * this logger is <code>DEBUG</code> enabled, then it converts the message object (passed as
   * parameter) to a string by invoking the appropriate {@link org.apache.log4j.or.ObjectRenderer}.
   * It then proceeds to call all the registered appenders in this logger and also higher in the
   * hierarchy depending on the value of the additivity flag.
   * <p>
   * <b>WARNING</b> Note that passing a {@link Throwable} to this method will print the name of the
   * <code>Throwable</code> but no stack trace. To print a stack trace use the
   * {@link #debug(Object, Throwable)} form instead.
   * 
   * @param message the message object to log.
   */
  public void debug( Object message )
  {
    logger.debug( message );
  }

  /**
   * Log a message object with the <code>DEBUG</code> priority including the stack trace of the
   * {@link Throwable} <code>t</code> passed as parameter.
   * <p>
   * See {@link #debug(Object)} form for more detailed information.
   * 
   * @param message the message object to log.
   * @param t the exception to log, including its stack trace.
   */
  public void debug( Object message, Throwable t )
  {
    logger.debug( message, t );
  }
}
