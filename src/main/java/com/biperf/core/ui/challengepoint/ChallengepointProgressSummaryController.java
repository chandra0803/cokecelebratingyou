
package com.biperf.core.ui.challengepoint;

import java.math.BigDecimal;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxValueBean;

/**
 * ChallengepointPromoProgressController.
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
 * <td>reddy</td>
 * <td>Jul 16, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointProgressSummaryController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
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
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
    Long userId = null;
    String userIdString = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( userIdString != null )
    {
      userId = new Long( userIdString );
    }
    if ( userId == null || userId.longValue() == 0 )
    {
      userId = UserManager.getUserId();
    }

    request.setAttribute( "userId", userId );
    request.setAttribute( "promotionId", promotionId );
    ChallengepointPaxValueBean challengepointPaxValueBean = getChallengepointProgressService().getChallengepointProgressSummary( promotionId, userId );
    // BugFix 21473.
    if ( challengepointPaxValueBean.getPaxGoal() != null && challengepointPaxValueBean.getPaxGoal().getCurrentValue() == null )
    {
      challengepointPaxValueBean.getPaxGoal().setCurrentValue( new BigDecimal( 0 ) );
    }
    request.setAttribute( "cpPaxBean", challengepointPaxValueBean );

  }

  /**
   * Get the ChallengePointService from the beanLocator.
   * 
   * @return ChallengePointService
   */
  private ChallengepointProgressService getChallengepointProgressService()
  {
    return (ChallengepointProgressService)getService( ChallengepointProgressService.BEAN_NAME );
  }

}
