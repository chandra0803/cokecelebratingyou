/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/integration/hibernate/SupplierDAOImplTest.java,v $
 */

package com.biperf.core.dao.integration.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.domain.enums.SupplierStatusType;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * SupplierDAOImplTest.
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
 * <td>June 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SupplierDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link Supplier} object.
   * 
   * @param supplierName the name of the supplier.
   * @return a {@link Supplier} object.
   */
  public static Supplier buildSupplier( String supplierName )
  {
    Supplier supplier = new Supplier();

    supplier.setSupplierName( supplierName );
    supplier.setSupplierType( "Test Type" );
    supplier.setAllowPartnerSso( true );
    supplier.setStatus( SupplierStatusType.lookup( SupplierStatusType.ACTIVE ) );

    return supplier;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests saving or updating the Supplier. This needs to fetch the Supplier by Id so it is also
   * testing SupplierDAO.getSupplierById(Long id).
   */
  public void testSaveGetSupplierById()
  {

    SupplierDAO supplierDAO = getSupplierDAO();

    Supplier expectedSupplier = buildSupplier( "Test Supplier 1" );
    supplierDAO.saveSupplier( expectedSupplier );

    Supplier actualSupplier = supplierDAO.getSupplierById( expectedSupplier.getId() );

    assertEquals( "Supplier not equals", expectedSupplier, actualSupplier );

    // Update the Supplier
    expectedSupplier.setSupplierName( "testUpdatedName" );

    supplierDAO.saveSupplier( expectedSupplier );

    // Updated Supplier from the database
    actualSupplier = supplierDAO.getSupplierByName( expectedSupplier.getSupplierName() );

    assertEquals( "Updated Supplier not equals", expectedSupplier, actualSupplier );

  }

  /**
   * Tests saving and getting all the supplier records saved.
   */
  public void testGetAll()
  {
    SupplierDAO supplierDAO = getSupplierDAO();

    int count = 0;

    count = supplierDAO.getAll().size();

    List expectedSuppliers = new ArrayList();

    Supplier expectedSupplier1 = buildSupplier( "Test Supplier 1" );
    expectedSuppliers.add( expectedSupplier1 );
    supplierDAO.saveSupplier( expectedSupplier1 );

    Supplier expectedSupplier2 = buildSupplier( "Test Supplier 2" );
    expectedSuppliers.add( expectedSupplier2 );
    supplierDAO.saveSupplier( expectedSupplier2 );

    List actualSuppliers = supplierDAO.getAll();

    assertEquals( "List of suppliers aren't the same size.", expectedSuppliers.size() + count, actualSuppliers.size() );
  }

  public void testGetSupplierByName()
  {
    // Test valid supplier
    assertTrue( "Supplier not found", getSupplierDAO().getSupplierByName( Supplier.BI_BANK ) != null );

    // Test invalid supplier
    assertTrue( "Supplier not supposed to be found", getSupplierDAO().getSupplierByName( "Test Supplier" ) == null );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Get the SupplierDAO.
   * 
   * @return SupplierDAO
   */
  private SupplierDAO getSupplierDAO()
  {
    return (SupplierDAO)ApplicationContextFactory.getApplicationContext().getBean( "supplierDAO" );
  }
}
