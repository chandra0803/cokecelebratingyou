
package com.biperf.core.value.throwdown;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * ThrowdownActivityByPaxReportValue.
 * 
 * @author kandhi
 * @since Oct 22, 2013
 * @version 1.0
 */
public class ThrowdownActivityByPaxReportValue
{
  private Long userId;
  private String participantName;
  private String loginId;
  private String country;
  private String participantStatus;
  private String orgName;
  private String jobPosition;
  private String department;
  private String promotionName;
  private Long winsCnt;
  private Long tiesCnt;
  private Long lossCnt;
  private BigDecimal activityCnt;
  private Long rank;
  private Long roundNumber;
  private Long points;
  private Date roundStartDate;
  private Date roundEndDate;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getParticipantName()
  {
    return participantName;
  }

  public void setParticipantName( String participantName )
  {
    this.participantName = participantName;
  }

  public String getLoginId()
  {
    return loginId;
  }

  public void setLoginId( String loginId )
  {
    this.loginId = loginId;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getParticipantStatus()
  {
    return participantStatus;
  }

  public void setParticipantStatus( String participantStatus )
  {
    this.participantStatus = participantStatus;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getWinsCnt()
  {
    return winsCnt;
  }

  public void setWinsCnt( Long winsCnt )
  {
    this.winsCnt = winsCnt;
  }

  public Long getTiesCnt()
  {
    return tiesCnt;
  }

  public void setTiesCnt( Long tiesCnt )
  {
    this.tiesCnt = tiesCnt;
  }

  public Long getLossCnt()
  {
    return lossCnt;
  }

  public void setLossCnt( Long lossCnt )
  {
    this.lossCnt = lossCnt;
  }

  public BigDecimal getActivityCnt()
  {
    return activityCnt;
  }

  public void setActivityCnt( BigDecimal activityCnt )
  {
    this.activityCnt = activityCnt;
  }

  public Long getRank()
  {
    return rank;
  }

  public void setRank( Long rank )
  {
    this.rank = rank;
  }

  public Long getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( Long roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public Date getRoundStartDate()
  {
    return roundStartDate;
  }

  public void setRoundStartDate( Date roundStartDate )
  {
    this.roundStartDate = roundStartDate;
  }

  public Date getRoundEndDate()
  {
    return roundEndDate;
  }

  public void setRoundEndDate( Date roundEndDate )
  {
    this.roundEndDate = roundEndDate;
  }

}
