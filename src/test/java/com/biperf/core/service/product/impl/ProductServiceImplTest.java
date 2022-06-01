/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/product/impl/ProductServiceImplTest.java,v $
 */

package com.biperf.core.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.system.SystemVariableService;

/**
 * ProductServiceImplTest.
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
 * <td>kumars</td>
 * <td>June 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductServiceImplTest extends MockObjectTestCase
{
  private ProductServiceImpl productServiceImpl = new ProductServiceImpl();

  private Mock mockProductDAO = null;
  private Mock mockProductCategoryDAO = null;
  private Mock mockPromotionPayoutDAO = null;
  private Mock mockSystemVariableService = null;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ProductServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockProductDAO = new Mock( ProductDAO.class );
    productServiceImpl.setProductDAO( (ProductDAO)mockProductDAO.proxy() );

    mockProductCategoryDAO = new Mock( ProductCategoryDAO.class );
    productServiceImpl.setProductCategoryDAO( (ProductCategoryDAO)mockProductCategoryDAO.proxy() );

    mockPromotionPayoutDAO = new Mock( PromotionPayoutDAO.class );
    productServiceImpl.setPromotionPayoutDAO( (PromotionPayoutDAO)mockPromotionPayoutDAO.proxy() );

    mockSystemVariableService = new Mock( SystemVariableService.class );
    productServiceImpl.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
  }

  /**
   * Test to save a product.
   * 
   * @throws UniqueConstraintViolationException
   */
  public void testSave() throws UniqueConstraintViolationException
  {
    // create a product object
    Product expectedProduct = getProductDomainObject( "100", null );
    expectedProduct.setId( new Long( 1 ) );

    // create a product object
    ProductCategory expectedProductCategory = new ProductCategory();
    expectedProductCategory.setId( new Long( 1 ) );

    // ProductDAO expected to call save once with the Product
    // which will return the Product expected
    mockProductCategoryDAO.expects( atLeastOnce() ).method( "getProductCategoryById" ).with( same( expectedProductCategory.getId() ) ).will( returnValue( expectedProductCategory ) );

    mockProductDAO.expects( once() ).method( "isProductNameUnique" ).will( returnValue( true ) );

    // ProductDAO expected to call save once with the Product
    // which will return the Product expected
    mockProductDAO.expects( once() ).method( "save" ).with( same( expectedProduct ) ).will( returnValue( expectedProduct ) );

    // make the service call
    Product actualProduct = productServiceImpl.save( expectedProductCategory.getId(), expectedProduct );

    assertEquals( "Actual product didn't match with what is expected", expectedProduct, actualProduct );

    mockProductDAO.verify();
  }

  /**
   * Tests getting the product by id.
   */
  public void testGetById()
  {
    Product expectedProduct = getProductDomainObject( "100", null );
    expectedProduct.setId( new Long( 1 ) );

    mockProductDAO.expects( once() ).method( "getProductById" ).with( same( expectedProduct.getId() ) ).will( returnValue( expectedProduct ) );

    Product actualProduct = productServiceImpl.getProductById( expectedProduct.getId() );

    assertEquals( "Actual Product does not match to what was expected", expectedProduct, actualProduct );

    mockProductDAO.verify();
  }

  /**
   * Tests getting the product by name and category id.
   */
  public void testGetByNameAndCategoryId()
  {
    Product expectedProduct = getProductDomainObject( "100", getProductCategoryDomainObject( "13" ) );
    expectedProduct.setId( new Long( 1 ) );

    mockProductDAO.expects( once() ).method( "getProductByNameAndCategoryId" ).with( same( expectedProduct.getName() ), same( expectedProduct.getProductCategory().getId() ) )
        .will( returnValue( expectedProduct ) );

    Product actualProduct = productServiceImpl.getProductByNameAndCategoryId( expectedProduct.getName(), expectedProduct.getProductCategory().getId() );

    assertEquals( "Actual Product does not match to what was expected", expectedProduct, actualProduct );

    mockProductDAO.verify();
  }

  /**
   * Tests deleting the product from the database through the service.
   * 
   * @throws Exception
   */
  public void testDelete() throws Exception
  {
    ProductCategory productCategory = getProductCategoryDomainObject( "100" );
    productCategory.setId( new Long( 1 ) );
    productCategory.setParentProductCategory( new ProductCategory() );
    Product expectedProduct = getProductDomainObject( "100", productCategory );
    expectedProduct.setId( new Long( 1 ) );
    expectedProduct.setProductCategory( productCategory );

    PropertySetItem pcProperty = new PropertySetItem();
    pcProperty.setBooleanVal( Boolean.TRUE );

    PropertySetItem refProperty = new PropertySetItem();
    refProperty.setBooleanVal( Boolean.FALSE );

    mockProductDAO.expects( once() ).method( "getProductById" ).with( same( expectedProduct.getId() ) ).will( returnValue( expectedProduct ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.INSTALL_PRODUCTCLAIMS ) ).will( returnValue( pcProperty ) );

    mockPromotionPayoutDAO.expects( once() ).method( "isProductAssignedToPayout" ).with( same( expectedProduct.getId() ) ).will( returnValue( true ) );

    mockPromotionPayoutDAO.expects( once() ).method( "isProductCategoryAssignedToPayout" ).with( same( expectedProduct.getProductCategory().getId() ) ).will( returnValue( true ) );

    mockPromotionPayoutDAO.expects( once() ).method( "isProductCategoryAssignedToPayout" ).with( same( expectedProduct.getProductCategory().getParentProductCategory().getId() ) )
        .will( returnValue( true ) );

    mockProductDAO.expects( once() ).method( "deleteProduct" ).with( same( expectedProduct ) );

    productServiceImpl.deleteProduct( expectedProduct.getId() );

    mockProductDAO.verify();
  }

  /**
   * Test getting all nodes from the database through the service.
   */
  public void testGetAll()
  {
    List expectedList = getProductList();

    mockProductDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedList ) );

    List actualList = productServiceImpl.getAll();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockProductDAO.verify();
  }

  /**
   * Build a list of product domain objects for testing.
   * 
   * @return List
   */
  private List getProductList()
  {
    List productList = new ArrayList();

    Product product1 = getProductDomainObject( "100", null );
    product1.setId( new Long( 1 ) );

    Product product2 = getProductDomainObject( "101", null );
    product2.setId( new Long( 2 ) );

    Product product3 = getProductDomainObject( "102", null );
    product3.setId( new Long( 3 ) );

    productList.add( product1 );
    productList.add( product2 );
    productList.add( product3 );

    return productList;

  }

  /**
   * creates a product domain object
   * 
   * @param suffix
   * @param productCategory
   * @return Product
   */
  private Product getProductDomainObject( String suffix, ProductCategory productCategory )
  {
    Product product = new Product();

    product.setName( "testProduct" + suffix );
    product.setDescription( "testProductDescription" + suffix );
    product.setProductCategory( productCategory );

    return product;
  }

  /**
   * creates a product category domain object
   * 
   * @param suffix
   * @return ProductCategory
   */
  private ProductCategory getProductCategoryDomainObject( String suffix )
  {
    ProductCategory productCategory = new ProductCategory();

    productCategory.setName( "testRootCategory" + suffix );
    productCategory.setDescription( "testRootCatgoryDescription" + suffix );
    productCategory.setParentProductCategory( null );

    return productCategory;
  }

}
