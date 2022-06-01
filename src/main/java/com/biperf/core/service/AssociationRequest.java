/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/AssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service;

/**
 * AssociationRequest is an interface for enabling the hydration of domain object associations. A
 * concrete implementation of the interface will do the necessary hydration by reading/initializing
 * the association.
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
public interface AssociationRequest
{
  /**
   * Method to hydrate the domain object association.
   * 
   * @param domainObject
   */
  public void execute( Object domainObject );
}
