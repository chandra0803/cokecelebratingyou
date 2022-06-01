
package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.domain.budget.Budget;
import com.biperf.util.StringUtils;

public class BudgetReallocationValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  // Current Node owner and their budget details
  private String nodeName;
  private String ownerBudgetNodeId;
  private String adjustmentAmount;
  private String totalAdjustments;
  private String ownerBudgetAfterAdjustments;

  // child Node Owner and their budget details
  private Long childBudgetId;
  private String budgetSpent;
  private String currentBudget;
  private String childNodeOwnerNodeId;
  private String calculatedCurrentAmount;
  private String amountAfterAdjustment;
  private String userId;

  // full budget object - not always populated
  private Budget childBudget;

  private boolean NA;

  // constructor
  public BudgetReallocationValueBean()
  {

  }

  public String getChildNodeOwnerNodeId()
  {
    return childNodeOwnerNodeId;
  }

  public void setChildNodeOwnerNodeId( String childNodeOwnerNodeId )
  {
    this.childNodeOwnerNodeId = childNodeOwnerNodeId;
  }

  public String getAdjustmentAmount()
  {
    return adjustmentAmount;
  }

  public void setAdjustmentAmount( String adjustmentAmount )
  {
    this.adjustmentAmount = adjustmentAmount;
  }

  public void setChildBudgetId( Long childBudgetId )
  {
    this.childBudgetId = childBudgetId;
  }

  public Long getChildBudgetId()
  {
    return childBudgetId;
  }

  public String getBudgetSpent()
  {
    return budgetSpent;
  }

  public void setBudgetSpent( String budgetSpent )
  {
    this.budgetSpent = budgetSpent;
  }

  public String getCurrentBudget()
  {
    return currentBudget;
  }

  public void setCurrentBudget( String currentBudget )
  {
    this.currentBudget = currentBudget;
  }

  public void setOwnerBudgetNodeId( String ownerBudgetNodeId )
  {
    this.ownerBudgetNodeId = ownerBudgetNodeId;
  }

  public String getOwnerBudgetNodeId()
  {
    return ownerBudgetNodeId;
  }

  public void setTotalAdjustments( String totalAdjustments )
  {
    this.totalAdjustments = totalAdjustments;
  }

  public String getTotoalAdjustments()
  {
    return totalAdjustments;
  }

  public void setOwnerBudgetAfterAdjustments( String ownerBudgetAfterAdjustments )
  {
    this.ownerBudgetAfterAdjustments = ownerBudgetAfterAdjustments;
  }

  public String getOwnerBudgetAfterAdjustments()
  {
    return ownerBudgetAfterAdjustments;
  }

  public void setCalculatedCurrentAmount( String calculatedCurrentAmount )
  {
    this.calculatedCurrentAmount = calculatedCurrentAmount;
  }

  public String getCalculatedCurrentAmount()
  {
    return calculatedCurrentAmount;
  }

  public void setAmountAfterAdjustment( String amountAfterAdjustment )
  {
    this.amountAfterAdjustment = amountAfterAdjustment;
  }

  public String getAmountAfterAdjustment()
  {
    return amountAfterAdjustment;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( nodeName == null ) ? 0 : nodeName.hashCode() );
    result = prime * result + ( ( userId == null ) ? 0 : userId.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    BudgetReallocationValueBean other = (BudgetReallocationValueBean)obj;
    if ( !StringUtils.isEmpty( other.nodeName ) && this.nodeName.equals( other.nodeName ) )
    {
      return true;
    }
    if ( !StringUtils.isEmpty( other.userId ) && this.userId.equals( other.userId ) )
    {
      return true;
    }
    return false;
  }

  public void setChildBudget( Budget childBudget )
  {
    this.childBudget = childBudget;
  }

  public Budget getChildBudget()
  {
    return childBudget;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public boolean isNA()
  {
    return NA;
  }

  public void setNA( boolean nA )
  {
    NA = nA;
  }

}
