package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApproverLevelTypeBean
{
  @JsonProperty( "id" )
  String code;
  @JsonProperty( "value" )
  String name;
  
  public String getCode()
  {
    return code;
  }
  public void setCode( String code )
  {
    this.code = code;
  }
  public String getName()
  {
    return name;
  }
  public void setName( String name )
  {
    this.name = name;
  }
  
}
