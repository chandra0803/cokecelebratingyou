/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPublicRecognitionAddOnController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.util.LabelValueBean;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceNameComparator;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPublicRecognitionAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionEcardController.
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
 * <td>Oct 05, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPublicRecognitionAddOnController extends BaseController
{
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    PromotionPublicRecAddOnForm publicRecognitionForm = (PromotionPublicRecAddOnForm)request.getAttribute( "promotionPublicRecAddOnForm" );

    String promotionTypeCode = publicRecognitionForm.getPromotionTypeCode();

    request.setAttribute( PromotionWizardManager.PROMOTION_TYPE_REQUEST_ATTR, publicRecognitionForm.getPromotionTypeCode() );

    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.RECOGNITION ), PromotionType.lookup( PromotionType.NOMINATION ) } );
    promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    List promotionList = getPromotionService().getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );

    List budgetMasterList = getBudgetMasterService().getActivePointsWithPromotionsAndSegments();
    List availableAudiences = getAudienceService().getAll();
    List newBudgetMasterList = new ArrayList();

    Set assignedAudiences = (LinkedHashSet)request.getSession().getAttribute( "sessionPubliRecogAudienceList" );
    if ( assignedAudiences != null )
    {
      Iterator audienceIterator = availableAudiences.iterator();

      // Iterate over the webRulesAudience
      while ( audienceIterator.hasNext() )
      {
        Audience publicRecognitionAudience = (Audience)audienceIterator.next();
        Iterator assignedIterator = assignedAudiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)assignedIterator.next();
          if ( promotionPublicRecognitionAudience.getAudience().getName().equals( publicRecognitionAudience.getName() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }

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
          if ( promotion.getId().equals( publicRecognitionForm.getPromotionId() ) )
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

        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
          if ( !budgetMaster.isMultiPromotion() && recPromo.getPublicRecogBudgetMaster() != null && recPromo.getPublicRecogBudgetMaster().getId().equals( budgetMaster.getId() )
              && !promotion.getId().equals( publicRecognitionForm.getPromotionId() ) )
          {
            canAdd = false;
            break; // no need to continue looping through all promotions
          }
        }
        else if ( promotion.isNominationPromotion() )
        {
          NominationPromotion nomPromo = (NominationPromotion)promotion;
          if ( !budgetMaster.isMultiPromotion() && nomPromo.getPublicRecogBudgetMaster() != null && nomPromo.getPublicRecogBudgetMaster().getId().equals( budgetMaster.getId() )
              && !promotion.getId().equals( publicRecognitionForm.getPromotionId() ) )
          {
            canAdd = false;
            break; // no need to continue looping through all promotions
          }
        }
      }

      if ( canAdd )
      {
        // Only show non-central budgets for recognition
        if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) || promotionTypeCode.equals( PromotionType.NOMINATION ) )
        // remove the next line because we need to add Central budge type in the award page
        // && !budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
        {
          populateBudgetMasterListElement( newBudgetMasterList, budgetMaster );
        }
      }

    }
    Promotion promotion = getPromotionService().getPromotionById( new Long( publicRecognitionForm.getPromotionId() ) );

    Collections.sort( availableAudiences, new AudienceNameComparator() );
    request.setAttribute( "availableAudiences", availableAudiences );
    request.setAttribute( "hasParent", Boolean.valueOf( publicRecognitionForm.isHasParent() ) );
    request.setAttribute( "promotionStatus", publicRecognitionForm.getPromotionStatus() );

    if ( promotion.isRecognitionPromotion() )
    {
      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        request.setAttribute( "pageNumber", "65" );
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      else
      {
        request.setAttribute( "pageNumber", "11" );
      }
      if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        request.setAttribute( "pageNumber", "75" );
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }
    else if ( promotion.isNominationPromotion() )
    {
      if ( promotion.getAwardType() != null && PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
      {
        request.setAttribute( "pageNumber", "111" );
      }
      else
      {
        request.setAttribute( "pageNumber", "9" );
      }
    }

    if ( ObjectUtils.equals( publicRecognitionForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) && !publicRecognitionForm.isAllowPublicRecognitionPoints() )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
    request.setAttribute( "budgetMasterList", newBudgetMasterList );

    request.setAttribute( "hardCapBudgetType", BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );

    List finalPayoutRuleDisplayList = new ArrayList();

    List finalPayoutRuleList = BudgetFinalPayoutRule.getList();

    Iterator ruleIter = finalPayoutRuleList.iterator();
    while ( ruleIter.hasNext() )
    {
      BudgetFinalPayoutRule payoutRule = (BudgetFinalPayoutRule)ruleIter.next();
      LabelValueBean labelValueBean = new LabelValueBean();

      labelValueBean.setValue( payoutRule.getCode() );
      labelValueBean.setLabel( MessageFormat.format( payoutRule.getName(), new Object[] { CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.QUIZ_LABEL" ) } ) );

      finalPayoutRuleDisplayList.add( labelValueBean );
    }

    request.setAttribute( "publicRecognitionFinalPayoutRuleList", finalPayoutRuleDisplayList );
  }

  private void populateBudgetMasterListElement( List newBudgetMasterList, BudgetMaster budgetMaster )
  {
    PromotionAwardsFormBean bean = new PromotionAwardsFormBean();
    bean.setBudgetMasterId( budgetMaster.getId() );
    String name = CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() );
    bean.setBudgetMasterName( name );
    newBudgetMasterList.add( bean );
  }

  /**
   * Retrieves a Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

}
