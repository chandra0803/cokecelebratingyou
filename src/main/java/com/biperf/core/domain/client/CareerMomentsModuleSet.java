
package com.biperf.core.domain.client;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "jobChanges", "newHires" } )
public class CareerMomentsModuleSet
{

  @JsonProperty( "jobChanges" )
  private JobChanges jobChanges;
  @JsonProperty( "newHires" )
  private NewHires newHires;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty( "jobChanges" )
  public JobChanges getJobChanges()
  {
    return jobChanges;
  }

  @JsonProperty( "jobChanges" )
  public void setJobChanges( JobChanges jobChanges )
  {
    this.jobChanges = jobChanges;
  }

  @JsonProperty( "newHires" )
  public NewHires getNewHires()
  {
    return newHires;
  }

  @JsonProperty( "newHires" )
  public void setNewHires( NewHires newHires )
  {
    this.newHires = newHires;
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
