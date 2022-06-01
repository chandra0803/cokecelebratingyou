
package com.biperf.core.service.challengepoint;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.ChallengepointPaxValueBean;

public interface ChallengePointService extends SAO
{
  public static final String BEAN_NAME = "challengePointService";

  /**
   * @param userId
   * @return List of all ChallengePointPromotionObjects with PaxGoals present for selected promotions
   */
  public List<ChallengePointPromotion> getAllLiveChallengePointPromotionsByUserId( Long userId ) throws ServiceErrorException;

  public List<PaxGoal> getAllLivePromotionsWithPaxGoalsByUserIdWithAssociations( Long userId, AssociationRequestCollection ascReqCol );

  public List<PaxGoal> getAllLivePromotionsWithPaxGoalsByUserId( Long userId );

  public BigDecimal computePercentOfBaseAmount( ChallengePointPromotion promotion, BigDecimal base, BigDecimal value );

  public BigDecimal computePercent( ChallengePointPromotion promotion, BigDecimal total, BigDecimal achieved );

  public ChallengepointPaxValueBean populateChallengepointPaxValueBean( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel ) throws ServiceErrorException;

  public ChallengepointPaxValueBean populateChallengepointPaxValueBean( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel, Long userId ) throws ServiceErrorException;

  public ChallengepointPaxValueBean populateChallengepointPaxValueBean( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel, boolean productFlag ) throws ServiceErrorException;

  public BigDecimal getCalculatedAchievementAmount( GoalLevel goalLevel, PaxGoal paxChallengePoint );

  public GoalLevel getChallengePointLevelForOwnerOrMember( ChallengePointPromotion ChallengePointPromotion, Integer ChallengePointLevelSequence, boolean isOwner );

  public boolean isValidChallengePointPromotionForParticipant( Promotion promotion );

  public ChallengePointPromotion getPromotion( Long promotionId );

  public Participant getParticipant( Long userId );

  public PaxGoal getPaxChallengePoint( Long promotionId, Long userId );

  public PaxGoal getPaxChallengePoint( Long promotionId, Long userId, AssociationRequestCollection ascReqCollection );

  public List<ChallengepointPaxValueBean> getParticipantChallengePointLevelBeans( ChallengePointPromotion promotion, PaxGoal paxChallengePoint, boolean productsFlag ) throws ServiceErrorException;

  public List<ChallengepointAward> getParticipantChallengePointAwards( Long promotionId, Long userId );

  /**
   * Get the max CpLevel sequence for all active promotions
   * 
   * @return int
   */
  public int getMaxSequence();

  /**
   * Get the max CpLevel sequence for all active promotions where cp selection start
   * date has passed
   * 
   * @return int
   */
  public int getMaxSequenceWhereCpSelectionStarted();

  /**
   * Get the max CpLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  public int getMaxSequenceWhereIssueAwardsRun();

  /**
   * This method calculates the threshold for a user for a given promotion
   * @param promotionId Long
   * @param userId Long
   * @return calculatedThreshold BigDecimal
   */
  public BigDecimal getCalculatedThreshold( Long promotionId, Long userId );

  public boolean isParticipantOwner( Participant pax );

  public boolean isParticipantManager( Participant pax );

  /**
   * This method saves the ChallengepointAward object
   * @param challengepointAward
   * @return challengepointAward
   */
  public ChallengepointAward saveChallengepointAward( ChallengepointAward challengepointAward );

  /**
   * Generate and mail a detail extract from the challengepoint CalculationList
   * 
   * @param cpCalculationList
   * @return boolean 
   */
  public boolean generateAndMailExtractReport( List<ChallengepointPaxAwardValueBean> cpCalculationList, ChallengePointPromotion promotion, List cpManagerOverrideList );

  /**
   * Generate and mail a detail extract from the challengepoint CalculationList
   * 
   * @param cpCalculationList
   * @return boolean 
   */
  public boolean generateAndMailExtractReport( List<ChallengepointPaxAwardValueBean> cpCalculationList, ChallengePointPromotion promotion, List cpManagerOverrideList, String batchComments );

  /**
   * @param pax
   * @param promotion
   * @return boolean
   */
  public boolean isChallengepointSelected( Participant pax, Promotion promotion );

  public PaxGoal getLevelSpecificChallengePoint( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel );

}
