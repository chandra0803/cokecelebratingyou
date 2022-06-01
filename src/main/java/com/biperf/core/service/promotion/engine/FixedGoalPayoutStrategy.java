/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/FixedGoalPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/**
 * FixedGoalPayoutStrategy.
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
public class FixedGoalPayoutStrategy extends AbstractGoalPayoutStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractGoalPayoutStrategy#processGoalInternal
   * (com.biperf.core.domain.goalquest.PaxGoal)
   * @param paxGoal
   * @return GoalCalculationResult
   */
  protected GoalCalculationResult processGoalInternal( PaxGoal paxGoal )
  {
    GoalCalculationResult goalCalculationResult = new GoalCalculationResult();

    // Validate the calculation parameters
    if ( paxGoal != null && paxGoal.getGoalQuestPromotion() != null && paxGoal.getGoalLevel() != null && ( (GoalLevel)paxGoal.getGoalLevel() ).getAchievementAmount() != null )
    {
      // Calculate pax current progress
      BigDecimal overriddenValue = null;
      if ( paxGoal.getCurrentValue() != null )
      {
        overriddenValue = paxGoal.getCurrentValue();
        if ( paxGoal.getOverrideQuantity() != null )
        {
          overriddenValue = overriddenValue.add( paxGoal.getOverrideQuantity() );
        }
      }

      // Calculate amount to achieve goal
      BigDecimal amountToAchieve = null;
      GoalQuestPromotion promotion = paxGoal.getGoalQuestPromotion();
      GoalLevel goalLevel = (GoalLevel)paxGoal.getGoalLevel();
      if ( AchievementRuleType.lookup( AchievementRuleType.FIXED ).equals( promotion.getAchievementRule() ) )
      {
        amountToAchieve = goalLevel.getAchievementAmount();
        if ( paxGoal.getBaseQuantity() != null )
        {
          amountToAchieve = amountToAchieve.add( paxGoal.getBaseQuantity() );
        }
        goalCalculationResult.setAmountToAchieve( amountToAchieve );

        // Check if pax meets goal payout requirements and calculate result
        if ( meetsAchievement( overriddenValue, amountToAchieve, promotion.getAchievementPrecision(), promotion.getRoundingMethod() ) )
        {
          goalCalculationResult.setAchieved( true );
          BigDecimal awardAmount = goalLevel.getAward();
          if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
          {
            goalCalculationResult.setCalculatedPayout( new BigDecimal( awardAmount.longValue() ) );
          }
          else
          {
            goalCalculationResult.setCalculatedPayout( awardAmount );
          }
        }
      }
      else if ( AchievementRuleType.lookup( AchievementRuleType.PERCENT_OF_BASE ).equals( promotion.getAchievementRule() ) )
      {
        if ( paxGoal.getBaseQuantity() != null )
        {
          BigDecimal percentage = goalLevel.getAchievementAmount().movePointLeft( 2 );
          amountToAchieve = percentage.multiply( paxGoal.getBaseQuantity() );
          goalCalculationResult.setAmountToAchieve( amountToAchieve.setScale( promotion.getAchievementPrecision().getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() ) );

          // Check if pax meets goal payout requirements and calculate result
          if ( meetsAchievement( overriddenValue, amountToAchieve, promotion.getAchievementPrecision(), promotion.getRoundingMethod() ) )
          {
            goalCalculationResult.setAchieved( true );
            BigDecimal awardAmount = goalLevel.getAward();
            if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
            {
              goalCalculationResult.setCalculatedPayout( new BigDecimal( awardAmount.longValue() ) );
            }
            else
            {
              goalCalculationResult.setCalculatedPayout( awardAmount );
            }
          }
        }
      }
    }
    return goalCalculationResult;
  }
}
