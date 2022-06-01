/**
 * 
 */

package com.biperf.core.domain.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class CommenterInfo
{
  @JsonProperty( "id" )
  private Long id;
  @JsonProperty( "firstName" )
  private String firstName;
  @JsonProperty( "lastName" )
  private String lastName;
  @JsonProperty( "avatarUrl" )
  private String avatarUrl;

  public CommenterInfo()
  {
    super();
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

}
