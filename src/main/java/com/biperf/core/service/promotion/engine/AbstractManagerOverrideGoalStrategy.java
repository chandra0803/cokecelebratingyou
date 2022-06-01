/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/AbstractManagerOverrideGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/**
 * AbstractManagerOverrideGoalStrategy.
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
 * <td>meadows</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractManagerOverrideGoalStrategy implements ManagerOverrideGoalStrategy
{

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategy#processGoal(com.biperf.core.domain.promotion.GoalQuestPromotion, java.util.Set, java.util.Set)
   * @param promotion
   * @param managerOverrideGoal
   * @param goalCalculationResults
   * @return
   */
  public List<GoalCalculationResult> processGoal( GoalQuestPromotion promotion, PaxGoal managerOverrideGoal, List goalCalculationResults, Long nodeId )
  {
    List<GoalCalculationResult> goalCalculationResult = processGoalInternal( promotion, managerOverrideGoal, goalCalculationResults, nodeId );
    return goalCalculationResult;
  }

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideGoal is a PaxGoal (of manager override type) representing the goal selected by the manager
   * @param goalCalculationResults is a List of GoalCalculationResults that have been calculated 
   *                               for members of the managers node(s).
   * @return a GoalCalculationResult
   */
  protected abstract List<GoalCalculationResult> processGoalInternal( GoalQuestPromotion promotion, PaxGoal managerOverrideGoal, List goalCalculationResults, Long nodeId );

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideResults is a List of GoalCalculationResult for the manager overrides
   * @param paxGoalList is a List of all paxGoals for the promotion 
   * @return a List of GoalQuestAwardSummary objects
   */
  public List summarizeResults( GoalQuestPromotion promotion, List managerOverrideResults, List paxGoalList )
  {
    return summarizeResultsInternal( promotion, managerOverrideResults, paxGoalList );
  }

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideResults is a List of GoalCalculationResult for the manager overrides
   * @param paxGoalList is a List of all paxGoals for the promotion   * 
   * @return a List of GoalQuestAwardSummary objects
   */
  public abstract List summarizeResultsInternal( GoalQuestPromotion promotion, List managerOverrideResults, List paxGoalList );

  protected Map getEmptyManagerOverrideMap( GoalQuestPromotion promotion )
  {
    Map managerOverrides = new TreeMap();
    Set goalLevels = null;
    if ( promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) )
    {
      goalLevels = promotion.getGoalLevels();
    }
    else
    {
      goalLevels = promotion.getManagerOverrideGoalLevels();
    }
    if ( goalLevels != null )
    {
      for ( Iterator goalLevelIterator = goalLevels.iterator(); goalLevelIterator.hasNext(); )
      {
        AbstractGoalLevel currentGoalLevel = (AbstractGoalLevel)goalLevelIterator.next();
        GoalQuestAwardSummary goalQuestAwardSummary = new GoalQuestAwardSummary();
        goalQuestAwardSummary.setGoalLevel( currentGoalLevel );
        goalQuestAwardSummary.setManagerOverrideGoalQuestAwardSummary( true );
        goalQuestAwardSummary.setTotalAward( new BigDecimal( "0" ) );
        managerOverrides.put( currentGoalLevel.getId(), goalQuestAwardSummary );
      }
    }
    return managerOverrides;
  }

}
