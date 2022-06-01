/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionManagerOverrideController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the PromotionPayout page.
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
 * <td>meadows</td>
 * <td>December 15, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionManagerOverrideController extends BaseController
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
    PromotionManagerOverrideForm promoManagerOverrideForm = (PromotionManagerOverrideForm)request.getAttribute( "promotionManagerOverrideForm" );

    String type = promoManagerOverrideForm.getOverrideStructure();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );

    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promoManagerOverrideForm.getPromotionId(), promoAssociationRequestCollection );
    if ( promoManagerOverrideForm.getAwardType() != null && promoManagerOverrideForm.getAwardType().equalsIgnoreCase( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
    {
      request.setAttribute( "pageNumber", "5" );
      request.setAttribute( "isPartnersEnabled", "true" );
    }
    else
    {
      request.setAttribute( "pageNumber", "4" );
      request.setAttribute( "isPartnersEnabled", "false" );
    }

    // List overrideStructureList = OverrideStructure.getList();
    List overrideStructureList = ManagerOverrideStructure.getList();
    // If awardtype is merchandise or travel then percent team earnings and amount per achiever not
    // valid.
    /*
     * if (!promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS)) { List
     * newOverrideStructureList = new ArrayList(overrideStructureList.size() - 1); for (Iterator
     * overrideStructureIter = overrideStructureList.iterator(); overrideStructureIter.hasNext();) {
     * ManagerOverrideStructure overrideStructure = (ManagerOverrideStructure)
     * overrideStructureIter.next(); if (!overrideStructure.getCode().equals(
     * ManagerOverrideStructure.OVERRIDE_PERCENT ) && !overrideStructure.getCode().equals(
     * ManagerOverrideStructure.AWARD_PER_ACHIEVER )) { newOverrideStructureList.add(
     * overrideStructure ); } } request.setAttribute( "overrideStructureList",
     * newOverrideStructureList ); } else {
     */
    request.setAttribute( "overrideStructureList", overrideStructureList );
    // }

    populateSaveTilesAttribute( context, type, promotion );

    if ( ObjectUtils.equals( promoManagerOverrideForm.getPromotionStatus(), PromotionStatusType.EXPIRED )
        || ObjectUtils.equals( promoManagerOverrideForm.getPromotionStatus(), PromotionStatusType.LIVE ) && promotion.getGoalCollectionEndDate().before( new Date() ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

  }

  private void populateSaveTilesAttribute( ComponentContext context, String pageType, GoalQuestPromotion promotion )
  {
    if ( pageType != null )
    {
      if ( pageType.equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) )
      {
        context.putAttribute( "overrideTable", "/promotion/goalquest/promotionOverridePerAchiever.jsp" );
      }
      if ( pageType.equals( ManagerOverrideStructure.STACK_RANKING_LEVEL ) )
      {
        context.putAttribute( "overrideTable", "/promotion/goalquest/promotionMngrOverrideStackRank.jsp" );
      }
      if ( pageType.equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
      {
        context.putAttribute( "overrideTable", "/promotion/goalquest/promotionOverrideTeamAchievement.jsp" );
      }
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
