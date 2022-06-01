
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationSubmissionTeamViewBean
{
  @JsonProperty( "userId" )
  private Long userId;
  @JsonProperty( "firstName" )
  private String firstName;
  @JsonProperty( "lastName" )
  private String lastName;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
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

}
