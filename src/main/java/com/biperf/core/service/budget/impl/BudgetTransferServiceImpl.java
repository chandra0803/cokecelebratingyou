
package com.biperf.core.service.budget.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.budget.BudgetTransferDAO;
import com.biperf.core.service.budget.BudgetTransferService;

public class BudgetTransferServiceImpl implements BudgetTransferService
{

  private static final Log logger = LogFactory.getLog( BudgetTransferServiceImpl.class );

  /* imports start */
  private BudgetTransferDAO budgetTransferDAO;

  /* imports end */

  public BudgetTransferDAO getbudgetTransferDAO()
  {
    return budgetTransferDAO;
  }

  public void setbudgetTransferDAO( BudgetTransferDAO budgetTransferDAO )
  {
    this.budgetTransferDAO = budgetTransferDAO;
  }

  /**
   * Gets the most recent batchId
   * @param month
   * @param year
   * @return
   */
  public List getBudgetGivers()
  {
    return budgetTransferDAO.getBudgetGivers();
  }

  public List getBudgetReceivers()
  {
    return budgetTransferDAO.getBudgetReceivers();
  }

  public Long getFromNodeOwnerBudgetAmount()
  {
    return budgetTransferDAO.getFromNodeOwnerBudgetAmount();
  }

  public int updateReminderSent( Long mgrUserId )
  {
    return budgetTransferDAO.updateReminderSent( mgrUserId );
  }

}
