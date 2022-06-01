/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

/**
 * PayoutStrategyFactory.
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
 * <td>wadzinsk</td>
 * <td>Aug 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PayoutStrategyFactory
{
  /**
   * Construct a PayoutStrategy instance. The type of strategy is determined by the payout type.
   * 
   * @param payoutType
   * @return PayoutStrategy
   */
  public PayoutStrategy getPayoutStrategy( String payoutType );
}
