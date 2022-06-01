/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/PerAchieverPartnerGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.List;

/**
 * PerAchieverPartnerGoalStrategy.
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
 * <td>gadapa</td>
 * <td>Apr 1, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PerAchieverPartnerGoalStrategy extends AbstractPartnerGoalStrategy
{
  protected GoalCalculationResult processGoalInternal( GoalCalculationResult paxGoalResult, List partnerPaxGoalResults )
  {
    GoalCalculationResult goalCalculationResult = new GoalCalculationResult();

    // Process only if PAX for the partner has a Goal Result, and has achieved the goal
    if ( null != paxGoalResult && paxGoalResult.isAchieved() )
    {
      // Partner achieved a payout
      goalCalculationResult.setAchieved( true );
    }

    return goalCalculationResult;
  }
}
