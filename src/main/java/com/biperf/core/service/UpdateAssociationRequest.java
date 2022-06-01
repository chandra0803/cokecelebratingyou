/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/UpdateAssociationRequest.java,v $
 */

package com.biperf.core.service;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;

/**
 * UpdateAssociationRequest is an abstract class for enabling the updating of a domain object. A
 * concrete implementation of the class will have the necessary business logic for updating an
 * attached domain object.
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
 * <td>sathish</td>
 * <td>July 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class UpdateAssociationRequest
{
  private BaseDomain detachedDomain = null;

  /**
   * constructor
   * 
   * @param domain
   */
  protected UpdateAssociationRequest( BaseDomain domain )
  {
    detachedDomain = domain;
  }

  /**
   * Get the detached objects for the association request
   * 
   * @return Object
   */
  public Object getDetachedDomain()
  {
    return detachedDomain;
  }

  /**
   * Setter for the detached domain object for the association request
   * 
   * @param detachedDomain
   */
  public void setDetachedDomain( BaseDomain detachedDomain )
  {
    this.detachedDomain = detachedDomain;
  }

  /**
   * Uses attached and detached versions of a domain object to perform additional validation of the
   * domain object.
   * 
   * @param attachedDomain
   */
  public void validate( BaseDomain attachedDomain ) throws ServiceErrorExceptionWithRollback
  {
    // no validation - is this true?
  }

  /**
   * Method to hydrate the domain domain object association.
   * 
   * @param domainObject
   */
  public abstract void execute( BaseDomain domainObject );

}
