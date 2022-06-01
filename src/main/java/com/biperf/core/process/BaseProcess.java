/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import java.util.Map;

import org.quartz.UnableToInterruptJobException;

/**
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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface BaseProcess
{
  public void execute( Map jobDataMap, Long processInvocationId );

  public void interrupt() throws UnableToInterruptJobException;

  public Map getProcessParameters();

  /**
   * Injected metadata defining the process.
   * 
   * @param processParameters
   */
  public void setProcessParameters( Map processParameters );
}
