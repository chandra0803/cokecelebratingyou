
package com.biperf.core.ui.promotion;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.RankingsPayoutType;
import com.biperf.core.domain.enums.ThrowdownAchievementPrecision;
import com.biperf.core.domain.enums.ThrowdownRoundingMethod;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseController;

public class PromotionThrowdownPayoutController extends BaseController
{
  /**
   * Tiles controller for the PromotionPayout page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionThrowdownPayoutForm promotionThrowdownPayoutForm = (PromotionThrowdownPayoutForm)request.getAttribute( "promotionThrowdownPayoutForm" );
    populateRequestAttibutes( promotionThrowdownPayoutForm, context, request );
  }

  protected void populateRequestAttibutes( PromotionThrowdownPayoutForm promotionGoalPayoutForm, ComponentContext context, HttpServletRequest request ) throws Exception
  {
    PromotionThrowdownPayoutForm payoutForm = (PromotionThrowdownPayoutForm)request.getAttribute( "promotionThrowdownPayoutForm" );
    request.setAttribute( "pageNumber", "3" );
    request.setAttribute( "baseunitPositionList", BaseUnitPosition.getList() );
    request.setAttribute( "promotionType", getPromotionType() );
    request.setAttribute( "achievementPrecisionList", ThrowdownAchievementPrecision.getList() );
    request.setAttribute( "roundingMethodList", ThrowdownRoundingMethod.getList() );
    request.setAttribute( "rankingsPayoutTypeList", RankingsPayoutType.getList() );
    // No fetch by ID for now. Just bring them all.
    List userCharList = getUserCharacteristicService().getAllCharacteristics();
    request.setAttribute( "promotionStatus", promotionGoalPayoutForm.getPromotionStatus() );
    request.setAttribute( "userCharList", userCharList );
    request.setAttribute( "nodeTypeList", getNodeTypeService().getAll() );
    request.setAttribute( "outcomeTypeList", MatchTeamOutcomeType.getList() );
    List<Division> divisions = getThrowdownService().getDivisionsByPromotionId( payoutForm.getPromotionId() );
    request.setAttribute( "divisionList", divisions );

    if ( ObjectUtils.equals( promotionGoalPayoutForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

  }

  protected String getPromotionType()
  {
    return PromotionType.THROWDOWN;
  }

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  private NodeTypeService getNodeTypeService() throws Exception
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  private ThrowdownService getThrowdownService() throws Exception
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

}
