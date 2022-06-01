/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.ScoreBy;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionAwardsUpdateAssociation;
import com.biperf.core.service.promotion.PromotionBudgetMasterUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.UserValueBean;
import com.biperf.util.StringUtils;

/**
 * PromotionAwardsAction.
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
public class PromotionAwardsAction extends PromotionBaseDispatchAction
{
  private static final String SESSION_PROMO_AWARDS_FORM = "SESSION_PROMO_AWARDS_FORM";

  private static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  private static String FIXED = "true";
  private static String RANGE = "false";
  private static String CALCULATOR = "cal";

  /**
   * Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward redisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAwardsForm promoAwardsForm = (PromotionAwardsForm)form;
    promoAwardsForm.setAwardAmountTypeFixed( "true" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionAwardsForm promoAwardsForm = (PromotionAwardsForm)form;

    int budgetSegmentVBListSize = promoAwardsForm.getBudgetSegmentVBListSize();
    if ( budgetSegmentVBListSize > 0 )
    {
      request.setAttribute( "lastSegmentIndex", budgetSegmentVBListSize - 1 );
    }
    else
    {
      request.setAttribute( "lastSegmentIndex", 0 );
    }

    Promotion promotion = null;
    promoAwardsForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      if ( promotion.isNominationPromotion() )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );
        NominationPromotion attachedPromotion = (NominationPromotion)getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
        ( (NominationPromotion)promotion ).setNominationLevels( attachedPromotion.getNominationLevels() );
      }
    }
    else
    {
      Long promotionId = promoAwardsForm.getPromotionId();

      if ( promotionId != null )
      {

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOM_BUDGET_APPROVER ) );
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS ) );
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }
    if ( promotion != null )
    {
      promoAwardsForm.load( promotion );
    }
    ActionMessages errors = new ActionMessages();
    if ( null != promotion && promotion.isRecognitionPromotion() && null != promotion.getAwardType() && promotion.getAwardType().isMerchandiseAwardType() )
    {
      try
      {
        /*
         * Create Associate Request for Country and hydrate the levels.
         */
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
        List countryList = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promoAwardsForm.getPromotionId(), associationRequestCollection );
        MerchLevelService merchLevelService = getMerchLevelService();
        merchLevelService.mergeMerchLevelWithOMList( countryList );
        promoAwardsForm.loadCountryList( countryList );
        loadMerchLevels( countryList, promoAwardsForm );

      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    request.setAttribute( "promotionType", promotion.getPromotionType().getCode() );
    return mapping.findForward( forwardTo );
  }

  private void loadMerchLevels( List countryList, PromotionAwardsForm promoAwardsForm ) throws ServiceErrorException
  {
    List programIds = new ArrayList();
    for ( int i = 0; i < countryList.size(); i++ )
    {
      PromoMerchCountry country = (PromoMerchCountry)countryList.get( i );
      String programId = country.getProgramId();
      if ( !programIds.contains( programId ) )
      {
        programIds.add( programId );
      }
    }

    promoAwardsForm.loadStdMerchLevelProducts( getMerchLevelService().getUniqueMerchlinqLevelDataForPrograms( programIds ) );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionAwardsForm promoAwardsForm = (PromotionAwardsForm)form;
    // Bug # 29597 -Start
    String bgMasterId = request.getParameter( "budgetMasterId" );
    if ( bgMasterId != null && !bgMasterId.equals( "" ) )
    {
      promoAwardsForm.setBudgetMasterId( new Long( bgMasterId ) );
    }

    String cashBgMasterId = request.getParameter( "cashBudgetMasterId" );
    if ( cashBgMasterId != null && !cashBgMasterId.equals( "" ) )
    {
      promoAwardsForm.setCashBudgetMasterId( new Long( cashBgMasterId ) );
    }
    // Bug # 29597 -End

    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }

      return forward;
    }

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOM_BUDGET_APPROVER ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );

      promotion = getPromotionService().getPromotionByIdWithAssociations( promoAwardsForm.getPromotionId(), associationRequestCollection );

    }

    if ( promotion.isRecognitionPromotion() )
    {
      if ( promoAwardsForm.getAwardsType().equals( PromotionAwardsType.MERCHANDISE ) && !promoAwardsForm.isUseRecognitionCalculator() )
      {
        promotion.setCalculator( null );
        promotion.setScoreBy( null );
      }
      else if ( promoAwardsForm.getCalculatorId() != null && promoAwardsForm.getCalculatorId().longValue() != 0 )
      {
        Calculator calculator = getCalculatorService().getCalculatorById( promoAwardsForm.getCalculatorId() );
        promotion.setCalculator( calculator );
        promotion.setScoreBy( ScoreBy.lookup( promoAwardsForm.getScoreBy() ) );
      }
    }

    // Need to save new BudgetMaster before updating Promotion
    // (otherwise a transient object exception occurs)
    if ( promoAwardsForm.isCreateNewBudgetMaster() )
    {
      if ( promoAwardsForm.getBudgetMasterName().trim() != null && !promoAwardsForm.getBudgetMasterName().trim().equals( "" ) )
      {
        BudgetMaster budgetMaster = promoAwardsForm.getNewBudgetMaster();

        // segment logic
        budgetMaster.getBudgetSegments().clear();
        for ( Iterator<BudgetSegmentValueBean> iter = promoAwardsForm.getBudgetSegmentVBList().iterator(); iter.hasNext(); )
        {
          BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)iter.next();

          // build budget segment obj
          BudgetSegment budgetSegment = new BudgetSegment();
          budgetSegment = promoAwardsForm.populateBudgetSegment( budgetSegmentVB );

          if ( budgetMaster.getBudgetType().isCentralBudgetType() )
          {
            Budget budget = new Budget();

            budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );

            BigDecimal originalValueLocal = new BigDecimal( budgetSegmentVB.getOriginalValue() );
            budget.setOriginalValue( originalValueLocal );
            budget.setCurrentValue( originalValueLocal );
            budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

            budgetSegment.getBudgets().clear();
            budgetSegment.addBudget( budget );
          }

          // logic to add budget sweep date to recognition promotion for the associated
          // budget segment.
          if ( promotion.isRecognitionPromotion() && PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
          {
            if ( budgetMaster.getBudgetType().isPaxBudgetType() && promoAwardsForm.isBudgetSweepEnabled() )
            {
              if ( budgetSegmentVB.getBudgetSweepDate() != null && budgetSegmentVB.getBudgetSweepDate().length() > 0 )
              {
                PromotionBudgetSweep promoBudgetSweep = new PromotionBudgetSweep();
                promoBudgetSweep.setBudgetSweepDate( DateUtils.toDate( budgetSegmentVB.getBudgetSweepDate() ) );
                promoBudgetSweep.setStatus( Boolean.TRUE );
                promoBudgetSweep.setRecognitionPromotion( (RecognitionPromotion)promotion );
                budgetSegment.addPromotionBudgetSweep( promoBudgetSweep );
              }
            }
          }

          budgetMaster.addBudgetSegment( budgetSegment );
        }

        if ( budgetMaster.getBudgetType().isCentralBudgetType() )
        {
          budgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( BudgetFinalPayoutRule.NO_PAYOUT ) );
        }

        // While creating a new Budget Master
        // If this promotion is eligible for Budget Sweep, validate Budget Master to make sure its
        // of type PAX
        if ( promoAwardsForm.isBudgetSweepEnabled() && !validateBudgetSweep( budgetMaster ) )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.BUDGET_SWEEP_BUDGET_ERROR" ) );
        }
        // If want to display this promotion in Budget Tracker, validate Budget Master to make sure
        // its of type PAX or NODE
        if ( promoAwardsForm.isShowInBudgetTracker() && !validateBudgetTracker( budgetMaster ) )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.BUDGET_TRACKER_BUDGET_ERROR" ) );
        }

        if ( errors.isEmpty() )
        {
          try
          {
            budgetMaster = getBudgetMasterService().saveBudgetMaster( budgetMaster );
            promotion.setBudgetMaster( budgetMaster );
          }
          catch( NonUniqueDataServiceErrorException e )
          {
            // If the name is not unique, the send back the error instead of
            // continuing on
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.BUDGET_NAME_EXISTS" ) );
          }
          catch( ServiceErrorException se )
          {
            ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
          }
          catch( Exception e )
          {
            e.printStackTrace();
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.GENERIC_ERROR" ) );
          }
        }
      }
      else
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.NOT_VALID" ) );
      }
    }

    if ( promoAwardsForm.isUseExistingBudgetMaster() )
    {
      Long budgetMasterId = promoAwardsForm.getBudgetMasterId();
      if ( null == budgetMasterId )
      {
        budgetMasterId = promoAwardsForm.getHiddenBudgetMasterId();
      }
      if ( budgetMasterId.longValue() == 0 )
      {

        if ( !StringUtils.isEmpty( request.getParameter( "budgetMasterId" ) ) )
        {
          budgetMasterId = new Long( request.getParameter( "budgetMasterId" ) );
          promoAwardsForm.setBudgetMasterId( budgetMasterId );
        }
      }
      // If using an existing Budget Master
      // If this promotion is eligible for Budget Sweep, validate Budget Master to make sure its of
      // type PAX
      if ( promoAwardsForm.isBudgetSweepEnabled() && !validateBudgetSweep( budgetMasterId ) )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.BUDGET_SWEEP_BUDGET_ERROR" ) );
      }
      // If want to display this promotion in Budget Tracker, validate Budget Master to make sure
      // its of type PAX or NODE
      if ( promoAwardsForm.isShowInBudgetTracker() && !validateBudgetTracker( budgetMasterId ) )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.BUDGET_TRACKER_BUDGET_ERROR" ) );
      }
    }

    // Need to save new cash BudgetMaster before updating Promotion
    // (otherwise a transient object exception occurs)
    if ( promoAwardsForm.isCreateNewCashBudgetMaster() )
    {
      if ( promoAwardsForm.getCashBudgetMasterName().trim() != null && !promoAwardsForm.getCashBudgetMasterName().trim().equals( "" ) )
      {
        BudgetMaster cashBudgetMaster = promoAwardsForm.getNewCashBudgetMaster();

        // segment logic
        cashBudgetMaster.getBudgetSegments().clear();
        for ( Iterator<BudgetSegmentValueBean> iter = promoAwardsForm.getCashBudgetSegmentVBList().iterator(); iter.hasNext(); )
        {
          BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)iter.next();

          // build budget segment obj
          BudgetSegment budgetSegment = new BudgetSegment();
          budgetSegment = promoAwardsForm.populateBudgetSegment( budgetSegmentVB );

          if ( cashBudgetMaster.getBudgetType().isCentralBudgetType() )
          {
            Budget budget = new Budget();

            budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );

            BigDecimal originalValueLocal = new BigDecimal( budgetSegmentVB.getOriginalValue() );
            budget.setOriginalValue( originalValueLocal );
            budget.setCurrentValue( originalValueLocal );
            budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

            budgetSegment.getBudgets().clear();
            budgetSegment.addBudget( budget );
          }

          cashBudgetMaster.addBudgetSegment( budgetSegment );
        }

        if ( cashBudgetMaster.getBudgetType().isCentralBudgetType() )
        {
          cashBudgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( BudgetFinalPayoutRule.NO_PAYOUT ) );
        }

        if ( errors.isEmpty() )
        {
          try
          {
            cashBudgetMaster = getBudgetMasterService().saveBudgetMaster( cashBudgetMaster );
            promotion.setCashBudgetMaster( cashBudgetMaster );
          }
          catch( NonUniqueDataServiceErrorException e )
          {
            // If the name is not unique, the send back the error instead of
            // continuing on
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.BUDGET_NAME_EXISTS" ) );
          }
          catch( ServiceErrorException se )
          {
            ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
          }
          catch( Exception e )
          {
            e.printStackTrace();
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.GENERIC_ERROR" ) );
          }
        }
      }
      else
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.NOT_VALID" ) );
      }
    }

    if ( promoAwardsForm.isUseExistingCashBudgetMaster() )
    {
      Long cashBudgetMasterId = promoAwardsForm.getCashBudgetMasterId();
      if ( null == cashBudgetMasterId )
      {
        cashBudgetMasterId = promoAwardsForm.getHiddenCashBudgetMasterId();
      }
      if ( cashBudgetMasterId.longValue() == 0 )
      {

        if ( !StringUtils.isEmpty( request.getParameter( "cashBudgetMasterId" ) ) )
        {
          cashBudgetMasterId = new Long( request.getParameter( "cashBudgetMasterId" ) );
          promoAwardsForm.setCashBudgetMasterId( cashBudgetMasterId );
        }
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    try
    {
      List countryList = null;
      if ( promoAwardsForm.getAwardsType() != null && promoAwardsForm.getAwardsType().equals( "merchandise" ) )
      {
        if ( promoAwardsForm.isAwardsActive() )
        {
          if ( promotion.isRecognitionPromotion() )
          {
            ( (RecognitionPromotion)promotion ).setAwardAmountTypeFixed( false );
          }
          promoAwardsForm.setAwardAmountTypeFixed( RANGE );
        }
        /*
         * Read the country list with the level names from DB
         */
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );

        countryList = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promoAwardsForm.getPromotionId(), associationRequestCollection );

        /*
         * Update the level names from the screen back to DB.
         */
        if ( countryList != null )
        {
          int ordinalPositionCounter = 1;
          MerchLevelService merchLevelService = getMerchLevelService();
          // Do the merging for each country
          for ( int index = 0; index < countryList.size(); index++ )
          {
            PromoMerchCountry promoMerchCountry = (PromoMerchCountry)countryList.get( index );
            AwardBanqMerchResponseValueObject merchlinqLevelData = null;
            if ( !Environment.isCtech() )
            {
              merchlinqLevelData = merchLevelService.getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId(), "qa" );
            }
            else
            {
              merchlinqLevelData = merchLevelService.getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId() );
            }

            Collection levels = merchlinqLevelData.getMerchLevel();
            if ( levels != null )
            {
              ordinalPositionCounter = 1;
              for ( Iterator iter = levels.iterator(); iter.hasNext(); )
              {
                MerchLevelValueObject merchLevel = (MerchLevelValueObject)iter.next();
                PromoMerchProgramLevel levelObj = new PromoMerchProgramLevel();
                levelObj.setLevelName( merchLevel.getName() );
                boolean found = false;
                if ( promoMerchCountry.getLevels() != null && promoMerchCountry.getLevels().size() > 0 )
                {
                  for ( Iterator levelIter = promoMerchCountry.getLevels().iterator(); levelIter.hasNext(); )
                  {
                    PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
                    if ( promoMerchProgramLevel.getLevelName().equals( levelObj.getLevelName() ) )
                    {
                      promoMerchProgramLevel.setMinValue( merchLevel.getMinValue() );
                      promoMerchProgramLevel.setMaxValue( merchLevel.getMaxValue() );
                      promoMerchProgramLevel.setLevelName( merchLevel.getName() );
                      if ( promoMerchProgramLevel.getOrdinalPosition() == 0 )
                      {
                        promoMerchProgramLevel.setOrdinalPosition( ordinalPositionCounter );
                        ordinalPositionCounter++;
                      }
                      promoMerchProgramLevel.setProgramId( promoMerchCountry.getProgramId() );
                      found = true;
                      break;
                    }
                  }
                }
                if ( !found )
                {
                  levelObj.setMinValue( merchLevel.getMinValue() );
                  levelObj.setMaxValue( merchLevel.getMaxValue() );
                  levelObj.setLevelName( merchLevel.getName() );
                  levelObj.setProgramId( promoMerchCountry.getProgramId() );
                  if ( promoMerchCountry.getLevels() == null )
                  {
                    promoMerchCountry.setLevels( new LinkedHashSet() );
                  }
                  levelObj.setOrdinalPosition( ordinalPositionCounter );
                  ordinalPositionCounter++;
                  promoMerchCountry.getLevels().add( levelObj );
                }
              }
            }
            countryList.set( index, promoMerchCountry );
          }
          countryList = promoAwardsForm.toPromoMerchProgramLevelDomainObject( countryList );
          loadMerchLevels( countryList, promoAwardsForm );
        }
      }

      promotion = promoAwardsForm.toDomainObject( promotion );

      if ( promotion.isNominationPromotion() )
      {
        NominationPromotion nomPromo = (NominationPromotion)promotion;

        if ( !nomPromo.getCustomApproverOptions().isEmpty() )
        {

          for ( Iterator<ApproverOption> iter = nomPromo.getCustomApproverOptions().iterator(); iter.hasNext(); )
          {
            ApproverOption option = iter.next();

            if ( option.getApprovalLevel() > 1 && option.getApproverType().getCode().equals( CustomApproverType.AWARD ) )
            {
              Long awardAsApprovalOptionIndex = option.getApprovalLevel();

              List<NominationPromotionLevel> nomPromoLevels = new ArrayList<NominationPromotionLevel>( nomPromo.getNominationLevels() );

              if ( nomPromoLevels != null && nomPromoLevels.size() > 0 )
              {
                NominationPromotionLevel previousPromoLevel = null;
                for ( Iterator<NominationPromotionLevel> levelIter = nomPromo.getNominationLevels().iterator(); levelIter.hasNext(); )
                {
                  NominationPromotionLevel nomPromolevel = levelIter.next();

                  if ( nomPromolevel.getLevelIndex().equals( awardAsApprovalOptionIndex ) )
                  {
                    if ( previousPromoLevel != null && !nomPromolevel.getAwardPayoutType().equals( previousPromoLevel.getAwardPayoutType() ) )
                    {
                      errors.add( ActionMessages.GLOBAL_MESSAGE,
                                  new ActionMessage( "promotion.awards.errors.PAYOUT_TYPE_MATCH", String.valueOf( awardAsApprovalOptionIndex - 1 ), String.valueOf( awardAsApprovalOptionIndex ) ) );
                    }

                    if ( previousPromoLevel != null )
                    {
                      BigDecimal previousMin = previousPromoLevel.getNominationAwardAmountMin();
                      BigDecimal previousMax = previousPromoLevel.getNominationAwardAmountMax();

                      BigDecimal currentMin = nomPromolevel.getNominationAwardAmountMin();
                      BigDecimal currentMax = nomPromolevel.getNominationAwardAmountMax();

                      if ( previousMin != null && !previousMin.equals( currentMin ) )
                      {
                        Long tempMinApprovalOptionIndex = awardAsApprovalOptionIndex;
                        errors.add( ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage( "promotion.awards.errors.MIN_AMOUNT_MATCH", String.valueOf( tempMinApprovalOptionIndex - 1 ), String.valueOf( tempMinApprovalOptionIndex ) ) );
                      }

                      if ( previousMax != null && !previousMax.equals( currentMax ) )
                      {
                        Long tempMaxApprovalOptionIndex = awardAsApprovalOptionIndex;
                        errors.add( ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage( "promotion.awards.errors.MAX_AMOUNT_MATCH", String.valueOf( tempMaxApprovalOptionIndex - 1 ), String.valueOf( tempMaxApprovalOptionIndex ) ) );
                      }
                    }

                  }
                  previousPromoLevel = nomPromolevel;
                }
              }
            }
          }
        }

      }

      if ( promoAwardsForm.getNominationPayoutFinalLevelList().size() > 0 || promoAwardsForm.getNominationPayoutEachLevelList().size() > 0 )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)promotion;
        promotion = getPromotionService().saveLevelLabelCmText( nominationPromotion );
        promotion = getPromotionService().savePayoutDescriptionCmText( nominationPromotion );
      }

      if ( promoAwardsForm.getNominationTimePeriodVBList().size() > 0 )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)promotion;
        promotion = getPromotionService().saveTimePeriodNameCmText( nominationPromotion );
      }

      PromotionBudgetMasterUpdateAssociation promotionBudgetMasterUpdateAssociation = new PromotionBudgetMasterUpdateAssociation( promotion );
      PromotionAwardsUpdateAssociation promotionAwardsUpdateAssociation = new PromotionAwardsUpdateAssociation( promotion );

      List updateAssociations = new ArrayList();
      updateAssociations.add( promotionBudgetMasterUpdateAssociation );
      updateAssociations.add( promotionAwardsUpdateAssociation );

      promotion = getPromotionService().savePromotion( promoAwardsForm.getPromotionId(), updateAssociations );

      if ( countryList != null )
      {
        for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
        {
          getPromoMerchCountryService().savePromoMerchCountry( (PromoMerchCountry)countryIter.next() );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    catch( UniqueConstraintViolationException e )
    {
      throw new BeaconRuntimeException( "This call shouldn't change any unique fields, so must be software bug.", e );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );
        if ( promotion.isWellnessPromotion() )
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }

      if ( !promoAwardsForm.isAwardsActive() || promoAwardsForm.isFileloadBudgetAmount() || promoAwardsForm.getBudgetOption().equals( PromotionAwardsForm.BUDGET_NONE )
          || promoAwardsForm.getBudgetType().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
      {
        if ( isWizardMode( request ) )
        {
          if ( isSaveAndExit( request ) )
          {
            forward = saveAndExit( mapping, request, promotion );
          }
          else if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() && ! ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            forward = mapping.findForward( "saveAndContinueSweepstake" );
          }
          else if ( promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            forward = mapping.findForward( "saveAndContinueSweepstake" );
          }
          else
          {
            forward = getWizardNextPage( mapping, request, promotion );
          }
        }
        else
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }
      else if ( promoAwardsForm.getBudgetOption().equals( PromotionAwardsForm.BUDGET_EXISTING ) && promotion.getBudgetMaster().getBudgetType().isCentralBudgetType() )
      {
        if ( isWizardMode( request ) )
        {
          if ( isSaveAndExit( request ) )
          {
            forward = saveAndExit( mapping, request, promotion );
          }
          else if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() && ! ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            forward = mapping.findForward( "saveAndContinueSweepstake" );
          }
          else if ( promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            forward = mapping.findForward( "saveAndContinueSweepstake" );
          }
          else
          {
            forward = getWizardNextPage( mapping, request, promotion );
          }
        }
        else
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }
      else if ( promoAwardsForm.getBudgetOption().equals( PromotionAwardsForm.BUDGET_NEW ) && !promoAwardsForm.isFileloadBudgetAmount() )
      {
        forward = mapping.findForward( "saveAndContinueBudget" );
      }
      else
      {
        forward = mapping.findForward( "saveAndContinueBudget" );
      }
    }

    return forward;

  }

  private boolean validateBudgetSweep( Long budgetMasterId )
  {
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, null );
    if ( budgetMaster != null )
    {
      return validateBudgetSweep( budgetMaster );
    }
    else
    {
      return false;
    }
  }

  private boolean validateBudgetSweep( BudgetMaster budgetMaster )
  {
    // Should be PAX BudgetType
    return budgetMaster.getBudgetType().isPaxBudgetType();
  }

  private boolean validateBudgetTracker( Long budgetMasterId )
  {
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, null );
    return validateBudgetTracker( budgetMaster );
  }

  private boolean validateBudgetTracker( BudgetMaster budgetMaster )
  {
    // Should be either PAX or NODE BudgetType
    return budgetMaster.getBudgetType().isPaxBudgetType() || budgetMaster.getBudgetType().isNodeBudgetType();
  }

  /**
   * Makes a request to the Approver builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareApproverLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)form;

    // Put the form in the session to be reloaded when coming back from
    // approver lookup.
    request.getSession().setAttribute( SESSION_PROMO_AWARDS_FORM, promotionAwardsForm );

    String returnUrl = RequestUtils.getRequiredParamString( request, "approverLookupReturnUrl" );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/participant/listBuilderPaxDisplay.do?" + ListBuilderAction.AUDIENCE_MEMBERS_LOOKUP_RETURN_URL_PARAM + "=" + returnUrl
        + "&singleResult=true" );

    return null;
  }

  /**
   * Handles the return from the participant search. This will look for the participantId
   * (approverId) on the request.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward returnApproverLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionAwardsForm sessionPromotionAwardsForm = (PromotionAwardsForm)request.getSession().getAttribute( SESSION_PROMO_AWARDS_FORM );
    request.getSession().removeAttribute( SESSION_PROMO_AWARDS_FORM );

    if ( sessionPromotionAwardsForm != null )
    {
      List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

      if ( participants != null )
      {
        Iterator participantIter = participants.iterator();
        if ( participantIter.hasNext() )
        {
          FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
          sessionPromotionAwardsForm.setBudgetApproverId( participantBean.getId() );
          sessionPromotionAwardsForm.setBudgetApproverName( participantBean.getLastName() + ", " + participantBean.getFirstName() );
        }
      }

      try
      {
        BeanUtils.copyProperties( promotionAwardsForm, sessionPromotionAwardsForm );
      }
      catch( IllegalArgumentException iae )
      {
        // no audience returned
      }
      catch( Exception e )
      {
        log.info( "returnApproverLookup: Copy Properties failed." );
      }
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addAnotherSegment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success_add_another" );
    ActionMessages errors = new ActionMessages();
    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)actionForm;

    promotionAwardsForm.addEmptyBudgetSegment();
    request.setAttribute( "lastSegmentIndex", promotionAwardsForm.getBudgetSegmentVBListSize() - 1 );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( "failure_add_another" );
    }

    return forward;
  }

  public ActionForward addAnotherCashSegment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success_add_another" );
    ActionMessages errors = new ActionMessages();
    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)actionForm;

    promotionAwardsForm.addEmptyCashBudgetSegment();
    request.setAttribute( "lastSegmentIndex", promotionAwardsForm.getBudgetSegmentVBListSize() - 1 );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( "failure_add_another" );
    }

    return forward;
  }

  public ActionForward removeBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)form;
    promotionAwardsForm.getBudgetSegmentVBList().remove( promotionAwardsForm.getBudgetSegmentVBListSize() - 1 );
    return forward;
  }

  public ActionForward removeCashBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)form;
    promotionAwardsForm.getCashBudgetSegmentVBList().remove( promotionAwardsForm.getCashBudgetSegmentVBListSize() - 1 );
    return forward;
  }

  public ActionForward addAnotherTimePeriod( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success_add_another" );
    ActionMessages errors = new ActionMessages();
    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)actionForm;

    promotionAwardsForm.addFirstNominationTimePeriod();
    promotionAwardsForm.setTimePeriodActive( true );
    request.setAttribute( "lastSegmentIndex", promotionAwardsForm.getNominationTimePeriodVBListSize() - 1 );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( "failure_add_another" );
    }

    return forward;
  }

  public ActionForward removeTimePeriod( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)form;
    promotionAwardsForm.getNominationTimePeriodVBList().remove( promotionAwardsForm.getNominationTimePeriodVBListSize() - 1 );
    return forward;
  }

  /**
   * Search for a person to approve requests for more budget. Search is by last name.
   */
  public ActionForward searchApprover( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionAwardsForm promotionAwardsForm = (PromotionAwardsForm)form;

    String bgMasterId = request.getParameter( "budgetMasterId" );
    if ( bgMasterId != null && !bgMasterId.equals( "" ) )
    {
      promotionAwardsForm.setBudgetMasterId( new Long( bgMasterId ) );
    }

    // Get search results based on last name only.
    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setLastName( promotionAwardsForm.getBudgetApproverSearchLastName() );
    searchCriteria.setSortByLastNameFirstName( true );
    List<Participant> resultList = getParticipantService().searchParticipant( searchCriteria, true );

    // I'm not sure but I get the feel request attributes are usually light-weight, so change from
    // participant to lighter value bean object?
    List<UserValueBean> beanResultList = new ArrayList<UserValueBean>( resultList.size() );
    for ( Participant p : resultList )
    {
      UserValueBean bean = new UserValueBean();
      bean.setId( p.getId() );
      bean.setFirstName( p.getFirstName() );
      bean.setLastName( p.getLastName() );
      beanResultList.add( bean );
    }

    request.setAttribute( "approverSearchResults", beanResultList );
    request.setAttribute( "approverSearchResultsCount", beanResultList.size() );

    return forward;
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
    return (BudgetMasterService)BeanLocator.getBean( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

  /**
   * Get the UserService from the beanFactory locator.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

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
}
