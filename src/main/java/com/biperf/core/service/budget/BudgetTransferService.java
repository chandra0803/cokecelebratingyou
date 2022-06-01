
package com.biperf.core.service.budget;

import java.util.List;

public interface BudgetTransferService
{

  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "BudgetTransferService";

  /**
   * Gets list of managers participants for service anniversary calendar
   * @param month
   * @param year
   * @return
   */
  public List getBudgetGivers();

  public List getBudgetReceivers();

  public Long getFromNodeOwnerBudgetAmount();

  public int updateReminderSent( Long mgrUserId );

}
