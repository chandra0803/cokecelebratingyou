/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ChallengePointManagerPayoutStrategyFactory.java,v $
 */

package com.biperf.core.service.promotion.engine;

/**
 * ChallengePointManagerPayoutStrategyFactory.
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
 * <td>Aug 13, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ChallengePointManagerPayoutStrategyFactory
{
  public static final String BEAN_NAME = "challengePointManagerPayoutStrategyFactory";

  /**
   * Construct a ChallengePointManagerPayoutStrategyFactory instance. The type of strategy is determined by the achievement structure.
   * 
   * @param achievementType
   * @return ManagerPayoutStrategy
   */
  public ChallengePointManagerPayoutStrategy getChallengePointManagerPayoutStrategy( String achievementStructure );

  /**
   * Construct a ChallengePointManagerPayoutStrategyFactory instance. No achievement structure is passed for merch/travel.
   * 
   * @return ManagerPayoutStrategy
   */
  public ChallengePointManagerPayoutStrategy getChallengePointManagerPayoutStrategy();

}
