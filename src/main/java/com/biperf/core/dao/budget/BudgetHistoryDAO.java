
package com.biperf.core.dao.budget;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.budget.BudgetHistory;

public interface BudgetHistoryDAO extends DAO
{
  public static final String BEAN_NAME = "budgetHistoryDao";

  /**
   * Create the specified domain object in the database.
   * @param domain
   * @return
   */
  public void create( BudgetHistory domain );

}
