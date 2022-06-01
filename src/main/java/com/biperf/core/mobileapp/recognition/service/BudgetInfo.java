
package com.biperf.core.mobileapp.recognition.service;

import java.util.Date;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.utils.DateUtils;

public class BudgetInfo
{
  private final Long budgetId;
  private final Long nodeId;
  private final int originalValue;
  private final int currentValue;
  private final int daysRemaining;

  public static final int NO_BUDGET_END_DATE_DAYS_REMAINING = -1;

  public BudgetInfo( Long nodeId, Budget budget )
  {
    this.budgetId = budget.getId();
    this.nodeId = nodeId;
    originalValue = budget.getOriginalValueDisplay();
    currentValue = budget.getCurrentValueDisplay();

    if ( budget.getBudgetSegment().getEndDate() == null )
    {
      daysRemaining = NO_BUDGET_END_DATE_DAYS_REMAINING;
    }
    else
    {
      // always add one to the days remaining, so that if today is the last day,
      // the daysRemaining is one and not zero
      daysRemaining = DateUtils.getElapsedDays( new Date(), budget.getBudgetSegment().getEndDate() ) + 1;
    }
  }

  public Long getBudgetId()
  {
    return budgetId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public int getOriginalValue()
  {
    return originalValue;
  }

  public int getCurrentValue()
  {
    return currentValue;
  }

  public int getDaysRemaining()
  {
    return daysRemaining;
  }

  public boolean hasEndDate()
  {
    return daysRemaining != NO_BUDGET_END_DATE_DAYS_REMAINING;
  }
}
