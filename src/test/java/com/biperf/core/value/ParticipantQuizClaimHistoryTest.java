/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.QuizResponse;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.promotion.QuizPromotion;

import junit.framework.TestCase;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantQuizClaimHistoryTest extends TestCase
{

  /*
   * Test method for 'com.biperf.core.value.ParticipantQuizClaimHistory.getMostRecentClaim()'
   */
  public void testGetMostRecentClaim()
  {

    ParticipantQuizClaimHistory participantQuizClaimHistory = new ParticipantQuizClaimHistory();

    QuizClaim expectedMostRecentQuizClaim = new QuizClaim();
    expectedMostRecentQuizClaim.setSubmissionDate( new Date( 5 ) );
    expectedMostRecentQuizClaim.setClaimNumber( "mostrecent" );

    QuizClaim otherQuizClaim1 = new QuizClaim();
    otherQuizClaim1.setSubmissionDate( new Date( 3 ) );
    otherQuizClaim1.setClaimNumber( "1" );

    QuizClaim otherQuizClaim2 = new QuizClaim();
    otherQuizClaim2.setSubmissionDate( new Date( 4 ) );
    otherQuizClaim2.setClaimNumber( "2" );

    participantQuizClaimHistory.addQuizClaim( expectedMostRecentQuizClaim );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim1 );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim2 );

    assertEquals( expectedMostRecentQuizClaim, participantQuizClaimHistory.getMostRecentClaim() );
  }

  /*
   * Test method for 'com.biperf.core.value.ParticipantQuizClaimHistory.isRetakeable()'
   */
  public void testIsRetakeable()
  {
    // Set up basic histotry
    ParticipantQuizClaimHistory participantQuizClaimHistory = new ParticipantQuizClaimHistory();
    QuizPromotion promotion = new QuizPromotion();
    participantQuizClaimHistory.setPromotion( promotion );

    QuizClaim expectedMostRecentQuizClaim = new QuizClaim();
    expectedMostRecentQuizClaim.setSubmissionDate( new Date( 5 ) );
    expectedMostRecentQuizClaim.setClaimNumber( "mostrecent" );

    QuizClaim otherQuizClaim1 = new QuizClaim();
    otherQuizClaim1.setSubmissionDate( new Date( 3 ) );
    otherQuizClaim1.setClaimNumber( "1" );

    QuizClaim otherQuizClaim2 = new QuizClaim();
    otherQuizClaim2.setSubmissionDate( new Date( 4 ) );
    otherQuizClaim2.setClaimNumber( "2" );

    participantQuizClaimHistory.addQuizClaim( expectedMostRecentQuizClaim );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim1 );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim2 );

    assertEquals( expectedMostRecentQuizClaim, participantQuizClaimHistory.getMostRecentClaim() );

    Date yesterday = new Date( new Date().getTime() - DateUtils.MILLIS_PER_DAY );
    Date tomorrow = new Date( new Date().getTime() + DateUtils.MILLIS_PER_DAY );
    Date twoDaysAgo = new Date( new Date().getTime() - ( 2 * DateUtils.MILLIS_PER_DAY ) );

    // //Test overall success
    // set good date range
    promotion.setSubmissionStartDate( yesterday );
    promotion.setSubmissionEndDate( tomorrow );

    // set not inprogress
    expectedMostRecentQuizClaim.setQuestionCount( 1 );
    expectedMostRecentQuizClaim.addQuizResponse( new QuizResponse() );

    // set not passing
    expectedMostRecentQuizClaim.setPass( Boolean.FALSE );

    // set unlimited
    promotion.setAllowUnlimitedAttempts( true );

    assertTrue( participantQuizClaimHistory.isRetakeable( TimeZoneId.CST ) );

    // Test date range - failure
    promotion.setSubmissionStartDate( twoDaysAgo );
    promotion.setSubmissionEndDate( twoDaysAgo );

    assertFalse( participantQuizClaimHistory.isRetakeable( TimeZoneId.CST ) );

  }

  /*
   * Test method for 'com.biperf.core.value.ParticipantQuizClaimHistory.isInProgress()'
   */
  public void testIsInProgress()
  {
    // Set up basic histotry
    ParticipantQuizClaimHistory participantQuizClaimHistory = new ParticipantQuizClaimHistory();
    QuizPromotion promotion = new QuizPromotion();
    participantQuizClaimHistory.setPromotion( promotion );

    QuizClaim expectedMostRecentQuizClaim = new QuizClaim();
    expectedMostRecentQuizClaim.setSubmissionDate( new Date( 5 ) );
    expectedMostRecentQuizClaim.setClaimNumber( "mostrecent" );

    QuizClaim otherQuizClaim1 = new QuizClaim();
    otherQuizClaim1.setSubmissionDate( new Date( 3 ) );
    otherQuizClaim1.setClaimNumber( "1" );

    QuizClaim otherQuizClaim2 = new QuizClaim();
    otherQuizClaim2.setSubmissionDate( new Date( 4 ) );
    otherQuizClaim2.setClaimNumber( "2" );

    participantQuizClaimHistory.addQuizClaim( expectedMostRecentQuizClaim );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim1 );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim2 );

    assertEquals( expectedMostRecentQuizClaim, participantQuizClaimHistory.getMostRecentClaim() );

    // set not inprogress
    expectedMostRecentQuizClaim.setQuestionCount( 1 );
    expectedMostRecentQuizClaim.addQuizResponse( new QuizResponse() );

    assertFalse( participantQuizClaimHistory.isInProgress() );

    // set inprogress
    expectedMostRecentQuizClaim.setQuestionCount( 2 );
    assertTrue( participantQuizClaimHistory.isInProgress() );
  }

  /*
   * Test method for 'com.biperf.core.value.ParticipantQuizClaimHistory.isAttemptsRemaining()'
   */
  public void testIsAttemptsRemaining()
  {
    // Set up basic histotry
    ParticipantQuizClaimHistory participantQuizClaimHistory = new ParticipantQuizClaimHistory();
    QuizPromotion promotion = new QuizPromotion();
    participantQuizClaimHistory.setPromotion( promotion );

    QuizClaim expectedMostRecentQuizClaim = new QuizClaim();
    expectedMostRecentQuizClaim.setSubmissionDate( new Date( 5 ) );
    expectedMostRecentQuizClaim.setClaimNumber( "mostrecent" );

    QuizClaim otherQuizClaim1 = new QuizClaim();
    otherQuizClaim1.setSubmissionDate( new Date( 3 ) );
    otherQuizClaim1.setClaimNumber( "1" );

    QuizClaim otherQuizClaim2 = new QuizClaim();
    otherQuizClaim2.setSubmissionDate( new Date( 4 ) );
    otherQuizClaim2.setClaimNumber( "2" );

    participantQuizClaimHistory.addQuizClaim( expectedMostRecentQuizClaim );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim1 );
    participantQuizClaimHistory.addQuizClaim( otherQuizClaim2 );

    assertEquals( expectedMostRecentQuizClaim, participantQuizClaimHistory.getMostRecentClaim() );

    // set unlimited
    promotion.setAllowUnlimitedAttempts( true );
    assertTrue( participantQuizClaimHistory.isAttemptsRemaining() );

    // set limited and attempts remain
    promotion.setAllowUnlimitedAttempts( false );
    promotion.setMaximumAttempts( 4 );
    assertTrue( "test has 3 quiz , so should allow another attempt", participantQuizClaimHistory.isAttemptsRemaining() );

    // set limited and no attempts remain
    promotion.setAllowUnlimitedAttempts( false );
    promotion.setMaximumAttempts( 3 );
    assertFalse( "test has 3 quizs taken so shoudln't allow another attempts", participantQuizClaimHistory.isAttemptsRemaining() );

  }

}
