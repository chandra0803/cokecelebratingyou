
package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
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
public class StackrankManagerOverrideGoalStrategy extends AbstractManagerOverrideGoalStrategy
{

  private ParticipantService participantService;
  private NodeService nodeService;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.AbstractManagerOverrideGoalStrategy#processGoalInternal(com.biperf.core.domain.promotion.GoalQuestPromotion, java.util.Set, java.util.Set)
   * @param promotion
   * @param managerOverrideGoal
   * @param goalCalculationResults
   * @return
   */
  protected List<GoalCalculationResult> processGoalInternal( GoalQuestPromotion promotion, PaxGoal managerOverrideGoal, List goalCalculationResults, Long nodeId )
  {
    int numberOfAchievers = 0;
    List<GoalCalculationResult> managerOverrides = new ArrayList<GoalCalculationResult>();
    GoalCalculationResult goalCalculationResult = new GoalCalculationResult();
    Set participantSet = participantService.getAllPaxesInPromotionAndNode( promotion.getId(), nodeId );
    double numberOfUsersInNode = 0;

    for ( Iterator paxIter = participantSet.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( !pax.isOwner() )
      {
        numberOfUsersInNode++;
      }
    }

    Map payoutsByGoal = new HashMap();
    for ( Iterator goalResultIter = goalCalculationResults.iterator(); goalResultIter.hasNext(); )
    {
      GoalCalculationResult currentGoalCalculationResult = (GoalCalculationResult)goalResultIter.next();
      if ( currentGoalCalculationResult.isAchieved() )
      {
        GoalQuestAwardSummary levelResult = (GoalQuestAwardSummary)payoutsByGoal.get( currentGoalCalculationResult.getGoalLevel() );
        if ( levelResult == null )
        {
          levelResult = new GoalQuestAwardSummary();
          payoutsByGoal.put( currentGoalCalculationResult.getGoalLevel(), levelResult );
        }
        levelResult.incrementTotalAchieved();

        if ( !currentGoalCalculationResult.getNodeRole().getCode().equals( HierarchyRoleType.OWNER ) )
        {
          numberOfAchievers++;
        }

        if ( numberOfAchievers > 0 )
        {
          goalCalculationResult.setAchieved( true );
        }

        if ( !goalResultIter.hasNext() )
        {
          double percentageAchieved = numberOfAchievers / numberOfUsersInNode * 100.0;
          BigDecimal percentageAchievedDecimal = new BigDecimal( percentageAchieved ).setScale( promotion.getAchievementPrecision().getPrecision(),
                                                                                                promotion.getRoundingMethod().getBigDecimalRoundingMode() );
          goalCalculationResult.setPercentAchieved( percentageAchievedDecimal );
        }
      }
      goalCalculationResult.setNodeRole( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
      goalCalculationResult.setNodeOwner( currentGoalCalculationResult.getNodeOwner() );
      goalCalculationResult.setOwner( true );
      goalCalculationResult.setReciever( currentGoalCalculationResult.getNodeOwner() );
    }

    goalCalculationResult.setTotalPerformance( new BigDecimal( numberOfAchievers ) );
    managerOverrides.add( goalCalculationResult );
    return managerOverrides;
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

    List results = new ArrayList( managerOverrideSummaries.size() + 1 );
    if ( managerOverrideResults != null )
    {
      for ( Iterator goalLevelIter = promotion.getManagerOverrideGoalLevels().iterator(); goalLevelIter.hasNext(); )
      {
        ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)goalLevelIter.next();
        Long totalAward = new Long( 0 );
        int totalSelected = 0;
        int totalAchieved = 0;
        for ( Iterator managerOverrideResultIter = managerOverrideResults.iterator(); managerOverrideResultIter.hasNext(); )
        {
          GoalCalculationResult managerOverrideResult = (GoalCalculationResult)managerOverrideResultIter.next();
          if ( managerOverrideResult.isAchieved() )
          {
            if ( managerOverrideResult.getTotalAward().compareTo( new BigDecimal( managerOverrideGoalLevel.getMoAwards() ) ) == 0 )
            {
              totalAchieved++;
              totalAward += managerOverrideGoalLevel.getMoAwards();
              totalSelected += managerOverrideResult.getTotalPerformance().intValue();
              goalQuestAwardSummary.incrementTotalAchieved();
              goalQuestAwardSummary.incrementTotalAward( new BigDecimal( managerOverrideGoalLevel.getMoAwards() ) );
            }
          }
        }
        GoalQuestAwardSummary finalGoalQuestAwardSummary = new GoalQuestAwardSummary();
        finalGoalQuestAwardSummary.setTotalAchieved( totalAchieved );
        finalGoalQuestAwardSummary.setTotalAward( new BigDecimal( totalAward ) );
        finalGoalQuestAwardSummary.setTotalSelected( totalSelected );
        finalGoalQuestAwardSummary.setManagerTotals( true );
        finalGoalQuestAwardSummary.setStartRank( managerOverrideGoalLevel.getMoStartRank() );
        finalGoalQuestAwardSummary.setEndRank( managerOverrideGoalLevel.getMoEndRank() );
        results.add( finalGoalQuestAwardSummary );
      }
    }
    results.add( goalQuestAwardSummary );
    return results;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

}
