/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/strategy/impl/QuizWinnersListStrategy.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.strategy.WinnersListStrategy;

/**
 * QuizWinnersListStrategy
 * 
 *
 */
public class QuizWinnersListStrategy extends AbstractPromotionWinnersListStrategy implements WinnersListStrategy
{

  /**
   * Builds a list of winners for the quizPromotion and sweepstakes.
   * 
   * @param promotion QuizPromotion should be passed in for QuizWinnersListStrategy (Promotion used
   *          for interface)
   * @param promoSweepstake
   * @param winnerReplacementTotal Number of winners needed for a given replacement call
   * @param receiverWinnerReplacementTotal Should always be 0 for QuizWinnersListStrategy (no
   *          receivers)
   * @return List
   */
  public List buildWinnersList( Promotion promotion, PromotionSweepstake promoSweepstake, int winnerReplacementTotal, int totalWinnersDisplayed )
  {

    List winners = new ArrayList();
    boolean isNewListCreate = false;
    Set promotionSweepstakes = promotion.getPromotionSweepstakes();
    PromotionSweepstake attachedSweepstake = null;
    for ( Iterator iter = promotionSweepstakes.iterator(); iter.hasNext(); )
    {
      PromotionSweepstake sweep = (PromotionSweepstake)iter.next();
      if ( !sweep.isProcessed() )
      {
        attachedSweepstake = sweep;
        break;
      }
    }
    if ( attachedSweepstake == null
        || attachedSweepstake != null && ( attachedSweepstake.getWinners() == null || attachedSweepstake.getWinners() != null && attachedSweepstake.getWinners().size() == 0 ) )
    {
      isNewListCreate = true;
    }
    QuizPromotion quizPromotion = (QuizPromotion)promotion;

    // Get QuizWinnerPool
    List winnerPool = activityService.getQuizActivityList( promotion.getId(), new Boolean( true ), promoSweepstake );

    boolean percentOfTotal = SweepstakesWinnersType.PERCENTAGE_CODE.equals( quizPromotion.getSweepstakesPrimaryBasisType().getCode() );

    // Determine the total number of winners
    // there is nothing to do with this isNewListCreate flag for Quiz Promo in calculating the
    // Winner Total,just pass this flag to ensure common method arguments are passed.
    int totalWinners = getWinnerTotal( promoSweepstake, winnerReplacementTotal, quizPromotion.getSweepstakesPrimaryWinners().intValue(), winnerPool, percentOfTotal, isNewListCreate );

    winners = selectWinners( promoSweepstake, winnerPool, totalWinners, null, null );

    promotion.addPromotionSweepstake( promoSweepstake );

    return winners;

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

  private static boolean searchForExistingWinner( List uniqueActivityList, QuizActivity activity )
  {
    boolean isSame = false;
    if ( uniqueActivityList != null && uniqueActivityList.size() > 0 )
    {
      for ( Iterator iter = uniqueActivityList.iterator(); iter.hasNext(); )
      {
        QuizActivity iterateActivity = (QuizActivity)iter.next();
        if ( activity.getParticipant().getId().longValue() == iterateActivity.getParticipant().getId().longValue() )
        {
          isSame = true;
          break;
        }

      }

    }
    return isSame;

  }

  /**
   * @param originalList
   * @return uniqueActivityList
   */
  public static List removeDuplicateAcivities( List originalList )
  {
    List copiedList = new ArrayList( originalList );
    List uniqueActivityList = new ArrayList();
    for ( int i = 0; i < originalList.size(); i++ )
    {
      QuizActivity activity = (QuizActivity)originalList.get( i );
      for ( int j = 0; j < copiedList.size(); j++ )
      {
        QuizActivity innerActivity = (QuizActivity)originalList.get( i );
        if ( activity.getParticipant().getId().longValue() == innerActivity.getParticipant().getId().longValue() )
        {
          if ( !searchForExistingWinner( uniqueActivityList, innerActivity ) )
          {
            uniqueActivityList.add( innerActivity );
            break;
          }

        }
        else if ( activity.getParticipant().getId().longValue() == innerActivity.getParticipant().getId().longValue() )
        {
          uniqueActivityList.add( innerActivity );
          break;
        }
      }
    }
    return uniqueActivityList;
  }

  /**
  * Randomly selects winners from the winnerPool and add the winner to the sweepstake. Uses
  * percentOfWinners and totalWinners to determine how many winners need to be selected. The
  * multiAwardType determines whether or not a winner can win multiple times for a drawing.
  * However, if the drawing is for a RecognitionPromotion with the eligibility type of 'givers and
  * receivers in separate drawings' then the pax can win once as a giver and once as a receiver -
  * multiAwardType specifies to check the time period, then a pax can only win once.
  * 
  * @param promoSweepstake
  * @param winnerPool
  * @param totalWinners
  * @param eligibilityType
  * @param multiAwardType
  * @return List
  */
  public List selectWinners( PromotionSweepstake promoSweepstake, List winnerPool, int totalWinners, String eligibilityType, String multiAwardType )
  {

    filterWinnerPool( winnerPool );
    List winners = new ArrayList();

    // Used if totalWinners is not reach and multiple winners is allowed
    List winnerPoolCopy = new ArrayList( winnerPool );

    if ( winnerPool.size() == 0 )
    {
      // There are no eligible winners
      return winners;
    }
    // Create a set of removed winners from current winners to check against
    Set currentWinners = promoSweepstake.getWinners();
    Set removedWinningPax = new HashSet();
    Set winningParticipants = new HashSet(); // Used when pax aren't allowed multiple wins
    Iterator iter = currentWinners.iterator();
    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( winner.isRemoved() )
      {
        removedWinningPax.add( winner.getParticipant() );
      }
      else
      {
        winningParticipants.add( winner.getParticipant() );
      }
    }
    int startTotalWinners = totalWinners;
    // Loop until we've exhausted all possible winners or we've met our winner quota
    while ( winnerPool.size() > 0 && totalWinners > 0 )
    {
      int winnerListId = (int) ( Math.random() * winnerPool.size() );
      Activity activity = (Activity)winnerPool.get( winnerListId );

      PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();
      winner.setParticipant( activity.getParticipant() );
      winner.setPromotionSweepstake( promoSweepstake );
      winner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE );
      // If the winner has already been flagged as removed, then look again
      if ( removedWinningPax.contains( winner.getParticipant() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }
      winner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE );
      // if we don't allow a pax to win multiple times per drawing, make sure the current winner
      // hasn't won yet.
      if ( winningParticipants.contains( winner.getParticipant() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      } // check current drawing
      Iterator separateDrawingWinners = promoSweepstake.getWinners().iterator();
      boolean alreadyWon = false;
      while ( separateDrawingWinners.hasNext() )
      {
        PromotionSweepstakeWinner existingWinner = (PromotionSweepstakeWinner)separateDrawingWinners.next();
        if ( existingWinner.getParticipant().equals( winner.getParticipant() ) && existingWinner.getWinnerType().equals( winner.getWinnerType() ) )
        {
          alreadyWon = true;
          break; // break out of the inner while loop
        }
      }

      if ( alreadyWon )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }
      winners.add( winner );
      promoSweepstake.addWinner( winner );
      winningParticipants.add( winner.getParticipant() );
      // Take the entry out of the pool now that it has been selected
      winnerPool.remove( winnerListId );
      totalWinners--;
    }

    // If totalWinners hasn't been met and multiple wins are allowed, then
    // Checks to see that totalWinners changed - if not everyone is invalid and shouldn't recurse
    if ( totalWinners > 0 && startTotalWinners != totalWinners )
    {
      List fillerWinners = selectWinners( promoSweepstake, winnerPoolCopy, totalWinners, eligibilityType, multiAwardType );

      Iterator fillerWinnersIter = fillerWinners.iterator();
      while ( fillerWinnersIter.hasNext() )
      {
        winners.add( fillerWinnersIter.next() );
      }
    }

    return winners;
  }

  /**
  * Remove inactive or supsended participants since they are not elible to win
  * 
  * @param winnerPool
  */
  private void filterWinnerPool( List winnerPool )
  {
    if ( winnerPool != null && !winnerPool.isEmpty() )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        Participant pax = ( (Activity)iter.next() ).getParticipant();
        if ( pax.getStatus() != null && !pax.getStatus().getCode().equals( ParticipantStatus.ACTIVE ) )
        {
          iter.remove();
        }
        else if ( pax.getSuspensionStatus() != null && !pax.getSuspensionStatus().getCode().equals( ParticipantSuspensionStatus.NONE ) )
        {
          iter.remove();
        }
      }
    }
  }
}
