/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ActualTeamAchievementManagerOverrideGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;

/**
 * PercentTeamManagerOverrideGoalStrategy.
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
public class ActualTeamAchievementManagerOverrideGoalStrategy extends AbstractManagerOverrideGoalStrategy
{

  private ParticipantService participantService;
  private NodeService nodeService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractManagerOverrideGoalStrategy#processGoalInternal(com.biperf.core.domain.promotion.GoalQuestPromotion,
   *      java.util.Set, java.util.Set)
   * @param promotion
   * @param managerOverrideGoal
   * @param goalCalculationResults
   * @return
   */
  protected List<GoalCalculationResult> processGoalInternal( GoalQuestPromotion promotion, PaxGoal managerOverrideGoal, List goalCalculationResults, Long nodeId )
  {
    GoalCalculationResult goalCalculationResult = new GoalCalculationResult();
    Set participantSet = participantService.getAllPaxesInPromotionAndNode( promotion.getId(), nodeId );
    double numberOfUsersInNode = participantSet.size();
    double numberOfUserAchieved = 0;
    for ( Iterator goalResultIter = goalCalculationResults.iterator(); goalResultIter.hasNext(); )
    {
      GoalCalculationResult currentGoalCalculationResult = (GoalCalculationResult)goalResultIter.next();
      if ( currentGoalCalculationResult.isAchieved() )
      {
        numberOfUserAchieved++;
      }
    }
    double percentageAchieved = numberOfUserAchieved / numberOfUsersInNode * 100.0;
    BigDecimal percentageAchievedDecimal = new BigDecimal( percentageAchieved ).setScale( promotion.getAchievementPrecision().getPrecision(),
                                                                                          promotion.getRoundingMethod().getBigDecimalRoundingMode() );
    goalCalculationResult.setTotalPerformance( percentageAchievedDecimal );
    goalCalculationResult.setCalculatedPayout( new BigDecimal( 0 ) );
    for ( Iterator goalLevelIter = promotion.getManagerOverrideGoalLevels().iterator(); goalLevelIter.hasNext(); )
    {
      ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)goalLevelIter.next();
      if ( percentageAchievedDecimal.compareTo( managerOverrideGoalLevel.getTeamAchievementPercent() ) >= 0 )
      {
        if ( managerOverrideGoalLevel.getManagerAward().compareTo( goalCalculationResult.getCalculatedPayout() ) > 0 )
        {
          if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
          {
            goalCalculationResult.setCalculatedPayout( new BigDecimal( managerOverrideGoalLevel.getManagerAward().longValue() ) );
          }
          else
          {
            goalCalculationResult.setCalculatedPayout( managerOverrideGoalLevel.getManagerAward() );
          }
          goalCalculationResult.setGoalLevel( managerOverrideGoalLevel );
          goalCalculationResult.setAchieved( true );
        }
      }
    }

    return null;// goalCalculationResult;
  }

  /**
   * @param promotion is a GoalQuestPromotion that goals are being calculated for
   * @param managerOverrideResults is a List of GoalCalculationResult for the manager overrides
   * @param paxGoalList is a List of all paxGoals for the promotion
   * @return a List of GoalQuestAwardSummary objects
   */
  public List summarizeResultsInternal( GoalQuestPromotion promotion, List managerOverrideResults, List paxGoalList )
  {
    Map managerOverrideSummaries = getEmptyManagerOverrideMap( promotion );
    GoalQuestAwardSummary goalQuestAwardSummary = new GoalQuestAwardSummary();
    goalQuestAwardSummary.setManagerTotals( true );
    if ( managerOverrideResults != null )
    {
      for ( Iterator managerOverrideResultIter = managerOverrideResults.iterator(); managerOverrideResultIter.hasNext(); )
      {
        GoalCalculationResult managerOverrideResult = (GoalCalculationResult)managerOverrideResultIter.next();
        if ( managerOverrideResult.isAchieved() )
        {
          Long goalLevelId = managerOverrideResult.getGoalLevel().getId();
          GoalQuestAwardSummary managerOverrideSummary = (GoalQuestAwardSummary)managerOverrideSummaries.get( goalLevelId );
          if ( managerOverrideSummary != null )
          {
            if ( managerOverrideResult.isAchieved() )
            {
              managerOverrideSummary.incrementTotalAchieved();
              managerOverrideSummary.incrementTotalAward( managerOverrideResult.getCalculatedPayout() );
              goalQuestAwardSummary.incrementTotalAchieved();
              goalQuestAwardSummary.incrementTotalAward( managerOverrideResult.getCalculatedPayout() );
            }
          }
        }
      }
    }

    List results = new ArrayList( managerOverrideSummaries.size() + 1 );
    results.addAll( managerOverrideSummaries.values() );
    results.add( goalQuestAwardSummary );
    return results;

  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}
