/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ChallengePointAchievementStrategyFactory.java,v $
 */

package com.biperf.core.service.promotion.engine;

/**
 * ChallengePointAchievementStrategyFactory.
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
public interface ChallengePointAchievementStrategyFactory
{
  public static final String BEAN_NAME = "challengePointAchievementStrategyFactory";

  /**
   * Construct a ChallengePointAchievementStrategy instance. The type of strategy is determined by the achievement structure.
   * 
   * @param achievementType
   * @return AchievementStrategy
   */
  public ChallengePointAchievementStrategy getChallengePointAchievementStrategy( String achievementStructure );

  /**
   * Construct a ChallengePointAchievementStrategy instance. No achievement structure is passed for merch/travel.
   * 
   * @return AchievementStrategy
   */
  public ChallengePointAchievementStrategy getChallengePointAchievementStrategy();

}
