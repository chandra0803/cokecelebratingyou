
package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "id", "firstName", "lastName", "avatarUrl" } )
public class Commenter
{

  @JsonProperty( "id" )
  private Long id;
  @JsonProperty( "firstName" )
  private String firstName;
  @JsonProperty( "lastName" )
  private String lastName;
  @JsonProperty( "avatarUrl" )
  private String avatarUrl;

  @JsonProperty( "id" )
  public Long getId()
  {
    return id;
  }

  @JsonProperty( "id" )
  public void setId( Long id )
  {
    this.id = id;
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

}
