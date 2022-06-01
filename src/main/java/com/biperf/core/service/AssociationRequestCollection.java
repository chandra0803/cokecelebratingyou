/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/AssociationRequestCollection.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;

/**
 * AssociationRequestCollection contains and applies the contained AssociationRequest objects on the
 * passed domain object(s).
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
 * <td>May 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AssociationRequestCollection extends ArrayList
{
  /**
   * Applies the associationRequests contained in this collection, to the passed domain object;
   * 
   * @param domainObject
   */
  public void process( BaseDomain domainObject )
  {
    if ( domainObject != null )
    {
      for ( Iterator iter = this.iterator(); iter.hasNext(); )
      {
        AssociationRequest associationRequest = (AssociationRequest)iter.next();
        associationRequest.execute( domainObject );

      }
    }
  }

  /**
   * Applies the associationRequests contained in this collection, to the passed list of domain
   * objects;
   * 
   * @param domainObjectCollection
   */
  public void process( Collection domainObjectCollection )
  {
    if ( domainObjectCollection != null )
    {
      for ( Iterator iter = domainObjectCollection.iterator(); iter.hasNext(); )
      {
        BaseDomain domainObject = (BaseDomain)iter.next();
        process( domainObject );
      }
    }
  }
}
