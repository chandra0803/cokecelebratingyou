
package com.biperf.core.domain.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "displayDate", "newHiresData" } )
public class NewHires
{

  @JsonProperty( "displayDate" )
  private String displayDate;
  @JsonProperty( "newHiresData" )
  private List<NewHiresDatum> newHiresData = null;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty( "displayDate" )
  public String getDisplayDate()
  {
    return displayDate;
  }

  @JsonProperty( "displayDate" )
  public void setDisplayDate( String displayDate )
  {
    this.displayDate = displayDate;
  }

  @JsonProperty( "newHiresData" )
  public List<NewHiresDatum> getNewHiresData()
  {
    return newHiresData;
  }

  @JsonProperty( "newHiresData" )
  public void setNewHiresData( List<NewHiresDatum> newHiresData )
  {
    this.newHiresData = newHiresData;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties()
  {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty( String name, Object value )
  {
    this.additionalProperties.put( name, value );
  }

}
