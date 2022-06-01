/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategy;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory;

/**
 * GoalPayoutStrategyFactoryImpl.
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
public class GoalPayoutStrategyFactoryImpl implements GoalPayoutStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory#getGoalPayoutStrategy(com.biperf.core.domain.enums.PayoutStructure)
   * @param payoutStructure
   * @return
   */
  public GoalPayoutStrategy getGoalPayoutStrategy( String payoutStructure )
  {
    GoalPayoutStrategy payoutStrategy = null;
    if ( payoutStructure != null )
    {
      payoutStrategy = (GoalPayoutStrategy)entries.get( payoutStructure );
    }
    if ( payoutStrategy == null )
    {
      throw new BeaconRuntimeException( "Unknown PayoutStructureCode: " + payoutStructure );
    }

    return payoutStrategy;
  }

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory#getGoalPayoutStrategy(com.biperf.core.domain.enums.PayoutStructure)
   * @return
   */
  public GoalPayoutStrategy getGoalPayoutStrategy()
  {
    // Default to fixed for merch/travel
    return getGoalPayoutStrategy( "fixed" );
  }

  public Map getEntries()
  {
    return entries;
  }

  public void setEntries( Map entries )
  {
    this.entries = entries;
  }

}
