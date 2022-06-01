/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.activity.StackRankActivity;
import com.biperf.core.domain.audit.SimplePayoutCalculationAudit;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.StackRankParticipant;

/*
 * StackRankPayoutStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 10, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankPayoutStrategy extends AbstractPayoutStrategy
{
  /**
   * Processes payouts for stack rank activities.
   * 
   * @param activities the activities on which the payout is based, as a <code>Set</code> of
   *          {@link StackRankActivity} objects.
   * @param promotion
   * @param payoutCalculationFacts
   * @return information about the result of calculating this payout.
   */
  public Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    Set payoutCalculationResults = new LinkedHashSet();

    // Should only ever be one activity for a stack rank process
    for ( Iterator activitiesIterator = activities.iterator(); activitiesIterator.hasNext(); )
    {
      StackRankActivity activity = (StackRankActivity)activitiesIterator.next();
      StackRankParticipant stackRankParticipant = activity.getStackRankParticipant();

      SimplePayoutCalculationAudit audit = new SimplePayoutCalculationAudit();
      audit.setParticipant( stackRankParticipant.getParticipant() );
      audit.setReason( PayoutCalculationAuditReasonType.STACK_RANK_SUCCESS, new String[] { String.valueOf( stackRankParticipant.getPayout() ) } );

      PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();
      payoutCalculationResult.setCalculatedPayout( stackRankParticipant.getPayout() );
      payoutCalculationResult.addContributingActivity( activity );
      payoutCalculationResult.setPayoutCalculationAudit( audit );

      payoutCalculationResults.add( payoutCalculationResult );

    }

    return payoutCalculationResults;
  }
}
