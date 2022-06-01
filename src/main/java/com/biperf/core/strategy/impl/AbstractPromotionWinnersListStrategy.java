
package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.AbstractRecognitionActivity;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.SweepStakeWinnerParticipantValue;
import com.biperf.core.value.SweepstakeWinnerPoolValue;

/**
 * AbstractPromotionWinnersListStrategy
 * 
 *
 */
public abstract class AbstractPromotionWinnersListStrategy extends BaseStrategy
{

  ActivityService activityService;
  ClaimService claimService;
  private static final Log logger = LogFactory.getLog( AbstractPromotionWinnersListStrategy.class );

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

    boolean allowMultiple = SweepstakesMultipleAwardsType.MULTIPLE_CODE.equals( multiAwardType );
    boolean checkTimePeriod = SweepstakesMultipleAwardsType.PERIOD_CODE.equals( multiAwardType );

    // Create a set of removed winners from current winners to check against
    Set currentWinners = promoSweepstake.getWinners();
    Set removedWinningPax = new HashSet();
    Set winningParticipants = new HashSet(); // Used when pax aren't allowed multiple wins
    Set currentWinningPax = new HashSet(); // Used only for separate drawings
    Iterator iter = currentWinners.iterator();
    if ( !allowMultiple )
    {
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
    }
    int startTotalWinners = totalWinners;
    // Loop until we've exhausted all possible winners or we've met our winner quota
    while ( winnerPool.size() > 0 && totalWinners > 0 )
    {
      int winnerListId = (int) ( Math.random() * winnerPool.size() );
      PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();

      Activity activity = null;
      SweepstakeWinnerPoolValue winnerPoolValueBean = null;
      if ( winnerPool.get( 0 ) instanceof Activity )
      {
        activity = (Activity)winnerPool.get( winnerListId );
        winner.setParticipant( activity.getParticipant() );
      }
      else if ( winnerPool.get( 0 ) instanceof SweepstakeWinnerPoolValue )
      {
        winnerPoolValueBean = (SweepstakeWinnerPoolValue)winnerPool.get( winnerListId );
        winner.setParticipant( getParticipantService().getParticipantById( winnerPoolValueBean.getUserId() ) );
      }
      winner.setPromotionSweepstake( promoSweepstake );
      winner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE );

      // If the winner has already been flagged as removed, then look again
      if ( removedWinningPax.contains( winner.getParticipant() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }

      // If this is a AbstractRecognitionActivity, then mark the winner as a primary or secondary
      if ( activity == null || activity instanceof AbstractRecognitionActivity )
      {
        if ( activity == null )
        {
          if ( winnerPoolValueBean.isSubmitter() )
          {
            winner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE );
          }
          else
          {
            winner.setWinnerType( PromotionSweepstakeWinner.RECEIVER_TYPE );
          }
        }
        else // activity is not null
        {
          if ( ( (AbstractRecognitionActivity)activity ).isSubmitter() )
          {
            if ( activity instanceof RecognitionActivity )
            {
              winner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE );
            }
            else
            {
              winner.setWinnerType( PromotionSweepstakeWinner.NOMINATOR_TYPE );
            }
          }
          else
          {
            if ( activity instanceof RecognitionActivity )
            {
              winner.setWinnerType( PromotionSweepstakeWinner.RECEIVER_TYPE );
            }
            else
            {
              winner.setWinnerType( PromotionSweepstakeWinner.NOMINEE_TYPE );
            }
          }
        }
        // If we don't allow a pax to win multiple times per drawing, make sure
        // the current winner hasn't won yet.
        if ( !allowMultiple )
        {

          // If multiples is not allowed and eligibilityType is givers only, receivers only, and
          // combined drawings
          if ( !SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE.equals( eligibilityType )
              && !SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE.equals( eligibilityType ) )
          {
            if ( winningParticipants.contains( winner.getParticipant() ) )
            {
              // Take the entry out of the pool now since we can't use it
              winnerPool.remove( winnerListId );
              continue;
            }
            // The eligibilityType is a givers and receivers separate drawing
          }
          else
          {

            // If pax can only win once total (as giver or receiver)
            if ( checkTimePeriod )
            {
              if ( winningParticipants.contains( winner.getParticipant() ) )
              {
                // Take the entry out of the pool now since we can't use it
                winnerPool.remove( winnerListId );
                continue;
              }

              // If the pax can win once per drawing (i.e. once as giver and once as receiver)
            }
            else
            {
              // If the user has won already we need to make sure it hasn't been the winner type
              // as the current winner (i.e. if they won already then it can be only once as a giver
              // and
              // once as receiver)

              // check current drawing
              if ( currentWinningPax.contains( winner.getParticipant() ) )
              {
                // Take the entry out of the pool now since we can't use it
                winnerPool.remove( winnerListId );
                continue;
              }

              // check previous drawing
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
            }

          }

        }
      } // END if( activity instanceof RecognitionActivity )

      /*
       * // If this is a NominationActivity, then mark the winner as a giver or receiver if (
       * activity instanceof NominationActivity ) { if ( ( (RecognitionActivity)activity
       * ).isSubmitter() ) { winner.setWinnerType( PromotionSweepstakeWinner.GIVER_TYPE ); } else {
       * winner.setWinnerType( PromotionSweepstakeWinner.RECEIVER_TYPE ); } // If we don't allow a
       * pax to win multiple times per drawing, make sure // the current winner hasn't won yet. if (
       * !allowMultiple ) { // If multiples is not allowed and eligibilityType is givers only,
       * receivers only, and // combined drawings if (
       * !SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE .equals(
       * eligibilityType ) ) { if ( winningParticipants.contains( winner.getParticipant() ) ) { //
       * Take the entry out of the pool now since we can't use it winnerPool.remove( winnerListId );
       * continue; } // The eligibilityType is a givers and receivers separate drawing } else { //
       * If pax can only win once total (as giver or receiver) if ( checkTimePeriod ) { if (
       * winningParticipants.contains( winner.getParticipant() ) ) { // Take the entry out of the
       * pool now since we can't use it winnerPool.remove( winnerListId ); continue; } // If the pax
       * can win once per drawing (i.e. once as giver and once as receiver) } else { // If the user
       * has won already we need to make sure it hasn't been the winner type // as the current
       * winner (i.e. if they won already then it can be only once as a giver // and // once as
       * receiver) // check current drawing if ( currentWinningPax.contains( winner.getParticipant()
       * ) ) { // Take the entry out of the pool now since we can't use it winnerPool.remove(
       * winnerListId ); continue; } // check previous drawing Iterator separateDrawingWinners =
       * promoSweepstake.getWinners().iterator(); boolean alreadyWon = false; while (
       * separateDrawingWinners.hasNext() ) { PromotionSweepstakeWinner existingWinner =
       * (PromotionSweepstakeWinner)separateDrawingWinners .next(); if (
       * existingWinner.getParticipant().equals( winner.getParticipant() ) &&
       * existingWinner.getWinnerType().equals( winner.getWinnerType() ) ) { alreadyWon = true;
       * break; // break out of the inner while loop } } if ( alreadyWon ) { // Take the entry out
       * of the pool now since we can't use it winnerPool.remove( winnerListId ); continue; } } } }
       * }// END if( activity instanceof NominationActivity )
       */

      winners.add( winner );
      promoSweepstake.addWinner( winner );
      winningParticipants.add( winner.getParticipant() );
      currentWinningPax.add( winner.getParticipant() );

      // Take the entry out of the pool now that it has been selected
      winnerPool.remove( winnerListId );
      totalWinners--;
    }

    // If totalWinners hasn't been met and multiple wins are allowed, then
    // Checks to see that totalWinners changed - if not everyone is invalid and shouldn't recurse
    if ( totalWinners > 0 && startTotalWinners != totalWinners && allowMultiple )
    {
      List fillerWinners = selectWinners( promoSweepstake, winnerPoolCopy, totalWinners, eligibilityType, multiAwardType );

      Iterator fillerWinnersIter = fillerWinners.iterator();
      while ( fillerWinnersIter.hasNext() )
      {
        winners.add( fillerWinnersIter.next() );
      }
    }

    logger.error( "***** winners.size=" + winners.size() );
    return winners;
  }

  /**
   * Get winner total for quiz promotion sweepstake or giver only, receiver only, giver and receiver
   * combined recognition promotion sweepstake.
   * 
   * @param detachedSweepstake
   * @param replacementWinnersTotal Number of winners requested for replacement purposes
   * @param promotionWinnersValue Total number of winners specified in promotion sweepstakes OR
   *          percentage value - both found promotion (example:
   *          recognitionPromotion.getSweepstakesPrimaryWinners().intValue())
   * @param winnerPool Pool of eligible winners
   * @param percentOfTotal Total winners to be determined as a percent of eligible winners (note
   *          promotionSweepstakesWinnersTotal is used as percentage and taken from same value)
   * @param isNewListCreate 
   * @return int Total number of winners
   */
  public int getWinnerTotal( PromotionSweepstake detachedSweepstake, int replacementWinnersTotal, int promotionWinnersValue, List winnerPool, boolean percentOfTotal, boolean isNewListCreate )
  {
    return getWinnerTotal( detachedSweepstake, replacementWinnersTotal, promotionWinnersValue, winnerPool, percentOfTotal, false, false, isNewListCreate );
  }

  /**
   * Get the winner total for recognition promotion sweepstakes with giver and receiver seperate
   * 
   * @param detachedSweepstake
   * @param replacementWinnersTotal Number of winners requested for replacement purposes
   * @param promotionSweepstakesWinnersValue Total number of winners specified in promotion
   *          sweepstakes OR percentage value - both found promotion (example:
   *          recognitionPromotion.getSweepstakesPrimaryWinners().intValue())
   * @param winnerPool Pool of eligible winners
   * @param percentOfTotal Total winners to be determined as a percent of eligible winners (note
   *          promotionSweepstakesWinnersTotal is used as percentage and taken from same value)
   * @param isSeperate Is this a recognition promotion sweepstake with seperate giver and receiver
   * @param isGiverType Is this a giverType (used for recognition only)
   * @param isNewListCreate 
   * @return int Total number of winners
   */
  public int getWinnerTotal( PromotionSweepstake detachedSweepstake,
                             int replacementWinnersTotal,
                             int promotionSweepstakesWinnersValue,
                             List winnerPool,
                             boolean percentOfTotal,
                             boolean isSeperate,
                             boolean isGiverType,
                             boolean isNewListCreate )
  {
    // Check winner replacement total - if more than zero
    // this allows an override to take place for replacement purposes to get
    // a lower number of winners than the sweepstake has been setup for.
    // If the replacement number specified is more than the sweepstake setting
    // it will only return the value that the sweepstake was originally setup for.
    int totalWinners = 0;
    if ( replacementWinnersTotal > 0 )
    {
      if ( replacementWinnersTotal > promotionSweepstakesWinnersValue )
      {
        // do not allow more than specified winnerPool
        return promotionSweepstakesWinnersValue;
      }
      return replacementWinnersTotal;
    }
    // Bug Fix 15761.Do not build winners if the list is not going to be create new or if the
    // replacement winner total is not greater tahn zero
    if ( detachedSweepstake.getPromotion() != null && ( detachedSweepstake.getPromotion().isRecognitionPromotion() || detachedSweepstake.getPromotion().isNominationPromotion() ) )
    {
      if ( isNewListCreate || replacementWinnersTotal > 0 )
      {
        totalWinners = promotionSweepstakesWinnersValue;

        if ( percentOfTotal )
        {
          double percentage = (double)totalWinners / (double)100;

          // use standard rounding per bug 12859
          totalWinners = (int)Math.round( winnerPool.size() * percentage );
        }

        totalWinners = checkExistingWinners( detachedSweepstake, totalWinners, isSeperate, isGiverType );

      }
    }
    else
    {

      totalWinners = promotionSweepstakesWinnersValue;

      if ( percentOfTotal )
      {
        double percentage = (double)totalWinners / (double)100;

        // use standard rounding per bug 12859
        totalWinners = (int)Math.round( winnerPool.size() * percentage );
      }

      totalWinners = checkExistingWinners( detachedSweepstake, totalWinners, isSeperate, isGiverType );

    }

    return totalWinners;

  }

  /**
   * Check the sweepstake for existing winners and whether or not any of them have been removed.
   * 
   * @param promoSweepstake Promotion sweepstake
   * @param totalWinners Total number of winners needed without including existing
   * @param isSeperate Is this a recognition sweepstakes with seperate giver & receiver
   * @param isGiverType Is this a giver type
   * @return int Number of winners needed after considering existing and removed winners
   */
  private int checkExistingWinners( PromotionSweepstake promoSweepstake, int totalWinners, boolean isSeperate, boolean isGiverType )
  {

    Set currentWinners = promoSweepstake.getWinners();

    Iterator iter = currentWinners.iterator();
    int nonRemovedWinners = 0;

    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();

      if ( isSeperate )
      {
        if ( isGiverType && ( PromotionSweepstakeWinner.GIVER_TYPE.equals( winner.getWinnerType() ) || winner.getWinnerType().equalsIgnoreCase( PromotionSweepstakeWinner.NOMINATOR_TYPE ) )
            || !isGiverType && ( PromotionSweepstakeWinner.RECEIVER_TYPE.equals( winner.getWinnerType() ) || winner.getWinnerType().equalsIgnoreCase( PromotionSweepstakeWinner.NOMINEE_TYPE ) ) )
        {
          if ( !winner.isRemoved() )
          {
            nonRemovedWinners++;
          }
        }
        /*
         * Todo: For Nomination if ( ( isGiverType && PromotionSweepstakeWinner.GIVER_TYPE.equals(
         * winner.getWinnerType() ) ) || ( !isGiverType &&
         * PromotionSweepstakeWinner.RECEIVER_TYPE.equals( winner .getWinnerType() ) ) ) { if (
         * !winner.isRemoved() ) { nonRemovedWinners++; } }
         */
      }
      else
      {
        if ( !winner.isRemoved() )
        {
          nonRemovedWinners++;
        }
      }
    }

    // This tells us that how many winners we need to lookup.
    // For example: If we need 10 winners, but we only have 8 'nonRemovedWinners'
    // - then we still need 2 more winners
    return totalWinners - nonRemovedWinners;
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
        Object temp = iter.next();
        if ( temp instanceof Participant )
        {
          Participant pax = ( (Activity)temp ).getParticipant();
          if ( pax.getStatus() != null && pax.getStatus().getCode() != null && !pax.getStatus().getCode().equals( ParticipantStatus.ACTIVE ) )
          {
            iter.remove();
          }
          else if ( pax.getSuspensionStatus() != null && !pax.getSuspensionStatus().getCode().equals( ParticipantSuspensionStatus.NONE ) )
          {
            iter.remove();
          }
        }
        /*
         * else if ( temp instanceof SweepstakeWinnerPoolValue ) { // do nothing. The exclusions are
         * already done in the query when the winnerList is // populated. // See ActivityDAOImpl
         * sweepSqlBottom. }
         */
      }
    }
  }

  /**
   * Set activity service
   * 
   * @param activityService
   */
  public void setActivityService( ActivityService activityService )
  {
    this.activityService = activityService;
  }

  /**
   * Set claim service
   * 
   * @param claimService
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  /**
   * Get the winner total for ProductClaim promotion sweepstakes with giver and receiver seperate
   * 
   * @param detachedSweepstake
   * @param replacementWinnersTotal Number of winners requested for replacement purposes
   * @param promotionSweepstakesWinnersValue Total number of winners specified in promotion
   *          sweepstakes OR percentage value - both found promotion (example:
   *          recognitionPromotion.getSweepstakesPrimaryWinners().intValue())
   * @param winnerPool Pool of eligible winners
   * @param percentOfTotal Total winners to be determined as a percent of eligible winners (note
   *          promotionSweepstakesWinnersTotal is used as percentage and taken from same value)
   * @param isSeperate Is this a recognition promotion sweepstake with seperate giver and receiver
   * @param isGiverType Is this a giverType (used for recognition only)
   * @return int Total number of winners
   */
  public int getProductClaimWinnerTotal( PromotionSweepstake detachedSweepstake,
                                         int replacementWinnersTotal,
                                         int promotionSweepstakesWinnersValue,
                                         List winnerPool,
                                         boolean percentOfTotal,
                                         boolean isSeperate,
                                         boolean isGiverType )
  {
    // Check winner replacement total - if more than zero
    // this allows an override to take place for replacement purposes to get
    // a lower number of winners than the sweepstake has been setup for.
    // If the replacement number specified is more than the sweepstake setting
    // it will only return the value that the sweepstake was originally setup for.

    if ( replacementWinnersTotal > 0 )
    {
      if ( replacementWinnersTotal > promotionSweepstakesWinnersValue )
      {
        // do not allow more than specified winnerPool
        return promotionSweepstakesWinnersValue;
      }
      return replacementWinnersTotal;
    }

    int totalWinners = promotionSweepstakesWinnersValue;

    if ( percentOfTotal )
    {
      double percentage = (double)totalWinners / (double)100;

      // use standard rounding per bug 12859
      totalWinners = (int)Math.round( winnerPool.size() * percentage );
    }

    totalWinners = checkExistingProductClaimWinners( detachedSweepstake, totalWinners, isSeperate, isGiverType );

    return totalWinners;

  }

  /**
   * Check the sweepstake for existing winners and whether or not any of them have been removed.
   * 
   * @param promoSweepstake Promotion sweepstake
   * @param totalWinners Total number of winners needed without including existing
   * @param isSeperate Is this a recognition sweepstakes with seperate giver & receiver
   * @param isGiverType Is this a giver type
   * @return int Number of winners needed after considering existing and removed winners
   */
  private int checkExistingProductClaimWinners( PromotionSweepstake promoSweepstake, int totalWinners, boolean isSeperate, boolean isGiverType )
  {

    Set currentWinners = promoSweepstake.getWinners();

    Iterator iter = currentWinners.iterator();
    int nonRemovedWinners = 0;

    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();

      if ( isSeperate )
      {
        if ( isGiverType && PromotionSweepstakeWinner.SUBMITTERS_TYPE.equals( winner.getWinnerType() ) || !isGiverType && PromotionSweepstakeWinner.TEAM_MEMBERS_TYPE.equals( winner.getWinnerType() ) )
        {
          if ( !winner.isRemoved() )
          {
            nonRemovedWinners++;
          }
        }
      }
      else
      {
        if ( !winner.isRemoved() )
        {
          nonRemovedWinners++;
        }
      }
    }

    // This tells us that how many winners we need to lookup.
    // For example: If we need 10 winners, but we only have 8 'nonRemovedWinners'
    // - then we still need 2 more winners
    return totalWinners - nonRemovedWinners;
  }

  /**
   * Remove inactive or supsended participants since they are not elible to win
   * 
   * @param winnerPool
   */
  private void filterProductClaimWinnerPool( List winnerPool )
  {
    if ( winnerPool != null && !winnerPool.isEmpty() )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        // check submitter
        SweepStakeWinnerParticipantValue sweepStakeWinnerParticipantValue = (SweepStakeWinnerParticipantValue)iter.next();
        Participant pax = sweepStakeWinnerParticipantValue.getParticipant();
        if ( pax != null )
        {
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

  public List selectProductClaimWinners( PromotionSweepstake promoSweepstake, List winnerPool, int totalWinners, String eligibilityType, String multiAwardType )
  {

    filterProductClaimWinnerPool( winnerPool );
    List winners = new ArrayList();

    // Used if totalWinners is not reach and multiple winners is allowed
    List winnerPoolCopy = new ArrayList( winnerPool );

    if ( winnerPool.size() == 0 )
    {
      // There are no eligible winners
      return winners;
    }

    boolean allowMultiple = SweepstakesMultipleAwardsType.MULTIPLE_CODE.equals( multiAwardType );
    boolean checkTimePeriod = SweepstakesMultipleAwardsType.PERIOD_CODE.equals( multiAwardType );

    // Create a set of removed winners from current winners to check against
    Set currentWinners = promoSweepstake.getWinners();
    Set removedWinningPax = new HashSet();
    Set winningParticipants = new HashSet(); // Used when pax aren't allowed multiple wins
    Set currentWinningPax = new HashSet(); // Used only for separate drawings
    Iterator iter = currentWinners.iterator();
    if ( !allowMultiple )
    {
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
    }
    int startTotalWinners = totalWinners;
    // Loop until we've exhausted all possible winners or we've met our winner quota
    while ( winnerPool.size() > 0 && totalWinners > 0 )
    {
      int winnerListId = (int) ( Math.random() * winnerPool.size() );

      // Activity activity = (Activity)winnerPool.get( winnerListId );
      // Claim claim = (Claim)winnerPool.get( winnerListId );
      SweepStakeWinnerParticipantValue sweepStakeWinnerParticipantValue = (SweepStakeWinnerParticipantValue)winnerPool.get( winnerListId );
      Participant pax = sweepStakeWinnerParticipantValue.getParticipant();
      PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();
      // TODO double check whter here you need participants also
      winner.setParticipant( pax );
      winner.setPromotionSweepstake( promoSweepstake );
      // winner.setWinnerType( sweepStakeWinnerParticipantValue.getEligibilityType());
      if ( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE.equals( sweepStakeWinnerParticipantValue.getEligibilityType() ) )
      {
        winner.setWinnerType( PromotionSweepstakeWinner.SUBMITTERS_TYPE );
      }
      else
      {
        winner.setWinnerType( PromotionSweepstakeWinner.TEAM_MEMBERS_TYPE );
      }
      // If the winner has already been flagged as removed, then look again
      if ( removedWinningPax.contains( winner.getParticipant() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }

      // If we don't allow a pax to win multiple times per drawing, make sure
      // the current winner hasn't won yet.
      if ( !allowMultiple )
      {

        // If multiples is not allowed and eligibilityType is givers only, receivers only, and
        // combined drawings
        if ( !SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE.equals( eligibilityType ) )
        {
          if ( winningParticipants.contains( winner.getParticipant() ) )
          {
            // Take the entry out of the pool now since we can't use it
            winnerPool.remove( winnerListId );
            continue;
          }
          // The eligibilityType is a givers and receivers separate drawing
        }
        else
        {

          // If pax can only win once total (as giver or receiver)
          if ( checkTimePeriod )
          {
            if ( winningParticipants.contains( winner.getParticipant() ) )
            {
              // Take the entry out of the pool now since we can't use it
              winnerPool.remove( winnerListId );
              continue;
            }

            // If the pax can win once per drawing (i.e. once as giver and once as receiver)
          }
          else
          {
            // If the user has won already we need to make sure it hasn't been the winner type
            // as the current winner (i.e. if they won already then it can be only once as a giver
            // and
            // once as receiver)

            // check current drawing
            if ( currentWinningPax.contains( winner.getParticipant() ) )
            {
              // Take the entry out of the pool now since we can't use it
              winnerPool.remove( winnerListId );
              continue;
            }

            // check previous drawing
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
          }

        }

      }

      winners.add( winner );
      promoSweepstake.addWinner( winner );
      winningParticipants.add( winner.getParticipant() );
      currentWinningPax.add( winner.getParticipant() );

      // Take the entry out of the pool now that it has been selected
      winnerPool.remove( winnerListId );
      totalWinners--;
    }

    // If totalWinners hasn't been met and multiple wins are allowed, then
    // make a recursive call to fulfill all the winners.
    if ( totalWinners > 0 && allowMultiple && startTotalWinners != totalWinners )
    {
      List fillerWinners = selectProductClaimWinners( promoSweepstake, winnerPoolCopy, totalWinners, eligibilityType, multiAwardType );

      Iterator fillerWinnersIter = fillerWinners.iterator();
      while ( fillerWinnersIter.hasNext() )
      {
        winners.add( fillerWinnersIter.next() );
      }
    }

    return winners;
  }

  public ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

}
