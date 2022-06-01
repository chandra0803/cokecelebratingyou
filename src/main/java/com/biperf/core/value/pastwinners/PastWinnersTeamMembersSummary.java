
package com.biperf.core.value.pastwinners;

public class PastWinnersTeamMembersSummary
{
  private Long activityId;
  private String memberId;
  private String firstName;
  private String lastName;
  private Long teamId;

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getMemberId()
  {
    return memberId;
  }

  public void setMemberId( String memberId )
  {
    this.memberId = memberId;
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

  public String getName()
  {
    return getLastName() + ", " + getFirstName();
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }
}
