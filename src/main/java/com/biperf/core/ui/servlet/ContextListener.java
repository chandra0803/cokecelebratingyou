/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/ContextListener.java,v $
 */

package com.biperf.core.ui.servlet;

import java.beans.Introspector;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.CachedIntrospectionResults;

public class ContextListener implements ServletContextListener
{
  private static Log logger = LogFactory.getLog( ContextListener.class );

  public void contextInitialized( ServletContextEvent event )
  {
    // set the class loader for spring cache introspection
    CachedIntrospectionResults.acceptClassLoader( Thread.currentThread().getContextClassLoader() );
  }

  public void contextDestroyed( ServletContextEvent event )
  {
    cleanupThreadLocals( null, "cglib", Thread.currentThread().getContextClassLoader() );

    // clear the class loader from the spring cache introspection
    CachedIntrospectionResults.clearClassLoader( Thread.currentThread().getContextClassLoader() );

    // cleanup the reference to the JDBC driver manager
    cleanupJDBCDriverManager();

    // Flush all of the Introspector's internal caches
    Introspector.flushCaches();
  }

  private void cleanupThreadLocals( Thread thread, final String classFilter, final ClassLoader classLoader )
  {
    if ( classLoader != null )
    {
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "@@ cleanupThreadLocals for classLoader=" + classLoader.getClass().getName() + "@" + Integer.toHexString( classLoader.hashCode() ) );
      }
    }

    Thread[] threadList;
    if ( thread != null )
    {
      threadList = new Thread[1];
      threadList[0] = thread;
    }
    else
    {
      // Every thread
      threadList = new Thread[Thread.activeCount()];
      Thread.enumerate( threadList );
    }

    for ( int iThreadList = 0; iThreadList < threadList.length; iThreadList++ )
    {
      Thread t = threadList[iThreadList];

      Field field;
      try
      {
        Class cl;
        cl = t.getClass();
        while ( cl != null && cl != java.lang.Thread.class )
        {
          cl = cl.getSuperclass();
        }
        if ( cl != null )
        {
          field = cl.getDeclaredField( "threadLocals" );
          field.setAccessible( true );

          Object threadLocals = field.get( t );
          if ( threadLocals != null )
          {
            Field entries = threadLocals.getClass().getDeclaredField( "table" );
            entries.setAccessible( true );
            Object[] entryList = (Object[])entries.get( threadLocals );
            for ( int iEntry = 0; iEntry < entryList.length; iEntry++ )
            {
              if ( entryList[iEntry] != null )
              {
                Field fValue = entryList[iEntry].getClass().getDeclaredField( "value" );
                if ( fValue != null )
                {
                  fValue.setAccessible( true );
                  Object value = fValue.get( entryList[iEntry] );
                  if ( value != null )
                  {
                    boolean flag = true;
                    if ( logger.isDebugEnabled() )
                    {
                      logger.debug( "found entry: value=" + value.getClass().getName() + "@" + Integer.toHexString( value.hashCode() ) );
                    }
                    if ( classFilter != null && value.getClass().getName().indexOf( classFilter ) == -1 )
                    {
                      flag = false;
                    }
                    else
                    {
                      if ( value instanceof SoftReference )
                      {
                        SoftReference sf = (SoftReference)value;
                        Object referent = sf.get();
                        if ( referent != null )
                        {
                          if ( logger.isDebugEnabled() )
                          {
                            logger.debug( "@ Entry for " + value.getClass().getName() + " holding " + sf.get().getClass() + " will be cleared." );
                            logger.debug( "@ Object Held" + sf.get() );
                          }
                        }
                        if ( classLoader != null && value.getClass().getClassLoader() != classLoader )
                        {
                          if ( logger.isDebugEnabled() )
                          {
                            logger.debug( "@ Entry for " + value.getClass().getName() + " holding " + sf.get() + " will be cleared." );
                          }
                        }
                        sf.clear();
                      }
                    }

                    if ( flag )
                    {
                      if ( logger.isDebugEnabled() )
                      {
                        logger.debug( "@@ Entry for " + value.getClass().getName() + "@" + Integer.toHexString( value.hashCode() ) + " cleared." );
                      }
                      entryList[iEntry] = null;
                    }
                  }
                }
              }
            }
          }
          else
          {
            if ( logger.isDebugEnabled() )
            {
              logger.debug( "@@ no threadLocals for '" + t.getName() + "'" );
            }
          }
        }
      }
      catch( IllegalAccessException ex )
      {
        // Globals.reportException(ex);
      }
      catch( SecurityException ex )
      {
        // Globals.reportException(ex);
      }
      catch( NoSuchFieldException ex )
      {
        // Globals.reportException(ex);
        Field[] fields = t.getClass().getDeclaredFields();
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Fields available for " + t.getClass().getName() + ": " );
          for ( int ii = 0; ii < fields.length; ii++ )
          {
            System.out.println( fields[ii].getName() );
            logger.debug( fields[ii].getName() );
          }
          logger.debug( "<br />" );
        }

      }
    }
  }

  /**
   * De-register the JDBC driver manager
   */
  private void cleanupJDBCDriverManager()
  {
    try
    {
      for ( Enumeration enumerator = DriverManager.getDrivers(); enumerator.hasMoreElements(); )
      {
        Driver driver = (Driver)enumerator.nextElement();
        if ( driver.getClass().getClassLoader() == getClass().getClassLoader() )
        {
          DriverManager.deregisterDriver( driver );
        }
      }
    }
    catch( SQLException sqle )
    {
      System.err.println( "Failed to cleanup JDBC driver manager for the webapp" );
      sqle.printStackTrace();
    }
  }

}
