/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/impl/OrderStrategyFactoryImpl.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.strategy.OrderStrategy;
import com.biperf.core.strategy.OrderStrategyFactory;

/**
 * OrderStrategyFactoryImpl.
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
 * <td>FeiMo</td>
 * <td>Nov 6, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OrderStrategyFactoryImpl implements OrderStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Construct a OrderStrategy instance. The type of strategy is determined by the payment method.
   * 
   * @param orderType
   * @return OrderStrategy
   */
  public OrderStrategy getOrderStrategy( String orderType )
  {
    OrderStrategy orderStrategy = (OrderStrategy)entries.get( orderType );
    if ( orderType == null )
    {
      throw new BeaconRuntimeException( "Unknown order type: " + orderType );
    }

    return orderStrategy;
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
