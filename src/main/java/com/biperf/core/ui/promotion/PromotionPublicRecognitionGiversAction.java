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
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;

public class PromotionPublicRecognitionGiversAction extends PromotionBaseDispatchAction
{
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * This is the display method struts actually calls. 
   * It'll grab the promotion and then delegate off to split out methods depending on promotion type. 
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionPublicRecognitionGiversForm giversForm = (PromotionPublicRecognitionGiversForm)form;

    // clearing the list to get fresh list for each budget segment change
    if ( giversForm.getGiverList() != null && giversForm.getGiverList().size() > 0 )
    {
      giversForm.getGiverList().clear();
    }

    Promotion promotion = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {

      promotion = getWizardPromotion( request );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_PUBLIC_REC_BUDGET_MASTER ) );
      Promotion attachedPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
      setPublicRecogBudgetMaster( promotion, getPublicRecogBudgetMaster( attachedPromotion ) );
    }
    else
    {
      Long promotionId = giversForm.getPromotionId();

      if ( promotionId != null )
      {

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_PUBLIC_REC_BUDGET_MASTER ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }

    // Split off by promotion type to avoid lots of these if-statements
    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
      forwardTo = displayForRecognition( request, recPromo, giversForm );
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;
      forwardTo = displayForNomination( request, nomPromo, giversForm );
    }

    return mapping.findForward( forwardTo );
  }

  /** Method to take care of recognition promotion - rather than putting if-statements everywhere */
  public String displayForRecognition( HttpServletRequest request, RecognitionPromotion recPromo, PromotionPublicRecognitionGiversForm giversForm )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    giversForm.loadBudgetSegmentList( recPromo );

    if ( giversForm.getBudgetSegmentId() == null )
    {
      giversForm.setBudgetSegmentId( giversForm.getBudgetSegmentList().iterator().next().getId() );
    }

    if ( BudgetType.NODE_BUDGET_TYPE.equals( recPromo.getPublicRecogBudgetMaster().getBudgetType().getCode() ) )
    {
      giversForm.load( recPromo, getPromotionService().getNodeWithoutPublicRecogBudgetAllocationWithinPromotion( recPromo.getId(), giversForm.getBudgetSegmentId() ), true );
    }
    else
    {
      giversForm.load( recPromo, getPromotionService().getParticipantsWithoutPublicRecogBudgetAllocationWithinPromotion( recPromo.getId(), giversForm.getBudgetSegmentId() ), false );
    }

    request.setAttribute( "budgetSegmentList", giversForm.getBudgetSegmentList() );

    return forwardTo;
  }

  public String displayForNomination( HttpServletRequest request, NominationPromotion nomPromo, PromotionPublicRecognitionGiversForm giversForm )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    giversForm.loadBudgetSegmentList( nomPromo );

    if ( giversForm.getBudgetSegmentId() == null )
    {
      giversForm.setBudgetSegmentId( giversForm.getBudgetSegmentList().iterator().next().getId() );
    }

    if ( BudgetType.NODE_BUDGET_TYPE.equals( nomPromo.getPublicRecogBudgetMaster().getBudgetType().getCode() ) )
    {
      giversForm.load( nomPromo, getPromotionService().getNodeWithoutPublicRecogBudgetAllocationWithinPromotion( nomPromo.getId(), giversForm.getBudgetSegmentId() ), true );
    }
    else
    {
      giversForm.load( nomPromo, getPromotionService().getParticipantsWithoutPublicRecogBudgetAllocationWithinPromotion( nomPromo.getId(), giversForm.getBudgetSegmentId() ), false );
    }

    request.setAttribute( "budgetSegmentList", giversForm.getBudgetSegmentList() );

    return forwardTo;
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

    PromotionPublicRecognitionGiversForm giversForm = (PromotionPublicRecognitionGiversForm)form;

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
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_PUBLIC_REC_BUDGET_MASTER ) );

      promotion = getPromotionService().getPromotionByIdWithAssociations( giversForm.getPromotionId(), associationRequestCollection );
    }

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;

      if ( giversForm.getGiverList() != null && !giversForm.getGiverList().isEmpty() )
      { // if nothing was changed, no reason to attempt a save
        recPromo = giversForm.toDomainObject( recPromo );

        try
        {
          BudgetMaster budgetMaster = recPromo.getPublicRecogBudgetMaster();
          budgetMaster = ( (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME ) ).saveBudgetMaster( budgetMaster );
          recPromo.setPublicRecogBudgetMaster( budgetMaster );
        }
        catch( ServiceErrorException e )
        {
          ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
        }
      }
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;

      if ( giversForm.getGiverList() != null && !giversForm.getGiverList().isEmpty() )
      { // if nothing was changed, no reason to attempt a save
        nomPromo = giversForm.toDomainObject( nomPromo );

        try
        {
          BudgetMaster budgetMaster = nomPromo.getPublicRecogBudgetMaster();
          budgetMaster = ( (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME ) ).saveBudgetMaster( budgetMaster );
          nomPromo.setPublicRecogBudgetMaster( budgetMaster );
        }
        catch( ServiceErrorException e )
        {
          ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
        }
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
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }
    return forward;
  }

  /** Helper method to get public recognition budget master based on promotion type */
  private BudgetMaster getPublicRecogBudgetMaster( Promotion promotion )
  {
    if ( promotion.isRecognitionPromotion() )
    {
      return ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster();
    }
    else if ( promotion.isNominationPromotion() )
    {
      return ( (NominationPromotion)promotion ).getPublicRecogBudgetMaster();
    }
    else
    {
      throw new RuntimeException( "Unable to get public recognition budget master for given promotion type" );
    }
  }

  /** Helper method to set the budget master object to a promotion based on type */
  private void setPublicRecogBudgetMaster( Promotion promotion, BudgetMaster publicRecogBudgetMaster )
  {
    if ( promotion.isRecognitionPromotion() )
    {
      ( (RecognitionPromotion)promotion ).setPublicRecogBudgetMaster( publicRecogBudgetMaster );
    }
    else if ( promotion.isNominationPromotion() )
    {
      ( (NominationPromotion)promotion ).setPublicRecogBudgetMaster( publicRecogBudgetMaster );
    }
    else
    {
      throw new RuntimeException( "Unable to set public recognition budget master for given promotion type" );
    }
  }

}
