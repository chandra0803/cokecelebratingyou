
package com.biperf.core.value.ssi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIPaxContestLevelValueBean
{
  private Long id;
  private String name;
  private Long payout;
  private String payoutDescription;
  private Boolean currentLevel;
  private Boolean completed;
  private String goalPercent;
  private Double goalAmount;
  private Double goal;
  private Double progress;
  private Double remaining;
  private Long index; // sequence number from contestLevel
  private String progressFormatted;
  private Long remainingProgress;
  private Long levelMin;
  private Long levelMax;
  private Long baseline;
  private Long bonusEarned;

  // VALUES FOR PAX STACK RANK
  private int participantsCount;
  private int stackRank;
  private String avatarUrl;

  // Badge Info
  private Long badgeRuleId;
  private String badgeName;
  private String badgeUrl;

  public SSIPaxContestLevelValueBean()
  {
  }

  public SSIPaxContestLevelValueBean( Long index, Double goalAmount, int paxCount, Long payout, String payoutDesc )
  {
    this.index = index;
    this.goalAmount = goalAmount;
    this.goal = goalAmount;
    this.participantsCount = paxCount;
    this.payout = payout;
    this.payoutDescription = payoutDesc;
  }

  /**
   * @param id
   * @param name
   * @param payout
   * @param isCurrentLevel
   * @param isCompleted
   * @param goalPercent
   * @param goalAmount
   * @param goal
   * @param goalFormatted
   * @param progress
   * @param remaining
   * @param participantsCount
   * @param index
   * @param progressFormatted
   * @param remainingProgress
   * @param levelMin
   * @param levelMax
   */
  public SSIPaxContestLevelValueBean( Long id,
                                      String name,
                                      Long payout,
                                      Boolean currentLevel,
                                      Boolean completed,
                                      String goalPercent,
                                      Double goalAmount,
                                      Double progress,
                                      Double remaining,
                                      int participantsCount,
                                      Long index,
                                      String progressFormatted,
                                      Long remainingProgress,
                                      Long levelMin,
                                      Long levelMax )
  {
    super();
    this.id = id;
    this.name = name;
    this.payout = payout;
    this.currentLevel = currentLevel;
    this.completed = completed;
    this.goalPercent = goalPercent;
    this.goalAmount = goalAmount;
    this.progress = progress;
    this.remaining = remaining;
    this.participantsCount = participantsCount;
    this.index = index;
    this.progressFormatted = progressFormatted;
    this.remainingProgress = remainingProgress;
    this.levelMin = levelMin;
    this.levelMax = levelMax;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Long getPayout()
  {
    return payout;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public Boolean getCurrentLevel()
  {
    return currentLevel;
  }

  public void setCurrentLevel( Boolean currentLevel )
  {
    this.currentLevel = currentLevel;
  }

  public Boolean getCompleted()
  {
    return completed;
  }

  public void setCompleted( Boolean completed )
  {
    this.completed = completed;
  }

  public String getGoalPercent()
  {
    return goalPercent != null ? goalPercent : "0";
  }

  public void setGoalPercent( String goalPercent )
  {
    this.goalPercent = goalPercent;
  }

  public Double getGoalAmount()
  {
    return goalAmount;
  }

  public void setGoalAmount( Double goalAmount )
  {
    this.goalAmount = goalAmount;
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

  public int getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( int participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public Long getIndex()
  {
    return index;
  }

  public void setIndex( Long index )
  {
    this.index = index;
  }

  public String getProgressFormatted()
  {
    return progressFormatted;
  }

  public void setProgressFormatted( String progressFormatted )
  {
    this.progressFormatted = progressFormatted;
  }

  public Long getRemainingProgress()
  {
    return remainingProgress;
  }

  public void setRemainingProgress( Long remainingProgress )
  {
    this.remainingProgress = remainingProgress;
  }

  public Long getLevelMin()
  {
    return levelMin;
  }

  public void setLevelMin( Long levelMin )
  {
    this.levelMin = levelMin;
  }

  public Long getLevelMax()
  {
    return levelMax;
  }

  public void setLevelMax( Long levelMax )
  {
    this.levelMax = levelMax;
  }

  public Long getBaseline()
  {
    return baseline;
  }

  public void setBaseline( Long baseline )
  {
    this.baseline = baseline;
  }

  public int getStackRank()
  {
    return stackRank;
  }

  public void setStackRank( int stackRank )
  {
    this.stackRank = stackRank;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public Long getBadgeRuleId()
  {
    return badgeRuleId;
  }

  public void setBadgeRuleId( Long badgeRuleId )
  {
    this.badgeRuleId = badgeRuleId;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBadgeUrl()
  {
    return badgeUrl;
  }

  public void setBadgeUrl( String badgeUrl )
  {
    this.badgeUrl = badgeUrl;
  }

  public Double getGoal()
  {
    return goal;
  }

  public void setGoal( Double goal )
  {
    this.goal = goal;
  }

  public Long getBonusEarned()
  {
    return bonusEarned;
  }

  public void setBonusEarned( Long bonusEarned )
  {
    this.bonusEarned = bonusEarned;
  }

}
