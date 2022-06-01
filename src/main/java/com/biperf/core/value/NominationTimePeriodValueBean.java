
package com.biperf.core.value;

import java.util.Date;

import com.objectpartners.cms.util.DateUtils;

public class NominationTimePeriodValueBean
{
  private Long timePeriodId;
  private String timePeriodName;
  private String timePeriodStartDate;
  private boolean timePeriodStartDateEditable;
  private String timePeriodEndDate;
  private boolean timePeriodEndDateEditable;
  private String maxSubmissionAllowed;
  private String maxNominationsAllowed;
  private String maxWinsllowed;

  public Long getTimePeriodId()
  {
    return timePeriodId;
  }

  public void setTimePeriodId( Long timePeriodId )
  {
    this.timePeriodId = timePeriodId;
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public String getTimePeriodStartDate()
  {
    return timePeriodStartDate;
  }

  public void setTimePeriodStartDate( String timePeriodStartDate )
  {
    this.timePeriodStartDate = timePeriodStartDate;
  }

  public String getTimePeriodEndDate()
  {
    return timePeriodEndDate;
  }

  public void setTimePeriodEndDate( String timePeriodEndDate )
  {
    this.timePeriodEndDate = timePeriodEndDate;
  }

  public String getMaxSubmissionAllowed()
  {
    return maxSubmissionAllowed;
  }

  public void setMaxSubmissionAllowed( String maxSubmissionAllowed )
  {
    this.maxSubmissionAllowed = maxSubmissionAllowed;
  }

  public String getMaxNominationsAllowed()
  {
    return maxNominationsAllowed;
  }

  public void setMaxNominationsAllowed( String maxNominationsAllowed )
  {
    this.maxNominationsAllowed = maxNominationsAllowed;
  }

  public String getMaxWinsllowed()
  {
    return maxWinsllowed;
  }

  public void setMaxWinsllowed( String maxWinsllowed )
  {
    this.maxWinsllowed = maxWinsllowed;
  }

  public boolean isTimePeriodStartDateEditable()
  {
    return timePeriodStartDateEditable;
  }

  public void setTimePeriodStartDateEditable( boolean timePeriodStartDateEditable )
  {
    this.timePeriodStartDateEditable = timePeriodStartDateEditable;
  }

  public boolean isTimePeriodEndDateEditable()
  {
    return timePeriodEndDateEditable;
  }

  public void setTimePeriodEndDateEditable( boolean timePeriodEndDateEditable )
  {
    this.timePeriodEndDateEditable = timePeriodEndDateEditable;
  }

  public Date getStartDate()
  {
    return DateUtils.toDate( this.getTimePeriodStartDate() );
  }

  public Date getEndDate()
  {
    return DateUtils.toDate( this.getTimePeriodEndDate() );
  }

  public Date getCurrentDate()
  {
    return DateUtils.getCurrentDate();
  }

  public Boolean isStartDatePast()
  {
    return getStartDate().before( getCurrentDate() ) && !org.apache.commons.lang3.time.DateUtils.isSameDay( getStartDate(), getCurrentDate() );
  }

  public Boolean isEndDatePast()
  {
    return getEndDate().before( getCurrentDate() ) && !org.apache.commons.lang3.time.DateUtils.isSameDay( getEndDate(), getCurrentDate() );
  }
}
