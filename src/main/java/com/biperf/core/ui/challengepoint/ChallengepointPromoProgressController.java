
package com.biperf.core.ui.challengepoint;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ChallengepointPaxActivityType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
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
public class ChallengepointPromoProgressController extends BaseController
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
    if ( userId != null )
    {
      request.setAttribute( "userId", userId );
    }
    request.setAttribute( "promotionId", promotionId );
    Map<String, Object> progressMap = getChallengepointProgressService().getChallengepointProgressByPromotionIdAndUserId( promotionId, userId );
    request.setAttribute( "cpProgressList", progressMap.get( "challengepointProgressList" ) );
    if ( progressMap.get( "paxLevel" ) != null )
    {
      request.setAttribute( "paxValueBean", progressMap.get( "paxLevel" ) );
    }
    else
    {
      ChallengePointPromotion cpPromotion = getChallengePointService().getPromotion( promotionId );
      ChallengepointPaxValueBean cpPaxValueBean = getChallengePointService().populateChallengepointPaxValueBean( null, cpPromotion, null );
      request.setAttribute( "paxValueBean", cpPaxValueBean );
    }
    request.setAttribute( "addReplaceList", ChallengepointPaxActivityType.getList() );
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

  private ChallengePointService getChallengePointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }
}
