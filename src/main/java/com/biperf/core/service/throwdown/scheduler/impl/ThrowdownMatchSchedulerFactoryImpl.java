
package com.biperf.core.service.throwdown.scheduler.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.throwdown.scheduler.ThrowdownMatchScheduler;
import com.biperf.core.service.throwdown.scheduler.ThrowdownMatchSchedulerFactory;

public class ThrowdownMatchSchedulerFactoryImpl implements ThrowdownMatchSchedulerFactory
{
  private static final Log log = LogFactory.getLog( ThrowdownMatchSchedulerFactory.class );
  private Map<String, ThrowdownMatchScheduler> entries;

  public void setEntries( Map<String, ThrowdownMatchScheduler> entries )
  {
    this.entries = entries;
  }

  public ThrowdownMatchScheduler getThrowdownMatchSchedulerService()
  {
    ThrowdownMatchScheduler throwdownMatchScheduler = (ThrowdownMatchScheduler)entries.get( ThrowdownMatchSchedulerFactory.RANDOM );
    if ( throwdownMatchScheduler == null )
    {
      throw new BeaconRuntimeException( "Unable to get ThrowdownMatchScheduler Implementation." );
    }

    return throwdownMatchScheduler;
  }

}
