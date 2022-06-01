
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestUniqueCheckValueBean.
 * 
 * @author kandhi
 * @since Dec 18, 2014
 * @version 1.0
 */
public class SSIContestUniqueCheckValueBean
{
  private boolean activityDescSame;
  private boolean amountSame;
  private boolean payoutSame;
  private boolean payoutDescSame;
  private boolean bonusSame;
  private boolean bonusCapSame;
  private String activityDesc;
  private Double amount;
  private Long payout;
  private String payoutDesc;
  private Long bonusIncrement;
  private Long bonusPayout;
  private Long bonusCap;
  private Double totalAmount;
  private Long totalPayout;
  private Long totalBonusCap;

  public Double getTotalAmount()
  {
    return totalAmount;
  }

  public void setTotalAmount( Double totalAmount )
  {
    this.totalAmount = totalAmount;
  }

  public Long getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( Long totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public Long getTotalBonusCap()
  {
    return totalBonusCap;
  }

  public void setTotalBonusCap( Long totalBonusCap )
  {
    this.totalBonusCap = totalBonusCap;
  }

  public boolean isActivityDescSame()
  {
    return activityDescSame;
  }

  public void setActivityDescSame( boolean activityDescSame )
  {
    this.activityDescSame = activityDescSame;
  }

  public boolean isAmountSame()
  {
    return amountSame;
  }

  public void setAmountSame( boolean amountSame )
  {
    this.amountSame = amountSame;
  }

  public boolean isPayoutSame()
  {
    return payoutSame;
  }

  public void setPayoutSame( boolean payoutSame )
  {
    this.payoutSame = payoutSame;
  }

  public boolean isPayoutDescSame()
  {
    return payoutDescSame;
  }

  public void setPayoutDescSame( boolean payoutDescSame )
  {
    this.payoutDescSame = payoutDescSame;
  }

  public boolean isBonusSame()
  {
    return bonusSame;
  }

  public void setBonusSame( boolean bonusSame )
  {
    this.bonusSame = bonusSame;
  }

  public boolean isBonusCapSame()
  {
    return bonusCapSame;
  }

  public void setBonusCapSame( boolean bonusCapSame )
  {
    this.bonusCapSame = bonusCapSame;
  }

  public String getActivityDesc()
  {
    return activityDesc;
  }

  public void setActivityDesc( String activityDesc )
  {
    this.activityDesc = activityDesc;
  }

  public Double getAmount()
  {
    return amount;
  }

  public void setAmount( Double amount )
  {
    this.amount = amount;
  }

  public Long getPayout()
  {
    return payout;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  public String getPayoutDesc()
  {
    return payoutDesc;
  }

  public void setPayoutDesc( String payoutDesc )
  {
    this.payoutDesc = payoutDesc;
  }

  public Long getBonusIncrement()
  {
    return bonusIncrement;
  }

  public void setBonusIncrement( Long bonusIncrement )
  {
    this.bonusIncrement = bonusIncrement;
  }

  public Long getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( Long bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public Long getBonusCap()
  {
    return bonusCap;
  }

  public void setBonusCap( Long bonusCap )
  {
    this.bonusCap = bonusCap;
  }

}
