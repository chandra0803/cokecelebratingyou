/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionAwardsUpdateAssociation;
import com.biperf.core.service.promotion.PromotionBadgeUpdateAssociation;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.PromotionBasicsBadgeFormBean;

/**
 * 
 * PromotionSSIAwardsAction.
 * 
 * @author chowdhur
 * @since Oct 22, 2014
 */
public class PromotionSSIAwardsAction extends PromotionBaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PromotionBasicsAction.class );

  private static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

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

    PromotionSSIAwardsForm promoAwardsForm = (PromotionSSIAwardsForm)form;

    Promotion promotion = null;
    promoAwardsForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      Long promotionId = promoAwardsForm.getPromotionId();

      if ( promotionId != null )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
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

    request.setAttribute( "activeCountryListCount", String.valueOf( promoAwardsForm.getActiveCountryList().size() ) );

    ActionMessages errors = new ActionMessages();
    if ( null != promotion && promotion.isSSIPromotion() )
    {
      SSIPromotion ssiPromotion = (SSIPromotion)promotion;
      /*
       * if ( ssiPromotion.getAllowAwardMerchandise() ) { createMerchLevels( promoAwardsForm, errors
       * ); }
       */
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    return mapping.findForward( forwardTo );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    {
      ActionMessages errors = new ActionMessages();
      PromotionSSIAwardsForm ssiPromoAwardsForm = (PromotionSSIAwardsForm)form;
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
      if ( isWizardMode( request ) )
      {
        promotion = getWizardPromotion( request );
      }
      else
      {
        promotion = getPromotionService().getPromotionById( ssiPromoAwardsForm.getPromotionId() );
      }
      try
      {
        if ( promotion instanceof SSIPromotion )
        {
          SSIPromotion ssiPromotion = (SSIPromotion)promotion;
          boolean badgePromotionExists = false;
          Badge badgeReturned = ssiPromotion.getBadge();
          if ( badgeReturned != null )
          {
            badgePromotionExists = badgePromotionExists( ssiPromotion.getId() );
          }
          ssiPromoAwardsForm.toDomainObject( ssiPromotion );
          // merch level data
          /* Mercheandise option has been moved to SSI_Phase_2. This section is commented out */
          // ssiPromotion = savePromoMerchLevels( ssiPromoAwardsForm, ssiPromotion );

          // badge
          ssiPromotion = createBadge( ssiPromotion, ssiPromoAwardsForm );
          // WIZARD MODE
          if ( isWizardMode( request ) && !badgePromotionExists )
          {
            // save badge, badge rules and badge promotions
            getPromotionService().savePromotion( ssiPromotion );
            BadgePromotion badgePromo = new BadgePromotion();
            badgePromo.setBadgePromotion( ssiPromotion.getBadge() );
            badgePromo.setEligiblePromotion( ssiPromotion );
            getGamificationService().saveBadgePromotion( badgePromo );
          }
          else
          {
            // Existing promotion
            List updateAssociations = new ArrayList();
            PromotionAwardsUpdateAssociation promoAwardsUpdateAssociation = new PromotionAwardsUpdateAssociation( ssiPromotion );
            updateAssociations.add( promoAwardsUpdateAssociation );
            if ( ssiPromotion.getBadge() != null )
            {
              PromotionBadgeUpdateAssociation promotionBadgeUpdateAssociation = new PromotionBadgeUpdateAssociation( ssiPromotion );
              updateAssociations.add( promotionBadgeUpdateAssociation );
            }
            ssiPromotion = (SSIPromotion)getPromotionService().savePromotion( ssiPromotion.getId(), updateAssociations );

          }
        }
      }
      catch( ConstraintViolationException e )
      {
        logger.error( e.getMessage(), e );
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
      }
      catch( UniqueConstraintViolationException e )
      {
        logger.error( e.getMessage(), e );
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
      }
      catch( ServiceErrorExceptionWithRollback e )
      {
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
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
          if ( isSaveAndExit( request ) )
          {
            forward = saveAndExit( mapping, request, promotion );
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
      return forward;
    }
  }

  // => get promotion => save promo merch country => populate promo merch levels
  public ActionForward saveAndPopulateMerchLevels( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    String forwardTo = "lookupMerchLevels";
    PromotionSSIAwardsForm promoAwardsForm = (PromotionSSIAwardsForm)form;
    ActionMessages errors = new ActionMessages();
    ActionMessages warnings = new ActionMessages();
    try
    {
      Promotion promotion = null;
      if ( isWizardMode( request ) )
      {
        promotion = getWizardPromotion( request );
      }
      else
      {
        Long promotionId = promoAwardsForm.getPromotionId();

        if ( promotionId != null )
        {
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
          promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
        }
        else
        {
          throw new IllegalArgumentException( "promotionId is null" );
        }
      }

      promotion = promoAwardsForm.toDomainObject( promotion );
      promoAwardsForm.buildSSIDomainPromoMerchCountrySet( promotion );

      if ( errors.isEmpty() )
      {
        boolean setPromotionUnderConstruction = false;
        try
        {
          if ( !getPromoMerchCountryService().validateAndDeleteInvalidProgramLevels( promotion, false ) )
          {
            setPromotionUnderConstruction = true;
          }
          else if ( StringUtils.equalsIgnoreCase( promoAwardsForm.getPromotionStatus(), PromotionStatusType.COMPLETE )
              || StringUtils.equalsIgnoreCase( promoAwardsForm.getPromotionStatus(), PromotionStatusType.LIVE ) )
          {
            for ( Iterator promoMerchCountryIter = promotion.getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
            {
              PromoMerchCountry currentPromoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
              if ( currentPromoMerchCountry.getId() == null || currentPromoMerchCountry.getId().longValue() == 0 )
              {
                setPromotionUnderConstruction = true;
                break;
              }
            }
          }
          if ( setPromotionUnderConstruction )
          {
            String messageKey = "promotion.errors.MERCHCOUNTRY_PROGRAMID_CHANGED";
            warnings.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( messageKey ) );
            saveWarningMessages( request, warnings );
          }
        }
        catch( ServiceErrorException see )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_FORWARD );
        }

        List updateAssociations = new ArrayList();
        PromotionAwardsUpdateAssociation promoAwardsUpdateAssociation = new PromotionAwardsUpdateAssociation( promotion );
        updateAssociations.add( promoAwardsUpdateAssociation );
        promotion = getPromotionService().savePromotion( new Long( promoAwardsForm.getPromotionId() ), updateAssociations );
        if ( setPromotionUnderConstruction )
        {
          promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
          getPromotionService().savePromotion( promotion );
        }

      }
      List activeCountryList = promoAwardsForm.getActiveCountryList();
      validateMerchandisePrograms( errors, activeCountryList );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    return mapping.findForward( forwardTo );
  }

  private void validateMerchandisePrograms( ActionMessages errors, List programs ) throws ServiceErrorException
  {
    if ( programs == null || programs.isEmpty() )
    {
      return;
    }
    boolean isError = false;
    StringBuffer errorProgramNumbers = new StringBuffer();
    for ( int i = 0; i < programs.size(); i++ )
    {
      PromoMerchCountryFormBean merchCountry = (PromoMerchCountryFormBean)programs.get( i );
      if ( merchCountry != null && merchCountry.getProgramId() != null && merchCountry.getProgramId().trim().length() > 0 )
      {
        boolean valid = getAwardBanQService().isValidGiftCodeProgram( merchCountry.getProgramId() );
        if ( !valid )
        {
          isError = true;
          // Even first program ID will have ',' in front of it, which we will
          // remove before adding it to error.
          errorProgramNumbers.append( ", " + merchCountry.getProgramId() );
        }
      }
    }
    if ( isError )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.PROGRAM_NUMBER_INVALID", errorProgramNumbers.toString().substring( 1 ) ) );
    }
  }

  private void createMerchLevels( PromotionSSIAwardsForm promoAwardsForm, ActionMessages errors )
  {
    try
    {
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

  private void loadMerchLevels( List countryList, PromotionSSIAwardsForm promoAwardsForm ) throws ServiceErrorException
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
  }

  private SSIPromotion createBadge( SSIPromotion ssiPromotion, PromotionSSIAwardsForm promotionSSIAwardsForm ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    Badge badgeReturned = ssiPromotion.getBadge();
    if ( badgeReturned == null )
    {
      Badge badge = new Badge();
      badge.setName( ssiPromotion.getName() + "Badge" );
      badge.setBadgeType( BadgeType.lookup( BadgeType.EARNED_OR_NOT_EARNED ) );
      badge.setPromotionType( PromotionType.lookup( PromotionType.BADGE ) );
      badge = (Badge)getPromotionService().savePromoNameCmText( badge, ssiPromotion.getName() + " Badge" );
      badge.setDisplayEndDays( new Long( 0 ) );
      badge.setTileHighlightPeriod( new Long( 0 ) );
      badge.setStatus( Badge.BADGE_ACTIVE );
      badge.setPromotionStatus( generatePromotionStatus( ssiPromotion.getSubmissionStartDate() ) );
      badge.setSubmissionStartDate( ssiPromotion.getSubmissionStartDate() );
      badge.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
      badge.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );

      badgeReturned = (Badge)getPromotionService().savePromotion( badge );
    }

    List<BadgeRule> badgeRulesList = createBadgeRules( badgeReturned, promotionSSIAwardsForm, ssiPromotion );
    badgeReturned.setBadgeRules( new HashSet<BadgeRule>( badgeRulesList ) );
    ssiPromotion.setBadge( badgeReturned );

    return ssiPromotion;
  }

  public PromotionStatusType generatePromotionStatus( Date badgeStartDate )
  {
    Date currentDate = new Date();
    // if the submission start date is has passed, automatically set the promotion to live
    if ( badgeStartDate.before( currentDate ) )
    {
      return PromotionStatusType.lookup( PromotionStatusType.LIVE );
    }
    else
    {
      return PromotionStatusType.lookup( PromotionStatusType.COMPLETE );
    }
  }

  private List<BadgeRule> createBadgeRules( Badge badgeReturned, PromotionSSIAwardsForm promotionSSIAwardsForm, SSIPromotion ssiPromotion ) throws ServiceErrorException
  {
    List<PromotionBasicsBadgeFormBean> promotionBasicsBadgeFormBeanList = promotionSSIAwardsForm.getPromotionSSIBadgeFormBeanList();
    List<BadgeRule> badgeRulesFinalList = new ArrayList<BadgeRule>();
    for ( PromotionBasicsBadgeFormBean displayBean : promotionBasicsBadgeFormBeanList )
    {
      if ( displayBean.isSelected() )
      {
        BadgeRule badgeRule = new BadgeRule();
        if ( displayBean.getBadgeRuleId() != null && !displayBean.getBadgeRuleId().equals( 0L ) )
        {
          badgeRule.setId( displayBean.getBadgeRuleId() );
        }
        String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRule.getBadgeName(), displayBean.getBadgeName() );
        badgeRule.setBadgePromotion( badgeReturned );
        badgeRule.setMaximumQualifier( 0L );
        badgeRule.setMinimumQualifier( 0L );
        badgeRule.setBadgeLibraryCMKey( displayBean.getCmAssetKey() );
        badgeRule.setBadgeName( badgeNameCmAsset );
        badgeRulesFinalList.add( badgeRule );
      }
    }
    return badgeRulesFinalList;
  }

  private boolean badgePromotionExists( Long promotionId )
  {
    return getPromotionService().getBadgePromotionCountForPromoId( promotionId ).intValue() > 0;
  }

  /**
   * Get the GamificationService from the beanFactory locator.
   * 
   * @return GamificationService
   */
  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  protected static PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  protected static MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

}
