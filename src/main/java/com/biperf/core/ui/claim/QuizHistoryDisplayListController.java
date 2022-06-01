/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/QuizHistoryDisplayListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.QuizPromotionQueryConstraint;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ParticipantQuizClaimHistory;

//TODO: This controller class is not used in G5. But keeping this class since it has reference for G4. We may have to remove this class once the other references removed
/**
 * QuizHistoryDisplayListController.
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
public class QuizHistoryDisplayListController extends BaseController
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
    Long promotionId = null;
    Date startDate = null;
    Date endDate = null;
    List promotionList = null;
    String timeZoneID = "";

    if ( request.getAttribute( "promotionId" ) != null && !request.getAttribute( "promotionId" ).equals( "" ) )
    {
      promotionId = new Long( (String)request.getAttribute( "promotionId" ) );
    }

    startDate = (Date)request.getAttribute( "startDate" );
    endDate = DateUtils.toEndDate( (Date)request.getAttribute( "endDate" ) ); // get the latest time
                                                                              // on the specified
                                                                              // date

    QuizPromotionQueryConstraint quizPromotionQueryConstraint = new QuizPromotionQueryConstraint();
    promotionList = getPromotionService().getPromotionList( quizPromotionQueryConstraint );

    QuizClaimQueryConstraint quizClaimQueryConstraint = new QuizClaimQueryConstraint();
    quizClaimQueryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    quizClaimQueryConstraint.setStartDate( startDate );
    quizClaimQueryConstraint.setEndDate( endDate );
    // bug fix # 34779
    // quizClaimQueryConstraint.setPass(Boolean.TRUE);

    if ( request.getAttribute( "submitterId" ) != null )
    {
      quizClaimQueryConstraint.setSubmitterId( (Long)request.getAttribute( "submitterId" ) );
      timeZoneID = getUserService().getUserTimeZone( (Long)request.getAttribute( "submitterId" ) );
    }
    else
    {
      quizClaimQueryConstraint.setSubmitterId( UserManager.getUser().getUserId() );
      timeZoneID = UserManager.getUser().getTimeZoneId();
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );

    Map quizClaimGroupings = getClaimService().getParticipantQuizClaimHistoryByPromotionMap( quizClaimQueryConstraint, associationRequestCollection );

    Set keySet = quizClaimGroupings.keySet();
    ArrayList quizValueAttemptedList = new ArrayList();

    for ( Iterator quizClaimsIter = keySet.iterator(); quizClaimsIter.hasNext(); )
    {

      ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)quizClaimGroupings.get( quizClaimsIter.next() );

      ArrayList quizAttemptedList = (ArrayList)participantQuizClaimHistory.getQuizClaimsBySubmissionDate();

      boolean resumeQuiz = false;

      for ( Iterator quizAttemptedListIter = quizAttemptedList.iterator(); quizAttemptedListIter.hasNext(); )
      {

        QuizClaim quizClaim = (QuizClaim)quizAttemptedListIter.next();

        if ( participantQuizClaimHistory.getMostRecentClaim().equals( quizClaim ) && !quizClaim.isQuizComplete() && DateUtils
            .isDateBetween( new Date(), participantQuizClaimHistory.getPromotion().getSubmissionStartDate(), participantQuizClaimHistory.getPromotion().getSubmissionEndDate(), timeZoneID ) )
        {
          resumeQuiz = true;
        }
      }

      int count = 0;
      Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
      for ( Iterator quizAttemptedListIter = quizAttemptedList.iterator(); quizAttemptedListIter.hasNext(); )
      {

        count++;

        QuizClaim quizClaim = (QuizClaim)quizAttemptedListIter.next();
        QuizHistoryValueObject quizHistoryValueObject = new QuizHistoryValueObject();
        quizHistoryValueObject.setId( quizClaim.getId() );
        quizHistoryValueObject.setQuizAttempt( count );
        quizHistoryValueObject.setPromotionId( quizClaim.getPromotion().getId() );
        quizHistoryValueObject.setPromotionName( quizClaim.getPromotion().getName() );
        quizHistoryValueObject.setQuizComplete( quizClaim.isQuizComplete() );

        // Need to display Award amount if there is one on the journal for this quiz
        quizHistoryValueObject.setAwardQuantity( getAwardForQuiz( quizClaim ) );
        quizHistoryValueObject.setAwardTypeName( PromotionAwardsType.lookup( quizClaim.getPromotion().getAwardType().getCode() ).getName() );

        if ( quizClaim.isQuizComplete() )
        {
          quizHistoryValueObject.setDateCompleted( quizClaim.getSubmissionDate() );
        }

        if ( quizClaim.isQuizComplete() )
        {
          quizHistoryValueObject.setQuizComplete( true );
          if ( quizClaim.getPass() != null && quizClaim.getPass().booleanValue() )
          {
            quizHistoryValueObject.setPassed( true );
          }
          else
          {
            quizHistoryValueObject.setPassed( false );
          }
        }
        else
        {
          quizHistoryValueObject.setQuizComplete( false );
        }

        quizHistoryValueObject.setResumeQuiz( resumeQuiz );

        quizHistoryValueObject.setRetakeQuiz( participantQuizClaimHistory.isRetakeable( timeZoneID ) );

        quizValueAttemptedList.add( quizHistoryValueObject );
      }
    }

    // Add the non-quiz related Deposits eg. file load, discretionary, etc to the quiz list.
    quizValueAttemptedList.addAll( getNonQuizRelatedDeposits( UserManager.getUser().getUserId(), promotionId ) );
    request.setAttribute( "quizValueAttemptedList", quizValueAttemptedList );
    request.setAttribute( "startDate", DateUtils.toDisplayString( startDate ) );
    request.setAttribute( "endDate", DateUtils.toDisplayString( endDate ) );
    request.setAttribute( "promotionList", promotionList );
    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

  }

  /**
   * @param participantId
   * @return List of 'faked' quiz history value object that are really non-quiz related deposits
   *         such as file load, discretionary awards. It was decided to display these items in the
   *         Quiz list in order to show the Pax their award
   */
  private List getNonQuizRelatedDeposits( Long participantId, Long promotionId )
  {
    List depositList = new ArrayList();

    // Get any deposits for this promotion not linked to any quiz taking.
    // eg. file load, discretionary
    JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
    journalQueryConstraint.setParticipantId( participantId );
    journalQueryConstraint.setNotResultOfClaim( true );
    journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
    journalQueryConstraint.setPromotionId( promotionId ); // Fix 21341

    // need to do the association to read the Promotion obj from Journal obj in jsp
    AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
    journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

    List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );
    for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
    {
      Journal journal = (Journal)journalIter.next();
      // only want quiz promotion type deposits
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
          valueObject.setAwardTypeName( journal.getPromotion().getAwardType().getName() );
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
   * @param quizClaim
   * @return the award amount posted on the journal for a specific quiz, maybe zero, if quiz was not
   *         passed/completed etc.
   */
  private int getAwardForQuiz( QuizClaim quizClaim )
  {

    int awardAmount = 0;

    AssociationRequestCollection journalAssociationRequest = new AssociationRequestCollection();
    journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
    journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

    List journals = getJournalService().getJournalsByClaimIdAndUserId( quizClaim.getId(), quizClaim.getSubmitter().getId(), journalAssociationRequest );

    if ( !quizClaim.isOpen() && journals != null && journals.size() > 0 )
    {
      Journal journal = (Journal)journals.get( 0 );
      awardAmount = journal.getTransactionAmount().intValue();
    }

    return awardAmount;
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /**
   * Get the JournalService from the beanLocator.
   * 
   * @return JournalService
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ClaimService from the beanLocator.
   * 
   * @return ClaimService
   */
  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

}
