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
public class Aggregate
{
  private Long id;

  private Double distance;

  private Double bodymass;

  private Double bmi;

  private Double fatMassPercent;

  private Double heartRateResting;

  private Double activeTime;

  private Double energyExpanded;

  private Double steps;

  private Double targetNetEnergy;
  private Set<ConsumptionSummary> consumptionSummaries;
  private Date bodyMassUTCDate;

  private Double restIndicator;

  private String restReason;

  private Date heartRateRestingUTC;

  private Double sum;

  private Double awake;

  private Double deepSleep;

  private Double lightSleep;

  private Double timeToSleep;

  private Double timesAwakened;

  private Double workoutCount;

  private Double workoutActiveTime;

  private Double workoutEnergyExpanded;

  public Aggregate()
  {
  }

  public Aggregate( Long id )
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

  public Double getDistance()
  {
    return distance;
  }

  public void setDistance( Double distance )
  {
    this.distance = distance;
  }

  public Double getBodymass()
  {
    return bodymass;
  }

  public void setBodymass( Double bodymass )
  {
    this.bodymass = bodymass;
  }

  public Double getBmi()
  {
    return bmi;
  }

  public void setBmi( Double bmi )
  {
    this.bmi = bmi;
  }

  public Double getFatMassPercent()
  {
    return fatMassPercent;
  }

  public void setFatMassPercent( Double fatMassPercent )
  {
    this.fatMassPercent = fatMassPercent;
  }

  public Double getHeartRateResting()
  {
    return heartRateResting;
  }

  public void setHeartRateResting( Double heartRateResting )
  {
    this.heartRateResting = heartRateResting;
  }

  public Double getActiveTime()
  {
    return activeTime;
  }

  public void setActiveTime( Double activeTime )
  {
    this.activeTime = activeTime;
  }

  public Double getEnergyExpanded()
  {
    return energyExpanded;
  }

  public void setEnergyExpanded( Double energyExpanded )
  {
    this.energyExpanded = energyExpanded;
  }

  public Double getSteps()
  {
    return steps;
  }

  public void setSteps( Double steps )
  {
    this.steps = steps;
  }

  public Double getTargetNetEnergy()
  {
    return targetNetEnergy;
  }

  public void setTargetNetEnergy( Double targetNetEnergy )
  {
    this.targetNetEnergy = targetNetEnergy;
  }

  @XmlTransient
  public Set<ConsumptionSummary> getConsumptionSummaries()
  {
    return consumptionSummaries;
  }

  public void setConsumptionSummaries( Set<ConsumptionSummary> consumptionSummaries )
  {
    this.consumptionSummaries = consumptionSummaries;
  }

  public Date getBodyMassUTCDate()
  {
    return bodyMassUTCDate;
  }

  public void setBodyMassUTCDate( Date bodyMassUTCDate )
  {
    this.bodyMassUTCDate = bodyMassUTCDate;
  }

  public Double getRestIndicator()
  {
    return restIndicator;
  }

  public void setRestIndicator( Double restIndicator )
  {
    this.restIndicator = restIndicator;
  }

  public String getRestReason()
  {
    return restReason;
  }

  public void setRestReason( String restReason )
  {
    this.restReason = restReason;
  }

  public Date getHeartRateRestingUTC()
  {
    return heartRateRestingUTC;
  }

  public void setHeartRateRestingUTC( Date heartRateRestingUTC )
  {
    this.heartRateRestingUTC = heartRateRestingUTC;
  }

  public Double getSum()
  {
    return sum;
  }

  public void setSum( Double sum )
  {
    this.sum = sum;
  }

  public Double getAwake()
  {
    return awake;
  }

  public void setAwake( Double awake )
  {
    this.awake = awake;
  }

  public Double getDeepSleep()
  {
    return deepSleep;
  }

  public void setDeepSleep( Double deepSleep )
  {
    this.deepSleep = deepSleep;
  }

  public Double getLightSleep()
  {
    return lightSleep;
  }

  public void setLightSleep( Double lightSleep )
  {
    this.lightSleep = lightSleep;
  }

  public Double getTimeToSleep()
  {
    return timeToSleep;
  }

  public void setTimeToSleep( Double timeToSleep )
  {
    this.timeToSleep = timeToSleep;
  }

  public Double getTimesAwakened()
  {
    return timesAwakened;
  }

  public void setTimesAwakened( Double timesAwakened )
  {
    this.timesAwakened = timesAwakened;
  }

  public Double getWorkoutCount()
  {
    return workoutCount;
  }

  public void setWorkoutCount( Double workoutCount )
  {
    this.workoutCount = workoutCount;
  }

  public Double getWorkoutActiveTime()
  {
    return workoutActiveTime;
  }

  public void setWorkoutActiveTime( Double workoutActiveTime )
  {
    this.workoutActiveTime = workoutActiveTime;
  }

  public Double getWorkoutEnergyExpanded()
  {
    return workoutEnergyExpanded;
  }

  public void setWorkoutEnergyExpanded( Double workoutEnergyExpanded )
  {
    this.workoutEnergyExpanded = workoutEnergyExpanded;
  }

}
