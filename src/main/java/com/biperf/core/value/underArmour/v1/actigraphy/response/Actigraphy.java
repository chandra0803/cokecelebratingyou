/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biperf.core.value.underArmour.v1.actigraphy.response;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

public class Actigraphy
{

  private Long id;
  private String date;
  private int timeSeriesInterval;
  private Date startDatetimeUtc;
  private Date endDatetimeUtc;
  private String timezones;
  private Integer totalCount;
  private Date oldestDate;
  private Set<Aggregate> aggregates;
  private Set<Metric> metrics;
  private Set<Workout> workouts;

  public Actigraphy()
  {
  }

  public Actigraphy( Long id )
  {
    this.id = id;
  }

  public Actigraphy( Long id, String date, int timeSeriesInterval )
  {
    this.id = id;
    this.date = date;
    this.timeSeriesInterval = timeSeriesInterval;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public int getTimeSeriesInterval()
  {
    return timeSeriesInterval;
  }

  public void setTimeSeriesInterval( int timeSeriesInterval )
  {
    this.timeSeriesInterval = timeSeriesInterval;
  }

  public Date getStartDatetimeUtc()
  {
    return startDatetimeUtc;
  }

  public void setStartDatetimeUtc( Date startDatetimeUtc )
  {
    this.startDatetimeUtc = startDatetimeUtc;
  }

  public Date getEndDatetimeUtc()
  {
    return endDatetimeUtc;
  }

  public void setEndDatetimeUtc( Date endDatetimeUtc )
  {
    this.endDatetimeUtc = endDatetimeUtc;
  }

  public String getTimezones()
  {
    return timezones;
  }

  public void setTimezones( String timezones )
  {
    this.timezones = timezones;
  }

  public Integer getTotalCount()
  {
    return totalCount;
  }

  public void setTotalCount( Integer totalCount )
  {
    this.totalCount = totalCount;
  }

  public Date getOldestDate()
  {
    return oldestDate;
  }

  public void setOldestDate( Date oldestDate )
  {
    this.oldestDate = oldestDate;
  }

  @XmlTransient
  public Set<Aggregate> getAggregates()
  {
    return aggregates;
  }

  public void setAggregates( Set<Aggregate> aggregates )
  {
    this.aggregates = aggregates;
  }

  @XmlTransient
  public Set<Metric> getMetrics()
  {
    return metrics;
  }

  public void setMetrics( Set<Metric> metrics )
  {
    this.metrics = metrics;
  }

  @XmlTransient
  public Set<Workout> getWorkouts()
  {
    return workouts;
  }

  public void setWorkouts( Set<Workout> workouts )
  {
    this.workouts = workouts;
  }
}
