
package com.biperf.core.value.underArmour.v1.actigraphy.response;

public enum ConsumptionType
{

  ENERGY_CONSUMED( "energy_consumed" ), NUTRIENTS_CONSUMED( "nutrients_consumed" );

  private String name;

  ConsumptionType( String name )
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
