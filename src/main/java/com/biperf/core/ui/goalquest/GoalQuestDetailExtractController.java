/*
 * (c) 2007 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.goalquest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.ui.BaseController;

/**
 * GoalQuestProcessConfirmationController.
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
 * <td>Satish</td>
 * <td>Jan 11, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class GoalQuestDetailExtractController extends BaseController
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

    // get goalcalculation list from session
    HttpSession session = request.getSession();
    List goalCalculationList = (List)session.getAttribute( "goalCalculationList" );
    GoalQuestPromotion promotion = (GoalQuestPromotion)session.getAttribute( "promotion" );
    getGoalQuestService().generateAndMailExtractReport( goalCalculationList, promotion );
    String promotionName = null;
    request.setAttribute( "promotion", promotion );
  }

  /**
   * Get the GoalQuestService from the beanLocator.
   * 
   * @return GoalQuestService
   */
  private GoalQuestService getGoalQuestService()
  {
    return (GoalQuestService)getService( GoalQuestService.BEAN_NAME );
  }

}
