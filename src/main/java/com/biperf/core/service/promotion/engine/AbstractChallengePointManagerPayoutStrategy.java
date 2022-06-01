/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/AbstractChallengePointManagerPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Map;

import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.promotion.ChallengePointPromotion;

/**
 * AbstractChallengePointManagerPayoutStrategy.
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
 * <td>Babu</td>
 * <td>Aug 13, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractChallengePointManagerPayoutStrategy implements ChallengePointManagerPayoutStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.ChallengePointPayoutStrategy#processChallengePoint(com.biperf.core.domain.challengePointquest.PaxLevel)
   * @param paxChallengePoint
   * @return
   */
  public final PendingChallengepointAwardSummary processChallengePoint( ChallengePointPromotion promotion, Map nodeToPax )
  {
    PendingChallengepointAwardSummary pendingChallengepointAwardSummary = processChallengePointInternal( promotion, nodeToPax );
    return pendingChallengepointAwardSummary;
  }

  /**
   * @param paxChallengePoint to calcluate award for
   * @return a ChallengePointCalculationResult
   */
  protected abstract PendingChallengepointAwardSummary processChallengePointInternal( ChallengePointPromotion promotion, Map nodeToPax );

}
