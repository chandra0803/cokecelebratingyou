
package com.biperf.core.ui.budget;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.ui.participant.ParticipantSearchAjaxAction;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * ParticipantSearchAction.
 * 
 *
 */
public class ParticipantSearchAction extends ParticipantSearchAjaxAction
{

  protected static final String FORWARD_SUCCESS_BUDGET_INFO = "success_budget_info";
  private static final String USER_BUDGET = "userBudget";
  private static final String NODE_BUDGET = "nodeBudget";

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = null;
    String searchBy = request.getParameter( "criteria" );
    String query = request.getParameter( "query" );
    if ( USER_BUDGET.equals( searchBy ) )
    {
      return mapping.findForward( FORWARD_SUCCESS_BUDGET_INFO );
    }
    else if ( NODE_BUDGET.equals( searchBy ) )
    {
      return mapping.findForward( FORWARD_SUCCESS_BUDGET_INFO );
    }
    else
    {
      return super.execute( mapping, form, request, response );
    }
  }

  // override
  protected String doParticipantSearch( ParticipantSearchCriteria criteria, HttpServletRequest request )
  {
    long budgetSegmentId = RequestUtils.getRequiredParamLong( request, "budgetSegmentId" );
    Boolean forTransfer = RequestUtils.getOptionalParamBoolean( request, "transfer" );
    if ( forTransfer == null || !forTransfer.booleanValue() )
    {
      if ( budgetSegmentId != 0 )
      {
        criteria.setBudgetSegmentId( new Long( budgetSegmentId ) );
      }
    }
    return super.doParticipantSearch( criteria, request );
  }

  // override
  protected List filterUsers( List participants, HttpServletRequest request )
  {
    long budgetMasterId = RequestUtils.getOptionalParamLong( request, "budgetSegmentId" );
    long budgetId = RequestUtils.getOptionalParamLong( request, "budgetId" );
    Boolean forTransfer = RequestUtils.getOptionalParamBoolean( request, "transfer" );
    if ( budgetMasterId != 0 && forTransfer != null && forTransfer.booleanValue() )
    {
      List notActiveBudgets = getBudgetMasterService().getAllBudgetsNotActive( budgetMasterId );
      Budget currentBudget = getBudgetMasterService().getBudget( new Long( budgetMasterId ), new Long( budgetId ) );
      // filter nodes
      for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
      {
        Participant pax = (Participant)paxIter.next();
        // If this is the current budget then remove from list
        if ( pax.getId().equals( currentBudget.getUser().getId() ) )
        {
          paxIter.remove();
          continue;
        }
        // If the budget is not active remove from list
        for ( Iterator budgetIter = notActiveBudgets.iterator(); budgetIter.hasNext(); )
        {
          Budget budget = (Budget)budgetIter.next();
          if ( pax.getId().equals( budget.getUser().getId() ) )
          {
            paxIter.remove();
            break;
          }
        }
      }
    }
    return participants;

  }

  // override
  protected String doLocationSearch( String query, NodeSearchCriteria criteria, boolean isSecondLevelSearch, HttpServletRequest request )
  {
    long budgetSegmentId = RequestUtils.getOptionalParamLong( request, "budgetSegmentId" );
    Boolean forTransfer = RequestUtils.getOptionalParamBoolean( request, "transfer" );
    if ( forTransfer == null || !forTransfer.booleanValue() )
    {
      if ( budgetSegmentId != 0 )
      {
        if ( criteria == null )
        {
          criteria = new NodeSearchCriteria();
        }
        criteria.setBudgetSegmentId( new Long( budgetSegmentId ) );
      }
    }
    return super.doLocationSearch( query, criteria, isSecondLevelSearch, request );
  }

  // override
  protected List filterNodes( List nodes, HttpServletRequest request )
  {
    long budgetSegmentId = RequestUtils.getOptionalParamLong( request, "budgetSegmentId" );
    long budgetId = RequestUtils.getOptionalParamLong( request, "budgetId" );
    Boolean forTransfer = RequestUtils.getOptionalParamBoolean( request, "transfer" );
    if ( budgetSegmentId != 0 && forTransfer != null && forTransfer.booleanValue() )
    {
      List notActiveBudgets = getBudgetMasterService().getAllBudgetsNotActive( budgetSegmentId );
      Budget currentBudget = getBudgetMasterService().getBudget( new Long( budgetSegmentId ), new Long( budgetId ) );
      // filter nodes
      for ( Iterator nodeIter = nodes.iterator(); nodeIter.hasNext(); )
      {
        Node node = (Node)nodeIter.next();
        // If this is the current budget then remove from list
        // ToFix 20975 restrict budget transfer to deleted node
        if ( node.getId().equals( currentBudget.getNode().getId() ) || node.isDeleted() )
        {
          nodeIter.remove();
          continue;
        }
        // If the budget is not active remove from list
        for ( Iterator budgetIter = notActiveBudgets.iterator(); budgetIter.hasNext(); )
        {
          Budget budget = (Budget)budgetIter.next();
          if ( node.getId().equals( budget.getNode().getId() ) )
          {
            nodeIter.remove();
            break;
          }
        }
      }
    }
    return nodes;
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
