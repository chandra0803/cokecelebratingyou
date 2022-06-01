
package com.biperf.core.value.pastwinners;

import java.math.BigDecimal;

public class NominationModalNomineeDetails
{
  private String timePeriodName;
  private String promotionName;
  private Long winCout;
  private Long pointsWon;
  private String currencyCode;
  private BigDecimal cashWon;
  private Long teamId;
  private Long approverUserId;
  private Long activityId;
  private String payoutDescription;

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getWinCout()
  {
    return winCout;
  }

  public void setWinCout( Long winCout )
  {
    this.winCout = winCout;
  }

  public Long getPointsWon()
  {
    return pointsWon;
  }

  public void setPointsWon( Long pointsWon )
  {
    this.pointsWon = pointsWon;
  }

  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode( String currencyCode )
  {
    this.currencyCode = currencyCode;
  }

  public BigDecimal getCashWon()
  {
    return cashWon;
  }

  public void setCashWon( BigDecimal cashWon )
  {
    this.cashWon = cashWon;
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

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getNominationDetails()
  {
    String timePeriodName = "";
    String promotionName = "";

    if ( getTimePeriodName() != null )
    {
      timePeriodName = getTimePeriodName();
    }

    if ( promotionName != null )
    {
      promotionName = getPromotionName();
    }

    return timePeriodName + " " + promotionName;
  }
}
