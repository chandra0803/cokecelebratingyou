/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/budget/BudgetMasterListController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.budget;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.PresentationUtils;

/**
 * BudgetMasterListController.
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
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterListController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // get the list of master budgets
    List budgetMasterList = getBudgetMasterService().getAll();
    request.setAttribute( "budgetMasterList", budgetMasterList );
    request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( budgetMasterList.size() ) ) );
  }

  /**
   * Gets a Budget Service
   * 
   * @return BudgetService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end
}
