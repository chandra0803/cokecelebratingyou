/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionSweepstakesAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionAwardsUpdateAssociation;
import com.biperf.core.service.promotion.PromotionSweepstakeCriteriaUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * PromotionSweepstakesAction.
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
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionSweepstakesAction extends PromotionBaseDispatchAction
{
  private static final String SWEEPS_FORWARD = "sweeps_list";

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

    Promotion promotion = null;

    PromotionSweepstakesForm sweepsForm = (PromotionSweepstakesForm)form;
    String pageMode = (String)request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE );

    if ( pageMode != null && pageMode.equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SWEEP_PROMO_BILLCODES ) );

      Promotion attachedPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
      promotion.setPromotionSweepstakes( attachedPromotion.getPromotionSweepstakes() );
      if ( promotion.isAbstractRecognitionPromotion() )
      {
        promotion.setPromoMerchCountries( attachedPromotion.getPromoMerchCountries() );
      }

      // Skip forward to the next step when the award type is not points
      if ( promotion != null && promotion.isNominationPromotion() && promotion.getAwardType() != null && !promotion.getAwardType().isPointsAwardType() )
      {
        forwardTo = ActionConstants.WIZARD_NEXT_FORWARD;
      }
    }
    else
    {
      if ( sweepsForm.getPromotionId() != null && sweepsForm.getPromotionId().length() > 0 )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();

        /* Bug # 34020 - to speed up the screen. this association request is unnecessary */
        // promoAssociationRequestCollection
        // .add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES
        // ) );
        /* Bug # 34020 end */
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SWEEP_PROMO_BILLCODES ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( sweepsForm.getPromotionId() ), promoAssociationRequestCollection );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }
    sweepsForm.load( promotion );

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
    ActionForward forward = null;
    ActionMessages errors = new ActionMessages();
    String pageMode = (String)request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE );

    PromotionSweepstakesForm sweepsForm = (PromotionSweepstakesForm)form;

    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else if ( pageMode.equals( ViewAttributeNames.SWEEPS_MODE ) )
      {
        request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.NORMAL_MODE );
        forward = mapping.findForward( SWEEPS_FORWARD );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }

      return forward;
    }

    // WIZARD MODE
    if ( pageMode.equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      promotion = sweepsForm.toDomainObject( promotion );
    }
    else
    {
      /* Bug # 34020 start - to speed up the screen. this association request is unnecessary */
      // AssociationRequestCollection promoAssociationRequestCollection = new
      // AssociationRequestCollection();

      // promoAssociationRequestCollection
      // .add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES )
      // );

      // promotion = getPromotionService()
      // .getPromotionByIdWithAssociations( new Long( sweepsForm.getPromotionId() ),
      // promoAssociationRequestCollection );

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SWEEP_PROMO_BILLCODES ) );
      promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( sweepsForm.getPromotionId() ), associationRequestCollection );
      /* Bug # 34020 end */

      // update promotion with form data
      promotion = sweepsForm.toDomainObject( promotion );
    }

    List updateAssociations = new ArrayList();

    PromotionSweepstakeCriteriaUpdateAssociation promotionSweepstakeUpdateAssociation = new PromotionSweepstakeCriteriaUpdateAssociation( promotion );

    updateAssociations.add( promotionSweepstakeUpdateAssociation );

    // if Awardlevel promotion, must update the Promotion Object
    if ( sweepsForm.isAwardLevelPromotion() )
    {
      updateAssociations.add( new PromotionAwardsUpdateAssociation( promotion ) );
    }

    try
    {
      promotion = getPromotionService().savePromotion( new Long( sweepsForm.getPromotionId() ), updateAssociations );
      // promotion = getPromotionService().savePromotion( promotion );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
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
        else if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          forward = mapping.findForward( "saveAndContinueApprovals" );
        }
        else
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
      }
      else if ( pageMode.equals( ViewAttributeNames.SWEEPS_MODE ) )
      {
        request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.NORMAL_MODE );
        forward = mapping.findForward( SWEEPS_FORWARD );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  /**
   * cancel action
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String pageMode = (String)request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE );
    if ( pageMode.equals( ViewAttributeNames.SWEEPS_MODE ) )
    {
      request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.NORMAL_MODE );
      return mapping.findForward( SWEEPS_FORWARD );
    }

    PromotionSweepstakesForm sweepsForm = (PromotionSweepstakesForm)form;

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", sweepsForm.getPromotionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String method = "method=display";
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, method } );
  }

  /**
   * Back to Awards Action
   * 
   * @param request
   * @param mapping
   * @param actionForm
   * @param response
   * @return ActionForward
   */
  public ActionForward backToAwards( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.WIZARD_BACK_TO_AWARDS );
  }
}
