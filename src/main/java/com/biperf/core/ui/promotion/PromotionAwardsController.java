/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.util.LabelValueBean;

import com.biperf.core.dao.calculator.CalculatorQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.BudgetSegmentComparator;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.enums.NominationAwardSpecifierType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.ScoreBy;
import com.biperf.core.domain.enums.TimeframePeriodType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.calculator.CalculatorAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionAwardsController.
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
 * <td>asondgeroth</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAwardsController extends BaseController
{
  private static final String ANNUALLY = "annually";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
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
    PromotionAwardsForm promoAwardsForm = (PromotionAwardsForm)request.getAttribute( "promotionAwardsForm" );
    request.setAttribute( "promotionStatus", promoAwardsForm.getPromotionStatus() );

    if ( promoAwardsForm.isExpired() )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    request.setAttribute( PromotionWizardManager.PROMOTION_TYPE_REQUEST_ATTR, promoAwardsForm.getPromotionTypeCode() );

    String promotionTypeCode = promoAwardsForm.getPromotionTypeCode();
    if ( promoAwardsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( promoAwardsForm.getPromotionId() ) );
      if ( promo.isIncludePurl() )
      {
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      if ( promo.isMobAppEnabled() )
      {
        request.setAttribute( "isMobAppEnabled", Boolean.TRUE );
      }
      if ( promo.isIncludeCelebrations() )
      {
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }

    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      request.setAttribute( "pageNumber", "40" );

      CalculatorQueryConstraint calcQueryConstraint = new CalculatorQueryConstraint();
      // todo: set variable to status..
      calcQueryConstraint.setCalculatorStatusTypeIncluded( new CalculatorStatusType[] { CalculatorStatusType.lookup( CalculatorStatusType.COMPLETED ),
                                                                                        CalculatorStatusType.lookup( CalculatorStatusType.ASSIGNED ) } );
      // get the correct calculators for the awards type
      if ( promoAwardsForm.getAwardsType() != null && PromotionAwardsType.MERCHANDISE.equals( promoAwardsForm.getAwardsType() ) )
      {
        calcQueryConstraint.setCalculatorAwardTypeIncluded( new CalculatorAwardType[] { CalculatorAwardType.lookup( CalculatorAwardType.MERCHANDISE_LEVEL_AWARD ) } );
      }
      else
      {
        calcQueryConstraint.setCalculatorAwardTypeIncluded( new CalculatorAwardType[] { CalculatorAwardType.lookup( CalculatorAwardType.FIXED_AWARD ),
                                                                                        CalculatorAwardType.lookup( CalculatorAwardType.RANGE_AWARD ) } );
      }
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.CALCULATOR_PAYOUT ) );

      // calcQueryConstraint.setStatus(CalculatorStatusType.COMPLETED);
      List calculatorTypeList = getCalculatorService().getCalculatorListWithAssociations( calcQueryConstraint, associationRequestCollection );
      request.setAttribute( "calculatorTypeList", calculatorTypeList );
      if ( promoAwardsForm.getAwardsType() != null && PromotionAwardsType.MERCHANDISE.equals( promoAwardsForm.getAwardsType() ) )
      {
        if ( promoAwardsForm.isNumOfLevelsEqual() && promoAwardsForm.getNumberOfLevels() != null )
        {
          for ( Iterator calculatorIter = calculatorTypeList.iterator(); calculatorIter.hasNext(); )
          {
            Calculator calculator = (Calculator)calculatorIter.next();
            if ( calculator.getCalculatorPayouts().size() != promoAwardsForm.getNumberOfLevels().longValue() )
            {
              calculatorIter.remove();
            }
          }
        }
      }
      request.setAttribute( "scoreTypeList", ScoreBy.getList() );
    }
    if ( promoAwardsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "pageNumber", "70" );
      request.setAttribute( "awardSpecifierTypes", NominationAwardSpecifierType.getList() );
      request.setAttribute( "approverAwardSpecifierType", NominationAwardSpecifierType.lookup( NominationAwardSpecifierType.APPROVER ) );

      if ( promoAwardsForm.isApproverTypeCustom() && promoAwardsForm.isAwardSelected() && promoAwardsForm.isAwardsActive() )
      {
        request.setAttribute( "customAwardTypeList", PromotionAwardsType.getCustomAwardTypeList() );
      }

      request.setAttribute( "awardTypeList", PromotionAwardsType.getNominationList() );

      request.setAttribute( "awardTypeListAwardsInactive", PromotionAwardsType.getInactiveAwardTypeList() );
      List<Currency> currencies = getCurrencyService().getAllCurrency();
      if ( currencies == null )
      {
        currencies = new ArrayList<Currency>();
      }
      request.setAttribute( "currencies", currencies );
      if ( promoAwardsForm.getNomBudgetApproverId() != null && promoAwardsForm.getNomBudgetApproverId() != 0 )
      {
        request.setAttribute( "approverName", getParticipantService().getParticipantById( promoAwardsForm.getNomBudgetApproverId() ).getNameLFMWithComma() );
      }
      else if ( promoAwardsForm.getSelectedBudgetApproverUserId() != null && promoAwardsForm.getSelectedBudgetApproverUserId() != 0 )
      {
        request.setAttribute( "approverName", getParticipantService().getParticipantById( promoAwardsForm.getSelectedBudgetApproverUserId() ).getNameLFMWithComma() );
      }
      request.setAttribute( "nominationTimePeriodList", getNominationTimePeriods() );
    }
    if ( promoAwardsForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) || promoAwardsForm.getPromotionTypeCode().equals( PromotionType.WELLNESS ) )
    {
      boolean isPlateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
      request.setAttribute( "isPlateauPlatformOnly", isPlateauPlatformOnly );
      request.setAttribute( "pageNumber", "3" );
    }
    if ( promoAwardsForm.getPromotionTypeCode().equals( PromotionType.WELLNESS ) )
    {
      request.setAttribute( "isLastPage", Boolean.TRUE );
    }
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.RECOGNITION ) } );
    promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    List promotionList = getPromotionService().getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );

    List budgetMasterList = getBudgetMasterService().getActivePointsWithPromotionsAndSegments();
    List newBudgetMasterList = new ArrayList();
    List newBudgetMasterWithSweepBudgetList = new ArrayList();
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
          if ( promotion.getId().equals( promoAwardsForm.getPromotionId() ) )
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
            && ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster().getId().equals( budgetMaster.getId() ) && !promotion.getId().equals( promoAwardsForm.getPromotionId() ) )
        {
          canAdd = false;
          break; // no need to continue looping through all promotions
        }
      }

      // if awardType is merchandise, only display active participant budget
      if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) && promoAwardsForm.getAwardsType().equals( PromotionAwardsType.MERCHANDISE ) )
      {
        if ( !budgetMaster.isParticipantBudget() && !budgetMaster.isNodeBudget() )
        {
          canAdd = false;
        }
      }

      if ( canAdd )
      {
        // Only show central budgets for nominations
        if ( ( promotionTypeCode.equals( PromotionType.NOMINATION ) || promotionTypeCode.equals( PromotionType.QUIZ ) )
            && budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
        {
          populateBudgetMasterListElement( newBudgetMasterList, null, budgetMaster, promotionTypeCode );
        }
        // Only show non-central budgets for recognition
        if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
        // remove the next line because we need to add Central budge type in the award page
        // && !budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
        {
          populateBudgetMasterListElement( newBudgetMasterList, newBudgetMasterWithSweepBudgetList, budgetMaster, promotionTypeCode );
        }
      }

    }

    Collections.sort( newBudgetMasterList, new PromotionAwardsFormBeanComparator() );
    Collections.sort( newBudgetMasterWithSweepBudgetList, new PromotionAwardsFormBeanComparator() );
    request.setAttribute( "budgetMasterWithSweepBudgetList", newBudgetMasterWithSweepBudgetList );
    request.setAttribute( "budgetMasterList", newBudgetMasterList );

    List cashBudgetMasterList = getBudgetMasterService().getActiveCashWithPromotionsAndSegments();
    List newCashBudgetMasterList = new ArrayList();
    for ( Iterator iter = cashBudgetMasterList.iterator(); iter.hasNext(); )
    {
      boolean canAdd = true;
      BudgetMaster budgetMaster = (BudgetMaster)iter.next();
      // If the BudgetMaster is 'one promotion only' and is assigned to another promotion
      // then it cannot be displayed
      if ( !budgetMaster.isMultiPromotion() && budgetMaster.getCashPromotions() != null && !budgetMaster.getCashPromotions().isEmpty() )
      {
        canAdd = false;
        Iterator promotionIter = budgetMaster.getCashPromotions().iterator();
        while ( promotionIter.hasNext() )
        {
          Promotion promotion = (Promotion)promotionIter.next();
          if ( promotion.getId().equals( promoAwardsForm.getPromotionId() ) )
          {
            canAdd = true;
            break; // no need to continue looping through all promotions
          }
        }
      }

      if ( canAdd )
      {
        populateBudgetMasterListElement( newCashBudgetMasterList, null, budgetMaster, promotionTypeCode );
      }

    }

    Collections.sort( newCashBudgetMasterList, new PromotionAwardsFormBeanComparator() );
    request.setAttribute( "cashBudgetMasterList", newCashBudgetMasterList );

    request.setAttribute( "hardCapBudgetType", BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );

    List finalPayoutRuleList = BudgetFinalPayoutRule.getList();

    List finalPayoutRuleDisplayList = new ArrayList();

    Iterator ruleIter = finalPayoutRuleList.iterator();
    while ( ruleIter.hasNext() )
    {
      BudgetFinalPayoutRule payoutRule = (BudgetFinalPayoutRule)ruleIter.next();
      LabelValueBean labelValueBean = new LabelValueBean();

      labelValueBean.setValue( payoutRule.getCode() );
      labelValueBean.setLabel( MessageFormat.format( payoutRule.getName(), new Object[] { CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.QUIZ_LABEL" ) } ) );

      finalPayoutRuleDisplayList.add( labelValueBean );
    }

    request.setAttribute( "budgetFinalPayoutRuleList", finalPayoutRuleDisplayList );

  }

  private void populateBudgetMasterListElement( List newBudgetMasterList, List newBudgetMasterWithSweepBudgetList, BudgetMaster budgetMaster, String promotionTypeCode )
  {
    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      // if one segment in a budget master has budget sweep enabled, then the remaining
      // ones should have budget sweep enabled too
      Set<BudgetSegment> budgetSegments = budgetMaster.getBudgetSegments();
      List<BudgetSegment> budgetSegmentsList = new ArrayList<BudgetSegment>();
      if ( budgetSegments != null && !budgetSegments.isEmpty() )
      {
        // sort list by name
        budgetSegmentsList.addAll( budgetSegments );
        Collections.sort( budgetSegmentsList, new BudgetSegmentComparator() );
      }

      for ( BudgetSegment budgetSegment : budgetSegmentsList )
      {
        if ( budgetSegment.getPromotionBudgetSweeps().isEmpty() )
        {
          PromotionAwardsFormBean bean = new PromotionAwardsFormBean();
          bean.setBudgetMasterId( budgetMaster.getId() );
          String name = CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() );
          bean.setBudgetMasterName( name );
          bean.setBudgetType( budgetMaster.getBudgetType().getCode() );
          newBudgetMasterList.add( bean );
        }
        else
        {
          PromotionAwardsFormBean promotionAwardsFormBean = new PromotionAwardsFormBean();
          promotionAwardsFormBean.setBudgetMasterId( budgetMaster.getId() );
          String name = CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() );
          promotionAwardsFormBean.setBudgetMasterName( name );
          promotionAwardsFormBean.setBudgetType( budgetMaster.getBudgetType().getCode() );
          newBudgetMasterWithSweepBudgetList.add( promotionAwardsFormBean );
        }
        break;
      }
    }
    else
    {
      PromotionAwardsFormBean bean = new PromotionAwardsFormBean();
      bean.setBudgetMasterId( budgetMaster.getId() );
      String name = CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() );
      bean.setBudgetMasterName( name );
      bean.setBudgetType( budgetMaster.getBudgetType().getCode() );
      newBudgetMasterList.add( bean );
    }
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
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

  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

  protected static PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  protected static MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Returns a reference to Promotion Service
   *
   * @return reference to Promotion Service
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private CurrencyService getCurrencyService()
  {
    return (CurrencyService)getService( CurrencyService.BEAN_NAME );
  }

  private List<PickListItem> getNominationTimePeriods()
  {
    List<PickListItem> pickList = TimeframePeriodType.getList();
    List<PickListItem> nominationTimePeriods = new ArrayList<PickListItem>();

    if ( pickList != null && pickList.size() > 0 )
    {
      for ( PickListItem pickListItem : pickList )
      {
        if ( !pickListItem.getCode().equals( ANNUALLY ) )
        {
          nominationTimePeriods.add( pickListItem );
        }
      }
    }
    return nominationTimePeriods;
  }

}
