
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionBillCodeUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;

public class PromotionBillCodesAction extends PromotionBaseDispatchAction
{
  /** Log */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  private static final String SESSION_BILL_CODE_FORM = "SESSION_BILL_CODE_FORM";

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
    PromotionBillCodesForm promotionBillCodesForm = (PromotionBillCodesForm)form;

    Promotion promotion = null;
    List promotionBillCodes = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      // Skip forward to the next step when the award type is 'Other'
      if ( promotion != null && promotion.getAwardType() != null && promotion.getAwardType().isOtherAwardType() )
      {
        forwardTo = ActionConstants.WIZARD_NEXT_FORWARD;
      }
    }
    // NORMAL MODE
    else
    {
      String promotionId = promotionBillCodesForm.getPromotionId().toString();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NONSWEEP_PROMO_BILLCODES ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), arCollection );
      }
    }
    if ( promotion != null )
    {
      promotionBillCodesForm.load( promotion );
    }
    request.getSession().setAttribute( SESSION_BILL_CODE_FORM, promotionBillCodesForm );

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

    PromotionBillCodesForm promotionBillCodesForm = (PromotionBillCodesForm)form;
    ActionMessages errors = new ActionMessages();

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
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NONSWEEP_PROMO_BILLCODES ) );
      promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promotionBillCodesForm.getPromotionId() ), associationRequestCollection );

    }
    try
    {
      promotion = promotionBillCodesForm.toDomainObject( promotion );

      PromotionBillCodeUpdateAssociation billCodeUpdateAssociation = new PromotionBillCodeUpdateAssociation( promotion );

      List updateAssociations = new ArrayList();

      updateAssociations.add( billCodeUpdateAssociation );

      promotion = getPromotionService().savePromotion( new Long( promotionBillCodesForm.getPromotionId() ), updateAssociations );

    }

    catch( Exception e )
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

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

}
