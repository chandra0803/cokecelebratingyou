/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionGoalPayoutController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

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
public class PromotionGoalPayoutController extends BaseController
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
    PromotionGoalPayoutForm promotionGoalPayoutForm = (PromotionGoalPayoutForm)request.getAttribute( "promotionGoalPayoutForm" );
    populateRequestAttibutes( promotionGoalPayoutForm, context, request );
  }

  protected void populateRequestAttibutes( PromotionGoalPayoutForm promotionGoalPayoutForm, ComponentContext context, HttpServletRequest request ) throws Exception
  {
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List planningWorksheets = (List)contentReader.getContent( "promotion.goalquest.planning.worksheets" );
    if ( planningWorksheets != null )
    {
      Map worksheetMap = new TreeMap();
      for ( Iterator iter = planningWorksheets.iterator(); iter.hasNext(); )
      {
        Content content = (Content)iter.next();
        worksheetMap.put( content.getContentDataMap().get( "NAME" ), content.getContentDataMap().get( "URL" ) );
      }
      request.setAttribute( "planningWorksheetMap", worksheetMap );
    }
    request.setAttribute( "achievementRuleList", AchievementRuleType.getList() );
    request.setAttribute( "payoutStructureList", PayoutStructure.getList() );
    request.setAttribute( "achievementPrecisionList", AchievementPrecision.getList() );
    request.setAttribute( "roundingMethodList", RoundingMethod.getList() );
    request.setAttribute( "pageNumber", "3" );
    // BugFix 17935
    request.setAttribute( "baseunitPositionList", BaseUnitPosition.getList() );
    request.setAttribute( "promotionType", getPromotionType() );

    String type = promotionGoalPayoutForm.getPayoutStructure();

    GoalQuestPromotion promotion = getPromotionWithAssociations( promotionGoalPayoutForm.getPromotionId() );

    List achievementRuleTypeList = new ArrayList( AchievementRuleType.getList() );
    if ( promotion.isGoalQuestPromotion() )
    {
      for ( Iterator iter = achievementRuleTypeList.iterator(); iter.hasNext(); )
      {
        AchievementRuleType acRule = (AchievementRuleType)iter.next();
        if ( acRule.getCode().equals( AchievementRuleType.BASE_PLUS_FIXED ) )
        {
          iter.remove();
        }
      }
    }
    request.setAttribute( "achievementRuleList", achievementRuleTypeList );

    // BugFix 20294.When user switch from merchandise to points,populate the payout structure for
    // the goal payout.
    String payoutStructureType = "";
    if ( StringUtils.isEmpty( type ) && promotion.getGoalLevels() != null && promotion.getGoalLevels().size() > 0 )
    {
      for ( Iterator<AbstractGoalLevel> iter = promotion.getGoalLevels().iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        if ( goalLevel.getBonusAward() != null && goalLevel.getIncrementalQuantity() != null && goalLevel.getMinimumQualifier() != null )
        {
          payoutStructureType = PayoutStructure.BOTH;
          break;
        }
        else if ( goalLevel.getBonusAward() == null && goalLevel.getIncrementalQuantity() != null && goalLevel.getMinimumQualifier() != null )
        {
          payoutStructureType = PayoutStructure.RATE;
          break;
        }
        else
        {
          payoutStructureType = PayoutStructure.FIXED;
          break;
        }

      }
      type = payoutStructureType;
      promotionGoalPayoutForm.setPayoutStructure( type );
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      if ( promotionGoalPayoutForm.getAwardType() != null && promotionGoalPayoutForm.getAwardType().equalsIgnoreCase( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
      {
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "isPartnersEnabled", "false" );
      }
    }
    populateSaveTilesAttribute( context, type, promotion );

    if ( ObjectUtils.equals( promotionGoalPayoutForm.getPromotionStatus(), PromotionStatusType.EXPIRED )
        || ObjectUtils.equals( promotionGoalPayoutForm.getPromotionStatus(), PromotionStatusType.LIVE ) && promotion.getGoalCollectionEndDate().before( new Date() ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
    if ( promotionGoalPayoutForm.getAwardType().equalsIgnoreCase( "merchandise" )
        || promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null
            && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
    {
      AwardBanqMerchResponseValueObject merchlinqLevelData = null;
      if ( System.getProperty( "environment.name" ) != null && System.getProperty( "environment.name" ).equals( "dev" ) )
      {
        merchlinqLevelData = getMerchLevelService().getMerchlinqLevelDataWebService( promotion.getProgramId(), "qa" );
      }
      else
      {
        merchlinqLevelData = getMerchLevelService().getMerchlinqLevelDataWebService( promotion.getProgramId() );
      }
      if ( merchlinqLevelData != null )
      {
        request.setAttribute( "merchLevelList", merchlinqLevelData.getMerchLevel() );
      }
    }

  }

  @SuppressWarnings( "unchecked" )
  protected GoalQuestPromotion getPromotionWithAssociations( Long promotionId )
  {
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    return (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
  }

  protected void populateSaveTilesAttribute( ComponentContext context, String pageType, GoalQuestPromotion promotion )
  {
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      if ( pageType.equals( PayoutStructure.FIXED ) )
      {
        context.putAttribute( "payoutTable", "/promotion/goalquest/promotionPayoutFixed.jsp" );
      }
      if ( pageType.equals( PayoutStructure.RATE ) )
      {
        context.putAttribute( "payoutTable", "/promotion/goalquest/promotionPayoutRate.jsp" );
      }
      if ( pageType.equals( PayoutStructure.BOTH ) )
      {
        context.putAttribute( "payoutTable", "/promotion/goalquest/promotionPayoutBoth.jsp" );
      }
    }
    else
    {
      context.putAttribute( "payoutTable", "/promotion/goalquest/promotionPayoutTravelMerch.jsp" );
    }
  }

  protected String getPromotionType()
  {
    return PromotionType.GOALQUEST;
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

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  protected static MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
