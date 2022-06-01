/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPartnerPayoutAction.java,v $ */

package com.biperf.core.ui.promotion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionPartnerUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * PromotionPartnerPayoutAction.
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
 * <td>reddy</td>
 * <td>Feb 25, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionPartnerPayoutAction extends PromotionBaseDispatchAction
{
  public static final String SESSION_PROMO_GOAL_PAYOUT_FORM = "sessionPromoGoalPayoutForm";
  public static final String SEARCH_TYPE_PARAM = "searchType";
  public static final String PAGE_TYPE_PARAM = "pageType";

  public static final String ALTERNATE_RETURN_URL = "alternateReturnUrl";

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

    PromotionGoalPartnerPayoutForm promopartnerPayoutForm = (PromotionGoalPartnerPayoutForm)form;

    GoalQuestPromotion promotion;

    // WIZARD MODE
    if ( ViewAttributeNames.WIZARD_MODE.equals( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = (GoalQuestPromotion)getWizardPromotion( request );

      if ( promotion != null )
      {
        // If the promotion has been saved and the sure is coming back, then we need to
        // initialize the lazy payout fields
        // TODO review to see if this should only be done for child promos.
        GoalQuestPromotion attachedPromotion = (GoalQuestPromotion)getPromotion( promotion.getId() );

        updateWizardPromotion( promotion, attachedPromotion );
        /*
         * promotion.setPromotionPayoutGroups( attachedPromotion.getPromotionPayoutGroups() ); if (
         * promotion.getParentPromotion() != null ) { promotion.setParentPromotion(
         * attachedPromotion.getParentPromotion() ); }
         */
        promopartnerPayoutForm.loadPromotion( promotion );
      }

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    // NORMAL MODE
    else
    {
      Long promotionId = promopartnerPayoutForm.getPromotionId();
      promotion = (GoalQuestPromotion)getPromotion( promotionId );
      promopartnerPayoutForm.loadPromotion( promotion );
    }

    promopartnerPayoutForm.setMethod( "save" );

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
     * Save
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionGoalPartnerPayoutForm promoPayoutForm = (PromotionGoalPartnerPayoutForm)form;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        String alternateReturnUrl = RequestUtils.getOptionalParamString( request, ALTERNATE_RETURN_URL );
        if ( !StringUtils.isBlank( alternateReturnUrl ) )
        {
          response.sendRedirect( alternateReturnUrl );
          return null;
        }
        if ( promoPayoutForm.getPromotionId() != null )
        {
          Map clientStateParameterMap = new HashMap();
          clientStateParameterMap.put( "promotionId", promoPayoutForm.getPromotionId() );
          String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
          String method = "method=display";
          forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, method } );
        }
        else
        {
          forward = mapping.findForward( ActionConstants.WIZARD_CANCEL_FORWARD );
        }
      }

      return forward;
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL ) );

    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promoPayoutForm.getPromotionId(), associationRequestCollection );

    Long promotionId = promoPayoutForm.getPromotionId();
    promotion = (GoalQuestPromotion)promoPayoutForm.toDomainObject();

    PromotionPartnerUpdateAssociation promotionPartnerUpdateAssociation = new PromotionPartnerUpdateAssociation( promotion );

    List updateAssociations = new ArrayList();
    updateAssociations.add( promotionPartnerUpdateAssociation );

    try
    {
      promotion = (GoalQuestPromotion)getPromotionService().savePromotion( promotionId, updateAssociations );
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

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        String alternateReturnUrl = RequestUtils.getOptionalParamString( request, ALTERNATE_RETURN_URL );
        if ( !StringUtils.isBlank( alternateReturnUrl ) )
        {
          response.sendRedirect( alternateReturnUrl );
          return null;
        }
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  private Promotion getPromotion( Long promotionId )
  {
    Promotion promotion = getPromotionService().getPromotionById( promotionId );
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    if ( promotion.isGoalQuestPromotion() )
    {
      // TODO: Need to add hydrate for goallevels
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
      return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    }
    else
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
      return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    }

  }

  private void updateWizardPromotion( GoalQuestPromotion wizardPromotion, GoalQuestPromotion attachedPromotion )
  {
    wizardPromotion.setGoalLevels( attachedPromotion.getGoalLevels() );
    wizardPromotion.setPartnerGoalLevels( attachedPromotion.getPartnerGoalLevels() );
  }
}
