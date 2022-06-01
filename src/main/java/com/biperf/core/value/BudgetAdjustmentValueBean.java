
package com.biperf.core.value;

import java.io.Serializable;

public class BudgetAdjustmentValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String budgetName;
  private String budgetHistoryDate;
  private String adjustedAmount;

  public void setBudgetHistoryDate( String budgetHistoryDate )
  {
    this.budgetHistoryDate = budgetHistoryDate;
  }

  public String getBudgetHistoryDate()
  {
    return budgetHistoryDate;
  }

  public void setAdjustedAmount( String adjustedAmount )
  {
    this.adjustedAmount = adjustedAmount;
  }

  public String getAdjustedAmount()
  {
    return adjustedAmount;
  }

  public void setBudgetName( String budgetName )
  {
    this.budgetName = budgetName;
  }

  public String getBudgetName()
  {
    return budgetName;
  }

}
