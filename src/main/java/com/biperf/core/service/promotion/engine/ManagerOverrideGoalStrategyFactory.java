/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

/**
 * ManagerOverrideGoalStrategyFactory.
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
public interface ManagerOverrideGoalStrategyFactory
{

  /**
   * Construct a ManagerOverrideGoalStrategy instance. The type of strategy is determined by the override structure.
   * 
   * @param overrideStructure
   * @return ManagerOverrideGoalStrategy
   */
  public ManagerOverrideGoalStrategy getManagerOverrideGoalStrategy( String overrideStructure );

}
