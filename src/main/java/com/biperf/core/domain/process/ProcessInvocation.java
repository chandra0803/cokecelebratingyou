/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.util.ProcessUtil;

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
public class ProcessInvocation extends BaseDomain
{
  private Process process;
  private Date startDate;
  private Date endDate;
  private User runAsUser;

  private Set processInvocationParameters = new LinkedHashSet();
  private List processInvocationComments = new ArrayList();

  public List getProcessInvocationComments()
  {
    return processInvocationComments;
  }

  public void setProcessInvocationComments( List comments )
  {
    this.processInvocationComments = comments;
  }

  public void addProcessInvocationComment( ProcessInvocationComment comment )
  {
    comment.setProcessInvocation( this );
    processInvocationComments.add( comment );
  }

  /**
   * @return value of endDate property
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate value for endDate property
   */
  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @return value of processInvocationParameters property
   */
  public Set getProcessInvocationParameters()
  {
    return processInvocationParameters;
  }

  public void setProcessInvocationParameters( Set processInvocationParameters )
  {
    this.processInvocationParameters = processInvocationParameters;
  }

  public void addProcessInvocationParameter( ProcessInvocationParameter processInvocationParameter )
  {
    processInvocationParameter.setProcessInvocation( this );
    processInvocationParameters.add( processInvocationParameter );
  }

  /**
   * @return value of runAsUser property
   */
  public User getRunAsUser()
  {
    return runAsUser;
  }

  /**
   * @param runAsUser value for runAsUser property
   */
  public void setRunAsUser( User runAsUser )
  {
    this.runAsUser = runAsUser;
  }

  /**
   * @return value of startDate property
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate value for startDate property
   */
  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
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
   * Get a simplified Map of Parameter Values as a List of formatted Parameter Values (showing
   * formatted value for picklist items and for db source items) keyed by the parameter name.
   * 
   * @return a Map<String, List>
   */
  public Map getFormattedParameterValueMap()
  {
    LinkedHashMap unformattedParameterValueMap = new LinkedHashMap();
    // first, sort parameters alphabetically by name
    List sortedParameters = new ArrayList( processInvocationParameters );
    Collections.sort( sortedParameters, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        ProcessInvocationParameter param1 = (ProcessInvocationParameter)object;
        ProcessInvocationParameter param2 = (ProcessInvocationParameter)object1;

        return param1.getProcessParameterName().compareTo( param2.getProcessParameterName() );
      }
    } );

    for ( Iterator iter = sortedParameters.iterator(); iter.hasNext(); )
    {
      // First build up unformatted map that is suitable input to
      // ProcessUtil.getFormattedParameterValueMap().

      ProcessInvocationParameter processInvocationParameter = (ProcessInvocationParameter)iter.next();
      String processParameterName = processInvocationParameter.getProcessParameterName();
      String[] processInvocationParameterValues = new String[processInvocationParameter.getProcessInvocationParameterValues().size()];
      unformattedParameterValueMap.put( processParameterName, processInvocationParameterValues );

      int index = 0;
      for ( Iterator iterator = processInvocationParameter.getProcessInvocationParameterValues().iterator(); iterator.hasNext(); )
      {
        ProcessInvocationParameterValue processInvocationParameterValue = (ProcessInvocationParameterValue)iterator.next();
        processInvocationParameterValues[index] = processInvocationParameterValue.getValue();
        index++;
      }
    }

    return ProcessUtil.getFormattedParameterValueMap( process, unformattedParameterValueMap );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   */
  public boolean equals( Object object )
  {
    if ( true )
    {
      throw new BeaconRuntimeException( "No business key. If equals needed, add guid" );
    }
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    return 0;
  }

}
