
package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.throwdown.StackRankingCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownRoundCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseController;

public class ThrowdownDetailExtractController extends BaseController
{

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    // get calculationResult and stackRankingResult from session
    HttpSession session = request.getSession();
    ThrowdownRoundCalculationResult throwdownRoundCalculationResult = (ThrowdownRoundCalculationResult)session.getAttribute( "calculationResult" );
    StackRankingCalculationResult stackRankingCalculationResult = (StackRankingCalculationResult)session.getAttribute( "stackRankingResult" );
    getThrowdownService().generateAndMailExtractReport( throwdownRoundCalculationResult.getPromotion(),
                                                        throwdownRoundCalculationResult.getRoundNumber(),
                                                        throwdownRoundCalculationResult,
                                                        stackRankingCalculationResult );
    request.setAttribute( "promotion", throwdownRoundCalculationResult.getPromotion() );
    request.setAttribute( "roundNumber", throwdownRoundCalculationResult.getRoundNumber() );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }
}
