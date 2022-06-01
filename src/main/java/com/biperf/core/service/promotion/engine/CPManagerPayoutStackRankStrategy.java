/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/CPManagerPayoutStackRankStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.challengepoint.ChallengepointAwardSummary;
import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.service.participant.ParticipantService;
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
public class CPManagerPayoutStackRankStrategy extends AbstractChallengePointManagerPayoutStrategy
{

  private ParticipantService participantService;

  protected PendingChallengepointAwardSummary processChallengePointInternal( ChallengePointPromotion promotion, Map nodeToPax )
  {
    PendingChallengepointAwardSummary pendingChallengepointAwardSummary = new PendingChallengepointAwardSummary();

    List managerList = new ArrayList();
    List<ChallengepointAwardSummary> listManagerAwardSummary = new ArrayList<ChallengepointAwardSummary>();
    ChallengepointAwardSummary managerAwardSummary = new ChallengepointAwardSummary();
    int totalAchieved = 0;
    for ( Iterator iterNode = nodeToPax.keySet().iterator(); iterNode.hasNext(); )
    {
      ChallengepointPaxAwardValueBean managerAward = new ChallengepointPaxAwardValueBean();
      Node node = (Node)iterNode.next();
      List userList = (List)nodeToPax.get( node );
      Participant pax = (Participant)node.getNodeOwner();

      Set participantSet = participantService.getAllPaxesInPromotionAndNode( promotion.getId(), node.getId() );
      double numberOfUsersInNode = 0;

      for ( Iterator paxIter = participantSet.iterator(); paxIter.hasNext(); )
      {
        Participant participant = (Participant)paxIter.next();
        if ( !participant.isOwner() )
        {
          numberOfUsersInNode++;
        }
      }

      long teamEarnings = 0;
      int numberOfAchievers = 0;
      // If one of the participant achieves the challengepoint, then
      // count manager as achieved the challengepoint.
      boolean isAchieved = false;
      for ( Iterator paxIter = userList.iterator(); paxIter.hasNext(); )
      {
        ChallengepointPaxAwardValueBean challengepointAward = (ChallengepointPaxAwardValueBean)paxIter.next();
        if ( challengepointAward.getAwardEarned() != null )
        {
          teamEarnings += challengepointAward.getAwardEarned().longValue();
        }
        if ( pax != null && challengepointAward.isAchieved() && !challengepointAward.getParticipant().getId().equals( pax.getId() ) )
        {
          numberOfAchievers++;
        }

        if ( !paxIter.hasNext() )
        {
          double percentageAchieved = numberOfAchievers / numberOfUsersInNode * 100.0;
          BigDecimal percentageAchievedDecimal = new BigDecimal( percentageAchieved ).setScale( promotion.getAchievementPrecision().getPrecision(),
                                                                                                promotion.getRoundingMethod().getBigDecimalRoundingMode() );
          managerAward.setPercentAchieved( percentageAchievedDecimal );
          managerAward.setGoalSelecter( challengepointAward.getParticipant() );
          managerAward.setChallengePointPromotion( promotion );
          if ( numberOfAchievers > 0 )
          {
            isAchieved = true;
          }
          if ( isAchieved )
          {
            totalAchieved++;
          }
        }
        managerAward.setAchieved( isAchieved );
      } // End of pax in the node for loop

      managerAward.setParticipant( pax );
      managerAward.setAwardType( "manageroverride" );
      managerList.add( managerAward ); // Used for Extracts
    }

    managerList = computeFinalManagerOverrideList( managerList, promotion );
    pendingChallengepointAwardSummary.setManagerOverrideResults( managerList );

    if ( managerList != null )
    {
      for ( Iterator goalLevelIter = promotion.getManagerOverrideGoalLevels().iterator(); goalLevelIter.hasNext(); )
      {
        ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)goalLevelIter.next();
        Long totalAward = new Long( 0 );
        int totalSelected = 0;
        int managerAchieved = 0;
        for ( Iterator managerOverrideResultIter = managerList.iterator(); managerOverrideResultIter.hasNext(); )
        {
          ChallengepointPaxAwardValueBean managerOverrideResult = (ChallengepointPaxAwardValueBean)managerOverrideResultIter.next();
          if ( managerOverrideResult.isAchieved() )
          {
            if ( managerOverrideResult.getTotalAward().compareTo( new BigDecimal( managerOverrideGoalLevel.getMoAwards() ) ) == 0 )
            {
              managerAchieved++;
              totalAward += managerOverrideGoalLevel.getMoAwards();
              totalSelected += managerOverrideResult.getTotalAchieved();
              managerAwardSummary.incrementTotalAchieved();
              managerAwardSummary.incrementTotalAward( new BigDecimal( managerOverrideGoalLevel.getMoAwards() ) );
            }
          }
        }
        ChallengepointAwardSummary finalManagerAwardSummary = new ChallengepointAwardSummary();
        finalManagerAwardSummary.setTotalAchieved( managerAchieved );
        finalManagerAwardSummary.setTotalAward( new BigDecimal( totalAward ) );
        finalManagerAwardSummary.setTotalSelected( new Long( totalSelected ) );
        finalManagerAwardSummary.setStartRank( managerOverrideGoalLevel.getMoStartRank() );
        finalManagerAwardSummary.setEndRank( managerOverrideGoalLevel.getMoEndRank() );
        listManagerAwardSummary.add( finalManagerAwardSummary );
      }
    }
    listManagerAwardSummary.add( managerAwardSummary );
    pendingChallengepointAwardSummary.setManagerChallengepointAwardSummary( listManagerAwardSummary );

    return pendingChallengepointAwardSummary;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public List computeFinalManagerOverrideList( List managerList, ChallengePointPromotion promotion )
  {
    List<ChallengepointPaxAwardValueBean> sortedPets = new ArrayList<ChallengepointPaxAwardValueBean>( managerList );
    PropertyComparator.sort( sortedPets, new MutableSortDefinition( "percentAchieved", true, false ) );
    managerList.removeAll( managerList );
    int indx = 0;
    int rankIncrement = 0;
    BigDecimal prevPayout = new BigDecimal( 0 );
    for ( Iterator goalLevelIter = sortedPets.iterator(); sortedPets.size() >= indx + 1; )
    {
      ChallengepointPaxAwardValueBean cpAwardValueBean = sortedPets.get( indx );
      if ( cpAwardValueBean.isAchieved() )
      {
        if ( indx - 1 >= 0 )
        {
          ChallengepointPaxAwardValueBean prevCpAwardValueBean = sortedPets.get( indx - 1 );
          if ( prevCpAwardValueBean.getPercentAchieved() != null )
          {
            prevPayout = prevCpAwardValueBean.getPercentAchieved();
          }
        }
        if ( cpAwardValueBean.getPercentAchieved() != null && prevPayout.compareTo( cpAwardValueBean.getPercentAchieved() ) != 0 )
        {
          rankIncrement++;
        }
        for ( Iterator managerOverrideIter = promotion.getManagerOverrideGoalLevels().iterator(); managerOverrideIter.hasNext(); )
        {
          ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)managerOverrideIter.next();
          long totalAward = 0;
          if ( new Long( rankIncrement ) >= managerOverrideGoalLevel.getMoStartRank() && new Long( rankIncrement ) <= managerOverrideGoalLevel.getMoEndRank() )
          {
            BigDecimal managerAward = new BigDecimal( managerOverrideGoalLevel.getMoAwards() );
            totalAward += managerAward.longValue();
            cpAwardValueBean.incrementTotalAchieved();
            if ( totalAward > 0 )
            {
              BigDecimal managersAward = new BigDecimal( totalAward );
              cpAwardValueBean.setTotalAward( managersAward.setScale( 0, promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );
              cpAwardValueBean.setCalculatedAchievement( managerAward );
              cpAwardValueBean.setAwardEarned( managerAward.longValue() );
              cpAwardValueBean.setAwardIssued( managerAward.longValue() );
            }
            managerList.add( cpAwardValueBean );
            break;
          }
        }
      }
      indx++;
    }
    return managerList;
  }

}
