/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/giftcode/GiftCodeLookupAction.java,v $ */

package com.biperf.core.ui.giftcode;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

/**
 * Action class for Event Form List operations.
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
 * <td>babu</td>
 * <td>Oct 25, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class GiftCodeLookupAction extends BaseDispatchAction
{
  /**
   * Display the Event
   *
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( "display" );
  }

  private ActionForward loadMerchOrderInfo( ActionMapping mapping, HttpServletRequest request, GiftCodeLookupForm gcForm, MerchOrder merchOrder ) throws ServiceErrorException
  {
    ActionMessages errors = new ActionMessages();
    if ( null == merchOrder )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "giftcode.lookup.NOT_FOUND" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }
    String cmAssetKey = "";
    if ( merchOrder.getClaim() == null && merchOrder.getPromoMerchProgramLevel() == null )
    {
      Long promotionId = getActivityService().getPromotionbyMerchOrderId( merchOrder.getId() );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
      GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
      Set<AbstractGoalLevel> goalLevels = promotion.getGoalLevels();
      for ( Iterator<AbstractGoalLevel> iter = goalLevels.iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        if ( goalLevel.getAward().equals( new BigDecimal( merchOrder.getPoints() ) ) )
        {
          cmAssetKey = goalLevel.getGoalLevelName();
        }
      }
    }

    else
    {
      cmAssetKey = merchOrder.getPromoMerchProgramLevel().getCmAssetKey();
    }
    gcForm.setLevelCMKey( cmAssetKey );
    gcForm.setIssuedDate( merchOrder.getAuditCreateInfo().getDateCreated() );
    gcForm.setReferenceNumber( merchOrder.getReferenceNumber() );
    gcForm.setGiftCode( merchOrder.getFullGiftCode() );
    gcForm.setMerchOrder( merchOrder );
    gcForm = loadPartipantInfo( gcForm, merchOrder );

    // we should only need to check OM if the flag is NOT already set to 'true' on
    // our side
    if ( !merchOrder.isRedeemed() )
    {
      try
      {
        GiftcodeStatusResponseValueObject status = getMerchLevelService().getGiftCodeStatusWebService( merchOrder.getFullGiftCode(),
                                                                                                       merchOrder.getPromoMerchProgramLevel() != null
                                                                                                           ? merchOrder.getPromoMerchProgramLevel().getProgramId()
                                                                                                           : null,
                                                                                                       merchOrder.getOrderNumber(),
                                                                                                       merchOrder.getReferenceNumber() );
        gcForm.setRedeemed( status.getBalanceAvailable() == 0 );
      }
      catch( ServiceErrorException e )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "giftcode.lookup.ERROR_RETRIEVING_STATUS" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_SEARCH );
      }
    }
    else
    {
      gcForm.setRedeemed( merchOrder.isRedeemed() );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward searchByGiftCode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    GiftCodeLookupForm gcForm = (GiftCodeLookupForm)form;

    MerchOrder merchOrder = getMerchOrderService().getMerchOrderByGiftCode( gcForm.getGiftCode() );

    return loadMerchOrderInfo( mapping, request, gcForm, merchOrder );
  }

  public ActionForward searchByReferenceNumber( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    GiftCodeLookupForm gcForm = (GiftCodeLookupForm)form;

    MerchOrder merchOrder = getMerchOrderService().getMerchOrderByReferenceNumber( gcForm.getReferenceNumber() );

    return loadMerchOrderInfo( mapping, request, gcForm, merchOrder );
  }

  private GiftCodeLookupForm loadPartipantInfo( GiftCodeLookupForm form, MerchOrder merchOrder )
  {
    // get participant info or non-pax info
    Participant pax = merchOrder.getParticipant();
    if ( pax != null )
    {
      form.setRecipientFirstName( pax.getFirstName() );
      form.setRecipientLastName( pax.getLastName() );
    }
    return form;
  }

  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
