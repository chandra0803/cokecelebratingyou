/*
 * (c) 2018 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.promotion;

import java.util.Date;

/**
 * 
 * @author rajadura
 * @since Jan 29, 2018
 * 
 */
public class RecognitionAdvisorPromotionValueBean
{
  private Long promotionId;
  private String promotionName;
  private Date currentDate;
  private Date budgetExpDate;
  private Integer daysToBudgetExpiry;
  private Integer amountRemaining;
  private Long budgetId;
  private Long userId;
  private Long nodeId;
  private Long budgetMasterId;
  private Long budgetSegmentId;
  private Integer budgetAmt;

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

  public Date getCurrentDate()
  {
    return currentDate;
  }

  public void setCurrentDate( Date currentDate )
  {
    this.currentDate = currentDate;
  }

  public Date getBudgetExpDate()
  {
    return budgetExpDate;
  }

  public void setBudgetExpDate( Date budgetExpDate )
  {
    this.budgetExpDate = budgetExpDate;
  }

  public Integer getDaysToBudgetExpiry()
  {
    return daysToBudgetExpiry;
  }

  public void setDaysToBudgetExpiry( Integer daysToBudgetExpiry )
  {
    this.daysToBudgetExpiry = daysToBudgetExpiry;
  }

  public Integer getAmountRemaining()
  {
    return amountRemaining;
  }

  public void setAmountRemaining( Integer amountRemaining )
  {
    this.amountRemaining = amountRemaining;
  }

  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public Integer getBudgetAmt()
  {
    return budgetAmt;
  }

  public void setBudgetAmt( Integer budgetAmt )
  {
    this.budgetAmt = budgetAmt;
  }
}
