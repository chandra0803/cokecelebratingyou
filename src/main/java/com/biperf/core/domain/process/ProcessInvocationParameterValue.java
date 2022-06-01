/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/process/ProcessInvocationParameterValue.java,v $
 */

package com.biperf.core.domain.process;

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
public class ProcessInvocationParameterValue extends BaseDomain
{
  private ProcessInvocationParameter processInvocationParameter;
  private String value;
  private Object objectValue;

  /**
   * @return value of processInvocationParameter property
   */
  public ProcessInvocationParameter getProcessInvocationParameter()
  {
    return processInvocationParameter;
  }

  /**
   * @param processInvocationParameter value for processInvocationParameter property
   */
  public void setProcessInvocationParameter( ProcessInvocationParameter processInvocationParameter )
  {
    this.processInvocationParameter = processInvocationParameter;
  }

  /**
   * @return value of value property
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @param value value for value property
   */
  public void setValue( String value )
  {
    this.value = value;
  }

  public Object getObjectValue()
  {
    return objectValue;
  }

  public void setObjectValue( Object objectValue )
  {
    this.objectValue = objectValue;
  }

  public boolean equals( Object obj )
  {
    ProcessInvocationParameterValue processInvocationParameterValue = (ProcessInvocationParameterValue)obj;

    boolean equals = false;

    equals = processInvocationParameter != null && processInvocationParameter.equals( processInvocationParameterValue.getProcessInvocationParameter() );

    if ( equals )
    {
      equals = value != null && value.equals( processInvocationParameterValue.getValue() );
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

    hashCode += processInvocationParameter != null ? processInvocationParameter.hashCode() : 13;
    hashCode += value != null ? value.hashCode() : 17;

    return hashCode;
  }

}
