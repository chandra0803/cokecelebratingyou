
package com.biperf.core.domain.client;

import java.util.ArrayList;
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
@JsonPropertyOrder( { "messages", "careerMomentsModuleSets" } )
public class CareerMomentsModuleSets
{

  @JsonProperty( "messages" )
  private List<Object> messages = new ArrayList();
  private List<CareerMomentsModuleSet> careerMomentsSets = new ArrayList<CareerMomentsModuleSet>();

  public List<Object> getMessages()
  {
    return messages;
  }

  public void setMessages( List<Object> messages )
  {
    this.messages = messages;
  }

  public List<CareerMomentsModuleSet> getCareerMomentsSets()
  {
    return careerMomentsSets;
  }

  public void setCareerMomentsSets( List<CareerMomentsModuleSet> careerMomentsSets )
  {
    this.careerMomentsSets = careerMomentsSets;
  }

  

}
