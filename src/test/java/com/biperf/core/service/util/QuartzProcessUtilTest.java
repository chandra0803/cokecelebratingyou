/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.util;

import static org.quartz.JobBuilder.newJob;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.process.ProcessBeanJob;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.process.impl.ProcessServiceImplTest;

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
public class QuartzProcessUtilTest extends BaseServiceTest
{
  public void testBuildTriggerAndBuildProcessScheduleForMonthly()
  {
    ProcessSchedule expectedProcessSchedule = ProcessServiceImplTest.buildMonthlyProcessSchedule();

    innerTestBuildTriggerAndBuildProcessSchedule( expectedProcessSchedule );
  }

  public void testBuildTriggerAndBuildProcessScheduleForWeekly()
  {
    ProcessSchedule expectedProcessSchedule = ProcessServiceImplTest.buildWeeklyProcessSchedule();

    innerTestBuildTriggerAndBuildProcessSchedule( expectedProcessSchedule );
  }

  public void testBuildTriggerAndBuildProcessScheduleForDaily()
  {
    ProcessSchedule expectedProcessSchedule = ProcessServiceImplTest.buildDailyProcessSchedule();

    innerTestBuildTriggerAndBuildProcessSchedule( expectedProcessSchedule );
  }

  public void testBuildTriggerAndBuildProcessScheduleForOneTimeOnly()
  {
    ProcessSchedule expectedProcessSchedule = ProcessServiceImplTest.buildOneTimeOnlyProcessSchedule();

    innerTestBuildTriggerAndBuildProcessSchedule( expectedProcessSchedule );
  }

  private void innerTestBuildTriggerAndBuildProcessSchedule( ProcessSchedule expectedProcessSchedule )
  {
    Date roundedStartDate = quartzRoundTime( expectedProcessSchedule.getStartDate() );
    expectedProcessSchedule.setStartDate( roundedStartDate );

    Date roundedEndDate = quartzRoundTime( expectedProcessSchedule.getEndDate() );
    expectedProcessSchedule.setEndDate( roundedEndDate );

    String jobName = "testJobName";
    String jobGroupName = "testJobGroupName";
    JobDetail job = newJob( ProcessBeanJob.class ).withIdentity( jobName, jobGroupName ).storeDurably().build();

    // Define Trigger Instance
    String triggerGroupName = "testTriggerGroupName";
    String triggerName = "testTriggerName";

    Trigger testTrigger = QuartzProcessUtil.buildTrigger( expectedProcessSchedule, job, triggerName, triggerGroupName, jobGroupName );

    // Do the opposite action to build up the process schedule
    ProcessSchedule actualProcessSchedule = QuartzProcessUtil.buildProcessSchedule( testTrigger );
    ProcessServiceImplTest.assertProcessScheduleEquals( expectedProcessSchedule, actualProcessSchedule );
  }

  private Date quartzRoundTime( Date date )
  {
    // Quartz truncates millis, so we do the same for testing
    if ( date == null )
    {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );
    calendar.set( Calendar.MILLISECOND, 0 );
    Date roundedStartDate = calendar.getTime();
    return roundedStartDate;
  }
}
