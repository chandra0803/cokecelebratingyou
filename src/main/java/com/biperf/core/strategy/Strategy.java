/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/Strategy.java,v $
 */

package com.biperf.core.strategy;

import com.biperf.core.service.system.SystemVariableService;

/**
 * Strategy interface is a marker interface for Strategy classes.
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
 * <td>sharma</td>
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface Strategy
{
  /**
   * SystemVariableService is for retrieving system variables
   * 
   * @return SystemVariableService
   */
  public abstract SystemVariableService getSystemVariableService();
}
