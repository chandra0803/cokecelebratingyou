
package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.strategy.WinnersListStrategy;

/*
 * BadgeWinnerListStrategy
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
 * <td>Bala</td>
 * <td>Mar 19, 2014</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class BadgeWinnersListStrategy extends AbstractPromotionWinnersListStrategy implements WinnersListStrategy
{

  private GamificationService gamificationService;

  public List buildWinnersList( Promotion promotion, PromotionSweepstake promotionSweepstake, int giverWinnerReplacementTotal, int receiverWinnerReplacementTotal )
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

    BadgePromotionAdapter badgePromotion = new BadgePromotionAdapter( (Badge)promotion );
    if ( badgePromotion.isSweepstakesActive() )
    {
      // Get the winner pool.
      List<ParticipantBadge> winnerPool = gamificationService.getParticipantBadgeByPromotionId( badgePromotion.getId() );

      // Determine the total number of winners.
      // there is nothing to do with this isNewListCreate flag for Survey Promo in calculating the
      // Winner Total,just pass this flag to ensure common method arguments are passed.
      int maxWinnerCount = getWinnerTotal( promotionSweepstake, receiverWinnerReplacementTotal, badgePromotion.getMaxWinnersCount(), winnerPool, badgePromotion.isPercentage(), isNewListCreate );

      // Select the winners.
      winners = selectWinners( promotionSweepstake, winnerPool, giverWinnerReplacementTotal > 0 ? giverWinnerReplacementTotal : maxWinnerCount, null, null );

      // Add the promotion sweepstakes to the promotion.
      promotion.addPromotionSweepstake( promotionSweepstake );

      // Delete the winner pool.
      // sweepstakesParticipantService.deleteSweepstakesParticipants( promotion.getId() );
    }

    return winners;
  }

  public GamificationService getGamificationService()
  {
    return gamificationService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public List selectWinners( PromotionSweepstake promoSweepstake, List winnerPool, int totalWinners, String eligibilityType, String multiAwardType )
  {

    // filterWinnerPool( winnerPool );
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
        removedWinningPax.add( winner.getParticipantBadge() );
      }
      else
      {
        winningParticipants.add( winner.getParticipantBadge() );
      }
    }
    int startTotalWinners = totalWinners;
    // Loop until we've exhausted all possible winners or we've met our winner quota
    while ( winnerPool.size() > 0 && totalWinners > 0 )
    {
      int winnerListId = (int) ( Math.random() * winnerPool.size() );
      ParticipantBadge participantBadge = (ParticipantBadge)winnerPool.get( winnerListId );

      PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();
      winner.setParticipant( (Participant)participantBadge.getParticipant() );
      winner.setPromotionSweepstake( promoSweepstake );
      winner.setWinnerType( PromotionSweepstakeWinner.BADGE_WINNER );
      winner.setParticipantBadge( participantBadge );
      // If the winner has already been flagged as removed, then look again
      if ( removedWinningPax.contains( winner.getParticipantBadge() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }
      // if we don't allow a pax to win multiple times per drawing, make sure the current winner
      // hasn't won yet.
      if ( winningParticipants.contains( winner.getParticipantBadge() ) )
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
        if ( existingWinner.getParticipantBadge().equals( winner.getParticipantBadge() ) && existingWinner.getWinnerType().equals( winner.getWinnerType() ) )
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
      winningParticipants.add( winner.getParticipantBadge() );
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

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private static class BadgePromotionAdapter
  {
    /**
     * the survey promotion to be adapted
     */
    private Badge badge;

    /**
     * Constructions a <code>PromotionAdapter</code> object.
     *
     * @param promotion  the promotion to be adapted
     */
    public BadgePromotionAdapter( Badge badge )
    {
      this.badge = badge;
    }

    /**
     * Returns the ID of this promotion.
     *
     * @return the ID of this promotion.
     */
    public Long getId()
    {
      return badge.getId();
    }

    /**
     * Returns the maximum number or percentage of sweepstakes winners.
     *
     * @return the maximum number or percentage of sweepstakes winners.
     */
    public int getMaxWinnersCount()
    {
      int maxWinnersCount = 0;

      Integer sweepstakesPrimaryWinners = badge.getSweepstakesPrimaryWinners();
      if ( sweepstakesPrimaryWinners != null )
      {
        maxWinnersCount = sweepstakesPrimaryWinners.intValue();
      }

      return maxWinnersCount;
    }

    /**
     * Returns true if the number of sweepstakes winners is a percentage of the
     * number of sweepstakes candidates; returns false if the number of
     * sweepstakes winners is fixed.
     *
     * @return true if the number of sweepstakes winners is a percentage of the
     *         number of sweepstakes candidates; false if the number of
     *         sweepstakes winners is fixed.
     */
    public boolean isPercentage()
    {
      boolean isPercentage = false;

      SweepstakesWinnersType sweepstakesWinnersType = badge.getSweepstakesPrimaryBasisType();
      if ( sweepstakesWinnersType != null )
      {
        isPercentage = sweepstakesWinnersType.isPercentage();
      }

      return isPercentage;
    }

    /**
     * Returns true if a sweepstakes can be associated with this survey
     * promotion; returns false otherwise.
     *
     * @return true if a sweepstakes can be associated with this survey
     *         promotion; returns false otherwise.
     */
    public boolean isSweepstakesActive()
    {
      boolean sweepActive = false;
      Iterator iter = badge.getBadgeRules().iterator();
      while ( iter.hasNext() )
      {
        BadgeRule badgeRule = (BadgeRule)iter.next();
        if ( badgeRule.isEligibleForSweepstake() )
        {
          sweepActive = true;
          break;
        }
      }
      return sweepActive;
    }
  }

  public static void removeRemovedParticipants( List winnerPool, PromotionSweepstake detachedSweepStake )
  {
    List removedPax = new ArrayList();
    if ( detachedSweepStake != null && detachedSweepStake.getWinners() != null && detachedSweepStake.getWinners().size() > 0 )
    {
      for ( Iterator iter = detachedSweepStake.getWinners().iterator(); iter.hasNext(); )
      {
        PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
        if ( winner.isRemoved() )
        {
          removedPax.add( winner.getParticipantBadge() );
        }
      }
    }
    if ( winnerPool != null && !winnerPool.isEmpty() )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        ParticipantBadge pax = (ParticipantBadge)iter.next();
        if ( removedPax.size() > 0 )
        {
          for ( Iterator iterator = removedPax.iterator(); iterator.hasNext(); )
          {
            ParticipantBadge paxRemoved = (ParticipantBadge)iterator.next();
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
