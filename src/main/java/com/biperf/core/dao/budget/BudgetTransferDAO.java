
package com.biperf.core.dao.budget;

import java.util.List;

import com.biperf.core.dao.DAO;

public interface BudgetTransferDAO extends DAO
{
  public static final String BEAN_NAME = "budgetTransferDAO";

  public List getBudgetGivers();

  public List getBudgetReceivers();

  public Long getFromNodeOwnerBudgetAmount();

  public int updateReminderSent( Long mgrUserId );

}
