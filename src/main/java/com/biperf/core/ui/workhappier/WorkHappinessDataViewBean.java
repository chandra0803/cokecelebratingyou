
package com.biperf.core.ui.workhappier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class WorkHappinessDataViewBean
{
  private String headline;
  private String feeling;
  @JsonProperty( "min" )
  private Long minValue;
  private String[] thoughts;

  public String getHeadline()
  {
    return headline;
  }

  public void setHeadline( String headline )
  {
    this.headline = headline;
  }

  public String getFeeling()
  {
    return feeling;
  }

  public void setFeeling( String feeling )
  {
    this.feeling = feeling;
  }

  public Long getMinValue()
  {
    return minValue;
  }

  public void setMinValue( Long minValue )
  {
    this.minValue = minValue;
  }

  public String[] getThoughts()
  {
    return thoughts;
  }

  public void setThoughts( String[] thoughts )
  {
    this.thoughts = thoughts;
  }

}
