
package com.biperf.core.value.ssi;

import java.util.ArrayList;
import java.util.List;

public class SSIContestPayoutsValueBean
{
  // Objectives
  private String totalGoal;
  private String totalProgress;

  // Objectives points
  private String totalObjectivePayout;

  // Objectives; Step It up points
  private String totalBonusPayout;

  // Objectives;StackRank;Step It up points
  private String totalPayout;

  // Objectives;StackRank;Step It up other
  private String totalPayoutValue;

  // stack rank; Step It Up
  private String totalActivity;

  // StackRank Tied
  private String payoutCap;
  private boolean hasTie;

  // Step It up points
  private String totalLevelPayout;

  private int totalParticipantCount;

  // DTGT Activity Totals
  private List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> dtgtActivityPayoutTotals = new ArrayList<SSIContestDoThisGetThatActivityPayoutsTotalValueBean>();

  // Participant Payouts
  private List<SSIContestParticipantPayoutsValueBean> partiticpantPayoutsList = new ArrayList<SSIContestParticipantPayoutsValueBean>();

  private boolean includeBonus = false;

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public int getTotalParticipantCount()
  {
    return totalParticipantCount;
  }

  public void setTotalParticipantCount( int totalParticipantCount )
  {
    this.totalParticipantCount = totalParticipantCount;
  }

  public String getTotalGoal()
  {
    return totalGoal;
  }

  public void setTotalGoal( String totalGoal )
  {
    this.totalGoal = totalGoal;
  }

  public String getTotalProgress()
  {
    return totalProgress;
  }

  public void setTotalProgress( String totalProgress )
  {
    this.totalProgress = totalProgress;
  }

  public String getTotalObjectivePayout()
  {
    return totalObjectivePayout;
  }

  public void setTotalObjectivePayout( String totalObjectivePayout )
  {
    this.totalObjectivePayout = totalObjectivePayout;
  }

  public String getTotalBonusPayout()
  {
    return totalBonusPayout;
  }

  public void setTotalBonusPayout( String totalBonusPayout )
  {
    this.totalBonusPayout = totalBonusPayout;
  }

  public String getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( String totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public String getTotalPayoutValue()
  {
    return totalPayoutValue;
  }

  public void setTotalPayoutValue( String totalPayoutValue )
  {
    this.totalPayoutValue = totalPayoutValue;
  }

  public String getTotalActivity()
  {
    return totalActivity;
  }

  public void setTotalActivity( String totalActivity )
  {
    this.totalActivity = totalActivity;
  }

  public String getPayoutCap()
  {
    return payoutCap;
  }

  public void setPayoutCap( String payoutCap )
  {
    this.payoutCap = payoutCap;
  }

  public boolean isHasTie()
  {
    return hasTie;
  }

  public void setHasTie( boolean hasTie )
  {
    this.hasTie = hasTie;
  }

  public String getTotalLevelPayout()
  {
    return totalLevelPayout;
  }

  public void setTotalLevelPayout( String totalLevelPayout )
  {
    this.totalLevelPayout = totalLevelPayout;
  }

  public List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> getDtgtActivityPayoutTotals()
  {
    return dtgtActivityPayoutTotals;
  }

  public void setDtgtActivityPayoutTotals( List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> dtgtActivityPayoutTotals )
  {
    this.dtgtActivityPayoutTotals = dtgtActivityPayoutTotals;
  }

  public List<SSIContestParticipantPayoutsValueBean> getPartiticpantPayoutsList()
  {
    return partiticpantPayoutsList;
  }

  public void setPartiticpantPayoutsList( List<SSIContestParticipantPayoutsValueBean> partiticpantPayoutsList )
  {
    this.partiticpantPayoutsList = partiticpantPayoutsList;
  }

}
