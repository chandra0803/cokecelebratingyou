/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/ReviewGoalQuestProgressController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.goalquest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.MerchLinqProductDataUtils;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.GoalLevelValueBean;
import com.biperf.util.StringUtils;

/**
 * ReviewGoalQuestProgressController.
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
 * <td>Satish</td>
 * <td>Jan 23, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReviewGoalQuestProgressController extends BaseController
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

    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
    Long userId = null;
    String userIdString = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    // BugFix 17937
    Boolean isgiftCodeRedeemed = Boolean.FALSE;
    if ( userIdString != null )
    {
      userId = new Long( userIdString );
    }
    if ( userId == null || userId.longValue() == 0 )
    {
      userId = UserManager.getUserId();
    }
    // BugFix 20276.
    Object partnerPaxId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "partnerPaxId" );
    if ( partnerPaxId != null )
    {
      userId = new Long( (String)partnerPaxId );
    }

    Boolean adminView = null;
    adminView = (Boolean)ClientStateUtils.getParameterValueAsObject( request, ClientStateUtils.getClientStateMap( request ), "adminView" );
    if ( adminView != null && adminView.booleanValue() )
    {
      request.setAttribute( "adminView", Boolean.TRUE );
      if ( userId != null )
      {
        request.setAttribute( "userId", userId );
      }
    }

    Map progressMap = getGoalQuestService().getGoalQuestProgressByPromotionIdAndUserId( promotionId, userId );
    GoalQuestReviewProgress goalQuestReviewProgress = null;
    List progressList = (List)progressMap.get( "goalQuestProgressList" );
    request.setAttribute( "goalQuestProgressList", progressList );
    if ( progressList != null && progressList.size() > 0 )
    {
      goalQuestReviewProgress = (GoalQuestReviewProgress)progressList.get( progressList.size() - 1 );
      request.setAttribute( "goalQuestProgress", goalQuestReviewProgress );
    }
    if ( promotionId != null )
    {
      GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionById( promotionId );
      request.setAttribute( "promotion", goalQuestPromotion );
      if ( goalQuestPromotion != null && goalQuestPromotion.getProgressLoadType() != null && goalQuestPromotion.getProgressLoadType().getCode().equals( ProgressLoadType.AUTOMOTIVE ) )
      {
        request.setAttribute( "isAutomotive", Boolean.TRUE );
      }
      else
      {
        request.setAttribute( "isAutomotive", Boolean.FALSE );
      }
      PaxGoal paxGoal = (PaxGoal)progressMap.get( "paxGoal" );
      Boolean isAchieved = Boolean.FALSE;
      String giftCode = null;
      GoalLevelValueBean goalLevelValueBean = null;
      if ( paxGoal != null )
      {
        if ( paxGoal.getGoalLevel() instanceof ManagerOverrideGoalLevel )
        {
          goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( (ManagerOverrideGoalLevel)paxGoal.getGoalLevel() );
        }
        else
        {
          goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( paxGoal, goalQuestPromotion, (GoalLevel)paxGoal.getGoalLevel() );
        }
      }
      // GoalLevelValueBean goalLevelValueBean = SelectGoalUtil
      // .populateGoalLevelValueBean( paxGoal, goalQuestPromotion, (GoalLevel)paxGoal
      // .getGoalLevel() );
      if ( goalQuestPromotion != null && goalQuestPromotion.isIssueAwardsRun() )
      {
        request.setAttribute( "interim", Boolean.FALSE );
        if ( ( adminView == null || !adminView.booleanValue() ) && paxGoal != null )
        {
          // check atleast one MerchOrder is available
          List merchOrders = getMerchOrderEntries( goalQuestPromotion.getId(), userId );
          MerchOrder merchOrder = null;
          if ( merchOrders != null && merchOrders.size() > 0 )
          {
            isAchieved = Boolean.TRUE;
            merchOrder = (MerchOrder)merchOrders.get( 0 );
          }

          goalLevelValueBean.setSelectedProduct( MerchLinqProductDataUtils.getProduct( goalQuestPromotion.getProgramId(), paxGoal.getCatalogId(), paxGoal.getProductSetId() ) );
          if ( isAchieved.booleanValue() )
          {
            goalLevelValueBean.setShoppingURL( getShoppingURL( goalQuestPromotion, paxGoal, giftCode, userId, request ) );
            isgiftCodeRedeemed = new Boolean( MerchLinqProductDataUtils.isGiftCodeRedeemed( merchOrder ) );
          }
        }
      }
      else
      {
        request.setAttribute( "interim", Boolean.TRUE );
      }
      request.setAttribute( "paxGoal", paxGoal );
      request.setAttribute( "goalLevelValueBean", goalLevelValueBean );
      request.setAttribute( "isAchieved", isAchieved );
      request.setAttribute( "isgiftCodeRedeemed", isgiftCodeRedeemed );
      if ( goalQuestPromotion != null && goalQuestPromotion.getPartnerAudienceType() != null )
      {
        // BugFix 20276.
        if ( partnerPaxId != null )
        {
          request.setAttribute( "partner", "false" );
          request.setAttribute( "partnerrsList", null );
        }
        else
        {
          ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
          List partnersList = getPromotionService().getPartnersByPromotionAndParticipantWithAssociations( goalQuestPromotion.getId(), userId, paxAscReq );
          request.setAttribute( "partner", "true" );
          request.setAttribute( "partnerrsList", partnersList );
        }

      }
      else
      {
        request.setAttribute( "partner", "false" );
      }

    }
  }

  private String getShoppingURL( GoalQuestPromotion promotion, PaxGoal paxGoal, String giftCode, Long userId, HttpServletRequest request )
  {
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {

      // -------------------------
      // Check the shopping type
      // ---------------------------
      String shoppingType = getShoppingService().checkShoppingType( userId );

      if ( shoppingType.equals( ShoppingService.INTERNAL ) )
      {
        return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/shopping.do?method=displayInternal", null );
      }
      else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
      {
        return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/externalSupplier.do?method=displayExternal", null );
      }
    }
    else
    {
      return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/shopping.do?method=displayMerchLinq", getShoppingParameters( giftCode, paxGoal ) );
    }
    return null;
  }

  private List getMerchOrderEntries( Long promotionId, Long userId )
  {
    MerchOrderActivityQueryConstraint merchOrderActivityQueryConstraint = new MerchOrderActivityQueryConstraint();
    merchOrderActivityQueryConstraint.setParticipantId( userId );
    merchOrderActivityQueryConstraint.setPromotionId( promotionId );
    List journalEntries = getMerchOrderService().getMerchOrderList( merchOrderActivityQueryConstraint );
    return journalEntries;
  }

  private Map getShoppingParameters( String giftCode, PaxGoal paxGoal )
  {
    Map shoppingParameters = new HashMap();
    if ( !StringUtils.isEmpty( giftCode ) )
    {
      shoppingParameters.put( "giftcode", giftCode );
      // CR - Convert to PerQs - START
      shoppingParameters.put( "promotionId", paxGoal.getGoalQuestPromotion().getId() );
      // CR - Convert to PerQs - END
      if ( paxGoal != null && !StringUtils.isEmpty( paxGoal.getProductSetId() ) && !StringUtils.isEmpty( paxGoal.getCatalogId() ) )
      {
        shoppingParameters.put( "productSetId", paxGoal.getProductSetId() );
        shoppingParameters.put( "catalogId", paxGoal.getCatalogId() );
      }
    }
    return shoppingParameters;
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the GoalQuestService from the beanLocator.
   * 
   * @return GoalQuestService
   */
  private GoalQuestService getGoalQuestService()
  {
    return (GoalQuestService)getService( GoalQuestService.BEAN_NAME );
  }

  /**
   * Get the MerchOrderService from the beanLocator.
   * 
   * @return MerchOrderService
   */
  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

  /**
   * Get the ShoppingService from the beanLocator.
   * 
   * @return ShoppingService
   */
  private ShoppingService getShoppingService()
  {
    return (ShoppingService)getService( ShoppingService.BEAN_NAME );
  }
}
