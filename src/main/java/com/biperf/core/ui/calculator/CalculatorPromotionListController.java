/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/calculator/CalculatorPromotionListController.java,v $
 */

package com.biperf.core.ui.calculator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the QuizPromotionList page.
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
 * <td>jenniget</td>
 * <td>Nov 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorPromotionListController extends BaseController
{
  /**
   * Tiles controller for the QuizPromotionList page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    CalculatorFormListForm form = (CalculatorFormListForm)request.getAttribute( "calculatorFormListForm" );
    AssociationRequestCollection assocReqs = new AssociationRequestCollection();
    assocReqs.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.ALL ) );
    Calculator calculator = getCalculatorService().getCalculatorByIdWithAssociations( new Long( form.getCalculatorId() ), assocReqs );
    // Calculator calculator = getCalculatorService().getCalculatorById( new Long(
    // form.getCalculatorId()) ) ;
    request.setAttribute( "library", calculator );
  }

  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

}
