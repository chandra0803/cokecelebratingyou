
package com.biperf.core.value.pastwinners;

public class PastWinnersNominatorSummary
{
  private Long activityId;
  private Long userId;
  private String firstName;
  private String lastName;
  private String countryCode;
  private String countryName;
  private Long hierarchyId;
  private String organisationName;
  private String position;
  private String commentText;
  private String departmentName;
  private String timePeriodName;
  private Long timePeriodId;
  private Long claimGroupId;
  private Long teamId;
  private String teamName;

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

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

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public Long getHierarchyId()
  {
    return hierarchyId;
  }

  public void setHierarchyId( Long hierarchyId )
  {
    this.hierarchyId = hierarchyId;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getCommentText()
  {
    return commentText;
  }

  public void setCommentText( String commentText )
  {
    this.commentText = commentText;
  }

  public String getOrganisationName()
  {
    return organisationName;
  }

  public void setOrganisationName( String organisationName )
  {
    this.organisationName = organisationName;
  }

  public String getName()
  {
    return getFirstName() + " " + getLastName();
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public Long getTimePeriodId()
  {
    return timePeriodId;
  }

  public void setTimePeriodId( Long timePeriodId )
  {
    this.timePeriodId = timePeriodId;
  }

  public Long getClaimGroupId()
  {
    return claimGroupId;
  }

  public void setClaimGroupId( Long claimGroupId )
  {
    this.claimGroupId = claimGroupId;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }
}
