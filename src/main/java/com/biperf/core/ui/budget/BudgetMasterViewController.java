/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetMasterViewController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.budget;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.BudgetSegmentComparator;
import com.biperf.core.domain.budget.BudgetSegmentValueBeanComparator;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterToOverdrawApproverAssociation;
import com.biperf.core.service.budget.BudgetSegmentToBudgetsAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.ActionFormUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * BudgetMasterViewController.
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
public class BudgetMasterViewController extends BaseController
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
    BudgetMasterForm budgetMasterForm = (BudgetMasterForm)ActionFormUtils.getActionForm( request, servletContext, "budgetMasterForm" );
    Long budgetMasterId = budgetMasterForm.getId();
    Long budgetSegmentId = budgetMasterForm.getBudgetSegmentId();
    // Bug Fix 20031. Get the Updated Budget Type From Encoded params.
    String strBudgetMasterId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "id" );
    String strBudgetSegmentId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "budgetSegmentId" );
    String budgetType = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "budgetType" );

    if ( budgetMasterId == null || budgetMasterId.longValue() == 0 )
    {
      if ( !com.biperf.util.StringUtils.isEmpty( strBudgetMasterId ) )
      {
        budgetMasterId = new Long( strBudgetMasterId );
      }
    }

    if ( budgetSegmentId == null || budgetSegmentId.longValue() == 0 )
    {
      if ( !com.biperf.util.StringUtils.isEmpty( strBudgetSegmentId ) )
      {
        budgetSegmentId = new Long( strBudgetSegmentId );
      }
    }

    if ( !com.biperf.util.StringUtils.isEmpty( budgetType ) )
    {
      budgetMasterForm.setBudgetType( budgetType );
    }
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, associationRequestCollection );
    budgetMaster.setBudgetName( CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );
    request.setAttribute( "budgetMaster", budgetMaster );
    request.setAttribute( "budgetMasterId", budgetMasterId );

    List budgetSegmentList = new ArrayList( budgetMaster.getBudgetSegments() );
    Collections.sort( budgetSegmentList, new BudgetSegmentComparator() );
    request.setAttribute( "budgetSegmentList", budgetSegmentList );

    if ( budgetSegmentId != null && budgetSegmentId != 0 )
    {
      BudgetSegment selectedBudgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegmentId );

      request.setAttribute( "selectedBudgetSegment", selectedBudgetSegment );
      request.setAttribute( "budgetSegmentId", budgetSegmentId );
      budgetMasterForm.setBudgetSegmentId( budgetSegmentId );
      budgetMasterForm.setBudgetSegmentName( selectedBudgetSegment.getDisplaySegmentName() );
    }

    List budgetSegmentVBList = budgetMasterForm.getBudgetSegmentVBList();
    for ( Iterator iter = budgetSegmentList.iterator(); iter.hasNext(); )
    {
      BudgetSegment budgetSegment = (BudgetSegment)iter.next();

      if ( BudgetType.CENTRAL_BUDGET_TYPE.equals( budgetMasterForm.getBudgetType() ) )
      {
        AssociationRequestCollection segmentAssociationRequestCollection = new AssociationRequestCollection();
        segmentAssociationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
        budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegment.getId(), segmentAssociationRequestCollection );
      }
      BudgetSegmentValueBean budgetSegmentValueBean = budgetMasterForm.loadBudgetSegment( budgetSegment, budgetMaster.getBudgetType() );
      budgetSegmentVBList.add( budgetSegmentValueBean );
    }
    Collections.sort( budgetSegmentVBList, new BudgetSegmentValueBeanComparator() );
    budgetMasterForm.setBudgetSegmentVBList( budgetSegmentVBList );
    request.setAttribute( "budgetSegmentVBList", budgetSegmentVBList );

    Collection budgetCollection = null;
    if ( BudgetType.NODE_BUDGET_TYPE.equals( budgetMasterForm.getBudgetType() ) || BudgetType.PAX_BUDGET_TYPE.equals( budgetMasterForm.getBudgetType() ) )
    {
      if ( BudgetMasterForm.ALL_BUDGETS.equals( budgetMasterForm.getBudgetsToShow() ) )
      {
        if ( budgetSegmentId != null )
        {
          List<Budget> budgets = getBudgetMasterService().getPaxorNodeBudgetsBySegmentIdForViewingBudgets( budgetSegmentId );
          if ( budgets != null )
          {
            budgetCollection = budgets;
          }
        }
      }
      else
      {
        if ( BudgetType.NODE_BUDGET_TYPE.equals( budgetMasterForm.getBudgetType() ) && budgetMasterForm.getSelectedNodeId() != null && budgetMasterForm.getSelectedNodeId().longValue() > 0 )
        {
          Budget selectedBudget = getBudgetMasterService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, budgetMasterForm.getSelectedNodeId() );
          if ( selectedBudget != null )
          {
            budgetCollection = new ArrayList( 1 );
            budgetCollection.add( selectedBudget );
          }
        }
        else if ( BudgetType.PAX_BUDGET_TYPE.equals( budgetMasterForm.getBudgetType() ) && budgetMasterForm.getSelectedParticipantId() != null
            && budgetMasterForm.getSelectedParticipantId().longValue() > 0 )
        {
          Budget selectedBudget = getBudgetMasterService().getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, budgetMasterForm.getSelectedParticipantId() );
          if ( selectedBudget != null )
          {
            budgetCollection = new ArrayList( 1 );
            budgetCollection.add( selectedBudget );
          }
        }
      }
    }
    if ( budgetCollection != null && budgetCollection.size() > 0 )
    {
      for ( Iterator iter = budgetCollection.iterator(); iter.hasNext(); )
      {
        Budget budget = (Budget)iter.next();
        if ( budgetMaster.isCentralBudget() && budget.isDeletable() )
        {
          request.setAttribute( "containsDeletableBudgets", Boolean.TRUE );
          break;
        }
        else if ( !budgetMaster.isCentralBudget() && budget.isBudgetDeletable() )
        {
          request.setAttribute( "containsDeletableBudgets", Boolean.TRUE );
          break;
        }
      }
      request.setAttribute( "budgetCollection", budgetCollection );
      request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( budgetCollection.size() ) ) );
    }
    else if ( budgetMasterForm.getBudgetsToShow() != null )
    {
      request.setAttribute( "noBudgetsFound", Boolean.TRUE );
    }
    if ( StringUtils.isBlank( budgetMasterForm.getBudgetsToShow() ) )
    {
      budgetMasterForm.setBudgetsToShow( BudgetMasterForm.ALL_BUDGETS );
    }

    if ( budgetMaster.getFinalPayoutRule() != null )
    {
      String finalPayoutText = MessageFormat.format( budgetMaster.getFinalPayoutRule().getName(),
                                                     new Object[] { CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.QUIZ_CLAIM_LABEL" ) } );

      request.setAttribute( "budgetFinalPayoutRuleText", finalPayoutText );
    }

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
