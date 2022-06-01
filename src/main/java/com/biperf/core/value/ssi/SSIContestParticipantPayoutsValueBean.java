
package com.biperf.core.value.ssi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestParticipantPayoutsValueBean
{
  private String id;
  private String lastName;
  private String firstName;

  private boolean includeBonus = false;
  private boolean optOutAwards = false;

  // Objectives Data
  private String goal;

  // Objectives, Stck Rank, DTGT Data
  private String progress;

  // Objectives Points Data
  private String objectivePayout;

  // Objectives;Step It Up Points Data
  private String bonusPayout;

  // Objectives,Stack Rank Points Data
  private String payout;

  // Objectives,Step It Up Other Data
  private String qty;

  // Objectives,Step It Up,Stack rank,DTGT Other Data
  private String payoutValue;
  private String payoutDescription;

  // Step It Up Data
  private String activityAmount;
  private String levelAchieved;

  // Step It Up Points Data
  private String levelPayout;

  // Step It Up, DTGT Points Data
  private String totalPayout;

  // Stck Rank Data
  private SSIContestPaxPayoutBadgeValueBean badge;
  private String rank;

  // DTGT Data
  private String qualifiedActivity;
  private String payoutIncrements;

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getQualifiedActivity()
  {
    return qualifiedActivity;
  }

  public void setQualifiedActivity( String qualifiedActivity )
  {
    this.qualifiedActivity = qualifiedActivity;
  }

  public String getPayoutIncrements()
  {
    return payoutIncrements;
  }

  public void setPayoutIncrements( String payoutIncrements )
  {
    this.payoutIncrements = payoutIncrements;
  }

  public SSIContestPaxPayoutBadgeValueBean getBadge()
  {
    return badge;
  }

  public void setBadge( SSIContestPaxPayoutBadgeValueBean badge )
  {
    this.badge = badge;
  }

  public String getRank()
  {
    return rank;
  }

  public void setRank( String rank )
  {
    this.rank = rank;
  }

  public String getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( String activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public String getLevelAchieved()
  {
    return levelAchieved;
  }

  public void setLevelAchieved( String levelAchieved )
  {
    this.levelAchieved = levelAchieved;
  }

  public String getLevelPayout()
  {
    return levelPayout;
  }

  public void setLevelPayout( String levelPayout )
  {
    this.levelPayout = levelPayout;
  }

  public String getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( String totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
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
    return progress;
  }

  public void setProgress( String progress )
  {
    this.progress = progress;
  }

  public String getObjectivePayout()
  {
    return objectivePayout;
  }

  public void setObjectivePayout( String objectivePayout )
  {
    this.objectivePayout = objectivePayout;
  }

  public String getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( String bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public String getPayout()
  {
    return payout;
  }

  public void setPayout( String payout )
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

  public String getQty()
  {
    return qty;
  }

  public void setQty( String qty )
  {
    this.qty = qty;
  }

  public String getPayoutValue()
  {
    return payoutValue;
  }

  public void setPayoutValue( String payoutValue )
  {
    this.payoutValue = payoutValue;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

}
