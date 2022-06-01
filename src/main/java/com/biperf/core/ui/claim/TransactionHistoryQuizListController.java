/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryQuizListController.java,v $
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

import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.TransactionHistoryModuleAwareType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ParticipantQuizClaimHistory;
import com.biperf.util.StringUtils;

/**
 * TransactionHistoryQuizListController.
 * 
 * Fetches the quiz transaction history for an administrator. 
 * 
 * Trx History -- User's View -- Quiz.
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
 */
public class TransactionHistoryQuizListController extends TransactionHistoryClaimListController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the quiz version of the Transaction History page.
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

    //
    // Get the claim list.
    //
    String promotionType = requestWrapper.getTransactionHistoryType();
    Long promotionId = requestWrapper.getPromotionId();
    Long participantId = null;
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
      if ( StringUtils.isEmpty( promotionType ) )
      {
        promotionType = (String)clientStateMap.get( "promotionType" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Get the promotion list.
    request.setAttribute( "promotionList", getPromotionList( promotionType ) );

    if ( request.getAttribute( "promotionId" ) != null )
    {
      if ( !request.getAttribute( "promotionId" ).equals( "" ) )
      {
        promotionId = new Long( (String)request.getAttribute( "promotionId" ) );
      }
    }

    Date startDate = requestWrapper.getStartDate();
    Date endDate = DateUtils.toEndDate( requestWrapper.getEndDate() ); // get the latest time on the
                                                                       // specified date
    // Date endDate = requestWrapper.getEndDate();

    //
    // Get other information used by the transaction history page.
    //
    request.setAttribute( "participant", getParticipantService().getParticipantById( participantId ) );
    request.setAttribute( "startDate", startDate );
    request.setAttribute( "endDate", endDate );
    request.setAttribute( "promotionTypeCode", promotionType + ".history" );

    List quizValueAttemptedList = getClaimList( participantId, promotionId, startDate, endDate );
    quizValueAttemptedList.addAll( getNonQuizRelatedDeposits( participantId, promotionId ) );
    request.setAttribute( "quizValueAttemptedList", quizValueAttemptedList );

    //
    // Select the quiz version of the transaction history page.
    //
    tileContext.putAttribute( "transactionList", "/quiz/quizClaimTransactionList.jsp" );
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
   * @return the specified claim list, as a <code>List</code> of {@link QuizHistoryValueObject}
   *         objects.
   */
  private List getClaimList( Long participantId, Long promotionId, Date startDate, Date endDate )
  {
    ArrayList quizValueAttemptedList = new ArrayList();

    // Get the quiz claim groupings.
    QuizClaimQueryConstraint queryConstraint = new QuizClaimQueryConstraint();

    queryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );
    queryConstraint.setSubmitterId( participantId );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );

    Map participantQuizClaimHistoryMap = getClaimService().getParticipantQuizClaimHistoryByPromotionMap( queryConstraint, associationRequestCollection );

    for ( Iterator pqchIter = participantQuizClaimHistoryMap.keySet().iterator(); pqchIter.hasNext(); )
    {
      ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)participantQuizClaimHistoryMap.get( pqchIter.next() );
      List quizClaimsBySubmisstionDate = participantQuizClaimHistory.getQuizClaimsBySubmissionDate();

      boolean resumeQuiz = false;
      for ( Iterator qcIter = participantQuizClaimHistory.getQuizClaimsBySubmissionDate().iterator(); qcIter.hasNext(); )
      {
        QuizClaim quizClaim = (QuizClaim)qcIter.next();

        if ( participantQuizClaimHistory.getMostRecentClaim().equals( quizClaim ) && !quizClaim.isQuizComplete() )
        {
          resumeQuiz = true;
        }
      }

      int count = 1;
      for ( Iterator qcbsdIter = quizClaimsBySubmisstionDate.iterator(); qcbsdIter.hasNext(); )
      {
        QuizClaim quizClaim = (QuizClaim)qcbsdIter.next();

        QuizHistoryValueObject valueObject = new QuizHistoryValueObject();
        valueObject.setId( quizClaim.getId() );
        valueObject.setQuizAttempt( count++ );
        valueObject.setPromotionId( quizClaim.getPromotion().getId() );
        valueObject.setPromotionName( quizClaim.getPromotion().getName() );
        valueObject.setQuizComplete( quizClaim.isQuizComplete() );

        if ( quizClaim.isQuizComplete() )
        {
          valueObject.setQuizComplete( true );
          valueObject.setDateCompleted( quizClaim.getSubmissionDate() );

          if ( quizClaim.getPass() != null && quizClaim.getPass().booleanValue() )
          {
            valueObject.setPassed( true );
            valueObject.setAwardQuantity( getAwardForQuiz( quizClaim ) );
            valueObject.setAwardTypeName( PromotionAwardsType.lookup( quizClaim.getPromotion().getAwardType().getCode() ).getName() );
          }
          else
          {
            valueObject.setPassed( false );
          }
        }
        else
        {
          valueObject.setQuizComplete( false );
        }

        quizValueAttemptedList.add( valueObject );

        valueObject.setResumeQuiz( resumeQuiz );
        valueObject.setRetakeQuiz( participantQuizClaimHistory.isAttemptsRemaining() );
      }
    }

    return quizValueAttemptedList;
  }

  /**
   * @param participantId
   * @return List of 'faked' quiz history value object that are really non-quiz related deposits
   *         such as file load. Note Discretionary Awards are EXCLUDED because there is a UI to show
   *         transaction by Discretionary Type already for the administrators. It was decided to
   *         display these items in the Quiz list (to be consisent with the pax's history view)
   */
  private List getNonQuizRelatedDeposits( Long participantId, Long promotionId )
  {
    List depositList = new ArrayList();

    // Get any deposits for this promotion not linked to any quiz taking.
    // eg. file load, EXCLUDING discretionary
    JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
    queryConstraint.setParticipantId( participantId );
    queryConstraint.setPromotionId( promotionId );// Fix 21341
    queryConstraint.setNotResultOfClaim( true );
    queryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
    // excludes Discretionary here because there is a Discretionary Trx History for the User Admin
    queryConstraint.setJournalTypesExcluded( new String[] { Journal.DISCRETIONARY } );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

    List journalList = getJournalService().getJournalList( queryConstraint, associationRequestCollection );
    for ( Iterator iter = journalList.iterator(); iter.hasNext(); )
    {
      Journal journal = (Journal)iter.next();

      // only want quiz promotion type deposits such as file load
      if ( journal.getPromotion() != null && journal.getPromotion().isQuizPromotion() )
      {
        QuizHistoryValueObject valueObject = new QuizHistoryValueObject();
        valueObject.setNonClaimRelatedDeposits( true );
        valueObject.setPromotionName( journal.getPromotion().getName() );
        valueObject.setDateCompleted( new Timestamp( journal.getTransactionDate().getTime() ) );
        if ( journal.getTransactionAmount() != null )
        {
          valueObject.setJournalType( journal.getJournalType() );
          valueObject.setAwardQuantity( journal.getTransactionAmount().intValue() );
          valueObject.setAwardTypeName( PromotionAwardsType.lookup( journal.getPromotion().getAwardType().getCode() ).getName() );
        }
        // For Fileload, should be false here
        valueObject.setDiscretionary( Journal.DISCRETIONARY.equals( journal.getJournalType() ) );
        valueObject.setSweepstakes( Journal.SWEEPSTAKES.equals( journal.getJournalType() ) );
        if ( JournalTransactionType.REVERSE.equals( journal.getTransactionType().getCode() ) )
        {
          valueObject.setReversalDescription( journal.getTransactionDescription() );
        }
        depositList.add( valueObject );
      }
    }

    return depositList;
  }

  /**
   * Returns the award amount for the specific quiz claim.
   *
   * @param quizClaim  the quiz claim whose award amount this method returns.
   * @return the award amount for the specific quiz claim.
   */
  private int getAwardForQuiz( QuizClaim quizClaim )
  {
    int awardAmount = 0;

    if ( quizClaim != null )
    {
      List journals = getJournalService().getJournalsByClaimIdAndUserId( quizClaim.getId(), quizClaim.getSubmitter().getId(), null );

      if ( journals != null && journals.size() > 0 )
      {
        Journal journal = (Journal)journals.get( 0 );
        awardAmount = journal.getTransactionAmount().intValue();
      }
    }

    return awardAmount;
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
    private static final String PROMOTION_ID = "promotionId";
    private static final String START_DATE = "startDate";
    private static final String TRANSACTION_HISTORY_TYPE = "promotionType";

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
}
