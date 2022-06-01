/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/PendingGQAwardSummaryController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.goalquest;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;

/**
 * PendingGQAwardSummaryController.
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
 * <td>Jan 4, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PendingGQAwardSummaryController extends BaseController
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
    GoalQuestAwardSummary grandTotalGoalQuestAwardSummary = new GoalQuestAwardSummary();

    // Get the award summary
    PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary = getGoalQuestService().getGoalQuestAwardSummaryByPromotionId( getPromotionId( request ) );

    // Get the promotion
    GoalQuestPromotion promotion = (GoalQuestPromotion)pendingGoalQuestAwardSummary.getPromotion();

    // get participant goalquestawardsummary list
    List participantGoalQuestAwardSummaryList = pendingGoalQuestAwardSummary.getParticipantGoalQuestAwardSummaryList();
    calculateManagerGrandSummary( grandTotalGoalQuestAwardSummary, participantGoalQuestAwardSummaryList );
    request.setAttribute( "participantGoalQuestAwardSummaryList", participantGoalQuestAwardSummaryList );

    // get manager goalquestawardsummary list
    List managerGoalQuestAwardSummaryList = pendingGoalQuestAwardSummary.getManagerGoalQuestAwardSummaryList();
    calculateManagerGrandSummary( grandTotalGoalQuestAwardSummary, managerGoalQuestAwardSummaryList );
    request.setAttribute( "managerGoalQuestAwardSummaryList", managerGoalQuestAwardSummaryList );

    boolean isPartnerSummaryAvailable = false;
    if ( null != promotion.getPartnerAudienceType() )
    {
      isPartnerSummaryAvailable = true;
      // get manager goalquestawardsummary list
      List partnerGoalQuestAwardSummaryList = pendingGoalQuestAwardSummary.getPartnerGoalQuestAwardSummaryList();
      calculateManagerGrandSummary( grandTotalGoalQuestAwardSummary, partnerGoalQuestAwardSummaryList );
      request.setAttribute( "partnerGoalQuestAwardSummaryList", partnerGoalQuestAwardSummaryList );
    }

    // set goalcalculation list and promotion in session for later use in extract report
    HttpSession session = request.getSession();
    session.setAttribute( "goalCalculationList", pendingGoalQuestAwardSummary.getGoalCalculationResultList() );
    session.setAttribute( "promotion", promotion );

    // set as request attributes
    request.setAttribute( "partnerSummaryAvailable", new Boolean( isPartnerSummaryAvailable ) );
    request.setAttribute( "grandTotalGoalQuestAwardSummary", grandTotalGoalQuestAwardSummary );
    request.setAttribute( "promotion", promotion );
  }

  private void calculateGrandSummary( GoalQuestAwardSummary grandSummary, List summaryList )
  {
    int listSize = summaryList == null || summaryList.isEmpty() ? 0 : summaryList.size();
    if ( listSize > 0 )
    {
      GoalQuestAwardSummary totalAwardSummary = (GoalQuestAwardSummary)summaryList.get( listSize - 1 );
      grandSummary.incrementTotalSelected( totalAwardSummary.getTotalSelected() );
      grandSummary.incrementTotalAchieved( totalAwardSummary.getTotalAchieved() );
      grandSummary.incrementTotalAward( totalAwardSummary.getTotalAward() );
    }
  }

  private void calculateManagerGrandSummary( GoalQuestAwardSummary grandSummary, List summaryList )
  {
    int listSize = summaryList == null || summaryList.isEmpty() ? 0 : summaryList.size();
    if ( listSize > 0 )
    {
      for ( Iterator<GoalQuestAwardSummary> gqAwardSummary = summaryList.iterator(); gqAwardSummary.hasNext(); )
      {
        GoalQuestAwardSummary totalAwardSummary = (GoalQuestAwardSummary)gqAwardSummary.next();
        if ( gqAwardSummary.hasNext() )
        {
          grandSummary.incrementTotalAchieved( totalAwardSummary.getTotalAchieved() );
          grandSummary.incrementTotalAward( totalAwardSummary.getTotalAward() );
        }
      }
    }
  }

  private Long getPromotionId( HttpServletRequest request )
  {
    return new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "id" ) );
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
