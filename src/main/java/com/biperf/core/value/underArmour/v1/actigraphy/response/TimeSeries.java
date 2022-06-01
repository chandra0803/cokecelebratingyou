/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biperf.core.value.underArmour.v1.actigraphy.response;

/**
 *
 * @author panneers
 */

public class TimeSeries
{

  private Long id;

  private String epochValue;

  public TimeSeries()
  {
  }

  public TimeSeries( Long id )
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

  public String getEpochValue()
  {
    return epochValue;
  }

  public void setEpochValue( String epochValue )
  {
    this.epochValue = epochValue;
  }

}
