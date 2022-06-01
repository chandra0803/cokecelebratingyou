
package com.biperf.core.value.pastwinners;

import java.sql.Date;

public class NominationMyWinners
{

  private Long teamId;
  private Long approverUserId;
  private Long activityId;
  private String promotionName;
  private Date dateWon;
  private Long levelNumber;
  private String levelName;

  public NominationMyWinners()
  {

  }

  public NominationMyWinners( Long teamId, Long approverUserId, Long activityId, String promotionName, Date dateWon, Long levelNumber, String levelName )
  {
    this.teamId = teamId;
    this.approverUserId = approverUserId;
    this.activityId = activityId;
    this.promotionName = promotionName;
    this.dateWon = dateWon;
    this.levelNumber = levelNumber;
    this.levelName = levelName;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public Long getApproverUserId()
  {
    return approverUserId;
  }

  public void setApproverUserId( Long approverUserId )
  {
    this.approverUserId = approverUserId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getDateWon()
  {
    return dateWon;
  }

  public void setDateWon( Date dateWon )
  {
    this.dateWon = dateWon;
  }

  public Long getLevelNumber()
  {
    return levelNumber;
  }

  public void setLevelNumber( Long levelNumber )
  {
    this.levelNumber = levelNumber;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

}
