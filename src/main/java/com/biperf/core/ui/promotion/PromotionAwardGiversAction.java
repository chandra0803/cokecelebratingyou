/**
 * 
 */

package com.biperf.core.ui.promotion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;

/**
 * PromotionAwardGiversAction.
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
 * <td>jenniget</td>
 * <td>Oct 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAwardGiversAction extends PromotionBaseDispatchAction
{
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

    PromotionAwardGiversForm giversForm = (PromotionAwardGiversForm)form;

    if ( giversForm.getGiverList() != null && giversForm.getGiverList().size() > 0 )
    {
      giversForm.getGiverList().clear();
    }

    RecognitionPromotion promotion = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {

      promotion = (RecognitionPromotion)getWizardPromotion( request );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
      RecognitionPromotion attachedPromotion = (RecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
      promotion.setBudgetMaster( attachedPromotion.getBudgetMaster() );
    }
    else
    {
      Long promotionId = giversForm.getPromotionId();

      if ( promotionId != null )
      {

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
        promotion = (RecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }
    giversForm.loadBudgetSegmentList( promotion );

    if ( giversForm.getBudgetSegmentId() == null && giversForm.getBudgetSegmentList() != null && !giversForm.getBudgetSegmentList().isEmpty() )
    {
      giversForm.setBudgetSegmentId( giversForm.getBudgetSegmentList().iterator().next().getId() );
    }

    if ( giversForm.getBudgetSegmentId() != null )
    {
      if ( promotion.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
      {
        giversForm.load( promotion, getPromotionService().getNodesWithoutBudgetAllocationWithinPromotion( promotion.getId(), giversForm.getBudgetSegmentId() ), true );
      }
      else
      {
        giversForm.load( promotion, getPromotionService().getParticipantsWithoutBudgetAllocationWithinPromotion( promotion.getId(), giversForm.getBudgetSegmentId() ), false );
      }
    }
    else
    {
      giversForm.load( promotion );
    }

    request.setAttribute( "budgetSegmentList", giversForm.getBudgetSegmentList() );

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

    PromotionAwardGiversForm giversForm = (PromotionAwardGiversForm)form;

    RecognitionPromotion promotion = null;

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
      promotion = (RecognitionPromotion)getWizardPromotion( request );
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );

      promotion = (RecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( giversForm.getPromotionId(), associationRequestCollection );
    }

    if ( giversForm.getGiverList() != null && !giversForm.getGiverList().isEmpty() )
    { // if nothing was changed, no reason to attempt a save
      promotion = giversForm.toDomainObject( promotion );

      try
      {
        BudgetMaster budgetMaster = promotion.getBudgetMaster();
        budgetMaster = ( (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME ) ).saveBudgetMaster( budgetMaster );
        promotion.setBudgetMaster( budgetMaster );
      }
      catch( ServiceErrorException e )
      {
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
      }
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
        if ( forward.getName().equals( ActionConstants.WIZARD_SAVE_AND_CONTINUE_FORWARD ) && promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          forward = mapping.findForward( "saveAndContinueSweepstake" );
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
