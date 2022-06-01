/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.activity.SweepstakesAwardAmountActivity;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.service.BaseServiceTest;

/*
 * <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th> <th>Version</th>
 * <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug 29, 2005</td> <td>1.0</td> <td>created</td>
 * </tr> </table> </p>
 * 
 *
 */

public class SweepstakesPayoutStrategyTest extends BaseServiceTest
{

  public void testBasicSuccess()
  {
    Long expectedAwardQuantity = new Long( 5 );

    Promotion promotion = new QuizPromotion();
    Participant participant = new Participant();

    PromotionSweepstake promotionSweepstake = new PromotionSweepstake();
    PromotionSweepstakeWinner promotionSweepstakeWinner = new PromotionSweepstakeWinner();
    promotionSweepstakeWinner.setPromotionSweepstake( promotionSweepstake );

    SweepstakesAwardAmountActivity activity = new SweepstakesAwardAmountActivity();
    activity.setParticipant( participant );
    activity.setPromotion( promotion );
    activity.setPromotionSweepstake( promotionSweepstake );
    activity.setAwardQuantity( expectedAwardQuantity );
    activity.setGuid( "1" );
    Set activities = new LinkedHashSet();
    activities.add( activity );

    SweepstakesFacts facts = new SweepstakesFacts();
    facts.setPromotionSweepstakeWinner( promotionSweepstakeWinner );

    SweepstakesPayoutStrategy strategy = new SweepstakesPayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promotion, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Sweepstakes Payout should have been successful , got reason code: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SWEEPSTAKES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = result.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( expectedAwardQuantity, calculatedPayout );
    assertEquals( 1, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

  }

  // public void testBasicFailure()
  // {
  // //No failure possible with in Sweepstakes
  // }

}
