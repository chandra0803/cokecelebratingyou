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

public class ConsumptionSummary
{

  private Long id;

  private String consumptionType;

  private String consumptionSubtype;

  private Double total;

  private Double lunch;

  private Double breakfast;

  private Double snack;

  private Double dinner;

  public ConsumptionSummary()
  {
  }

  public ConsumptionSummary( Long id )
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

  public String getConsumptionType()
  {
    return consumptionType;
  }

  public void setConsumptionType( String consumptionType )
  {
    this.consumptionType = consumptionType;
  }

  public String getConsumptionSubtype()
  {
    return consumptionSubtype;
  }

  public void setConsumptionSubtype( String consumptionSubtype )
  {
    this.consumptionSubtype = consumptionSubtype;
  }

  public Double getTotal()
  {
    return total;
  }

  public void setTotal( Double total )
  {
    this.total = total;
  }

  public Double getLunch()
  {
    return lunch;
  }

  public void setLunch( Double lunch )
  {
    this.lunch = lunch;
  }

  public Double getBreakfast()
  {
    return breakfast;
  }

  public void setBreakfast( Double breakfast )
  {
    this.breakfast = breakfast;
  }

  public Double getSnack()
  {
    return snack;
  }

  public void setSnack( Double snack )
  {
    this.snack = snack;
  }

  public Double getDinner()
  {
    return dinner;
  }

  public void setDinner( Double dinner )
  {
    this.dinner = dinner;
  }

}
