/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.SweepstakesAwardAmountActivity;
import com.biperf.core.domain.audit.SimplePayoutCalculationAudit;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.promotion.Promotion;

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
 * <td>zahler</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesPayoutStrategy extends AbstractPayoutStrategy
{
  private static final Log log = LogFactory.getLog( SweepstakesPayoutStrategy.class );

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
    Set payoutCalculationResults = new LinkedHashSet();

    // Should only ever be one activity for a sweepstakes process
    Long awardQuantity = new Long( 0 );

    for ( Iterator activitiesIterator = activities.iterator(); activitiesIterator.hasNext(); )
    {
      Activity activity = (Activity)activitiesIterator.next();
      if ( activity instanceof SweepstakesAwardAmountActivity )
      {
        SweepstakesAwardAmountActivity sweepstakesAwardAmountActivity = (SweepstakesAwardAmountActivity)activity;

        log.info( "** Calculating payout for sweepstakes drawing id: " + sweepstakesAwardAmountActivity.getPromotionSweepstake().getId() );

        awardQuantity = sweepstakesAwardAmountActivity.getAwardQuantity();
      }

      SimplePayoutCalculationAudit audit = new SimplePayoutCalculationAudit();
      audit.setParticipant( activity.getParticipant() );
      audit.setReason( PayoutCalculationAuditReasonType.SWEEPSTAKES_SUCCESS, new String[] { String.valueOf( awardQuantity ) } );

      PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();
      payoutCalculationResult.setCalculatedPayout( awardQuantity );
      payoutCalculationResult.addContributingActivity( activity );
      payoutCalculationResult.setPayoutCalculationAudit( audit );

      payoutCalculationResults.add( payoutCalculationResult );

    }

    return payoutCalculationResults;
  }
}
