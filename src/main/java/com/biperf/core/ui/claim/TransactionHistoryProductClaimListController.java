/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryProductClaimListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.claim.hibernate.ProductClaimClaimQueryConstraint;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.activity.StackRankActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.TransactionHistoryModuleAwareType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * TransactionHistoryProductClaimListController.
 * 
 * Fetches the product claim transaction history for an administrator. 
 * 
 * Trx History -- User's View -- Product Claim.
 * 
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
public class TransactionHistoryProductClaimListController extends TransactionHistoryClaimListController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the product claim version of the Transaction History page.
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

    //
    // Get the claim list.
    //
    String promotionType = request.getParameter( "promotionType" );
    Long participantId = null;
    Long promotionId = null;

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
        participantId = new Long( (String)clientStateMap.get( "userId" ) );
      }
      if ( StringUtils.isEmpty( promotionType ) )
      {
        promotionType = (String)clientStateMap.get( "promotionType" );
      }
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        String id = (String)clientStateMap.get( "promotionId" );
        if ( id != null && !id.equals( "" ) )
        {
          promotionId = new Long( id );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( promotionId == null && request.getAttribute( "promotionId" ) != null )
    {
      String promoId = (String)request.getAttribute( "promotionId" );
      if ( !StringUtils.isEmpty( promoId ) )
      {
        promotionId = new Long( promoId );
      }
    }

    Date startDate = requestWrapper.getStartDate();
    Date endDate = DateUtils.toEndDate( requestWrapper.getEndDate() ); // get the latest time on the
                                                                       // specified date

    Boolean open = requestWrapper.getOpen();
    request.setAttribute( "open", open );

    List claimList = null;
    if ( open.booleanValue() )
    {
      // Get the open claim list.
      claimList = getClaimList( participantId, promotionId, startDate, endDate, true );
      claimList.addAll( getManagerOverrideDeposits( participantId, promotionId, startDate, endDate, true ) );
    }
    else
    {
      // Get the closed claim list.
      claimList = getClaimList( participantId, promotionId, startDate, endDate, false );
      // To fix bug 18768
      // Bug # 38451
      claimList.addAll( getNonClaimRelatedDeposits( participantId, promotionId, startDate, endDate ) );
      claimList.addAll( getManagerOverrideDeposits( participantId, promotionId, startDate, endDate, false ) );
      claimList.addAll( getStackRankAwards( participantId, promotionId ) );
    }
    request.setAttribute( "transactionHistoryClaimList", claimList );

    //
    // Get other information used by the transaction history page.
    //
    request.setAttribute( "promotionTypeCode", promotionType + ".history" );
    request.setAttribute( "participant", getParticipantService().getParticipantById( participantId ) );

    //
    // Select the product claim version of the transaction history page.
    //
    tileContext.putAttribute( "transactionList", "/productclaim/productClaimTransactionList.jsp" );
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
   * @param isOpen         true if the claim list contains only open claims; false if the claim list
   *                       contains only closed claims.
   * @return the specified claim list, as a <code>List</code> of
   *         {@link TransactionHistoryValueObject} objects.
   */
  private List getClaimList( Long participantId, Long promotionId, Date startDate, Date endDate, boolean isOpen )
  {
    List valueObjects = new ArrayList();

    // Get the claim list.
    ProductClaimClaimQueryConstraint queryConstraint = new ProductClaimClaimQueryConstraint();

    queryConstraint.setSubmitterOrTeamMemberParticipantId( participantId );
    queryConstraint.setSubmitter( true );
    queryConstraint.setTeamMember( true );
    queryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );
    queryConstraint.setOpen( Boolean.valueOf( isOpen ) );

    List claimList = getClaimService().getClaimList( queryConstraint );

    // Create the list of value objects.
    for ( int i = 0; i < claimList.size(); i++ )
    {
      ClaimAdapter claimAdapter = new ClaimAdapter( (Claim)claimList.get( i ), participantId );

      TransactionHistoryValueObject valueObject = new TransactionHistoryValueObject();
      valueObject.setClaim( claimAdapter.getClaim() );
      valueObject.setEarnings( claimAdapter.getEarnings() );
      valueObject.setAwardTypeName( PromotionAwardsType.lookup( claimAdapter.claim.getPromotion().getAwardType().getCode() ).getName() );
      valueObject.setStackRankClaim( claimAdapter.isStackRankClaim() );

      valueObjects.add( valueObject );
    }

    return valueObjects;
  }

  /**
   * Returns information about non-claim-related deposits, such as file loads awards.
   *
   * @param participantId  identifies the participant whose non-claim-related deposits this
   *                       method returns.
   * @return information about non-claim-related deposits, such as file loads and discretionary
   *         awards, as a <code>List</code> of {@link TransactionHistoryValueObject} objects.
   */
  // Bug # 38451
  private List getNonClaimRelatedDeposits( Long participantId, Long promotionId, Date startDate, Date endDate )
  {
    List valueObjects = new ArrayList();

    // Bug # 38451
    List journalList = getJournalList( participantId, promotionId, startDate, endDate );
    for ( Iterator iter = journalList.iterator(); iter.hasNext(); )
    {
      Journal journal = (Journal)iter.next();
      if ( journal.getPromotion() != null && journal.getPromotion().isProductClaimPromotion() )
      {
        // Create the mock claim.
        Claim claim = new ProductClaim();

        claim.setId( new Long( 0 ) );
        claim.setPromotion( journal.getPromotion() );
        claim.setSubmissionDate( new Timestamp( journal.getTransactionDate().getTime() ) );

        AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
        // auditCreateInfo.setCreatedBy( UserManager.getUserId() );
        auditCreateInfo.setCreatedBy( journal.getAuditCreateInfo().getCreatedBy() );
        auditCreateInfo.setDateCreated( new Timestamp( journal.getTransactionDate().getTime() ) );
        claim.setAuditCreateInfo( auditCreateInfo );

        // Create the value object.
        TransactionHistoryValueObject valueObject = new TransactionHistoryValueObject();
        if ( auditCreateInfo.getCreatedBy() != null )
        {
          User user = getUserService().getUserById( auditCreateInfo.getCreatedBy() );
          valueObject.setCreatedBy( user );
        }
        valueObject.setClaim( claim );
        valueObject.setEarnings( journal.getTransactionAmount() );
        valueObject.setAwardTypeName( PromotionAwardsType.lookup( journal.getPromotion().getAwardType().getCode() ).getName() );

        // For Fileload, should be false here
        valueObject.setDiscretionary( Journal.DISCRETIONARY.equals( journal.getJournalType() ) );
        valueObject.setSweepstakes( Journal.SWEEPSTAKES.equals( journal.getJournalType() ) );
        valueObject.setManagerOverride( Journal.MANAGER_OVERRIDE.equals( journal.getJournalType() ) );
        if ( JournalTransactionType.REVERSE.equals( journal.getTransactionType().getCode() ) )
        {
          valueObject.setReversalDescription( journal.getTransactionDescription() );
        }
        valueObjects.add( valueObject );
      }
    }

    return valueObjects;
  }

  /**
   * Returns information about manager-override claims.
   *
   * @param participantId  identifies the participant who is the manager(actually owner) this
   *                       method returns.
   * @return information about manager-override claim
   *         awards, as a <code>List</code> of {@link TransactionHistoryValueObject} objects.
   */
  private List getManagerOverrideDeposits( Long participantId, Long promotionId, Date startDate, Date endDate, boolean isOpen )
  {
    List valueObjects = new ArrayList();

    // Get the claim list.
    ProductClaimClaimQueryConstraint queryConstraint = new ProductClaimClaimQueryConstraint();

    queryConstraint.setSubmitter( false );
    queryConstraint.setTeamMember( false );
    queryConstraint.setManagerId( participantId );
    queryConstraint.setManagerOverrideItem( true );
    queryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );
    queryConstraint.setOpen( Boolean.valueOf( isOpen ) );

    List claimList = getClaimService().getClaimList( queryConstraint );

    // Create the list of value objects.
    for ( int i = 0; i < claimList.size(); i++ )
    {
      ClaimAdapter claimAdapter = new ClaimAdapter( (Claim)claimList.get( i ), participantId );

      TransactionHistoryValueObject valueObject = new TransactionHistoryValueObject();
      valueObject.setClaim( claimAdapter.getClaim() );
      valueObject.setEarnings( getClaimService().getEarningsForClaim( claimAdapter.getClaim().getId(), participantId ) );
      valueObject.setAwardTypeName( PromotionAwardsType.lookup( claimAdapter.claim.getPromotion().getAwardType().getCode() ).getName() );
      valueObject.setStackRankClaim( claimAdapter.isStackRankClaim() );
      valueObject.setManagerOverride( true );
      valueObjects.add( valueObject );
    }

    return valueObjects;
  }

  /**
   * Returns information about stack rank awards for the specified participant.
   *
   * @param participantId  the ID of the participant who received the stack rank awards.
   * @return information about stack rank awards, as a <code>List</code> of
   *         {@link ProductClaimValueObject} objects.
   */
  private List getStackRankAwards( Long participantId, Long promotionId )
  {
    List stackRankAwards = new ArrayList();

    List journalList = getJournalListForStackRankAwards( participantId, promotionId );
    for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
    {
      JournalAdapter journalAdapter = new JournalAdapter( (Journal)journalIter.next() );

      // Create the mock claim.
      Claim claim = new ProductClaim();

      claim.setId( new Long( 0 ) );
      claim.setSubmitter( journalAdapter.getSubmitter() );
      claim.setClaimNumber( journalAdapter.getClaimNumber() );
      claim.setPromotion( journalAdapter.getPromotion() );
      claim.setSubmissionDate( journalAdapter.getSubmissionDate() );

      AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
      auditCreateInfo.setCreatedBy( UserManager.getUserId() );
      auditCreateInfo.setDateCreated( journalAdapter.getSubmissionDate() );
      claim.setAuditCreateInfo( auditCreateInfo );

      // Create the value object.
      TransactionHistoryValueObject valueObject = new TransactionHistoryValueObject();
      valueObject.setClaim( claim );
      valueObject.setEarnings( journalAdapter.getEarnings() );
      valueObject.setAwardTypeName( PromotionAwardsType.lookup( journalAdapter.getPromotion().getAwardType().getCode() ).getName() );
      valueObject.setStackRank( true );

      stackRankAwards.add( valueObject );
    }

    return stackRankAwards;
  }

  /**
   * Returns deposits not linked to any claim.  Such as File loads but excluding discretionary awards
   * cause these deposits.
   *
   * @param participantId
   * @return deposits not linked to any claim, as a <code>List</code> of {@link Journal} objects.
   */
  // Bug # 38451
  private List getJournalList( Long participantId, Long promotionId, Date startDate, Date endDate )
  {
    JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
    queryConstraint.setParticipantId( participantId );
    queryConstraint.setPromotionId( promotionId );
    queryConstraint.setNotResultOfClaim( true );
    queryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );

    /* Bug # 38451 start */
    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );
    /* Bug # 38451 end */

    // excludes Discretionary here because there is a Discretionary Trx History for the User Admin
    // Also, exclude StackRank Journal Payout(s) as they will be fetched by getStackRankAwards()
    // Fix to Bug #15720
    queryConstraint.setJournalTypesExcluded( new String[] { Journal.DISCRETIONARY, Journal.STACK_RANK } );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

    return getJournalService().getJournalList( queryConstraint, associationRequestCollection );
  }

  /**
   * Returns journals for stack rank awards.
   *
   * @param participantId  the ID of the participant whose journals this method returns.
   * @return journals for stack rank awards, as a <code>List</code> of {@link Journal} objects.
   */
  private List getJournalListForStackRankAwards( Long participantId, Long prmotionId )
  {
    JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
    queryConstraint.setParticipantId( participantId );
    queryConstraint.setPromotionId( prmotionId );
    queryConstraint.setPromotionPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.STACK_RANK ) );
    queryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
    queryConstraint.setJournalTransactionTypesIncluded( new JournalTransactionType[] { JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ),
                                                                                       JournalTransactionType.lookup( JournalTransactionType.PAYOUT ) } );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.STACK_RANK_AWARD ) );

    return getJournalService().getJournalList( queryConstraint, associationRequestCollection );
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

  /**
   * Returns the user service.
   *
   * @return a reference to the user service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adapts a {@link Claim} object to a {@link TransactionHistoryValueObject}
   * object.
   */
  private static class ClaimAdapter
  {
    /**
     * The adapted {@link Claim} object.
     */
    private Claim claim;

    /**
     * A participant associated with the wrapped claim.
     */
    private Long participantId;

    /**
     * Constructs a <code>ClaimAdapter</code> object.
     *
     * @param claim          the {@link Claim} object to be adapted.
     * @param participantId  the ID of a participant associated with this claim.
     */
    public ClaimAdapter( Claim claim, Long participantId )
    {
      this.claim = claim;
      this.participantId = participantId;
    }

    /**
     * Returns the claim adapted by this adapter.
     *
     * @return the claim adapted by this adapter.
     */
    public Claim getClaim()
    {
      return claim;
    }

    /**
     * Returns the amount the participanted received as a result of submitting
     * this claim.
     *
     * @return the amount the participanted received as a result of submitting
     *         this claim.
     */
    public Long getEarnings()
    {
      return getClaimService().getEarningsForClaim( claim.getId(), participantId );
    }

    /**
     * Returns true if the payout type for this claim's promotion is "stack
     * rank."
     *
     * @return true if the payout type for this claim's promotion is "stack
     *         rank."
     */
    public boolean isStackRankClaim()
    {
      boolean isStackRankClaim = false;

      if ( claim.getPromotion() instanceof ProductClaimPromotion )
      {
        ProductClaimPromotion promotion = (ProductClaimPromotion)claim.getPromotion();
        if ( promotion.getPayoutType().getCode().equals( PromotionPayoutType.STACK_RANK ) )
        {
          isStackRankClaim = true;
        }
      }

      return isStackRankClaim;
    }

    /**
     * Get the claim service.
     *
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }

  }

  /**
   * Adapts a {@link Journal} object to a {@link TransactionHistoryValueObject}
   * object.
   */
  private static class JournalAdapter
  {
    /**
     * The adapted {@link Journal} object.
     */
    private Journal journal;

    /**
     * Constructs a <code>JournalAdapter</code> object.
     *
     * @param journal  the {@link Journal} object to be adapted.
     */
    public JournalAdapter( Journal journal )
    {
      this.journal = journal;
    }

    /**
     * Returns the text displayed in the Claim Number column of the Closed Claims list on the
     * Product Claim History page when the row displays information about a stack rank award.
     *
     * @return the text displayed in the Claim Number when the row displays information about a stack
     *         rank award.
     */
    public String getClaimNumber()
    {
      StackRankParticipant stackRankParticipant = getStackRankParticipant();
      if ( stackRankParticipant == null )
      {
        return null;
      }
      else
      {
        StackRankNode stackRankNode = stackRankParticipant.getStackRankNode();

        Integer rank = new Integer( stackRankParticipant.getRank() );
        Integer stackRankParticipantCount = new Integer( stackRankNode.getStackRankParticipants().size() );

        return MessageFormat.format( ContentReaderManager.getText( "claims.list", "STACK_RANK_AWARD" ), new Object[] { rank, stackRankParticipantCount } );
      }
    }

    /**
     * Returns the date and time at which the stack rank award was deposited in the participant's
     * account.
     *
     * @return the date and time at which the stack rank award was deposited in the participant's
     *         account.
     */
    public Timestamp getSubmissionDate()
    {
      return new Timestamp( journal.getTransactionDate().getTime() );
    }

    /**
     * Returns the amount the participant was awarded.
     *
     * @return the amount the participant was awarded.
     */
    public Long getEarnings()
    {
      return journal.getTransactionAmount();
    }

    /**
     * Returns the participant associated with the journal.
     *
     * @return the participant associated with the journal.
     */
    public Participant getSubmitter()
    {
      return journal.getParticipant();
    }

    /**
     * Returns the promotion associated with the journal.
     *
     * @return the promotion associated with the journal.
     */
    public Promotion getPromotion()
    {
      return journal.getPromotion();
    }

    /**
     * Returns the stack rank participant associated with the journal.
     *
     * @return the stack rank participant associated with the journal.
     */
    private StackRankParticipant getStackRankParticipant()
    {
      StackRankParticipant stackRankParticipant = null;

      Set activityJournals = journal.getActivityJournals();
      if ( activityJournals != null && activityJournals.size() > 0 )
      {
        // Assumption: The journal is associated with one and only one stack rank activity.
        ActivityJournal activityJournal = (ActivityJournal)activityJournals.iterator().next();
        if ( !activityJournal.getJournal().getJournalType().equals( Journal.SWEEPSTAKES ) )
        {
          StackRankActivity activity = (StackRankActivity)activityJournal.getActivity();
          stackRankParticipant = activity.getStackRankParticipant();
        }
      }

      return stackRankParticipant;
    }
  }

  /**
   * Adds claim list controller specific behavior to an <code>HttpServletRequest</code> object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    /**
     * Keys to request attributes and parameters.
     */
    private static final String END_DATE = "endDate";
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
     * Returns the promotion type.
     *
     * @return the earliest date on which claims in the claim list are submitted.
     */
    public String getTransactionHistoryType()
    {
      return (String)getAttribute( TRANSACTION_HISTORY_TYPE );
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
  }
}
