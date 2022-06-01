/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.process;

import java.util.Date;

import com.biperf.core.domain.enums.DayOfMonthType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.exception.BeaconRuntimeException;

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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessSchedule
{

  // NOTE: Not a hibernate persisted class (underlying persisted data maintained by quartz)
  private ProcessFrequencyType processFrequencyType;
  private DayOfMonthType dayOfMonthType;
  private DayOfWeekType dayOfWeekType;

  private String cronExpression;
  private Long timeOfDayMillis;
  private Date startDate;
  private Date endDate;

  public Date getOneTimeOnlyLaunchTime()
  {
    if ( processFrequencyType == null || !processFrequencyType.getCode().equals( ProcessFrequencyType.ONE_TIME_ONLY ) )
    {
      throw new BeaconRuntimeException( "processFrequencyType must be set to ProcessFrequencyType.ONE_TIME_ONLY to " + "use this method" );
    }

    return new Date( startDate.getTime() + timeOfDayMillis.longValue() );
  }

  /**
   * @return value of dayOfMonthType property
   */
  public DayOfMonthType getDayOfMonthType()
  {
    return dayOfMonthType;
  }

  /**
   * @param dayOfMonthType value for dayOfMonthType property
   */
  public void setDayOfMonthType( DayOfMonthType dayOfMonthType )
  {
    this.dayOfMonthType = dayOfMonthType;
  }

  /**
   * @return value of dayOfWeekType property
   */
  public DayOfWeekType getDayOfWeekType()
  {
    return dayOfWeekType;
  }

  /**
   * @param dayOfWeekType value for dayOfWeekType property
   */
  public void setDayOfWeekType( DayOfWeekType dayOfWeekType )
  {
    this.dayOfWeekType = dayOfWeekType;
  }

  /**
   * @return value of endDate property
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate value for endDate property
   */
  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @return value of processFrequencyType property
   */
  public ProcessFrequencyType getProcessFrequencyType()
  {
    return processFrequencyType;
  }

  /**
   * @param processFrequencyType value for processFrequencyType property
   */
  public void setProcessFrequencyType( ProcessFrequencyType processFrequencyType )
  {
    this.processFrequencyType = processFrequencyType;
  }

  /**
   * @return value of startDate property
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate value for startDate property
   */
  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  /**
   * @return value of timeOfDayMillis property
   */
  public Long getTimeOfDayMillis()
  {
    return timeOfDayMillis;
  }

  /**
   * @param timeOfDayMillis value for timeOfDayMillis property
   */
  public void setTimeOfDayMillis( Long timeOfDayMillis )
  {
    this.timeOfDayMillis = timeOfDayMillis;
  }

  /**
   * @return value of timeOfDayMillis property as a Date
   */
  public Date getTimeOfDayAsDate()
  {
    if ( timeOfDayMillis != null )
    {
      return new Date( startDate.getTime() + timeOfDayMillis.longValue() );
    }
    return new Date( 0 );
  }

  public String getCronExpression()
  {
    return cronExpression;
  }

  public void setCronExpression( String cronExpression )
  {
    this.cronExpression = cronExpression;
  }

}
