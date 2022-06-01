
package com.biperf.core.value.ssi;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SSIContestPaxProgressDetailValueBean.
 * 
 * @author dudam
 * @since Jan 29, 2015
 * @version 1.0
 */
public class SSIContestPaxProgressDetailValueBean
{
  private Long contestId;
  private Double objectiveAmount;
  private Double activityAmount;
  private Double toGoAmount;
  private Integer percentageAcheived;
  private Long potentialPayout;
  private Long objectivePayout;
  private Long objectiveBonusPayout;
  private Long objectiveBonusIncrement;
  private Long bonusEarned;
  private Integer stackRank;
  private Integer totalPax;
  private Date lastProgressDate;
  private String avatarUrl;
  private Double minQualifier;
  private String objectiveDescription;
  private String objectivePayoutDescription;

  private List<SSIPaxDTGTActivityProgressValueBean> activities;// for DTGT

  private List<SSIPaxContestLevelValueBean> levels;// for SIU

  private Long behindLeader; // for SR
  private List<SSIContestStackRankPayoutValueBean> payouts;// for SR
  private Long payoutAmount; // for SR
  private String payoutDescription; // for SR
  private List<SSIContestStackRankTeamValueBean> stackRankParticipants; // for SR contest when it is
                                                                        // closed
  private Locale locale;
  private Long objectiveBonusCap;

  public SSIContestPaxProgressDetailValueBean()
  {

  }

  public SSIContestPaxProgressDetailValueBean( List<SSIPaxDTGTActivityProgressValueBean> activities )
  {
    this.activities = activities;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Double getObjectiveAmount()
  {
    return objectiveAmount;
  }

  public void setObjectiveAmount( Double objectiveAmount )
  {
    this.objectiveAmount = objectiveAmount;
  }

  public Double getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( Double activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public Double getToGoAmount()
  {
    return toGoAmount;
  }

  public void setToGoAmount( Double toGoAmount )
  {
    this.toGoAmount = toGoAmount;
  }

  public Integer getPercentageAcheived()
  {
    return percentageAcheived;
  }

  public void setPercentageAcheived( Integer percentageAcheived )
  {
    this.percentageAcheived = percentageAcheived;
  }

  public Long getPotentialPayout()
  {
    return potentialPayout;
  }

  public void setPotentialPayout( Long potentialPayout )
  {
    this.potentialPayout = potentialPayout;
  }

  public Long getObjectivePayout()
  {
    return objectivePayout;
  }

  public void setObjectivePayout( Long objectivePayout )
  {
    this.objectivePayout = objectivePayout;
  }

  public Long getObjectiveBonusPayout()
  {
    return objectiveBonusPayout;
  }

  public void setObjectiveBonusPayout( Long objectiveBonusPayout )
  {
    this.objectiveBonusPayout = objectiveBonusPayout;
  }

  public Long getObjectiveBonusIncrement()
  {
    return objectiveBonusIncrement;
  }

  public void setObjectiveBonusIncrement( Long objectiveBonusIncrement )
  {
    this.objectiveBonusIncrement = objectiveBonusIncrement;
  }

  public Integer getStackRank()
  {
    return stackRank;
  }

  public void setStackRank( Integer stackRank )
  {
    this.stackRank = stackRank;
  }

  public Integer getTotalPax()
  {
    return totalPax;
  }

  public void setTotalPax( Integer totalPax )
  {
    this.totalPax = totalPax;
  }

  public Date getLastProgressDate()
  {
    return lastProgressDate;
  }

  public void setLastProgressDate( Date lastProgressDate )
  {
    this.lastProgressDate = lastProgressDate;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public List<SSIPaxDTGTActivityProgressValueBean> getActivities()
  {
    return activities;
  }

  public void setActivities( List<SSIPaxDTGTActivityProgressValueBean> activities )
  {
    this.activities = activities;
  }

  public List<SSIPaxContestLevelValueBean> getLevels()
  {
    return levels;
  }

  public void setLevels( List<SSIPaxContestLevelValueBean> levels )
  {
    this.levels = levels;
  }

  public Long getBehindLeader()
  {
    return behindLeader;
  }

  public void setBehindLeader( Long behindLeader )
  {
    this.behindLeader = behindLeader;
  }

  public List<SSIContestStackRankPayoutValueBean> getPayouts()
  {
    return payouts;
  }

  public void setPayouts( List<SSIContestStackRankPayoutValueBean> payouts )
  {
    this.payouts = payouts;
  }

  public Double getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( Double minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public String getObjectiveDescription()
  {
    return objectiveDescription;
  }

  public void setObjectiveDescription( String objectiveDescription )
  {
    this.objectiveDescription = objectiveDescription;
  }

  public String getObjectivePayoutDescription()
  {
    return objectivePayoutDescription;
  }

  public void setObjectivePayoutDescription( String objectivePayoutDescription )
  {
    this.objectivePayoutDescription = objectivePayoutDescription;
  }

  public Long getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Long payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public List<SSIContestStackRankTeamValueBean> getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setStackRankParticipants( List<SSIContestStackRankTeamValueBean> stackRankParticipants )
  {
    this.stackRankParticipants = stackRankParticipants;
  }

  public Long getBonusEarned()
  {
    return bonusEarned;
  }

  public void setBonusEarned( Long bonusEarned )
  {
    this.bonusEarned = bonusEarned;
  }

  public Locale getLocale()
  {
    return locale;
  }

  public void setLocale( Locale locale )
  {
    this.locale = locale;
  }

  public Long getObjectiveBonusCap()
  {
    return objectiveBonusCap;
  }

  public void setObjectiveBonusCap( Long objectiveBonusCap )
  {
    this.objectiveBonusCap = objectiveBonusCap;
  }

}
