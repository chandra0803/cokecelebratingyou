
package com.biperf.core.value.pastwinners;

import org.springframework.util.StringUtils;

import com.objectpartners.cms.util.CmsResourceBundle;

public class PastWinnersNomineeSummary
{
  private Long activityId;
  private Long claimId;
  private boolean teamNomination;
  private String firstName;
  private String lastName;
  private Long userId;
  private Long timePeriodId;
  private String timePeriodName;
  private Long hierarchyId;
  private String hierarchyName;
  private String position;
  private String countryCode;
  private String countryName;
  private String avatar;
  private String promotionName;
  private Long teamId;
  private String teamName;
  private String levelName;
  private Long levelId;
  private Long promotionId;
  private String departmentName;
  private Long claimGroupId;

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
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

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public String getHierarchyName()
  {
    return hierarchyName;
  }

  public void setHierarchyName( String hierarchyName )
  {
    this.hierarchyName = hierarchyName;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getAvatar()
  {
    return avatar;
  }

  public void setAvatar( String avatar )
  {
    this.avatar = avatar;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public boolean isTeamNomination()
  {
    return teamNomination;
  }

  public void setTeamNomination( boolean teamNomination )
  {
    this.teamNomination = teamNomination;
  }

  public Long getHierarchyId()
  {
    return hierarchyId;
  }

  public void setHierarchyId( Long hierarchyId )
  {
    this.hierarchyId = hierarchyId;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getName()
  {
    return getFirstName() + " " + getLastName();
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getOrgName()
  {
    String hierarchyId = "";
    String hierarchyName = "";
    if ( getHierarchyId() != null && getHierarchyId() != 0 )
    {
      hierarchyId = getHierarchyId() + "";
    }
    if ( getHierarchyName() != null )
    {
      hierarchyName = getHierarchyName();
    }

    if ( StringUtils.isEmpty( hierarchyId ) && StringUtils.isEmpty( hierarchyName ) )
    {
      return "";
    }
    else
    {
      return hierarchyName;
    }
  }

  public String getDetailedName( boolean isTeamNomination )
  {
    String detailMessage = null;

    if ( isTeamNomination )
    {
      detailMessage = CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.WINNER_DETAILS" );
    }
    else
    {
      detailMessage = CmsResourceBundle.getCmsBundle().getString( "nomination.past.winners.WINNER" );
    }

    String timePeriodName = "";
    if ( getTimePeriodName() != null )
    {
      timePeriodName = getTimePeriodName();
    }

    return timePeriodName + " " + detailMessage;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
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

}
