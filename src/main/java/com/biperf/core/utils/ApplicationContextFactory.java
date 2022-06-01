/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/ApplicationContextFactory.java,v $
 */

package com.biperf.core.utils;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.biperf.awardslinqDataRetriever.delegate.AwardslinqFeaturedItemsDelegate;
import com.biperf.awardslinqDataRetriever.delegate.AwardslinqMyGoalDelegate;
import com.biperf.awardslinqDataRetriever.delegate.MerchlinqLevelDelegate;
import com.biperf.cache.oscache.ManageableCacheAdministrator;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;

/**
 * ApplicationContextFactory.
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
 * <td>jdunne</td>
 * <td>Feb 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApplicationContextFactory
{
  private static Log log = LogFactory.getLog( ApplicationContextFactory.class );

  private static Object configLocation = null;
  private static int count = 0;
  private static ApplicationContext applicationContext = null;
  private static ApplicationContext cmApplicationContext = null;
  private static ServletContext cmServletContext = null;

  /**
   * Initialize the application context.
   * 
   * @param configLocations a comma delimited string with all of your config files
   */
  public static void init( Object configLocations )
  {

    if ( count > 0 )
    {
      log.error( "Can't initialize the application context twice: THIS SHOULD ONLY HAPPEN DURING TESTING" );
    }
    configLocation = configLocations;
    count++;
  }

  /**
   * Destroys the application context
   */
  public static void destroy()
  {
    count--;
    if ( count <= 0 )
    {
      log.info( "Start ApplicationContext destruction" );

      // clear the main application cache
      if ( applicationContext != null )
      {
        ManageableCacheAdministrator cacheAdmin = (ManageableCacheAdministrator)applicationContext.getBean( "cacheAdministrator" );
        cacheAdmin.destroy();
      }

      XmlWebApplicationContext ac = (XmlWebApplicationContext)applicationContext;

      log.info( "Destroying Application Context :" + ac );
      if ( ac != null )
      {
        ConfigurableListableBeanFactory bf = ac.getBeanFactory();
        bf.setParentBeanFactory( null );
        bf.destroySingletons();
        ac.setParent( null );
        ac.close();
        log.info( "Application Context Destroyed." );
      }

      // clear the CM application cache
      if ( cmApplicationContext != null )
      {
        ManageableCacheAdministrator cmCacheAdmin = (ManageableCacheAdministrator)cmApplicationContext.getBean( "cmsCacheAdministrator" );
        cmCacheAdmin.destroy();
      }

      XmlWebApplicationContext xwac = (XmlWebApplicationContext)cmApplicationContext;
      log.info( "Destroying CM Application Context :" + xwac );
      if ( xwac != null )
      {
        ConfigurableListableBeanFactory bf = xwac.getBeanFactory();
        bf.setParentBeanFactory( null );
        bf.destroySingletons();
        xwac.close();
      }

      AwardslinqMyGoalDelegate.shutdown();
      AwardslinqFeaturedItemsDelegate.shutdown();
      OMRemoteDelegate.shutdown();
      MerchlinqLevelDelegate.shutdown();
      log.info( "CM Application Context Destroyed" );

      configLocation = null;
      applicationContext = null;
      cmApplicationContext = null;
      cmServletContext = null;

      log.info( "Finish ApplicationContext destruction" );
    }
  }

  public static ApplicationContext getApplicationContext()
  {

    if ( applicationContext != null )
    {
      return applicationContext;
    }

    if ( configLocation == null )
    {
      throw new IllegalStateException( "Application context not initialized" );
    }
    else if ( configLocation instanceof String )
    {

      String config = (String)configLocation;
      if ( log.isInfoEnabled() )
      {
        log.info( "Start ApplicationContext initialization with configs: " + config );
      }
      if ( StringUtils.contains( config, "," ) )
      {

        String[] configs = org.springframework.util.StringUtils.tokenizeToStringArray( config, "," );
        try
        {
          applicationContext = new ClassPathXmlApplicationContext( configs );
          if ( log.isInfoEnabled() )
          {
            log.info( "Finish ApplicationContext initialization" );
          }
        }
        catch( BeansException e )
        {
          // Log the exception even though we are rethrowing it since when this method gets called
          // from a static context such as when unit testing, the exception detail is swallowed (at
          // least through the IDE)
          // by an ExceptionInInitializerError.
          log.error( e.getMessage(), e );

          // If underlying exception is due to a missing datasource config file, give caller extra
          // advice
          if ( e.getMessage() != null && e.getMessage().indexOf( "datasourceContext-test.xml" ) > 0 )
          {
            String message = "datasourceContext-test.xml missing.  You must first select your dev or local datasource by " + "running ant test-db-configure-dev or ant test-db-configure-local";
            log.error( message );
            throw new BeaconRuntimeException( message, e );
          }
          // If non datasource config file exception, just rethrow the same exception so caller can
          // respond.
          throw e;
        }
        return applicationContext;
      }

      applicationContext = new ClassPathXmlApplicationContext( config );
      if ( log.isInfoEnabled() )
      {
        log.info( "Finish ApplicationContext initialization" );
      }
      return applicationContext;
    }
    else
    {
      throw new IllegalStateException( "You must initialize the context with a String" );
    }

    // return applicationContext;

  }

  public static ApplicationContext getContentManagerApplicationContext()
  {

    if ( getApplicationContext() != null )
    {
      return getApplicationContext();
    }

    // If the WAC is null, then try to retrieve it from the servlet context
    if ( cmApplicationContext == null && cmServletContext != null )
    {
      ApplicationContext wac = null;
      String contextName = cmServletContext.getContextPath();
      ServletContext cmContext = cmServletContext.getContext( contextName + "-cm" );
      if ( cmContext != null )
      {
        wac = (ApplicationContext)cmContext.getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE );
        ApplicationContextFactory.setContentManagerApplicationContext( wac );
      }
    }
    return cmApplicationContext;

  }

  public static void setContentManagerApplicationContext( ApplicationContext applicationContext )
  {
    cmApplicationContext = applicationContext;
  }

  public static ServletContext getContentManagerServletContext()
  {
    return cmServletContext;
  }

  public static void setContentManagerServletContext( ServletContext servletContext )
  {
    cmServletContext = servletContext;
  }

  public static void setApplicationContext( ApplicationContext applicationContext )
  {
    ApplicationContextFactory.applicationContext = applicationContext;
  }

}
