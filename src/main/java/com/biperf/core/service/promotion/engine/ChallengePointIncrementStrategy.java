/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ChallengePointIncrementStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;

import com.biperf.core.domain.promotion.ChallengePointPromotion;

/**
 * ChallengePointIncrementStrategy.
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
 * <td>Jul 14, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ChallengePointIncrementStrategy
{

  /**
   * Calculate the increment for base award for Challengepoint promotion
   * @param promotion challengepointpromotion
   * @param userid participantid
   * @return a calculatedIncrement
   */
  public BigDecimal processIncrement( ChallengePointPromotion promotion, Long userId );
}
