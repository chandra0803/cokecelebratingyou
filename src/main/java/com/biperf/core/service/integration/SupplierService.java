/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/integration/SupplierService.java,v $
 */

package com.biperf.core.service.integration;

import java.util.List;

import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.SAO;

/**
 * SupplierService.
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
 * <td>sedey</td>
 * <td>Jun 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface SupplierService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "supplierService";

  /**
   * Saves the Supplier to the database.
   * 
   * @param supplier
   * @return Supplier
   * @throws UniqueConstraintViolationException
   */
  public Supplier saveSupplier( Supplier supplier ) throws UniqueConstraintViolationException, ServiceErrorException;

  /**
   * Gets the Supplier by the id.
   * 
   * @param id
   * @return Supplier
   */
  public Supplier getSupplierById( Long id );

  /**
   * Gets the Supplier by the supplier name.
   * 
   * @param supplierName
   * @return Supplier
   */
  public Supplier getSupplierByName( String supplierName );

  /**
   * Return a List of all Suppliers.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Return a List of all active Suppliers.
   * 
   * @return List
   */
  public List getAllActive();

  public Long getNumberOfAssociationsForSupplier( Long supplierCode );

}
