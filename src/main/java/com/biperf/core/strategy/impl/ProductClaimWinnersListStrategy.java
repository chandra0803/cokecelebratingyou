
package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.strategy.WinnersListStrategy;
import com.biperf.core.value.SweepStakeWinnerParticipantValue;

public class ProductClaimWinnersListStrategy extends AbstractPromotionWinnersListStrategy implements WinnersListStrategy
{

  /**
   * @param promotion
   * @param promoSweepstake
   * @param submitterWinnerReplacementTotal
   * @param teammemberWinnerReplacementTotal
   * @return List
   */
  public List buildWinnersList( Promotion promotion, PromotionSweepstake promoSweepstake, int submitterWinnerReplacementTotal, int teammemberWinnerReplacementTotal )
  {

    List winners = new ArrayList();

    ProductClaimPromotion productClaimPromotion = (ProductClaimPromotion)promotion;
    String eligibilityType = productClaimPromotion.getSweepstakesWinnerEligibilityType().getCode();
    String multiAwardType = productClaimPromotion.getSweepstakesMultipleAwardType().getCode();
    String eligibilityClaimsType = productClaimPromotion.getSweepstakesClaimEligibilityType().getCode();

    boolean percentOfSubmittersTotal = false;
    if ( productClaimPromotion.getSweepstakesPrimaryBasisType() != null )
    {
      percentOfSubmittersTotal = SweepstakesWinnersType.PERCENTAGE_CODE.equals( productClaimPromotion.getSweepstakesPrimaryBasisType().getCode() );
    }
    boolean percentOfTeamMembersTotal = false;
    if ( productClaimPromotion.getSweepstakesSecondaryBasisType() != null )
    {
      percentOfTeamMembersTotal = SweepstakesWinnersType.PERCENTAGE_CODE.equals( productClaimPromotion.getSweepstakesSecondaryBasisType().getCode() );
    }

    int sweepstakeSubmittersWinnersCount = 0;
    if ( productClaimPromotion.getSweepstakesPrimaryWinners() != null )
    {
      sweepstakeSubmittersWinnersCount = productClaimPromotion.getSweepstakesPrimaryWinners().intValue();
    }

    int sweepstakeTeamMembersWinnersCount = 0;
    if ( productClaimPromotion.getSweepstakesSecondaryWinners() != null )
    {
      sweepstakeTeamMembersWinnersCount = productClaimPromotion.getSweepstakesSecondaryWinners().intValue();
    }

    // For looking up the Claims available for Sweepstakes, need to always use parent's promotion_id
    Long promotionIdForClaimLookup = productClaimPromotion.getId();
    if ( productClaimPromotion.getParentPromotion() != null )
    {
      promotionIdForClaimLookup = productClaimPromotion.getParentPromotion().getId();
    }

    // submitters only
    if ( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE.equals( eligibilityType ) )
    {
      // Get claim list for winner pool

      List winnerPool = claimService.getProductClaimClaimsList( promotionIdForClaimLookup, eligibilityClaimsType, promoSweepstake );
      List winnerPoolParticipants = getWinnerPoolParticipants( winnerPool, SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE );
      // Get total number of winners that need to be selected for the promosweepstake
      // taking in consideration the replacement number request
      int totalGiversWinners = getProductClaimWinnerTotal( promoSweepstake,
                                                           submitterWinnerReplacementTotal,
                                                           sweepstakeSubmittersWinnersCount,
                                                           winnerPoolParticipants,
                                                           percentOfSubmittersTotal,
                                                           false,
                                                           false );

      // Get winners
      winners = selectProductClaimWinners( promoSweepstake, winnerPoolParticipants, totalGiversWinners, eligibilityType, multiAwardType );
    }

    if ( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE.equals( eligibilityType ) )
    {
      // get team members activities for the test promo
      List winnerPool = claimService.getProductClaimClaimsList( promotionIdForClaimLookup, eligibilityClaimsType, promoSweepstake );
      List winnerPoolParticipants = getWinnerPoolParticipants( winnerPool, SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE );
      // Determine the total number of winners
      int totalReceiversWinners = getProductClaimWinnerTotal( promoSweepstake,
                                                              teammemberWinnerReplacementTotal,
                                                              sweepstakeTeamMembersWinnersCount,
                                                              winnerPoolParticipants,
                                                              percentOfTeamMembersTotal,
                                                              false,
                                                              false );

      winners = selectProductClaimWinners( promoSweepstake, winnerPoolParticipants, totalReceiversWinners, eligibilityType, multiAwardType );
    }

    if ( SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE.equals( eligibilityType ) )
    {
      // get receiver activities for the test promo
      List winnerPool = claimService.getProductClaimClaimsList( promotionIdForClaimLookup, eligibilityClaimsType, promoSweepstake );
      List winnerPoolParticipants = getWinnerPoolParticipants( winnerPool, SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE );
      // Determine the total number of winners
      int totalWinners = getProductClaimWinnerTotal( promoSweepstake,
                                                     submitterWinnerReplacementTotal,
                                                     sweepstakeSubmittersWinnersCount,
                                                     winnerPoolParticipants,
                                                     percentOfSubmittersTotal,
                                                     false,
                                                     false );

      winners = selectProductClaimWinners( promoSweepstake, winnerPoolParticipants, totalWinners, eligibilityType, multiAwardType );
    }

    if ( SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE.equals( eligibilityType ) )
    {
      // get giver activities for the test promo
      List winnerPool = claimService.getProductClaimClaimsList( promotionIdForClaimLookup, eligibilityClaimsType, promoSweepstake );
      List submitterWinnerPoolParticipants = getWinnerPoolParticipants( winnerPool, SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE );
      // Determine the total number of winners
      int totalGiversWinners = getProductClaimWinnerTotal( promoSweepstake,
                                                           submitterWinnerReplacementTotal,
                                                           sweepstakeSubmittersWinnersCount,
                                                           submitterWinnerPoolParticipants,
                                                           percentOfSubmittersTotal,
                                                           true,
                                                           true );

      List giverWinners = selectProductClaimWinners( promoSweepstake, submitterWinnerPoolParticipants, totalGiversWinners, eligibilityType, multiAwardType );

      // get teammember for the test promo
      // List teammemberWinnerPool = claimService.getSubmittersOnlyProductClaimClaimsList(
      // productClaimPromotion
      // .getId(), promoSweepstake, eligibilityClaimsType );
      List teammemberWinnerPoolParticipants = getWinnerPoolParticipants( winnerPool, SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE );

      // Determine the total number of winners
      int totalReceiversWinners = getProductClaimWinnerTotal( promoSweepstake,
                                                              teammemberWinnerReplacementTotal,
                                                              sweepstakeTeamMembersWinnersCount,
                                                              teammemberWinnerPoolParticipants,
                                                              percentOfTeamMembersTotal,
                                                              true,
                                                              false );

      List receiverWinners = selectProductClaimWinners( promoSweepstake, teammemberWinnerPoolParticipants, totalReceiversWinners, eligibilityType, multiAwardType );

      // merge the giversWinners and the receiverWinners
      for ( Iterator iter = giverWinners.iterator(); iter.hasNext(); )
      {
        winners.add( iter.next() );
      }

      for ( Iterator iter = receiverWinners.iterator(); iter.hasNext(); )
      {
        winners.add( iter.next() );
      }
    }

    promotion.addPromotionSweepstake( promoSweepstake );

    return winners;

  }

  /**
   * Randomly selects winners from the winnerPool and add the winner to the sweepstake. Uses
   * percentOfWinners and totalWinners to determine how many winners need to be selected. The
   * multiAwardType determines whether or not a winner can win multiple times for a drawing.
   * However, if the drawing is for a RecognitionPromotion with the eligibility type of 'givers and
   * receivers in separate drawings' then the pax can win once as a giver and once as a receiver -
   * multiAwardType specifies to check the time period, then a pax can only win once.
   * 
   * @param winnerPool
   * @param eligibilityType
   * @return List
   */
  public List getWinnerPoolParticipants( List winnerPool, String eligibilityType )
  {
    List winnerPoolParticipants = new ArrayList();
    if ( winnerPool.size() == 0 )
    {
      // There are no eligible winners
      return winnerPoolParticipants;
    }

    for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
    {
      // check submitter
      Claim claim = (Claim)iter.next();

      if ( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE.equals( eligibilityType ) || SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE.equals( eligibilityType ) )
      {
        SweepStakeWinnerParticipantValue sweepStakeWinnerParticipantValue = new SweepStakeWinnerParticipantValue();
        sweepStakeWinnerParticipantValue.setEligibilityType( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE );
        sweepStakeWinnerParticipantValue.setParticipant( claim.getSubmitter() );
        winnerPoolParticipants.add( sweepStakeWinnerParticipantValue );
      }
      if ( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE.equals( eligibilityType ) || SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE.equals( eligibilityType ) )
      {

        // check for participant
        Set claimParticipants = ( (ProductClaim)claim ).getClaimParticipants();
        for ( Iterator claimParticipantsIter = claimParticipants.iterator(); claimParticipantsIter.hasNext(); )
        {
          SweepStakeWinnerParticipantValue sweepStakeWinnerParticipantValue = new SweepStakeWinnerParticipantValue();
          sweepStakeWinnerParticipantValue.setEligibilityType( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE );
          ProductClaimParticipant claimParticipant = (ProductClaimParticipant)claimParticipantsIter.next();
          sweepStakeWinnerParticipantValue.setParticipant( claimParticipant.getParticipant() );
          winnerPoolParticipants.add( sweepStakeWinnerParticipantValue );
        }
      }

    }
    return winnerPoolParticipants;
  }

  public static List removeDuplicateAcivities( List originalList )
  {
    List copiedList = new ArrayList( originalList );
    List uniqueActivityList = new ArrayList();
    HashMap uniqueActivityHash = new HashMap(); //

    for ( int i = 0; i < originalList.size(); i++ )
    {
      Activity activity = (Activity)originalList.get( i );
      for ( int j = 0; j < copiedList.size(); j++ )
      {
        Activity innerActivity = (Activity)originalList.get( i );
        if ( activity.getParticipant().getId().longValue() == innerActivity.getParticipant().getId().longValue() )
        {
          if ( !newSearchForExistingWinner( uniqueActivityHash, innerActivity ) )
          {
            uniqueActivityList.add( innerActivity );
            uniqueActivityHash.put( innerActivity.getParticipant().getId(), innerActivity.getParticipant().getId() ); //
            break;
          }
        }
      }
    }
    return uniqueActivityList;
  }

  private static boolean newSearchForExistingWinner( HashMap uniqueActivityHash, Activity activity )
  {
    boolean isSame = false;
    if ( uniqueActivityHash.get( activity.getParticipant().getId() ) != null )
    {
      isSame = true;
    }
    return isSame;
  }

  public static void removeRemovedAcivities( List winnerPool, PromotionSweepstake detachedSweepStake )
  {
    List removedPax = new ArrayList();
    if ( detachedSweepStake != null && detachedSweepStake.getWinners() != null && detachedSweepStake.getWinners().size() > 0 )
    {
      for ( Iterator iter = detachedSweepStake.getWinners().iterator(); iter.hasNext(); )
      {
        PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
        if ( winner.isRemoved() )
        {
          removedPax.add( winner.getParticipant() );
        }
      }
    }
    if ( winnerPool != null && !winnerPool.isEmpty() )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        Participant pax = ( (Activity)iter.next() ).getParticipant();
        if ( removedPax.size() > 0 )
        {
          for ( Iterator iterator = removedPax.iterator(); iterator.hasNext(); )
          {
            Participant paxRemoved = (Participant)iterator.next();
            if ( pax.getId().longValue() == paxRemoved.getId().longValue() )
            {
              iter.remove();
              break;
            }
          }

        }
      }
    }
  }

}
