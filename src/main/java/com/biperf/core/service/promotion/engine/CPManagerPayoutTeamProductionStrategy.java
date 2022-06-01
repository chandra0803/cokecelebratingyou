/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/CPManagerPayoutTeamProductionStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.challengepoint.ChallengepointAwardSummary;
import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;

/**
 * CPManagerPayoutTeamProductionStrategy.
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
public class CPManagerPayoutTeamProductionStrategy extends AbstractChallengePointManagerPayoutStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractChallengePointPayoutStrategy#processChallengePointInternal
   * (com.biperf.core.domain.challengePointquest.PaxChallengePoint)
   * @param paxLevel
   * @return ChallengePointCalculationResult
   */
  protected PendingChallengepointAwardSummary processChallengePointInternal( ChallengePointPromotion promotion, Map nodeToPax )
  {
    PendingChallengepointAwardSummary pendingChallengepointAwardSummary = new PendingChallengepointAwardSummary();
    List managerList = new ArrayList();
    ChallengepointAwardSummary managerAwardSummary = new ChallengepointAwardSummary();
    long totalOverrideAward = 0;
    int totalAchieved = 0;
    for ( Iterator iterNode = nodeToPax.keySet().iterator(); iterNode.hasNext(); )
    {
      ChallengepointPaxAwardValueBean managerAward = new ChallengepointPaxAwardValueBean();
      Node node = (Node)iterNode.next();
      List userList = (List)nodeToPax.get( node );
      Participant pax = (Participant)node.getNodeOwner();
      managerAward.setParticipant( pax );

      long teamBaseline = 0;
      long teamProduction = 0;
      // If one of the participant achieves the challengepoint, then
      // count manager as achieved the challengepoint.
      boolean isAchieved = false;
      for ( Iterator paxIter = userList.iterator(); paxIter.hasNext(); )
      {
        ChallengepointPaxAwardValueBean challengepointAward = (ChallengepointPaxAwardValueBean)paxIter.next();
        if ( challengepointAward.getAwardEarned() != null )
        {
          if ( challengepointAward.getBaseQuantity() != null )
          {
            teamBaseline += challengepointAward.getBaseQuantity().longValue();
          }
          if ( challengepointAward.getResult() != null )
          {
            teamProduction += challengepointAward.getResult().longValue();
          }
        }
        if ( !paxIter.hasNext() )
        {
          managerAward.setGoalSelecter( challengepointAward.getParticipant() );
        }

      } // End of pax in the node for loop
      long managerOverrideAward = 0;
      if ( promotion.getTotalTeamProductionMeasure() != null && promotion.getTotalTeamProductionMeasure().equals( ChallengePointPromotion.SECONDARY_TOTAL_PRODUCTION_PERCENTAGE )
          && promotion.getTotalTeamProduction() != null )
      {
        long teamProductionTarget = teamBaseline * promotion.getTotalTeamProduction().intValue();
        teamProductionTarget = teamProductionTarget / 100;
        if ( teamProduction >= teamProductionTarget )
        {
          managerOverrideAward = promotion.getManagerAward().intValue();
        }
      }
      else if ( promotion.getTotalTeamProductionMeasure() != null && promotion.getTotalTeamProductionMeasure().equals( ChallengePointPromotion.SECONDARY_TOTAL_PRODUCTION_QUANTITY )
          && promotion.getTotalTeamProduction() != null )
      {
        long teamProductionTarget = teamBaseline + promotion.getTotalTeamProduction().intValue();
        if ( teamProduction >= teamProductionTarget )
        {
          managerOverrideAward = promotion.getManagerAward().intValue();
        }
      }
      if ( managerOverrideAward > 0 )
      {
        isAchieved = true;
      }
      if ( isAchieved )
      {
        totalAchieved++;
      }
      totalOverrideAward += managerOverrideAward;
      managerAward.setAwardEarned( new Long( managerOverrideAward ) );
      managerAward.setAwardIssued( new Long( managerOverrideAward ) );
      managerAward.setAwardType( "manageroverride" );
      managerList.add( managerAward ); // Used for Extracts
    }
    managerAwardSummary.setTotalAchieved( totalAchieved );
    managerAwardSummary.setTotalAward( new BigDecimal( totalOverrideAward ) );

    // TODO pendingChallengepointAwardSummary.setManagerChallengepointAwardSummary(
    // managerAwardSummary );
    pendingChallengepointAwardSummary.setManagerOverrideResults( managerList );

    return pendingChallengepointAwardSummary;
  }
}
