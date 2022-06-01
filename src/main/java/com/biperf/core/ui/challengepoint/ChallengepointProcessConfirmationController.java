/*
 * (c) 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.challengepoint;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.process.ChallengepointPayoutProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

/**
 * ChallengepointProcessConfirmationController.
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
 * <td>Aug 5, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ChallengepointProcessConfirmationController extends BaseController
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

    // get promotion from session
    HttpSession session = request.getSession();
    ChallengePointPromotion promotion = (ChallengePointPromotion)session.getAttribute( "promotion" );
    if ( promotion != null )
    {
      String promotionName = promotion.getUpperCaseName();
      request.setAttribute( "promotionName", promotionName );
    }
    Map challengepointPayoutParams = new HashMap();// getChallengepointPayoutParameters();
    if ( promotion != null && promotion.getId() != null )
    {
      challengepointPayoutParams.put( "promotionId", new String[] { promotion.getId().toString() } );
    }
    // create and launch the goal quest process for async processing of pay outs.
    Process process = getProcessService().createOrLoadSystemProcess( "challengepointPayoutProcess", ChallengepointPayoutProcess.BEAN_NAME );
    getProcessService().launchProcess( process, challengepointPayoutParams, UserManager.getUserId() );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

}
