
package com.biperf.core.value.ssi;

import java.util.List;

import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SSIPaxDTGTActivityProgressValueBean
{
  private Long contestId;
  private Long activityId;
  private String activityDescription;
  private Double forEvery;
  private Long willEarn;
  private Double minQualifier;
  private String name;
  private Long payout;
  private Long payoutPercentProgress;
  private String payoutProgress;
  private Double payoutCap;
  private Double goal;
  private Double progress;
  private Double remaining;
  private Long percentProgress;
  private Boolean includeStackRanking;
  private List<SSIContestStackRankTeamValueBean> stackRankParticipants;
  private SSIContestStackRankPaxValueBean stackRank;
  private String payoutOtherCurrency;
  private String paid;
  private String payoutDescription;
  private Long payoutQuantity;

  /**
   * 
   */
  public SSIPaxDTGTActivityProgressValueBean()
  {
    super();
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Long getPayoutPercentProgress()
  {
    return payoutPercentProgress;
  }

  public void setPayoutPercentProgress( Long payoutPercentProgress )
  {
    this.payoutPercentProgress = payoutPercentProgress;
  }

  public String getPayoutProgress()
  {
    return payoutProgress;
  }

  public void setPayoutProgress( String payoutProgress )
  {
    this.payoutProgress = payoutProgress;
  }

  public Long getPayout()
  {
    return payout;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  @JsonIgnore
  public Double getPayoutCap()
  {
    return payoutCap;
  }

  public void setPayoutCap( Double payoutCap )
  {
    this.payoutCap = payoutCap;
  }

  @JsonProperty( "payoutCap" )
  public String getDisplayPayoutCap()
  {
    return SSIContestUtil.getFormattedValue( this.payoutCap, SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
  }

  public Long getPercentProgress()
  {
    return percentProgress;
  }

  public void setPercentProgress( Long percentProgress )
  {
    this.percentProgress = percentProgress;
  }

  public Boolean getIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( Boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public List<SSIContestStackRankTeamValueBean> getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setStackRankParticipants( List<SSIContestStackRankTeamValueBean> stackRankParticipants )
  {
    this.stackRankParticipants = stackRankParticipants;
  }

  public SSIContestStackRankPaxValueBean getStackRank()
  {
    return stackRank;
  }

  public void setStackRank( SSIContestStackRankPaxValueBean stackRank )
  {
    this.stackRank = stackRank;
  }

  public String getPayoutOtherCurrency()
  {
    return payoutOtherCurrency;
  }

  public void setPayoutOtherCurrency( String payoutOtherCurrency )
  {
    this.payoutOtherCurrency = payoutOtherCurrency;
  }

  public String getPaid()
  {
    return paid;
  }

  public void setPaid( String paid )
  {
    this.paid = paid;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public Double getForEvery()
  {
    return forEvery;
  }

  public void setForEvery( Double forEvery )
  {
    this.forEvery = forEvery;
  }

  public Long getWillEarn()
  {
    return willEarn;
  }

  public void setWillEarn( Long willEarn )
  {
    this.willEarn = willEarn;
  }

  public Double getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( Double minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public Double getGoal()
  {
    return goal;
  }

  public void setGoal( Double goal )
  {
    this.goal = goal;
  }

  public Double getProgress()
  {
    return progress;
  }

  public void setProgress( Double progress )
  {
    this.progress = progress;
  }

  public Double getRemaining()
  {
    return remaining;
  }

  public void setRemaining( Double remaining )
  {
    this.remaining = remaining;
  }

  public Long getPayoutQuantity()
  {
    return payoutQuantity;
  }

  public void setPayoutQuantity( Long payoutQuantity )
  {
    this.payoutQuantity = payoutQuantity;
  }

}
