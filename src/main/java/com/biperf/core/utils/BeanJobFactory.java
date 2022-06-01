/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import com.biperf.core.process.ProcessBeanJob;

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
 * <td>Nov 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BeanJobFactory implements JobFactory
{
  private static final Log log = LogFactory.getLog( BeanJobFactory.class );

  public Job newJob( TriggerFiredBundle bundle ) throws SchedulerException
  {
    String processBeanJobName = ProcessBeanJob.BEAN_NAME;

    try
    {
      if ( log.isInfoEnabled() )
      {
        String recoveredJobStatus = bundle.isRecovering() ? " recovered " : " ";
        log.info( "Producing" + recoveredJobStatus + "instance of Job " + bundle.getTrigger().getJobDataMap().get( "processBeanName" ) );
      }
      return (Job)BeanLocator.getBean( processBeanJobName );
    }
    catch( Exception e )
    {
      throw new SchedulerException( "Problem instantiating bean: " + processBeanJobName, e );
    }
  }

  @Override
  public Job newJob( TriggerFiredBundle bundle, Scheduler scheduler ) throws SchedulerException
  {
    return newJob( bundle );
  }

}
