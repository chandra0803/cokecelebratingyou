/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ChallengePointManagerPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Map;

import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.promotion.ChallengePointPromotion;

/**
 * ChallengePointManagerPayoutStrategy.
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
 * <td>Prabu</td>
 * <td>Aug 13, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ChallengePointManagerPayoutStrategy
{

  /**
   * @param paxChallengePoint to calcluate award for
   * @return a ChallengePointCalculationResult
   */
  public PendingChallengepointAwardSummary processChallengePoint( ChallengePointPromotion promotion, Map nodeToPax );

}
