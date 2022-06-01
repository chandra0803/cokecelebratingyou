/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.service.BaseServiceTest;

/*
 * ManagerOverridePayoutStrategyTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 29, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class QuizPayoutStrategyTest extends BaseServiceTest
{

  protected void setUp() throws Exception
  {
    super.setUp();

  }

  public void testWithAwardSuccess()
  {
    Long expectedAwardAmount = new Long( 20 );

    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( "TestPromo" );
    promotion.setAwardActive( true );
    promotion.setAwardAmount( expectedAwardAmount );

    QuizFacts facts = new QuizFacts( new QuizClaim(), new Participant() );

    QuizClaim claim = new QuizClaim();
    claim.setPass( Boolean.TRUE );
    claim.setPromotion( promotion );

    Set activities = new LinkedHashSet();
    QuizActivity quizActivity = new QuizActivity();
    quizActivity.setGuid( "1" );
    quizActivity.setClaim( claim );

    activities.add( quizActivity );

    QuizPayoutStrategy strategy = new QuizPayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promotion, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Payout unsuccessful with reason: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.QUIZ_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = result.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( expectedAwardAmount.longValue(), calculatedPayout.longValue() );
    assertEquals( 1, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

  }

  public void testWithNoAwardSuccess()
  {
    Long expectedAwardAmount = new Long( 20 );

    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( "TestPromo" );
    promotion.setAwardActive( false );
    promotion.setAwardAmount( expectedAwardAmount );

    QuizFacts facts = new QuizFacts( new QuizClaim(), new Participant() );

    QuizClaim claim = new QuizClaim();
    claim.setPass( Boolean.TRUE );
    claim.setPromotion( promotion );

    Set activities = new LinkedHashSet();
    QuizActivity quizActivity = new QuizActivity();
    quizActivity.setGuid( "1" );
    quizActivity.setClaim( claim );

    activities.add( quizActivity );

    QuizPayoutStrategy strategy = new QuizPayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promotion, facts );

    // No payout result since no award for this promo
    assertEquals( payoutCalculationResults.size(), 0 );

  }

  public void testWithAwardFailureDidNotPass()
  {
    Long expectedAwardAmount = new Long( 20 );

    QuizPromotion promotion = PromotionDAOImplTest.buildQuizPromotion( "TestPromo" );
    promotion.setAwardActive( true );
    promotion.setAwardAmount( expectedAwardAmount );

    QuizFacts facts = new QuizFacts( new QuizClaim(), new Participant() );

    QuizClaim claim = new QuizClaim();
    claim.setPass( Boolean.FALSE );
    claim.setPromotion( promotion );

    Set activities = new LinkedHashSet();
    QuizActivity quizActivity = new QuizActivity();
    quizActivity.setGuid( "1" );
    quizActivity.setClaim( claim );

    activities.add( quizActivity );

    QuizPayoutStrategy strategy = new QuizPayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promotion, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Payout incorrectly successful with reason: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.QUIZ_FAILURE_DID_NOT_PASS );

    // assert results
    assertEquals( 1, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

    // Confirm no payout
    Long calculatedPayout = result.getCalculatedPayout();
    assertEquals( new Long( 0 ), calculatedPayout );

  }

}
