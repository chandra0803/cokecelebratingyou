/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/integration/SupplierDAO.java,v $
 */

package com.biperf.core.dao.integration;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.supplier.Supplier;

/**
 * SupplierDAO.
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
 */
public interface SupplierDAO extends DAO
{
  /**
   * Get the Supplier by id.
   * 
   * @param id
   * @return Supplier
   */
  public Supplier getSupplierById( Long id );

  /**
   * Get the Supplier by name.
   * 
   * @param supplierName
   * @return Supplier
   */
  public Supplier getSupplierByName( String supplierName );

  /**
   * Get All Supplier records.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get All Active Supplier records.
   * 
   * @return List
   */
  public List getAllActive();

  /**
   * Save or update the Supplier.
   * 
   * @param supplier
   * @return Supplier
   */
  public Supplier saveSupplier( Supplier supplier );

  public Long getNumberOfAssociationsForSupplier( Long supplierCode );

}
