
package com.biperf.core.value.ssi;

/**
 * SSIContestPayoutStepItUpTotalsValueBean.
 * 
 * @author dudam
 * @since Feb 6, 2015
 * @version 1.0
 */
public class SSIContestPayoutStepItUpTotalsValueBean
{
  private Double activityAmount;
  private Long levelPayout;
  private Long bonusPayout;
  private Long totalPayout;// ALSO for payoutValue Total
  private Double baselineTotal;

  public Double getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( Double activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public Long getLevelPayout()
  {
    return levelPayout;
  }

  public void setLevelPayout( Long levelPayout )
  {
    this.levelPayout = levelPayout;
  }

  public Long getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( Long bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public Long getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( Long totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public Double getBaselineTotal()
  {
    return baselineTotal;
  }

  public void setBaselineTotal( Double baselineTotal )
  {
    this.baselineTotal = baselineTotal;
  }

}
