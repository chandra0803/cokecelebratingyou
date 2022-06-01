/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategy;
import com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategyFactory;

/**
 * ManagerOverrideGoalStrategyFactoryImpl.
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
 * <td>Jan 9, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ManagerOverrideGoalStrategyFactoryImpl implements ManagerOverrideGoalStrategyFactory
{

  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategyFactory#getManagerOverrideGoalStrategy(java.lang.String)
   * @param overrideStructure
   * @return
   */
  public ManagerOverrideGoalStrategy getManagerOverrideGoalStrategy( String overrideStructure )
  {
    ManagerOverrideGoalStrategy goalStrategy = null;
    if ( overrideStructure != null )
    {
      goalStrategy = (ManagerOverrideGoalStrategy)entries.get( overrideStructure );
    }
    if ( overrideStructure == null )
    {
      throw new BeaconRuntimeException( "Unknown Override StructureCode: " + overrideStructure );
    }

    return goalStrategy;
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
