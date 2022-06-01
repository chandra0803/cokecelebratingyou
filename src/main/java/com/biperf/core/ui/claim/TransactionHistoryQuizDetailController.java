/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/TransactionHistoryQuizDetailController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ParticipantQuizClaimHistory;

/**
 * TransactionHistoryQuizDetailController.
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
public class TransactionHistoryQuizDetailController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long claimId = null;
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
      claimId = (Long)clientStateMap.get( "id" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Boolean resumeQuiz = new Boolean( request.getParameter( "resume" ) );
    Journal journal = null;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CURRENT_QUIZ_QUESTION ) );
    QuizClaim quizClaim = (QuizClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

    Participant quizSubmitter = quizClaim.getSubmitter();
    QuizPromotion quizPromotion = (QuizPromotion)quizClaim.getPromotion();

    // Get quiz history for this claim's quiz.
    ParticipantQuizClaimHistory participantQuizClaimHistory = QuizHistoryDetailController.getQuizHistory( associationRequestCollection, quizClaim, quizSubmitter.getId() );

    QuizHistoryValueObject quizHistoryValueObject = new QuizHistoryValueObject();
    quizHistoryValueObject.setId( quizClaim.getId() );
    quizHistoryValueObject.setQuizAttempt( 1 );
    quizHistoryValueObject.setPromotionName( quizClaim.getPromotion().getName() );
    quizHistoryValueObject.setQuizComplete( quizClaim.isQuizComplete() );

    if ( quizClaim.isQuizComplete() )
    {
      quizHistoryValueObject.setDateCompleted( quizClaim.getSubmissionDate() );
    }

    if ( quizClaim.getPass().booleanValue() )
    {
      quizHistoryValueObject.setPassed( true );

      List journalList = getJournalService().getJournalsByClaimIdAndUserId( claimId, quizSubmitter.getId(), null );
      // Fix 21289 display quiz details in transaction history.
      if ( journalList.size() > 0 )
      {
        journal = (Journal)journalList.get( 0 );
      }
      else if ( journalList.size() < 1 )
      {
        journal = null;
      }

    }
    else if ( quizClaim.isQuizComplete() )
    {
      quizHistoryValueObject.setPassed( false );
    }

    if ( participantQuizClaimHistory.getMostRecentClaim().equals( quizClaim ) && !quizClaim.isQuizComplete() )
    {
      quizHistoryValueObject.setResumeQuiz( true );
    }
    else
    {
      quizHistoryValueObject.setResumeQuiz( false );
    }

    if ( quizPromotion.isAllowUnlimitedAttempts() )
    {
      quizHistoryValueObject.setUnlimitedAttempts( true );
    }
    else
    {
      quizHistoryValueObject.setUnlimitedAttempts( false );
    }

    quizHistoryValueObject.setAttemptsRemaining( participantQuizClaimHistory.isAttemptsRemaining() );
    quizHistoryValueObject.setQuizAttemptsRemaining( participantQuizClaimHistory.getAttemptsRemaining() );
    quizHistoryValueObject.setRetakeQuiz( participantQuizClaimHistory.isAttemptsRemaining() );

    request.setAttribute( "journal", journal );
    request.setAttribute( "quizSubmitter", quizSubmitter );
    request.setAttribute( "quizPromotion", quizPromotion );
    request.setAttribute( "quizHistoryValueObject", quizHistoryValueObject );
    request.setAttribute( "resumeQuiz", resumeQuiz );
    request.setAttribute( "claim", quizClaim );
  }

  /**
   * Returns a reference to the Claim service.
   * 
   * @return a reference to the Claim service.
   */
  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Claim service.
   * 
   * @return a reference to the Claim service.
   */
  private static JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }
}
