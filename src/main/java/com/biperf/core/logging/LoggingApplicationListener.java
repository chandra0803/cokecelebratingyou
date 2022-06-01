/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/logging/LoggingApplicationListener.java,v $
 */

package com.biperf.core.logging;

import java.net.URL;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ResourceManager;

/**
 * LoggingApplicationListener.
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
 * <td>Brian Repko</td>
 * <td>Nov 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoggingApplicationListener implements ServletContextListener
{

  private static final String DEFAULT_LOG4J_FILE = "log4j-config.xml";

  /**
   * Initialize Logging for application Overridden from
   * 
   * @Override
   */
  public void contextInitialized( ServletContextEvent servletContextEvent )
  {
    initLogging();
    ResourceManager.initResources();
  }

  /**
   * Shutdown Logging for application Overridden from
   * 
   * @Override
   */
  public void contextDestroyed( ServletContextEvent servletContextEvent )
  {
    destroyLogging();
    ResourceManager.destroyResources();
  }

  /**
   * Initializes the logging system
   */
  private void initLogging()
  {
    try
    {
      String url = Environment.getEnvironment() + "." + DEFAULT_LOG4J_FILE;
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      URL resource = loader.getResource( url );
      DOMConfigurator.configure( resource );
    }
    catch( FactoryConfigurationError t )
    {
      System.err.println( "FATAL: LOG4J configuration failed" );
      t.printStackTrace();
    }
  }

  /**
   * Destroys the logging system
   */
  private void destroyLogging()
  {
    LogManager.shutdown();
    LogFactory.releaseAll();
  }

}
