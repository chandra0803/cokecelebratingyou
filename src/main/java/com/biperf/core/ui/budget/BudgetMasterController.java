/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetMasterController.java,v $
 */

package com.biperf.core.ui.budget;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.util.LabelValueBean;

import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetReallocationEligType;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class BudgetMasterController extends BaseController
{
  /**
   * Fetches data for the Add Budget Master page.
   *
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    request.setAttribute( "hardCapBudgetType", BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    request.setAttribute( "budgetFinalPayoutRuleList", populateBudgetFinalPayoutRule() );
    request.setAttribute( "budgetReallocationEligTypeList", BudgetReallocationEligType.getList() );
    request.setAttribute( "currentDate", DateUtils.getCurrentDateTrimmed() );
  }

  /**
   * @return list of budget final payout rules for Break the Bank functionality 
   * (strip off text in picklist items to display what's appropriate for Product Claim)
   */
  private List populateBudgetFinalPayoutRule()
  {
    List finalPayoutRuleList = BudgetFinalPayoutRule.getList();

    List finalPayoutRuleDisplayList = new ArrayList();

    Iterator ruleIter = finalPayoutRuleList.iterator();
    while ( ruleIter.hasNext() )
    {
      BudgetFinalPayoutRule payoutRule = (BudgetFinalPayoutRule)ruleIter.next();
      LabelValueBean labelValueBean = new LabelValueBean();

      labelValueBean.setValue( payoutRule.getCode() );
      labelValueBean.setLabel( MessageFormat.format( payoutRule.getName(), new Object[] { CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.QUIZ_CLAIM_LABEL" ) } ) );

      finalPayoutRuleDisplayList.add( labelValueBean );
    }
    return finalPayoutRuleDisplayList;
  }
}
