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
public class PointsBudgetBalanceReportValue
{
  private String promotionId;
  private String promotionName;
  private String budgetMasterName;
  private String budgetPeriod;
  private Long originalBudget;
  private Long budgetAdjustments;
  private Long awarded;
  private Long availableBalance;

  private BigDecimal pointsPercentUsed;

  private String budgetOwnerName;
  private Long budgetSegmentId;
  private Long totalRecords;
  
  // Client customizations for WIP #27192 start
  private String promotionDateRange;

  public String getPromotionDateRange()
  {
    return promotionDateRange;
  }

  public void setPromotionDateRange( String promotionDateRange )
  {
    this.promotionDateRange = promotionDateRange;
  }
  // Client customizations for WIP #27192 end

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
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

  public Long getOriginalBudget()
  {
    return originalBudget;
  }

  public void setOriginalBudget( Long originalBudget )
  {
    this.originalBudget = originalBudget;
  }

  public Long getBudgetAdjustments()
  {
    return budgetAdjustments;
  }

  public void setBudgetAdjustments( Long budgetAdjustments )
  {
    this.budgetAdjustments = budgetAdjustments;
  }

  public Long getAwarded()
  {
    return awarded;
  }

  public void setAwarded( Long awarded )
  {
    this.awarded = awarded;
  }

  public Long getAvailableBalance()
  {
    return availableBalance;
  }

  public void setAvailableBalance( Long availableBalance )
  {
    this.availableBalance = availableBalance;
  }

  public void setBudgetPeriod( String budgetPeriod )
  {
    this.budgetPeriod = budgetPeriod;
  }

  public BigDecimal getPointsPercentUsed()
  {
    return pointsPercentUsed;
  }

  public void setPointsPercentUsed( BigDecimal pointsPercentUsed )
  {
    this.pointsPercentUsed = pointsPercentUsed;
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

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

}
