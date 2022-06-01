
package com.biperf.core.value.pastwinners;

public class PastWinnersBehaviorDetails
{
  private Long claimId;
  private String behaviorName;
  private String badgeName;
  private Long badgeId;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getBehaviorName()
  {
    return behaviorName;
  }

  public void setBehaviorName( String behaviorName )
  {
    this.behaviorName = behaviorName;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }
}
