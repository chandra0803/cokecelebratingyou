/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.transaction.support.TransactionSynchronization;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.exception.BeaconRuntimeException;

/**
 * Avoids quartz db row lock timeouts by moving specific quartz scheduler calls to just before
 * commit gets called by the TX manager. For usage, see
 * {@link com.biperf.core.service.process.impl.ProcessServiceImpl#scheduleProcess(Process, ProcessSchedule, Map, Long)}
 * <br/> Prior to using this technique, scheuler calls were being made part of a larger TX and a
 * Quartz lock would be made in the middle of the TX, so any other call to schedule a process would
 * block and would occasionally timeout. We need to have the scheduler calls part of the service's
 * TX so that we don't attempt to start a process against data that hasn't yet been committed.
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
 * <td>Jan 17, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuartzScheduleJobTransactionSynchronization implements TransactionSynchronization
{
  private Scheduler scheduler;
  private JobDetail job;
  private List triggers = new ArrayList();

  /**
   * @param scheduler
   * @param job
   * @param trigger initial trigger to add
   */
  public QuartzScheduleJobTransactionSynchronization( Scheduler scheduler, JobDetail job, Trigger trigger )
  {
    super();
    this.scheduler = scheduler;
    this.job = job;
    this.triggers.add( trigger );
  }

  /**
   * NOP implementation. Overridden from
   * 
   * @see org.springframework.transaction.support.TransactionSynchronization#suspend()
   */
  public void suspend()
  {
    // nothing to do
  }

  /**
   * NOP implementation. Overridden from
   * 
   * @see org.springframework.transaction.support.TransactionSynchronization#resume()
   */
  public void resume()
  {
    // nothing to do
  }

  /**
   * Overridden from
   * 
   * @see org.springframework.transaction.support.TransactionSynchronization#beforeCommit(boolean)
   * @param readOnly
   */
  public void beforeCommit( boolean readOnly )
  {
    try
    {
      // Make sure the job exists
      try
      {
        scheduler.addJob( job, false );
      }
      catch( ObjectAlreadyExistsException e )
      {
        // nop
      }

      // Schedule the job for all triggers
      for ( Iterator i = triggers.iterator(); i.hasNext(); )
      {
        Trigger trigger = (Trigger)i.next();
        //
        // if simple one time trigger in past, then reset start time
        //
        if ( trigger instanceof SimpleTrigger && ( (SimpleTrigger)trigger ).getRepeatCount() == 0 )
        {
          Date startTime = trigger.getStartTime();
          Date now = new Date();
          if ( startTime.before( now ) )
          {
            //
            // To avoid unusual cluster misfire issue (which slows processing) by putting job
            // sufficiently in the future (30sec) so a cluster member sees the job before its start
            // time.
            //
            Date clusterDelay = new Date( new Date().getTime() + 30 * 1000 );
            trigger.getTriggerBuilder().startAt( clusterDelay );
          }
        }
        scheduler.scheduleJob( trigger );
      }
    }
    catch( SchedulerException e )
    {
      throw new BeaconRuntimeException( "SchedulerException scheduling process", e );
    }
  }

  /**
   * NOP implementation. Overridden from
   * 
   * @see org.springframework.transaction.support.TransactionSynchronization#beforeCompletion()
   */
  public void beforeCompletion()
  {
    // nothing to do
  }

  /**
   * NOP implementation. Overridden from
   * 
   * @see org.springframework.transaction.support.TransactionSynchronization#afterCompletion(int)
   * @param status
   */
  public void afterCompletion( int status )
  {
    // nothing to do
  }

  public JobDetail getJob()
  {
    return job;
  }

  public void setJob( JobDetail job )
  {
    this.job = job;
  }

  public void setScheduler( Scheduler scheduler )
  {
    this.scheduler = scheduler;
  }

  public void addTrigger( Trigger trigger )
  {
    this.triggers.add( trigger );
  }

  @Override
  public void afterCommit()
  {
  }

  @Override
  public void flush()
  {
  }
}
