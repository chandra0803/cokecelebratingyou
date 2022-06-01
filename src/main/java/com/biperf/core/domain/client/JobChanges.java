
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
@JsonPropertyOrder( { "displayDate", "jobChangesData" } )
public class JobChanges
{

  @JsonProperty( "displayDate" )
  private String displayDate;
  @JsonProperty( "jobChangesData" )
  private List<JobChangesDatum> jobChangesData = null;
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

  @JsonProperty( "jobChangesData" )
  public List<JobChangesDatum> getJobChangesData()
  {
    return jobChangesData;
  }

  @JsonProperty( "jobChangesData" )
  public void setJobChangesData( List<JobChangesDatum> jobChangesData )
  {
    this.jobChangesData = jobChangesData;
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
