
package com.biperf.core.dao.challengepoint;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.challengepoint.ChallengepointAward;

public interface ChallengepointAwardDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "challengepointAwardDAO";

  /**
   * Get the ChallengepointAward of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return paxLevel
   */
  public ChallengepointAward getChallengepointAwardByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Get the  Basic Award of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return Basic Award
   */
  public ChallengepointAward getBasicAwardByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Get the ChallengepointAwards of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return paxLevel
   */
  public List<ChallengepointAward> getAllChallengepointAwardsByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Saves the ChallengepointAward.
   * 
   * @param challengepointAward
   * @return challengepointAward
   */
  public ChallengepointAward saveChallengepointAward( ChallengepointAward challengepointAward );

  /**
   * Get the challengepointAward of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return challengepointAward
   */
  public ChallengepointAward getAwardByPromotionIdAndUserId( Long promotionId, Long userId );

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
  /**
   * Get the  Sum of Basic Award of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return Basic Award
   */
  public Long getSumBasicAwardByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Check for payout complete for a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return boolean - true if payout complete , false if payout not complete
   * date : May 23 2011
   */

  public boolean isParticipantPayoutComplete( Long promotionId, Long userId );

}
