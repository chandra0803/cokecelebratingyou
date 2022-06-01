/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/process/impl/ProcessInvocationAssociationRequest.java,v $
 */

package com.biperf.core.service.process.impl;

import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.service.BaseAssociationRequest;

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
 * <td>robinsra</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessInvocationAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;
  public static final int ALL = 1;
  public static final int COMMENTS = 2;
  public static final int PARAMETERS = 3;
  public static final int USERS = 4;
  public static final int PROCESS = 5;

  public ProcessInvocationAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    ProcessInvocation processInvocation = (ProcessInvocation)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
      {
        hydrateAll( processInvocation );
        break;
      }
      case COMMENTS:
      {
        hydrateComments( processInvocation );
        break;
      }
      case PARAMETERS:
      {
        hydrateParameters( processInvocation );
        break;
      }
      case USERS:
      {
        hydrateUsers( processInvocation );
        break;
      }
      case PROCESS:
      {
        hydrateProcess( processInvocation );
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * hydrates all attributes on process invocation.
   * 
   * @param processInvocation
   */
  private void hydrateAll( ProcessInvocation processInvocation )
  {
    hydrateComments( processInvocation );
    hydrateParameters( processInvocation );
    hydrateUsers( processInvocation );
    hydrateProcess( processInvocation );
  }

  /**
   * hydrates the processInvocationComments on process invocation.
   * 
   * @param processInvocation
   */
  private void hydrateComments( ProcessInvocation processInvocation )
  {
    initialize( processInvocation.getProcessInvocationComments() );
  }

  /**
   * hydrates the processInvocation parameters on process invocation.
   * 
   * @param processInvocation
   */
  private void hydrateParameters( ProcessInvocation processInvocation )
  {
    initialize( processInvocation.getProcessInvocationParameters() );
  }

  /**
   * hydrates the runAsUser on process invocation.
   * 
   * @param processInvocation
   */
  private void hydrateUsers( ProcessInvocation processInvocation )
  {
    initialize( processInvocation.getRunAsUser() );
  }

  /**
   * hydrates the process on process invocation.
   * 
   * @param processInvocation
   */
  private void hydrateProcess( ProcessInvocation processInvocation )
  {
    initialize( processInvocation.getProcess() );
  }

}
