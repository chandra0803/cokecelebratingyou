
package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.activity.hibernate.NominationActivityQueryConstraint;
import com.biperf.core.domain.activity.AbstractRecognitionActivity;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.strategy.WinnersListStrategy;
import com.biperf.core.value.SweepstakeWinnerPoolValue;

/**
 * RecognitionWinnersListStrategy.
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
 * <td>reddy</td>
 * <td>Apr 11, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RecognitionWinnersListStrategy extends AbstractPromotionWinnersListStrategy implements WinnersListStrategy
{

  /* Bug # 34020 start */
  private static final Log logger = LogFactory.getLog( RecognitionWinnersListStrategy.class );
  /* Bug # 34020 end */

  private ParticipantService participantService;

  /**
   * @param promotion
   * @param promoSweepstake
   * @param giverWinnerReplacementTotal
   * @param receiverWinnerReplacementTotal
   * @return List
   */
  public List buildWinnersList( Promotion promotion, PromotionSweepstake promoSweepstake, int giverWinnerReplacementTotal, int receiverWinnerReplacementTotal )
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
    AbstractRecognitionPromotion abstractRecognitionPromotion = (AbstractRecognitionPromotion)promotion;

    SweepstakesWinnerEligibilityType eligibilityType = abstractRecognitionPromotion.getSweepstakesWinnerEligibilityType();
    String eligibilityTypeCode = eligibilityType.getCode();
    String multiAwardTypeCode = abstractRecognitionPromotion.getSweepstakesMultipleAwardType().getCode();

    boolean percentOfGiversTotal = false;
    if ( abstractRecognitionPromotion.getSweepstakesPrimaryBasisType() != null )
    {
      percentOfGiversTotal = SweepstakesWinnersType.PERCENTAGE_CODE.equals( abstractRecognitionPromotion.getSweepstakesPrimaryBasisType().getCode() );
    }
    boolean percentOfReceiversTotal = false;
    if ( abstractRecognitionPromotion.getSweepstakesSecondaryBasisType() != null )
    {
      percentOfReceiversTotal = SweepstakesWinnersType.PERCENTAGE_CODE.equals( abstractRecognitionPromotion.getSweepstakesSecondaryBasisType().getCode() );
    }

    // this is to fix the bug# 10783
    int sweepstakeGiversWinnersCount = 0;
    if ( abstractRecognitionPromotion.getSweepstakesPrimaryWinners() != null )
    {
      sweepstakeGiversWinnersCount = abstractRecognitionPromotion.getSweepstakesPrimaryWinners().intValue();
    }

    int sweepstakeReceiversWinnersCount = 0;
    if ( abstractRecognitionPromotion.getSweepstakesSecondaryWinners() != null )
    {
      sweepstakeReceiversWinnersCount = abstractRecognitionPromotion.getSweepstakesSecondaryWinners().intValue();
    }

    // givers only
    if ( eligibilityType.isPrimaryOnly() )
    {
      // Get activities for winner pool
      List winnerPool = getPrimaryOnlyNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Get total number of winners that need to be selected for the promosweepstake
      // taking in consideration the replacement number request
      int totalGiversWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal, sweepstakeGiversWinnersCount, winnerPool, percentOfGiversTotal, isNewListCreate );

      // Get winners
      winners = selectWinners( promoSweepstake, winnerPool, totalGiversWinners, eligibilityTypeCode, multiAwardTypeCode );
    }

    if ( eligibilityType.isSecondaryOnly() )
    {
      // get receiver activities for the test promo
      List winnerPool = getSecondaryOnlyNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Determine the total number of winners
      int totalReceiversWinners = getWinnerTotal( promoSweepstake, receiverWinnerReplacementTotal, sweepstakeReceiversWinnersCount, winnerPool, percentOfReceiversTotal, isNewListCreate );

      winners = selectWinners( promoSweepstake, winnerPool, totalReceiversWinners, eligibilityTypeCode, multiAwardTypeCode );
    }

    if ( abstractRecognitionPromotion.isRecognitionPromotion() && eligibilityType.isPrimarySecondaryCombined() )
    {
      // get activities for the test promo
      List winnerPool = getPrimaryAndSecondaryNominationActivityList( abstractRecognitionPromotion, promoSweepstake );
      winnerPool = removeDuplicateAcivities( winnerPool );

      /* Bug # 34020 start */
      logger.error( " >>>>>> SWEEPSTAKES buildWinnersList.isPrimarySecondaryCombined  winnerPool.size = " + winnerPool.size() );
      /* Bug # 34020 end */

      if ( promoSweepstake.getWinners().size() == 0 )
      {
        // Determine the total number of winners
        int totalWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal, sweepstakeGiversWinnersCount, winnerPool, percentOfGiversTotal, isNewListCreate );

        winners = selectWinners( promoSweepstake, winnerPool, totalWinners, eligibilityTypeCode, multiAwardTypeCode );
      }
      else
      {

        // Determine the total number of winners
        List winnerPoolCopy = new ArrayList( winnerPool );
        List replacedGiverwinners = new ArrayList();
        List replacedReciverwinners = new ArrayList();
        if ( giverWinnerReplacementTotal > 0 )
        {
          int totalGiverWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal, sweepstakeGiversWinnersCount, winnerPool, percentOfGiversTotal, false, true, isNewListCreate );
          replacedGiverwinners = selectWinnersForReplacement( promoSweepstake, winnerPool, totalGiverWinners, eligibilityTypeCode, multiAwardTypeCode, true );

        }
        if ( receiverWinnerReplacementTotal > 0 )
        {
          int totalReceiverWinners = getWinnerTotal( promoSweepstake,
                                                     receiverWinnerReplacementTotal,
                                                     sweepstakeReceiversWinnersCount,
                                                     winnerPoolCopy,
                                                     percentOfReceiversTotal,
                                                     false,
                                                     false,
                                                     isNewListCreate );

          replacedReciverwinners = selectWinnersForReplacement( promoSweepstake, winnerPoolCopy, totalReceiverWinners, eligibilityTypeCode, multiAwardTypeCode, false );
        }

        replacedGiverwinners.addAll( replacedReciverwinners );
        winners = replacedGiverwinners;

      }
      // Determine the total number of winners
      /*
       * int totalWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal,
       * sweepstakeGiversWinnersCount, winnerPool, percentOfGiversTotal ); winners = selectWinners(
       * promoSweepstake, winnerPool, totalWinners, eligibilityTypeCode, multiAwardTypeCode );
       */
    }
    else if ( abstractRecognitionPromotion.isNominationPromotion() && eligibilityType.isPrimarySecondaryCombined() )
    {
      // get receiver activities for the test promo
      List winnerPool = getPrimaryAndSecondaryNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Determine the total number of winners
      int totalGiversWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal, sweepstakeGiversWinnersCount, winnerPool, percentOfGiversTotal, isNewListCreate );

      List giverWinners = selectWinners( promoSweepstake, winnerPool, totalGiversWinners, eligibilityTypeCode, multiAwardTypeCode );

      int totalReceiversWinners = getWinnerTotal( promoSweepstake, receiverWinnerReplacementTotal, sweepstakeReceiversWinnersCount, winnerPool, percentOfReceiversTotal, isNewListCreate );

      List receiverWinners = selectWinners( promoSweepstake, winnerPool, totalReceiversWinners, eligibilityTypeCode, multiAwardTypeCode );

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

    if ( abstractRecognitionPromotion.isRecognitionPromotion() && eligibilityType.isPrimarySecondarySeparate() )
    {
      // get giver activities for the test promo
      List giverWinnerPool = getPrimaryOnlyNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Determine the total number of winners
      int totalGiversWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal, sweepstakeGiversWinnersCount, giverWinnerPool, percentOfGiversTotal, true, true, isNewListCreate );

      List giverWinners = selectWinners( promoSweepstake, giverWinnerPool, totalGiversWinners, eligibilityTypeCode, multiAwardTypeCode );

      // get receiver activities for the test promo
      List receiverWinnerPool = getSecondaryOnlyNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Determine the total number of winners
      int totalReceiversWinners = getWinnerTotal( promoSweepstake,
                                                  receiverWinnerReplacementTotal,
                                                  sweepstakeReceiversWinnersCount,
                                                  receiverWinnerPool,
                                                  percentOfReceiversTotal,
                                                  true,
                                                  false,
                                                  isNewListCreate );

      List receiverWinners = selectWinners( promoSweepstake, receiverWinnerPool, totalReceiversWinners, eligibilityTypeCode, multiAwardTypeCode );

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
    else if ( abstractRecognitionPromotion.isNominationPromotion() && eligibilityType.isPrimarySecondarySeparate() )
    {
      // get giver activities for the test promo
      List giverWinnerPool = getPrimaryOnlyNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Determine the total number of winners
      int totalGiversWinners = getWinnerTotal( promoSweepstake, giverWinnerReplacementTotal, sweepstakeGiversWinnersCount, giverWinnerPool, percentOfGiversTotal, true, true, isNewListCreate );

      List giverWinners = selectWinners( promoSweepstake, giverWinnerPool, totalGiversWinners, eligibilityTypeCode, multiAwardTypeCode );

      // get receiver activities for the test promo
      List receiverWinnerPool = getSecondaryOnlyNominationActivityList( abstractRecognitionPromotion, promoSweepstake );

      // Determine the total number of winners
      int totalReceiversWinners = getWinnerTotal( promoSweepstake,
                                                  receiverWinnerReplacementTotal,
                                                  sweepstakeReceiversWinnersCount,
                                                  receiverWinnerPool,
                                                  percentOfReceiversTotal,
                                                  true,
                                                  false,
                                                  isNewListCreate );

      List receiverWinners = selectWinners( promoSweepstake, receiverWinnerPool, totalReceiversWinners, eligibilityTypeCode, multiAwardTypeCode );

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

  public List getPrimaryOnlyNominationActivityList( Promotion promotion, PromotionSweepstake promoSweepstake )
  {
    List winnerPool;
    if ( promotion.isRecognitionPromotion() )
    {
      winnerPool = activityService.getGiversOnlyRecognitionActivityList( promotion.getId(), promoSweepstake );
    }
    else
    {
      winnerPool = getNominationActivityList( promotion.getId(), NominationActivityQueryConstraint.GIVERS_ONLY, promoSweepstake );
    }

    return winnerPool;
  }

  public List getSecondaryOnlyNominationActivityList( Promotion promotion, PromotionSweepstake promoSweepstake )
  {
    List winnerPool;
    if ( promotion.isRecognitionPromotion() )
    {
      winnerPool = activityService.getReceiversOnlyRecognitionActivityList( promotion.getId(), promoSweepstake );
    }
    else
    {
      winnerPool = getNominationActivityList( promotion.getId(), NominationActivityQueryConstraint.RECEIVERS_ONLY, promoSweepstake );
    }

    return winnerPool;
  }

  public List getPrimaryAndSecondaryNominationActivityList( Promotion promotion, PromotionSweepstake promoSweepstake )
  {
    List winnerPool;
    if ( promotion.isRecognitionPromotion() )
    {
      winnerPool = activityService.getGiversAndReceiversRecognitionActivityList( promotion.getId(), promoSweepstake );
    }
    else
    {
      winnerPool = getNominationActivityList( promotion.getId(), NominationActivityQueryConstraint.GIVERS_AND_RECEIVERS, promoSweepstake );
    }

    return winnerPool;
  }

  public List getNominationActivityList( Long promotionId, Boolean poolType, PromotionSweepstake promoSweepstake )
  {
    NominationActivityQueryConstraint recPromoQueryConstraint = new NominationActivityQueryConstraint();
    recPromoQueryConstraint.setGiverOrReceiver( poolType );
    recPromoQueryConstraint.setPromotionId( promotionId );
    recPromoQueryConstraint.setStartDate( promoSweepstake.getStartDate() );
    recPromoQueryConstraint.setEndDate( promoSweepstake.getEndDate() );
    recPromoQueryConstraint.setPosted( Boolean.TRUE );

    return activityService.getActivityList( recPromoQueryConstraint );
  }

  /* *****Bug # 34020 start - to try and speed this up */
  /**
   * @param originalList
   * @return uniqueActivityList
   */
  public static List removeDuplicateAcivities( List originalList )
  {
    List copiedList = new ArrayList( originalList );
    List uniqueActivityList = new ArrayList();
    HashMap uniqueActivityHash1 = new HashMap(); //
    HashMap uniqueActivityHash2 = new HashMap();

    for ( int i = 0; i < originalList.size(); i++ )
    {
      Object temp = originalList.get( i );
      if ( temp instanceof AbstractRecognitionActivity )
      {
        AbstractRecognitionActivity activity = (AbstractRecognitionActivity)originalList.get( i );
        for ( int j = 0; j < copiedList.size(); j++ )
        {
          AbstractRecognitionActivity innerActivity = (AbstractRecognitionActivity)originalList.get( i );
          if ( activity.getParticipant().getId().longValue() == innerActivity.getParticipant().getId().longValue() && activity.isSubmitter() )
          {
            if ( !newSearchForExistingWinner( uniqueActivityHash1, innerActivity ) )
            {
              uniqueActivityList.add( innerActivity );
              uniqueActivityHash1.put( innerActivity.getParticipant().getId(), innerActivity.getParticipant().getId() ); //
              break;
            }

          }
          else if ( activity.getParticipant().getId().longValue() == innerActivity.getParticipant().getId().longValue() && !activity.isSubmitter() )
          {
            if ( !newSearchForExistingWinner( uniqueActivityHash2, innerActivity ) )
            {
              uniqueActivityList.add( innerActivity );
              uniqueActivityHash2.put( innerActivity.getParticipant().getId(), innerActivity.getParticipant().getId() ); //
              break;
            }
          }
        }
      }
      else if ( temp instanceof SweepstakeWinnerPoolValue )
      {
        SweepstakeWinnerPoolValue activity = (SweepstakeWinnerPoolValue)originalList.get( i );
        for ( int j = 0; j < copiedList.size(); j++ )
        {
          SweepstakeWinnerPoolValue innerActivity = (SweepstakeWinnerPoolValue)originalList.get( i );
          if ( activity.getUserId() == innerActivity.getUserId() && activity.isSubmitter() )
          {
            if ( !newSearchForExistingWinnerPoolValue( uniqueActivityHash1, innerActivity ) )
            {
              uniqueActivityList.add( innerActivity );
              uniqueActivityHash1.put( innerActivity.getUserId(), innerActivity.getUserId() ); //
              break;
            }

          }
          else if ( activity.getUserId() == innerActivity.getUserId() && !activity.isSubmitter() )
          {
            if ( !newSearchForExistingWinnerPoolValue( uniqueActivityHash2, innerActivity ) )
            {
              uniqueActivityList.add( innerActivity );
              uniqueActivityHash2.put( innerActivity.getUserId(), innerActivity.getUserId() ); //
              break;
            }
          }
        }
      }
    }
    return uniqueActivityList;
  }

  private static boolean newSearchForExistingWinner( HashMap uniqueActivityHash, AbstractRecognitionActivity activity )
  {
    boolean isSame = false;
    if ( uniqueActivityHash.get( activity.getParticipant().getId() ) != null )
    {
      isSame = true;
    }
    return isSame;
  }

  private static boolean newSearchForExistingWinnerPoolValue( HashMap uniqueActivityHash, SweepstakeWinnerPoolValue activity )
  {
    boolean isSame = false;
    if ( uniqueActivityHash.get( activity.getUserId() ) != null )
    {
      isSame = true;
    }
    return isSame;
  }

  /* *****Bug # 34020 end */

  /**
   * @param promoSweepstake
   * @param winnerPool
   * @param totalWinners 
   * @param eligibilityType
   * @param multiAwardType
   * @param isGiversReplacement 
   * @return List
   */
  public List selectWinnersForReplacement( PromotionSweepstake promoSweepstake, List winnerPool, int totalWinners, String eligibilityType, String multiAwardType, boolean isGiversReplacement )
  {

    activePaxWinnerPool( winnerPool );
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
    // Filter out the Removed paxs from the Pool
    if ( removedWinningPax.size() > 0 )
    {
      ArrayList winnerPoolList = new ArrayList( winnerPool );
      for ( Iterator removedIterator = removedWinningPax.iterator(); removedIterator.hasNext(); )
      {
        Participant pax = (Participant)removedIterator.next();
        for ( int i = 0; i < winnerPoolList.size(); i++ )
        {
          Activity activity = (Activity)winnerPoolList.get( i );
          if ( pax.getId().longValue() == activity.getParticipant().getId().longValue() )
          {
            winnerPool.remove( activity );
            continue;
          }
        }
      }
    }

    /*
     * if(winningParticipants.size()>0) { ArrayList winnerPoolList=new ArrayList( winnerPool ); for
     * ( Iterator winningIterator = winningParticipants.iterator(); winningIterator.hasNext(); ) {
     * Participant pax=(Participant)winningIterator.next(); for (int i=0;i<winnerPoolList.size();i++
     * ) { Activity activity = (Activity) winnerPoolList.get(i);
     * if(pax.getId().longValue()==activity.getParticipant().getId().longValue()) {
     * winnerPool.remove( activity ); continue; } } } }
     */
    int startTotalWinners = totalWinners;
    // if totalWinners hasn't been met and multiple wins are allowed, then
    // Checks to see that totalWinners changed - if not everyone is invalid and shouldn't recurse

    while ( winnerPool.size() > 0 && totalWinners > 0 )
    {
      int winnerListId = (int) ( Math.random() * winnerPool.size() );
      SweepstakeWinnerPoolValue sweepstakeWinnerPoolValue = (SweepstakeWinnerPoolValue)winnerPool.get( winnerListId );
      Activity activity = activityService.getActivityById( sweepstakeWinnerPoolValue.getActivityId() );

      PromotionSweepstakeWinner winner = new PromotionSweepstakeWinner();
      winner.setParticipant( activity.getParticipant() );
      winner.setPromotionSweepstake( promoSweepstake );
      // If the winner has already been flagged as removed, then look again
      if ( removedWinningPax.contains( winner.getParticipant() ) )
      {
        // Take the entry out of the pool now since we can't use it
        winnerPool.remove( winnerListId );
        continue;
      }

      // If this is a AbstractRecognitionActivity, then mark the winner as a primary or secondary
      if ( activity instanceof AbstractRecognitionActivity )
      {
        if ( ( (AbstractRecognitionActivity)activity ).isSubmitter() && isGiversReplacement )
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
        else if ( ! ( (AbstractRecognitionActivity)activity ).isSubmitter() && !isGiversReplacement )
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
        else
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

      winners.add( winner );
      promoSweepstake.addWinner( winner );
      winningParticipants.add( winner.getParticipant() );
      currentWinningPax.add( winner.getParticipant() );

      // Take the entry out of the pool now that it has been selected
      winnerPool.remove( winnerListId );
      totalWinners--;
    }

    if ( totalWinners > 0 && startTotalWinners != totalWinners && allowMultiple )
    {
      List fillerWinners = selectWinnersForReplacement( promoSweepstake, winnerPoolCopy, totalWinners, eligibilityType, multiAwardType, isGiversReplacement );
      Iterator fillerWinnersIter = fillerWinners.iterator();
      while ( fillerWinnersIter.hasNext() )
      {
        winners.add( fillerWinnersIter.next() );
      }
    }

    return winners;

  }

  private void activePaxWinnerPool( List winnerPool )
  {
    if ( winnerPool != null && !winnerPool.isEmpty() )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        SweepstakeWinnerPoolValue sweepstakeWinnerPoolValue = (SweepstakeWinnerPoolValue)iter.next();

        Participant pax = participantService.getParticipantById( sweepstakeWinnerPoolValue.getUserId() );

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
        Object temp = iter.next();
        Long sweepUserId = null;
        if ( temp instanceof Participant )
        {
          sweepUserId = ( (Activity)temp ).getParticipant().getId();
        }
        else if ( temp instanceof SweepstakeWinnerPoolValue )
        {
          sweepUserId = ( (SweepstakeWinnerPoolValue)temp ).getUserId();
        }
        if ( removedPax.size() > 0 )
        {
          for ( Iterator iterator = removedPax.iterator(); iterator.hasNext(); )
          {
            Participant paxRemoved = (Participant)iterator.next();
            if ( sweepUserId == paxRemoved.getId().longValue() )
            {
              iter.remove();
              break;
            }
          }

        }
      }
    }

  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
}
