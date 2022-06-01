/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/challengepoint/PendingCPAwardSummaryController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.challengepoint;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.challengepoint.ChallengepointAwardSummary;
import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;

/**
 * PendingCPAwardSummaryController.
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
 * <td>Jul 31, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PendingCPAwardSummaryController extends BaseController
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
    ChallengepointAwardSummary grandTotalChallengepointAwardSummary = new ChallengepointAwardSummary();

    // Get the award summary
    PendingChallengepointAwardSummary pendingChallegenpointAwardSummary = getChallengepointProgressService().getAwardSummaryByLevels( getPromotionId( request ) );

    // Get the promotion
    ChallengePointPromotion promotion = (ChallengePointPromotion)pendingChallegenpointAwardSummary.getPromotion();

    // get participant Challengepointawardsummary list
    List participantChallengepointAwardSummaryList = pendingChallegenpointAwardSummary.getParticipantChallegenpointAwardSummaryList();
    calculateGrandSummary( grandTotalChallengepointAwardSummary, participantChallengepointAwardSummaryList );
    request.setAttribute( "participantChallengepointAwardSummaryList", participantChallengepointAwardSummaryList );

    boolean isPartnerSummaryAvailable = false;
    if ( null != promotion.getPartnerAudienceType() )
    {
      isPartnerSummaryAvailable = true;
      // get manager goalquestawardsummary list
      List partnerCPSummaryList = pendingChallegenpointAwardSummary.getPartnerCPAwardSummaryList();
      calculateGrandSummary( grandTotalChallengepointAwardSummary, partnerCPSummaryList );
      request.setAttribute( "partnerCPAwardSummaryList", partnerCPSummaryList );
    }

    // get manager Challengepointawardsummary list
    if ( pendingChallegenpointAwardSummary.getManagerChallengepointAwardSummary() != null )
    {
      calculateGrandSummary( grandTotalChallengepointAwardSummary, pendingChallegenpointAwardSummary.getManagerChallengepointAwardSummary() );
      request.setAttribute( "managerChallengepointAwardSummaryList", pendingChallegenpointAwardSummary.getManagerChallengepointAwardSummary() );
    }

    // set goalcalculation list and promotion in session for later use in extract report
    HttpSession session = request.getSession();
    session.setAttribute( "cpCalculationList", pendingChallegenpointAwardSummary.getChallengepointCalculationResultList() );
    session.setAttribute( "promotion", promotion );
    session.setAttribute( "managerChallengepointAwardSummary", pendingChallegenpointAwardSummary.getManagerOverrideResults() );
    // set as request attributes
    request.setAttribute( "grandTotalChallengepointAwardSummary", grandTotalChallengepointAwardSummary );
    request.setAttribute( "partnerSummaryAvailable", new Boolean( isPartnerSummaryAvailable ) );
    request.setAttribute( "promotion", promotion );
  }

  private void calculateGrandSummary( ChallengepointAwardSummary grandSummary, List summaryList )
  {
    int listSize = summaryList == null || summaryList.isEmpty() ? 0 : summaryList.size();
    if ( listSize > 0 )
    {
      for ( Iterator<ChallengepointAwardSummary> cpAwardSummary = summaryList.iterator(); cpAwardSummary.hasNext(); )
      {
        ChallengepointAwardSummary totalAwardSummary = (ChallengepointAwardSummary)cpAwardSummary.next();
        if ( cpAwardSummary.hasNext() )
        {
          grandSummary.incrementTotalAchieved( totalAwardSummary.getTotalAchieved() );
          grandSummary.incrementTotalAward( totalAwardSummary.getTotalAward() );
          grandSummary.incrementBasicPending( totalAwardSummary.getBasicPending() );
          grandSummary.setLeveloneAward( totalAwardSummary.isLeveloneAward() );
        }
      }
    }
  }

  private Long getPromotionId( HttpServletRequest request )
  {
    return new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "id" ) );
  }

  /**
   * Get the ChallengepointProgressService from the beanLocator.
   * 
   * @return ChallengepointProgressService
   */
  private ChallengepointProgressService getChallengepointProgressService()
  {
    return (ChallengepointProgressService)getService( ChallengepointProgressService.BEAN_NAME );
  }

}
