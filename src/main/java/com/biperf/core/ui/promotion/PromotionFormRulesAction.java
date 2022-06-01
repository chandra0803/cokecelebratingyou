/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionFormRulesAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PromotionFormRulesAction.
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
 * <td>crosenquest</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionFormRulesAction extends PromotionBaseDispatchAction
{
  /**
   * Prepare the display for creating PromotionElement validations.
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    Promotion promotion = null;

    List promotionValidations = new ArrayList();

    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      PromotionService promotionService = getPromotionService();

      if ( promotion.hasParent() )
      {
        if ( promotion.isProductClaimPromotion() )
        {
          ProductClaimPromotion pcPromoChild = (ProductClaimPromotion)promotion;
          ProductClaimPromotion pcPromoParent = pcPromoChild.getParentPromotion();

          // Uses parent promotion id to fetch CFSE Validation because child promo doesn't have
          // their own
          promotionValidations = promotionService.getAllPromotionClaimFormStepElementValidations( pcPromoParent.getId() );
        }
      }
      else
      {
        promotionValidations = promotionService.getAllPromotionClaimFormStepElementValidations( promotion.getId() );
      }
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

      PromotionService promotionService = getPromotionService();

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

      promotion = promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

      if ( promotion.hasParent() )
      {
        if ( promotion.isProductClaimPromotion() )
        {
          ProductClaimPromotion pcPromoChild = (ProductClaimPromotion)promotion;
          ProductClaimPromotion pcPromoParent = pcPromoChild.getParentPromotion();

          // Uses parent promotion id to fetch CFSE Validation because child promo doesn't have
          // their own
          promotionValidations = promotionService.getAllPromotionClaimFormStepElementValidations( pcPromoParent.getId() );
        }
      }
      else
      {
        promotionValidations = promotionService.getAllPromotionClaimFormStepElementValidations( promotionId );
      }
    }

    PromotionFormRulesForm promoElementValidationForm = (PromotionFormRulesForm)actionForm;

    promoElementValidationForm.load( promotion, promotionValidations );

    return mapping.findForward( forwardTo );
  }

  /**
   * Re-display for changing the validationType.
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward changeValidation( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Continue or exit without saving
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward continueOrExit( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    // ActionForward forward = mapping.findForward(ActionConstants.SUCCESS_FORWARD);
    Promotion promotion = getWizardPromotion( request );
    setPromotionInWizardManager( request, promotion );

    return getWizardNextPage( mapping, request, promotion );

    // return forward;
  }

  /**
   * Save the validations
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward save( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_EXIT_ATTRIBUTE );
    ActionMessages errors = new ActionMessages();

    PromotionFormRulesForm promotionElementValidationForm = (PromotionFormRulesForm)actionForm;

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
      if ( promotion == null )
      {
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    else
    {
      // TODO: review instantiation of promotion???
      promotion = new ProductClaimPromotion();
      Long promotionId = promotionElementValidationForm.getPromotionId();
      promotion.setId( promotionId );
    }

    List validationsToSave = promotionElementValidationForm.toDomainObjects();
    validationsToSave = getPromotionService().savePromotionClaimFormStepElementValidationList( validationsToSave );

    if ( isWizardMode( request ) )
    {
      setPromotionInWizardManager( request, promotion );

      forward = getWizardNextPage( mapping, request, promotion );
    }
    else
    {
      forward = saveAndExit( mapping, request, promotion );
    }

    return forward;
  }
}
