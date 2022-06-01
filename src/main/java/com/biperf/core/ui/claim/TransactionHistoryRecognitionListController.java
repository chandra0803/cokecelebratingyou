/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryRecognitionListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.claim.hibernate.RecognitionClaimQueryConstraint;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.activity.MerchandiseActivity;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TransactionHistoryModuleAwareType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.claim.RecognitionHistoryListController.NominationReceiveBuilder;
import com.biperf.core.ui.claim.RecognitionHistoryListController.NominationSentBuilder;
import com.biperf.core.ui.claim.RecognitionHistoryListController.ValueObjectBuilder;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

/**
 * TransactionHistoryRecognitionListController.
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
 * <td>zahler</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          Exp $
 */
public class TransactionHistoryRecognitionListController extends TransactionHistoryClaimListController
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  private static final String RECEIVED_MODE = "received";
  private static final String SENT_MODE = "sent";

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the recognition version of the Transaction History page.
   *
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for the servlets for this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    RequestWrapper requestWrapper = new RequestWrapper( request );

    //
    // Finish setting up the Show Transactions form. (Some of the setup is done in the method
    // TransactionHistoryAction.display.)
    //
    // Setup the transaction history type list.
    request.setAttribute( "transactionHistoryType", TransactionHistoryModuleAwareType.getListForTransactionHistory() );

    // Get the promotion list.
    request.setAttribute( "promotionList", getPromotionList( requestWrapper.getTransactionHistoryType() ) );

    String promotionType = request.getParameter( "promotionType" );
    //
    // Get the claim list.
    //
    Long participantId = null;
    Long promotionId = null;
    String mode = null;
    Long proxyUserId = null;
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
        participantId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        String id = (String)clientStateMap.get( "userId" );
        if ( id != null && !id.equals( "" ) )
        {
          participantId = new Long( id );
        }
      }
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        String promoId = (String)clientStateMap.get( "promotionId" );
        if ( promoId != null && !promoId.equals( "" ) )
        {
          promotionId = new Long( promoId );
        }
      }
      // Bug Fix for 17752
      try
      {
        proxyUserId = (Long)clientStateMap.get( "proxyUserId" );
      }
      catch( ClassCastException cce )
      {
        String temp = (String)clientStateMap.get( "proxyUserId" );
        if ( !StringUtils.isEmpty( temp ) )
        {
          proxyUserId = new Long( temp );
        }
      }
      if ( StringUtils.isEmpty( promotionType ) )
      {
        promotionType = (String)clientStateMap.get( "promotionType" );
      }
      mode = (String)clientStateMap.get( "mode" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( promotionId == null || promotionId.equals( new Long( 0 ) ) )
    {
      promotionId = requestWrapper.getPromotionId();
    }

    Date startDate = requestWrapper.getStartDate();
    Date endDate = DateUtils.toEndDate( requestWrapper.getEndDate() ); // get the latest time on the
                                                                       // specified date

    if ( StringUtils.isEmpty( mode ) )
    {
      mode = requestWrapper.getMode();
    }

    List valueObjects = null;
    ValueObjectBuilder builder = null;

    if ( promotionType.equals( PromotionType.NOMINATION ) )
    {
      if ( mode.equals( RECEIVED_MODE ) )
      {
        builder = new NominationReceiveBuilder( participantId, promotionId, startDate, endDate );
      }
      else if ( mode.equals( SENT_MODE ) )
      {
        builder = new NominationSentBuilder( participantId, proxyUserId, promotionId, startDate, endDate );
      }
      if ( builder != null )
      {
        valueObjects = builder.buildValueObjects();

        // Check for a reversed claim in the list, set a flag to show an extra instruction copy
        if ( valueObjects != null )
        {
          Iterator<RecognitionHistoryValueObject> objIt = valueObjects.iterator();
          while ( objIt.hasNext() )
          {
            if ( objIt.next().isReversal() )
            {
              request.setAttribute( "toShowRCopy", "Yes" );
              break;
            }
          }
        }

        request.setAttribute( "valueObjects", valueObjects );
      }

    }
    // Anything other than Nomination
    else
    {

      if ( mode.equals( SENT_MODE ) )
      {
        valueObjects = getClaimList( participantId, promotionId, startDate, endDate, mode );
      }
      else if ( mode.equals( RECEIVED_MODE ) )
      {
        valueObjects = getClaimList( participantId, promotionId, startDate, endDate, mode );
        // Bug # 38451
        valueObjects.addAll( getNonClaimRelatedDeposits( participantId, promotionId, startDate, endDate ) );
      }
      if ( valueObjects != null )
      {
        for ( Iterator valueObjectster = valueObjects.iterator(); valueObjectster.hasNext(); )
        {
          RecognitionHistoryValueObject historyValueObject = (RecognitionHistoryValueObject)valueObjectster.next();
          if ( historyValueObject.isReversal() )
          {
            request.setAttribute( "toShowRCopy", "Yes" );
            break;
          }
        }
        request.setAttribute( "recognitionHistoryValueObjects", valueObjects );
      }
    }

    //
    // Get other information used by the transaction history page.
    //
    request.setAttribute( "promotionTypeCode", promotionType + ".history" );
    request.setAttribute( "participantId", participantId );
    request.setAttribute( "startDate", startDate );
    request.setAttribute( "endDate", endDate );
    request.setAttribute( "mode", mode );
    // Bug Fix for 17752
    request.setAttribute( "proxyUser", proxyUserId );

    //
    // Select the recognition version of the transaction history page.
    //
    tileContext.putAttribute( "transactionList", "/claim/nomrec/recognitionClaimTransactionList.jsp" );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the specified claim list.
   *
   * @param participantId  the ID of the user who submitted the claims in the claim list. Must not
   *                       be null.
   * @param promotionId    the ID of the promotion for which claims in the claim list are submitted.
   *                       Can be null.
   * @param startDate      the earliest date on which claims in the claim list are submitted. Must
   *                       not be null.
   * @param endDate        the latest date on which claims in the claim list are submitted. Must not
   *                       be null.
   * @param mode           the mode, either sent or received.
   * @return the specified claim list, as a <code>List</code> of
   *         {@link TransactionHistoryValueObject} objects.
   */
  private List getClaimList( Long participantId, Long promotionId, Date startDate, Date endDate, String mode )
  {
    List valueObjects = new ArrayList();

    // Get the claim list.
    RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

    queryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );

    if ( mode.equals( RECEIVED_MODE ) )
    {
      queryConstraint.setRecipientId( participantId );
      queryConstraint.setOpen( Boolean.FALSE );
    }
    else // mode.equals( SENT_MODE )
    {
      queryConstraint.setSubmitterId( participantId );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    List claimList = getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );

    // Get the claim recipients.
    ArrayList claimRecipients = new ArrayList();

    for ( Iterator claimIter = claimList.iterator(); claimIter.hasNext(); )
    {
      RecognitionClaim claim = (RecognitionClaim)claimIter.next();

      if ( mode.equals( SENT_MODE ) )
      {
        for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
          if ( claimRecipient.getRecipient() != null )
          {
            Long recipientId = claimRecipient.getRecipient().getId();
            Long claimId = claimRecipient.getClaim().getId();

            boolean claimHasPendingJournals = getClaimService().hasPendingJournalForClaim( recipientId, claimId );
            if ( claimHasPendingJournals )
            {
              claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
            }
          }

          claimRecipients.add( claimRecipient );
        }
      }
      else if ( mode.equals( RECEIVED_MODE ) )
      {
        for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
          if ( claimRecipient.getRecipient() != null )
          {
            Long recipientId = claimRecipient.getRecipient().getId();
            Long claimId = claimRecipient.getClaim().getId();

            boolean recipientIsParticipant = recipientId.equals( participantId );
            boolean claimHasPendingJournals = getClaimService().hasPendingJournalForClaim( recipientId, claimId );

            if ( recipientIsParticipant && !claimHasPendingJournals )
            {
              claimRecipients.add( claimRecipient );
            }
          }
        }
      }
    }
    boolean flag = false;
    // Create the value objects for recognition claims.
    for ( Iterator iter = claimRecipients.iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
      Long claimId = claimRecipient.getClaim().getId();
      Long recipientUserId = null;
      if ( claimRecipient.getRecipient() != null )
      {
        recipientUserId = claimRecipient.getRecipient().getId();
      }
      RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject();
      // List activities = getActivityService()
      // .getMerchOrderActivitiesByClaimIdAndUserId( claimId, recipientUserId );
      List activities = null;
      if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
      {
        RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
        if ( recPromotion.isSelfRecognitionEnabled() )
        {
          if ( claimRecipient.getRecipient().getId().longValue() == claimRecipient.getClaim().getSubmitter().getId().longValue() )
          {
            flag = true;

          }
        }
      }
      if ( flag )
      {
        activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimId, recipientUserId, null );
      }
      else
      {
        activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimId, recipientUserId );
      }
      if ( ( (RecognitionClaim)claimRecipient.getClaim() ).isReversal() )
      {
        valueObject.setReversal( true );
      }
      valueObject.setMerchGiftCodeActivityList( activities );
      valueObject.setClaimRecipient( claimRecipient );
      valueObject.setAwardQuantity( claimRecipient.getAwardQuantity() );
      valueObject.setAwardTypeName( PromotionAwardsType.lookup( claimRecipient.getClaim().getPromotion().getAwardType().getCode() ).getName() );
      valueObject.setClaim( claimRecipient.getClaim() );
      valueObject.setSubmissionDate( new Timestamp( claimRecipient.getClaim().getSubmissionDate().getTime() ) );
      valueObject.setPromotion( claimRecipient.getClaim().getPromotion() );
      valueObject.setRecipient( claimRecipient.getRecipient() );
      valueObject.setSubmitter( claimRecipient.getClaim().getSubmitter() );
      valueObject.setProxyUser( claimRecipient.getClaim().getProxyUser() );
      valueObject.setApprovalStatusType( claimRecipient.getApprovalStatusType() );

      valueObjects.add( valueObject );
    }

    return valueObjects;
  }

  /**
   * Returns information about deposits associated with recognition promotions, but not associated
   * with recognition claims.
   *
   * @param participantId  identifies a participant.
   * @param promotionId    identifies a promotion.
   * @return information about non-claim-related deposits, as a <code>List</code> of
   *         {@link TransactionHistoryValueObject} objects.
   */
  // Bug # 38451
  private List getNonClaimRelatedDeposits( Long participantId, Long promotionId, Date startDate, Date endDate )
  {
    List valueObjects = new ArrayList();

    JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
    journalQueryConstraint.setParticipantId( participantId );
    journalQueryConstraint.setPromotionId( promotionId );
    journalQueryConstraint.setNotResultOfClaim( true );
    journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
    journalQueryConstraint.setJournalTypesExcluded( new String[] { Journal.DISCRETIONARY } );

    /* Bug # 38451 start */
    journalQueryConstraint.setStartDate( startDate );
    journalQueryConstraint.setEndDate( endDate );
    /* Bug # 38451 end */

    AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
    journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
    journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

    List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );

    for ( Iterator iter = journalList.iterator(); iter.hasNext(); )
    {
      Journal journal = (Journal)iter.next();

      // Select only deposits that are associated with recognition promotions, but not
      // associated with claims. Files loads cause this type of desposit. The application
      // does not show discretionary transactions on this page, it shows them on another page.
      if ( journal.getPromotion() != null && journal.getPromotion().isRecognitionPromotion() )
      {
        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject();

        valueObject.setSubmissionDate( new Timestamp( journal.getTransactionDate().getTime() ) );
        valueObject.setPromotion( journal.getPromotion() );
        valueObject.setRecipient( journal.getParticipant() );
        valueObject.setJournalId( journal.getId() );
        valueObject.setAwardQuantity( journal.getTransactionAmount() );
        valueObject.setAwardTypeName( PromotionAwardsType.lookup( journal.getPromotion().getAwardType().getCode() ).getName() );
        // For Fileload, should be false here
        valueObject.setDiscretionary( Journal.DISCRETIONARY.equals( journal.getJournalType() ) );
        valueObject.setSweepstakes( Journal.SWEEPSTAKES.equals( journal.getJournalType() ) );
        valueObject.setMerchGiftCodeActivityList( getMerchOrderActivityList( journal ) );
        if ( JournalTransactionType.REVERSE.equals( journal.getTransactionType().getCode() ) )
        {
          valueObject.setReversalDescription( journal.getTransactionDescription() );
        }
        valueObjects.add( valueObject );
      }
    }

    return valueObjects;
  }

  private List getMerchOrderActivityList( Journal journal )
  {
    List merchOrderActivityList = new ArrayList();
    AbstractRecognitionPromotion recognitionPromotion = (AbstractRecognitionPromotion)journal.getPromotion();
    if ( recognitionPromotion.isAwardActive() && recognitionPromotion.getAwardType().isMerchandiseAwardType()
        && RecognitionPromotion.AWARD_STRUCTURE_LEVEL.equals( recognitionPromotion.getAwardStructure() ) )
    {
      for ( Iterator activityJournalIter = journal.getActivityJournals().iterator(); activityJournalIter.hasNext(); )
      {
        ActivityJournal currentActivityJournal = (ActivityJournal)activityJournalIter.next();
        if ( currentActivityJournal.getActivity() instanceof MerchandiseActivity )
        {
          merchOrderActivityList.add( currentActivityJournal.getActivity() );
        }
      }
    }
    return merchOrderActivityList;
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Get the claim service.
   *
   * @return a reference to the claim service.
   */
  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  /**
   * Get the journal service.
   *
   * @return a reference to the journal service.
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  /**
   * Returns the participant service.
   *
   * @return a reference to the participant service.
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adds claim list controller specific behavior to an <code>HttpServletRequest</code> object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    /**
     * Keys to request attributes and parameters.
     */
    private static final String END_DATE = "endDate";
    private static final String MODE = "mode";
    private static final String OPEN = "open";
    private static final String PARTICIPANT_ID = "userId";
    private static final String PROMOTION_ID = "promotionId";
    private static final String TRANSACTION_HISTORY_TYPE = "promotionType";
    private static final String START_DATE = "startDate";

    /**
     * Constructs a <code>RequestWrapper</code> object.
     */
    RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    /**
     * Returns the latest date on which claims in the claim list are submitted. Returns now if the
     * user does not specify an end date; returns null if the user enters an invalid date.
     *
     * @return the latest date on which claims in the claim list are submitted.
     */
    public Date getEndDate()
    {
      return (Date)getAttribute( END_DATE );
    }

    /**
     * Returns the mode, either sent or received.
     *
     * @return the mode, either sent or received.
     */
    public String getMode()
    {
      String mode = getParameter( MODE );

      if ( mode == null || mode.length() == 0 )
      {
        mode = (String)getAttribute( MODE );
      }

      if ( mode == null || mode.length() == 0 )
      {
        mode = SENT_MODE;
      }

      return mode;
    }

    /**
     * Returns true if the application will show open claims, returns false if the application will
     * show closed claims.
     *
     * @return true if the application will show open claims, return false if the application will
     *         show closed claims
     */
    public Boolean getOpen()
    {
      Boolean open = new Boolean( true );

      String openString = getParameter( OPEN );
      if ( openString != null && openString.length() > 0 )
      {
        open = new Boolean( openString );
      }

      return open;
    }

    /**
     * Returns the ID of the participant who submitted or is a team member of
     * the claims in the claim list.
     *
     * @return the ID of the user who submitted or is a team member of the
     *         claims in the claim list.
     */
    public Long getParticipantId()
    {
      Long participantId = null;

      String participantIdString = getParameter( PARTICIPANT_ID );
      if ( participantIdString != null && participantIdString.length() > 0 )
      {
        participantId = new Long( participantIdString );
      }

      return participantId;
    }

    /**
     * Returns the ID of the promotion for which claims in the claim list are submitted. Returns
     * null if the user specified all promotions.
     *
     * @return the ID of the promotion for which claims in the claim list are submitted.
     */
    public Long getPromotionId()
    {
      Long promotionId = null;

      String promotionIdString = (String)getAttribute( PROMOTION_ID );
      if ( promotionIdString != null && promotionIdString.length() > 0 )
      {
        promotionId = new Long( promotionIdString );
      }

      return promotionId;
    }

    /**
     * Returns the earliest date on which claims in the claim list are submitted.
     *
     * @return the earliest date on which claims in the claim list are submitted.
     */
    public Date getStartDate()
    {
      return (Date)getAttribute( START_DATE );
    }

    /**
     * Returns the promotion type.
     *
     * @return the earliest date on which claims in the claim list are submitted.
     */
    public String getTransactionHistoryType()
    {
      return (String)getAttribute( TRANSACTION_HISTORY_TYPE );
    }
  }

  /**
   * Get the ActivityService from the beanLocator.
   * 
   * @return ActivityService
   */
  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }
}
