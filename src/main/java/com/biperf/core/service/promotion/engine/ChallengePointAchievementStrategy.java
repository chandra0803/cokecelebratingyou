/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ChallengePointAchievementStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import com.biperf.core.domain.goalquest.PaxGoal;

/**
 * ChallengePointPayoutStrategy.
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
 * <td>Todd</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ChallengePointAchievementStrategy
{

  /**
   * @param paxChallengePoint to calcluate award for
   * @return a ChallengePointCalculationResult
   */
  public ChallengePointCalculationResult processChallengePoint( PaxGoal paxGoal );

}
