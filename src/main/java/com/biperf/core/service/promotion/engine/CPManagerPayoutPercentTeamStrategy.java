/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/CPManagerPayoutPercentTeamStrategy.java,v $
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
 * CPManagerPayoutPercentTeamStrategy.
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
public class CPManagerPayoutPercentTeamStrategy extends AbstractChallengePointManagerPayoutStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractChallengePointPayoutStrategy#processChallengePointInternal
   *      (com.biperf.core.domain.challengePointquest.PaxChallengePoint)
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
      for ( Iterator iterNode = nodeToPax.keySet().iterator(); iterNode.hasNext(); )
      {
        long totalOverrideAward = 0;
        int totalAchieved = 0;
        ChallengepointAwardSummary managerAwardSummary = new ChallengepointAwardSummary();
        ChallengepointPaxAwardValueBean managerAward = new ChallengepointPaxAwardValueBean();
        Node node = (Node)iterNode.next();
        List userList = (List)nodeToPax.get( node );
        Participant pax = null;

        if ( (Participant)node.getNodeOwner() != null )
        {
          pax = (Participant)node.getNodeOwner();
        }

        long teamEarnings = 0;
        Integer overridePercent = promotion.getLevelOneMgrAward().intValue();
        // If one of the participant achieves the challengepoint, then
        // count manager as achieved the challengepoint.
        boolean isAchieved = false;
        if ( pax != null && overridePercent != null )
        {
          for ( Iterator paxIter = userList.iterator(); paxIter.hasNext(); )
          {
            ChallengepointPaxAwardValueBean challengepointAward = (ChallengepointPaxAwardValueBean)paxIter.next();
            // Ignore if pax is node owner and achived the goal.
            if ( challengepointAward != null && challengepointAward.getAwardEarned() != null && challengepointAward.isAchieved()
                && !challengepointAward.getParticipant().getId().equals( pax.getId() ) )
            {
              teamEarnings += challengepointAward.getAwardEarned().longValue();
            }
            if ( !paxIter.hasNext() )
            {
              managerAward.setGoalSelecter( challengepointAward.getParticipant() );
            }

          } // End of pax in the node for loop
          long managerOverrideAward = 0;
          if ( overridePercent > 0 )
          {
            // override= Total amount * ( amount entered/100)
            Double rawPayout = new Double( teamEarnings * ( overridePercent.doubleValue() / 100 ) );
            int roundPoint = Integer.parseInt( rawPayout.toString().substring( rawPayout.toString().indexOf( '.' ) + 1, rawPayout.toString().indexOf( '.' ) + 2 ) );
            if ( roundPoint > 0 && roundPoint < 5 )
            {
              // Add .5 to the raw number for the rounding to make sure all things round up.
              rawPayout = new Double( rawPayout.doubleValue() + .5 );
            }
            managerOverrideAward = Math.round( rawPayout.doubleValue() );
          }

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
          finalTotalAchieved += totalAchieved;

          totalOverrideAward += managerOverrideAward;
          finalTotalOverrideAward += totalOverrideAward;
          managerAward.setParticipant( pax );
          managerAward.setAwardEarned( new Long( managerOverrideAward ) );
          managerAward.setAwardIssued( new Long( managerOverrideAward ) );
          managerAward.setCalculatedAchievement( new BigDecimal( managerOverrideAward ) );
          managerAward.setChallengePointPromotion( promotion );
          managerAward.setAwardType( "manageroverride" );
          managerList.add( managerAward ); // Used for Extracts
          if ( isAchieved && managerAward.isLeveloneAward() )
          {
            managerAwardSummary.setLeveloneAward( true );
            managerAwardSummary.setTotalAchieved( totalAchieved );
            BigDecimal managersAward = new BigDecimal( totalOverrideAward );
            managerAwardSummary.setTotalAward( managersAward.setScale( 0, promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
            managerAwardSummary.setManagerTotals( true );
            listManagerAwardmmary.add( managerAwardSummary );
          }
        }
      }
    }

    if ( promotion != null && promotion.getLevelTwoMgrAward() != null )
    {
      for ( Iterator iterNode = nodeToPax.keySet().iterator(); iterNode.hasNext(); )
      {
        long totalOverrideAward = 0;
        int totalAchieved = 0;
        ChallengepointAwardSummary managerAwardSummary = new ChallengepointAwardSummary();
        ChallengepointPaxAwardValueBean managerAward = new ChallengepointPaxAwardValueBean();
        Node node = (Node)iterNode.next();
        List userList = (List)nodeToPax.get( node );
        Participant pax = null;
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

        long teamEarnings = 0;
        // BigDecimal overridePercent = new BigDecimal(promotion.getLevelTwoMgrAward().longValue());
        Integer overridePercent = promotion.getLevelTwoMgrAward().intValue();
        // If one of the participant achieves the challengepoint, then
        // count manager as achieved the challengepoint.
        boolean isAchieved = false;

        if ( parentNodeOwner != null && overridePercent != null )
        {
          for ( Iterator paxIter = userList.iterator(); paxIter.hasNext(); )
          {
            ChallengepointPaxAwardValueBean challengepointAward = (ChallengepointPaxAwardValueBean)paxIter.next();
            // Ignore if pax is node owner and achived the goal.
            if ( challengepointAward != null && challengepointAward.getAwardEarned() != null && challengepointAward.isAchieved()
                && ( pax == null || pax != null && !challengepointAward.getParticipant().getId().equals( pax.getId() ) ) )
            {
              teamEarnings += challengepointAward.getAwardEarned().longValue();
            }

          } // End of pax in the node for loop
          long managerOverrideAward = 0;

          if ( overridePercent > 0 )
          {
            // override= Total amount * ( amount entered/100)
            Double rawPayout = new Double( teamEarnings * ( overridePercent.doubleValue() / 100 ) );
            int roundPoint = Integer.parseInt( rawPayout.toString().substring( rawPayout.toString().indexOf( '.' ) + 1, rawPayout.toString().indexOf( '.' ) + 2 ) );
            if ( roundPoint > 0 && roundPoint < 5 )
            {
              // Add .5 to the raw number for the rounding to make sure all things round up.
              rawPayout = new Double( rawPayout.doubleValue() + .5 );
            }
            managerOverrideAward = Math.round( rawPayout.doubleValue() );

          }

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
          finalTotalAchieved += totalAchieved;

          totalOverrideAward = +managerOverrideAward;
          finalTotalOverrideAward += totalOverrideAward;
          managerAward.setParticipant( (Participant)parentNodeOwner );
          managerAward.setAwardEarned( new Long( managerOverrideAward ) );
          managerAward.setAwardIssued( new Long( managerOverrideAward ) );
          managerAward.setCalculatedAchievement( new BigDecimal( managerOverrideAward ) );
          managerAward.setChallengePointPromotion( promotion );
          managerAward.setAwardType( "manageroverride" );
          managerList.add( managerAward ); // Used for Extracts

          if ( isAchieved && !managerAward.isLeveloneAward() )
          {
            managerAwardSummary.setLeveloneAward( false );
            managerAwardSummary.setTotalAchieved( totalAchieved );
            BigDecimal managersAward = new BigDecimal( totalOverrideAward );
            managerAwardSummary.setTotalAward( managersAward.setScale( 0, promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
            managerAwardSummary.setManagerTotals( true );
            listManagerAwardmmary.add( managerAwardSummary );
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
