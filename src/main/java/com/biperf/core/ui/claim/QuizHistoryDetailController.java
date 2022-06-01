/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/QuizHistoryDetailController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ParticipantDIYQuizClaimHistory;
import com.biperf.core.value.ParticipantQuizClaimHistory;

/**
 * QuizHistoryDetailController.
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
public class QuizHistoryDetailController extends BaseController
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
    Long submitterId = null;
    String timeZoneID = "";
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
      try
      {
        submitterId = (Long)clientStateMap.get( "submitterId" );
      }
      catch( ClassCastException cce )
      {
        String s = (String)clientStateMap.get( "submitterId" );
        if ( s != null && s.length() > 0 )
        {
          submitterId = new Long( s );
        }
      }
      // START-g3Redux
      if ( clientStateMap.get( "returnURL" ) != null )
      {
        request.setAttribute( "returnURL", (String)clientStateMap.get( "returnURL" ) );
      }

      // End-g3Redux
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CURRENT_QUIZ_QUESTION ) );
    QuizClaim claim = (QuizClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

    // Get quiz history for this claim's quiz.
    Long claimUserId = null;
    if ( submitterId == null )
    {
      claimUserId = UserManager.getUserId();
      timeZoneID = UserManager.getUser().getTimeZoneId();
    }
    else
    {
      claimUserId = submitterId;
      timeZoneID = getUserService().getUserTimeZone( claimUserId );
    }
    ParticipantQuizClaimHistory participantQuizClaimHistory = getQuizHistory( associationRequestCollection, claim, claimUserId );

    QuizHistoryValueObject quizHistoryValueObject = new QuizHistoryValueObject();
    quizHistoryValueObject.setId( claim.getId() );
    quizHistoryValueObject.setQuizAttempt( 1 );
    quizHistoryValueObject.setPromotionName( claim.getPromotion().getName() );
    quizHistoryValueObject.setQuizComplete( claim.isQuizComplete() );

    if ( claim.getPromotion().isDIYQuizPromotion() )
    {
      DIYQuiz diyQuiz = (DIYQuiz)claim.getQuiz();
      if ( diyQuiz.getCertificate() != null )
      {
        QuizPromotion quizPromotion = (QuizPromotion)claim.getPromotion();
        quizPromotion.setIncludePassingQuizCertificate( true );
      }
    }

    if ( claim.isQuizComplete() )
    {
      quizHistoryValueObject.setDateCompleted( claim.getSubmissionDate() );
    }

    if ( claim.getPass() != null && claim.getPass().booleanValue() )
    {
      quizHistoryValueObject.setPassed( true );
    }
    else if ( claim.isQuizComplete() )
    {
      quizHistoryValueObject.setPassed( false );
    }

    if ( participantQuizClaimHistory.getMostRecentClaim().equals( claim ) && !claim.isQuizComplete() )
    {
      quizHistoryValueObject.setResumeQuiz( true );
    }
    else
    {
      quizHistoryValueObject.setResumeQuiz( false );
    }

    quizHistoryValueObject.setAttemptsRemaining( participantQuizClaimHistory.isAttemptsRemaining() );

    quizHistoryValueObject.setQuizAttemptsRemaining( participantQuizClaimHistory.getAttemptsRemaining() );

    if ( claim.getSubmitter().getId().equals( UserManager.getUser().getUserId() ) )
    {
      quizHistoryValueObject.setRetakeQuiz( participantQuizClaimHistory.isRetakeable( timeZoneID ) );
    }

    AssociationRequestCollection journalAssociationRequest = new AssociationRequestCollection();
    journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
    journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

    List journals = getJournalService().getJournalsByClaimIdAndUserId( claim.getId(), claim.getSubmitter().getId(), journalAssociationRequest );

    if ( !claim.isOpen() && journals != null && journals.size() > 0 )
    {
      request.setAttribute( "journal", journals.get( 0 ) );
    }

    request.setAttribute( "quizHistoryValueObject", quizHistoryValueObject );
    request.setAttribute( "claim", claim );
  }

  public static ParticipantQuizClaimHistory getQuizHistory( AssociationRequestCollection associationRequestCollection, QuizClaim claim, Long userId )
  {
    if ( claim.getPromotion().isDIYQuizPromotion() )
    {
      ParticipantDIYQuizClaimHistory participantQuizClaimHistory;
      QuizClaimQueryConstraint quizClaimQueryConstraint = new QuizClaimQueryConstraint();
      quizClaimQueryConstraint.setIncludedPromotionIds( new Long[] { claim.getPromotion().getId() } );
      quizClaimQueryConstraint.setDiyQuizId( claim.getQuiz().getId() );
      quizClaimQueryConstraint.setSubmitterId( userId );
      Map quizClaimGroupings = getClaimService().getParticipantDIYQuizClaimHistoryByQuizMap( quizClaimQueryConstraint, associationRequestCollection );
      if ( quizClaimGroupings.isEmpty() )
      {
        throw new BeaconRuntimeException( "There must be at least one quiz found if we are in the quiz detail, but wasn't" );
      }
      Map.Entry entry = (Entry)quizClaimGroupings.entrySet().iterator().next();
      participantQuizClaimHistory = (ParticipantDIYQuizClaimHistory)entry.getValue();
      return participantQuizClaimHistory;
    }
    else
    {
      ParticipantQuizClaimHistory participantQuizClaimHistory;
      QuizClaimQueryConstraint quizClaimQueryConstraint = new QuizClaimQueryConstraint();
      quizClaimQueryConstraint.setIncludedPromotionIds( new Long[] { claim.getPromotion().getId() } );
      quizClaimQueryConstraint.setQuizId( ( (QuizPromotion)claim.getPromotion() ).getQuiz().getId() );
      quizClaimQueryConstraint.setSubmitterId( userId );
      Map quizClaimGroupings = getClaimService().getParticipantQuizClaimHistoryByPromotionMap( quizClaimQueryConstraint, associationRequestCollection );
      if ( quizClaimGroupings.isEmpty() )
      {
        throw new BeaconRuntimeException( "There must be at least one quiz found if we are in the quiz detail, but wasn't" );
      }
      Map.Entry entry = (Entry)quizClaimGroupings.entrySet().iterator().next();
      participantQuizClaimHistory = (ParticipantQuizClaimHistory)entry.getValue();
      return participantQuizClaimHistory;
    }
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

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /**
   * Get the claimService from the beanFactory
   * 
   * @return ClaimService
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }
}
