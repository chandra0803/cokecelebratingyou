
package com.biperf.core.ui.budget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class BudgetReallocationController extends BaseController
{

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    HashSet<BudgetMaster> budgetMasterList = getPromotionService().getEligibleBudgetTransfer( UserManager.getUser() );
    List<BudgetMaster> sortbudgetMasterList = new ArrayList<>( budgetMasterList );
    Collections.sort( sortbudgetMasterList, new BudgetMasterComparator() );
    request.setAttribute( "budgetMasterList", sortbudgetMasterList );

    if ( budgetMasterList == null || budgetMasterList.size() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.BUDGET_NONE_TO_TRANSFER ) );
      request.setAttribute( Globals.ERROR_KEY, errors );
      request.setAttribute( "isInfMsg", Boolean.TRUE );
    }

    BudgetReallocationForm budgetReallocationForm = (BudgetReallocationForm)request.getAttribute( "budgetReallocationForm" );
    if ( budgetReallocationForm.getBudgetMasterId() != null && budgetReallocationForm.getBudgetMasterId() > 0 )
    {
      BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterAndPromotionsByUser( budgetReallocationForm.getBudgetMasterId() );

      // populate Eligible budget segments for the selected budget master id
      List<BudgetSegment> budgetSegmentList = getBudgetMasterService().getBudgetSegmentsToTransferByBudgetMasterId( budgetMaster.getId() );
      request.setAttribute( "budgetSegmentList", budgetSegmentList );
      if ( budgetSegmentList != null && !budgetSegmentList.isEmpty() )
      {
        request.setAttribute( "budgetSegmentListSize", budgetSegmentList.size() );
      }

      if ( budgetReallocationForm.getBudgetSegmentId() != null && budgetReallocationForm.getBudgetSegmentId() > 0 )
      {
        BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetReallocationForm.getBudgetSegmentId() );

        if ( budgetSegmentList.contains( budgetSegment ) )
        {
          if ( budgetSegmentList == null || budgetSegmentList.size() == 0 )
          {
            ActionMessages errors = new ActionMessages();
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.BUDGET_NONE_TO_TRANSFER ) );
            request.setAttribute( Globals.ERROR_KEY, errors );
            request.setAttribute( "isInfMsg", Boolean.TRUE );
          }
          if ( budgetSegmentList != null && !budgetSegmentList.isEmpty() )
          {
            request.setAttribute( "budgetSegmentList", budgetSegmentList );
            request.setAttribute( "budgetSegmentListSize", budgetSegmentList.size() );
          }

          // Populate Node List
          if ( budgetMaster.isNodeBudget() )
          {
            List<Node> eligibleNodeList = new ArrayList<Node>();
            List<Budget> eligibleNodeBudgets = (List<Budget>)getBudgetMasterService().getAllActiveInBudgetSegmentForOwnerUserNode( budgetSegment.getId(), UserManager.getUserId() );
            for ( Budget eligibleNodeBudget : eligibleNodeBudgets )
            {
              eligibleNodeList.add( eligibleNodeBudget.getNode() );
            }

            if ( !eligibleNodeList.isEmpty() )
            {
              request.setAttribute( "eligibleNodeList", eligibleNodeList );
              request.setAttribute( "eligibleNodeListSize", eligibleNodeList.size() );
            }
          }

        }
      }
    }
  }

  class BudgetMasterComparator implements Comparator<BudgetMaster>
  {

    public int compare( BudgetMaster bean1, BudgetMaster bean2 )
    {
      return bean1.getBudgetMasterName().toLowerCase().compareTo( bean2.getBudgetMasterName().toLowerCase() );
    }
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
