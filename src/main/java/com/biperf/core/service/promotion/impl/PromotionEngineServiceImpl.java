/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/impl/PromotionEngineServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.biperf.awardbanq.databeans.account.IssuedOMGiftCode;
import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.audit.PayoutCalculationAuditDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.activity.AbstractRecognitionActivity;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ChallengepointPayoutActivity;
import com.biperf.core.domain.activity.GoalQuestPayoutActivity;
import com.biperf.core.domain.audit.ChallengepointPayoutCalculationAudit;
import com.biperf.core.domain.audit.ClaimBasedPayoutCalculationAudit;
import com.biperf.core.domain.audit.GoalQuestPayoutCalculationAudit;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.GiftCodesWebServiceException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.MerchOrderCreateProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.awardbanq.GiftcodesResponseValueObject;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.journal.DepositBillingCodeStrategy;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ActivityLoader;
import com.biperf.core.service.promotion.engine.ActivityLoaderFactory;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.promotion.engine.PayoutStrategy;
import com.biperf.core.service.promotion.engine.PayoutStrategyFactory;
import com.biperf.core.service.promotion.engine.RecognitionFacts;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.MailingBatchHolder;
import com.biperf.util.StringUtils;

/**
 * PromotionEngineServiceImpl.
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
 * <td>tom</td>
 * <td>Jul 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionEngineServiceImpl implements PromotionEngineService
{

  private JournalService journalService = null;
  private MailingService mailingService;
  private MessageService messageService;
  private MerchLevelService merchLevelService;
  private SystemVariableService systemVariableService = null;
  private PaxGoalService paxGoalService;

  private ActivityDAO activityDAO;
  private PayoutCalculationAuditDAO payoutCalculationAuditDAO;
  private ParticipantDAO participantDAO;
  private NodeDAO nodeDAO;
  private MerchOrderDAO merchOrderDAO;

  private ActivityLoaderFactory activityLoaderFactory;
  private PayoutStrategyFactory payoutStrategyFactory;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private ParticipantService participantService;
  private PromotionService promotionService;
  private ChallengePointService challengePointService;
  private ChallengepointProgressService challengepointProgressService;

  private ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory;
  private DepositBillingCodeStrategy billingCodeStrategy; // WIP# 25130

  /**
   * Service to call calculate the goal quest payout process
   */
  private GoalQuestService goalQuestService;
  private GamificationService gamificationService;
  private ProcessService processService;

  Logger logger = Logger.getLogger( this.getClass() );

  public static final String OPT_OUT_TEXT = "-Opt Out Of Awards";

  /**
     * Calculates the payout for the given promotion and facts.
     * 
     * @param facts facts the help determine the payout.
     * @param promotion the promotion that will be paid out.
     * @param payoutType the payoutType of the payout.
     * @return the payouts for the given promotion and facts, as a <code>Set</code> of
     *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
     */
  public Set calculatePayout( PayoutCalculationFacts facts, Promotion promotion, String payoutType )
  {
    Set inputActivities = loadActivities( facts, promotion, payoutType );

    return calculatePayout( facts, promotion, inputActivities, payoutType );
  }

  /**
   * Calculates the payout for the given promotion and facts, then persists the various results.
   * 
   * @param facts facts the help determine the payout.
   * @param promotion the promotion that will be paid out.
   * @param participant the participant who will receive the payout.
   * @param payoutType the payoutType
   * @return the payouts for the given promotion and facts, as a <code>Set</code> of
   *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
   */
  public Set calculatePayoutAndSaveResults( PayoutCalculationFacts facts, Promotion promotion, Participant participant, String payoutType ) throws ServiceErrorException
  {
    Set inputActivities = loadActivities( facts, promotion, payoutType );
    return calculatePayoutAndSaveResults( facts, promotion, participant, payoutType, inputActivities );
  }

  /**
   * Calculates the payout for the given promotion and activities, then persists the various
   * results.
   * 
   * @param facts facts the help determine the payout.
   * @param promotion the promotion that will be paid out.
   * @param participant the participant who will receive the payout.
   * @param payoutType the payout type of the payout.
   * @param inputActivities the activities to be processed.
   * @return the payouts for the given promotion and facts, as a <code>Set</code> of
   *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
   */
  public Set calculatePayoutAndSaveResults( PayoutCalculationFacts facts, Promotion promotion, Participant participant, String payoutType, Set inputActivities ) throws ServiceErrorException
  {
    Set payoutCalculationResults = new LinkedHashSet();

    if ( !inputActivities.isEmpty() && inputActivities.size() != 0 )
    {
      payoutCalculationResults = calculatePayout( facts, promotion, inputActivities, payoutType );
      saveMerchOrders( inputActivities, facts, promotion, participant, payoutCalculationResults );
      updateInputActivitiesToPosted( inputActivities );
      savePayoutCalculationResults( payoutCalculationResults, promotion, participant );
    }

    return payoutCalculationResults;
  }

  /**
   * @param inputActivities
   */
  private void updateInputActivitiesToPosted( Set inputActivities )
  {
    for ( Iterator iter = inputActivities.iterator(); iter.hasNext(); )
    {
      Activity activity = (Activity)iter.next();
      activity.setPosted( true );
      activityDAO.saveActivity( activity );
    }
  }

  private List savePayoutCalculationResults( Set payoutCalculationResults, Promotion promotion, Participant participant ) throws ServiceErrorException
  {
    saveGeneratedActivities( payoutCalculationResults );

    return saveJournalEntriesAndAudits( payoutCalculationResults, promotion, participant );
  }

  private void saveMerchOrders( Set inputActivities, PayoutCalculationFacts facts, Promotion promotion, Participant participant, Set payoutCalculationResults )
  {
    if ( facts instanceof RecognitionFacts )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        generateGiftCodes( inputActivities, (RecognitionFacts)facts, (RecognitionPromotion)promotion, participant );
      }
    }
  }

  /**
   * Processes the GoalQuest Calculation Results.
   * Sends email with gift code incase of Merchandise or Travel or Deposit points
   * @param goalQuestCalculationResults
   * @param promotion
   */
  public DepositProcessBean processGoalQuestPayoutCalculationResults( GoalCalculationResult goalCalculationResult, GoalQuestPromotion promotion, MailingBatchHolder mailingBatch )
      throws ServiceErrorException
  {

    PromotionAwardsType awardType = null;
    Journal journal = null;
    DepositProcessBean depositProcessBean = null;
    if ( promotion != null && goalCalculationResult != null )
    {
      awardType = promotion.getAwardType();

      if ( goalCalculationResult.getReciever() == null )
      {
        return null;
      }

      Participant participant = getParticipant( goalCalculationResult.getReciever().getId() );

      // Check on GoalQuestPaxActivity for given promotion Id and user id with activity_id is not
      // null
      // if so Payout Activity already Exists
      // Skip payout creation.
      if ( goalQuestService.isParticipantPayoutComplete( participant.getId(), promotion.getId() ) )
      {
        // Activity is already completed. Can skip the calculation part and storing activity and
        // journal.
        // Also Skip PayoutCalculationAudit part.
        return null;
      }

      if ( !goalCalculationResult.isAchieved() )
      {
        if ( !goalCalculationResult.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED )
            || goalCalculationResult.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED ) || !goalCalculationResult.isPartner() )
        {
          // Send email only to active participant...
          if ( participant.isActive().booleanValue() )
          {
            // send goal not achieved email
            sendGoalAchievedOrNotEmailConfirmation( promotion, participant, goalCalculationResult, mailingBatch );
          }
        }

        // if not achieved do not process further
        return null;
      }

      // create activity
      Activity activity = createActivityForGoalQuest( goalCalculationResult, promotion, participant );
      depositProcessBean = new DepositProcessBean();

      // based on type of award, deposit the award(Points) or set the gift code for
      // merchandise/travel
      if ( awardType.getCode().equals( PromotionAwardsType.POINTS ) )
      {
        // create journal and journal activity
        journal = createJournal( goalCalculationResult, promotion, participant, activity );

        depositProcessBean.setJournalId( journal.getId() );
      }
      else if ( awardType.getCode().equals( PromotionAwardsType.MERCHANDISE ) || awardType.getCode().equals( PromotionAwardsType.TRAVEL_AWARD ) )
      {
        if ( goalCalculationResult.isOwner() )
        {
          journal = createJournal( goalCalculationResult, promotion, participant, activity );
          journalService.saveJournalEntry( journal );
          depositProcessBean.setJournalId( journal.getId() );
        }
        else
        {
          long valueOfGiftCode = goalCalculationResult.getCalculatedPayout().longValue();
          if ( isPaxOptedOutOfAwards( participant.getId() ) )
          {
            valueOfGiftCode = 0;
          }
          MerchOrder merchOrder = new MerchOrder();
          if ( promotionService.getMerchOrderByPromotionIdAndUserId( promotion.getId(), participant.getId() ) )
          {
            MerchOrder merchOrderProcessed = promotionService.getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( promotion.getId(), participant.getId() ).get( 0 );

            if ( merchOrderProcessed != null )
            {
              merchOrder = merchOrderProcessed;
              depositProcessBean.setParticipantId( participant.getId() );
              depositProcessBean.setProgramId( promotion.getProgramId() );
              depositProcessBean.setValueOfGiftCode( valueOfGiftCode );
              depositProcessBean.setMerchOrderId( merchOrder.getId() );
            }
            else
            {
              merchOrder = null;
              depositProcessBean = null;
            }

          }
          else
          {
            merchOrder = createMerchOrder( promotion, participant, activity, merchOrder );
            depositProcessBean.setParticipantId( participant.getId() );
            depositProcessBean.setProgramId( promotion.getProgramId() );
            depositProcessBean.setValueOfGiftCode( valueOfGiftCode );
            depositProcessBean.setMerchOrderId( merchOrder.getId() );
          }
        }
      }

      ParticipantBadge participantBadge = new ParticipantBadge();
      List<Badge> badgeList = new ArrayList<Badge>();
      badgeList = gamificationService.getBadgeByPromotion( promotion.getId() );
      Iterator badgeItr = badgeList.iterator();

      while ( badgeItr.hasNext() )
      {
        Badge badge = (Badge)badgeItr.next();

        if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.EARNED_OR_NOT_EARNED ) && promotion.isGoalQuestOrChallengePointPromotion() )
        {
          if ( !goalCalculationResult.isPartner() && goalCalculationResult.getGoalLevel() != null )
          {
            participantBadge = gamificationService.populateEarnedBadgesCPandGQ( goalCalculationResult.getGoalLevel().getGoalLevelName(), goalCalculationResult.getReciever(), promotion, badge, true );
          }
          else if ( goalCalculationResult.isPartner() )
          {
            participantBadge = gamificationService.populateEarnedBadgesCPandGQ( goalCalculationResult.getGoalLevel().getGoalLevelName(), goalCalculationResult.getReciever(), promotion, badge, false );
          }
        }
      }

      if ( !goalCalculationResult.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.GOAL_ACHIEVED )
          || goalCalculationResult.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED ) )
      {
        // Send email only to active participant...
        if ( participant.isActive().booleanValue() )
        {
          // send goal achieved email
          sendGoalAchievedOrNotEmailConfirmation( promotion, participant, goalCalculationResult, mailingBatch );
        }
      }

      savePayoutCalculationAudit( participant, journal, goalCalculationResult.isOwner() );
    }
    return depositProcessBean;
  }

  /**
   * 
   * Processes the Challengepoint Calculation Results for each cpCaluculationResult
   * Sends email with gift code incase of Merchandise or Travel or Deposit points
   * @param ChallengePointCalculationResults
   * @param promotion
   */
  public DepositProcessBean processChallengepointPayoutCalculationResults( List challengepointCalculationResults,
                                                                           ChallengepointPaxAwardValueBean cpPaxAwardValueBean,
                                                                           ChallengePointPromotion promotion,
                                                                           MailingBatchHolder mailingBatchHolder )
      throws ServiceErrorException
  {
    ChallengePointAwardType awardType = null;
    DepositProcessBean depositProcessBean = null;
    if ( promotion != null && cpPaxAwardValueBean != null )
    {

      awardType = ChallengePointAwardType.lookup( ChallengePointAwardType.POINTS );
      // ChallengepointPaxAwardValueBean cpPaxAwardValueBean =
      // (ChallengepointPaxAwardValueBean)it.next();
      Participant participant = getParticipant( cpPaxAwardValueBean.getParticipant().getId() );

      JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
      queryConstraint.setJournalTransactionTypesIncluded( new JournalTransactionType[] { JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) } );
      queryConstraint.setParticipantId( participant.getId() );
      queryConstraint.setPromotionId( promotion.getId() );

      List<Journal> journals = journalService.getJournalList( queryConstraint, null );
      boolean isPayoutProcessed = false;
      for ( Iterator<Journal> iter = journals.iterator(); iter.hasNext(); )
      {
        Journal journal = iter.next();
        isPayoutProcessed = payoutCalculationAuditDAO.isManagerOverrideExist( journal.getId(), participant.getId() );
        if ( isPayoutProcessed )
        {
          isPayoutProcessed = true;
          break;
        }
      }

      boolean isCPProcessed = false;
      for ( Iterator<Journal> iter = journals.iterator(); iter.hasNext(); )
      {
        Journal journal = iter.next();
        isCPProcessed = payoutCalculationAuditDAO.isCPAwardExist( journal.getId(), participant.getId() );
        if ( isCPProcessed )
        {
          isCPProcessed = true;
          break;
        }
      }

      if ( journals == null || journals.isEmpty() || cpPaxAwardValueBean.getAwardType().equals( "basic" ) || !isPayoutProcessed && cpPaxAwardValueBean.getAwardType().equals( "manageroverride" )
          || !isCPProcessed && cpPaxAwardValueBean.getAwardType().equals( "challengepoint" ) )
      {
        if ( cpPaxAwardValueBean.getAwardType() != null && cpPaxAwardValueBean.getAwardType().equals( "challengepoint" ) || cpPaxAwardValueBean.getAwardType().equals( "partner" ) )
        {
          // Does payout complete for given promotion Id and user id with journal is not null -
          // ChallangePointAward
          // if so Payout Activity already Exists
          // Skip payout creation.
          if ( getChallengepointProgressService().isParticipantPayoutComplete( participant.getId(), promotion.getId() ) && !cpPaxAwardValueBean.isPartner() )
          {
            // Activity is already completed. Can skip the calculation part and storing activity and
            // journal.
            // Also Skip PayoutCalculationAudit part.
            return null;
          }

          awardType = promotion.getChallengePointAwardType();
          if ( promotion.isAfterFinalProcessDate() && !cpPaxAwardValueBean.isAchieved() )
          {
            ChallengepointPaxValueBean challengepointPaxValueBean = null;
            try
            {
              ChallengePointPromotion challengepointPromotion = cpPaxAwardValueBean.getChallengePointPromotion();
              challengepointPaxValueBean = getChallengepointProgressService().getChallengepointProgressSummary( challengepointPromotion.getId(), participant.getId() );
              if ( challengepointPaxValueBean.getCalculatedThreshold() != null )
              {
                cpPaxAwardValueBean.setInterimAward( new Long( challengepointPaxValueBean.getCalculatedThreshold().longValue() ) );
              }
            }
            catch( ServiceErrorException e )
            {
              e.printStackTrace();
            }

            Participant pax = null;
            if ( cpPaxAwardValueBean.getGoalSelecter() != null )
            {
              pax = (Participant)cpPaxAwardValueBean.getGoalSelecter();
            }
            else
            {
              pax = participant;
            }
            if ( !cpPaxAwardValueBean.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_ACHIEVED ) )
            {
              setChallengepointEmailTotalAward( challengepointCalculationResults, participant, cpPaxAwardValueBean );
              // send goal not achieved email
              sendChallengepointAchievedOrNotEmailConfirmation( promotion, pax, null, cpPaxAwardValueBean, mailingBatchHolder );

            }
            if ( cpPaxAwardValueBean.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED ) )
            {
              PaxGoal paxGoal = challengePointService.getPaxChallengePoint( promotion.getId(), pax.getId() );
              if ( paxGoal != null )
              {
                ChallengePointCalculationResult challengePointCalculationResult = getChallengePointCalculationResult( promotion, paxGoal );

                sendChallengepointAchievedOrNotEmailConfirmation( promotion, pax, challengePointCalculationResult, cpPaxAwardValueBean, mailingBatchHolder );
              }

            }

            return null;
          }
          else if ( !cpPaxAwardValueBean.isAchieved() )// if not achieved do not process further(
                                                       // Bug
                                                       // Fix 21512,not sending cp achieved email to
                                                       // pax.)
          {
            return null;
          }
        }
        Journal journal = null;
        boolean skipActivityAndJournal = false;

        // Create Award record but skip activity and Journal for all the basic and manageroverride
        // awards that has zero points. Bug#21366
        if ( cpPaxAwardValueBean.getAwardType() != null && ( cpPaxAwardValueBean.getAwardType().equals( "basic" ) || cpPaxAwardValueBean.getAwardType().equals( "manageroverride" ) )
            && ( cpPaxAwardValueBean.getAwardIssued() == null || cpPaxAwardValueBean.getAwardIssued().longValue() <= 0 ) )
        {
          skipActivityAndJournal = true;
        }

        if ( !skipActivityAndJournal )
        {
          // create activity
          Activity activity = createActivityForChallengepoint( cpPaxAwardValueBean, promotion, participant );
          depositProcessBean = new DepositProcessBean();

          // based on type of award, deposit the award(Points) or set the gift code for
          // merchandise/travel
          if ( awardType.getCode().equals( ChallengePointAwardType.POINTS ) )
          {
            // create journal and journal activity
            journal = createCPJournal( cpPaxAwardValueBean, promotion, participant, activity );
            depositProcessBean.setJournalId( journal.getId() );
          }
          else if ( awardType.getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
          {
            long valueOfGiftCode = ( (GoalLevel)cpPaxAwardValueBean.getPaxGoal().getGoalLevel() ).getAward().longValue();
            AssociationRequestCollection arCollection = new AssociationRequestCollection();
            arCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
            promotion = (ChallengePointPromotion)promotionService.getPromotionByIdWithAssociations( promotion.getId(), arCollection );
            MerchOrder merchOrder = new MerchOrder();
            if ( promotionService.getMerchOrderByPromotionIdAndUserId( promotion.getId(), participant.getId() ) )
            {
              MerchOrder merchOrderProcessed = promotionService.getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( promotion.getId(), participant.getId() ).get( 0 );

              if ( merchOrderProcessed != null )
              {
                merchOrder = merchOrderProcessed;
                depositProcessBean.setParticipantId( participant.getId() );
                depositProcessBean.setProgramId( promotion.getProgramId() );
                depositProcessBean.setValueOfGiftCode( valueOfGiftCode );
                depositProcessBean.setMerchOrderId( merchOrder.getId() );
              }
              else
              {
                merchOrder = null;
                depositProcessBean = null;
              }

            }
            else
            {
              merchOrder = createMerchOrder( promotion, participant, activity, merchOrder );
              depositProcessBean.setParticipantId( participant.getId() );
              depositProcessBean.setProgramId( promotion.getProgramId() );
              depositProcessBean.setValueOfGiftCode( valueOfGiftCode );
              depositProcessBean.setMerchOrderId( merchOrder.getId() );
            }
          }
        }

        ChallengepointAward challengepointAward = createCPAward( cpPaxAwardValueBean, promotion, participant, journal );
        challengePointService.saveChallengepointAward( challengepointAward );
        // Create Award record but skip activity and Journal for all the basic and manageroverride
        // awards that has zero points. Bug#21366
        if ( !skipActivityAndJournal )
        {
          saveCPPayoutCalculationAudit( participant, journal, cpPaxAwardValueBean.getAwardType() );
        }
        if ( cpPaxAwardValueBean.getAwardType() != null && cpPaxAwardValueBean.getAwardType().equals( "challengepoint" ) || cpPaxAwardValueBean.getAwardType().equals( "partner" ) )
        {
          if ( promotion.isAfterFinalProcessDate() && cpPaxAwardValueBean.isAchieved() )
          {
            if ( cpPaxAwardValueBean.getTotalAwardIssued() == null )
            {
              ChallengepointPaxValueBean challengepointPaxValueBean = null;
              try
              {
                ChallengePointPromotion challengepointPromotion = cpPaxAwardValueBean.getChallengePointPromotion();
                challengepointPaxValueBean = getChallengepointProgressService().getChallengepointProgressSummary( challengepointPromotion.getId(), participant.getId() );
                List<ChallengepointAward> awards = challengepointPaxValueBean.getAwards();
                if ( awards != null )
                {
                  if ( awards.size() > 0 )
                  {
                    ChallengepointAward newChallengepointAward = (ChallengepointAward)awards.get( awards.size() - 1 );
                    if ( newChallengepointAward.getAwardIssued() != null )
                    {
                      if ( isPaxOptedOutOfAwards( participant.getId() ) )
                      {
                        cpPaxAwardValueBean.setTotalAwardIssued( new Long( 0 ) );// Opt Out of
                                                                                 // awards will
                                                                                 // override the
                                                                                 // original value
                                                                                 // to zero.
                      }
                      else
                      {
                        cpPaxAwardValueBean.setTotalAwardIssued( newChallengepointAward.getAwardIssued() != null ? new Long( newChallengepointAward.getAwardIssued().longValue() ) : null );
                      }
                    }
                  }
                }

              }
              catch( ServiceErrorException e )
              {
                e.printStackTrace();
              }
            }

            if ( !cpPaxAwardValueBean.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_ACHIEVED ) )
            {
              setChallengepointEmailTotalAward( challengepointCalculationResults, participant, cpPaxAwardValueBean );
              // send achieved email
              sendChallengepointAchievedOrNotEmailConfirmation( promotion, participant, null, cpPaxAwardValueBean, mailingBatchHolder );
            }

            if ( cpPaxAwardValueBean.isPartner() && promotion.isNotificationRequired( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED ) )
            {
              Participant pax = null;
              if ( cpPaxAwardValueBean.getGoalSelecter() != null )
              {
                pax = (Participant)cpPaxAwardValueBean.getGoalSelecter();
              }
              else
              {
                pax = participant;
              }

              PaxGoal paxGoal = challengePointService.getPaxChallengePoint( promotion.getId(), pax.getId() );
              if ( paxGoal != null )
              {
                ChallengePointCalculationResult challengePointCalculationResult = getChallengePointCalculationResult( promotion, paxGoal );

                sendChallengepointAchievedOrNotEmailConfirmation( promotion, participant, challengePointCalculationResult, cpPaxAwardValueBean, mailingBatchHolder );
              }
            }
          }

          if ( cpPaxAwardValueBean.isAchieved() )
          {
            ParticipantBadge participantBadge = new ParticipantBadge();
            List<Badge> badgeList = new ArrayList<Badge>();
            badgeList = gamificationService.getBadgeByPromotion( promotion.getId() );
            Iterator badgeItr = badgeList.iterator();

            while ( badgeItr.hasNext() )
            {
              Badge badge = (Badge)badgeItr.next();

              if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.EARNED_OR_NOT_EARNED ) && promotion.isGoalQuestOrChallengePointPromotion() )
              {
                if ( !cpPaxAwardValueBean.isPartner() )
                {
                  participantBadge = gamificationService.populateEarnedBadgesCPandGQ( cpPaxAwardValueBean.getPaxGoal().getGoalLevel().getGoalLevelName(),
                                                                                      cpPaxAwardValueBean.getParticipant(),
                                                                                      promotion,
                                                                                      badge,
                                                                                      true );
                }
                else if ( cpPaxAwardValueBean.isPartner() )
                {
                  AbstractGoalLevel goalLevel = cpPaxAwardValueBean.getPaxGoal() != null ? cpPaxAwardValueBean.getPaxGoal().getGoalLevel() : null;
                  if ( goalLevel == null )
                  {
                    goalLevel = cpPaxAwardValueBean.getGoalLevel() != null ? cpPaxAwardValueBean.getGoalLevel() : null;
                  }
                  participantBadge = gamificationService.populateEarnedBadgesCPandGQ( goalLevel != null ? goalLevel.getGoalLevelName() : null,
                                                                                      cpPaxAwardValueBean.getParticipant(),
                                                                                      promotion,
                                                                                      badge,
                                                                                      false );
                }
              }
            }
          }
        }

        if ( cpPaxAwardValueBean.getAwardType() != null && cpPaxAwardValueBean.getAwardType().equals( "basic" ) && cpPaxAwardValueBean.getAwardIssued() != null
            && cpPaxAwardValueBean.getAwardIssued().longValue() > 0 )
        {
          if ( promotion.isBeforeFinalProcessDate() && promotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED ) )
          {
            ChallengePointPromotion challengepointPromotion = cpPaxAwardValueBean.getChallengePointPromotion();

            ChallengepointPaxValueBean challengepointPaxValueBean = null;
            try
            {
              challengepointPaxValueBean = getChallengepointProgressService().getChallengepointProgressSummary( challengepointPromotion.getId(), participant.getId() );
              challengepointPaxValueBean.setInterimAwardDeposited( cpPaxAwardValueBean.getAwardIssued() );
            }
            catch( ServiceErrorException e )
            {
              e.printStackTrace();
            }
            sendChallengepointEmailConfirmation( promotion, participant, challengepointPaxValueBean, MessageService.CHALLENGEPOINT_INTERIMPAYOUTPROCESSED_MESSAGE_CM_ASSET_CODE, mailingBatchHolder );
          }
        }
        if ( cpPaxAwardValueBean.getAwardType() != null && cpPaxAwardValueBean.getAwardType().equals( "manageroverride" )
            && promotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_ACHIEVED ) )
        {
          Participant pax = null;
          if ( cpPaxAwardValueBean.getGoalSelecter() != null )
          {
            pax = (Participant)cpPaxAwardValueBean.getGoalSelecter();
          }
          else
          {
            pax = participant;
          }

          PaxGoal paxGoal = challengePointService.getPaxChallengePoint( promotion.getId(), pax.getId() );
          if ( paxGoal != null )
          {
            ChallengePointCalculationResult challengePointCalculationResult = getChallengePointCalculationResult( promotion, paxGoal );

            sendChallengepointAchievedOrNotEmailConfirmation( promotion, participant, challengePointCalculationResult, cpPaxAwardValueBean, mailingBatchHolder );
          }
        }
      }
    }
    return depositProcessBean;
  }

  private ChallengePointCalculationResult getChallengePointCalculationResult( ChallengePointPromotion promotion, PaxGoal paxGoal )
  {
    ChallengePointCalculationResult challengePointCalculationResult = null;
    challengePointCalculationResult = challengePointAchievementStrategyFactory.getChallengePointAchievementStrategy( promotion.getAchievementRule().getCode() ).processChallengePoint( paxGoal );
    return challengePointCalculationResult;
  }

  /*
   * Wrapper Method for EmailTotalAward private void setChallengepointEmailTotalAward(Participant
   * participant,ChallengepointPaxAwardValueBean cpPaxAwardValueBean) { boolean basicFlag=false;
   * //ChallengepointPaxAwardValueBean cpPaxAwardValueBeanTemp=null; //the below for block is to get
   * the total payout for a participant if( cpPaxAwardValueBean.getAwardType() != null &&
   * cpPaxAwardValueBean.getAwardType().equals("basic") ) { basicFlag=true; } if(basicFlag &&
   * cpPaxAwardValueBean.getTotalAwardIssued()!=null &&
   * cpPaxAwardValueBeanTemp.getTotalAwardIssued()!=null &&
   * cpPaxAwardValueBeanTemp.getTotalAwardIssued
   * ().longValue()>cpPaxAwardValueBean.getTotalAwardIssued().longValue()){
   * cpPaxAwardValueBean.setEmailTotalAwardIssued(cpPaxAwardValueBeanTemp.getTotalAwardIssued());
   * }else if(basicFlag && cpPaxAwardValueBeanTemp.getTotalAwardIssued()!=null &&
   * cpPaxAwardValueBean.getTotalAwardIssued()==null) {
   * cpPaxAwardValueBean.setEmailTotalAwardIssued(cpPaxAwardValueBeanTemp.getTotalAwardIssued());
   * }else {
   * cpPaxAwardValueBean.setEmailTotalAwardIssued(cpPaxAwardValueBean.getTotalAwardIssued()); } }
   */

  private void setChallengepointEmailTotalAward( List challengepointCalculationResults, Participant participant, ChallengepointPaxAwardValueBean cpPaxAwardValueBean )
  {
    boolean basicFlag = false;
    ChallengepointPaxAwardValueBean cpPaxAwardValueBeanTemp = null;
    // the below for block is to get the total payout for a participant
    for ( int temp = 0; temp < challengepointCalculationResults.size(); temp++ )
    {
      cpPaxAwardValueBeanTemp = (ChallengepointPaxAwardValueBean)challengepointCalculationResults.get( temp );
      Participant participantTemp = getParticipant( cpPaxAwardValueBeanTemp.getParticipant().getId() );
      if ( !participantTemp.getId().equals( participant.getId() ) )
      {
        continue;
      }
      else
      {
        if ( cpPaxAwardValueBeanTemp.getAwardType() != null && cpPaxAwardValueBeanTemp.getAwardType().equals( "basic" ) )
        {
          basicFlag = true;
          break;
        }
      }

    }

    if ( basicFlag && cpPaxAwardValueBean.getTotalAwardIssued() != null && cpPaxAwardValueBeanTemp.getTotalAwardIssued() != null
        && cpPaxAwardValueBeanTemp.getTotalAwardIssued().longValue() > cpPaxAwardValueBean.getTotalAwardIssued().longValue() )
    {
      cpPaxAwardValueBean.setEmailTotalAwardIssued( cpPaxAwardValueBeanTemp.getTotalAwardIssued() );
    }
    else if ( basicFlag && cpPaxAwardValueBeanTemp.getTotalAwardIssued() != null && cpPaxAwardValueBean.getTotalAwardIssued() == null )
    {
      if ( isPaxOptedOutOfAwards( participant.getId() ) )
      {
        cpPaxAwardValueBean.setEmailTotalAwardIssued( new Long( 0 ) );// Opt Out of awards will
                                                                      // override the original value
                                                                      // to zero.
      }
      else
      {
        cpPaxAwardValueBean.setEmailTotalAwardIssued( cpPaxAwardValueBeanTemp.getTotalAwardIssued() );
      }
    }
    else
    {
      if ( isPaxOptedOutOfAwards( participant.getId() ) )
      {
        cpPaxAwardValueBean.setEmailTotalAwardIssued( new Long( 0 ) );// Opt Out of awards will
                                                                      // override the original value
                                                                      // to zero.
      }
      else
      {
        cpPaxAwardValueBean.setEmailTotalAwardIssued( cpPaxAwardValueBean.getTotalAwardIssued() );
      }
    }

  }

  private ChallengepointAward createCPAward( ChallengepointPaxAwardValueBean cpPaxAwardValueBean, ChallengePointPromotion promotion, Participant participant, Journal journal )
  {
    ChallengepointAward challengepointAward = new ChallengepointAward();
    challengepointAward.setAwardEarned( cpPaxAwardValueBean.getAwardEarned() );
    challengepointAward.setAwardIssued( cpPaxAwardValueBean.getAwardIssued() );
    if ( isPaxOptedOutOfAwards( participant.getId() ) )
    {
      challengepointAward.setTotalAwardIssued( new Long( 0 ) );// Opt Out of awards will override
                                                               // the original value to zero.
    }
    else
    {
      challengepointAward.setTotalAwardIssued( cpPaxAwardValueBean.getTotalAwardIssued() );
    }
    challengepointAward.setAwardType( cpPaxAwardValueBean.getAwardType() );
    challengepointAward.setChallengePointPromotion( promotion );
    challengepointAward.setJournal( journal );
    challengepointAward.setParticipant( participant );
    challengepointAward.setResult( cpPaxAwardValueBean.getResult() );
    return challengepointAward;
  }

  private Participant getParticipant( Long id )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    return participantService.getParticipantByIdWithAssociations( id, associationRequestCollection );
  }

  private Activity createActivityForGoalQuest( GoalCalculationResult goalCalculationResult, GoalQuestPromotion promotion, Participant participant )
  {
    GoalQuestPayoutActivity goalQuestPayoutActivity = new GoalQuestPayoutActivity( GuidUtils.generateGuid() );
    goalQuestPayoutActivity.setPosted( true );
    goalQuestPayoutActivity.setSubmissionDate( DateUtils.getCurrentDate() );
    goalQuestPayoutActivity.setPromotion( promotion );
    goalQuestPayoutActivity.setParticipant( participant );

    if ( goalCalculationResult.getTotalPerformance() != null )
    {
      if ( isPaxOptedOutOfAwards( participant.getId() ) )
      {
        goalQuestPayoutActivity.setAmountAchieved( new Long( 0 ) );// Opt Out of awards will
                                                                   // override the original value to
                                                                   // zero.
      }
      else
      {
        goalQuestPayoutActivity.setAmountAchieved( new Long( goalCalculationResult.getTotalPerformance().longValue() ) );
      }
    }

    if ( goalCalculationResult.isManager() )
    {
      if ( goalCalculationResult.getNodeId() != null )
      {
        Node managersNode = nodeDAO.getNodeById( goalCalculationResult.getNodeId() );
        goalQuestPayoutActivity.setNode( managersNode );
      }
    }
    return activityDAO.saveActivity( goalQuestPayoutActivity );
  }

  private Activity createActivityForChallengepoint( ChallengepointPaxAwardValueBean challengepointAward, ChallengePointPromotion promotion, Participant participant )
  {
    ChallengepointPayoutActivity challengepointPayoutActivity = new ChallengepointPayoutActivity( GuidUtils.generateGuid() );
    challengepointPayoutActivity.setPosted( true );
    challengepointPayoutActivity.setSubmissionDate( DateUtils.getCurrentDate() );
    challengepointPayoutActivity.setPromotion( promotion );
    challengepointPayoutActivity.setParticipant( participant );
    if ( challengepointAward.getResult() != null )
    {
      if ( isPaxOptedOutOfAwards( participant.getId() ) )
      {
        challengepointPayoutActivity.setAmountAchieved( new Long( 0 ) );// Opt Out of awards will
                                                                        // override the original
                                                                        // value to zero.
      }
      else
      {
        challengepointPayoutActivity.setAmountAchieved( new Long( challengepointAward.getResult().longValue() ) );
      }
    }
    if ( challengepointAward.getParticipant().isManager() )
    {
      if ( challengepointAward.getNodeId() != null )
      {
        Node managersNode = nodeDAO.getNodeById( challengepointAward.getNodeId() );
        challengepointPayoutActivity.setNode( managersNode );
      }
    }
    return activityDAO.saveActivity( challengepointPayoutActivity );
  }

  private MerchOrder createMerchOrder( GoalQuestPromotion promotion, Participant participant, Activity activity, MerchOrder merchOrder )
  {
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );
    merchOrder.setParticipant( participant );
    merchOrder.setRedeemed( false );
    merchOrder.setProgramId( promotion.getProgramId() );
    // merchOrder.setPaxGoal( paxGoal ) ;
    merchOrder.setMerchGiftCodeType( promotion.getMerchGiftCodeType() );
    merchOrder.setBatchId( merchOrderDAO.getNextBatchId() );

    if ( paxGoal.getGoalLevel() instanceof GoalLevel )
    {
      GoalLevel goal = (GoalLevel)paxGoal.getGoalLevel();
      merchOrder.setPoints( goal.getAward().intValue() );
      merchOrder.setLevelPosition( new Long( goal.getSequenceNumber() ) );
    }
    Set activitySet = new HashSet();
    activitySet.add( activity );
    merchOrder.addActivities( activitySet );
    /* WIP# 25130 Start */
    if ( promotion.isBillCodesActive() )
    {
      billingCodeStrategy.setMerchOrderBillingCodes( merchOrder, promotion, activitySet );
    }
    /* WIP# 25130 End */
    merchOrderDAO.saveMerchOrder( merchOrder );
    return merchOrder;
  }

  private Journal createJournal( GoalCalculationResult goalCalculationResult, GoalQuestPromotion promotion, Participant participant, Activity activity )
  {
    Participant pax = participantDAO.getParticipantById( goalCalculationResult.getReciever().getId() );
    // Bug 53489 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( participant != null && participant.getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( promotion.getPromoNameAssetCode(), participant.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    Journal journal = new Journal();
    journal.setGuid( GuidUtils.generateGuid() );
    journal.setTransactionDate( DateUtils.getCurrentDate() );
    journal.setPromotion( promotion );
    journal.setParticipant( pax );
    journal.setTransactionDescription( promotionName );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    if ( goalCalculationResult.getCalculatedPayout() != null )
    {
      if ( isPaxOptedOutOfAwards( participant.getId() ) )
      {
        journal.setTransactionDescription( promotionName + OPT_OUT_TEXT ); // Add extra description,
                                                                           // might be useful for
                                                                           // debug.
        journal.setTransactionAmount( new Long( 0 ) );// Opt Out of awards will override the
                                                      // original value to zero.
      }
      else
      {
        journal.setTransactionAmount( new Long( goalCalculationResult.getCalculatedPayout().longValue() ) );
      }

    }
    if ( participant != null )
    {
      journal.setAccountNumber( participant.getAwardBanqNumber() );
    }
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      journal.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    }
    else
    {
      journal.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.MERCHANDISE ) );
    }
    Set activitySet = new HashSet();
    activitySet.add( activity );
    journal.addActivities( activitySet );
    journalService.saveJournalEntry( journal );
    return journal;

  }

  private boolean isPaxOptedOutOfAwards( Long paxId )
  {
    return participantDAO.isPaxOptedOutOfAwards( paxId );
  }

  private MerchOrder createCPMerchOrder( ChallengePointPromotion promotion, Participant participant, IssuedOMGiftCode giftCode, Activity activity )
  {
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );
    MerchOrder merchOrder = new MerchOrder();
    merchOrder.setExpirationDate( giftCode.getExpirationDate() );
    merchOrder.setFullGiftCode( giftCode.getGiftCode() );
    merchOrder.setParticipant( participant );
    merchOrder.setRedeemed( false );
    merchOrder.setReferenceNumber( giftCode.getReferenceNumber() );
    merchOrder.setProgramId( promotion.getProgramId() );
    // merchOrder.setPaxGoal( paxGoal ) ;
    merchOrder.setMerchGiftCodeType( promotion.getMerchGiftCodeType() );

    if ( paxGoal.getGoalLevel() instanceof GoalLevel )
    {
      GoalLevel goal = (GoalLevel)paxGoal.getGoalLevel();
      merchOrder.setPoints( goal.getAward().intValue() );
      merchOrder.setLevelPosition( new Long( goal.getSequenceNumber() ) );
    }
    Set activitySet = new HashSet();
    activitySet.add( activity );
    merchOrder.addActivities( activitySet );

    merchOrderDAO.saveMerchOrder( merchOrder );
    return merchOrder;
  }

  private Journal createCPJournal( ChallengepointPaxAwardValueBean challengepointPaxAwardValueBean, ChallengePointPromotion promotion, Participant participant, Activity activity )

  {
    Participant pax = participantDAO.getParticipantById( challengepointPaxAwardValueBean.getParticipant().getId() );
    // Bug 53489 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( participant != null && participant.getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( promotion.getPromoNameAssetCode(), participant.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    Journal journal = new Journal();
    journal.setGuid( GuidUtils.generateGuid() );
    journal.setTransactionDate( DateUtils.getCurrentDate() );
    journal.setPromotion( promotion );
    journal.setParticipant( pax );
    journal.setTransactionDescription( promotionName );
    journal.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    if ( challengepointPaxAwardValueBean.getAwardType() != null && challengepointPaxAwardValueBean.getAwardType().equals( "basic" ) )
    {
      journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
      if ( challengepointPaxAwardValueBean.getAwardIssued() != null && challengepointPaxAwardValueBean.getAwardIssued().longValue() > 0 )
      {
        if ( isPaxOptedOutOfAwards( participant.getId() ) )
        {
          journal.setTransactionDescription( promotionName + OPT_OUT_TEXT ); // Add extra
                                                                             // description, might
                                                                             // be useful for debug.
          journal.setTransactionAmount( new Long( 0 ) );// Opt Out of awards will override the
                                                        // original value to zero.
        }
        else
        {
          journal.setTransactionAmount( new Long( challengepointPaxAwardValueBean.getAwardIssued().longValue() ) );
        }
      }
      journal.setAccountNumber( participant.getAwardBanqNumber() );
      if ( promotion.isAfterFinalProcessDate() )
      {
        journal.setJournalType( Journal.CHALLENGEPOINT_FINAL_BASIC_AWARD );
      }
      else
      {
        journal.setJournalType( Journal.CHALLENGEPOINT_BASIC_AWARD );
      }
    } // Either challengepoing type with points type or manageroverride.
    else if ( challengepointPaxAwardValueBean.getAwardType() != null && challengepointPaxAwardValueBean.getAwardType().equals( "challengepoint" )
        && promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) || challengepointPaxAwardValueBean.getAwardType().equals( "manageroverride" )
        || challengepointPaxAwardValueBean.getAwardType().equals( "partner" ) )

    {
      journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
      if ( challengepointPaxAwardValueBean.getCalculatedAchievement() != null && challengepointPaxAwardValueBean.getCalculatedAchievement().longValue() > 0 )
      {
        if ( isPaxOptedOutOfAwards( participant.getId() ) )
        {
          journal.setTransactionDescription( promotionName + OPT_OUT_TEXT ); // Add extra
                                                                             // description, might
                                                                             // be useful for debug.
          journal.setTransactionAmount( new Long( 0 ) );// Opt Out of awards will override the
                                                        // original value to zero.
        }
        else
        {
          journal.setTransactionAmount( new Long( challengepointPaxAwardValueBean.getCalculatedAchievement().longValue() ) );
        }
      }
      journal.setAccountNumber( participant.getAwardBanqNumber() );
      if ( challengepointPaxAwardValueBean.getAwardType().equals( "manageroverride" ) )
      {
        journal.setJournalType( Journal.MANAGER_OVERRIDE );
      }
      else if ( challengepointPaxAwardValueBean.getAwardType().equals( "partner" ) )
      {
        journal.setJournalType( Journal.PARTNER_AWARD );
      }
      else
      {
        journal.setJournalType( Journal.CHALLENGEPOINT_CPACHIEVEMENT_AWARD );
      }
    }
    else if ( challengepointPaxAwardValueBean.getAwardType() != null && ( challengepointPaxAwardValueBean.getAwardType().equals( "challengepoint" )
        || challengepointPaxAwardValueBean.getAwardType().equals( "manageroverride" ) || challengepointPaxAwardValueBean.getAwardType().equals( "partner" ) )
        && promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
    {
      if ( challengepointPaxAwardValueBean.getAwardType().equals( "manageroverride" ) )
      {
        journal.setJournalType( Journal.MANAGER_OVERRIDE );
      }
      else if ( challengepointPaxAwardValueBean.getAwardType().equals( "partner" ) )
      {
        journal.setJournalType( Journal.PARTNER_AWARD );
      }
      else
      {
        journal.setJournalType( Journal.CHALLENGEPOINT_CPACHIEVEMENT_AWARD );
      }
    }
    Set activitySet = new HashSet();
    activitySet.add( activity );
    journal.addActivities( activitySet );
    journalService.saveJournalEntry( journal );
    return journal;

  }

  public GiftcodesResponseValueObject retrieveGiftCodesForMerchandiseOrTravelWebService( String programId, Participant participant, long valueOfGiftCode, String batchId, Object... arguments )
  {
    if ( programId == null )
    {
      throw new BeaconRuntimeException( "The Program Id cannot be null" );
    }
    // previously always 1, now use optionally passed value
    int noOfGiftCodes = 1;
    if ( arguments != null && arguments.length >= 1 && null != arguments[0] )
    {
      noOfGiftCodes = ( (Long)arguments[0] ).intValue();
    }

    // get giftCodeList from a AwardBanQ API service call
    try
    {
      return awardBanQServiceFactory.getAwardBanQService().getGiftCodesWebService( programId, noOfGiftCodes, valueOfGiftCode, batchId );
    }
    catch( ServiceErrorException see )
    {
      // append the second and third parameter to include the program number and participant id to
      // the existing
      // service error
      ServiceError se = (ServiceError)see.getServiceErrors().get( 0 );
      String[] args = new String[4];
      args[0] = se.getArg1();
      args[1] = se.getArg2();
      args[2] = programId;
      args[3] = null != participant ? participant.getId().toString() : "NON-PAX";
      se.setArgs( args );
      // throw BeaconRuntimeException so that transaction will be rolled back automatically
      throw new GiftCodesWebServiceException( see ); // bug 66870
    }
  }

  /*
   * SK private void sendGoalAchievedOrNotEmailConfirmation( GoalQuestPromotion promotion,
   * Participant participant, GoalCalculationResult goalCalculationResult , boolean surveyFlag) { }
   */
  private void sendGoalAchievedOrNotEmailConfirmation( GoalQuestPromotion promotion, Participant participant, GoalCalculationResult goalCalculationResult, MailingBatchHolder mailingBatchHolder )
  {
    MailingRecipient mailingRecipient = null;
    MailingBatch mailingBatch = null;

    boolean goalAchieved = promotion.isNotificationRequired( PromotionEmailNotificationType.GOAL_ACHIEVED );
    boolean goalNotAchieved = promotion.isNotificationRequired( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED );
    boolean partnerAchieved = promotion.isNotificationRequired( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED );
    boolean partnerNotAchieved = promotion.isNotificationRequired( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED );
    boolean sendEmail = false;
    Message message = null;

    // Partner Notification email
    if ( goalCalculationResult.isPartner() )
    {
      // If Partner achieved a payout
      if ( goalCalculationResult.isAchieved() && partnerAchieved )
      {
        if ( mailingBatchHolder.getPartnerAchievedMailingBatch() == null ) // generate Partner
                                                                           // Achieved Batch
        {
          // create partner, and set in holder
          MailingBatch partnerAchievedMailingBatch = mailingService.applyBatch( BEAN_NAME + " <i>Partner Achieved</i> " + promotion.getName() );
          mailingBatchHolder.setPartnerAchievedMailingBatch( partnerAchievedMailingBatch );
        }
        mailingBatch = mailingBatchHolder.getPartnerAchievedMailingBatch();
        message = getMessage( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED, promotion );
        sendEmail = true;
      } // end achieved

      // If Partner not achieved a payout, but partners PAX did achieve a payout
      else if ( goalCalculationResult.isPartnersParticipantAchieved() && partnerAchieved )
      {
        if ( mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch() == null ) // generate
                                                                                   // Partner no
                                                                                   // payout batch
        {
          // create partner, and set in holder
          MailingBatch partnerAchievedNoPayoutMailingBatch = mailingService.applyBatch( BEAN_NAME + " <i>Partner No Payout</i> " + promotion.getName() );
          mailingBatchHolder.setPartnerAchievedNoPayoutMailingBatch( partnerAchievedNoPayoutMailingBatch );
        }
        mailingBatch = mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch();
        message = messageService.getMessageByCMAssetCode( MessageService.GOALQUEST_PARTNER_GOAL_ACHIEVED_NO_PAYOUT_MESSAGE_CM_ASSET_CODE );
        sendEmail = true;
      } // end achieved with NO payout

      // If both Participant and Partner not achieved a payout
      else if ( partnerNotAchieved )
      {
        message = getMessage( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED, promotion );
        if ( mailingBatchHolder.getPartnerNotAchievedMailingBatch() == null ) // generate Partner
                                                                              // not achieved
        {
          // create partner, and set in holder
          MailingBatch partnerNotAchievedMailingBatch = mailingService.applyBatch( BEAN_NAME + " <i>Partner Not Acheived</i> " + promotion.getName() );
          mailingBatchHolder.setPartnerNotAchievedMailingBatch( partnerNotAchievedMailingBatch );
        }
        mailingBatch = mailingBatchHolder.getPartnerNotAchievedMailingBatch();
        sendEmail = true;
      } // end not achieved

      if ( sendEmail )
      {
        // ( Participant )goalCalculationResult.getPartnerToParticipant() - Is the participant
        // participant - Is the Partner
        // Todo SK - check whether we need this here.
        HashMap dataMap = mailingService.buildMailingRecipientForPartnerGoalQuestEmail( promotion, (Participant)goalCalculationResult.getPartnerToParticipant(), goalCalculationResult, participant );
        mailingRecipient = new MailingRecipient();
        mailingRecipient.setGuid( GuidUtils.generateGuid() );
        mailingRecipient.setUser( goalCalculationResult.getReciever() ); // Fix 28890 It should send
                                                                         // partner achievement mail
                                                                         // to partners not to pax
        if ( goalCalculationResult.getReciever().getLanguageType() != null )
        {
          mailingRecipient.setLocale( goalCalculationResult.getReciever().getLanguageType().getCode() );
        }
        else
        {
          mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
        }
        mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      }
    }
    // PAX Notification email
    else
    {

      if ( goalCalculationResult.isAchieved() && goalAchieved )
      {
        mailingBatch = mailingBatchHolder.getPaxAchievedMailingBatch();
        message = getMessage( PromotionEmailNotificationType.GOAL_ACHIEVED, promotion );
        sendEmail = true;
      }
      else if ( goalNotAchieved )
      {
        mailingBatch = mailingBatchHolder.getPaxNotAchievedMailingBatch();
        message = getMessage( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED, promotion );
        sendEmail = true;
      }

      if ( sendEmail )
      {
        List paxPartners = getPaxPartners( promotion, participant );
        if ( paxPartners == null || paxPartners.size() == 0 )
        {
          mailingRecipient = mailingService.buildMailingRecipientForGoalQuestEmail( promotion, participant, goalCalculationResult, null, null );
        }
        else
        {
          mailingRecipient = mailingService.buildMailingRecipientForGoalQuestEmail( promotion, participant, goalCalculationResult, paxPartners, null );
        }
      }
    }

    if ( sendEmail )
    {
      // Create mailing object
      Mailing mailing = new Mailing();
      mailing.setMessage( message );
      mailing.addMailingRecipient( mailingRecipient );
      mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
      mailing.setSender( "GoalQuest Promotion" );
      mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
      mailing.setGuid( GuidUtils.generateGuid() );
      mailing.setMailingBatch( mailingBatch );
      mailingService.submitMailing( mailing, null );
    }
  }

  private void savePayoutCalculationAudit( Participant participant, Journal journal, boolean isOwner )
  {
    // Create the payout calculation audit record.
    GoalQuestPayoutCalculationAudit auditRecord = new GoalQuestPayoutCalculationAudit();
    auditRecord.setParticipant( participant );
    auditRecord.setJournal( journal );
    if ( isOwner )
    {
      // If payout is for manager override achievement then set mo_success as reason type in payout
      // audit table.
      auditRecord.setReasonType( PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.MANAGER_OVERRIDE ) );
    }
    else
    {
      auditRecord.setReasonType( PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.GOALQUEST_SUCCESS ) );
    }

    auditRecord.setReasonText( "GoalQuest Promotion payout calculation succeeded." );
    // Save Audit
    payoutCalculationAuditDAO.save( auditRecord );
  }

  private void sendChallengepointEmailConfirmation( ChallengePointPromotion promotion,
                                                    Participant participant,
                                                    ChallengepointPaxValueBean cpPaxValueBean,
                                                    String messageCMCode,
                                                    MailingBatchHolder mailingBatchHolder )
  {

    MailingRecipient mailingRecipient = null;

    mailingRecipient = mailingService.buildMailingRecipientForChallengepointEmail( promotion, participant, cpPaxValueBean, null, null );

    Message message = messageService.getMessageByCMAssetCode( messageCMCode );
    if ( mailingBatchHolder != null && mailingBatchHolder.getChallengePointInterimPayoutBatch() == null ) // generate
                                                                                                          // Interim
                                                                                                          // Batch
                                                                                                          // if
                                                                                                          // necessary
    {
      // create partner progress batch, and set in holder
      MailingBatch challengePointInterimPayoutBatch = mailingService.applyBatch( "<i>CP Interim Payout</i> " + promotion.getName() );
      mailingBatchHolder.setChallengePointInterimPayoutBatch( challengePointInterimPayoutBatch );
    }
    // Create mailing object
    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    if ( mailingBatchHolder != null )
    {
      mailing.setMailingBatch( mailingBatchHolder.getChallengePointInterimPayoutBatch() );
    }
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    mailing.setSender( "Challengepoint Promotion" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setGuid( GuidUtils.generateGuid() );
    mailingService.submitMailing( mailing, null );
  }

  private void sendChallengepointAchievedOrNotEmailConfirmation( ChallengePointPromotion promotion,
                                                                 Participant participant,
                                                                 ChallengePointCalculationResult challengePointCalculationResult,
                                                                 ChallengepointPaxAwardValueBean cpPaxAwardValueBean,
                                                                 MailingBatchHolder mailingBatchHolder )
  {
    String messageCMCode = null;
    MailingBatch mailingBatch = null;
    MailingRecipient mailingRecipient = null;

    // Partner Notification email
    if ( cpPaxAwardValueBean.isPartner() )
    {
      // If Partner achieved a payout
      if ( challengePointCalculationResult.isAchieved() )
      {
        if ( mailingBatchHolder.getPartnerAchievedMailingBatch() == null ) // generate Partner
                                                                           // Achieved Batch
        {
          // create partner, and set in holder
          MailingBatch partnerAchievedMailingBatch = mailingService.applyBatch( BEAN_NAME + " <i>Partner Achieved</i> " + promotion.getName() );
          mailingBatchHolder.setPartnerAchievedMailingBatch( partnerAchievedMailingBatch );
        }
        mailingBatch = mailingBatchHolder.getPartnerAchievedMailingBatch();
        messageCMCode = MessageService.CHALLENGEPOINT_PARTNER_GOAL_ACHIEVED_WITH_PAYOUT_MESSAGE_CM_ASSET_CODE;
      }

      // If Partner not achieved a payout, but partners PAX did achieve a payout
      else if ( challengePointCalculationResult.isPartnersParticipantAchieved() )
      {
        if ( mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch() == null ) // generate
                                                                                   // Partner no
                                                                                   // payout batch
        {
          // create partner, and set in holder
          MailingBatch partnerAchievedNoPayoutMailingBatch = mailingService.applyBatch( BEAN_NAME + " <i>Partner No Payout</i> " + promotion.getName() );
          mailingBatchHolder.setPartnerAchievedNoPayoutMailingBatch( partnerAchievedNoPayoutMailingBatch );
        }
        mailingBatch = mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch();
        messageCMCode = MessageService.CHALLENGEPOINT_PARTNER_GOAL_ACHIEVED_NO_PAYOUT_MESSAGE_CM_ASSET_CODE;
      } // end achieved with NO payout

      else
      {
        if ( mailingBatchHolder.getPartnerNotAchievedMailingBatch() == null ) // generate Partner
        // Achieved Batch
        {
          // create partner, and set in holder
          MailingBatch partnerAchievedMailingBatch = mailingService.applyBatch( BEAN_NAME + " <i>Partner NOT Achieved</i> " + promotion.getName() );
          mailingBatchHolder.setPartnerNotAchievedMailingBatch( partnerAchievedMailingBatch );
        }
        mailingBatch = mailingBatchHolder.getPartnerNotAchievedMailingBatch();
        messageCMCode = MessageService.CHALLENGEPOINT_PARTNER_GOAL_NOT_ACHIEVED_MESSAGE_CM_ASSET_CODE;
      } // end not achieved

      if ( cpPaxAwardValueBean.getParticipant().isActive().booleanValue() )
      {
        HashMap dataMap = mailingService.buildMailingRecipientForPartnerCPEmail( promotion, participant, challengePointCalculationResult, cpPaxAwardValueBean.getParticipant() );

        mailingRecipient = new MailingRecipient();
        mailingRecipient.setGuid( GuidUtils.generateGuid() );
        mailingRecipient.setUser( cpPaxAwardValueBean.getParticipant() );
        mailingRecipient.setLocale( cpPaxAwardValueBean.getParticipant().getLanguageType() != null
            ? cpPaxAwardValueBean.getParticipant().getLanguageType().getCode()
            : systemVariableService.getDefaultLanguage().getStringVal() );
        mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      }
    }

    // PAX Notification email
    else
    {
      if ( cpPaxAwardValueBean.isAchieved() )
      {
        mailingBatch = mailingBatchHolder.getPaxAchievedMailingBatch();
        messageCMCode = MessageService.CHALLENGEPOINT_ACHIEVED_MESSAGE_CM_ASSET_CODE;
      }
      else
      {
        mailingBatch = mailingBatchHolder.getPaxNotAchievedMailingBatch();
        messageCMCode = MessageService.CHALLENGEPOINT_NOTACHIEVED_MESSAGE_CM_ASSET_CODE;
      }
      mailingRecipient = mailingService.buildMailingRecipientForChallengepointEmail( promotion, participant, cpPaxAwardValueBean, null );
    }

    Message message = messageService.getMessageByCMAssetCode( messageCMCode );

    // Create mailing object
    Mailing mailing = new Mailing();
    mailing.setMailingBatch( mailingBatch );
    mailing.setMessage( message );
    if ( mailingRecipient != null )
    {
      mailing.addMailingRecipient( mailingRecipient );
    }
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    mailing.setSender( "Challengepoint Promotion" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setGuid( GuidUtils.generateGuid() );
    mailingService.submitMailing( mailing, null );

  }

  private void saveCPPayoutCalculationAudit( Participant participant, Journal journal, String awardType )
  {
    // Create the payout calculation audit record.
    ChallengepointPayoutCalculationAudit auditRecord = new ChallengepointPayoutCalculationAudit();
    auditRecord.setParticipant( participant );
    auditRecord.setJournal( journal );
    if ( awardType.equals( "manageroverride" ) )
    {
      auditRecord.setReasonType( PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.MANAGER_OVERRIDE ) );
    }
    else if ( awardType.equals( "basic" ) )
    {
      auditRecord.setReasonType( PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.CP_BASIC_SUCCESS ) );
    }
    else
    {
      auditRecord.setReasonType( PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.CHALLENGEPOINT_SUCCESS ) );
    }
    auditRecord.setReasonText( "Challengepoint Promotion payout calculation succeeded." );
    // Save Audit
    payoutCalculationAuditDAO.save( auditRecord );

  }

  private Set calculatePayout( PayoutCalculationFacts facts, Promotion promotion, Set activities, String payoutType )
  {
    PayoutStrategy payoutStrategy = payoutStrategyFactory.getPayoutStrategy( payoutType );
    return payoutStrategy.processActivities( activities, promotion, facts );
  }

  private Set loadActivities( PayoutCalculationFacts facts, Promotion promotion, String payoutType )
  {
    // FLUSH: The flush was needed after hibernate 3.2.3 upgrade not
    // doing flush before fetch.
    getHibernateSession().flush();
    ActivityLoader activityLoader = activityLoaderFactory.getActivityLoader( payoutType );
    return activityLoader.loadActivities( facts, promotion );
  }

  private void saveGeneratedActivities( Set payoutCalculationResults )
  {
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      for ( Iterator iterator = payoutCalculationResult.getGeneratedActivities().iterator(); iterator.hasNext(); )
      {
        Activity activity = (Activity)iterator.next();
        activityDAO.saveActivity( activity );
      }
    }
  }

  private Journal createJournalEntry( PayoutCalculationResult payoutCalculationResult, Promotion promotion, Long participantId )
  {
    // participant retrieved from session to avoid hibernate non-unique exception.
    Participant participant = participantDAO.getParticipantById( participantId );

    // Bug 53489 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( participant != null && participant.getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( promotion.getPromoNameAssetCode(), participant.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    Journal journalEntry = new Journal();

    journalEntry.setGuid( GuidUtils.generateGuid() );
    journalEntry.setParticipant( participant );
    if ( participant != null )
    {
      journalEntry.setAccountNumber( participant.getAwardBanqNumber() );
    }
    journalEntry.setTransactionDate( new Date() );
    journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );

    markJournalStatusPendingQualifierIfMinQualiferNotMet( journalEntry, payoutCalculationResult );

    if ( isPaxOptedOutOfAwards( participant.getId() ) )
    {
      journalEntry.setTransactionAmount( new Long( 0 ) );
    }
    else
    {
      journalEntry.setTransactionAmount( payoutCalculationResult.getCalculatedPayout() );
    }

    if ( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode().equals( PayoutCalculationAuditReasonType.MANAGER_OVERRIDE ) )
    {
      journalEntry.setJournalType( Journal.MANAGER_OVERRIDE );
      journalEntry.setTransactionDescription( Journal.MANAGER_OVERRIDE + " - " + promotionName );
    }
    else if ( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode().equals( PayoutCalculationAuditReasonType.SWEEPSTAKES_SUCCESS ) )
    {
      journalEntry.setJournalType( Journal.SWEEPSTAKES );
      journalEntry.setTransactionDescription( Journal.SWEEPSTAKES + " - " + promotionName );
    }
    else if ( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode().equals( PayoutCalculationAuditReasonType.STACK_RANK_SUCCESS ) )
    {
      journalEntry.setJournalType( Journal.STACK_RANK );
      journalEntry.setTransactionDescription( Journal.STACK_RANK + " - " + promotionName );
    }
    else
    {
      journalEntry.setTransactionDescription( promotionName );
    }
    // Append description if participant is opted out of awards, to leave a clearer data trail
    if ( participant.getOptOutAwards() )
    {
      journalEntry.setTransactionDescription( journalEntry.getTransactionDescription() + OPT_OUT_TEXT );
    }
    journalEntry.setTransactionCashAmount( payoutCalculationResult.getCalculatedCashPayout() );

    if ( promotion.isNominationPromotion() )
    {
      if ( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode().equals( PayoutCalculationAuditReasonType.SWEEPSTAKES_SUCCESS ) )
      {
        journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
      }
      else
      {
        NominationPromotion nominationPromotion = (NominationPromotion)promotion;
        for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
        {
          ClaimBasedPayoutCalculationAudit claimBasedPayoutCalculationAudit = (ClaimBasedPayoutCalculationAudit)payoutCalculationResult.getPayoutCalculationAudit();
          if ( claimBasedPayoutCalculationAudit.getClaim() != null && claimBasedPayoutCalculationAudit.getClaim().getApprovableItems() != null
              && claimBasedPayoutCalculationAudit.getClaim().getApprovableItems().size() > 0 )
          {
            ClaimRecipient claimRecipient = (ClaimRecipient)claimBasedPayoutCalculationAudit.getClaim().getApprovableItems().iterator().next();
            if ( claimRecipient != null && nominationPromotionLevel.getLevelIndex().intValue() == claimRecipient.getApprovableItemApprovers().size()
                && nominationPromotionLevel.getAwardPayoutType() != null )
            {
              journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( nominationPromotionLevel.getAwardPayoutType().getCode() ) );
              if ( nominationPromotionLevel.getAwardPayoutType().isCashAwardType() )
              {
                journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
                journalEntry.setAccountNumber( null );
              }
              break;
            }
          }
          else
          {
            ClaimGroup claimGroup = (ClaimGroup)claimBasedPayoutCalculationAudit.getClaimGroup();
            if ( claimGroup != null && nominationPromotionLevel.getLevelIndex().intValue() == claimGroup.getApprovableItemApprovers().size() && nominationPromotionLevel.getAwardPayoutType() != null )
            {
              journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( nominationPromotionLevel.getAwardPayoutType().getCode() ) );
              if ( nominationPromotionLevel.getAwardPayoutType().isCashAwardType() )
              {
                journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
                journalEntry.setAccountNumber( null );
              }
              break;
            }
          }
        }
      }
    }
    else
    {
      journalEntry.setAwardPayoutType( promotion.getAwardType() );
    }
    journalEntry.addActivities( payoutCalculationResult.getContributingActivities() );
    journalEntry.setPromotion( promotion );

    return journalEntry;
  }

  /**
   * Note: this is currently only applicable to Product Claims where minimum qualifier is used.
   * 
   * @param journalEntry
   * @param payoutCalculationResult
   */
  private void markJournalStatusPendingQualifierIfMinQualiferNotMet( Journal journalEntry, PayoutCalculationResult payoutCalculationResult )
  {
    MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
    if ( minimumQualifierStatus != null && !minimumQualifierStatus.isMinQualifierMet() )
    {
      // qualifer exists but has not been met
      journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.PENDING_MINIMUM_QUALIFIER ) );
    }

  }

  private List saveJournalEntriesAndAudits( Set payoutCalculationResults, Promotion promotion, Participant participant )
  {
    List persistedJournalEntries = new ArrayList();

    // Journal entries only saved for pax. If participant is null then this is a non-pax award
    if ( participant != null && participant.getId() != null && participant.getId().longValue() != 0 )
    {
      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

        PayoutCalculationAudit payoutCalculationAudit = payoutCalculationResult.getPayoutCalculationAudit();

        // Save Journal if successful
        if ( payoutCalculationResult.isCashCalculationSuccessful() || payoutCalculationResult.isCalculationSuccessful()
            || promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
        {
          Journal journalEntry = createJournalEntry( payoutCalculationResult, promotion, participant.getId() );

          journalEntry = getJournalService().saveJournalEntry( journalEntry );
          persistedJournalEntries.add( journalEntry );

          payoutCalculationAudit.setJournal( journalEntry );
          payoutCalculationResult.setJournal( journalEntry );
        }else if ( promotion.getAdihCashOption() )
        {
            // Claim can be here only if it is approved and no awards attached to release immediately
            Claim claim = ( (ClaimBasedPayoutCalculationAudit)payoutCalculationAudit ).getClaim();
            if ( !claim.isOpen() )
            {
              getJournalService().sendCustomRecognizedReceivedEmail( claim );
            }
          }

        // Save Audit
        payoutCalculationAuditDAO.save( payoutCalculationAudit );
      }
    }

    return persistedJournalEntries;
  }

  /**
   * Deposit all of the posted payout record. If a budget is insufficient for deposit of payout
   * Journal record is marked as pending.
   * 
   * @param payoutCalculationResults
   */
  public void depositPostedPayouts( Set payoutCalculationResults ) throws ServiceErrorException
  {
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
      if ( payoutCalculationResult.isCalculationSuccessful() || payoutCalculationResult.getJournal() != null && payoutCalculationResult.getJournal().getPromotion().isRecognitionPromotion()
          && payoutCalculationResult.getJournal().getPromotion().getAwardType().isMerchandiseAwardType() )
      {
        Journal journal = payoutCalculationResult.getJournal();

        if ( journal.getJournalStatusType() != null && journal.getJournalStatusType().getCode().equals( JournalStatusType.POST ) )
        {
          journal.setProcessStartDate( payoutCalculationResult.getPayoutCalculationAudit().getProcessStartDate() );
          journal.setProcessEndDate( payoutCalculationResult.getPayoutCalculationAudit().getProcessEndDate() );
          boolean isRetryEnabled = true;
          journalService.saveAndDepositJournalEntry( journal, isRetryEnabled );
        } // if
      } // if isCalculationSuccessful
    } // for
  }

  /**
   * Deposit all of the approve payout record. If a budget is insufficient for deposit of payout
   * Journal record is marked as pending.
   * 
   * @param payoutCalculationResults
   */
  public void depositApprovedPayouts( Set payoutCalculationResults ) throws ServiceErrorException
  {
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
      if ( payoutCalculationResult.isCalculationSuccessful() || payoutCalculationResult.getJournal() != null && payoutCalculationResult.getJournal().getPromotion().isRecognitionPromotion()
          && payoutCalculationResult.getJournal().getPromotion().getAwardType().isMerchandiseAwardType() )
      {
        Journal journal = payoutCalculationResult.getJournal();

        if ( journal.getJournalStatusType() != null && journal.getJournalStatusType().getCode().equals( JournalStatusType.APPROVE ) )
        {
          journal.setProcessStartDate( payoutCalculationResult.getPayoutCalculationAudit().getProcessStartDate() );
          journal.setProcessEndDate( payoutCalculationResult.getPayoutCalculationAudit().getProcessEndDate() );

          if ( payoutCalculationResult.getJournal().getPromotion().isRecognitionPromotion() && payoutCalculationResult.getJournal().getPromotion().getAwardType().isMerchandiseAwardType() ) // bug
                                                                                                                                                                                             // 73458
          {
            journalService.saveJournalEntry( journal );
          }
          else if ( payoutCalculationResult.isFileLoadDeposit() )// bug 73458
          {
            journalService.saveAndDepositJournalEntry( journal, false );
          }
          else
          {
            journalService.saveAndLaunchPointsDepositProcess( journal, true ); // bug 73458
          }
        } // if
      } // if isCalculationSuccessful
    } // for
  }

  /**
   * Deposit all of the posted payout record. If a budget is insufficient for deposit of payout
   * Journal record is marked as pending.
   * 
   * @param promotion
   * @param payoutCalculationResults
   * @param winnerType
   */
  public void depositSweepstakeApprovedPayouts( Promotion promotion, Set payoutCalculationResults, String winnerType ) throws ServiceErrorException
  {
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      if ( payoutCalculationResult.isCalculationSuccessful()
          || payoutCalculationResult.getJournal() != null && promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
      {
        Journal journal = payoutCalculationResult.getJournal();

        if ( journal.getJournalStatusType() != null && journal.getJournalStatusType().getCode().equals( JournalStatusType.APPROVE ) )
        {
          journal.setProcessStartDate( payoutCalculationResult.getPayoutCalculationAudit().getProcessStartDate() );
          journal.setProcessEndDate( payoutCalculationResult.getPayoutCalculationAudit().getProcessEndDate() );

          if ( promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() ) // bug
                                                                                                         // 73458
          {
            journalService.saveJournalEntry( journal );
          }
          else
          {
            journalService.saveAndLaunchPointsDepositProcess( journal, true ); // bug 73458
          }
        } // if
      } // if isCalculationSuccessful for recognition
    } // for
  }

  /**
   * @param gqPromo
   * @param pax
   * @return List of Partners belongs to an pax
   */
  public List getPaxPartners( GoalQuestPromotion gqPromo, Participant pax )
  {
    ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
    List partnersList = promotionService.getPartnersByPromotionAndParticipantWithAssociations( gqPromo.getId(), pax.getId(), paxAscReq );
    return partnersList;
  }

  private void generateGiftCodes( Set inputActivities, RecognitionFacts recFacts, RecognitionPromotion recPromo, Participant participant )
  {
    boolean verifyPass = false;

    if ( !recFacts.getClaim().getSubmitter().equals( participant ) )
    {
      verifyPass = true;
    }
    else if ( recPromo.isSelfRecognitionEnabled() )
    {
      // Check if the pax is in the recipient list if both giver and receiver is same.
      for ( Iterator<ClaimRecipient> claimRecipientsIter = ( (RecognitionClaim)recFacts.getClaim() ).getClaimRecipients().iterator(); claimRecipientsIter.hasNext(); )
      {
        ClaimRecipient recipient = claimRecipientsIter.next();
        if ( recipient.getRecipient().getId().equals( participant.getId() ) )
        {
          verifyPass = true;
          break;
        }
      }
    }

    if ( verifyPass )
    {
      // if it is Merch promo save the Gift Code Order
      if ( recPromo != null && recPromo.isAwardActive() && recPromo.getAwardType().isMerchandiseAwardType() )
      {
        RecognitionClaim claim = (RecognitionClaim)recFacts.getClaim();
        Set<ClaimRecipient> claimRecipients = claim.getClaimRecipients();
        List<DepositProcessBean> depositProcessMerchList = new ArrayList<DepositProcessBean>();

        for ( Iterator<ClaimRecipient> iter = claimRecipients.iterator(); iter.hasNext(); )
        {
          ClaimRecipient recipient = iter.next();

          PromoMerchProgramLevel level = recipient.getPromoMerchProgramLevel();
          String programId = null;

          // persist the merch code appropriately
          MerchOrder merchOrder = new MerchOrder();
          merchOrder.setClaim( claim );
          merchOrder.setParticipant( recipient.getRecipient() );
          merchOrder.setRedeemed( false );
          merchOrder.setPromoMerchProgramLevel( level );
          merchOrder.setBatchId( merchOrderDAO.getNextBatchId() );

          merchOrder = merchOrderDAO.saveMerchOrder( merchOrder );
          /** Bug # 36422 START**/
          AbstractRecognitionActivity activity = null;
          if ( inputActivities.size() == 1 )
          {
            activity = (AbstractRecognitionActivity)inputActivities.iterator().next();
          }
          else
          {
            for ( Iterator<AbstractRecognitionActivity> iResults = inputActivities.iterator(); iResults.hasNext(); )
            {
              activity = iResults.next();
              // look for Receiver.
              if ( !activity.isSubmitter() )
              {
                break;
              }
            }
          }
          /** Bug # 36422 END**/
          activity.setMerchOrder( merchOrder );
          activityDAO.saveActivity( activity );

          /* WIP# 25130 Start */
          if ( recPromo.isBillCodesActive() )
          {
            billingCodeStrategy.setMerchOrderBillingCodes( merchOrder, recPromo, inputActivities );
          }
          /* WIP# 25130 End */

          DepositProcessBean depositProcessBean = new DepositProcessBean();
          depositProcessBean.setMerchOrderId( merchOrder.getId() );
          depositProcessBean.setParticipantId( recipient.getId() );
          depositProcessBean.setProgramId( programId );
          depositProcessBean.setProductId( recipient.getProductId() );
          // depositProcessBean.setLevel( recipient.getPromoMerchProgramLevel() );
          depositProcessMerchList.add( depositProcessBean );
        }
        Process process = processService.createOrLoadSystemProcess( MerchOrderCreateProcess.PROCESS_NAME, MerchOrderCreateProcess.BEAN_NAME );

        LinkedHashMap parameterValueMap = new LinkedHashMap();
        parameterValueMap.put( "depositProcessMerchList", depositProcessMerchList );
        parameterValueMap.put( "promotionId", recPromo.getId() );
        parameterValueMap.put( "retry", "0" ); // bug 66870
        parameterValueMap.put( "isRetriable", String.valueOf( Boolean.TRUE ) ); // bug 66870

        ProcessSchedule processSchedule = new ProcessSchedule();
        processSchedule.setStartDate( new Date() );
        processSchedule.setTimeOfDayMillis( new Long( 0 ) );
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

        processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

      }
    }
  }

  /**
   * @param journalService
   */
  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  /**
   * @param activityDAO value for activityDAO property
   */
  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  /**
   * @param activityLoaderFactory value for activityLoaderFactory property
   */
  public void setActivityLoaderFactory( ActivityLoaderFactory activityLoaderFactory )
  {
    this.activityLoaderFactory = activityLoaderFactory;
  }

  /**
   * @param payoutStrategyFactory value for payoutStrategyFactory property
   */
  public void setPayoutStrategyFactory( PayoutStrategyFactory payoutStrategyFactory )
  {
    this.payoutStrategyFactory = payoutStrategyFactory;
  }

  public PayoutCalculationAuditDAO getPayoutCalculationAuditDAO()
  {
    return this.payoutCalculationAuditDAO;
  }

  /**
   * @param payoutCalculationAuditDAO value for payoutCalculationAuditDAO property
   */
  public void setPayoutCalculationAuditDAO( PayoutCalculationAuditDAO payoutCalculationAuditDAO )
  {
    this.payoutCalculationAuditDAO = payoutCalculationAuditDAO;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
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

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  public ChallengepointProgressService getChallengepointProgressService()
  {
    return challengepointProgressService;
  }

  public void setChallengepointProgressService( ChallengepointProgressService challengepointProgressService )
  {
    this.challengepointProgressService = challengepointProgressService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public GoalQuestService getGoalQuestService()
  {
    return goalQuestService;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }

  public PaxGoalService getPaxGoalService()
  {
    return paxGoalService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public GamificationService getGamificationService()
  {
    return gamificationService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public ChallengePointAchievementStrategyFactory getChallengePointAchievementStrategyFactory()
  {
    return challengePointAchievementStrategyFactory;
  }

  public void setChallengePointAchievementStrategyFactory( ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory )
  {
    this.challengePointAchievementStrategyFactory = challengePointAchievementStrategyFactory;
  }

  /* WIP# 25130 Start */
  public void setDepositBillingCodeStrategy( DepositBillingCodeStrategy billingCodeStrategy )
  {
    this.billingCodeStrategy = billingCodeStrategy;
  }
  /* WIP# 25130 End */

  private Message getMessage( String messageCode, Promotion promotion )
  {
    Message message = null;
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator<PromotionNotification> notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( messageCode ) )
          {
            message = messageService.getMessageById( messageId );
            break;
          }
        }
      }
    }
    return message;
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }
  
  protected Session getHibernateSession()
  {
    return HibernateSessionManager.getSession();
  }

}
