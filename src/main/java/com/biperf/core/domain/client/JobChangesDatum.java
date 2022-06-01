
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
@JsonPropertyOrder( { "id", "lastName", "firstName", "avatarUrl", "division", "positionType" } )
public class JobChangesDatum
{

  @JsonProperty( "id" )
  private Integer id;
  @JsonProperty( "lastName" )
  private String lastName;
  @JsonProperty( "firstName" )
  private String firstName;
  @JsonProperty( "avatarUrl" )
  private String avatarUrl;
  @JsonProperty( "division" )
  private String division;
  @JsonProperty( "positionType" )
  private String positionType;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty( "id" )
  public Integer getId()
  {
    return id;
  }

  @JsonProperty( "id" )
  public void setId( Integer id )
  {
    this.id = id;
  }

  @JsonProperty( "lastName" )
  public String getLastName()
  {
    return lastName;
  }

  @JsonProperty( "lastName" )
  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  @JsonProperty( "firstName" )
  public String getFirstName()
  {
    return firstName;
  }

  @JsonProperty( "firstName" )
  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  @JsonProperty( "avatarUrl" )
  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  @JsonProperty( "avatarUrl" )
  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  @JsonProperty( "division" )
  public String getDivision()
  {
    return division;
  }

  @JsonProperty( "division" )
  public void setDivision( String division )
  {
    this.division = division;
  }

  @JsonProperty( "positionType" )
  public String getPositionType()
  {
    return positionType;
  }

  @JsonProperty( "positionType" )
  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
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
