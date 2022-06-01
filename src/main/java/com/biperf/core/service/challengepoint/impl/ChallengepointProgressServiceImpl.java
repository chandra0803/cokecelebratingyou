
package com.biperf.core.service.challengepoint.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.dao.challengepoint.ChallengepointProgressDAO;
import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.challengepoint.ChallengepointAwardSummary;
import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.challengepoint.ChallengepointReviewProgress;
import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.ChallengePointManagerPayoutStrategyFactory;
import com.biperf.core.service.promotion.engine.PartnerCPStrategyFactory;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.ChallengepointPaxValueBean;

/**
 * ChallengepointProgressServiceImpl.
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
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ChallengepointProgressServiceImpl implements ChallengepointProgressService
{

  private ChallengePointService challengePointService;
  private ChallengepointProgressDAO challengepointProgressDAO;
  private ChallengepointAwardDAO challengepointAwardDAO;
  private ParticipantPartnerDAO participantPartnerDAO;
  private PromotionService promotionService;
  private PaxGoalService paxGoalService;
  private MailingService mailingService;
  private MessageService messageService;
  private ParticipantService participantService;
  private AudienceService audienceService;
  private GoalQuestService goalQuestService;

  private ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory;

  private ChallengePointManagerPayoutStrategyFactory challengePointManagerPayoutStrategyFactory;

  private PartnerCPStrategyFactory partnerCPStrategyFactory;

  private static final Log log = LogFactory.getLog( ChallengePointServiceImpl.class );

  public void deleteChallengepointProgress( ChallengepointProgress challengepointProgress )
  {

    challengepointProgressDAO.deleteChallengepointProgress( challengepointProgress );
  }

  public ChallengepointProgress getChallengepointProgressById( Long id )
  {
    return challengepointProgressDAO.getChallengepointProgressById( id );
  }

  public List<ChallengepointProgress> getChallengepointProgressByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    return challengepointProgressDAO.getChallengepointProgressByPromotionIdAndUserId( promotionId, userId, associationRequestCollection );
  }

  public ChallengepointProgress saveChallengepointProgress( ChallengepointProgress challengepointProgress )
  {
    ChallengePointPromotion promotion = challengepointProgress.getChallengePointPromotion();
    Participant pax = challengepointProgress.getParticipant();
    Long promotionId = promotion.getId();
    Long userId = pax.getId();
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotionId, userId );
    if ( paxGoal == null )
    {
      paxGoal = new PaxGoal();
      paxGoal.setGoalQuestPromotion( promotion );
      paxGoal.setParticipant( pax );
    }
    BigDecimal progressQuantity = challengepointProgress.getQuantity();
    AchievementPrecision achPrecision = AchievementPrecision.lookup( AchievementPrecision.ZERO );
    if ( promotion.getAchievementPrecision() != null )
    {
      achPrecision = promotion.getAchievementPrecision();
    }
    progressQuantity = progressQuantity.setScale( achPrecision.getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() );
    if ( progressQuantity == null )
    {
      progressQuantity = new BigDecimal( 0 );
    }
    BigDecimal currentValue = new BigDecimal( 0 );
    if ( paxGoal.getCurrentValue() == null )
    {
      currentValue = progressQuantity;
    }
    else
    {
      if ( challengepointProgress.getType().equals( "replace" ) )
      {
        currentValue = progressQuantity;
      }
      else
      {
        currentValue = progressQuantity.add( paxGoal.getCurrentValue() );
      }
    }
    currentValue = currentValue.setScale( achPrecision.getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() );
    paxGoal.setCurrentValue( currentValue );
    paxGoal.setSubmissionDate( challengepointProgress.getSubmissionDate() );
    paxGoalService.savePaxGoal( paxGoal );

    return challengepointProgressDAO.saveChallengepointProgress( challengepointProgress );
  }

  // commented as this method is no longer user. Email is send only by progress import process

  /*
   * public ChallengepointProgress saveChallengepointProgress( ChallengepointProgress
   * challengepointProgress, boolean emailFlag ) { ChallengepointProgress
   * cpProgress=saveChallengepointProgress( challengepointProgress ); if(emailFlag) {
   * ChallengePointPromotion challengepointPromotion =
   * challengepointProgress.getChallengePointPromotion(); try { if(
   * challengepointPromotion.isNotificationRequired(
   * PromotionEmailNotificationType.CHALLENGEPOINT_PROGRESS_UPDATED))
   * sendChallengepointEmailConfirmation(
   * challengepointPromotion,cpProgress.getParticipant(),MessageService
   * .CHALLENGEPOINT_PROGRESSUPDATED_MESSAGE_CM_ASSET_CODE); } catch (ServiceErrorException e) { //
   * TODO Auto-generated catch block e.printStackTrace(); } } return cpProgress; }
   */

  /*
   * private void sendChallengepointEmailConfirmation( ChallengePointPromotion promotion,
   * Participant participant, String messageCMCode ) throws ServiceErrorException { if ( promotion
   * != null && participant != null && promotion.isNotificationRequired(
   * PromotionEmailNotificationType.PROGRESS_UPDATED ) ) { PaxGoal paxGoal =
   * paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() ); if (
   * !paxGoal.getParticipant().isOwner() ) { ChallengepointPaxValueBean cpPaxValueBean =
   * getChallengePointService().populateChallengepointPaxValueBean( paxGoal, promotion,
   * (GoalLevel)paxGoal.getGoalLevel(), false ); // Bug # 21735 - % of Challenge Point values in the
   * "Progress Updated" mails shows the wrong // values cpPaxValueBean.setPercentAchieved(
   * getChallengePointService().computePercent( cpPaxValueBean.getPromotion(),
   * cpPaxValueBean.getAmountToAchieve(), paxGoal.getCurrentValue() ) ); // Bug Fix 19164. Avoid
   * notifying Inactive Pax. if ( participant.isActive().booleanValue() ) { MailingRecipient
   * mailingRecipient = null; mailingRecipient =
   * mailingService.buildMailingRecipientForChallengepointEmail( promotion, participant,
   * cpPaxValueBean, null ); Message message = messageService.getMessageByCMAssetCode( messageCMCode
   * ); // Create mailing object Mailing mailing = new Mailing(); mailing.setMessage( message );
   * mailing.addMailingRecipient( mailingRecipient ); mailing.setDeliveryDate( new Timestamp(
   * DateUtils.getCurrentDateAsLong() ) ); mailing.setSender( "Challengepoint Promotion" );
   * mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) ); mailing.setGuid(
   * GuidUtils.generateGuid() ); mailingService.submitMailing( mailing, null ); } } } }
   */

  public void setChallengepointProgressDAO( ChallengepointProgressDAO challengepointProgressDAO )
  {
    this.challengepointProgressDAO = challengepointProgressDAO;
  }

  /**
   * Method returns the progress collective data uploaded so far for an pax
   * @param promotionId
   * @param userId
   * @return mAP
  * @throws ServiceErrorException 
   */
  public Map<String, Object> getChallengepointProgressByPromotionIdAndUserId( Long promotionId, Long userId ) throws ServiceErrorException
  {
    if ( promotionId == null || userId == null )
    {
      final String errorMessage = "promotion id or user id cannot be null for" + " requesting GoalQuest Progress information";
      log.error( errorMessage );
      throw new BeaconRuntimeException( errorMessage );
    }
    Map<String, Object> cpProgressMap = new HashMap<String, Object>();
    List<ChallengepointReviewProgress> cpPaxProgressList = new ArrayList<ChallengepointReviewProgress>();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ChallengepointProgressAssociationRequest( ChallengepointProgressAssociationRequest.ALL ) );
    List<ChallengepointProgress> cpProgressList = challengepointProgressDAO.getChallengepointProgressByPromotionIdAndUserId( promotionId, userId, associationRequestCollection );
    PaxGoal paxGoal = getChallengePointService().getPaxChallengePoint( promotionId, userId );
    ChallengePointPromotion challengePointPromotion = null;
    BigDecimal amountToAchieve = null;
    if ( paxGoal != null )
    {
      try
      {
        ChallengepointPaxValueBean cpPaxValueBean = getChallengePointService()
            .populateChallengepointPaxValueBean( paxGoal, (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(), (GoalLevel)paxGoal.getGoalLevel() );
        cpProgressMap.put( "paxLevel", cpPaxValueBean );
        if ( cpPaxValueBean != null )
        {
          amountToAchieve = cpPaxValueBean.getAmountToAchieve();
        }
        if ( null != paxGoal.getGoalLevel() )
        {
          challengePointPromotion = (ChallengePointPromotion)paxGoal.getGoalLevel().getPromotion();
        }
      }
      catch( ServiceErrorException se )
      {
        throw new BeaconRuntimeException( se.getMessage(), se );
      }
    }
    else
    {
      challengePointPromotion = (ChallengePointPromotion)promotionService.getPromotionById( promotionId );
    }
    if ( cpProgressList != null && cpProgressList.size() > 0 )
    {
      BigDecimal cumulativeTotal = new BigDecimal( "0" );
      for ( Iterator cpProgressListIter = cpProgressList.iterator(); cpProgressListIter.hasNext(); )
      {
        ChallengepointProgress challengepointProgress = (ChallengepointProgress)cpProgressListIter.next();
        ChallengepointReviewProgress cpReviewProgress = new ChallengepointReviewProgress();
        cpReviewProgress.setSubmissionDate( challengepointProgress.getSubmissionDate() );
        cpReviewProgress.setAmountToAchieve( amountToAchieve );
        cpReviewProgress.setRoundingType( ( challengePointPromotion != null ) ? challengePointPromotion.getRoundingMethod().getBigDecimalRoundingMode() : BigDecimal.ROUND_HALF_UP );
        cpReviewProgress.setAchievementPrecision( ( challengePointPromotion != null ) ? challengePointPromotion.getAchievementPrecision().getPrecision() : 0 );
        if ( challengepointProgress.getQuantity() != null )
        {
          if ( challengepointProgress.getType().equals( "increment" ) )
          {
            cpReviewProgress.setQuantity( challengepointProgress.getQuantity() );
            cumulativeTotal = cumulativeTotal.add( challengepointProgress.getQuantity() );
          }
          else if ( challengepointProgress.getType().equals( "replace" ) )
          {
            cpReviewProgress.setQuantity( challengepointProgress.getQuantity().subtract( cumulativeTotal ) );
            cumulativeTotal = challengepointProgress.getQuantity();
          }
        }
        cpReviewProgress.setLoadType( challengepointProgress.getType() );
        cpReviewProgress.setCumulativeTotal( cumulativeTotal );

        cpPaxProgressList.add( cpReviewProgress );
      }
      cpProgressMap.put( "cumulativeTotal", cumulativeTotal );
    }
    // if the arraylist is empty add
    if ( cpPaxProgressList.size() == 0 )
    {
      BigDecimal zero = new BigDecimal( "0" );
      ChallengepointReviewProgress cpReviewProgress = new ChallengepointReviewProgress();
      cpReviewProgress.setAmountToAchieve( amountToAchieve );
      cpReviewProgress.setQuantity( zero );
      cpReviewProgress.setCumulativeTotal( zero );
      cpPaxProgressList.add( cpReviewProgress );
    }
    if ( cpPaxProgressList != null )
    {
      Collections.reverse( cpPaxProgressList );
    }
    cpProgressMap.put( "challengepointProgressList", cpPaxProgressList );

    return cpProgressMap;
  }

  public Date getProgressLastSubmissionDate( Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ChallengepointProgressAssociationRequest( ChallengepointProgressAssociationRequest.ACTIVITY ) );
    List<ChallengepointProgress> cpProgressList = challengepointProgressDAO.getChallengepointProgressByPromotionId( promotionId, associationRequestCollection );
    if ( cpProgressList != null && cpProgressList.size() > 0 )
    {
      ChallengepointProgress cpProgress = (ChallengepointProgress)cpProgressList.get( 0 );
      return cpProgress.getSubmissionDate();

    }

    return null;
  }

  /**
   * Method returns the progress collective data uploaded so far for an pax
   * @param promotionId
   * @param userId
   * @return mAP
  * @throws ServiceErrorException 
   */
  public ChallengepointPaxValueBean getChallengepointProgressSummary( Long promotionId, Long userId ) throws ServiceErrorException
  {
    try
    {
      PaxGoal paxGoal = getChallengePointService().getPaxChallengePoint( promotionId, userId );
      ChallengepointPaxValueBean cpPaxValueBean = null;

      if ( paxGoal != null )
      {
        cpPaxValueBean = getChallengePointService().populateChallengepointPaxValueBean( paxGoal, (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(), (GoalLevel)paxGoal.getGoalLevel() );
        int achievedPercent = getChallengePointService().computePercent( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(), cpPaxValueBean.getAmountToAchieve(), paxGoal.getCurrentValue() )
            .intValue();
        BigDecimal percentAchieved = new BigDecimal( achievedPercent );
        cpPaxValueBean.setPercentAchieved( percentAchieved );
      }
      else
      {
        ChallengePointPromotion cpPromotion = getChallengePointService().getPromotion( promotionId );
        cpPaxValueBean = getChallengePointService().populateChallengepointPaxValueBean( null, cpPromotion, null, false );
      }
      ChallengepointAward cpAward = challengepointAwardDAO.getChallengepointAwardByPromotionIdAndUserId( promotionId, userId );
      cpPaxValueBean.setChallengepointAward( cpAward );
      ChallengepointAward award = challengepointAwardDAO.getAwardByPromotionIdAndUserId( promotionId, userId );
      List<ChallengepointAward> allAwardList = challengepointAwardDAO.getAllChallengepointAwardsByPromotionIdAndUserId( promotionId, userId );
      Long cpAwardEarned = new Long( 0 );
      Long totalAwardEarned = new Long( 0 );
      Long totalIssued = new Long( 0 );
      Long notDeposited = new Long( 0 );

      if ( cpAward != null )
      {
        cpAwardEarned = cpAward.getAwardEarned();
        if ( cpAwardEarned == null )
        {
          cpAwardEarned = new Long( 0 );
        }
      }

      cpPaxValueBean.setChallengepointAwardEarned( cpAwardEarned );
      Long totalBaseAwardEarned = cpPaxValueBean.getTotalBasicAwardEarned();
      if ( award != null && award.getAwardType() != null && award.getAwardType().equals( "manageroverride" ) )
      {
        totalBaseAwardEarned = award.getAwardEarned();
      }
      if ( totalBaseAwardEarned == null )
      {
        totalBaseAwardEarned = new Long( 0 );
      }
      totalAwardEarned = new Long( totalBaseAwardEarned.longValue() + cpAwardEarned.longValue() );
      if ( totalAwardEarned == null )
      {
        totalAwardEarned = new Long( 0 );
      }
      if ( award != null )
      {
        if ( award.getTotalAwardIssued() != null )
        {
          totalIssued = award.getTotalAwardIssued() - cpAwardEarned.longValue();
        }
        // for some reason for manager override total ward is not getting populated
        // but ideally totally issued and issued are same for mangers
        if ( award.getAwardType() != null && award.getAwardType().equals( "manageroverride" ) )
        {
          totalIssued = award.getAwardIssued();
        }
        if ( totalIssued == null )
        {
          totalIssued = new Long( 0 );
        }
        long difference = 0;
        if ( totalAwardEarned != null && totalIssued != null )
        {
          difference = totalAwardEarned.longValue() - totalIssued.longValue();
        }
        notDeposited = new Long( difference );
        if ( notDeposited == null )
        {
          notDeposited = new Long( 0 );
        }
      }
      else
      { // BugFix 21592,Award Earned but not deposited should be TotalAwardEarned if no award found
        notDeposited = new Long( totalAwardEarned.longValue() );
      }
      cpPaxValueBean.setTotalAwardEarned( totalAwardEarned );
      cpPaxValueBean.setTotalAwardDeposited( totalIssued );
      cpPaxValueBean.setTotalAwardEarnedNotDeposited( notDeposited );
      cpPaxValueBean.setProgressSubmissionDate( getProgressLastSubmissionDate( promotionId ) );
      cpPaxValueBean.setAwards( allAwardList );
      return cpPaxValueBean;

    }
    catch( ServiceErrorException se )
    {
      throw new BeaconRuntimeException( se.getMessage(), se );
    }

  }

  /**
   * @param userId
   * @return List of all ChallengePointPromotionObjects with PaxValueBeans present
   *         for selected promotions
   * @throws ServiceErrorException 
   */
  public List<ChallengepointPaxValueBean> getAllLiveChallengePointPromotionsWithProgressByUserId( Long userId ) throws ServiceErrorException
  {
    // note this method does not query on user id, it is a redundant parameter
    List<ChallengePointPromotion> cpPromotions = challengePointService.getAllLiveChallengePointPromotionsByUserId( userId );
    List<ChallengepointPaxValueBean> paxValueBeanList = new ArrayList<ChallengepointPaxValueBean>();

    for ( Iterator iter = cpPromotions.iterator(); iter.hasNext(); )
    {
      ChallengePointPromotion cpPromotion = (ChallengePointPromotion)iter.next();
      if ( cpPromotion != null )
      {
        ChallengepointPaxValueBean paxValueBean = getChallengepointProgressSummary( cpPromotion.getId(), userId );
        paxValueBeanList.add( paxValueBean );
      }
    }

    return paxValueBeanList;
  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  /*
   * private Map getEmptyChallengepointAwardSummaryMap( ChallengePointPromotion promotion ) { Map
   * summaryMap = new TreeMap(); Set<AbstractGoalLevel> cpLevels = promotion.getGoalLevels(); if (
   * cpLevels != null ) { for ( Iterator<AbstractGoalLevel> iter = cpLevels.iterator();
   * iter.hasNext(); ) { GoalLevel goalLevel = (GoalLevel)iter.next(); ChallengepointAwardSummary
   * challengepointAwardSummary = new ChallengepointAwardSummary();
   * challengepointAwardSummary.setGoalLevel( goalLevel ); challengepointAwardSummary.setTotalAward(
   * new BigDecimal( "0" ) ); summaryMap.put( goalLevel.getId(), challengepointAwardSummary ); } }
   * return summaryMap; }
   */

  /**
   * Get the map containing the summary lists for participant and manager
   * override levels by promotion id.
   * 
   * @param promotionId
   * @return map
   */
  public PendingChallengepointAwardSummary getAwardSummaryByLevels( Long promotionId ) throws ServiceErrorException
  {
    PendingChallengepointAwardSummary pendingChallengepointAwardSummary = new PendingChallengepointAwardSummary();

    List<ChallengepointAwardSummary> participantChallengepointAwardSummaryList = new ArrayList<ChallengepointAwardSummary>();
    List<ChallengepointPaxAwardValueBean> participantChallengepointAwardList = new ArrayList<ChallengepointPaxAwardValueBean>();
    List<ChallengepointPaxAwardValueBean> partnerChallengepointSummaryList = new ArrayList<ChallengepointPaxAwardValueBean>();
    List<ChallengePointCalculationResult> cpCalcResult = new ArrayList<ChallengePointCalculationResult>();
    List<ChallengepointAwardSummary> partnerCPAwardSummaryList = new ArrayList<ChallengepointAwardSummary>();
    ChallengepointAwardSummary participantChallengepointAwardTotal = new ChallengepointAwardSummary();

    // Get Promotion by ID
    ChallengePointPromotion promotion = (ChallengePointPromotion)getPromotionService().getPromotionById( promotionId );

    Map<Long, ChallengepointAwardSummary> cpAwardSummaryPartnerMap = getEmptyCPAwardSummaryMap( promotion );
    Map<Long, ChallengePointCalculationResult> cpResultsByPax = new HashMap<Long, ChallengePointCalculationResult>();
    Map<Long, Boolean> payoutEligibilityByPax = new HashMap<Long, Boolean>();
    List<Long> promotionEligiblePax = goalQuestService.getEligibleParticipantsForPromotion( promotion );

    /* Map of Manager CP for managers - key is node */
    Map nodeToPax = new HashMap();
    /* Get all pax who have selected the challengepoint */
    List<PaxGoal> paxChallengepointLevelList = getPaxGoalService().getPaxGoalByPromotionId( promotionId );

    if ( paxChallengepointLevelList != null && paxChallengepointLevelList.size() > 0 )
    {
      /* Map of Challengepoint resultCalculationResults by level id */
      Map cpResultsByLevel = new HashMap();

      // hydrate notifications as it will be used later.
      PromotionAssociationRequest promotionAssociationRequest = new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS );
      promotionAssociationRequest.execute( promotion );

      Iterator iter = paxChallengepointLevelList.iterator();
      while ( iter.hasNext() )
      {
        PaxGoal paxGoal = (PaxGoal)iter.next();

        if ( !isPaxEligibleForPayout( payoutEligibilityByPax, paxGoal.getParticipant(), promotionEligiblePax ) )
        {
          iter.remove();
          continue;
        }

        // Check if Participant is in eligible audience
        if ( !isPaxEligibleForPayout( paxGoal.getParticipant(), promotion ) )
        {
          iter.remove();
          continue;
        }

        if ( paxGoal.getGoalLevel() != null )
        {
          ChallengePointCalculationResult challengePointCalculationResult = addOrIncrementTotals( cpResultsByLevel, paxGoal.getGoalLevel().getId(), paxGoal, participantChallengepointAwardList );

          if ( challengePointCalculationResult != null )
          {
            challengePointCalculationResult.setReciever( paxGoal.getParticipant() );
            cpCalcResult.add( challengePointCalculationResult );
            cpResultsByPax.put( paxGoal.getParticipant().getId(), challengePointCalculationResult );
          }

        }
      }

      for ( Iterator iterGoalLevelIter = promotion.getGoalLevels().iterator(); iterGoalLevelIter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iterGoalLevelIter.next();
        for ( Iterator iterPaxByLevelIter = cpResultsByLevel.keySet().iterator(); iterPaxByLevelIter.hasNext(); )
        {
          Long levelId = (Long)iterPaxByLevelIter.next();
          ChallengepointAwardSummary challengepointAwardSummary = (ChallengepointAwardSummary)cpResultsByLevel.get( levelId );
          ChallengepointAwardSummary cpAward = new ChallengepointAwardSummary();
          cpAward.setGoalLevel( goalLevel );
          BigDecimal basicDeposited = new BigDecimal( 0 );
          BigDecimal basicEarned = new BigDecimal( 0 );
          BigDecimal basicPending = new BigDecimal( 0 );
          int totalAchieved = 0;
          BigDecimal totalAchievedAward = new BigDecimal( 0 );
          BigDecimal totalAward = new BigDecimal( 0 );
          Long totalSelected = new Long( 0 );
          if ( goalLevel.getId().equals( levelId ) )
          {
            cpAward.setBasicDeposited( challengepointAwardSummary.getBasicDeposited() );
            cpAward.setBasicEarned( challengepointAwardSummary.getBasicEarned() );
            cpAward.setBasicPending( challengepointAwardSummary.getBasicPending() );
            cpAward.setTotalAchieved( challengepointAwardSummary.getTotalAchieved() );
            cpAward.setTotalAchievedAward( challengepointAwardSummary.getTotalAchievedAward() );
            cpAward.setTotalAward( challengepointAwardSummary.getTotalAward() );
            cpAward.setTotalSelected( challengepointAwardSummary.getTotalSelected() );
            participantChallengepointAwardSummaryList.add( cpAward );
            participantChallengepointAwardTotal.incrementBasicDeposited( challengepointAwardSummary.getBasicDeposited() );
            participantChallengepointAwardTotal.incrementBasicEarned( challengepointAwardSummary.getBasicEarned() );
            participantChallengepointAwardTotal.incrementBasicPending( challengepointAwardSummary.getBasicPending() );
            participantChallengepointAwardTotal.incrementTotalAchieved( challengepointAwardSummary.getTotalAchieved() );
            participantChallengepointAwardTotal.incrementTotalAchievedAward( challengepointAwardSummary.getTotalAchievedAward() );

            participantChallengepointAwardTotal.incrementTotalAward( challengepointAwardSummary.getTotalAward() );
            participantChallengepointAwardTotal.incrementTotalSelected( challengepointAwardSummary.getTotalSelected().intValue() );
            break;
          }
          else
          {
            if ( !iterPaxByLevelIter.hasNext() )
            {
              cpAward.setBasicDeposited( basicDeposited );
              cpAward.setBasicEarned( basicEarned );
              cpAward.setBasicPending( basicPending );
              cpAward.setTotalAchieved( totalAchieved );
              cpAward.setTotalAchievedAward( totalAchievedAward );
              cpAward.setTotalAward( totalAward );
              cpAward.setTotalSelected( totalSelected );
              participantChallengepointAwardSummaryList.add( cpAward );
              break;
            }
          }
        }

      }
      participantChallengepointAwardTotal.setParticipantTotals( true );
      participantChallengepointAwardSummaryList.add( participantChallengepointAwardTotal );
    }

    // Calculate Manager Override if calculation period is over
    if ( promotion.isAfterFinalProcessDate() )
    {
      for ( Iterator paxIter = participantChallengepointAwardList.iterator(); paxIter.hasNext(); )
      {

        ChallengepointPaxAwardValueBean challengepointPaxAwardValueBean = (ChallengepointPaxAwardValueBean)paxIter.next();
        List<Node> ownedNodes = challengepointPaxAwardValueBean.getParticipant().getUserNodesWithinPrimaryHierarchyAsNodes();
        for ( Iterator ownedNodeIter = ownedNodes.iterator(); ownedNodeIter.hasNext(); )
        {
          Node node = (Node)ownedNodeIter.next();
          // Commenting out this code to issue manager override for level 2 node owner even if level
          // 1 node owner is null
          /*
           * if ( node.getNodeOwner() != null ) {
           */
          List paxList = (List)nodeToPax.get( node );
          if ( paxList == null )
          {
            paxList = new ArrayList();
          }
          paxList.add( challengepointPaxAwardValueBean );
          nodeToPax.put( node, paxList );
          // }
        }

      }

      if ( !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
      {
        PendingChallengepointAwardSummary managerAwardSummary = challengePointManagerPayoutStrategyFactory.getChallengePointManagerPayoutStrategy( promotion.getOverrideStructure().getCode() )
            .processChallengePoint( promotion, nodeToPax );
        if ( managerAwardSummary != null )
        {
          pendingChallengepointAwardSummary.setManagerChallengepointAwardSummary( managerAwardSummary.getManagerChallengepointAwardSummary() );
          pendingChallengepointAwardSummary.setManagerOverrideResults( managerAwardSummary.getManagerOverrideResults() );
        }
      }

      if ( promotion.getPartnerAudienceType() != null )
      {
        List<ChallengePointCalculationResult> partnerCPResults = computePartnerGQPayout( promotion, cpResultsByPax, cpAwardSummaryPartnerMap, payoutEligibilityByPax );
        if ( partnerCPResults != null && partnerCPResults.size() > 0 )
        {
          partnerCPAwardSummaryList = partnerCPStrategyFactory.getPartnerGoalStrategy( promotion.getPartnerEarnings().getCode() ).summarizeResults( cpAwardSummaryPartnerMap );

          for ( Iterator prtnIter = partnerCPResults.iterator(); prtnIter.hasNext(); )
          {
            ChallengePointCalculationResult awardSummary = (ChallengePointCalculationResult)prtnIter.next();
            ChallengepointPaxAwardValueBean cpBean = new ChallengepointPaxAwardValueBean();
            cpBean.setAmountToAchieve( awardSummary.getAmountToAchieve() );
            cpBean.setCalculatedAchievement( awardSummary.getCalculatedAchievement() );
            cpBean.setBaseQuantity( awardSummary.getBaseObjective() );
            cpBean.setChallengePointPromotion( promotion );
            cpBean.setParticipant( (Participant)awardSummary.getReciever() );
            cpBean.setGoalLevel( awardSummary.getGoalLevel() );
            cpBean.setAchieved( awardSummary.isAchieved() );
            cpBean.setPartner( true );
            cpBean.setGoalSelecter( awardSummary.getPartnerToParticipant() );
            cpBean.setAwardType( "partner" );

            if ( !prtnIter.hasNext() )
            {
              for ( Iterator cpAwardSummary = partnerCPAwardSummaryList.iterator(); cpAwardSummary.hasNext(); )
              {
                ChallengepointAwardSummary cpSummary = (ChallengepointAwardSummary)cpAwardSummary.next();
                cpBean.setTotalAchieved( cpSummary.getTotalAchieved() );
                cpBean.setTotalAward( cpSummary.getTotalAward() );
                cpBean.setTotalSelected( cpSummary.getTotalSelected() );
                cpBean.setPartnerTotals( cpSummary.isPartnerTotals() );
              }
            }
            partnerChallengepointSummaryList.add( cpBean );
            pendingChallengepointAwardSummary.setPartnerCPResults( partnerChallengepointSummaryList );
          }
        }
        pendingChallengepointAwardSummary.setPartnerCPAwardSummaryList( partnerCPAwardSummaryList );
      }
    }
    pendingChallengepointAwardSummary.setParticipantChallegenpointAwardSummaryList( participantChallengepointAwardSummaryList );
    pendingChallengepointAwardSummary.setParticipantChallengepointResults( participantChallengepointAwardList );
    pendingChallengepointAwardSummary.setPromotion( promotion );
    pendingChallengepointAwardSummary.setChallengepointAwardSummaryTotal( participantChallengepointAwardTotal );

    return pendingChallengepointAwardSummary;
  }

  private ChallengePointCalculationResult addOrIncrementTotals( Map map, Long levelId, PaxGoal paxGoal, List<ChallengepointPaxAwardValueBean> participantChallengepointAwardList )
      throws ServiceErrorException
  {
    ChallengepointPaxAwardValueBean basicAward = new ChallengepointPaxAwardValueBean();
    ChallengepointPaxAwardValueBean challengepointAward = null;
    basicAward.setParticipant( paxGoal.getParticipant() );
    basicAward.setChallengePointPromotion( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() );
    basicAward.setResult( paxGoal.getCurrentValue() );
    basicAward.setBaseQuantity( paxGoal.getBaseQuantity() );
    basicAward.setAwardType( "basic" );
    basicAward.setAwardIssued( new Long( 0 ) );

    ChallengePointCalculationResult challengePointCalculationResult;
    challengePointCalculationResult = challengePointAchievementStrategyFactory
        .getChallengePointAchievementStrategy( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getAchievementRule().getCode() ).processChallengePoint( paxGoal );

    ChallengepointAwardSummary levelAwardSummary = (ChallengepointAwardSummary)map.get( levelId );
    if ( levelAwardSummary == null )
    {
      levelAwardSummary = new ChallengepointAwardSummary();
      levelAwardSummary.setParticipantTotals( false );
      levelAwardSummary.setBasicDeposited( new BigDecimal( 0 ) );
      levelAwardSummary.setGoalLevel( (GoalLevel)paxGoal.getGoalLevel() );
      map.put( levelId, levelAwardSummary );
    }
    levelAwardSummary.incrementTotalSelected();
    if ( challengePointCalculationResult.isAchieved() )
    {
      levelAwardSummary.incrementTotalAchieved();
      if ( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getChallengePointAwardType().isPoints() )
      {
        levelAwardSummary.incrementTotalAward( challengePointCalculationResult.getCalculatedAchievement() );
        levelAwardSummary.incrementTotalAchievedAward( challengePointCalculationResult.getCalculatedAchievement() );
      }
      else if ( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getChallengePointAwardType().isMerchTravel() )
      {
        levelAwardSummary.setTotalAchievedAward( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward() );
      }
      challengepointAward = new ChallengepointPaxAwardValueBean();
      challengepointAward.setParticipant( paxGoal.getParticipant() );
      challengepointAward.setChallengePointPromotion( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() );
      challengepointAward.setResult( paxGoal.getCurrentValue() );
      challengepointAward.setAwardType( "challengepoint" );
      if ( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getChallengePointAwardType().isPoints() )
      {
        challengepointAward.setAwardEarned( new Long( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().longValue() ) );
        challengepointAward.setAwardIssued( new Long( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().longValue() ) );
        challengepointAward.setTotalAwardIssued( new Long( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().longValue() ) );
      }
      else if ( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getChallengePointAwardType().isMerchTravel() )
      {
        challengepointAward.setAwardEarned( new Long( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().longValue() ) );
        challengepointAward.setAwardIssued( new Long( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().longValue() ) );
        challengepointAward.setTotalAwardIssued( new Long( ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().longValue() ) );
      }
      challengepointAward.setAchieved( true );
      challengepointAward.setBaseQuantity( paxGoal.getBaseQuantity() );
      challengepointAward.setCalculatedAchievement( challengePointCalculationResult.getCalculatedAchievement() );
      challengepointAward.setPaxGoal( paxGoal );
      participantChallengepointAwardList.add( challengepointAward );

    }
    else
    {
      challengepointAward = new ChallengepointPaxAwardValueBean();
      challengepointAward.setParticipant( paxGoal.getParticipant() );
      challengepointAward.setChallengePointPromotion( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() );
      challengepointAward.setResult( paxGoal.getCurrentValue() );
      participantChallengepointAwardList.add( challengepointAward );
      challengepointAward.setBaseQuantity( paxGoal.getBaseQuantity() );
      challengepointAward.setAchieved( false );
      challengepointAward.setPaxGoal( paxGoal );
      challengepointAward.setAwardType( "challengepoint" );
    }
    challengepointAward.setAmountToAchieve( challengePointCalculationResult.getAmountToAchieve() );
    ChallengepointAward recentChallengepointAward = challengepointAwardDAO.getBasicAwardByPromotionIdAndUserId( paxGoal.getGoalQuestPromotion().getId(), paxGoal.getParticipant().getId() );
    Long sumBasicAward = challengepointAwardDAO.getSumBasicAwardByPromotionIdAndUserId( paxGoal.getGoalQuestPromotion().getId(), paxGoal.getParticipant().getId() );
    if ( recentChallengepointAward != null )
    {
      levelAwardSummary.incrementBasicDeposited( sumBasicAward );
      if ( recentChallengepointAward.getTotalAwardIssued() == null )
      {
        recentChallengepointAward.setTotalAwardIssued( new Long( 0 ) );
      }
      if ( challengepointAward != null && challengepointAward.getAwardIssued() != null )
      {
        challengepointAward.setTotalAwardIssued( new Long( recentChallengepointAward.getTotalAwardIssued().longValue() + challengepointAward.getAwardIssued().longValue() ) );
      }
      // recentChallengepointAward
    }
    ChallengepointPaxValueBean challengepointPaxValueBean = challengePointService
        .populateChallengepointPaxValueBean( paxGoal, (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(), (GoalLevel)paxGoal.getGoalLevel() );
    if ( challengepointPaxValueBean != null )
    {
      levelAwardSummary.incrementBasicEarned( challengepointPaxValueBean.getTotalBasicAwardEarned() );
      levelAwardSummary.incrementTotalAward( challengepointPaxValueBean.getTotalBasicAwardEarned() );
      if ( recentChallengepointAward != null && recentChallengepointAward.getTotalAwardIssued() != null && challengepointPaxValueBean.getTotalBasicAwardEarned() != null )
      {
        // bug fix made to make sure not paying if already over paid and only pay incremental
        // amounts
        if ( challengepointPaxValueBean.getTotalBasicAwardEarned().longValue() > recentChallengepointAward.getTotalAwardIssued().longValue() )
        {
          levelAwardSummary.incrementBasicPending( new Long( challengepointPaxValueBean.getTotalBasicAwardEarned().longValue() - recentChallengepointAward.getTotalAwardIssued().longValue() ) );
        }
        if ( levelAwardSummary.getBasicPending() != null && levelAwardSummary.getBasicPending().longValue() < 0 )
        {
          levelAwardSummary.setBasicPending( new BigDecimal( 0 ) );
        }
      }
      else
      {
        levelAwardSummary.incrementBasicPending( challengepointPaxValueBean.getTotalBasicAwardEarned() );
      }
      if ( challengepointAward != null && challengepointAward.getAwardIssued() != null )
      {
        long totalBasicAwardEarned = 0;
        if ( challengepointPaxValueBean.getTotalBasicAwardEarned() != null )
        {
          totalBasicAwardEarned = challengepointPaxValueBean.getTotalBasicAwardEarned().longValue();
        }
        basicAward.setTotalAwardIssued( new Long( totalBasicAwardEarned + challengepointAward.getAwardIssued().longValue() ) );
        basicAward.setTotalBasicAwardIssued( new Long( totalBasicAwardEarned ) );
      }
      else
      {
        basicAward.setTotalAwardIssued( challengepointPaxValueBean.getTotalBasicAwardEarned() );
        basicAward.setTotalBasicAwardIssued( challengepointPaxValueBean.getTotalBasicAwardEarned() );
      }
      basicAward.setPaxGoal( paxGoal );
      basicAward.setAwardEarned( challengepointPaxValueBean.getTotalBasicAwardEarned() );
      long recentTotalAwardIssued = 0;
      if ( recentChallengepointAward != null && recentChallengepointAward.getTotalAwardIssued() != null )
      {
        recentTotalAwardIssued = recentChallengepointAward.getTotalAwardIssued().longValue();
      }
      if ( challengepointPaxValueBean != null && challengepointPaxValueBean.getTotalBasicAwardEarned() != null )
      {
        basicAward.setAwardIssued( new Long( challengepointPaxValueBean.getTotalBasicAwardEarned().longValue() - recentTotalAwardIssued ) );
        if ( basicAward.getAwardIssued().longValue() < 0 )
        {
          basicAward.setAwardIssued( new Long( 0 ) );
        }
      }
    }

    // if(basicAward.getAwardIssued() != null && basicAward.getAwardIssued().longValue() > 0)
    // {
    participantChallengepointAwardList.add( basicAward );
    // }

    return challengePointCalculationResult;
  }

  private boolean isPaxEligibleForPayout( Participant participant, Promotion promotion )
  {
    // PAX should be active and belong to PAX Audience
    Boolean eligible = participant.isActive().booleanValue() && promotionService.isParticipantInAudience( participant, promotion ) ? Boolean.TRUE : Boolean.FALSE;
    return eligible.booleanValue();
  }

  public ChallengepointAwardDAO getChallengepointAwardDAO()
  {
    return challengepointAwardDAO;
  }

  public void setChallengepointAwardDAO( ChallengepointAwardDAO challengepointAwardDAO )
  {
    this.challengepointAwardDAO = challengepointAwardDAO;
  }

  public ChallengepointProgressDAO getChallengepointProgressDAO()
  {
    return challengepointProgressDAO;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public PaxGoalService getPaxGoalService()
  {
    return paxGoalService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public ChallengePointAchievementStrategyFactory getChallengePointAchievementStrategyFactory()
  {
    return challengePointAchievementStrategyFactory;
  }

  public void setChallengePointAchievementStrategyFactory( ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory )
  {
    this.challengePointAchievementStrategyFactory = challengePointAchievementStrategyFactory;
  }

  public ChallengePointManagerPayoutStrategyFactory getChallengePointManagerPayoutStrategyFactory()
  {
    return challengePointManagerPayoutStrategyFactory;
  }

  public void setChallengePointManagerPayoutStrategyFactory( ChallengePointManagerPayoutStrategyFactory challengePointManagerPayoutStrategyFactory )
  {
    this.challengePointManagerPayoutStrategyFactory = challengePointManagerPayoutStrategyFactory;
  }

  public PartnerCPStrategyFactory getPartnerCPStrategyFactory()
  {
    return partnerCPStrategyFactory;
  }

  public void setPartnerCPStrategyFactory( PartnerCPStrategyFactory partnerCPStrategyFactory )
  {
    this.partnerCPStrategyFactory = partnerCPStrategyFactory;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  /**
   *  isParticipantPayoutComplete()
   *  date : May 23 2011
   */

  public boolean isParticipantPayoutComplete( Long userId, Long promotionId )
  {
    return challengepointAwardDAO.isParticipantPayoutComplete( userId, promotionId );
  }

  private List<ChallengePointCalculationResult> computePartnerGQPayout( ChallengePointPromotion promotion,
                                                                        Map<Long, ChallengePointCalculationResult> cpResultsByPax,
                                                                        Map<Long, ChallengepointAwardSummary> cpAwardSummaryPartnerMap,
                                                                        Map<Long, Boolean> payoutEligibilityByPax )
  {
    List<ChallengePointCalculationResult> partnerPayouts = new ArrayList<ChallengePointCalculationResult>();

    Map<Long, List<ChallengePointCalculationResult>> paxCPResultsByPartner = new HashMap<Long, List<ChallengePointCalculationResult>>();
    List<ParticipantPartner> paxPartners = getEligiblePaxPartners( promotion, paxCPResultsByPartner, cpResultsByPax, payoutEligibilityByPax );

    Map<Long, PromotionPartnerPayout> partnerGoalLevelsBySequence = new HashMap<Long, PromotionPartnerPayout>();
    for ( Iterator<PromotionPartnerPayout> iter = promotion.getPartnerGoalLevels().iterator(); iter.hasNext(); )
    {
      PromotionPartnerPayout partnerGoal = (PromotionPartnerPayout)iter.next();
      partnerGoalLevelsBySequence.put( new Long( partnerGoal.getSequenceNumber() ), partnerGoal );
    }

    for ( Iterator<ParticipantPartner> iter = paxPartners.iterator(); iter.hasNext(); )
    {
      ParticipantPartner paxPartner = iter.next();
      if ( promotion.getPartnerEarnings() != null )
      {
        ChallengePointCalculationResult cpCalculationResult = partnerCPStrategyFactory.getPartnerGoalStrategy( promotion.getPartnerEarnings().getCode() )
            .processGoal( paxPartner, cpResultsByPax, paxCPResultsByPartner, partnerGoalLevelsBySequence );
        partnerPayouts.add( cpCalculationResult );

        ChallengepointAwardSummary levelSummary = (ChallengepointAwardSummary)cpAwardSummaryPartnerMap.get( cpCalculationResult.getGoalLevel().getId() );
        if ( null != levelSummary )
        {
          levelSummary.incrementTotalSelected();
          if ( cpCalculationResult.isAchieved() )
          {
            levelSummary.incrementTotalAchieved();
            levelSummary.incrementTotalAward( cpCalculationResult.getCalculatedAchievement() );
          }
        }
      }
    }

    return partnerPayouts;
  }

  private List<ParticipantPartner> getEligiblePaxPartners( ChallengePointPromotion promotion,
                                                           Map<Long, List<ChallengePointCalculationResult>> paxCPResultsByPartner,
                                                           Map<Long, ChallengePointCalculationResult> cpResultsByPax,
                                                           Map<Long, Boolean> payoutEligibilityByPax )
  {
    // Map of eligibility by Partner ID
    Map<Long, Boolean> eligibilityByPartner = new HashMap<Long, Boolean>();

    // Get All Pax Partners for this promotion
    List<ParticipantPartner> paxPartners = participantPartnerDAO.getParticipantPartnersByPromotion( promotion.getId() );

    // Organize partners PAX in a MAP
    for ( Iterator<ParticipantPartner> iter = paxPartners.iterator(); iter.hasNext(); )
    {
      ParticipantPartner paxPartner = iter.next();

      // Check if Partner is in eligible audience
      if ( !isPartnerEligibleForPayout( paxPartner, promotion, payoutEligibilityByPax, eligibilityByPartner ) )
      {
        iter.remove();
        continue;
      }

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );

      Participant partner = participantService.getParticipantByIdWithAssociations( paxPartner.getPartner().getId(), associationRequestCollection );

      paxPartner.setPartner( partner );
      ChallengePointCalculationResult paxGoalCalculationResult = cpResultsByPax.get( paxPartner.getParticipant().getId() );
      // If PAX who selected the partner has a Goal Result
      if ( null != paxGoalCalculationResult )
      {
        List<ChallengePointCalculationResult> partnerPaxGoalResults = paxCPResultsByPartner.get( paxPartner.getPartner().getId() );
        if ( null == partnerPaxGoalResults )
        {
          partnerPaxGoalResults = new ArrayList<ChallengePointCalculationResult>();
          paxCPResultsByPartner.put( paxPartner.getPartner().getId(), partnerPaxGoalResults );
        }
        // Add PAX to the partner list
        partnerPaxGoalResults.add( paxGoalCalculationResult );
      }
    }

    return paxPartners;
  }

  private boolean isPartnerEligibleForPayout( ParticipantPartner paxPartner, ChallengePointPromotion promotion, Map<Long, Boolean> payoutEligibilityByPax, Map<Long, Boolean> eligibilityByPartner )
  {
    Boolean partnerEligibility = (Boolean)eligibilityByPartner.get( paxPartner.getPartner().getId() );
    if ( null == partnerEligibility )
    {
      // Partner should be active and should belong to the Partner Audience List
      if ( promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) )
      {
        partnerEligibility = paxPartner.getPartner().isActive().booleanValue() && audienceService.isUserInPromotionPartnerAudiences( paxPartner.getPartner(), promotion.getPartnerAudiences() )
            ? Boolean.TRUE
            : Boolean.FALSE;
      }
      else
      {
        partnerEligibility = Boolean.TRUE;
      }
      eligibilityByPartner.put( paxPartner.getPartner().getId(), partnerEligibility );
    }

    boolean isEligible = false;

    Boolean paxPayoutEligibility = payoutEligibilityByPax.get( paxPartner.getParticipant().getId() );
    // PAX who picked the Partner should be eligible for payout and so should the partner
    if ( null != paxPayoutEligibility && paxPayoutEligibility.booleanValue() && partnerEligibility.booleanValue() )
    {
      isEligible = true;
    }

    return isEligible;
  }

  private Map<Long, ChallengepointAwardSummary> getEmptyCPAwardSummaryMap( ChallengePointPromotion promotion )
  {
    Map<Long, ChallengepointAwardSummary> summaryMap = new TreeMap<Long, ChallengepointAwardSummary>();
    Set<AbstractGoalLevel> goalLevels = promotion.getGoalLevels();
    if ( goalLevels != null )
    {
      for ( Iterator<AbstractGoalLevel> iter = goalLevels.iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        ChallengepointAwardSummary cpAwardSummary = new ChallengepointAwardSummary();
        cpAwardSummary.setGoalLevel( goalLevel );
        cpAwardSummary.setTotalAward( new BigDecimal( "0" ) );
        summaryMap.put( goalLevel.getId(), cpAwardSummary );
      }
    }
    return summaryMap;
  }

  private boolean isPaxEligibleForPayout( Map<Long, Boolean> map, Participant participant, List<Long> promotionEligiblePax )
  {
    Boolean eligible = map.get( participant.getId() );
    if ( null == eligible )
    {
      // PAX should be active and belong to PAX Audience
      eligible = participant.isActive().booleanValue() && promotionEligiblePax.contains( participant.getId() ) ? Boolean.TRUE : Boolean.FALSE;
      map.put( participant.getId(), eligible );
    }

    return eligible.booleanValue();
  }

  public ParticipantPartnerDAO getParticipantPartnerDAO()
  {
    return participantPartnerDAO;
  }

  public void setParticipantPartnerDAO( ParticipantPartnerDAO participantPartnerDAO )
  {
    this.participantPartnerDAO = participantPartnerDAO;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public GoalQuestService getGoalQuestService()
  {
    return goalQuestService;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }

}
