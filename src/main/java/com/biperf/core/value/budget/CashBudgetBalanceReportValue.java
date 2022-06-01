/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/budget/BudgetBalanceReportValue.java,v $
 */

package com.biperf.core.value.budget;

import java.math.BigDecimal;

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
public class CashBudgetBalanceReportValue
{
  private Long promotionId;
  private String promotionName;
  private String budgetMasterName;
  private String budgetPeriod;
  private BigDecimal originalBudget;
  private BigDecimal budgetAdjustments;
  private BigDecimal awarded;
  private BigDecimal availableBalance;

  private BigDecimal cashPercentUsed;

  private String budgetOwnerName;
  private Long totalRecords;

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

  public String getBudgetPeriod()
  {
    return budgetPeriod;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public BigDecimal getOriginalBudget()
  {
    return originalBudget;
  }

  public void setOriginalBudget( BigDecimal originalBudget )
  {
    this.originalBudget = originalBudget;
  }

  public BigDecimal getBudgetAdjustments()
  {
    return budgetAdjustments;
  }

  public void setBudgetAdjustments( BigDecimal budgetAdjustments )
  {
    this.budgetAdjustments = budgetAdjustments;
  }

  public BigDecimal getAwarded()
  {
    return awarded;
  }

  public void setAwarded( BigDecimal awarded )
  {
    this.awarded = awarded;
  }

  public BigDecimal getAvailableBalance()
  {
    return availableBalance;
  }

  public void setAvailableBalance( BigDecimal availableBalance )
  {
    this.availableBalance = availableBalance;
  }

  public void setBudgetPeriod( String budgetPeriod )
  {
    this.budgetPeriod = budgetPeriod;
  }

  public BigDecimal getCashPercentUsed()
  {
    return cashPercentUsed;
  }

  public void setCashPercentUsed( BigDecimal cashPercentUsed )
  {
    this.cashPercentUsed = cashPercentUsed;
  }

  public String getBudgetOwnerName()
  {
    return budgetOwnerName;
  }

  public void setBudgetOwnerName( String budgetOwnerName )
  {
    this.budgetOwnerName = budgetOwnerName;
  }

  public Long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

}
