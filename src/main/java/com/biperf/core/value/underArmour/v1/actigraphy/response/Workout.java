/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biperf.core.value.underArmour.v1.actigraphy.response;

import java.util.Date;

/**
 *
 * @author panneers
 */

public class Workout
{

  private Long id;

  private String name;
  private Date startDatetimeUtc;

  private Date endDatetimeUtc;

  private Double distance;

  private Double energyExpended;

  private Double steps;
  private Double activeTime;

  public Workout()
  {
  }

  public Workout( Long id )
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

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
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

  public Double getDistance()
  {
    return distance;
  }

  public void setDistance( Double distance )
  {
    this.distance = distance;
  }

  public Double getEnergyExpended()
  {
    return energyExpended;
  }

  public void setEnergyExpended( Double energyExpended )
  {
    this.energyExpended = energyExpended;
  }

  public Double getSteps()
  {
    return steps;
  }

  public void setSteps( Double steps )
  {
    this.steps = steps;
  }

  public Double getActiveTime()
  {
    return activeTime;
  }

  public void setActiveTime( Double activeTime )
  {
    this.activeTime = activeTime;
  }

}
