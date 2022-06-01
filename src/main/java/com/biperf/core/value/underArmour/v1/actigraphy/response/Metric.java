/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biperf.core.value.underArmour.v1.actigraphy.response;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author panneers
 */

public class Metric
{

  private Long id;

  private String metricType;
  private Date startDatetimeUtc;
  private Date endDatetimeUtc;

  private Double total;
  private Set<TimeSeries> timeSeries;
  private Boolean hasTimeSeries;

  public Metric()
  {
  }

  public Metric( Long id )
  {
    this.id = id;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getMetricType()
  {
    return metricType;
  }

  public void setMetricType( String metricType )
  {
    this.metricType = metricType;
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

  public Double getTotal()
  {
    return total;
  }

  public void setTotal( Double total )
  {
    this.total = total;
  }

  @XmlTransient
  public Set<TimeSeries> getTimeSeries()
  {
    return timeSeries;
  }

  public void setTimeSeries( Set<TimeSeries> timeSeries )
  {
    this.timeSeries = timeSeries;
  }

  public Boolean getHasTimeSeries()
  {
    return hasTimeSeries;
  }

  public void setHasTimeSeries( Boolean hasTimeSeries )
  {
    this.hasTimeSeries = hasTimeSeries;
  }

}
