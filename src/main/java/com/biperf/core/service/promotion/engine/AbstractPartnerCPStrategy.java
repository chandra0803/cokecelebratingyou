/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/AbstractPartnerCPStrategy.java,v $
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

import com.biperf.core.domain.challengepoint.ChallengepointAwardSummary;
import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;

public abstract class AbstractPartnerCPStrategy implements PartnerCPStrategy
{

  public ChallengePointCalculationResult processGoal( ParticipantPartner paxPartner, Map cpResultsByPax, Map paxGoalResultsByPartner, Map partnerCPLevelsBySequence )
  {
    // Guard clause
    if ( null == paxPartner )
    {
      throw new BeaconRuntimeException( "paxPartner, must be present while processing goal, is NULL" );
    }
    if ( null == cpResultsByPax )
    {
      throw new BeaconRuntimeException( "goalResultsByPax, must be present while processing goal, is NULL" );
    }
    if ( null == paxGoalResultsByPartner )
    {
      throw new BeaconRuntimeException( "paxGoalResultsByPartner, must be present while processing goal, is NULL" );
    }
    if ( null == partnerCPLevelsBySequence )
    {
      throw new BeaconRuntimeException( "partnerGoalLevelsByLevelName, must be present while processing goal, is NULL" );
    }

    // Get the PAX Goal Result
    ChallengePointCalculationResult paxCPResult = (ChallengePointCalculationResult)cpResultsByPax.get( paxPartner.getParticipant().getId() );

    // List of all participants who picked the partner
    List partnerPaxCPResults = (List)paxGoalResultsByPartner.get( paxPartner.getPartner().getId() );

    // Get the Partner GoalLevel
    PromotionPartnerPayout partnerGoalLevel = getPartnerCPLevel( partnerCPLevelsBySequence, (GoalLevel)paxCPResult.getGoalLevel() );

    ChallengePointCalculationResult cpCalculationResult = processCPInternal( paxCPResult, partnerPaxCPResults );

    if ( cpCalculationResult.isAchieved() )
    {
      ChallengePointPromotion promotion = (ChallengePointPromotion)paxPartner.getPromotion();
      // Calculate Partner payout
      BigDecimal awardAmount = getPartnerAwardAmount( promotion, partnerGoalLevel, paxCPResult );

      // Set partner payout
      if ( null != awardAmount )
      {
        if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
        {
          cpCalculationResult.setCalculatedAchievement( new BigDecimal( awardAmount.longValue() ) );
        }
        else
        {
          cpCalculationResult.setCalculatedAchievement( awardAmount );
        }
      }
    }

    cpCalculationResult.setPartner( true );
    cpCalculationResult.setReciever( paxPartner.getPartner() );
    cpCalculationResult.setGoalLevel( paxCPResult.getGoalLevel() );
    Set userNodes = paxPartner.getPartner().getUserNodes();
    if ( userNodes != null && userNodes.size() > 0 )
    {
      UserNode userNode = (UserNode)userNodes.iterator().next();
      if ( userNode.getNode() != null )
      {
        cpCalculationResult.setNodeRole( userNode.getHierarchyRoleType() );
      }
    }
    cpCalculationResult.setTotalPerformance( paxCPResult.getTotalPerformance() );
    cpCalculationResult.setBaseObjective( paxCPResult.getBaseObjective() );
    cpCalculationResult.setAmountToAchieve( paxCPResult.getAmountToAchieve() );
    cpCalculationResult.setPartnerToParticipant( paxPartner.getParticipant() );

    return cpCalculationResult;
  }

  private BigDecimal getPartnerAwardAmount( ChallengePointPromotion promotion, PromotionPartnerPayout partnerGoalLevel, ChallengePointCalculationResult paxGoalResult )
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
      BigDecimal partnerPayout = percentage.multiply( paxGoalResult.getCalculatedAchievement() );
      // Standard Partner Payout
      awardAmount = partnerPayout.setScale( 0, BigDecimal.ROUND_HALF_UP );
    }
    else
    {
      throw new BeaconRuntimeException( "Invalid PartnerPayoutStructure('" + partnerPayoutStructure + "')" );
    }

    return awardAmount;
  }

  private PromotionPartnerPayout getPartnerCPLevel( Map partnerGoalLevelsBySequence, GoalLevel paxGoalLevel )
  {
    Long paxGoalLevelSequence = new Long( paxGoalLevel.getSequenceNumber() );
    PromotionPartnerPayout partnerGoalLevel = (PromotionPartnerPayout)partnerGoalLevelsBySequence.get( paxGoalLevelSequence );
    if ( null == partnerGoalLevel )
    {
      throw new BeaconRuntimeException( "Partner GoalLevel not defined for corresponding Participant GoalLevel('" + paxGoalLevelSequence + "')" );
    }
    return partnerGoalLevel;
  }

  protected abstract ChallengePointCalculationResult processCPInternal( ChallengePointCalculationResult paxGoalResult, List partnerPaxGoalResults );

  public List<ChallengepointAwardSummary> summarizeResults( Map<Long, ChallengepointAwardSummary> cpAwardSummaryPartnerMap )
  {
    List<ChallengepointAwardSummary> cpAwardSummaryPartnerList = new ArrayList<ChallengepointAwardSummary>();

    int totalSelected = 0;
    int totalAchieved = 0;
    BigDecimal totalAward = new BigDecimal( "0" );
    SortedMap<Long, ChallengepointAwardSummary> sortedMap = null;
    if ( cpAwardSummaryPartnerMap != null )
    {
      sortedMap = new TreeMap<Long, ChallengepointAwardSummary>( cpAwardSummaryPartnerMap );
    }
    if ( sortedMap != null )
    {
      cpAwardSummaryPartnerList.addAll( sortedMap.values() );
    }

    for ( Iterator<ChallengepointAwardSummary> it = cpAwardSummaryPartnerList.iterator(); it.hasNext(); )
    {
      ChallengepointAwardSummary cpAwardSummary = it.next();
      cpAwardSummary.setPartnerGoalQuestAwardSummary( true );
      totalSelected += cpAwardSummary.getTotalSelected();
      totalAchieved += cpAwardSummary.getTotalAchieved();
      if ( cpAwardSummary.getTotalAward() != null )
      {
        totalAward = totalAward.add( cpAwardSummary.getTotalAward() );
      }
    }
    ChallengepointAwardSummary totalParticipantCPAwardSummary = new ChallengepointAwardSummary();
    totalParticipantCPAwardSummary.setTotalSelected( new Long( totalSelected ) );
    totalParticipantCPAwardSummary.setTotalAchieved( totalAchieved );
    totalParticipantCPAwardSummary.setTotalAward( totalAward );
    totalParticipantCPAwardSummary.setPartnerGoalQuestAwardSummary( true );
    totalParticipantCPAwardSummary.setPartnerTotals( true );
    cpAwardSummaryPartnerList.add( totalParticipantCPAwardSummary );

    return cpAwardSummaryPartnerList;
  }

}
