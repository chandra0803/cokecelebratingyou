
package com.biperf.core.ui.promotion;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.process.ThrowdownRoundPayoutProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.throwdown.ThrowdownRoundCalculationResult;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class ThrowdownProcessConfirmationController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // get promotion from session
    HttpSession session = request.getSession();
    ThrowdownRoundCalculationResult throwdownRoundCalculationResult = (ThrowdownRoundCalculationResult)session.getAttribute( "calculationResult" );
    ThrowdownPromotion promotion = throwdownRoundCalculationResult.getPromotion();
    if ( promotion != null )
    {
      String promotionName = promotion.getUpperCaseName();
      request.setAttribute( "promotionName", promotionName );
    }
    int roundNumber = throwdownRoundCalculationResult.getRoundNumber();
    Map throwdownPayoutParams = new HashMap();// getGoalQuestPayoutParameters();
    if ( promotion != null && promotion.getId() != null )
    {
      throwdownPayoutParams.put( "promotionId", new String[] { promotion.getId().toString() } );
    }
    throwdownPayoutParams.put( "roundNumber", new String[] { String.valueOf( roundNumber ) } );
    // create and launch the goal quest process for async processing of pay outs.
    Process process = getProcessService().createOrLoadSystemProcess( "throwdownRoundPayoutProcess", ThrowdownRoundPayoutProcess.BEAN_NAME );
    getProcessService().launchProcess( process, throwdownPayoutParams, UserManager.getUserId() );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }
}
