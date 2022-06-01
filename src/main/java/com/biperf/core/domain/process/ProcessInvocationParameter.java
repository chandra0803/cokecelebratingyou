/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.process;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

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
public class ProcessInvocationParameter extends BaseDomain
{

  private ProcessInvocation processInvocation;
  private String processParameterName;
  private Set processInvocationParameterValues = new LinkedHashSet();

  /**
   * @return value of processInvocation property
   */
  public ProcessInvocation getProcessInvocation()
  {
    return processInvocation;
  }

  /**
   * @param processInvocation value for processInvocation property
   */
  public void setProcessInvocation( ProcessInvocation processInvocation )
  {
    this.processInvocation = processInvocation;
  }

  /**
   * @return value of processInvocationParameterValues property
   */
  public Set getProcessInvocationParameterValues()
  {
    return processInvocationParameterValues;
  }

  /**
   * @param processInvocationParameterValues value for processInvocationParameterValues property
   */
  public void setProcessInvocationParameterValues( Set processInvocationParameterValues )
  {
    this.processInvocationParameterValues = processInvocationParameterValues;
  }

  public void addProcessInvocationParameterValue( ProcessInvocationParameterValue processInvocationParameterValue )
  {
    processInvocationParameterValue.setProcessInvocationParameter( this );
    processInvocationParameterValues.add( processInvocationParameterValue );
  }

  /**
   * @return value of processParameterName property
   */
  public String getProcessParameterName()
  {
    return processParameterName;
  }

  /**
   * @param processParameterName value for processParameterName property
   */
  public void setProcessParameterName( String processParameterName )
  {
    this.processParameterName = processParameterName;
  }

  public boolean equals( Object obj )
  {
    ProcessInvocationParameter processInvocationParameter = (ProcessInvocationParameter)obj;

    boolean equals = false;

    equals = processInvocation != null && processInvocation.equals( processInvocationParameter.getProcessInvocation() );

    if ( equals )
    {
      equals = processParameterName != null && processParameterName.equals( processInvocationParameter.getProcessParameterName() );
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int the hashCode
   */
  public int hashCode()
  {

    int hashCode = 0;

    hashCode += processInvocation != null ? processInvocation.hashCode() : 13;
    hashCode += processParameterName != null ? processParameterName.hashCode() : 17;

    return hashCode;
  }

}
