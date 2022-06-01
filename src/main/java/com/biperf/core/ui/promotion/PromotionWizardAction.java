
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionWizardUpdateAssociation;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionWizardAction extends PromotionBaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( PromotionTranslationsAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionWizardForm promoWizardForm = (PromotionWizardForm)form;

    Promotion promotion;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {

      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = getWizardPromotion( request );

      if ( promotion.isNominationPromotion() )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_WIZARD ) );
        NominationPromotion attachedPromotion = (NominationPromotion)getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
        ( (NominationPromotion)promotion ).setPromotionWizardOrder( attachedPromotion.getPromotionWizardOrder() );
      }

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    else
    {
      // Get the promotionId from the request and get the promotion
      Long promotionId;

      if ( RequestUtils.containsAttribute( request, "promotionId" ) )
      {
        promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
      }
      else
      {
        try
        {
          String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
          String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
          String password = ClientStatePasswordManager.getPassword();

          if ( cryptoPass != null && cryptoPass.equals( "1" ) )
          {
            password = ClientStatePasswordManager.getGlobalPassword();
          }
          // Deserialize the client state.
          Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
          try
          {
            String promotionIdString = (String)clientStateMap.get( "promotionId" );
            promotionId = new Long( promotionIdString );
          }
          catch( ClassCastException e )
          {
            promotionId = (Long)clientStateMap.get( "promotionId" );
          }
          if ( promotionId == null )
          {
            ActionMessages errors = new ActionMessages();
            errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "promotionId as part of clientState" ) );
            saveErrors( request, errors );
            return mapping.findForward( forwardTo );
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }
      }

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();

      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_WIZARD ) );

      promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    }
    promoWizardForm.load( promotion );

    return mapping.findForward( forwardTo );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionWizardForm promotionWizardForm = (PromotionWizardForm)form;
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
      String promotionId = promotionWizardForm.getPromotionId().toString();
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

    try
    {

      promotionWizardForm.toDomainObject( promotion );

      boolean validWizardOrder = promotionWizardForm.isValidWizardOrder( promotion );

      if ( !validWizardOrder )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.INVALID_SORT_ORDER", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "WIZARD" ) ) );
      }

      PromotionWizardUpdateAssociation wizardUpdateAssociation = new PromotionWizardUpdateAssociation( promotion );

      List updateAssociations = new ArrayList();

      updateAssociations.add( wizardUpdateAssociation );

      promotion = getPromotionService().savePromotion( new Long( promotionWizardForm.getPromotionId() ), updateAssociations );
    }
    catch( NumberFormatException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }
    catch( UniqueConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
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

  private Promotion getPromotion( Promotion promotion )
  {
    return getPromotionService().getPromotionById( promotion.getId() );
  }

  public ActionForward preparePreview( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    // -------------------------
    // Get the claimFormStepId
    // ------------------------
    PromotionWizardForm promotionWizardForm = (PromotionWizardForm)form;
    Long claimFormStepId = null;
    Long claimFormId = null;
    Long promotionId = null;
    String whyFlag = null;

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      whyFlag = request.getParameter( "why" );
      claimFormId = promotionWizardForm.getClaimFormId();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );

      ClaimForm claimForm = getClaimDefinitionService().getClaimFormByIdWithAssociations( claimFormId, associationRequestCollection );

      claimFormStepId = claimForm.getClaimFormSteps().get( 0 ).getId();

      if ( claimFormId == null || claimFormStepId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId and claimFormStepId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // -------------------------------------------------------------
    // Validate that this claim form step Id is ready to preview
    // -------------------------------------------------------------

    try
    {
      getClaimFormDefinitionService().preparePreviewClaimForm( claimFormStepId );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "why", whyFlag );
    clientStateParameterMap.put( "claimFormStepId", claimFormStepId );
    clientStateParameterMap.put( "claimFormId", claimFormId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, "preview", new String[] { queryString, "method=preparePreview" } );

  }

  private ClaimFormDefinitionService getClaimDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }
}
