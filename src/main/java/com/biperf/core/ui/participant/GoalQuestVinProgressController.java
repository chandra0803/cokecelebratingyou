/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.participant;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.value.GoalLevelValueBean;

/**
 * GoalQuestVinProgressController.
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
 * <td>meadows</td>
 * <td>Mar 22, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestVinProgressController extends BaseController
{

  /**
   * Overridden from @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
    Long userId = null;
    String userIdString = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( userIdString != null )
    {
      userId = new Long( userIdString );
    }
    if ( userId != null )
    {
      request.setAttribute( "userId", userId );
    }
    request.setAttribute( "promotionId", promotionId );
    Map progressMap = getGoalQuestService().getGoalQuestProgressByPromotionIdAndUserId( promotionId, userId );
    List goalQuestProgressList = (List)progressMap.get( "goalQuestProgressList" );
    request.setAttribute( "goalQuestProgressList", goalQuestProgressList );
    boolean isAutomotive = false;
    if ( goalQuestProgressList != null && goalQuestProgressList.size() > 0 )
    {
      GoalQuestReviewProgress goalQuestReviewProgress = (GoalQuestReviewProgress)goalQuestProgressList.get( 0 );
      isAutomotive = goalQuestReviewProgress.isAutomotive();
    }
    request.setAttribute( "isAutomotive", new Boolean( isAutomotive ) );
    PaxGoal paxGoal = (PaxGoal)progressMap.get( "paxGoal" );
    if ( paxGoal != null )
    {
      request.setAttribute( "paxGoal", paxGoal );
      GoalQuestPromotion promotion = paxGoal.getGoalQuestPromotion();
      GoalLevelValueBean goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( paxGoal, promotion, (GoalLevel)paxGoal.getGoalLevel() );
      request.setAttribute( "goalLevelValueBean", goalLevelValueBean );
    }
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
