
package com.biperf.core.ui.budget;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * BudgetSearchAction.
 * 
 *
 */
public class BudgetSearchAction extends BaseDispatchAction
{

  protected static final String FORWARD_SUCCESS_BUDGET_INFO = "success_budget_info";
  public static final String USER_BUDGET = "userBudget";
  public static final String NODE_BUDGET = "nodeBudget";

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = null;
    String searchBy = request.getParameter( "criteria" );
    String query = request.getParameter( "query" );

    long budgetSegmentId = RequestUtils.getRequiredParamLong( request, "budgetSegmentId" );

    if ( USER_BUDGET.equals( searchBy ) )
    {
      Budget budget = getBudgetMasterService().getAvailableUserBudgetByBudgetSegmentAndUserId( new Long( budgetSegmentId ), new Long( query ) );
      List budgetList = new ArrayList( 1 );
      if ( budget != null )
      {
        budgetList.add( budget );
      }
      request.setAttribute( "budgets", budgetList );
      return mapping.findForward( FORWARD_SUCCESS_BUDGET_INFO );
    }
    else if ( NODE_BUDGET.equals( searchBy ) )
    {
      Budget budget = getBudgetMasterService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( new Long( budgetSegmentId ), new Long( query ) );
      List budgetList = new ArrayList( 1 );
      if ( budget != null )
      {
        budgetList.add( budget );
      }
      request.setAttribute( "budgets", budgetList );
      return mapping.findForward( FORWARD_SUCCESS_BUDGET_INFO );
    }
    else
    {
      return null;
    }
  }

  /**
   * Retrieves a Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

}
