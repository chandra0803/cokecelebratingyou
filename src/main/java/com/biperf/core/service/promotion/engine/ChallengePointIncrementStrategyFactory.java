/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ChallengePointIncrementStrategyFactory.java,v $
 */

package com.biperf.core.service.promotion.engine;

/**
 * ChallengePointIncrementStrategyFactory.
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
 * <td>babu</td>
 * <td>Jul 14, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ChallengePointIncrementStrategyFactory
{
  public static final String BEAN_NAME = "challengePointIncrementStrategyFactory";

  /**
   * Construct a ChallengePointIncrementStrategy instance. The type of strategy is determined by the achievement structure.
   * 
   * @param incrementStructure
   * @return AchievementStrategy
   */
  public ChallengePointIncrementStrategy getChallengePointIncrementStrategy( String incrementStructure );

}
