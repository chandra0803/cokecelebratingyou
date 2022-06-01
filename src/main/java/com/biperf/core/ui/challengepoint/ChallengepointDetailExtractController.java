/*
 * (c) 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.challengepoint;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;

/**
 * ChallengepointDetailExtractController.
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
 * <td>Babu</td>
 * <td>Aug 20, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ChallengepointDetailExtractController extends BaseController
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
    @SuppressWarnings( "unchecked" )
    List<ChallengepointPaxAwardValueBean> cpCalculationList = (List<ChallengepointPaxAwardValueBean>)session.getAttribute( "cpCalculationList" );
    ChallengePointPromotion promotion = (ChallengePointPromotion)session.getAttribute( "promotion" );
    // List cpManagerOverrideList = (List)session.getAttribute( "managerChallengepointAwardSummary"
    // );
    getChallengePointService().generateAndMailExtractReport( cpCalculationList, promotion, null );
    request.setAttribute( "promotion", promotion );
  }

  /**
   * Get the ChallengePointService from the beanLocator.
   * 
   * @return ChallengePointService
   */
  private ChallengePointService getChallengePointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }

}
