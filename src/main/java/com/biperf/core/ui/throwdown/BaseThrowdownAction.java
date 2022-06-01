
package com.biperf.core.ui.throwdown;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.value.PromotionMenuBean;

public class BaseThrowdownAction extends BaseDispatchAction
{
  protected List<PromotionMenuBean> getEligibleThrowdownPromotions( HttpServletRequest request )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    List<PromotionMenuBean> eligibleThrowDownPromotions = new ArrayList<PromotionMenuBean>();

    Date now = Calendar.getInstance().getTime();
    for ( PromotionMenuBean promoBean : eligiblePromotions )
    {
      Promotion promotion = promoBean.getPromotion();
      if ( promotion.isThrowdownPromotion() && promotion.getTileDisplayStartDate().before( now ) && promotion.getTileDisplayEndDate().after( now ) )
      {
        eligibleThrowDownPromotions.add( promoBean );
      }
    }
    return eligibleThrowDownPromotions;
  }

  protected Long getAppropriateThrowdownPromotion( HttpServletRequest request )
  {
    for ( PromotionMenuBean promoBean : getEligiblePromotions( request ) )
    {
      if ( promoBean.getPromotion().isThrowdownPromotion() )
      {
        return promoBean.getPromotion().getId();
      }
    }
    return null;
  }

  protected Long buildPromotionId( HttpServletRequest request )
  {
    Long promotionId = buildPrimary( request, "promotionId" );

    if ( promotionId == null )
    {
      promotionId = getAppropriateThrowdownPromotion( request );
    }
    return promotionId;
  }

  protected Long buildPrimary( HttpServletRequest request, String primerName )
  {
    Long primerId = null;
    try
    {
      String strPrimerId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), primerName );

      if ( !StringUtils.isEmpty( strPrimerId ) )
      {
        primerId = new Long( strPrimerId );
      }

      // check the standard request (for the JSON calls)
      if ( primerId == null )
      {
        primerId = !StringUtils.isEmpty( request.getParameter( primerName ) ) ? new Long( request.getParameter( primerName ) ) : null;
      }
    }
    catch( NumberFormatException e )
    {
      log.error( " Error occured while getting primer. Primer Name : " + primerName );
    }
    return primerId;
  }

  protected String generateRulesUrl( Long promotionId, HttpServletRequest request )
  {

    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    String rulesUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/viewRules.do?method=detail", parameterMap );
    return rulesUrl;
  }

  protected void reorderEligiblePromotions( HttpServletRequest request, Long promotionId )
  {
    List<PromotionMenuBean> promotionMenuBean = getEligiblePromotions( request );
    List<PromotionMenuBean> reorderedPromotions = new ArrayList<PromotionMenuBean>();
    List<PromotionMenuBean> otherPromotions = new ArrayList<PromotionMenuBean>();

    for ( PromotionMenuBean promotionBean : promotionMenuBean )
    {
      if ( promotionBean.getPromotion().getId().equals( promotionId != null ? promotionId : buildPrimary( request, "promotionId" ) ) )
      {
        reorderedPromotions.add( promotionBean );
      }
      else
      {
        otherPromotions.add( promotionBean );
      }
    }
    reorderedPromotions.addAll( otherPromotions );
    request.getSession().setAttribute( "eligiblePromotions", reorderedPromotions );
  }

  protected TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }

  protected ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

  protected StackStandingService getStackStandingService()
  {
    return (StackStandingService)getService( StackStandingService.BEAN_NAME );
  }

  protected UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
