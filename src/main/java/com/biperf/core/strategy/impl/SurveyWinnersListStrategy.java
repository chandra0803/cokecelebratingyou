/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/strategy/impl/SurveyWinnersListStrategy.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;
import com.biperf.core.strategy.WinnersListStrategy;

/*
 * SurveyWinnersListStrategy
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
 * <td>Thomas Eaton</td>
 * <td>Jun 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class SurveyWinnersListStrategy extends AbstractPromotionWinnersListStrategy implements WinnersListStrategy
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private ParticipantService participantService;
  // private SweepstakesParticipantService sweepstakesParticipantService;
  private ParticipantSurveyService participantSurveyService;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Selects the winners of the sweepstakes associated with the given survey
   * promotion.
   *
   * @param promotion  the promotion for which this sweepstakes is being run.
   * @param promotionSweepstake  information about this running of the
   *                             sweepstakes.  Note: this object is not
   *                             attached to a Hibernate session.
   * @param giverWinnerReplacementTotal  not used by this method.
   * @param receiverWinnerReplacementTotal  not used by this method.
   * @return the winners of the sweepstakes associated with the given survey
   *         promotion, as a <code>List</code> of
   *        {@link PromotionSweepstakeWinner} objects.
   */
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
    SurveyPromotionAdapter surveyPromotion = new SurveyPromotionAdapter( (SurveyPromotion)promotion );
    if ( surveyPromotion.isSweepstakesActive() )
    {
      // Get the winner pool.
      List<ParticipantSurvey> winnerPool = participantSurveyService.getCompletedSurveyByPromoId( surveyPromotion.getId() );

      // Determine the total number of winners.
      // there is nothing to do with this isNewListCreate flag for Survey Promo in calculating the
      // Winner Total,just pass this flag to ensure common method arguments are passed.
      int maxWinnerCount = getWinnerTotal( promotionSweepstake, receiverWinnerReplacementTotal, surveyPromotion.getMaxWinnersCount(), winnerPool, surveyPromotion.isPercentage(), isNewListCreate );

      // Select the winners.
      winners = selectWinners( promotionSweepstake, winnerPool, giverWinnerReplacementTotal > 0 ? giverWinnerReplacementTotal : maxWinnerCount, null, null );

      // Add the promotion sweepstakes to the promotion.
      promotion.addPromotionSweepstake( promotionSweepstake );

      // Delete the winner pool.
      // sweepstakesParticipantService.deleteSweepstakesParticipants( promotion.getId() );
    }

    return winners;
  }

  /**
   * Returns a list of participants who have won this promotion.
   *
   * @param promotionSweepstake  information about this running of the
   *                             sweepstakes.  Note: this object is not
   *                             attached to a Hibernate session.
   * @param winnerPool  the participants who are eligible to win this
   *                    sweepstakes, as a <code>List</code> of
   *                    {@link SweepstakesParticipant} objects.
   * @param maxWinnerCount  the maximum number or percentage of winners.
   * @return the participants who have won this sweepstakes, as a
   *         <code>List</code> of {@link PromotionSweepstakeWinner} objects.
   */
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
      ParticipantSurvey participantSurvey = (ParticipantSurvey)winnerPool.get( winnerListId );

      PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();
      winner.setParticipant( (Participant)participantSurvey.getParticipant() );
      winner.setPromotionSweepstake( promoSweepstake );
      winner.setWinnerType( PromotionSweepstakeWinner.SURVEY_TAKER_TYPE );
      // If the winner has already been flagged as removed, then look again
      if ( removedWinningPax.contains( winner.getParticipant() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }
      winner.setWinnerType( PromotionSweepstakeWinner.SURVEY_TAKER_TYPE );
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

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setParticipantSurveyService( ParticipantSurveyService participantSurveyService )
  {
    this.participantSurveyService = participantSurveyService;
  }

  /*
   * public void setSweepstakesParticipantService( SweepstakesParticipantService
   * sweepstakesParticipantService ) { this.sweepstakesParticipantService =
   * sweepstakesParticipantService; }
   */

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private static class SurveyPromotionAdapter
  {
    /**
     * the survey promotion to be adapted
     */
    private SurveyPromotion surveyPromotion;

    /**
     * Constructions a <code>SurveyPromotionAdapter</code> object.
     *
     * @param surveyPromotion  the survey promotion to be adapted
     */
    public SurveyPromotionAdapter( SurveyPromotion surveyPromotion )
    {
      this.surveyPromotion = surveyPromotion;
    }

    /**
     * Returns the ID of this survey promotion.
     *
     * @return the ID of this survey promotion.
     */
    public Long getId()
    {
      return surveyPromotion.getId();
    }

    /**
     * Returns the maximum number or percentage of sweepstakes winners.
     *
     * @return the maximum number or percentage of sweepstakes winners.
     */
    public int getMaxWinnersCount()
    {
      int maxWinnersCount = 0;

      Integer sweepstakesPrimaryWinners = surveyPromotion.getSweepstakesPrimaryWinners();
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

      SweepstakesWinnersType sweepstakesWinnersType = surveyPromotion.getSweepstakesPrimaryBasisType();
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
      return surveyPromotion.isSweepstakesActive();
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
          removedPax.add( winner.getParticipant() );
        }

      }

    }
    if ( winnerPool != null && !winnerPool.isEmpty() )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        Participant pax = (Participant) ( (ParticipantSurvey)iter.next() ).getParticipant();
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
