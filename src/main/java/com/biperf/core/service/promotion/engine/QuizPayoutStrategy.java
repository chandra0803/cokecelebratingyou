/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/QuizPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.audit.ClaimBasedPayoutCalculationAudit;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;

/**
 * RecognitionPayoutStrategy.
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
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizPayoutStrategy extends AbstractPayoutStrategy
{
  private static final Log log = LogFactory.getLog( QuizPayoutStrategy.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractPayoutStrategy#processActivitiesInternal(java.util.Set,
   *      com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.service.promotion.engine.PayoutCalculationFacts)
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set
   */
  public Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {

    QuizFacts facts = (QuizFacts)payoutCalculationFacts;

    log.info( "** Calculating payout for quiz claim id: " + facts.getClaim().getId() );

    Set payoutCalculationResults = new LinkedHashSet();

    QuizPromotion quizPromotion = (QuizPromotion)promotion;

    for ( Iterator activitiesIterator = activities.iterator(); activitiesIterator.hasNext(); )
    {
      QuizActivity activity = (QuizActivity)activitiesIterator.next();

      PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();

      if ( quizPromotion.isAwardActive() )
      {
        Participant participant = facts.getParticipant();

        ClaimBasedPayoutCalculationAudit audit = new ClaimBasedPayoutCalculationAudit();
        audit.setClaim( facts.getClaim() );
        audit.setParticipant( participant );

        QuizClaim claim = (QuizClaim)activity.getClaim();
        Long awardQuantity;
        if ( BooleanUtils.isTrue( claim.getPass() ) )
        {
          // Quiz passed
          awardQuantity = new Long( quizPromotion.getAwardAmount().longValue() );

          audit.setReason( PayoutCalculationAuditReasonType.QUIZ_SUCCESS, new String[] { awardQuantity.toString() } );
          log.info( "Quiz payout: " + awardQuantity + " to: " + participant );

        }
        else
        {
          // quiz not passed
          awardQuantity = new Long( 0 );

          audit.setReason( PayoutCalculationAuditReasonType.QUIZ_FAILURE_DID_NOT_PASS, new String[] {} );
        }

        payoutCalculationResult.setCalculatedPayout( awardQuantity );
        payoutCalculationResult.addContributingActivity( activity );
        payoutCalculationResult.setPayoutCalculationAudit( audit );

        payoutCalculationResults.add( payoutCalculationResult );
      }

    }

    return payoutCalculationResults;
  }

}
