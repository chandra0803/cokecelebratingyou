/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ManagerOverrideGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.List;

import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/**
 * ManagerOverrideGoalStrategy.
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
public interface ManagerOverrideGoalStrategy
{

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideGoal is a PaxGoal Represent the goallevel chosen by the manager
   * @param goalCalculationResults is a Set of GoalCalculationResults that have been calculated 
   *                               for members of the managers node(s).
   * @param nodeId a Long indicating which node the managers results are being calculated for
   * @return a GoalCalculationResult
   */
  public List<GoalCalculationResult> processGoal( GoalQuestPromotion promotion, PaxGoal managerOverrideGoal, List goalCalculationResults, Long nodeId );

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideResults is a List of GoalCalculationResult for the manager overrides
   * @param paxGoalList is a List of all paxGoals for the promotion
   * @return a List of GoalQuestAwardSummary objects
   */
  public List<GoalCalculationResult> summarizeResults( GoalQuestPromotion promotion, List managerOverrideResults, List paxGoalList );

}
