/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/CPManagerPayoutPerLevelStrategy.java,v $
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
import com.biperf.core.domain.user.User;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;

/**
 * CPManagerPayoutPerLevelStrategy.
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
public class CPManagerPayoutPerLevelStrategy extends AbstractChallengePointManagerPayoutStrategy
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

    long finalTotalOverrideAward = 0;
    int finalTotalAchieved = 0;

    List<ChallengepointAwardSummary> listManagerAwardmmary = new ArrayList<ChallengepointAwardSummary>();
    if ( promotion != null && promotion.getLevelOneMgrAward() != null )
    {
      ChallengepointAwardSummary managerAwardSummary = new ChallengepointAwardSummary();
      ChallengepointPaxAwardValueBean managerAward = new ChallengepointPaxAwardValueBean();
      long totalOverrideAward = 0;
      int totalAchieved = 0;
      boolean isAchieved = false;

      for ( Iterator iterNode = nodeToPax.keySet().iterator(); iterNode.hasNext(); )
      {
        Node node = (Node)iterNode.next();
        List userList = (List)nodeToPax.get( node );
        Participant pax = null;
        managerAward = new ChallengepointPaxAwardValueBean();

        if ( (Participant)node.getNodeOwner() != null )
        {
          pax = (Participant)node.getNodeOwner();
        }

        long managerOverrideAward = 0;
        long numberOfUserAchieved = 0;
        BigDecimal overridePercent = new BigDecimal( promotion.getLevelOneMgrAward().longValue() );
        // If one of the participant achieves the challengepoint, then
        // count manager as achieved the challengepoint.
        if ( pax != null && overridePercent != null )
        {
          for ( Iterator paxIter = userList.iterator(); paxIter.hasNext(); )
          {
            ChallengepointPaxAwardValueBean challengepointAward = (ChallengepointPaxAwardValueBean)paxIter.next();
            if ( challengepointAward != null && challengepointAward.isAchieved() && !challengepointAward.getParticipant().getId().equals( pax.getId() ) )
            {

              numberOfUserAchieved++;
            }
            if ( !paxIter.hasNext() )
            {
              managerAward.setGoalSelecter( challengepointAward.getParticipant() );
            }
          } // End of pax in the node for loop
          managerOverrideAward = numberOfUserAchieved * overridePercent.intValue();
          if ( managerOverrideAward > 0 )
          {
            isAchieved = true;
            managerAward.setLeveloneAward( true );
            managerAward.setAchieved( true );
          }

          if ( isAchieved )
          {
            totalAchieved++;
          }

          totalOverrideAward += managerOverrideAward;
          managerAward.setParticipant( pax );
          managerAward.setAwardEarned( new Long( managerOverrideAward ) );
          managerAward.setAwardIssued( new Long( managerOverrideAward ) );
          managerAward.setCalculatedAchievement( new BigDecimal( managerOverrideAward ) );
          managerAward.setAwardType( "manageroverride" );
          managerAward.setChallengePointPromotion( promotion );
          managerList.add( managerAward ); // Used for Extracts
          if ( !iterNode.hasNext() )
          {
            finalTotalAchieved = totalAchieved;
            finalTotalOverrideAward = totalOverrideAward;
            if ( isAchieved && managerAward.isLeveloneAward() )
            {
              managerAwardSummary.setLeveloneAward( true );
              managerAwardSummary.setManagerTotals( true );
              managerAwardSummary.setTotalAchieved( totalAchieved );
              managerAwardSummary.setTotalAward( new BigDecimal( totalOverrideAward ) );
              listManagerAwardmmary.add( managerAwardSummary );
            }
          }
        }
      }
    }

    if ( promotion != null && promotion.getLevelTwoMgrAward() != null )
    {
      ChallengepointAwardSummary managerAwardSummary = new ChallengepointAwardSummary();
      ChallengepointPaxAwardValueBean managerAward = new ChallengepointPaxAwardValueBean();
      boolean isAchieved = false;
      long totalOverrideAward = 0;
      int totalAchieved = 0;

      for ( Iterator iterNode = nodeToPax.keySet().iterator(); iterNode.hasNext(); )
      {
        Node node = (Node)iterNode.next();
        List userList = (List)nodeToPax.get( node );
        Participant pax = null;
        managerAward = new ChallengepointPaxAwardValueBean();

        if ( (Participant)node.getNodeOwner() != null )
        {
          pax = (Participant)node.getNodeOwner();
        }
        Node parentNode = node.getParentNode();
        User parentNodeOwner = null;
        if ( parentNode != null && parentNode.getNodeOwner() != null )
        {
          parentNodeOwner = parentNode.getNodeOwner();
        }

        long managerOverrideAward = 0;
        long numberOfUserAchieved = 0;
        BigDecimal overridePercent = new BigDecimal( promotion.getLevelTwoMgrAward().longValue() );
        // If one of the participant achieves the challengepoint, then
        // count manager as achieved the challengepoint.
        if ( parentNodeOwner != null && overridePercent != null )
        {
          for ( Iterator paxIter = userList.iterator(); paxIter.hasNext(); )
          {
            ChallengepointPaxAwardValueBean challengepointAward = (ChallengepointPaxAwardValueBean)paxIter.next();
            if ( challengepointAward != null && challengepointAward.isAchieved() && ( pax == null || pax != null && !challengepointAward.getParticipant().getId().equals( pax.getId() ) ) )
            {
              numberOfUserAchieved++;
            }

          } // End of pax in the node for loop
          managerOverrideAward = numberOfUserAchieved * overridePercent.intValue();

          if ( managerOverrideAward > 0 )
          {
            isAchieved = true;
            managerAward.setLeveloneAward( false );
            managerAward.setAchieved( true );
          }

          if ( isAchieved )
          {
            totalAchieved++;
          }

          totalOverrideAward += managerOverrideAward;
          managerAward.setParticipant( (Participant)parentNodeOwner );
          managerAward.setAwardEarned( new Long( managerOverrideAward ) );
          managerAward.setAwardIssued( new Long( managerOverrideAward ) );
          managerAward.setAwardType( "manageroverride" );
          managerAward.setChallengePointPromotion( promotion );
          managerAward.setCalculatedAchievement( new BigDecimal( managerOverrideAward ) );
          managerList.add( managerAward ); // Used for Extracts
          if ( !iterNode.hasNext() )
          {
            finalTotalAchieved += totalAchieved;
            finalTotalOverrideAward += totalOverrideAward;
            if ( isAchieved && !managerAward.isLeveloneAward() )
            {
              managerAwardSummary.setLeveloneAward( false );
              managerAwardSummary.setManagerTotals( true );
              managerAwardSummary.setTotalAchieved( totalAchieved );
              managerAwardSummary.setTotalAward( new BigDecimal( totalOverrideAward ) );
              listManagerAwardmmary.add( managerAwardSummary );
            }
          }
        }
      }
    }
    ChallengepointAwardSummary cpSummary = new ChallengepointAwardSummary();
    cpSummary.setTotalAchieved( finalTotalAchieved );
    cpSummary.setTotalAward( new BigDecimal( finalTotalOverrideAward ) );
    cpSummary.setManagerTotals( false );
    listManagerAwardmmary.add( cpSummary );

    pendingChallengepointAwardSummary.setManagerChallengepointAwardSummary( listManagerAwardmmary );
    pendingChallengepointAwardSummary.setManagerOverrideResults( managerList );

    return pendingChallengepointAwardSummary;
  }
}
