
package com.biperf.core.ui.promotion;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.promotion.ThrowdownAwardRunCalculationView;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.StackRankingCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownRoundCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.ClientStateUtils;

public class ThrowdownAwardSummaryAction extends BaseDispatchAction
{
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "id" );
    String roundNumber = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "roundNumber" );

    Map<String, String> parameterMap = new HashMap<String, String>();
    parameterMap.put( "promotionId", promotionId );
    parameterMap.put( "roundNumber", roundNumber );
    String viewUrl = ClientStateUtils.generateEncodedLink( "", "/promotionThrowDown/throwdownAwardSummary.do?method=runCalculation", parameterMap );
    request.setAttribute( "viewUrl", viewUrl );
    request.setAttribute( "siteUrlPrefix", siteUrlPrefix );
    return mapping.findForward( "success" );

  }

  /**
   * Method to populate the Award Summary for a promotion for a roundNumber
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward runCalculation( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    ThrowdownRoundCalculationResult throwdownRoundCalculationResult = new ThrowdownRoundCalculationResult();
    StackRankingCalculationResult stackRankingCalculationResult = new StackRankingCalculationResult();
    Long promotionId = getPromotionId( request );
    int roundNumber = Integer.parseInt( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "roundNumber" ) );

    throwdownRoundCalculationResult = getThrowdownService().generateHeadToHeadAwardSummaryForRound( promotionId, roundNumber );
    stackRankingCalculationResult = getThrowdownService().generateRankingAwardSummaryForRound( promotionId, roundNumber );
    ThrowdownPromotion promotion = throwdownRoundCalculationResult.getPromotion();

    ThrowdownAwardRunCalculationView throwdownAwardRunCalculationViewJson = new ThrowdownAwardRunCalculationView();

    throwdownAwardRunCalculationViewJson.setStackRankingCalculationResult( stackRankingCalculationResult );
    throwdownAwardRunCalculationViewJson.setThrowdownRoundCalculationResult( throwdownRoundCalculationResult );
    throwdownAwardRunCalculationViewJson.setPromotionUpperCaseName( promotion.getUpperCaseName() );

    HttpSession session = request.getSession();
    request.setAttribute( "promotion", promotion );
    // set calculationResult and stackRankingResult in session for later use in extract report
    session.setAttribute( "calculationResult", throwdownRoundCalculationResult );
    session.setAttribute( "stackRankingResult", stackRankingCalculationResult );
    request.setAttribute( "siteUrlPrefix", siteUrlPrefix );

    super.writeAsJsonToResponse( throwdownAwardRunCalculationViewJson, response );
    return null;
  }

  private Long getPromotionId( HttpServletRequest request )
  {
    return new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
