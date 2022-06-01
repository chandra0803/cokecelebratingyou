/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author poddutur
 * @since Aug 18, 2016
 */
public class NominationsApprovalBoxValueBean
{
  private boolean isCumulativeNomination;
  private String awardPayoutType;
  private BigDecimal budgetBalance;
  private boolean potentialBudgetExceeded;
  private String payoutDescription;
  private String otherAwardQuantity;
  private String budgetPeriodName;
  private Date lastBudgetRequestDate;
  private String previousLevelName;
  private String nextLevelName;
  private int pendingNominationsCount;

  public boolean isCumulativeNomination()
  {
    return isCumulativeNomination;
  }

  public void setCumulativeNomination( boolean isCumulativeNomination )
  {
    this.isCumulativeNomination = isCumulativeNomination;
  }

  public String getAwardPayoutType()
  {
    return awardPayoutType;
  }

  public void setAwardPayoutType( String awardPayoutType )
  {
    this.awardPayoutType = awardPayoutType;
  }

  public BigDecimal getBudgetBalance()
  {
    return budgetBalance;
  }

  public void setBudgetBalance( BigDecimal budgetBalance )
  {
    this.budgetBalance = budgetBalance;
  }

  public boolean isPotentialBudgetExceeded()
  {
    return potentialBudgetExceeded;
  }

  public void setPotentialBudgetExceeded( boolean potentialBudgetExceeded )
  {
    this.potentialBudgetExceeded = potentialBudgetExceeded;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getOtherAwardQuantity()
  {
    return otherAwardQuantity;
  }

  public void setOtherAwardQuantity( String otherAwardQuantity )
  {
    this.otherAwardQuantity = otherAwardQuantity;
  }

  public String getBudgetPeriodName()
  {
    return budgetPeriodName;
  }

  public void setBudgetPeriodName( String budgetPeriodName )
  {
    this.budgetPeriodName = budgetPeriodName;
  }

  public Date getLastBudgetRequestDate()
  {
    return lastBudgetRequestDate;
  }

  public void setLastBudgetRequestDate( Date lastBudgetRequestDate )
  {
    this.lastBudgetRequestDate = lastBudgetRequestDate;
  }

  public String getPreviousLevelName()
  {
    return previousLevelName;
  }

  public void setPreviousLevelName( String previousLevelName )
  {
    this.previousLevelName = previousLevelName;
  }

  public String getNextLevelName()
  {
    return nextLevelName;
  }

  public void setNextLevelName( String nextLevelName )
  {
    this.nextLevelName = nextLevelName;
  }

  public int getPendingNominationsCount()
  {
    return pendingNominationsCount;
  }

  public void setPendingNominationsCount( int pendingNominationsCount )
  {
    this.pendingNominationsCount = pendingNominationsCount;
  }

}
