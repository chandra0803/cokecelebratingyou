/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ChallengePointAchievementFixedStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;

/**
 * FixedChallengePointPayoutStrategy.
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
 * <td>meadows</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointAchievementFixedStrategy extends AbstractChallengePointAchievementStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractChallengePointPayoutStrategy#processChallengePointInternal
   * (com.biperf.core.domain.challengePointquest.PaxChallengePoint)
   * @param paxGoal
   * @return ChallengePointCalculationResult
   */
  protected ChallengePointCalculationResult processChallengePointInternal( PaxGoal paxGoal )
  {
    ChallengePointCalculationResult challengePointCalculationResult = new ChallengePointCalculationResult();

    // Validate the calculation parameters
    if ( paxGoal != null && paxGoal.getGoalQuestPromotion() != null && paxGoal.getGoalLevel() != null && ( (GoalLevel)paxGoal.getGoalLevel() ).getAchievementAmount() != null )
    {
      // Calculate pax current progress
      BigDecimal currentValue = null;
      if ( paxGoal.getCurrentValue() != null )
      {
        currentValue = paxGoal.getCurrentValue();
        if ( paxGoal.getOverrideQuantity() != null )
        {
          currentValue = currentValue.add( paxGoal.getOverrideQuantity() );
        }
      }

      // Calculate amount to achieve challengePoint
      BigDecimal amountToAchieve = null;
      ChallengePointPromotion promotion = (ChallengePointPromotion)paxGoal.getGoalQuestPromotion();
      GoalLevel goalLevel = (GoalLevel)paxGoal.getGoalLevel();
      if ( promotion.getAchievementPrecision().getCode().equals( "two" ) && goalLevel.getAchievementAmount() != null )
      {
        amountToAchieve = goalLevel.getAchievementAmount().setScale( 2 );
      }
      else
      {
        amountToAchieve = goalLevel.getAchievementAmount();
      }

      challengePointCalculationResult.setAmountToAchieve( amountToAchieve );

      // Check if pax meets challengePoint payout requirements and calculate result
      if ( meetsAchievement( currentValue, amountToAchieve, promotion.getAchievementPrecision(), promotion.getRoundingMethod() ) )
      {
        challengePointCalculationResult.setAchieved( true );
        BigDecimal awardAmount = goalLevel.getAward();
        if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
        {
          challengePointCalculationResult.setCalculatedAchievement( new BigDecimal( awardAmount.longValue() ) );
        }
        else
        {
          challengePointCalculationResult.setCalculatedAchievement( awardAmount );
        }
      }

    }
    return challengePointCalculationResult;
  }
}
