/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/GoalPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;

/**
 * GoalPayoutStrategy.
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
public interface GoalPayoutStrategy
{

  /**
   * @param paxGoal to calcluate award for
   * @return a GoalCalculationResult
   */
  public GoalCalculationResult processGoal( PaxGoal paxGoal );

  /**
   * @param goalQuestAwardSummaryParticipantMap is a map containing GoalQuestAwardSummary objects
   * @return a List of GoalQuestAwardSummary objects
   */
  public List<GoalQuestAwardSummary> summarizeResults( Map goalQuestAwardSummaryParticipantMap );

}
