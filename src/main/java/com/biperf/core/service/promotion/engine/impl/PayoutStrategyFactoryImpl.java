/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.PayoutStrategy;
import com.biperf.core.service.promotion.engine.PayoutStrategyFactory;

/**
 * PayoutStrategyFactoryImpl.
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
 * <td>Aug 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutStrategyFactoryImpl implements PayoutStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Construct a PayoutStrategy instance. The type of strategy is determined by the payout type.
   * 
   * @param payoutType
   * @return PayoutStrategy
   */
  public PayoutStrategy getPayoutStrategy( String payoutType )
  {
    PayoutStrategy payoutStrategy = (PayoutStrategy)entries.get( payoutType );
    if ( payoutStrategy == null )
    {
      throw new BeaconRuntimeException( "Unknown promotionPayoutTypeCode: " + payoutType );
    }

    return payoutStrategy;
  }

  /**
   * @return value of entries property
   */
  public Map getEntries()
  {
    return entries;
  }

  /**
   * @param entries value for entries property
   */
  public void setEntries( Map entries )
  {
    this.entries = entries;
  }
}
