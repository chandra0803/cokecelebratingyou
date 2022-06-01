/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/util/QuartzProcessUtil.java,v $
 */

package com.biperf.core.service.util;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.CronScheduleBuilder.monthlyOnDayAndHourAndMinute;
import static org.quartz.CronScheduleBuilder.weeklyOnDayAndHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.biperf.core.domain.enums.DayOfMonthType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.DateUtils;

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
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuartzProcessUtil
{
  private static final int REQUIRED_CRON_TERMS = 6;

  /**
   * protected since util class and no one should instantiate, just call static methods.
   */
  protected QuartzProcessUtil()
  {
    super();
  }

  /**
   * Build a trigger, setting the trigger schedule based on processSchedule.
   */
  public static Trigger buildTrigger( ProcessSchedule processSchedule, JobDetail job, String triggerName, String triggerGroupName, String jobGroupName )
  {
    TriggerBuilder triggerBuilder = null;

    String processFrequencyCode = processSchedule.getProcessFrequencyType().getCode();
    // cron-based jobs don't rely on hour of day or minute of day attributee..
    if ( processFrequencyCode.equalsIgnoreCase( ProcessFrequencyType.CRON_EXPRESSION ) )
    {
      try
      {
        triggerBuilder = newTrigger().withDescription( ProcessFrequencyType.CRON_EXPRESSION ).withSchedule( cronSchedule( processSchedule.getCronExpression() ) )
            .startAt( processSchedule.getStartDate() ).forJob( job );
      }
      catch( ParseException e )
      {
        throw new BeaconRuntimeException( "Invalid Cron Expression: " + processSchedule.getCronExpression(), e );
      }
    }
    else // standard time-based schedule
    {
      int hourOfDay = toHourOfDay( processSchedule.getTimeOfDayMillis() );
      int minuteOfDay = toMinuteOfDay( processSchedule.getTimeOfDayMillis() );
      if ( processFrequencyCode.equals( ProcessFrequencyType.ONE_TIME_ONLY ) )
      {
        Date oneTimeOnlyLaunchTime = processSchedule.getOneTimeOnlyLaunchTime();
        Date now = new Date();
        if ( oneTimeOnlyLaunchTime.before( now ) )
        {
          // If launch time is before now, reset to now so quartz doesn't consider a misfire
          oneTimeOnlyLaunchTime = now;
        }
        triggerBuilder = newTrigger().forJob( job ).startAt( oneTimeOnlyLaunchTime );

      }
      else if ( processFrequencyCode.equalsIgnoreCase( ProcessFrequencyType.DAILY ) )
      {
        triggerBuilder = newTrigger().withSchedule( dailyAtHourAndMinute( hourOfDay, minuteOfDay ) ).startAt( processSchedule.getStartDate() ).forJob( job );
      }
      else if ( processFrequencyCode.equalsIgnoreCase( ProcessFrequencyType.WEEKLY ) )
      {
        int dayOfWeek = toDayOfWeek( processSchedule.getDayOfWeekType() );
        triggerBuilder = newTrigger().withSchedule( weeklyOnDayAndHourAndMinute( dayOfWeek, hourOfDay, minuteOfDay ) ).startAt( processSchedule.getStartDate() ).forJob( job );
      }
      else if ( processFrequencyCode.equalsIgnoreCase( ProcessFrequencyType.MONTHLY ) )
      {
        int dayOfMonth = toDayOfMonth( processSchedule.getDayOfMonthType() );
        triggerBuilder = newTrigger().withSchedule( monthlyOnDayAndHourAndMinute( dayOfMonth, hourOfDay, minuteOfDay ) ).startAt( processSchedule.getStartDate() ).forJob( job );
      }
      else
      {
        throw new BeaconRuntimeException( "unknown ProcessFrequencyType code: " + processFrequencyCode );
      }
    }
    triggerBuilder.endAt( processSchedule.getEndDate() );
    triggerBuilder.withIdentity( triggerName, triggerGroupName );

    return triggerBuilder.build();
  }

  public static ProcessSchedule buildProcessSchedule( Trigger trigger )
  {
    ProcessSchedule processSchedule = new ProcessSchedule();
    // startDate alway begining of the day.
    Date startDate = DateUtils.toStartDate( trigger.getStartTime() );
    processSchedule.setStartDate( startDate );
    processSchedule.setEndDate( trigger.getEndTime() );
    if ( trigger instanceof SimpleTrigger )
    {
      // used for one time-only
      processSchedule.setTimeOfDayMillis( new Long( trigger.getStartTime().getTime() - startDate.getTime() ) );
      processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );
    }
    else if ( trigger instanceof CronTrigger )
    {
      CronTrigger triggerAsCronTrigger = (CronTrigger)trigger;
      String cronExpression = triggerAsCronTrigger.getCronExpression();
      String[] cronFields = StringUtils.split( cronExpression );
      if ( cronFields.length < REQUIRED_CRON_TERMS )
      {
        throw new BeaconRuntimeException( "Invalid cron expression (too few cron fields): " + cronExpression );
      }

      // String secondField = cronFields[0];
      String minuteField = cronFields[1];
      String hourField = cronFields[2];
      String dayOfMonthField = cronFields[3];
      // String monthField = cronFields[4];
      String dayOfWeekField = cronFields[5];

      if ( StringUtils.isEmpty( triggerAsCronTrigger.getDescription() ) )
      {
        // one last check...
        if ( StringUtils.isNumeric( hourField ) && StringUtils.isNumeric( minuteField ) )
        {
          long hourMillis = org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR * Integer.parseInt( hourField );
          long minuteMillis = org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE * Integer.parseInt( minuteField );
          processSchedule.setTimeOfDayMillis( new Long( hourMillis + minuteMillis ) );
        }
      }
      else // cron expression based trigger. Cron triggers have a description of
           // ProcessFrequencyType.CRON_EXPRESSION
      {
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.CRON_EXPRESSION ) );
        processSchedule.setCronExpression( cronExpression );
        return processSchedule;
      }
      if ( StringUtils.isNumeric( dayOfMonthField ) )
      {
        // Monthly schedule
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.MONTHLY ) );
        processSchedule.setDayOfMonthType( DayOfMonthType.lookup( String.valueOf( dayOfMonthField ) ) );
      }
      else if ( StringUtils.isNumeric( dayOfWeekField ) )
      {
        // weekly schedule
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.WEEKLY ) );
        processSchedule.setDayOfWeekType( DayOfWeekType.lookup( String.valueOf( dayOfWeekField ) ) );
      }
      else
      {
        // daily schedule
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.DAILY ) );
      }
    }
    else
    {
      throw new BeaconRuntimeException( "unknown trigger class: " + trigger.getClass().getName() );
    }

    return processSchedule;
  }

  /**
   * Convert the date to the 0-based hour of day int value.
   */
  public static int toHourOfDay( Long timeOfDayMillis )
  {
    return (int) ( timeOfDayMillis.longValue() / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR );
  }

  /**
   * Convert the date to the 0-based minute of day int value.
   */
  public static int toMinuteOfDay( Long timeOfDayMillis )
  {
    return (int) ( timeOfDayMillis.longValue() / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE % 60 );
  }

  /**
   * Convert the dayOfWeekType to the 1-based bay of week int value, for example "1" returns 1, "2"
   * returns 2.
   */
  public static int toDayOfWeek( DayOfWeekType dayOfWeekType )
  {
    return Integer.valueOf( dayOfWeekType.getCode() ).intValue();
  }

  /**
   * Convert the dayOfMonthType to the 1-based bay of month int value, for example "1" returns 1,
   * "2" returns 2.
   */
  public static int toDayOfMonth( DayOfMonthType dayOfMonthType )
  {
    return Integer.valueOf( dayOfMonthType.getCode() ).intValue();
  }
}
