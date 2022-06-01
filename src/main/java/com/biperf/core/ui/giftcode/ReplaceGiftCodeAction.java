/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/giftcode/ReplaceGiftCodeAction.java,v $
 */

package com.biperf.core.ui.giftcode;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.activity.GoalQuestPayoutActivity;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.DateUtils;

/**
 * ReplaceGiftCodeAction. Used initially only for GoalQuest gift codes (stored in the JOURNAL
 * table), later also for merch order gift codes (stored in the MERCH_ORDER table). Gift codes are
 * unique across journals and merch orders, and this class searches both areas to find gift codes
 * entered by the end user.
 */
public class ReplaceGiftCodeAction extends BaseDispatchAction
{
  /**
   * Find gift code
   */
  public ActionForward findGiftCode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ReplaceGiftCodeForm replaceGiftCodeForm = (ReplaceGiftCodeForm)form;
    String giftCode = replaceGiftCodeForm.getOldGiftCode();
    replaceGiftCodeForm.setOldGiftCodeCopy( giftCode );

    // search within merch orders
    MerchOrder merchOrder = searchMerchOrderGiftCode( giftCode );
    if ( merchOrder != null )
    {
      return foundMerchOrderGiftCode( merchOrder, giftCode, mapping, replaceGiftCodeForm, request );
    }

    // not found
    return invalidGiftCode( mapping, request );
  }

  /*
   * Send merch order gift code info to jsp.
   */
  private ActionForward foundMerchOrderGiftCode( MerchOrder merchOrder, String giftCode, ActionMapping mapping, ReplaceGiftCodeForm form, HttpServletRequest request )
  {

    form.setDetailsAvailable( true );
    form.setParticipantName( getParticipantName( merchOrder ) );
    form.setGiftCodeIssueDate( DateUtils.toDisplayString( getGiftCodeIssueDate( merchOrder ) ) );
    form.setEmailAddress( getParticipantEmailAddress( merchOrder ) );

    if ( merchOrder.getPromoMerchProgramLevel() != null && merchOrder.getClaim() != null )
    {
      Promotion promo = merchOrder.getPromoMerchProgramLevel().getPromoMerchCountry().getPromotion();
      form.setPromotionName( promo.getName() );
    }
    else
    {
      form.setPromotionName( getActivityService().getPromotionNamebyMerchOrderId( merchOrder.getId() ) );
    }
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  /**
   * Replace gift code
   */
  public ActionForward replaceGiftCode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ReplaceGiftCodeForm replaceGiftCodeForm = (ReplaceGiftCodeForm)form;
    String giftCode = replaceGiftCodeForm.getOldGiftCode();
    String emailAddress = replaceGiftCodeForm.getEmailAddress();
    String message = replaceGiftCodeForm.getMessage();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    // replace within merch order
    MerchOrder merchOrder = searchMerchOrderGiftCode( giftCode );
    if ( merchOrder != null )
    {
      return replaceMerchOrderGiftCode( merchOrder, giftCode, emailAddress, message, mapping, request );
    }

    // not found
    return invalidGiftCode( mapping, request );
  }

  /*
   * Replace merch order gift code.
   */
  private ActionForward replaceMerchOrderGiftCode( MerchOrder merchOrder, String giftCode, String emailAddress, String message, ActionMapping mapping, HttpServletRequest request )
  {
    String programId = null;
    if ( merchOrder.getPromoMerchProgramLevel() != null )
    {
      programId = merchOrder.getPromoMerchProgramLevel().getProgramId();
    }
    else
    {
      Promotion promotion = getPromotionService().getPromotionById( getActivityService().getPromotionbyMerchOrderId( merchOrder.getId() ) );
      if ( promotion instanceof GoalQuestPromotion )
      {
        programId = ( (GoalQuestPromotion)promotion ).getProgramId();
      }
    }

    try
    {
      getMerchOrderService().replaceGiftCodeAndSendEmail( merchOrder.getId(), programId, giftCode, emailAddress, message );
    }
    catch( ServiceErrorException see )
    {
      ActionMessages errors = new ActionMessages();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( see.getServiceErrors(), errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /*
   * Search for gift code within merch orders.
   */
  private MerchOrder searchMerchOrderGiftCode( String giftCode )
  {
    return getMerchOrderService().getMerchOrderByGiftCode( giftCode );
  }

  /*
   * Report invalid gift code.
   */
  private ActionForward invalidGiftCode( ActionMapping mapping, HttpServletRequest request )
  {
    ActionMessages errors = new ActionMessages();
    errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.goalquest.replacegiftcode.INVALID_GIFT_CODE" ) );
    saveErrors( request, errors );
    return mapping.findForward( ActionConstants.FAIL_FORWARD );
  }

  /**
   * Get pax name for a merch order.
   */
  private String getParticipantName( MerchOrder merchOrder )
  {
    String participantName = null;

    if ( merchOrder.getParticipant() != null )
    {
      participantName = getParticipantName( merchOrder.getParticipant() );
    }

    return participantName;
  }

  /**
   * Get pax name.
   */
  private String getParticipantName( Participant participant )
  {
    String participantName = null;

    if ( participant != null )
    {
      participantName = formatParticipantName( participant.getFirstName(), participant.getLastName() );
    }

    return participantName;
  }

  /**
   * Format pax name.
   */
  private String formatParticipantName( String firstName, String lastName )
  {
    StringBuffer participantName = new StringBuffer();

    if ( firstName != null )
    {
      participantName.append( firstName );
      participantName.append( ' ' );
    }

    if ( lastName != null )
    {
      participantName.append( lastName );
    }

    return participantName.toString();
  }

  /**
   * Get pax email address.
   */
  private String getParticipantEmailAddress( Participant pax )
  {
    String email = null;

    if ( pax != null )
    {
      AssociationRequestCollection assoc = new AssociationRequestCollection();
      assoc.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      Participant participant = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), assoc );

      if ( participant.getPrimaryEmailAddress() != null )
      {
        email = participant.getPrimaryEmailAddress().getEmailAddr();
      }
    }

    return email;
  }

  /**
   * Get pax email address for a merch order.
   */
  private String getParticipantEmailAddress( MerchOrder merchOrder )
  {
    String email = getParticipantEmailAddress( merchOrder.getParticipant() );

    return email;
  }

  /**
   * Get gift code issue date for a journal.
   */
  private Date getGiftCodeIssueDate( Journal journal )
  {
    Date giftCodeIssueDate = null;

    if ( journal.getActivityJournals() != null && journal.getActivityJournals().size() > 0 )
    {
      ActivityJournal activityJournal = (ActivityJournal)journal.getActivityJournals().iterator().next();
      GoalQuestPayoutActivity activity = (GoalQuestPayoutActivity)activityJournal.getActivity();
      if ( activity.getGiftCodeIssueDate() != null )
      {
        giftCodeIssueDate = activity.getGiftCodeIssueDate();
      }
    }

    return giftCodeIssueDate;
  }

  /**
   * Get gift code issue date for a merch order.
   */
  private Date getGiftCodeIssueDate( MerchOrder merchOrder )
  {
    return merchOrder.getAuditCreateInfo().getDateCreated();
  }

  /*
   * Helpers
   */
  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
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
