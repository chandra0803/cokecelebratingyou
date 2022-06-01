
package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BudgetMeterData implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Long budgetMasterId;
  private String budgetMasterType;
  private String budgetMasterName;
  private Long budgetId;
  private String budgetNodeName;
  private Long budgetNodeId;
  private Boolean budgetNodeIsPrimary;
  // Bug 67645 start
  private BigDecimal usedBudget;
  private BigDecimal remainingBudget;
  private BigDecimal totalBudget;
  // Bug 67645 end
  private Long promotionId;
  private String promotionName;
  private Date promotionStartDate;
  private Date promotionEndDate;
  private Date budgetStartDate;
  private Date budgetEndDate;

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public String getBudgetMasterType()
  {
    return budgetMasterType;
  }

  public void setBudgetMasterType( String budgetMasterType )
  {
    this.budgetMasterType = budgetMasterType;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public String getBudgetNodeName()
  {
    return budgetNodeName;
  }

  public void setBudgetNodeName( String budgetNodeName )
  {
    this.budgetNodeName = budgetNodeName;
  }

  public void setBudgetNodeId( Long budgetNodeId )
  {
    this.budgetNodeId = budgetNodeId;
  }

  public Long getBudgetNodeId()
  {
    return budgetNodeId;
  }

  public void setBudgetNodeIsPrimary( Boolean budgetNodeIsPrimary )
  {
    this.budgetNodeIsPrimary = budgetNodeIsPrimary;
  }

  public Boolean getBudgetNodeIsPrimary()
  {
    return budgetNodeIsPrimary;
  }

  // Bug 67645 start
  public BigDecimal getUsedBudget()
  {
    return usedBudget;
  }

  public void setUsedBudget( BigDecimal usedBudget )
  {
    this.usedBudget = usedBudget;
  }

  public BigDecimal getRemainingBudget()
  {
    return remainingBudget;
  }

  public void setRemainingBudget( BigDecimal remainingBudget )
  {
    this.remainingBudget = remainingBudget;
  }

  public BigDecimal getTotalBudget()
  {
    return totalBudget;
  }

  public void setTotalBudget( BigDecimal totalBudget )
  {
    this.totalBudget = totalBudget;
  }

  // Bug 67645 end
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

  public Date getPromotionStartDate()
  {
    return promotionStartDate;
  }

  public void setPromotionStartDate( Date promotionStartDate )
  {
    this.promotionStartDate = promotionStartDate;
  }

  public Date getPromotionEndDate()
  {
    return promotionEndDate;
  }

  public void setPromotionEndDate( Date promotionEndDate )
  {
    this.promotionEndDate = promotionEndDate;
  }

  public Date getBudgetStartDate()
  {
    return budgetStartDate;
  }

  public void setBudgetStartDate( Date budgetStartDate )
  {
    this.budgetStartDate = budgetStartDate;
  }

  public Date getBudgetEndDate()
  {
    return budgetEndDate;
  }

  public void setBudgetEndDate( Date budgetEndDate )
  {
    this.budgetEndDate = budgetEndDate;
  }

}
