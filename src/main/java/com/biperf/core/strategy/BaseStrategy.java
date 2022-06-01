/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/BaseStrategy.java,v $
 */

package com.biperf.core.strategy;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * BaseStrategy.
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
 * <td>waldal</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseStrategy implements Strategy
{
  private SystemVariableService systemVariableService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.strategy.Strategy#getSystemVariableService()
   * @return SystemVariableService
   */
  public SystemVariableService getSystemVariableService()
  {
    if ( systemVariableService == null )
    {
      systemVariableService = (SystemVariableService)ApplicationContextFactory.getApplicationContext().getBean( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;
  }

  /**
   * This sets the SystemVariableService on the Strategy. Mostly for testing with mocks.
   * 
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

}
