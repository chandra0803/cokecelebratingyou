/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionGoalPartnerPayoutController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PartnerEarnings;
import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * PromotionGoalPartnerPayoutController.
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
 * <td>Feb 25, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PromotionGoalPartnerPayoutController extends BaseController
{
  /**
   * Tiles controller for the PromotionPartnerPayout page Overridden from
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
    PromotionGoalPartnerPayoutForm promoGoalPayoutForm = (PromotionGoalPartnerPayoutForm)request.getAttribute( "promotionGoalPartnerPayoutForm" );
    request.setAttribute( "partnerPayoutStructureList", PartnerPayoutStructure.getList() );
    request.setAttribute( "partnerEarningsList", PartnerEarnings.getList() );
    if ( promoGoalPayoutForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      request.setAttribute( "pageNumber", "4" );
    }
    else
    {
      request.setAttribute( "pageNumber", "10" );
    }
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );

    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promoGoalPayoutForm.getPromotionId(), promoAssociationRequestCollection );
    if ( promoGoalPayoutForm.getAwardType() != null && promoGoalPayoutForm.getAwardType().equalsIgnoreCase( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
    {
      request.setAttribute( "isPartnersEnabled", "true" );
    }
    else
    {
      request.setAttribute( "isPartnersEnabled", "false" );
    }
    if ( ObjectUtils.equals( promoGoalPayoutForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) && promotion.getGoalCollectionEndDate().before( new Date() ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
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

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
