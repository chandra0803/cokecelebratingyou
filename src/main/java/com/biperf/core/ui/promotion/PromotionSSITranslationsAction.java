
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
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
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsUtil;

public class PromotionSSITranslationsAction extends PromotionBaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PromotionSSITranslationsAction.class );

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

    PromotionSSITranslationsForm promoTransForm = (PromotionSSITranslationsForm)form;

    Promotion promotion = null;
    promoTransForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      Long promotionId = promoTransForm.getPromotionId();

      if ( promotionId != null )
      {
        promotion = getPromotionService().getPromotionById( promotionId );
        /* merchandise option moved to SSI_Phase_2 */
        // AssociationRequestCollection associationRequestCollection = new
        // AssociationRequestCollection();
        // associationRequestCollection.add( new PromotionAssociationRequest(
        // PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
        // promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId,
        // associationRequestCollection );

      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }

    if ( promotion != null )
    {
      promoTransForm.load( promotion );
    }

    // request.setAttribute( "activeCountryListCount", String.valueOf(
    // promoTransForm.getActiveCountryList().size() ) );

    ActionMessages errors = new ActionMessages();
    /*
     * if ( null != promotion && promotion.isSSIPromotion() ) { SSIPromotion ssiPromotion =
     * (SSIPromotion)promotion; if ( ssiPromotion.getAllowAwardMerchandise() ) { //
     * createMerchLevels( promoTransForm, errors ); } }
     */

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

      PromotionSSITranslationsForm ssiPromoTranslationsForm = (PromotionSSITranslationsForm)form;

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
        promotion = getPromotionService().getPromotionById( ssiPromoTranslationsForm.getPromotionId() );
      }
      try
      {
        if ( promotion instanceof SSIPromotion )
        {
          saveBadgeText( ssiPromoTranslationsForm.getTranslations() );
          /* merchandise option moved to SSI_Phase_2 */
          // saveLevelText( ssiPromoTranslationsForm.getLevelTranslations() );
        }
      }
      catch( ConstraintViolationException e )
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

  public void saveBadgeText( List<PromotionSSITranslationsFormBean> translations ) throws ServiceErrorException
  {

    try
    {
      for ( PromotionSSITranslationsFormBean transBean : translations )
      {
        for ( PromotionSSITranslationsDetailBean badge : transBean.getDetails() )
        {
          if ( !StringUtils.isEmpty( badge.getName() ) )
          {
            Locale locale = CmsUtil.getLocale( badge.getLocaleCode() );
            getGamificationService().saveRulesCmText( transBean.getTranslationCMKey(), badge.getName(), locale );
          }
        }
      }
    }
    catch( ServiceErrorException e )
    {
      List<ServiceError> errors = new ArrayList<ServiceError>();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }
  }

  public GamificationService getGamificationService()
  {
    return (GamificationService)BeanLocator.getBean( GamificationService.BEAN_NAME );
  }

  public PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)BeanLocator.getBean( PromoMerchCountryService.BEAN_NAME );
  }

}
