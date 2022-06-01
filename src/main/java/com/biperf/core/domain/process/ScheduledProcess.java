/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.process;

import java.util.Map;

/**
 * .
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
public class ScheduledProcess
{
  private String name;
  private Process process;
  private ProcessSchedule processSchedule;
  private Map processParameterStringArrayMap;

  /**
   * @return value of name property
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name value for name property
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return value of process property
   */
  public Process getProcess()
  {
    return process;
  }

  /**
   * @param process value for process property
   */
  public void setProcess( Process process )
  {
    this.process = process;
  }

  /**
   * @return value of processParameterStringArrayMap property
   */
  public Map getProcessParameterStringArrayMap()
  {
    return processParameterStringArrayMap;
  }

  /**
   * @param processParameterStringArrayMap value for processParameterStringArrayMap property
   */
  public void setProcessParameterStringArrayMap( Map processParameterStringArrayMap )
  {
    this.processParameterStringArrayMap = processParameterStringArrayMap;
  }

  /**
   * @return value of processSchedule property
   */
  public ProcessSchedule getProcessSchedule()
  {
    return processSchedule;
  }

  /**
   * @param processSchedule value for processSchedule property
   */
  public void setProcessSchedule( ProcessSchedule processSchedule )
  {
    this.processSchedule = processSchedule;
  }

}
