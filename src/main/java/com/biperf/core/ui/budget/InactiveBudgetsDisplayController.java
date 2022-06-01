/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/InactiveBudgetsDisplayController.java,v $
 */

package com.biperf.core.ui.budget;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.budget.BudgetMasterComparator;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.BudgetSegmentComparator;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.ui.BaseController;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * @author poddutur
 * @since Oct 28, 2015
 */
public class InactiveBudgetsDisplayController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    InactiveBudgetsForm inactiveBudgetsForm = (InactiveBudgetsForm)request.getAttribute( "inactiveBudgetsForm" );
    List budgetMasterList = getBudgetMasterService().getBudgetMasterListForDistribution();
    Collections.sort( budgetMasterList, new BudgetMasterComparator() );
    request.setAttribute( "budgetMasterList", budgetMasterList );
    request.setAttribute( "budgetMasterListSize", new Integer( budgetMasterList.size() ) );

    if ( inactiveBudgetsForm != null && inactiveBudgetsForm.getBudgetMasterId() != null )
    {
      Long budgetMasterId = inactiveBudgetsForm.getBudgetMasterId();
      List<BudgetSegment> budgetSegmentList = getBudgetMasterService().getBudgetSegmentsForDistribution( budgetMasterId );
      Collections.sort( budgetSegmentList, new BudgetSegmentComparator() );
      request.setAttribute( "budgetSegmentList", budgetSegmentList );
      request.setAttribute( "budgetSegmentListSize", budgetSegmentList.size() );

      if ( budgetSegmentList.isEmpty() )
      {
        request.setAttribute( "noBudgetSegmentsError", CmsResourceBundle.getCmsBundle().getString( "inactive.budgets.extract.NO_BUDGET_SEGMENTS_ERROR" ) );
      }
    }

  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

}
