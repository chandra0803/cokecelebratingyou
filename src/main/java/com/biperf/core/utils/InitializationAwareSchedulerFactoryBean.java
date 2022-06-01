/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/InitializationAwareSchedulerFactoryBean.java,v $
 */

package com.biperf.core.utils;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
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
 * <td>wadzinsk</td>
 * <td>Dec 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class InitializationAwareSchedulerFactoryBean extends SchedulerFactoryBean
{
  private static final Log log = LogFactory.getLog( InitializationAwareSchedulerFactoryBean.class );
  private static final String QUARTZ_TABLE_PREFIX_PROPERTY = "org.quartz.jobStore.tablePrefix";
  private static final String DEV_QUARTZ_TABLE_PREFIX = "DEVQ_";

  /**
   * Sets the quartz properties. If the environment is not qa, preprod or prod, then set the table
   * prefix to "DEVQ_" instead of the default "QRTZ_".
   */
  public void setQuartzProperties( Properties quartzProperties )
  {
    if ( !Environment.isCtech() )
    {
      log.info( "Using development quartz tables" );
      // note: throws away anything in applicationContext-process.xml
      quartzProperties.put( QUARTZ_TABLE_PREFIX_PROPERTY, DEV_QUARTZ_TABLE_PREFIX );
    }
    else
    {
      log.info( "Using ctech quartz tables" );
    }
    super.setQuartzProperties( quartzProperties );
  }

  /**
   * Overridden from
   * 
   * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#startScheduler(org.quartz.Scheduler,
   *      int) If startupDelay value is negative, the scheduler startup will happen after the
   *      ApplicationContextFactory CM context is initialized, or when startupDelay seconds have
   *      gone by.
   * @param scheduler
   * @param startupDelay
   * @throws SchedulerException
   */
  protected void startScheduler( final Scheduler scheduler, final int startupDelay ) throws SchedulerException
  {
    if ( startupDelay < 0 )
    {
      final int positiveStartupDelay = -1 * startupDelay;

      logger.info( "Will start Quartz scheduler after ApplicationContextFactory CM Context is initialized or in " + positiveStartupDelay + " seconds" );
      new Thread()
      {
        public void run()
        {
          int secondCounter = 0;
          while ( !isFactoryInitialized() && secondCounter < positiveStartupDelay )
          {
            try
            {
              log.debug( secondCounter + ": Scheduler startup Waiting for ApplicationContextFactory CM Context Initialization or " + positiveStartupDelay + " seconds" );
              secondCounter++;
              Thread.sleep( 1000 );
            }
            catch( InterruptedException ex )
            {
              // Proceed with startup
              break;
            }
          }
          logger.info( "Starting Quartz scheduler now, after delay of " + secondCounter + " seconds waiting for CM Context Initialization" );
          try
          {
            scheduler.start();
          }
          catch( SchedulerException ex )
          {
            throw new DelayedSchedulerStartException( ex );
          }
        }

        private boolean isFactoryInitialized()
        {
          return true;// return ( ApplicationContextFactory.getContentManagerApplicationContext() !=
                      // null );
        }

      }.start();
    }
    else
    {
      // Use standard startup
      super.startScheduler( scheduler, startupDelay );
    }
  }

  /**
   * Exception to be thrown if the Quartz scheduler cannot be started after the specified delay has
   * passed.
   */
  public static class DelayedSchedulerStartException extends NestedRuntimeException
  {
    private DelayedSchedulerStartException( SchedulerException ex )
    {
      super( "Could not start Quartz scheduler after delay", ex );
    }
  }

}
