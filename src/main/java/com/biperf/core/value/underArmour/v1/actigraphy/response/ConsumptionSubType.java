
package com.biperf.core.value.underArmour.v1.actigraphy.response;

public enum ConsumptionSubType
{
  CARBOHYDRATES( "carbohydrates" ), FATS( "fats" ), PROTEIN( "protein" );

  private String name;

  ConsumptionSubType( String name )
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

}
