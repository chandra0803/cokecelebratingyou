
package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.activity.hibernate.NominationActivityQueryConstraint;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.dao.promotion.PromotionSweepstakeDAO;
import com.biperf.core.domain.activity.AbstractRecognitionActivity;
import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.activity.SweepstakesAwardAmountActivity;
import com.biperf.core.domain.activity.SweepstakesMerchLevelActivity;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.process.JournalSweepstakesMailingProcess;
import com.biperf.core.process.MerchOrderCreateProcess;
import com.biperf.core.process.SweepstakesAwardProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.journal.DepositBillingCodeStrategy;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.service.promotion.PromotionSweepstakeUpdateAssociation;
import com.biperf.core.service.promotion.PromotionSweepstakeWinnersUpdateAssociation;
import com.biperf.core.service.promotion.WinnersListStrategyFactory;
import com.biperf.core.service.promotion.engine.SweepstakesFacts;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.impl.BadgeWinnersListStrategy;
import com.biperf.core.strategy.impl.ProductClaimWinnersListStrategy;
import com.biperf.core.strategy.impl.QuizWinnersListStrategy;
import com.biperf.core.strategy.impl.RecognitionWinnersListStrategy;
import com.biperf.core.strategy.impl.SurveyWinnersListStrategy;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.SweepstakeWinnerPoolValue;

/**
 * PromotionSweepstakeServiceImpl.
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
 * <td>asondgeroth</td>
 * <td>Nov 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakeServiceImpl implements PromotionSweepstakeService
{
  private PromotionSweepstakeDAO promotionSweepstakeDAO = null;
  private PromotionService promotionService = null;
  private PromotionEngineService promotionEngineService = null;
  private ActivityService activityService;
  private WinnersListStrategyFactory winnersListStrategyFactory;
  private PromoMerchCountryDAO promoMerchCountryDAO = null;
  private MerchOrderDAO merchOrderDAO = null;
  private MerchLevelService merchLevelService = null;

  private ProcessService processService;

  private ParticipantSurveyService participantSurveyService;
  private GamificationService gamificationService;

  private DepositBillingCodeStrategy billingCodeStrategy; // WIP# 25130

  /**
   * @param activityService value for activityService property
   */
  public void setActivityService( ActivityService activityService )
  {
    this.activityService = activityService;
  }

  /**
   * @param promotionEngineService value for promotionEngineService property
   */
  public void setPromotionEngineService( PromotionEngineService promotionEngineService )
  {
    this.promotionEngineService = promotionEngineService;
  }

  /**
   * Set the promotionSweepstakeDAO through dependency injection (IoC).
   * 
   * @param promotionSweepstakeDAO
   */
  public void setPromotionSweepstakeDAO( PromotionSweepstakeDAO promotionSweepstakeDAO )
  {
    this.promotionSweepstakeDAO = promotionSweepstakeDAO;
  }

  /**
   * Saves the hierarchy to the database.
   * 
   * @param promotionSweepstake
   * @return PromotionSweepstake
   */
  public PromotionSweepstake save( PromotionSweepstake promotionSweepstake )
  {
    return this.promotionSweepstakeDAO.save( promotionSweepstake );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionSweepstakeService#delete(com.biperf.core.domain.promotion.PromotionSweepstake)
   * @param promotionSweepstake
   */
  public void delete( PromotionSweepstake promotionSweepstake )
  {
    this.promotionSweepstakeDAO.delete( promotionSweepstake );
  }

  /**
   * Get the hierarchy from the database by the id.
   * 
   * @param id
   * @return PromotionSweepstake
   */
  public PromotionSweepstake getPromotionSweepstakeById( Long id )
  {
    return this.promotionSweepstakeDAO.getPromotionSweepstakeById( id );
  }

  /**
   * Retrieves all the PromotionSweepstakes from the database.
   * 
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakes()
  {
    return this.promotionSweepstakeDAO.getAllPromotionSweepstakes();
  }

  /**
   * Retrieves the most recently ended PromotionSweepstake
   * 
   * @param promotionId
   * @return promotionSweepstake
   */
  public PromotionSweepstake getMostRecentlyEndedPromotionSweepstake( Long promotionId )
  {
    return (PromotionSweepstake)this.promotionSweepstakeDAO.getAllPromotionSweepstakesListByPromotionIdSortedByDate( promotionId ).get( 0 );
  }

  /**
   * Retrieves all the PromotionSweepstakes sorted by date from the database.
   * 
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakesListSortedByDate()
  {
    return this.promotionSweepstakeDAO.getAllPromotionSweepstakesListSortedByDate();
  }

  /* WIP# 25130 Start */
  public void setDepositBillingCodeStrategy( DepositBillingCodeStrategy billingCodeStrategy )
  {
    this.billingCodeStrategy = billingCodeStrategy;
  }
  /* WIP# 25130 End */

  /**
   * Removes pending winners (actually just the flip of a flag on the database)
   * 
   * @param winnerIds of PendingWinnerFormBeans representing winners to be removed
   * @param promotionId id of Promotion
   * @throws UniqueConstraintViolationException
   */
  public Promotion removeWinners( List winnerIds, Long promotionId ) throws UniqueConstraintViolationException
  {
    AssociationRequestCollection assocReqs = new AssociationRequestCollection();
    assocReqs.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
    Promotion promo = promotionService.getPromotionByIdWithAssociations( promotionId, assocReqs );
    PromotionSweepstake sweepstake = null;
    for ( Iterator iter = promo.getPromotionSweepstakes().iterator(); iter.hasNext(); )
    {
      PromotionSweepstake sweep = (PromotionSweepstake)iter.next();
      if ( !sweep.isProcessed() )
      {
        sweepstake = sweep;
        break;
      }
    }

    if ( sweepstake != null && sweepstake.getWinners() != null )
    {
      updateWinnersRemoveFlag( winnerIds, sweepstake.getWinners() );
    }
    // save the promo to save flags
    PromotionSweepstakeWinnersUpdateAssociation updateAssociation = new PromotionSweepstakeWinnersUpdateAssociation( promo );
    return promotionService.savePromotion( promo.getId(), updateAssociation );
  }

  private void updateWinnersRemoveFlag( List winnerIds, Set winners )
  {
    PromotionSweepstakeWinner winner = null;
    // iterate the winners and set flag for any with a matching id in the HashSet
    for ( Iterator winnerIter = winners.iterator(); winnerIter.hasNext(); )
    {
      winner = (PromotionSweepstakeWinner)winnerIter.next();
      if ( winnerIds.contains( winner.getId() ) )
      {
        winner.setRemoved( true );
      }
    }
  }

  /**
   * Removes pending winners (actually just the flip of a flag on the database) and replaces them
   * with new ones
   * 
   * @param winnerIds List of PendingWinnerFormBeans representing winners to be replaced
   * @param promotionId Long id of Promotion
   * @param winnersType 
   * @return Promotion
   * @throws UniqueConstraintViolationException 
   * @throws ServiceErrorException 
   */
  public Promotion replaceWinners( List winnerIds, Long promotionId, List winnersType, int giversDisplayed, int receiversDisplayed ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    AssociationRequestCollection assocReqs = new AssociationRequestCollection();
    assocReqs.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
    Promotion promo = promotionService.getPromotionByIdWithAssociations( promotionId, assocReqs );
    PromotionSweepstake sweepstake = null;
    for ( Iterator iter = promo.getPromotionSweepstakes().iterator(); iter.hasNext(); )
    {
      PromotionSweepstake sweep = (PromotionSweepstake)iter.next();
      if ( !sweep.isProcessed() )
      {
        sweepstake = sweep;
        break;
      }
    }
    // updateWinnersRemoveFlag( winnerIds, sweepstake.getWinners() );

    if ( promo instanceof QuizPromotion || ! ( promo instanceof RecognitionPromotion ) || ! ( promo instanceof NominationPromotion ) || ! ( promo instanceof ProductClaimPromotion ) )
    {
      createWinnersList( promo.getId(),
                         sweepstake,
                         getNumberToReplace( winnerIds, sweepstake, true ),
                         getNumberToReplace( winnerIds, sweepstake, false ),
                         winnerIds,
                         winnersType,
                         giversDisplayed,
                         receiversDisplayed );
    }
    else
    {
      createWinnersList( promo.getId(), sweepstake, getNumberToReplace( winnerIds, sweepstake, true ), getNumberToReplace( winnerIds, sweepstake, false ), winnerIds, winnersType, 0, 0 );
    }

    // save the promo to save flags
    // We did persisting of the sweep stake winners in the createwinnersList method for recognition
    // promos,hence bypassing the follwing code.
    if ( ! ( promo.isRecognitionPromotion() || promo.isNominationPromotion() || promo.isProductClaimPromotion() ) )
    {
      PromotionSweepstakeWinnersUpdateAssociation updateAssociation = new PromotionSweepstakeWinnersUpdateAssociation( promo );
      return promotionService.savePromotion( promo.getId(), updateAssociation );
    }
    return promo;
  }

  private int getNumberToReplace( List ids, PromotionSweepstake sweep, boolean givers )
  {
    int num = 0;
    for ( Iterator iter = sweep.getWinners().iterator(); iter.hasNext(); )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( ids.contains( winner.getId() ) )
      {
        if ( givers && ( winner.getWinnerType().equals( PromotionSweepstakeWinner.GIVER_TYPE ) || winner.getWinnerType().equals( PromotionSweepstakeWinner.SUBMITTERS_TYPE )
            || winner.getWinnerType().equals( PromotionSweepstakeWinner.NOMINATOR_TYPE ) ) )
        {
          num++;
        }
        else if ( !givers && ( winner.getWinnerType().equals( PromotionSweepstakeWinner.RECEIVER_TYPE ) || winner.getWinnerType().equals( PromotionSweepstakeWinner.TEAM_MEMBERS_TYPE )
            || winner.getWinnerType().equals( PromotionSweepstakeWinner.NOMINEE_TYPE ) ) )
        {
          num++;
        }
      }
    }
    return num;
  }

  /**
   * Marks sweepstake as processed and deposits points to each winner's account.
   * 
   * @param promotionId Long id of Promotion
   */
  public Long processAward( Long promotionId ) throws UniqueConstraintViolationException, ServiceErrorExceptionWithRollback
  {
    /*
     * Bug # 34020 start to speed it up AssociationRequestCollection assocReqs = new
     * AssociationRequestCollection(); assocReqs .add( new PromotionAssociationRequest(
     * PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) ); Promotion promo =
     * promotionService.getPromotionByIdWithAssociations( promotionId, assocReqs );
     * PromotionSweepstake sweepstake = null; for ( Iterator iter =
     * promo.getPromotionSweepstakes().iterator(); iter.hasNext(); ) { PromotionSweepstake sweep =
     * (PromotionSweepstake)iter.next(); if ( !sweep.isProcessed() ) { sweepstake = sweep; break; }
     * }
     */
    Promotion promo = promotionService.getPromotionById( promotionId );
    PromotionSweepstake sweepstake = null;
    List sweepstakeDrawingList = promotionSweepstakeDAO.getPromotionSweepstakesByPromotionIdNotProcessed( promotionId );
    sweepstake = (PromotionSweepstake)sweepstakeDrawingList.get( 0 );
    Long winnerSize = new Long( 0 );
    /* Bug # 34020 end */

    sweepstake.setProcessed( true );

    String eligibilityCode = null;
    Long giverAmount = null;
    Long receiverAmount = null;

    if ( promo.isRecognitionPromotion() )
    {
      eligibilityCode = ( (RecognitionPromotion)promo ).getSweepstakesWinnerEligibilityType().getCode();
      giverAmount = ( (RecognitionPromotion)promo ).getSweepstakesPrimaryAwardAmount();
      receiverAmount = ( (RecognitionPromotion)promo ).getSweepstakesSecondaryAwardAmount();

      /* Bug # 34020 start */
      List sweepstakeWinners = promotionSweepstakeDAO.getAllPromotionSweepstakeWinnersByDrawingId( sweepstake.getId() );
      // for ( Iterator winnerIter = sweepstake.getWinners().iterator(); winnerIter.hasNext(); )
      for ( Iterator winnerIter = sweepstakeWinners.iterator(); winnerIter.hasNext(); )
      /* Bug # 34020 end */
      {
        PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)winnerIter.next();
        if ( winner.isRemoved() )
        {
          continue;
        }
        Participant winnerParticipant = winner.getParticipant();
        if ( promo.getAwardType().isMerchandiseAwardType() )
        {
          if ( winnerParticipant.getPrimaryAddress() == null )
          {
            throw new ServiceErrorExceptionWithRollback( winnerParticipant.getNameLFMWithComma() );
          }
        }
        Long awardQuantity = null;
        if ( eligibilityCode.equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_COMBINED_CODE ) || eligibilityCode.equals( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) )
        {
          awardQuantity = giverAmount;
        }
        else if ( eligibilityCode.equals( SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE ) )
        {
          awardQuantity = receiverAmount;
        }
        else
        {
          if ( winner.getWinnerType().equals( PromotionSweepstakeWinner.GIVER_TYPE ) )
          {
            awardQuantity = giverAmount;
          }
          else
          {
            awardQuantity = receiverAmount;
          }
        }

        processSweepstakesPayout( winnerParticipant, awardQuantity, promo, winner );

        winnerSize++;
      }
    }

    else if ( promo.isNominationPromotion() )
    {
      eligibilityCode = ( (NominationPromotion)promo ).getSweepstakesWinnerEligibilityType().getCode();
      giverAmount = ( (NominationPromotion)promo ).getSweepstakesPrimaryAwardAmount();
      receiverAmount = ( (NominationPromotion)promo ).getSweepstakesSecondaryAwardAmount();

      for ( Iterator winnerIter = sweepstake.getWinners().iterator(); winnerIter.hasNext(); )
      {
        PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)winnerIter.next();
        if ( winner.isRemoved() )
        {
          continue;
        }
        Participant winnerParticipant = winner.getParticipant();
        Long awardQuantity = null;
        if ( eligibilityCode.equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_COMBINED_CODE ) || eligibilityCode.equals( SweepstakesWinnerEligibilityType.NOMINATORS_ONLY_CODE ) )
        {
          awardQuantity = giverAmount;
        }
        else if ( eligibilityCode.equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE ) )
        {
          awardQuantity = receiverAmount;
        }
        else
        {
          if ( winner.getWinnerType().equals( PromotionSweepstakeWinner.NOMINEE_TYPE ) )
          {
            awardQuantity = receiverAmount;
          }
          else
          {
            awardQuantity = giverAmount;
          }
        }

        processSweepstakesPayout( winnerParticipant, awardQuantity, promo, winner );
        winnerSize++;

      }
    }

    else if ( promo.isQuizPromotion() || promo.isSurveyPromotion() || promo.isBadgePromotion() )
    {
      for ( Iterator iter = sweepstake.getWinners().iterator(); iter.hasNext(); )
      {
        PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
        if ( winner.isRemoved() )
        {
          continue;
        }

        Participant winnerParticipant = winner.getParticipant();
        Long awardQuantity = promo.getSweepstakesPrimaryAwardAmount();

        processSweepstakesPayout( winnerParticipant, awardQuantity, promo, winner );
        winnerSize++;
      }
    }

    else if ( promo.isProductClaimPromotion() )
    {
      eligibilityCode = ( (ProductClaimPromotion)promo ).getSweepstakesWinnerEligibilityType().getCode();
      giverAmount = ( (ProductClaimPromotion)promo ).getSweepstakesPrimaryAwardAmount();
      receiverAmount = ( (ProductClaimPromotion)promo ).getSweepstakesSecondaryAwardAmount();

      for ( Iterator winnerIter = sweepstake.getWinners().iterator(); winnerIter.hasNext(); )
      {
        PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)winnerIter.next();
        if ( winner.isRemoved() )
        {
          continue;
        }
        Participant winnerParticipant = winner.getParticipant();
        Long awardQuantity = null;
        if ( eligibilityCode.equals( SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE ) || eligibilityCode.equals( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE ) )
        {
          awardQuantity = giverAmount;
        }
        else if ( eligibilityCode.equals( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE ) )
        {
          awardQuantity = receiverAmount;
        }
        else
        {
          if ( winner.getWinnerType().equals( PromotionSweepstakeWinner.SUBMITTERS_TYPE ) )
          {
            awardQuantity = giverAmount;
          }
          else
          {
            awardQuantity = receiverAmount;
          }
        }

        processSweepstakesPayout( winnerParticipant, awardQuantity, promo, winner );
        winnerSize++;

      }
    }

    // save the promo to save flag on sweepstake
    PromotionSweepstakeUpdateAssociation updateAssociation = new PromotionSweepstakeUpdateAssociation( promo );
    promotionService.savePromotion( promo.getId(), updateAssociation );
    return winnerSize;
  }

  public Promotion scheduleProcessAward( Long promotionId ) throws UniqueConstraintViolationException, ServiceErrorExceptionWithRollback
  {
    processService.createOrLoadSystemProcess( JournalSweepstakesMailingProcess.PROCESS_NAME, JournalSweepstakesMailingProcess.BEAN_NAME );
    // schedule job
    Process process = processService.createOrLoadSystemProcess( SweepstakesAwardProcess.PROCESS_NAME, SweepstakesAwardProcess.BEAN_NAME );

    Promotion promo = promotionService.getPromotionById( promotionId );

    LinkedHashMap parameterValueMap = new LinkedHashMap();

    parameterValueMap.put( "promotionId", new String[] { promotionId.toString() } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

    return promo;
  }

  /**
   * Generate sweepstakes activities and process promotion engine
   * 
   * @param winner
   * @param awardQuantity
   * @param promotion
   * @param promotionSweepstakeWinner
   */
  private void processSweepstakesPayout( Participant winner, Long awardQuantity, Promotion promotion, PromotionSweepstakeWinner promotionSweepstakeWinner )
  {
    List activities = null;
    // Generate activities and facts
    // If recogniton and merchandise award, create a SweepstakesMerchLevelActivity object
    if ( promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
    {
      activities = createSweepstakesMerchLevelActivity( winner, promotion, promotionSweepstakeWinner );
    }
    else
    {
      // If participant does not want awards, zero out amount
      if ( winner.getOptOutAwards() )
      {
        awardQuantity = new Long( 0 );
      }

      // create SweepstakesAwardAmountActivity
      activities = createSweepstakesActivities( winner, awardQuantity, promotion, promotionSweepstakeWinner.getPromotionSweepstake() );

    }
    activityService.saveActivities( activities );

    SweepstakesFacts sweepstakesFacts = new SweepstakesFacts();
    sweepstakesFacts.setPromotionSweepstakeWinner( promotionSweepstakeWinner );

    try
    {
      // Immediately process activities
      Set savedPayouts = promotionEngineService.calculatePayoutAndSaveResults( sweepstakesFacts, promotion, winner, PromotionPayoutType.SWEEPSTAKES );
      promotionEngineService.depositSweepstakeApprovedPayouts( promotion, savedPayouts, promotionSweepstakeWinner.getWinnerType() );

    }
    catch( ServiceErrorException e )
    {
      // log.error( e.getServiceErrorsCMText() );
    }
  }

  /**
   * Creates a new sweepstakes and a list of winners for a specified promotion for a given
   * timeframe. Only participants who were/are qualified during the specified timeframe. Only use
   * this when making a new sweepstake.
   * 
   * @param promotionId
   * @param startDate
   * @param endDate
   * @return List
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public List createWinnersList( Long promotionId, Date startDate, Date endDate ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    PromotionSweepstake sweepstake = new PromotionSweepstake();
    sweepstake.setStartDate( startDate );
    sweepstake.setEndDate( endDate );
    return createWinnersList( promotionId, sweepstake );
  }

  /**
   * Creates a list of winners for a specified promotion for the given sweepstake. Only participants
   * who were/are qualified during the specified timeframe. The detachedSweepstake must contain a
   * begin and end date.
   * 
   * @param promotionId
   * @param detachedSweepstake
   */
  public List createWinnersList( Long promotionId, PromotionSweepstake detachedSweepstake ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    return createWinnersList( promotionId, detachedSweepstake, 0, null, null, 0, 0 );
  }

  /**
   * @param promotionId
   * @param detachedSweepstake
   * @param winnerReplacementTotal
   * @param ids 
   * @param winnersType 
   * @return List
   * @throws UniqueConstraintViolationException 
   * @throws ServiceErrorException 
   */
  public List createWinnersList( Long promotionId, PromotionSweepstake detachedSweepstake, int winnerReplacementTotal, List ids, List winnersType, int giversDisplayed, int receiversDisplayed )
      throws UniqueConstraintViolationException, ServiceErrorException
  {
    return createWinnersList( promotionId, detachedSweepstake, winnerReplacementTotal, 0, ids, winnersType, giversDisplayed, receiversDisplayed );
  }

  private void removeUnprocessedSweepstakes( PromotionSweepstake detachedSweepstake, Set promotionSweepstakes )
  {
    // Check to see if this sweepstakes already exists
    if ( !promotionSweepstakes.contains( detachedSweepstake ) )
    {

      // We need to remove any unprocessed sweepstakes because (in the words of The Highlander),
      // "There can be only one."
      Iterator sweepstakeIter = promotionSweepstakes.iterator();
      while ( sweepstakeIter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)sweepstakeIter.next();

        // If there is already an unprocessed sweepstake then we need to remove it
        if ( !sweepstake.isProcessed() )
        {
          sweepstakeIter.remove();
        }
      }
    }
  }

  /**
   * @param promotionId
   * @param detachedSweepstake
   * @param giverWinnerReplacementTotal
   * @param receiverWinnerReplacementTotal
   * @param ids 
   * @param types 
   * @return List
   * @throws UniqueConstraintViolationException 
   * @throws ServiceErrorException 
   */
  public List createWinnersList( Long promotionId,
                                 PromotionSweepstake detachedSweepstake,
                                 int giverWinnerReplacementTotal,
                                 int receiverWinnerReplacementTotal,
                                 List ids,
                                 List types,
                                 int giversDisplayed,
                                 int receiversDisplayed )
      throws UniqueConstraintViolationException, ServiceErrorException
  {
    List winners = new ArrayList();
    List winnerPool = new ArrayList();
    boolean allowMultiple = false;
    // Lookup the promotion
    AssociationRequestCollection assocReqs = new AssociationRequestCollection();
    assocReqs.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
    Promotion promotion = this.promotionService.getPromotionByIdWithAssociations( promotionId, assocReqs );
    Set promotionSweepstakes = promotion.getPromotionSweepstakes();

    // If there is an unprocessed sweepstakes, then removed it
    removeUnprocessedSweepstakes( detachedSweepstake, promotionSweepstakes );
    if ( promotion.isQuizPromotion() )
    {
      winnerPool = activityService.getQuizActivityList( promotion.getId(), new Boolean( true ), detachedSweepstake );
      validateQuizWinnerReplacement( new ArrayList( winnerPool ), detachedSweepstake, ids, types );
      winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, ids == null ? 0 : ids.size(), giversDisplayed );
    }
    else if ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() )
    {
      AbstractRecognitionPromotion abstractRecognitionPromotion = (AbstractRecognitionPromotion)promotion;
      String multiAwardTypeCode = abstractRecognitionPromotion.getSweepstakesMultipleAwardType().getCode();
      SweepstakesWinnerEligibilityType eligibilityType = abstractRecognitionPromotion.getSweepstakesWinnerEligibilityType();
      allowMultiple = SweepstakesMultipleAwardsType.MULTIPLE_CODE.equals( multiAwardTypeCode );
      if ( ids != null && ids.size() > 0 )
      {
        int giverWinnerReplacementSum = 0;
        int receiverWinnerReplacementSum = 0;
        for ( Iterator iter = types.iterator(); iter.hasNext(); )
        {
          String paxType = (String)iter.next();
          if ( paxType.equalsIgnoreCase( "Giver" ) || paxType.equalsIgnoreCase( "Nominator" ) )
          {
            giverWinnerReplacementSum++;
          }
          else
          {
            receiverWinnerReplacementSum++;
          }
        }
        if ( promotion.isNominationPromotion() )
        {
          winnerPool = getNominationActivityList( promotion.getId(), NominationActivityQueryConstraint.GIVERS_AND_RECEIVERS, detachedSweepstake );
        }
        else
        {
          winnerPool = activityService.getGiversAndReceiversRecognitionActivityList( promotion.getId(), detachedSweepstake );
        }

        if ( allowMultiple )
        {
          List multipleWinnerPool = new ArrayList( winnerPool );
          validateWinnerForMultipleAward( multipleWinnerPool, detachedSweepstake, ids, giverWinnerReplacementSum, receiverWinnerReplacementSum );
        }
        if ( !allowMultiple )
        {
          RecognitionWinnersListStrategy.removeRemovedAcivities( winnerPool, detachedSweepstake );
          winnerPool = RecognitionWinnersListStrategy.removeDuplicateAcivities( winnerPool );
          int totalReplacement = receiverWinnerReplacementSum + giverWinnerReplacementSum;
          int totalDisplayed = giversDisplayed + receiversDisplayed;
          if ( eligibilityType.isPrimarySecondaryCombined() && winnerPool.size() - totalDisplayed < totalReplacement )
          {
            List serviceErrors = new ArrayList();
            ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_RECEIVER_FOR_REPLACEMENT_ERROR );
            serviceErrors.add( error );
            throw new ServiceErrorException( serviceErrors );
          }
          else if ( !eligibilityType.isPrimarySecondaryCombined() )
          {
            if ( giverWinnerReplacementSum > 0 && receiverWinnerReplacementSum > 0 && getWinnerPoolTypeCount( winnerPool, true ) - giversDisplayed < giverWinnerReplacementSum
                && getWinnerPoolTypeCount( winnerPool, false ) - receiversDisplayed < receiverWinnerReplacementSum )
            {
              List serviceErrors = new ArrayList();
              ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_RECEIVER_FOR_REPLACEMENT_ERROR );
              serviceErrors.add( error );
              throw new ServiceErrorException( serviceErrors );
            }
            if ( giverWinnerReplacementSum > 0 && getWinnerPoolTypeCount( winnerPool, true ) == giversDisplayed )
            {
              List serviceErrors = new ArrayList();
              ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_FOR_REPLACEMENT_ERROR );
              serviceErrors.add( error );
              throw new ServiceErrorException( serviceErrors );
            }
            if ( giverWinnerReplacementSum > 0 )
            {
              if ( getWinnerPoolTypeCount( winnerPool, true ) - giversDisplayed < giverWinnerReplacementSum )
              {
                List serviceErrors = new ArrayList();
                ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_FOR_REPLACEMENT_ERROR );
                serviceErrors.add( error );
                throw new ServiceErrorException( serviceErrors );
              }
            }

            if ( receiverWinnerReplacementSum > 0 && getWinnerPoolTypeCount( winnerPool, false ) == receiversDisplayed )
            {
              List serviceErrors = new ArrayList();
              ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_RECEIVER_FOR_REPLACEMENT_ERROR );
              serviceErrors.add( error );
              throw new ServiceErrorException( serviceErrors );
            }
            if ( receiverWinnerReplacementSum > 0 )
            {
              if ( getWinnerPoolTypeCount( winnerPool, false ) - receiversDisplayed < receiverWinnerReplacementSum )
              {
                List serviceErrors = new ArrayList();
                ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_RECEIVER_FOR_REPLACEMENT_ERROR );
                serviceErrors.add( error );
                throw new ServiceErrorException( serviceErrors );
              }
            }
          }
        } // not allow Multiple
        winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, giverWinnerReplacementSum, receiverWinnerReplacementSum );
      }
      else
      {

        winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, 0, 0 );
      }
    }
    else if ( promotion.isProductClaimPromotion() )
    {
      ProductClaimPromotion promo = (ProductClaimPromotion)promotion;
      String multiAwardTypeCode = promo.getSweepstakesMultipleAwardType().getCode();
      allowMultiple = SweepstakesMultipleAwardsType.MULTIPLE_CODE.equals( multiAwardTypeCode );

      winnerPool = activityService.getPCActivityList( promo.getId(), detachedSweepstake );

      if ( allowMultiple )
      {
        List multipleWinnerPool = new ArrayList( winnerPool );
        validatePCWinnerForMultipleAward( multipleWinnerPool, detachedSweepstake, ids, types );
      }
      else
      {
        ProductClaimWinnersListStrategy.removeRemovedAcivities( winnerPool, detachedSweepstake );
        winnerPool = ProductClaimWinnersListStrategy.removeDuplicateAcivities( winnerPool );
        int totalReplacement = types != null ? types.size() : 0;

        if ( totalReplacement > 0 && winnerPool.size() == receiversDisplayed )
        {
          List serviceErrors = new ArrayList();
          ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_FOR_REPLACEMENT_ERROR );
          serviceErrors.add( error );
          throw new ServiceErrorException( serviceErrors );
        }
        if ( totalReplacement > 0 )
        {
          if ( winnerPool.size() - receiversDisplayed < totalReplacement )
          {
            List serviceErrors = new ArrayList();
            ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_FOR_REPLACEMENT_ERROR );
            serviceErrors.add( error );
            throw new ServiceErrorException( serviceErrors );
          }
        }

      }

      winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    }
    else if ( promotion.isSurveyPromotion() )
    {
      winnerPool = participantSurveyService.getCompletedSurveyByPromoId( promotion.getId() );
      validateSurveyWinnerReplacement( new ArrayList( winnerPool ), detachedSweepstake, ids, types );
      winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, ids == null ? 0 : ids.size(), giversDisplayed );

    }
    else if ( promotion.isBadgePromotion() )
    {
      winnerPool = gamificationService.getParticipantBadgeByPromotionId( promotion.getId() );
      validateBadgesWinnerReplacement( new ArrayList( winnerPool ), detachedSweepstake, ids, types );
      winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, ids == null ? 0 : ids.size(), giversDisplayed );
    }
    else
    {
      winnersListStrategyFactory.getWinnersListStrategy( promotion ).buildWinnersList( promotion, detachedSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );
    }

    if ( ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() || promotion.isProductClaimPromotion() ) && ids != null && ids.size() > 0 )
    {
      PromotionSweepstake promoSweepStake = null;
      for ( Iterator iter = promotion.getPromotionSweepstakes().iterator(); iter.hasNext(); )
      {
        promoSweepStake = (PromotionSweepstake)iter.next();
        if ( !promoSweepStake.isProcessed() )
        {
          break;
        }
      }
      if ( promoSweepStake != null )
      {
        List sweepWinners = new ArrayList( promoSweepStake.getWinners() );
        for ( int i = 0; i < sweepWinners.size(); i++ )
        {
          PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)sweepWinners.get( i );
          if ( ids.contains( winner.getId() ) )
          {
            winner.setRemoved( true );
          }
        }
        if ( allowMultiple )
        {
          promoSweepStake.getWinners().clear();
          promoSweepStake.getWinners().addAll( sweepWinners );
        }
      }
      List updateAscRequestList = new ArrayList();
      updateAscRequestList.add( new PromotionSweepstakeUpdateAssociation( promotion ) );
      if ( allowMultiple )
      {
        updateAscRequestList.add( new PromotionSweepstakeWinnersUpdateAssociation( promotion ) );
      }
      this.promotionService.savePromotion( promotion.getId(), updateAscRequestList );
    }
    else if ( ( promotion.isSurveyPromotion() || promotion.isQuizPromotion() || promotion.isBadgePromotion() ) && ids != null && ids.size() > 0 )
    {
      PromotionSweepstake promoSweepStake = null;
      for ( Iterator iter = promotion.getPromotionSweepstakes().iterator(); iter.hasNext(); )
      {
        promoSweepStake = (PromotionSweepstake)iter.next();
        if ( !promoSweepStake.isProcessed() )
        {
          break;
        }
      }
      if ( promoSweepStake != null )
      {
        List sweepWinners = new ArrayList( promoSweepStake.getWinners() );
        for ( int i = 0; i < sweepWinners.size(); i++ )
        {
          PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)sweepWinners.get( i );
          if ( ids.contains( winner.getId() ) )
          {
            winner.setRemoved( true );
          }
        }
        promoSweepStake.getWinners().clear();
        promoSweepStake.getWinners().addAll( sweepWinners );
      }
      List updateAscRequestList = new ArrayList();
      updateAscRequestList.add( new PromotionSweepstakeUpdateAssociation( promotion ) );
      updateAscRequestList.add( new PromotionSweepstakeWinnersUpdateAssociation( promotion ) );
      this.promotionService.savePromotion( promotion.getId(), updateAscRequestList );
    }
    return winners;
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

  /**
   * @param winner
   * @param awardQuantity
   * @param promotion
   * @param promotionSweepstake
   */
  private List createSweepstakesActivities( Participant winner, Long awardQuantity, Promotion promotion, PromotionSweepstake promotionSweepstake )
  {
    // Only one activity created for a single sweepstakes winner.
    List activities = new ArrayList();

    SweepstakesAwardAmountActivity activity = new SweepstakesAwardAmountActivity();

    activity.setGuid( GuidUtils.generateGuid() );
    activity.setPromotionSweepstake( promotionSweepstake );
    activity.setPromotion( promotion );
    activity.setParticipant( winner );
    activity.setSubmissionDate( new Date() );
    activity.setAwardQuantity( awardQuantity );
    activity.setNode( winner.getPrimaryUserNode().getNode() );
    Set<PromotionSweepstakeWinner> promotionSweepstakeWinnerSet = (Set<PromotionSweepstakeWinner>)promotionSweepstake.getWinners();
    for ( PromotionSweepstakeWinner promotionSweepstakeWinner : promotionSweepstakeWinnerSet )
    {
      if ( promotionSweepstakeWinner.getWinnerType().equals( PromotionSweepstakeWinner.NOMINATOR_TYPE ) )
      {
        activity.setSubmitter( true );
      }
    }

    activities.add( activity );

    return activities;
  }

  /**
   * @param winner
   * @param promotion
   * @param promotionSweepstake
   */
  private List createSweepstakesMerchLevelActivity( Participant winner, Promotion promotion, PromotionSweepstakeWinner promotionSweepstakeWinner )
  {
    // Only one activity created for a single sweepstakes winner.
    List activities = new ArrayList();

    SweepstakesMerchLevelActivity activity = new SweepstakesMerchLevelActivity();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
    List promoMerchCountryList = promoMerchCountryDAO.getPromoMerchCountriesByPromotionId( promotion.getId(), associationRequestCollection );

    try
    {
      merchLevelService.mergeMerchLevelWithOMList( promoMerchCountryList );
    }
    catch( ServiceErrorException e )
    {
    }

    PromoMerchCountry promoMerchCountry = null;

    // find the promo Merch Country object for this winning participant - match the countries
    for ( Iterator pmcIter = promoMerchCountryList.iterator(); pmcIter.hasNext(); )
    {
      promoMerchCountry = (PromoMerchCountry)pmcIter.next();
      if ( winner != null )
      {
        UserAddress winnerAddress = winner.getPrimaryAddress();
        if ( winnerAddress.getAddress().getCountry().equals( promoMerchCountry.getCountry() ) )
        {
          // we've found the match, exit out
          break;
        }
      }
    }

    // find the promo merch program level for the winning participant
    Long levelToAchieve = null;
    if ( promotion.isRecognitionPromotion() )
    {
      if ( promotionSweepstakeWinner.getWinnerType().equals( PromotionSweepstakeWinner.GIVER_TYPE ) )
      {
        levelToAchieve = promotion.getSweepstakesPrimaryAwardLevel();
      }
      else
      {
        levelToAchieve = promotion.getSweepstakesSecondaryAwardLevel();
      }
    }

    // find the promo Merch program level for the winner
    PromoMerchProgramLevel promoMerchProgramLevel = null;
    int i = 0;
    for ( Iterator pmplIter = promoMerchCountry.getLevels().iterator(); pmplIter.hasNext(); )
    {
      i++;
      promoMerchProgramLevel = (PromoMerchProgramLevel)pmplIter.next();
      if ( levelToAchieve.intValue() == i )
      {
        break;
      }
    }

    MerchOrder merchOrder = new MerchOrder();
    merchOrder.setMerchGiftCodeType( MerchGiftCodeType.lookup( MerchGiftCodeType.LEVEL ) );
    merchOrder.setPromoMerchProgramLevel( promoMerchProgramLevel );
    merchOrder.setParticipant( winner );
    merchOrder.setRedeemed( false );
    merchOrder.setBatchId( merchOrderDAO.getNextBatchId() );
    /* WIP# 25130 Start */
    if ( promotion.isRecognitionPromotion() )
    {
      if ( promotion.isSwpBillCodesActive() )
      {
        billingCodeStrategy.setMerchOrderBillingCodesForSweepstakes( merchOrder, (RecognitionPromotion)promotion );
      }
    }
    /* WIP# 25130 End */
    merchOrderDAO.saveMerchOrder( merchOrder );

    activity.setMerchOrder( merchOrder );

    activity.setGuid( GuidUtils.generateGuid() );
    activity.setPromotionSweepstake( promotionSweepstakeWinner.getPromotionSweepstake() );
    activity.setPromotion( promotion );
    activity.setParticipant( winner );
    activity.setSubmissionDate( new Date() );

    activities.add( activity );

    // Call MerchOrderCreateProcess to generate gift code
    List<DepositProcessBean> depositProcessMerchList = new ArrayList<DepositProcessBean>();
    DepositProcessBean depositProcessBean = new DepositProcessBean();
    depositProcessBean.setMerchOrderId( merchOrder.getId() );
    depositProcessBean.setParticipantId( winner.getId() );
    depositProcessBean.setProgramId( promoMerchCountry.getProgramId() );
    depositProcessMerchList.add( depositProcessBean );

    Process process = processService.createOrLoadSystemProcess( MerchOrderCreateProcess.PROCESS_NAME, MerchOrderCreateProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "depositProcessMerchList", depositProcessMerchList );
    parameterValueMap.put( "promotionId", promotion.getId() );
    parameterValueMap.put( "retry", "0" ); // bug 66870
    parameterValueMap.put( "isRetriable", String.valueOf( Boolean.TRUE ) ); // bug 66870

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

    return activities;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setWinnersListStrategyFactory( WinnersListStrategyFactory winnersListStrategyFactory )
  {
    this.winnersListStrategyFactory = winnersListStrategyFactory;
  }

  public PromoMerchCountryDAO getPromoMerchCountryDAO()
  {
    return promoMerchCountryDAO;
  }

  public void setPromoMerchCountryDAO( PromoMerchCountryDAO promoMerchCountryDAO )
  {
    this.promoMerchCountryDAO = promoMerchCountryDAO;
  }

  public MerchOrderDAO getMerchOrderDAO()
  {
    return merchOrderDAO;
  }

  public void setMerchOrderDAO( MerchOrderDAO merchOrderDAO )
  {
    this.merchOrderDAO = merchOrderDAO;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  /**
   * @param winnerPool
   * @param isGiver
   * @return count
   */
  public int getWinnerPoolTypeCount( List winnerPool, boolean isGiver )
  {
    int giversCount = 0;
    int receiversCount = 0;
    if ( winnerPool != null && winnerPool.size() > 0 )
    {
      for ( Iterator iter = winnerPool.iterator(); iter.hasNext(); )
      {
        Object temp = iter.next();
        boolean isSubmitter = false;
        if ( temp instanceof AbstractRecognitionActivity )
        {
          AbstractRecognitionActivity activity = (AbstractRecognitionActivity)temp;
          isSubmitter = activity.isSubmitter();
        }
        else if ( temp instanceof SweepstakeWinnerPoolValue )
        {
          SweepstakeWinnerPoolValue activity = (SweepstakeWinnerPoolValue)temp;
          isSubmitter = activity.isSubmitter();
        }

        if ( isSubmitter )
        {
          giversCount++;
        }
        else
        {
          receiversCount++;
        }

      }
    }
    if ( isGiver )
    {
      return giversCount;
    }
    return receiversCount;
  }

  private void validatePCWinnerForMultipleAward( List multipleWinnerPool, PromotionSweepstake detachedSweepstake, List idsForReplacement, List totalWinnerBeans ) throws ServiceErrorException
  {
    int submitterReplacementCount = totalWinnerBeans != null ? totalWinnerBeans.size() : 0;
    multipleWinnerPool = ProductClaimWinnersListStrategy.removeDuplicateAcivities( multipleWinnerPool );
    if ( multipleWinnerPool.size() > 0 && detachedSweepstake.getWinners().size() > 0 )
    {
      boolean isAllWinnersDisplayed = false;
      if ( submitterReplacementCount > 0 )
      {
        isAllWinnersDisplayed = isAllSubmittersDisplayed( multipleWinnerPool, detachedSweepstake.getWinners(), idsForReplacement, submitterReplacementCount );
      }

      if ( submitterReplacementCount > 0 && isAllWinnersDisplayed )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }

    }

  }

  private void validateWinnerForMultipleAward( List multipleWinnerPool, PromotionSweepstake detachedSweepstake, List ids, int giverWinnerReplacementSum, int receiverWinnerReplacementSum )
      throws ServiceErrorException
  {

    multipleWinnerPool = RecognitionWinnersListStrategy.removeDuplicateAcivities( multipleWinnerPool );
    if ( multipleWinnerPool.size() > 0 && detachedSweepstake.getWinners().size() > 0 )
    {
      boolean isAllGiversDisplayed = false;
      boolean isAllReceiversDisplayed = false;
      if ( giverWinnerReplacementSum > 0 && receiverWinnerReplacementSum > 0 )
      {
        isAllGiversDisplayed = isAllWinnersDisplayed( multipleWinnerPool, detachedSweepstake.getWinners(), ids, giverWinnerReplacementSum, true );
        isAllReceiversDisplayed = isAllWinnersDisplayed( multipleWinnerPool, detachedSweepstake.getWinners(), ids, giverWinnerReplacementSum, false );
      }
      else if ( giverWinnerReplacementSum > 0 )
      {
        isAllGiversDisplayed = isAllWinnersDisplayed( multipleWinnerPool, detachedSweepstake.getWinners(), ids, giverWinnerReplacementSum, true );
      }
      else if ( receiverWinnerReplacementSum > 0 )
      {
        isAllReceiversDisplayed = isAllWinnersDisplayed( multipleWinnerPool, detachedSweepstake.getWinners(), ids, giverWinnerReplacementSum, false );
      }

      if ( giverWinnerReplacementSum > 0 && receiverWinnerReplacementSum > 0 && isAllGiversDisplayed && isAllReceiversDisplayed )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_RECEIVER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }
      if ( giverWinnerReplacementSum > 0 && isAllGiversDisplayed )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_GIVER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }
      if ( receiverWinnerReplacementSum > 0 && isAllReceiversDisplayed )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_RECEIVER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }

    }

  }

  private void validateQuizWinnerReplacement( List multipleWinnerPool, PromotionSweepstake detachedSweepstake, List idsForReplacement, List totalWinnerBeans ) throws ServiceErrorException
  {

    multipleWinnerPool = QuizWinnersListStrategy.removeDuplicateAcivities( multipleWinnerPool );
    QuizWinnersListStrategy.removeRemovedAcivities( multipleWinnerPool, detachedSweepstake );
    List giverWinnersList = new ArrayList();
    if ( totalWinnerBeans != null && detachedSweepstake.getWinners() != null )
    {
      for ( int i = 0; i < totalWinnerBeans.size(); i++ )
      {
        Long winnerId = (Long)totalWinnerBeans.get( i );
        for ( Iterator iter = detachedSweepstake.getWinners().iterator(); iter.hasNext(); )
        {
          PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)iter.next();
          if ( winnerId.longValue() == promoSweepWinner.getId().longValue() )
          {
            giverWinnersList.add( promoSweepWinner );
            break;
          }

        }
      }
      for ( int i = 0; i < giverWinnersList.size(); i++ )
      {
        PromotionSweepstakeWinner promoWinner = (PromotionSweepstakeWinner)giverWinnersList.get( i );
        Participant inPax = promoWinner.getParticipant();
        for ( int j = 0; j < multipleWinnerPool.size(); j++ )
        {
          Participant activityPax = ( (QuizActivity)multipleWinnerPool.get( j ) ).getParticipant();
          if ( inPax.getId().longValue() == activityPax.getId().longValue() )
          {
            multipleWinnerPool.remove( j );
          }
        }
      }
      if ( idsForReplacement.size() > 0 && idsForReplacement.size() > multipleWinnerPool.size() )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_WINNER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }
    } // end of totalWinnerBeans != null

  }

  private void validateSurveyWinnerReplacement( List multipleWinnerPool, PromotionSweepstake detachedSweepstake, List idsForReplacement, List totalWinnerBeans ) throws ServiceErrorException
  {
    SurveyWinnersListStrategy.removeRemovedParticipants( multipleWinnerPool, detachedSweepstake );
    List giverWinnersList = new ArrayList();
    if ( totalWinnerBeans != null && detachedSweepstake.getWinners() != null )
    {
      for ( int i = 0; i < totalWinnerBeans.size(); i++ )
      {
        Long winnerId = (Long)totalWinnerBeans.get( i );
        for ( Iterator iter = detachedSweepstake.getWinners().iterator(); iter.hasNext(); )
        {
          PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)iter.next();
          if ( winnerId.longValue() == promoSweepWinner.getId().longValue() )
          {
            giverWinnersList.add( promoSweepWinner );
            break;
          }

        }
      }
      for ( int i = 0; i < giverWinnersList.size(); i++ )
      {
        PromotionSweepstakeWinner promoWinner = (PromotionSweepstakeWinner)giverWinnersList.get( i );
        Participant inPax = promoWinner.getParticipant();
        for ( int j = 0; j < multipleWinnerPool.size(); j++ )
        {
          Participant activityPax = (Participant) ( (ParticipantSurvey)multipleWinnerPool.get( j ) ).getParticipant();
          if ( inPax.getId().longValue() == activityPax.getId().longValue() )
          {
            multipleWinnerPool.remove( j );
          }
        }
      }
      if ( idsForReplacement.size() > 0 && idsForReplacement.size() > multipleWinnerPool.size() )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_WINNER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }
    } // end of totalWinnerBeans != null
  }

  private void validateBadgesWinnerReplacement( List multipleWinnerPool, PromotionSweepstake detachedSweepstake, List idsForReplacement, List totalWinnerBeans ) throws ServiceErrorException
  {
    BadgeWinnersListStrategy.removeRemovedParticipants( multipleWinnerPool, detachedSweepstake );
    List giverWinnersList = new ArrayList();
    if ( totalWinnerBeans != null && detachedSweepstake.getWinners() != null )
    {
      for ( int i = 0; i < totalWinnerBeans.size(); i++ )
      {
        Long winnerId = (Long)totalWinnerBeans.get( i );
        for ( Iterator iter = detachedSweepstake.getWinners().iterator(); iter.hasNext(); )
        {
          PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)iter.next();
          if ( winnerId.longValue() == promoSweepWinner.getId().longValue() )
          {
            giverWinnersList.add( promoSweepWinner );
            break;
          }

        }
      }
      for ( int i = 0; i < giverWinnersList.size(); i++ )
      {
        PromotionSweepstakeWinner promoWinner = (PromotionSweepstakeWinner)giverWinnersList.get( i );
        ParticipantBadge inPax = promoWinner.getParticipantBadge();
        for ( int j = 0; j < multipleWinnerPool.size(); j++ )
        {
          ParticipantBadge paxBadge = (ParticipantBadge)multipleWinnerPool.get( j );
          if ( inPax.getId().longValue() == paxBadge.getId().longValue() )
          {
            multipleWinnerPool.remove( j );
          }
        }
      }
      if ( idsForReplacement.size() > 0 && idsForReplacement.size() > multipleWinnerPool.size() )
      {
        List serviceErrors = new ArrayList();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.NO_WINNER_FOR_REPLACEMENT_ERROR );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }

    } // end of totalWinnerBeans != null
  }

  private boolean isAllSubmittersDisplayed( List activityPool, Set multipleWinnerPool, List ids, int giverWinnerReplacementSum )
  {
    boolean isAllPCSubmittersDisplayed = true;
    List pcWinnerList = new ArrayList();
    List receiverWinnersList = new ArrayList();

    for ( int i = 0; i < ids.size(); i++ )
    {
      Long winnerId = (Long)ids.get( i );
      for ( Iterator iter = multipleWinnerPool.iterator(); iter.hasNext(); )
      {
        PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)iter.next();
        if ( winnerId.longValue() == promoSweepWinner.getId().longValue() )
        {
          pcWinnerList.add( promoSweepWinner );
          break;
        }

      }
    }
    // Remove Incoming Pax ids From Activity Pax pool Before Validating
    for ( int i = 0; i < pcWinnerList.size(); i++ )
    {
      Participant inPax = ( (PromotionSweepstakeWinner)pcWinnerList.get( i ) ).getParticipant();
      for ( int j = 0; j < activityPool.size(); j++ )
      {
        Participant activityPax = ( (SalesActivity)activityPool.get( j ) ).getParticipant();
        if ( inPax.getId().longValue() == activityPax.getId().longValue() )
        {
          activityPool.remove( j );
          break;
        }
      }
    }

    for ( int i = 0; i < pcWinnerList.size(); i++ )
    {
      PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)pcWinnerList.get( i );
      for ( int j = 0; j < activityPool.size(); j++ )
      {
        SalesActivity salesActivity = (SalesActivity)activityPool.get( j );
        if ( salesActivity.isSubmitter() )
        {
          Participant pax = salesActivity.getParticipant();
          if ( promoSweepWinner.getParticipant().getId().longValue() != pax.getId().longValue() )
          {
            isAllPCSubmittersDisplayed = false;
            return isAllPCSubmittersDisplayed;
          }
        }
      }
    }
    return isAllPCSubmittersDisplayed;

  }

  private boolean isAllWinnersDisplayed( List activityPool, Set multipleWinnerPool, List ids, int giverWinnerReplacementSum, boolean isGiver )
  {
    // boolean isDisplayed = false;
    boolean isAllGiversDisplayed = true;
    boolean isAllReceiversDisplayed = true;
    List giverWinnersList = new ArrayList();
    List receiverWinnersList = new ArrayList();

    if ( isGiver )
    {
      for ( int i = 0; i < ids.size(); i++ )
      {
        Long winnerId = (Long)ids.get( i );
        for ( Iterator iter = multipleWinnerPool.iterator(); iter.hasNext(); )
        {
          PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)iter.next();
          if ( winnerId.longValue() == promoSweepWinner.getId().longValue() && ( promoSweepWinner.getWinnerType().equals( "Giver" ) || promoSweepWinner.getWinnerType().equals( "Nominator" ) ) )
          {
            giverWinnersList.add( promoSweepWinner );
            break;
          }

        }
      }
      // Remove Incoming Pax ids From Activity Pax pool Before Validating
      for ( int i = 0; i < giverWinnersList.size(); i++ )
      {
        Participant inPax = ( (PromotionSweepstakeWinner)giverWinnersList.get( i ) ).getParticipant();
        for ( int j = 0; j < activityPool.size(); j++ )
        {
          SweepstakeWinnerPoolValue sweepstakeWinnerPoolValue = (SweepstakeWinnerPoolValue)activityPool.get( j );
          if ( inPax.getId().longValue() == sweepstakeWinnerPoolValue.getUserId().longValue() )
          {
            activityPool.remove( j );
            break;
          }
        }
      }

      for ( int i = 0; i < giverWinnersList.size(); i++ )
      {
        PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)giverWinnersList.get( i );
        if ( promoSweepWinner.getWinnerType().equals( "Giver" ) || promoSweepWinner.getWinnerType().equals( "Nominator" ) )
        {

          for ( int j = 0; j < activityPool.size(); j++ )
          {
            SweepstakeWinnerPoolValue sweepstakeWinnerPoolValue = (SweepstakeWinnerPoolValue)activityPool.get( j );
            if ( sweepstakeWinnerPoolValue.isSubmitter() )
            {
              if ( promoSweepWinner.getParticipant().getId().longValue() != sweepstakeWinnerPoolValue.getUserId().longValue() )
              {
                return false;
              }
            }

          }
        }
      }
      return isAllGiversDisplayed;
    }
    else
    {
      for ( int i = 0; i < ids.size(); i++ )
      {
        Long winnerId = (Long)ids.get( i );
        for ( Iterator iter = multipleWinnerPool.iterator(); iter.hasNext(); )
        {
          PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)iter.next();
          if ( winnerId.longValue() == promoSweepWinner.getId().longValue() && ( promoSweepWinner.getWinnerType().equals( "Receiver" ) || promoSweepWinner.getWinnerType().equals( "Nominee" ) ) )
          {
            receiverWinnersList.add( promoSweepWinner );
            break;
          }

        }
      }
      // Remove Incoming Pax ids From Activity Pax pool Before Validating
      for ( int i = 0; i < receiverWinnersList.size(); i++ )
      {
        Participant inPax = ( (PromotionSweepstakeWinner)receiverWinnersList.get( i ) ).getParticipant();
        for ( int j = 0; j < activityPool.size(); j++ )
        {
          SweepstakeWinnerPoolValue sweepstakeWinnerPoolValue = (SweepstakeWinnerPoolValue)activityPool.get( j );
          if ( inPax.getId().longValue() == sweepstakeWinnerPoolValue.getUserId().longValue() )
          {
            activityPool.remove( j );
          }
        }
      }
      for ( int i = 0; i < receiverWinnersList.size(); i++ )
      {
        PromotionSweepstakeWinner promoSweepWinner = (PromotionSweepstakeWinner)receiverWinnersList.get( i );
        if ( promoSweepWinner.getWinnerType().equals( "Receiver" ) || promoSweepWinner.getWinnerType().equals( "Nominee" ) )
        {

          for ( int j = 0; j < activityPool.size(); j++ )
          {
            SweepstakeWinnerPoolValue sweepstakeWinnerPoolValue = (SweepstakeWinnerPoolValue)activityPool.get( j );

            if ( !sweepstakeWinnerPoolValue.isSubmitter() )
            {
              if ( promoSweepWinner.getParticipant().getId().longValue() != sweepstakeWinnerPoolValue.getUserId().longValue() )
              {
                isAllReceiversDisplayed = false;
                return isAllReceiversDisplayed;
              }
            }
          }
        }
      }
      return isAllReceiversDisplayed;
    }
  }

  /* Bug # 34020 start */
  public int getPromotionSweepstakesNotProcessedCount( Long promotionId )
  {
    return promotionSweepstakeDAO.getPromotionSweepstakesNotProcessedCount( promotionId );
  }

  public int getPromotionSweepstakesHistoryCount( Long promotionId )
  {
    return promotionSweepstakeDAO.getPromotionSweepstakesHistoryCount( promotionId );
  }

  public List getPromotionSweepstakesByPromotionIdNotProcessed( Long promotionId )
  {
    return promotionSweepstakeDAO.getPromotionSweepstakesByPromotionIdNotProcessed( promotionId );
  }

  public List getAllPromotionSweepstakeWinnersByDrawingId( Long drawingId )
  {
    return promotionSweepstakeDAO.getAllPromotionSweepstakeWinnersByDrawingId( drawingId );
  }
  /* Bug # 34020 end */

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setParticipantSurveyService( ParticipantSurveyService participantSurveyService )
  {
    this.participantSurveyService = participantSurveyService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

}
