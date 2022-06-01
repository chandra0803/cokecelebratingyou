/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/budget/BudgetBalanceReportValue.java,v $
 */

package com.biperf.core.value.budget;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BudgetBalanceReportValue.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 29, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class BudgetBalanceReportValue
{
  private Long promotionId;
  private String promotionName;
  private String promotionDateRange;
  private String budgetName;
  private String budgetPeriod;
  private String budgetOwner;
  private String budgetDistribution;
  private String budgetType;
  private Long currentDeposited;
  private Long currentNetTransfers;
  private Long currentTotalBudget;
  private Long currentIssued;
  private Long currentBudgetRemaining;
  private BigDecimal currentBudgetUtilization;
  private Long priorPeriodDeposited;
  private Long priorPeriodNetTransfers;
  private Long priorPeriodTotalBudget;
  private Long priorPeriodIssued;
  private Long priorPeriodBudgetRemaining;
  private BigDecimal priorPeriodUtilization;
  private Long budgetId;
  private Date inceptionDate;

  //
  private Long deposited;
  private Long issued;
  private Long allocated;
  private Long remaining;
  private BigDecimal utilization;

  // detail
  private Long budgetAmount;
  private Date budgetDate;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionDateRange()
  {
    return promotionDateRange;
  }

  public void setPromotionDateRange( String promotionDateRange )
  {
    this.promotionDateRange = promotionDateRange;
  }

  public String getBudgetName()
  {
    return budgetName;
  }

  public void setBudgetName( String budgetName )
  {
    this.budgetName = budgetName;
  }

  public String getBudgetPeriod()
  {
    return budgetPeriod;
  }

  public void setBudgetPeriod( String budgetPeriod )
  {
    this.budgetPeriod = budgetPeriod;
  }

  public Long getCurrentDeposited()
  {
    return currentDeposited;
  }

  public void setCurrentDeposited( Long currentDeposited )
  {
    this.currentDeposited = currentDeposited;
  }

  public Long getCurrentNetTransfers()
  {
    return currentNetTransfers;
  }

  public void setCurrentNetTransfers( Long currentNetTransfers )
  {
    this.currentNetTransfers = currentNetTransfers;
  }

  public Long getCurrentTotalBudget()
  {
    return currentTotalBudget;
  }

  public void setCurrentTotalBudget( Long currentTotalBudget )
  {
    this.currentTotalBudget = currentTotalBudget;
  }

  public Long getCurrentIssued()
  {
    return currentIssued;
  }

  public void setCurrentIssued( Long currentIssued )
  {
    this.currentIssued = currentIssued;
  }

  public Long getCurrentBudgetRemaining()
  {
    return currentBudgetRemaining;
  }

  public void setCurrentBudgetRemaining( Long currentBudgetRemaining )
  {
    this.currentBudgetRemaining = currentBudgetRemaining;
  }

  public BigDecimal getCurrentBudgetUtilization()
  {
    return currentBudgetUtilization;
  }

  public void setCurrentBudgetUtilization( BigDecimal currentBudgetUtilization )
  {
    this.currentBudgetUtilization = currentBudgetUtilization;
  }

  public Long getPriorPeriodDeposited()
  {
    return priorPeriodDeposited;
  }

  public void setPriorPeriodDeposited( Long priorPeriodDeposited )
  {
    this.priorPeriodDeposited = priorPeriodDeposited;
  }

  public Long getPriorPeriodNetTransfers()
  {
    return priorPeriodNetTransfers;
  }

  public void setPriorPeriodNetTransfers( Long priorPeriodNetTransfers )
  {
    this.priorPeriodNetTransfers = priorPeriodNetTransfers;
  }

  public Long getPriorPeriodTotalBudget()
  {
    return priorPeriodTotalBudget;
  }

  public void setPriorPeriodTotalBudget( Long priorPeriodTotalBudget )
  {
    this.priorPeriodTotalBudget = priorPeriodTotalBudget;
  }

  public Long getPriorPeriodIssued()
  {
    return priorPeriodIssued;
  }

  public void setPriorPeriodIssued( Long priorPeriodIssued )
  {
    this.priorPeriodIssued = priorPeriodIssued;
  }

  public String getBudgetOwner()
  {
    return budgetOwner;
  }

  public void setBudgetOwner( String budgetOwner )
  {
    this.budgetOwner = budgetOwner;
  }

  public String getBudgetDistribution()
  {
    return budgetDistribution;
  }

  public void setBudgetDistribution( String budgetDistribution )
  {
    this.budgetDistribution = budgetDistribution;
  }

  public String getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public Date getInceptionDate()
  {
    return inceptionDate;
  }

  public void setInceptionDate( Date inceptionDate )
  {
    this.inceptionDate = inceptionDate;
  }

  public Long getPriorPeriodBudgetRemaining()
  {
    return priorPeriodBudgetRemaining;
  }

  public void setPriorPeriodBudgetRemaining( Long priorPeriodBudgetRemaining )
  {
    this.priorPeriodBudgetRemaining = priorPeriodBudgetRemaining;
  }

  public BigDecimal getPriorPeriodUtilization()
  {
    return priorPeriodUtilization;
  }

  public void setPriorPeriodUtilization( BigDecimal priorPeriodUtilization )
  {
    this.priorPeriodUtilization = priorPeriodUtilization;
  }

  public Long getBudgetAmount()
  {
    return budgetAmount;
  }

  public void setBudgetAmount( Long budgetAmount )
  {
    this.budgetAmount = budgetAmount;
  }

  public Date getBudgetDate()
  {
    return budgetDate;
  }

  public void setBudgetDate( Date budgetDate )
  {
    this.budgetDate = budgetDate;
  }

  //
  public Long getDeposited()
  {
    return deposited;
  }

  public void setDeposited( Long deposited )
  {
    this.deposited = deposited;
  }

  public Long getIssued()
  {
    return issued;
  }

  public void setIssued( Long issued )
  {
    this.issued = issued;
  }

  public Long getAllocated()
  {
    return allocated;
  }

  public void setAllocated( Long allocated )
  {
    this.allocated = allocated;
  }

  public Long getRemaining()
  {
    return remaining;
  }

  public void setRemaining( Long remaining )
  {
    this.remaining = remaining;
  }

  public BigDecimal getUtilization()
  {
    return utilization;
  }

  public void setUtilization( BigDecimal utilization )
  {
    this.utilization = utilization;
  }

}
