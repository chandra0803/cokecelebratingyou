/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/budget/BudgetMasterToOverdrawApproverAssociation.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.budget;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * BudgetMasterToOverdrawApproverAssociation.
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
 * <td>sharma</td>
 * <td>May 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterToOverdrawApproverAssociation extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    BudgetMaster budgetMaster = (BudgetMaster)domainObject;
  }

}
