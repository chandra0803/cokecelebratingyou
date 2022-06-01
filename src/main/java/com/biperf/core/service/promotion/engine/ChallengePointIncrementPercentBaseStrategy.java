/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ChallengePointIncrementPercentBaseStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengePointService;

/**
 * ChallengePointIncrementPercentBaseStrategy.
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
public class ChallengePointIncrementPercentBaseStrategy implements ChallengePointIncrementStrategy
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
    BigDecimal increment = null;
    PaxGoal paxGoal = challengePointService.getPaxChallengePoint( promotion.getId(), userId );
    if ( paxGoal == null || paxGoal.getBaseQuantity() == null )
    {
      return increment;
    }
    AchievementPrecision achPrecision = AchievementPrecision.lookup( AchievementPrecision.ZERO );
    if ( promotion.getAchievementPrecision() != null )
    {
      achPrecision = promotion.getAchievementPrecision();
    }
    increment = paxGoal.getBaseQuantity().multiply( new BigDecimal( promotion.getAwardIncrementValue().doubleValue() ) )
        .divide( new BigDecimal( 100 ), achPrecision.getPrecision(), promotion.getRoundingMethod().getBigDecimalRoundingMode() );

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
