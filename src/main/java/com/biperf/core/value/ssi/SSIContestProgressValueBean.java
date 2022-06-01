
package com.biperf.core.value.ssi;

import java.util.List;

/**
 * SSIContestProgressValueBean.
 * 
 * @author dudam
 * @since Jan 27, 2015
 * @version 1.0
 */
public class SSIContestProgressValueBean
{
  private Long contestId;
  private Long activityId;
  private String activityDescription;
  private Double goal;
  private Double teamActivity;
  private Double totalObjectiveAmount;
  private Long percentageAcheived;
  private Integer participantAchieved;
  private Integer totalParticipant;
  private Long potentialPayout;
  private Long maximumPayout;
  private Long maximumPayoutWithBonus;
  private Double forEvery;
  private Long willEarn;
  private Long payoutQuantity;
  private String payoutDescription;
  private Double minQualifier;
  private List<SSIContestStackRankTeamValueBean> stackRankParticipants;

  // Step it Up Contest
  private Double contestGoal;
  private String baselineType;
  private Double activity;
  private Double togo;
  private Long totalPayout;
  private Long totalPotentialPayout;
  private Long remainingPayout;
  private Long percPayout;
  private List<SSIPaxContestLevelValueBean> contestSiuLevels;

  // Stack Rank Contest
  private Double progress; // for activity amt
  private Long payoutCap; // for maximum points
  private Double payoutCapAmount;
  private List<SSIContestStackRankPayoutValueBean> payouts;

  // used for ssi_contest_creator_tile proc
  private Long participantId;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private Integer rank;
  private Double score;

  public void addStackRanks( List<SSIContestStackRankTeamValueBean> stackRanks, String activityMeasureType )
  {
    for ( SSIContestStackRankTeamValueBean stackRank : stackRanks )
    {
      stackRank.setActivityMeasureType( activityMeasureType );
    }
    this.setStackRankParticipants( stackRanks );
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

  public Double getGoal()
  {
    return goal;
  }

  public void setGoal( Double goal )
  {
    this.goal = goal;
  }

  public Double getTeamActivity()
  {
    return teamActivity;
  }

  public void setTeamActivity( Double teamActivity )
  {
    this.teamActivity = teamActivity;
  }

  public Long getPercentageAcheived()
  {
    return percentageAcheived;
  }

  public void setPercentageAcheived( Long percentageAcheived )
  {
    this.percentageAcheived = percentageAcheived;
  }

  public Integer getParticipantAchieved()
  {
    return participantAchieved;
  }

  public void setParticipantAchieved( Integer participantAchieved )
  {
    this.participantAchieved = participantAchieved;
  }

  public Integer getTotalParticipant()
  {
    return totalParticipant;
  }

  public void setTotalParticipant( Integer totalParticipant )
  {
    this.totalParticipant = totalParticipant;
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

  public Long getPayoutQuantity()
  {
    return payoutQuantity;
  }

  public void setPayoutQuantity( Long payoutQuantity )
  {
    this.payoutQuantity = payoutQuantity;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public Double getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( Double minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public List<SSIContestStackRankTeamValueBean> getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setStackRankParticipants( List<SSIContestStackRankTeamValueBean> stackRankParticipants )
  {
    this.stackRankParticipants = stackRankParticipants;
  }

  public Double getContestGoal()
  {
    return contestGoal;
  }

  public void setContestGoal( Double contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public String getBaselineType()
  {
    return baselineType;
  }

  public void setBaselineType( String baselineType )
  {
    this.baselineType = baselineType;
  }

  public Double getActivity()
  {
    return activity;
  }

  public void setActivity( Double activity )
  {
    this.activity = activity;
  }

  public Long getPercPayout()
  {
    return percPayout;
  }

  public void setPercPayout( Long percPayout )
  {
    this.percPayout = percPayout;
  }

  public List<SSIPaxContestLevelValueBean> getContestSiuLevels()
  {
    return contestSiuLevels;
  }

  public void setContestSiuLevels( List<SSIPaxContestLevelValueBean> contestSiuLevels )
  {
    this.contestSiuLevels = contestSiuLevels;
  }

  public List<SSIContestStackRankPayoutValueBean> getPayouts()
  {
    return payouts;
  }

  public void setPayouts( List<SSIContestStackRankPayoutValueBean> payouts )
  {
    this.payouts = payouts;
  }

  public Long getPayoutCap()
  {
    return payoutCap;
  }

  public void setPayoutCap( Long payoutCap )
  {
    this.payoutCap = payoutCap;
  }

  public Long getPotentialPayout()
  {
    return potentialPayout;
  }

  public void setPotentialPayout( Long potentialPayout )
  {
    this.potentialPayout = potentialPayout;
  }

  public Long getMaximumPayout()
  {
    return maximumPayout;
  }

  public void setMaximumPayout( Long maximumPayout )
  {
    this.maximumPayout = maximumPayout;
  }

  public Long getMaximumPayoutWithBonus()
  {
    return maximumPayoutWithBonus;
  }

  public void setMaximumPayoutWithBonus( Long maximumPayoutWithBonus )
  {
    this.maximumPayoutWithBonus = maximumPayoutWithBonus;
  }

  public Double getTogo()
  {
    return togo;
  }

  public void setTogo( Double togo )
  {
    this.togo = togo;
  }

  public Long getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( Long totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public Long getTotalPotentialPayout()
  {
    return totalPotentialPayout;
  }

  public void setTotalPotentialPayout( Long totalPotentialPayout )
  {
    this.totalPotentialPayout = totalPotentialPayout;
  }

  public Long getRemainingPayout()
  {
    return remainingPayout;
  }

  public void setRemainingPayout( Long remainingPayout )
  {
    this.remainingPayout = remainingPayout;
  }

  public Double getProgress()
  {
    return progress;
  }

  public void setProgress( Double progress )
  {
    this.progress = progress;
  }

  public Double getPayoutCapAmount()
  {
    return payoutCapAmount;
  }

  public void setPayoutCapAmount( Double payoutCapAmount )
  {
    this.payoutCapAmount = payoutCapAmount;
  }

  public Double getTotalObjectiveAmount()
  {
    return totalObjectiveAmount;
  }

  public void setTotalObjectiveAmount( Double totalObjectiveAmount )
  {
    this.totalObjectiveAmount = totalObjectiveAmount;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
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

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public Integer getRank()
  {
    return rank;
  }

  public void setRank( Integer rank )
  {
    this.rank = rank;
  }

  public Double getScore()
  {
    return score;
  }

  public void setScore( Double score )
  {
    this.score = score;
  }

}
