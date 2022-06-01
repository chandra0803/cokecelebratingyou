/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/process/impl/ProcessAssociationRequest.java,v $
 */

package com.biperf.core.service.process.impl;

import com.biperf.core.domain.process.Process;
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
public class ProcessAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;
  public static final int ROLES = 2;

  public ProcessAssociationRequest( int hydrateLevel )
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
    Process process = (Process)domainObject;

    switch ( hydrateLevel )
    {
      case ROLES:
      {
        hydrateRoles( process );
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * hydrates the budget with the budget's budgetmaster and owner.
   * 
   * @param process
   */
  private void hydrateRoles( Process process )
  {
    initialize( process.getEditRoles() );
    initialize( process.getLaunchRoles() );
    initialize( process.getViewLogRoles() );
  }
}
