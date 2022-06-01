package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LevelPayoutDataBean
{
  @JsonProperty( "id" )
  String levelName;
  @JsonProperty( "value" )
  String levelValue;
  public String getLevelName()
  {
    return levelName;
  }
  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }
  public String getLevelValue()
  {
    return levelValue;
  }
  public void setLevelValue( String levelValue )
  {
    this.levelValue = levelValue;
  }
  
  

}
