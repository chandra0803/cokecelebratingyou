/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;

/**
 * PromotionTranslationsAction.
 * 
 * @author arasi
 * @since 16-Aug-2012
 * @version 1.0
 */
public class PromotionTranslationsAction extends PromotionBaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionTranslationsAction.class );
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
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
    PromotionTranslationsForm promoTranslationsForm = (PromotionTranslationsForm)form;

    Promotion promotion = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    // NORMAL MODE
    else
    {
      String promotionId = promoTranslationsForm.getPromotionId();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
      }
    }

    if ( promotion != null )
    {
      Promotion attachedPromotion = getPromotion( promotion );

      promoTranslationsForm.load( attachedPromotion );
    }

    return mapping.findForward( forwardTo );
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

    PromotionTranslationsForm promoTranslationsForm = (PromotionTranslationsForm)form;
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

      promotion = getPromotion( promotion );

    }
    // NORMAL MODE
    else
    {
      String promotionId = promoTranslationsForm.getPromotionId();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
        promotion = getPromotion( promotion );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Promotion attachedPromotion = getPromotion( promotion );

    try
    {

      for ( Iterator iter = promoTranslationsForm.getTranslationsTextList().iterator(); iter.hasNext(); )
      {
        PromotionTranslationFormBean bean = (PromotionTranslationFormBean)iter.next();
        String data = bean.getRulesText();
        String localecode = bean.getLocaleCode();
        String promotionName = bean.getPromotionName();
        String overviewDetailsText = bean.getOverviewDetailsText();
        String promotionObjective = bean.getPromotionObjective();
        String managerRulesText = bean.getManagerRulesText();
        String partnerRulesText = bean.getPartnerRulesText();
        String baseUnit = bean.getBaseUnit();
        List<PromotionTranslationLevelLabelFormBean> levelLabelsList = bean.getLevelLabelsList();
        List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionsList = bean.getPayoutDescriptionList();
        List<PromotionTranslationTimePeriodNameFormBean> timePeriodNamesList = bean.getTimePeriodNamesList();
        List<PromotionTranslationBudgetSegmentNamesFormBean> budgetSegmentNamesList = bean.getBudgetSegmentNamesList();
        if ( StringUtils.isNotEmpty( promotionName ) )
        {
          promotion = getPromotionService().savePromoNameCmText( promotion, promotionName, CmsUtil.getLocale( localecode ), true );
        }

        if ( promotion.isNominationPromotion() )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)promotion;
          if ( levelLabelsList.size() > 0 )
          {
            int i = 0;
            for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
            {
              PromotionTranslationLevelLabelFormBean promotionTranslationLevelLabelFormBean = levelLabelsList.get( i );
              nominationPromotionLevel.setLevelLabel( promotionTranslationLevelLabelFormBean.getLevelLabel() );
              i++;
            }
            promotion = getPromotionService().saveLevelLabelCmText( nominationPromotion );
          }
          if ( payoutDescriptionsList.size() > 0 )
          {
            int i = 0;
            for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
            {
              if ( nominationPromotionLevel.getAwardPayoutType().isOtherAwardType() )
              {
                PromotionTranslationPayoutDescriptionFormBean promotionTranslationPayoutDescriptionFormBean = payoutDescriptionsList.get( i );
                nominationPromotionLevel.setPayoutDescription( promotionTranslationPayoutDescriptionFormBean.getPayoutDescription() );
                i++;
              }
            }
            promotion = getPromotionService().savePayoutDescriptionCmText( nominationPromotion );
          }
          if ( timePeriodNamesList.size() > 0 )
          {
            int j = 0;
            for ( NominationPromotionTimePeriod nominationPromotionTimePeriod : nominationPromotion.getNominationTimePeriods() )
            {
              PromotionTranslationTimePeriodNameFormBean promotionTranslationTimePeriodNameFormBean = timePeriodNamesList.get( j );
              nominationPromotionTimePeriod.setTimePeriodName( promotionTranslationTimePeriodNameFormBean.getTimePeriodName() );
              j++;
            }
            promotion = getPromotionService().saveTimePeriodNameCmText( nominationPromotion );
          }
          if ( budgetSegmentNamesList.size() > 0 )
          {
            int k = 0;
            if ( promotion.getBudgetMaster() != null )
            {
              for ( BudgetSegment budgetSegment : promotion.getBudgetMaster().getBudgetSegments() )
              {
                PromotionTranslationBudgetSegmentNamesFormBean promotionTranslationBudgetSegmentNamesFormBean = budgetSegmentNamesList.get( k );
                budgetSegment.setName( promotionTranslationBudgetSegmentNamesFormBean.getBudgetTimePeriodName() );
                k++;
              }
              promotion = getPromotionService().saveBudgetSegmentNameCmText( promotion );
            }
          }
        }

        if ( promoTranslationsForm.isWebRulesActive() && StringUtils.isNotEmpty( data ) )
        {
          promotion = getPromotionService().saveWebRulesCmText( promotion, data, CmsUtil.getLocale( localecode ) );
        }
        if ( promoTranslationsForm.isQuizPromotion() && StringUtils.isNotEmpty( overviewDetailsText ) )
        {
          promotion = getPromotionService().savePromotionOverviewCmText( promotion, overviewDetailsText, CmsUtil.getLocale( localecode ) );
        }
        if ( promotion.isThrowdownPromotion() && StringUtils.isNotEmpty( overviewDetailsText ) )
        {
          promotion = getPromotionService().savePromotionOverviewCmText( promotion, overviewDetailsText, CmsUtil.getLocale( localecode ) );
        }
        if ( promoTranslationsForm.isGoalQuestOrChallengePointPromotion() && StringUtils.isNotEmpty( promotionObjective ) )
        {
          promotion = getPromotionService().savePromoObjectivieCmText( (GoalQuestPromotion)promotion, promotionObjective, CmsUtil.getLocale( localecode ) );

          if ( !StringUtils.isEmpty( overviewDetailsText ) )
          {
            promotion = getPromotionService().savePromotionOverviewCmText( promotion, overviewDetailsText, CmsUtil.getLocale( localecode ) );
          }
        }
        if ( promotion.isGoalQuestOrChallengePointPromotion() && StringUtils.isNotEmpty( managerRulesText ) )
        {
          promotion = getPromotionService().saveWebRulesManagerCmText( (GoalQuestPromotion)promotion, managerRulesText, CmsUtil.getLocale( localecode ) );
        }
        if ( promoTranslationsForm.isPartnerAvailable( attachedPromotion ) )
        {
          if ( promotion.isGoalQuestOrChallengePointPromotion() && StringUtils.isNotEmpty( partnerRulesText ) )
          {
            promotion = getPromotionService().saveWebRulesPartnerCmText( (GoalQuestPromotion)promotion, partnerRulesText, CmsUtil.getLocale( localecode ) );
          }
        }
        if ( promotion.isGoalQuestOrChallengePointPromotion() && StringUtils.isNotEmpty( baseUnit ) )
        {
          promotion = getPromotionService().savePayoutStrutureBaseUnitInCM( (GoalQuestPromotion)promotion, baseUnit, CmsUtil.getLocale( localecode ) );
        }
        if ( promotion.isThrowdownPromotion() && StringUtils.isNotEmpty( baseUnit ) )
        {
          promotion = getPromotionService().savePayoutStrutureBaseUnitInCM( (ThrowdownPromotion)promotion, baseUnit, CmsUtil.getLocale( localecode ) );
        }

        /*
         * if(promotion.isThrowdownPromotion() && !divisionNames.isEmpty()) { Iterator<Division>
         * divsIter = divisionNames.iterator(); while(divsIter.hasNext()) { Division division =
         * divsIter.next(); promotion =
         * getPromotionService().saveDivisionNamesInCM((ThrowdownPromotion)promotion, division,
         * CmsUtil.getLocale( localecode )); } }
         */
      }
      List localeItems = getCMAssetService().getSupportedLocales( true );
      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
        {
          Content content = (Content)iter.next();
          if ( content != null )
          {
            String localeCode = (String)content.getContentDataMap().get( "CODE" );
            Locale locale = CmsUtil.getLocale( localeCode );
            List<AbstractGoalLevel> goalLevelsFromAdmin = new ArrayList<AbstractGoalLevel>();
            for ( Iterator translationPayoutiter = promoTranslationsForm.getTranslationsGoalNameAndDescriptionList().iterator(); translationPayoutiter.hasNext(); )
            {
              PromotionTranslationGoalAndPayoutFormBean goalLevelFormBean = (PromotionTranslationGoalAndPayoutFormBean)translationPayoutiter.next();
              if ( localeCode.equals( goalLevelFormBean.getLocaleCode() ) )
              {
                if ( StringUtils.isNotBlank( goalLevelFormBean.getName() ) && StringUtils.isNotBlank( goalLevelFormBean.getDescription() ) )
                {
                  // goalLevel Obejct is containg Goal Name and Description.It does not contain any
                  // CM Keys.
                  // This is only for Saving Goal Name and Description in CM.In other places
                  // goalLevel will behave normal.
                  GoalLevel goalLevel = new GoalLevel();
                  goalLevel.setId( goalLevelFormBean.getGoalLevelId() );
                  goalLevel.setGoalLevelNameKey( goalLevelFormBean.getName() );
                  goalLevel.setGoalLevelDescriptionKey( goalLevelFormBean.getDescription() );
                  goalLevel.setGoalLevelcmAssetCode( goalLevelFormBean.getGoalLevelcmAssetCode() );
                  goalLevelsFromAdmin.add( goalLevel );
                }
              }
            }
            if ( !goalLevelsFromAdmin.isEmpty() && goalLevelsFromAdmin.size() > 0 )
            {
              promotion = (GoalQuestPromotion)getPromotionService().saveGoalNameAndDescriptionInCM( promotion, goalLevelsFromAdmin, CmsUtil.getLocale( localeCode ) );
            }
          }
        }
      }

      if ( promotion.isThrowdownPromotion() )
      {
        for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
        {
          Content content = (Content)iter.next();
          if ( content != null )
          {
            String localeCode = (String)content.getContentDataMap().get( "CODE" );
            for ( Iterator<PromotionTranslationDivisionFormBean> translationDivisionsIter = promoTranslationsForm.getTranslationsDivisionAsList().iterator(); translationDivisionsIter.hasNext(); )
            {
              PromotionTranslationDivisionFormBean divFormBean = translationDivisionsIter.next();
              if ( localeCode.equals( divFormBean.getLocaleCode() ) )
              {
                if ( StringUtils.isNotBlank( divFormBean.getName() ) )
                {
                  Division division = new Division();
                  division.setId( divFormBean.getDivisionId() );
                  division.setDivisionName( divFormBean.getName() );
                  division.setDivisionNameAssetCode( divFormBean.getDivisionNameCmAssetCode() );
                  promotion = (ThrowdownPromotion)getPromotionService().saveDivisionNamesInCM( (ThrowdownPromotion)promotion, division, CmsUtil.getLocale( localeCode ) );
                }
              }
            }
          }
        }
      }

      promotion = getPromotionService().savePromotion( promotion );
    }
    catch( UniqueConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }
    catch( ServiceErrorException e1 )
    {
      logger.error( e1.getMessage(), e1 );
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

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

  private Promotion getPromotion( Promotion promotion )
  {
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    if ( promotion.isGoalQuestPromotion() )
    {
      // TODO: Need to add hydrate for goallevels
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
      return getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );

    }
    else if ( promotion.isChallengePointPromotion() )
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
      return getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
    }
    else if ( promotion.isThrowdownPromotion() )
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
      return getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
    }
    else if ( promotion.isNominationPromotion() )
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
      return getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
    }
    else
    {
      return getPromotionService().getPromotionById( promotion.getId() );
    }
  }

}
