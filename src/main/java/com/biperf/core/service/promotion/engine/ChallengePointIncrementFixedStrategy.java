/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ChallengePointIncrementFixedStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengePointService;

/**
 * ChallengePointIncrementFixedStrategy.
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
 * <td>Babu</td>
 * <td>Jul 15, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * 
 */
public class ChallengePointIncrementFixedStrategy implements ChallengePointIncrementStrategy
{
  ChallengePointService challengePointService = null;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategy#processIncrement
   *      (com.biperf.core.domain.promotion.ChallengePointPromotion, Long)
   * @param promotion
   *            challengepointpromotion
   * @param userid
   *            participantid
   * @return a calculatedIncrement
   */
  public BigDecimal processIncrement( ChallengePointPromotion promotion, Long userId )
  {
    PaxGoal paxGoal = challengePointService.getPaxChallengePoint( promotion.getId(), userId );
    BigDecimal increment = new BigDecimal( promotion.getAwardIncrementValue().doubleValue() );
    AchievementPrecision achPrecision = AchievementPrecision.lookup( AchievementPrecision.ZERO );
    if ( promotion.getAchievementPrecision() != null )
    {
      achPrecision = promotion.getAchievementPrecision();
    }
    increment = increment.setScale( achPrecision.getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() );
    return increment;

  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }
}
