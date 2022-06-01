/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/AbstractChallengePointAchievementStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;

/**
 * AbstractChallengePointPayoutStrategy.
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
 * <td>Todd</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractChallengePointAchievementStrategy implements ChallengePointAchievementStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.ChallengePointPayoutStrategy#processChallengePoint(com.biperf.core.domain.challengePointquest.PaxLevel)
   * @param paxChallengePoint
   * @return
   */
  public final ChallengePointCalculationResult processChallengePoint( PaxGoal paxChallengePoint )
  {
    ChallengePointCalculationResult challengePointCalculationResult = processChallengePointInternal( paxChallengePoint );
    if ( challengePointCalculationResult != null )
    {
      if ( paxChallengePoint != null )
      {
        challengePointCalculationResult.setGoalLevel( (GoalLevel)paxChallengePoint.getGoalLevel() );
        challengePointCalculationResult.setTotalPerformance( paxChallengePoint.getCurrentValue() );
        challengePointCalculationResult.setBaseObjective( paxChallengePoint.getBaseQuantity() );
      }

    }
    if ( paxChallengePoint != null )
    {
      if ( ( (ChallengePointPromotion)paxChallengePoint.getGoalQuestPromotion() ).isBeforeFinalProcessDate() )
      {
        challengePointCalculationResult.setAchieved( false );
      }
    }
    return challengePointCalculationResult;
  }

  /**
   * @param paxChallengePoint to calcluate award for
   * @return a ChallengePointCalculationResult
   */
  protected abstract ChallengePointCalculationResult processChallengePointInternal( PaxGoal paxChallengePoint );

  /**
   * @param currentValue
   * @param achievementAmount
   * @param achievementPrecision
   * @param roundingMethod
   * @return boolean
   */
  protected boolean meetsAchievement( BigDecimal currentValue, BigDecimal achievementAmount, AchievementPrecision achievementPrecision, RoundingMethod roundingMethod )
  {
    if ( currentValue != null && achievementAmount != null )
    {
      int precision = achievementPrecision.getPrecision();
      int roundingMode = roundingMethod.getBigDecimalRoundingMode();
      BigDecimal roundedCurrentValue = currentValue.setScale( precision, roundingMode );
      BigDecimal roundedAchievementAmount = achievementAmount.setScale( precision, roundingMode );
      if ( roundedCurrentValue.compareTo( roundedAchievementAmount ) >= 0 )
      {
        return true;
      }
    }
    return false;
  }

}
