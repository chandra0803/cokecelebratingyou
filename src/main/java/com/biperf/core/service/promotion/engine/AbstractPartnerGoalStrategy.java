/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/AbstractPartnerGoalStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;

/**
 * AbstractPartnerGoalStrategy.
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
 * 
 *
 */
public abstract class AbstractPartnerGoalStrategy implements PartnerGoalStrategy
{

  public GoalCalculationResult processGoal( ParticipantPartner paxPartner, Map goalResultsByPax, Map paxGoalResultsByPartner, Map partnerGoalLevelsBySequence )
  {
    // Guard clause
    if ( null == paxPartner )
    {
      throw new BeaconRuntimeException( "paxPartner, must be present while processing goal, is NULL" );
    }
    if ( null == goalResultsByPax )
    {
      throw new BeaconRuntimeException( "goalResultsByPax, must be present while processing goal, is NULL" );
    }
    if ( null == paxGoalResultsByPartner )
    {
      throw new BeaconRuntimeException( "paxGoalResultsByPartner, must be present while processing goal, is NULL" );
    }
    if ( null == partnerGoalLevelsBySequence )
    {
      throw new BeaconRuntimeException( "partnerGoalLevelsByLevelName, must be present while processing goal, is NULL" );
    }

    // Get the PAX Goal Result
    GoalCalculationResult paxGoalResult = (GoalCalculationResult)goalResultsByPax.get( paxPartner.getParticipant().getId() );

    // List of all participants who picked the partner
    List partnerPaxGoalResults = (List)paxGoalResultsByPartner.get( paxPartner.getPartner().getId() );

    // Get the Partner GoalLevel
    PromotionPartnerPayout partnerGoalLevel = getPartnerGoalLevel( partnerGoalLevelsBySequence, (GoalLevel)paxGoalResult.getGoalLevel() );

    GoalCalculationResult goalCalculationResult = processGoalInternal( paxGoalResult, partnerPaxGoalResults );

    if ( goalCalculationResult.isAchieved() )
    {
      GoalQuestPromotion promotion = (GoalQuestPromotion)paxPartner.getPromotion();
      // Calculate Partner payout
      BigDecimal awardAmount = getPartnerAwardAmount( promotion, partnerGoalLevel, paxGoalResult );

      // Set partner payout
      if ( null != awardAmount )
      {
        if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
        {
          goalCalculationResult.setCalculatedPayout( new BigDecimal( awardAmount.longValue() ) );
        }
        else
        {
          goalCalculationResult.setCalculatedPayout( awardAmount );
        }
      }
    }

    goalCalculationResult.setPartner( true );
    goalCalculationResult.setReciever( paxPartner.getPartner() );
    goalCalculationResult.setGoalLevel( paxGoalResult.getGoalLevel() );
    Set userNodes = paxPartner.getPartner().getUserNodes();
    if ( userNodes != null && userNodes.size() > 0 )
    {
      UserNode userNode = (UserNode)userNodes.iterator().next();
      if ( userNode.getNode() != null )
      {
        goalCalculationResult.setNodeRole( userNode.getHierarchyRoleType() );
      }
    }
    goalCalculationResult.setTotalPerformance( paxGoalResult.getTotalPerformance() );
    goalCalculationResult.setBaseObjective( paxGoalResult.getBaseObjective() );
    goalCalculationResult.setAmountToAchieve( paxGoalResult.getAmountToAchieve() );
    goalCalculationResult.setPartnerToParticipant( paxPartner.getParticipant() );

    return goalCalculationResult;
  }

  private BigDecimal getPartnerAwardAmount( GoalQuestPromotion promotion, PromotionPartnerPayout partnerGoalLevel, GoalCalculationResult paxGoalResult )
  {
    BigDecimal awardAmount = null;

    String partnerPayoutStructure = promotion.getPartnerPayoutStructure().getCode();
    if ( PartnerPayoutStructure.FIXED.equals( partnerPayoutStructure ) )
    {
      // If payout Structure is fixed
      awardAmount = partnerGoalLevel.getPartnerAwardAmount();
    }
    else if ( PartnerPayoutStructure.PERCENTAGE.equals( partnerPayoutStructure ) )
    {
      // If payout Structure is percent
      BigDecimal percentage = partnerGoalLevel.getPartnerAwardAmount().movePointLeft( 2 );
      BigDecimal partnerPayout = percentage.multiply( paxGoalResult.getCalculatedPayout() );
      // Standard Partner Payout
      awardAmount = partnerPayout.setScale( 0, BigDecimal.ROUND_HALF_UP );
    }
    else
    {
      throw new BeaconRuntimeException( "Invalid PartnerPayoutStructure('" + partnerPayoutStructure + "')" );
    }

    return awardAmount;
  }

  private PromotionPartnerPayout getPartnerGoalLevel( Map partnerGoalLevelsBySequence, GoalLevel paxGoalLevel )
  {
    Long paxGoalLevelSequence = new Long( paxGoalLevel.getSequenceNumber() );
    PromotionPartnerPayout partnerGoalLevel = (PromotionPartnerPayout)partnerGoalLevelsBySequence.get( paxGoalLevelSequence );
    if ( null == partnerGoalLevel )
    {
      throw new BeaconRuntimeException( "Partner GoalLevel not defined for corresponding Participant GoalLevel('" + paxGoalLevelSequence + "')" );
    }
    return partnerGoalLevel;
  }

  protected abstract GoalCalculationResult processGoalInternal( GoalCalculationResult paxGoalResult, List partnerPaxGoalResults );

  public List<GoalQuestAwardSummary> summarizeResults( Map<Long, GoalQuestAwardSummary> goalQuestAwardSummaryPartnerMap )
  {
    List<GoalQuestAwardSummary> goalQuestAwardSummaryPartnerList = new ArrayList<GoalQuestAwardSummary>();

    int totalSelected = 0;
    int totalAchieved = 0;
    BigDecimal totalAward = new BigDecimal( "0" );
    SortedMap<Long, GoalQuestAwardSummary> sortedMap = null;
    if ( goalQuestAwardSummaryPartnerMap != null )
    {
      sortedMap = new TreeMap<Long, GoalQuestAwardSummary>( goalQuestAwardSummaryPartnerMap );
    }
    if ( sortedMap != null )
    {
      goalQuestAwardSummaryPartnerList.addAll( sortedMap.values() );
    }

    for ( Iterator<GoalQuestAwardSummary> it = goalQuestAwardSummaryPartnerList.iterator(); it.hasNext(); )
    {
      GoalQuestAwardSummary goalQuestAwarySummary = it.next();
      goalQuestAwarySummary.setPartnerGoalQuestAwardSummary( true );
      totalSelected += goalQuestAwarySummary.getTotalSelected();
      totalAchieved += goalQuestAwarySummary.getTotalAchieved();
      if ( goalQuestAwarySummary.getTotalAward() != null )
      {
        totalAward = totalAward.add( goalQuestAwarySummary.getTotalAward() );
      }
    }
    GoalQuestAwardSummary totalParticipantGoalQuestAwardSummary = new GoalQuestAwardSummary();
    totalParticipantGoalQuestAwardSummary.setTotalSelected( totalSelected );
    totalParticipantGoalQuestAwardSummary.setTotalAchieved( totalAchieved );
    totalParticipantGoalQuestAwardSummary.setTotalAward( totalAward );
    totalParticipantGoalQuestAwardSummary.setPartnerGoalQuestAwardSummary( true );
    totalParticipantGoalQuestAwardSummary.setPartnerTotals( true );
    goalQuestAwardSummaryPartnerList.add( totalParticipantGoalQuestAwardSummary );

    return goalQuestAwardSummaryPartnerList;
  }

}
