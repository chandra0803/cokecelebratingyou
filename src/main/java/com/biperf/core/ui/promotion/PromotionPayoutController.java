/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.util.LabelValueBean;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.PromoMgrPayoutFreqType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.enums.SubmittersToRankType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseController;
import com.objectpartners.cms.util.CmsResourceBundle;

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
 * <td>sedey</td>
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutController extends BaseController
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
    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)request.getAttribute( "promotionPayoutForm" );

    request.setAttribute( "payoutTypeList", PromotionPayoutType.getList() );
    request.setAttribute( "managerPayoutFreqList", PromoMgrPayoutFreqType.getList() );
    request.setAttribute( "hasParent", Boolean.valueOf( promoPayoutForm.isHasParent() ) );
    request.setAttribute( "promotionStatus", promoPayoutForm.getPromotionStatus() );
    request.setAttribute( "pageNumber", "4" );

    if ( ObjectUtils.equals( promoPayoutForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    String type = promoPayoutForm.getPayoutType();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMO_PAYOUT ) );

    ProductClaimPromotion promotion = (ProductClaimPromotion)getPromotionService().getPromotionByIdWithAssociations( promoPayoutForm.getPromotionId(), promoAssociationRequestCollection );

    populateSaveTilesAttribute( context, type, promoPayoutForm.isHasParent(), promotion );

    if ( promoPayoutForm.isHasParent() )
    {
      request.setAttribute( "promotion", promotion.getParentPromotion() );
    }

    if ( "stack_rank".equals( type ) )
    {
      request.setAttribute( "nodeTypeList", getNodeTypeService().getAll() );
      request.setAttribute( "submittersToRankTypeList", SubmittersToRankType.getList() );
      request.setAttribute( "rankBasedList", getRankBasedList( promotion.getId() ) );
      StackRank stackRank = getStackRankService().getLatestStackRankByPromotionId( promotion.getId(), StackRankState.WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED, new AssociationRequestCollection() );
      boolean hasPending = false;
      if ( stackRank != null && stackRank.getState().getCode().equals( StackRankState.WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED ) )
      {
        hasPending = true;
      }
      promoPayoutForm.setHasPendingStackRank( hasPending );
    }
    else
    {
      // Break the Bank Budget information
      request.setAttribute( "budgetMasterList", populateBudgetMasterList( promotion ) );
      request.setAttribute( "hardCapBudgetType", BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
      request.setAttribute( "budgetFinalPayoutRuleList", populateBudgetFinalPayoutRule() );
    }
  }

  /**
   * @return list of budget final payout rules for Break the Bank functionality 
   * (strip off text in picklist items to display what's appropriate for Product Claim)
   */
  private List populateBudgetFinalPayoutRule()
  {
    List finalPayoutRuleList = BudgetFinalPayoutRule.getList();

    List finalPayoutRuleDisplayList = new ArrayList();

    Iterator ruleIter = finalPayoutRuleList.iterator();
    while ( ruleIter.hasNext() )
    {
      BudgetFinalPayoutRule payoutRule = (BudgetFinalPayoutRule)ruleIter.next();
      LabelValueBean labelValueBean = new LabelValueBean();

      labelValueBean.setValue( payoutRule.getCode() );
      labelValueBean.setLabel( MessageFormat.format( payoutRule.getName(), new Object[] { CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.CLAIM_LABEL" ) } ) );

      finalPayoutRuleDisplayList.add( labelValueBean );
    }
    return finalPayoutRuleDisplayList;
  }

  /**
   * Get a list of central budget masters for Break the Bank functionality  
   * @param promo
   * @return List of budget masters
   */
  private List populateBudgetMasterList( ProductClaimPromotion promo )
  {
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.RECOGNITION ) } );
    promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    List promotionList = getPromotionService().getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );

    List budgetMasterList = getBudgetMasterService().getActivePointsWithPromotionsAndSegments();
    List newBudgetMasterList = new ArrayList();
    for ( Iterator iter = budgetMasterList.iterator(); iter.hasNext(); )
    {
      boolean canAdd = true;
      BudgetMaster budgetMaster = (BudgetMaster)iter.next();
      // If the BudgetMaster is 'one promotion only' and is assigned to another promotion
      // then it cannot be displayed
      if ( !budgetMaster.isMultiPromotion() && budgetMaster.getPromotions() != null && !budgetMaster.getPromotions().isEmpty() )
      {
        canAdd = false;
        Iterator promotionIter = budgetMaster.getPromotions().iterator();
        while ( promotionIter.hasNext() )
        {
          Promotion promotion = (Promotion)promotionIter.next();
          if ( promotion.getId().equals( promo.getId() ) )
          {
            canAdd = true;
            break; // no need to continue looping through all promotions
          }
        }
      }

      // Budget Master created using Public Recognitions should be displayed only to its promotion
      // and should not be displayed to other recogition
      // promotions. Fix for Bug 55491

      Iterator eligiblePromotionListIterator = promotionList.iterator();
      while ( eligiblePromotionListIterator.hasNext() )
      {
        Promotion promotion = (Promotion)eligiblePromotionListIterator.next();
        if ( !budgetMaster.isMultiPromotion() && ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster() != null
            && ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster().getId().equals( budgetMaster.getId() ) && !promotion.getId().equals( promo.getId() ) )
        {
          canAdd = false;
          break; // no need to continue looping through all promotions
        }
      }

      if ( canAdd )
      {
        // Only show central budgets
        if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
        {
          populateBudgetMasterListElement( newBudgetMasterList, budgetMaster );
        }
      }
    }
    return newBudgetMasterList;
  }

  private void populateBudgetMasterListElement( List newBudgetMasterList, BudgetMaster budgetMaster )
  {
    PromotionAwardsFormBean bean = new PromotionAwardsFormBean();
    bean.setBudgetMasterId( budgetMaster.getId() );
    String name = CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() );
    bean.setBudgetMasterName( name );
    newBudgetMasterList.add( bean );
  }

  private void populateSaveTilesAttribute( ComponentContext context, String pageType, boolean hasParent, ProductClaimPromotion promotion )
  {
    if ( pageType.equals( "one_to_one" ) )
    {
      if ( hasParent )
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutChildOneToOne.jsp" );
      }
      else
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutOneToOne.jsp" );
      }
    }
    else if ( pageType.equals( "tiered" ) )
    {
      if ( hasParent )
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutChildTiered.jsp" );
      }
      else
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutTiered.jsp" );
      }
    }
    else if ( pageType.equals( "cross_sell" ) )
    {
      if ( hasParent )
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutChildCrossSell.jsp" );
      }
      else
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutCrossSell.jsp" );
      }
    }
    else if ( pageType.equals( "stack_rank" ) )
    {
      if ( hasParent )
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutChildStackRank.jsp" );
      }
      else
      {
        context.putAttribute( "payoutTable", "/promotion/productclaim/promotionPayoutStackRank.jsp" );
      }
      context.putAttribute( "payoutStackRankBottomTable", "/promotion/productclaim/promotionPayoutStackRankPayouts.jsp" );
    }

    if ( hasParent )
    {
      if ( promotion.getParentPromotion().getPayoutType().getCode().equals( PromotionPayoutType.ONE_TO_ONE ) )
      {
        context.putAttribute( "parentPayoutTable", "/promotion/productclaim/parentPayoutOneToOne.jsp" );
      }
      else if ( promotion.getParentPromotion().getPayoutType().getCode().equals( PromotionPayoutType.TIERED ) )
      {
        context.putAttribute( "parentPayoutTable", "/promotion/productclaim/parentPayoutTiered.jsp" );
      }
      else if ( promotion.getParentPromotion().getPayoutType().getCode().equals( PromotionPayoutType.CROSS_SELL ) )
      {
        context.putAttribute( "parentPayoutTable", "/promotion/productclaim/parentPayoutCrossSell.jsp" );
      }
      else if ( promotion.getParentPromotion().getPayoutType().getCode().equals( PromotionPayoutType.STACK_RANK ) )
      {
        context.putAttribute( "parentPayoutTable", "/promotion/productclaim/parentPayoutStackRank.jsp" );
        context.putAttribute( "parentPayoutStackRankBottomTable", "/promotion/productclaim/parentPayoutStackRankPayouts.jsp" );
      }
    }
  }

  private List getRankBasedList( Long promotionId )
  {

    List promotionRankBasedItemList = new ArrayList();

    /*
     * PromotionRankBasedItem promotionRankBasedQuantitySoldItem = new PromotionRankBasedItem();
     * //promotionRankBasedQuantitySoldItem.setId(StackRankFactorType.QUANTITY_SOLD);
     * promotionRankBasedQuantitySoldItem
     * .setCmAssetCode(StackRankFactorType.lookup(StackRankFactorType.QUANTITY_SOLD).getCode());
     * promotionRankBasedQuantitySoldItem
     * .setCmKeyForElementLabel(StackRankFactorType.lookup(StackRankFactorType
     * .QUANTITY_SOLD).getName());
     * promotionRankBasedItemList.add(0,promotionRankBasedQuantitySoldItem);
     */
    PromotionService promotionService = getPromotionService();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    Promotion promotion = promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    if ( promotion.getClaimForm().getClaimFormSteps() != null && promotion.getClaimForm().getClaimFormSteps().size() > 0 )
    {
      Iterator claimFormStepIterator = promotion.getClaimForm().getClaimFormSteps().iterator();
      while ( claimFormStepIterator.hasNext() )
      {
        ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepIterator.next();

        // If the ClaimFormStep has ClaimFormStepElements, iterate over them to build a list of
        // elements that will hold the element
        if ( claimFormStep.getClaimFormStepElements() != null && claimFormStep.getClaimFormStepElements().size() > 0 )
        {
          // List claimFormStepElements = new ArrayList();

          Iterator claimFormStepElementIterator = claimFormStep.getClaimFormStepElements().iterator();
          while ( claimFormStepElementIterator.hasNext() )
          {
            ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStepElementIterator.next();
            // We only want ClaimFormStepElements that have a type of number
            if ( claimFormStepElement.getClaimFormElementType().getCode().equals( "number" ) )
            {
              PromotionRankBasedItem promotionRankBasedItem = new PromotionRankBasedItem();
              Long claimFormStepElementId = claimFormStepElement.getId();
              promotionRankBasedItem.setId( claimFormStepElementId );
              promotionRankBasedItem.setCmAssetCode( promotion.getClaimForm().getCmAssetCode() );
              promotionRankBasedItem.setCmKeyForElementLabel( claimFormStepElement.getCmKeyForElementLabel() );
              promotionRankBasedItemList.add( promotionRankBasedItem );
            }
          }
        }
      }
    }

    return promotionRankBasedItemList;
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

  private NodeTypeService getNodeTypeService() throws Exception
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  private StackRankService getStackRankService() throws Exception
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
  }

  /**
   * Does a Bean lookup for the Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }
}
