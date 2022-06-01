/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

/**
 * GoalPayoutStrategyFactory.
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
public interface GoalPayoutStrategyFactory
{
  public static final String BEAN_NAME = "goalStrategyFactory";

  /**
   * Construct a GoalPayoutStrategy instance. The type of strategy is determined by the payout structure.
   * 
   * @param payoutType
   * @return PayoutStrategy
   */
  public GoalPayoutStrategy getGoalPayoutStrategy( String payoutStructure );

  /**
   * Construct a GoalPayoutStrategy instance. No payout structure is passed for merch/travel.
   * 
   * @return PayoutStrategy
   */
  public GoalPayoutStrategy getGoalPayoutStrategy();

}
