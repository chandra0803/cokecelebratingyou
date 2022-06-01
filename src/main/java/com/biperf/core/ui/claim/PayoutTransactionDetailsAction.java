/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/PayoutTransactionDetailsAction.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
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

import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PayoutTransactionDetailsAction.
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
 * <td>robinsra</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutTransactionDetailsAction extends BaseDispatchAction
{
  /**
   * Prepares anything necessary before showing the Update Message
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    PayoutTransactionDetailsForm payoutTransactionDetailsForm = (PayoutTransactionDetailsForm)actionForm;

    // ----------------
    // Get parameters
    // ----------------
    Long transactionId = null;
    Long userId = null;
    String claimId = null;
    String claimGroupId = null;
    String promotionId = null;
    String endDate = null;
    String startDate = null;
    String mode = null;
    String open = null;
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
        String transactionIdString = (String)clientStateMap.get( "transactionId" );
        transactionId = new Long( transactionIdString );
      }
      catch( ClassCastException cce )
      {
        transactionId = (Long)clientStateMap.get( "transactionId" );
      }
      try
      {
        String userIdString = (String)clientStateMap.get( "userId" );
        userId = new Long( userIdString );
      }
      catch( ClassCastException cce2 )
      {
        userId = (Long)clientStateMap.get( "userId" );
      }

      Object claimGroupIdObj = clientStateMap.get( "claimGroupId" );
      if ( claimGroupIdObj != null )
      {
        try
        {
          claimGroupId = (String)claimGroupIdObj;
        }
        catch( ClassCastException cce )
        {
          Long claimGroupIdLong = (Long)claimGroupIdObj;
          claimGroupId = claimGroupIdLong.toString();
        }
      }

      claimId = (String)clientStateMap.get( "claimId" );
      try
      {
        promotionId = (String)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        Long promotionIdLong = (Long)clientStateMap.get( "promotionId" );
        promotionId = promotionIdLong.toString();
      }
      endDate = (String)clientStateMap.get( "endDate" );
      startDate = (String)clientStateMap.get( "startDate" );
      open = (String)clientStateMap.get( "open" );
      mode = (String)clientStateMap.get( "mode" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( transactionId != null && userId != null )
    {
      // ----------------
      // Get journal
      // ----------------
      Journal journal = null;
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

      journal = getJournalService().getJournalById( transactionId, associationRequestCollection );
      request.setAttribute( "journal", journal );
      if ( journal == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.JOURNAL_NOT_FOUND ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
      // ----------------
      // Get participant
      // ----------------
      Participant participant = getParticipantService().getParticipantById( userId );
      if ( participant == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.PARTICIPANT_NOT_FOUND ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
      request.setAttribute( "participant", participant );
      // load the form
      payoutTransactionDetailsForm.load( journal, participant.getId() );
      payoutTransactionDetailsForm.setMethod( "update" );
      payoutTransactionDetailsForm.setClaimId( claimId );
      payoutTransactionDetailsForm.setClaimGroupId( claimGroupId );
      payoutTransactionDetailsForm.setPromotionId( promotionId );
      payoutTransactionDetailsForm.setEndDate( endDate );
      payoutTransactionDetailsForm.setStartDate( startDate );
      payoutTransactionDetailsForm.setMode( mode );
      payoutTransactionDetailsForm.setOpen( new Boolean( open ).booleanValue() );
    }
    else
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "transactionId and userId as part of clientState" ) );
      saveErrors( request, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }
    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  } // end prepareUpdate

  /**
   * Updates a message
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    PayoutTransactionDetailsForm payoutTransactionDetailsForm = (PayoutTransactionDetailsForm)actionForm;

    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      try
      {
        String actionCode = payoutTransactionDetailsForm.getActionCode();

        if ( actionCode.equals( JournalStatusType.APPROVE ) )
        {
          getJournalService().approveJournal( payoutTransactionDetailsForm.getTransactionId(), payoutTransactionDetailsForm.getComments(), payoutTransactionDetailsForm.getReasonCode() );
        }
        else if ( actionCode.equals( JournalStatusType.DENY ) )
        {
          getJournalService().denyJournal( payoutTransactionDetailsForm.getTransactionId(), payoutTransactionDetailsForm.getComments(), payoutTransactionDetailsForm.getReasonCode() );
        }
        else if ( actionCode.equals( JournalStatusType.POST ) )
        {
          getJournalService().postJournal( payoutTransactionDetailsForm.getTransactionId(), payoutTransactionDetailsForm.getComments(), payoutTransactionDetailsForm.getReasonCode() );
        }
        else if ( actionCode.equals( JournalStatusType.VOID ) )
        {
          getJournalService().voidJournal( payoutTransactionDetailsForm.getTransactionId(), payoutTransactionDetailsForm.getComments(), payoutTransactionDetailsForm.getReasonCode() );
        }
        else if ( actionCode.equals( JournalStatusType.REVERSE ) )
        {
          doReverseOption( payoutTransactionDetailsForm, request, errors );
        }
      }
      catch( ServiceErrorException e )
      {
        List<ServiceError> serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    if ( payoutTransactionDetailsForm.getCallingScreen().equals( "payouts" ) )
    {
      // <forward name="success_update_payouts" path="claim.transaction.payouts" redirect="true"/>
      return actionMapping.findForward( "success_update_payouts" );
    }
    else if ( payoutTransactionDetailsForm.getCallingScreen().equals( "activities" ) )
    {
      // <forward name="success_update_activities" path="claim.transaction.payouts"
      // redirect="true"/>
      return actionMapping.findForward( "success_update_activities" );
    }
    else if ( payoutTransactionDetailsForm.getCallingScreen().equals( "transactions" ) )
    {
      // <forward name="success_update_transactions" path="claim.transactions"
      // redirect="true"/>
      return actionMapping.findForward( "success_update_transactions" );
    }
    else
    {
      return actionMapping.findForward( "" );
    }
  } // end update

  /**
   * Called when the action code changes.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeActionCode( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    PayoutTransactionDetailsForm payoutTransactionDetailsForm = (PayoutTransactionDetailsForm)actionForm;
    payoutTransactionDetailsForm.setMethod( "update" );
    if ( !payoutTransactionDetailsForm.getActionCode().equals( JournalStatusType.DENY ) )
    {
      payoutTransactionDetailsForm.setReasonCode( "" );
    }
    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * Take action when the reverse option is chosen. Reverse all applicable journals.
   */
  private void doReverseOption( PayoutTransactionDetailsForm form, HttpServletRequest request, ActionMessages errors ) throws ServiceErrorException
  {
    List<Journal> journals = new ArrayList<>(); // List of journals to reverse
    Long claimId = null;
    if ( StringUtils.isNotEmpty( form.getClaimId() ) )
    {
      claimId = new Long( form.getClaimId() );
    }
    Long claimGroupId = null;
    if ( StringUtils.isNotEmpty( form.getClaimGroupId() ) )
    {
      claimGroupId = new Long( form.getClaimGroupId() );
    }

    // Grab the journal entry for the transaction being reversed
    Journal selectedJournal = getJournalService().getJournalById( form.getTransactionId() );

    // Only point awards can be reversed - do not attempt reversing anything else
    if ( !PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).equals( selectedJournal.getAwardPayoutType() ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "participant.payout.transaction.detail.CASH_REVERSE_ERROR" ) );
      return;
    }

    // Do not allow the reverse option more than once
    int count = getJournalService().getReverseJournalsByJournalId( selectedJournal.getId(), selectedJournal.getPromotion().getId() );
    if ( count != 0 || selectedJournal.getTransactionAmount().longValue() < 0 )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "participant.payout.transaction.detail.errors.PAYOUT_REVERSED" ) );
      return;
    }

    // Nominations requires reversing an entire team of winners.
    if ( PromotionType.NOMINATION.equals( form.getPromotionType() ) )
    {
      Long promotionId = null;
      if ( StringUtils.isNotEmpty( form.getPromotionId() ) )
      {
        promotionId = new Long( form.getPromotionId() );
      }
      else
      {
        promotionId = new Long( selectedJournal.getPromotion().getId() );
      }
      NominationPromotion nominationPromotion = getNominationPromotionService().getNominationPromotion( promotionId );

      // Cumulative
      if ( nominationPromotion != null && nominationPromotion.isCumulative() )
      {
        // Just use the provided journal. Cumulative cannot be team, there will only be one journal
        journals.add( selectedJournal );
      }
      // Assume independent
      else
      {
        JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();

        // Claim ID is needed - if it were null, we would end up reversing *every* journal
        if ( claimId == null )
        {
          throw new BeaconRuntimeException( "Claim ID cannot be null" );
        }
        journalQueryConstraint.setClaimId( claimId );

        journals = getJournalService().getJournalList( journalQueryConstraint );
      }
    }
    else
    {
      journals.add( selectedJournal );
    }

    Iterator<Journal> journalIterator = journals.iterator();
    while ( journalIterator.hasNext() )
    {
      Journal journal = journalIterator.next();
      if ( journal.getAwardPayoutType().getCode().equals( PromotionAwardsType.POINTS ) )
      {
        doReverseJournal( journal, form, request, errors );
      }
    }

    // For nomination reversals, mark everyone as non-winner
    if ( PromotionType.NOMINATION.equals( form.getPromotionType() ) )
    {
      getNominationClaimService().reverseWinnerStatus( claimGroupId, claimId );
    }
  }

  /**
   * Take action to reverse a single journal
   */
  private void doReverseJournal( Journal journal, PayoutTransactionDetailsForm form, HttpServletRequest request, ActionMessages errors ) throws ServiceErrorException
  {
    // To Fix the bug 20134
    int count = getJournalService().getReverseJournalsByJournalId( journal.getId(), journal.getPromotion().getId() );
    if ( count != 0 || journal.getTransactionAmount().longValue() < 0 )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "participant.payout.transaction.detail.errors.PAYOUT_REVERSED" ) );
    }
    else
    {
      getJournalService().reverseJournal( journal.getId(), form.getComments(), form.getReasonCode() );

      getJournalService().excecuteOnReversal( journal.getId() );
      // Below one is required for only Online claims.
      if ( form.getClaimId() != null && !form.getClaimId().equals( "" ) )
      {
        getClaimService().excecuteOnReversal( form.getClaimId(), form.getPromotionType() );
      }
      else if ( form.getClaimGroupId() != null && !form.getClaimGroupId().equals( "" ) )
      {
        ClaimGroup claimGroup = getClaimGroupService().getClaimGroupById( new Long( form.getClaimGroupId() ) );
        Iterator<Claim> claimIterator = claimGroup.getClaims().iterator();
        while ( claimIterator.hasNext() )
        {
          Claim claim = claimIterator.next();
          getClaimService().excecuteOnReversal( String.valueOf( claim.getId() ), form.getPromotionType() );
        }
      }
    }
  }

  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private ClaimGroupService getClaimGroupService()
  {
    return (ClaimGroupService)getService( ClaimGroupService.BEAN_NAME );
  }

  private NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private NominationPromotionService getNominationPromotionService()
  {
    return (NominationPromotionService)getService( NominationPromotionService.BEAN_NAME );
  }

}
