/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/integration/impl/SupplierServiceImplTest.java,v $
 */

package com.biperf.core.service.integration.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.cms.CMAssetService;

/**
 * SupplierServiceImplTest.
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
public class SupplierServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public SupplierServiceImplTest( String test )
  {
    super( test );
  }

  /** supplierServiceImplementation */
  private SupplierServiceImpl supplierService = new SupplierServiceImpl();
  private CMAssetService cmAssetServiceMock;

  /** mocks */
  private Mock mockSupplierDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockSupplierDAO = new Mock( SupplierDAO.class );

    supplierService.setSupplierDAO( (SupplierDAO)mockSupplierDAO.proxy() );

    cmAssetServiceMock = EasyMock.createMock( CMAssetService.class );

    supplierService.setCmAssetService( cmAssetServiceMock );
  }

  /**
   * Test getting the Supplier by id.
   */
  public void testGetSupplierById()
  {
    // Get the test Supplier.
    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestServiceSupplier" );
    supplier.setSupplierType( "TestType" );

    // SupplierDAO expected to call getSupplierById once with the SupplierId which will return the
    // Supplier expected
    mockSupplierDAO.expects( once() ).method( "getSupplierById" ).with( same( supplier.getId() ) ).will( returnValue( supplier ) );

    supplierService.getSupplierById( supplier.getId() );

    mockSupplierDAO.verify();
  }

  /**
   * Test updating the Supplier through the service.
   * 
   * @throws UniqueConstraintViolationException
   */
  public void testSaveSupplierUpdate() throws UniqueConstraintViolationException, ServiceErrorException
  {

    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestServiceSupplier" );
    supplier.setSupplierType( "TestType" );

    Supplier dbSupplier = new Supplier();
    dbSupplier.setId( new Long( 1 ) );
    dbSupplier.setSupplierName( "TestServiceSupplier" );
    dbSupplier.setSupplierType( "TestType" );

    // Supplier
    mockSupplierDAO.expects( once() ).method( "saveSupplier" ).with( same( supplier ) );
    mockSupplierDAO.expects( once() ).method( "getSupplierByName" ).with( same( supplier.getSupplierName() ) ).will( returnValue( dbSupplier ) );

    // test the supplierService.saveSupplier
    supplierService.saveSupplier( supplier );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockSupplierDAO.verify();
  }

  /**
   * Test adding the Supplier through the service.
   * 
   * @throws UniqueConstraintViolationException
   */
  public void testSaveSupplierInsert() throws UniqueConstraintViolationException, ServiceErrorException
  {
    // Create the test Supplier.
    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestServiceSupplier" );
    supplier.setSupplierType( "TestType" );
    supplier.setCmAssetCode( "testAssetName" );

    Supplier savedSupplier = new Supplier();
    savedSupplier.setId( new Long( 1 ) );
    savedSupplier.setSupplierName( "TestServiceSupplier" );
    savedSupplier.setSupplierType( "TestType" );
    supplier.setCmAssetCode( "testAssetName" );

    // insert a new supplier
    mockSupplierDAO.expects( once() ).method( "saveSupplier" ).with( same( supplier ) ).will( returnValue( savedSupplier ) );

    mockSupplierDAO.expects( once() ).method( "getSupplierByName" ).with( same( supplier.getSupplierName() ) ).will( returnValue( null ) );

    EasyMock.expect( cmAssetServiceMock.getUniqueAssetCode( Supplier.CM_SUPPLIER_ASSET_TYPE ) ).andReturn( supplier.getCmAssetCode() );

    // test the SupplierService.saveSupplier
    supplierService.saveSupplier( supplier );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockSupplierDAO.verify();
  }

  /**
   * Test adding duplicate supplier through the service.
   */
  public void testSaveSupplierInsertConstraintViolation()
  {
    // Create the test Supplier.
    Supplier supplier = new Supplier();
    supplier.setSupplierName( "TestServiceSupplier" );
    supplier.setSupplierType( "TestType" );

    Supplier dbSupplier = new Supplier();
    dbSupplier.setId( new Long( 1 ) );
    dbSupplier.setSupplierName( "TestServiceSupplier" );
    dbSupplier.setSupplierType( "TestType" );

    // insert a new supplier
    mockSupplierDAO.expects( once() ).method( "getSupplierByName" ).with( same( supplier.getSupplierName() ) ).will( returnValue( dbSupplier ) );

    try
    {
      // test the SupplierService.saveSupplier
      supplierService.saveSupplier( supplier );
    }
    catch( UniqueConstraintViolationException e )
    {
      return;
    }
    catch( ServiceErrorException e )
    {
      return;
    }

    fail( "Should have thrown UniqueConstraintViolationException" );
  }

  /**
   * Test adding duplicate supplier through the service.
   */
  public void testSaveSupplierUpdateConstraintViolation()
  {
    // Create the test Supplier.
    Supplier supplier = new Supplier();
    supplier.setId( new Long( 1 ) );
    supplier.setSupplierName( "TestServiceSupplier" );
    supplier.setSupplierType( "TestType" );

    Supplier dbSupplier = new Supplier();
    dbSupplier.setId( new Long( 2 ) );
    dbSupplier.setSupplierName( "TestServiceSupplier" );
    dbSupplier.setSupplierType( "TestType" );

    // insert a new supplier
    mockSupplierDAO.expects( once() ).method( "getSupplierByName" ).with( same( supplier.getSupplierName() ) ).will( returnValue( dbSupplier ) );

    try
    {
      // test the SupplierService.saveSupplier
      supplierService.saveSupplier( supplier );
    }
    catch( UniqueConstraintViolationException e )
    {
      return;
    }
    catch( ServiceErrorException se )
    {
      return;
    }

    fail( "Should have thrown UniqueConstraintViolationException" );
  }

  /**
   * Test get all Supplier returns at least 1 Supplier
   */
  public void testGetAllSuppliers()
  {
    List<Supplier> suppliers = new ArrayList<>();
    suppliers.add( new Supplier() );
    mockSupplierDAO.expects( once() ).method( "getAll" ).will( returnValue( suppliers ) );

    List returnedSuppliers = supplierService.getAll();
    assertTrue( returnedSuppliers.size() > 0 );
  }
}