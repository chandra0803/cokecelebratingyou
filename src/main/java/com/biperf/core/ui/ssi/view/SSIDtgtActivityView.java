
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIDtgtActivityView
{

  private Long contestId;
  private Long activityId;
  private String activityDescription;
  private String forEvery;
  private String willEarn;
  private String minQualifier;
  private String submitted;
  private String name;
  private String payout;
  private String payoutPercentProgress;
  private String payoutProgress;
  private String payoutCap;
  private String goal;
  private String progress;
  private String remaining;
  private String percentProgress;
  private Boolean includeStackRanking;
  private List<SSIStackRankParticipantViewBean> stackRankParticipants;
  private SSIContestStackRankPaxValueBean stackRank;
  private String paid;
  private String payoutDescription;
  private String payoutType;
  private Boolean goalAchieved;

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

  public String getForEvery()
  {
    return forEvery;
  }

  public void setForEvery( String forEvery )
  {
    this.forEvery = forEvery;
  }

  public String getWillEarn()
  {
    return willEarn;
  }

  public void setWillEarn( String willEarn )
  {
    this.willEarn = willEarn;
  }

  public String getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( String minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public String getSubmitted()
  {
    if ( submitted == null || submitted.equals( "" ) )
    {
      return "0";
    }
    return submitted;
  }

  public void setSubmitted( String submitted )
  {
    this.submitted = submitted;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getPayout()
  {
    return payout;
  }

  public void setPayout( String payout )
  {
    this.payout = payout;
  }

  public String getPayoutPercentProgress()
  {
    return payoutPercentProgress;
  }

  public void setPayoutPercentProgress( String payoutPercentProgress )
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

  public String getPayoutCap()
  {
    return payoutCap;
  }

  public void setPayoutCap( String payoutCap )
  {
    this.payoutCap = payoutCap;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
  }

  public String getProgress()
  {
    if ( progress == null || progress.equals( "" ) )
    {
      return "0";
    }
    return progress;
  }

  public void setProgress( String progress )
  {
    this.progress = progress;
  }

  public String getRemaining()
  {
    return remaining;
  }

  public void setRemaining( String remaining )
  {
    this.remaining = remaining;
  }

  public String getPercentProgress()
  {
    return percentProgress;
  }

  public void setPercentProgress( String percentProgress )
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

  public List<SSIStackRankParticipantViewBean> getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setStackRankParticipants( List<SSIStackRankParticipantViewBean> stackRankParticipants )
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

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public Boolean getGoalAchieved()
  {
    return goalAchieved;
  }

  public void setGoalAchieved( Boolean goalAchieved )
  {
    this.goalAchieved = goalAchieved;
  }

}
