
package com.biperf.core.value.contributor;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class Contributor implements Serializable
{
  private Long id;
  private String firstName;
  private String lastName;
  private String avatarUrl;

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
    this.firstName = firstName != null ? firstName : "";
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName != null ? lastName : "";
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl != null ? avatarUrl : "";
  }
}
