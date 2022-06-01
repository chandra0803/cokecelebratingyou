/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/PartnerGoalStrategyFactory.java,v $
 */

package com.biperf.core.service.promotion.engine;

/**
 * PartnerGoalStrategyFactory.
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
 * <td>gadapa</td>
 * <td>Mar 31, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PartnerGoalStrategyFactory
{

  /**
   * Construct a PartnerGoalStrategyFactory instance. The type of strategy is determined by the override structure.
   * 
   * @param payoutStructure
   * @return PartnerGoalStrategyFactory
   */
  public PartnerGoalStrategy getPartnerGoalStrategy( String payoutStructure );

}
