/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/product/impl/ProductCategoryServiceImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.product.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * ProductCategoryServiceImplTest.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCategoryServiceImplTest extends MockObjectTestCase
{

  /** ProductCategoryService */
  private ProductCategoryService productCategoryService = new ProductCategoryServiceImpl();

  private Mock mockProductCategoryDAO;
  private Mock mockPromotionPayoutDAO;
  private Mock mockSystemVariableService = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockProductCategoryDAO = new Mock( ProductCategoryDAO.class );
    ( (ProductCategoryServiceImpl)productCategoryService ).setProductCategoryDAO( (ProductCategoryDAO)mockProductCategoryDAO.proxy() );
    mockPromotionPayoutDAO = new Mock( PromotionPayoutDAO.class );
    ( (ProductCategoryServiceImpl)productCategoryService ).setPromotionPayoutDAO( (PromotionPayoutDAO)mockPromotionPayoutDAO.proxy() );
    mockSystemVariableService = new Mock( SystemVariableService.class );
    ( (ProductCategoryServiceImpl)productCategoryService ).setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
  }

  /**
   * Test saving the Root ProductCategory in database
   * 
   * @throws Exception
   */
  public void testSaveRootProductCategory() throws Exception
  {
    // test saving a root category
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );

    mockProductCategoryDAO.expects( once() ).method( "getMasterCategoryByName" ).with( same( productCategory.getName() ) ).will( returnValue( null ) );
    mockProductCategoryDAO.expects( once() ).method( "saveProductCategory" ).with( same( productCategory ) );

    productCategoryService.saveProductCategory( productCategory, null );

    mockProductCategoryDAO.verify();
  }

  /**
   * Test saving the Root ProductCategory with duplicate name in database
   */
  public void testSaveDuplicateRootProductCategory()
  {
    // test saving a root category
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    ProductCategory duplicate = getRootProductCategoryDomainObject( "1" );
    duplicate.setId( new Long( 2 ) );

    mockProductCategoryDAO.expects( once() ).method( "getMasterCategoryByName" ).with( same( productCategory.getName() ) ).will( returnValue( duplicate ) );
    try
    {
      productCategoryService.saveProductCategory( productCategory, null );
      fail( "Should have thrown UniqueConstraintViolationException" );
    }
    catch( UniqueConstraintViolationException e )
    {
      // worked as expected
    }

    mockProductCategoryDAO.verify();
  }

  /**
   * Test saving the Child ProductCategory in database
   * 
   * @throws Exception
   */
  public void testSaveChildProductCategory() throws Exception
  {
    // test saving a child category
    ProductCategory rootCategory = getRootProductCategoryDomainObject( "1" );
    // List existingChildren = getChildCategoryList(rootCategory);

    ProductCategory childCategory = getChildProductCategoryDomainObject( "100", rootCategory );

    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( rootCategory.getId() ) ).will( returnValue( rootCategory ) );
    mockProductCategoryDAO.expects( once() ).method( "saveProductCategory" ).with( same( childCategory ) );

    productCategoryService.saveProductCategory( childCategory, rootCategory.getId() );

    mockProductCategoryDAO.verify();
  }

  /**
   * Test saving the Child ProductCategory with duplicate name in database
   */
  public void testSaveDuplicateChildProductCategory()
  {
    // test saving a child category with duplicate name
    ProductCategory rootCategory = getRootProductCategoryDomainObject( "1" );
    // List existingChildren = getChildCategoryList(rootCategory);

    ProductCategory childCategory = getChildProductCategoryDomainObject( "1", rootCategory );

    rootCategory.addSubcategory( childCategory );

    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( rootCategory.getId() ) ).will( returnValue( rootCategory ) );
    try
    {
      productCategoryService.saveProductCategory( childCategory, rootCategory.getId() );
      fail( "Should have thrown UniqueConstraintViolationException" );
    }
    catch( UniqueConstraintViolationException e )
    {
      // worked as expected
    }

    mockProductCategoryDAO.verify();
  }

  /**
   * Test delete the product category from database / public void testDeleteRootProductCategory( ) { //
   * test deleting a category ProductCategory rootCategory =
   * getRootProductCategoryDomainObject("1"); List treeList = new ArrayList();
   * treeList.add(rootCategory); mockProductCategoryDAO.expects( once() ).method(
   * "getProductCategoryTree" ).with( same( rootCategory.getId() ) ).will( returnValue( treeList ) );
   * mockPromotionPayoutDAO.expects( once() ).method( "isProductCategoryAssignedToPayout" ).with(
   * same( rootCategory.getId() ) ).will( returnValue( false ) ); mockProductCategoryDAO.expects(
   * once() ).method( "getProductCategoryById" ).with( same( rootCategory.getId() ) ).will(
   * returnValue( rootCategory ) ); mockProductCategoryDAO.expects( once() ).method(
   * "deleteProductCategory" ).with( same( rootCategory ) ); try {
   * productCategoryService.deleteProductCategory(rootCategory.getId());
   * mockProductCategoryDAO.verify(); } catch( ServiceErrorException e ) { fail("Delete of
   * RootCategory failed"); } } /** Test delete the product category with products from database /
   * public void testDeleteRootProductCategoryWithProducts( ) { // test saving a child category
   * ProductCategory rootCategory = getRootProductCategoryDomainObject("1"); List rootChildren =
   * getChildCategoryList(rootCategory); // attach a product to first child ProductCategory child =
   * (ProductCategory)rootChildren.get(0); Set productSet = new HashSet(); Product product = new
   * Product(); productSet.add(product); child.setProducts(productSet); List treeList = new
   * ArrayList(); treeList.add(rootCategory); treeList.addAll(rootChildren);
   * mockProductCategoryDAO.expects( once() ).method( "getProductCategoryTree" ).with( same(
   * rootCategory.getId() ) ).will( returnValue( treeList ) ); mockPromotionPayoutDAO.expects(
   * once() ).method( "isProductCategoryAssignedToPayout" ).with( same( rootCategory.getId() )
   * ).will( returnValue( false ) ); try {
   * productCategoryService.deleteProductCategory(rootCategory.getId()); fail("Delete of
   * RootCategoryWithProducts failed"); } catch( ServiceErrorException e ) { // test successfull }
   * mockProductCategoryDAO.verify(); } /** Test delete the product category with products from
   * database / public void testDeleteRootProductCategoryWithPromotions( ) { // test deleting a
   * category ProductCategory rootCategory = getRootProductCategoryDomainObject("1"); List treeList =
   * new ArrayList(); treeList.add(rootCategory); //build the PromotionPayout PromotionPayout
   * promotionPayout = getPromotionPayoutDomainObject( "1" ); promotionPayout.setProductCategory(
   * rootCategory ); List promotionPayoutList = new ArrayList(); promotionPayoutList.add(
   * promotionPayout ); mockProductCategoryDAO.expects( once() ).method( "getProductCategoryTree"
   * ).with( same( rootCategory.getId() ) ).will( returnValue( treeList ) );
   * mockPromotionPayoutDAO.expects( once() ).method( "isProductCategoryAssignedToPayout" ).with(
   * same( rootCategory.getId() ) ).will( returnValue( true ) ); try {
   * productCategoryService.deleteProductCategory(rootCategory.getId()); fail("Delete of
   * RootCategoryWithProducts failed"); } catch( ServiceErrorException e ) { // test successfull }
   * mockProductCategoryDAO.verify(); }
   */

  /**
   * Test delete the product category from database
   */
  public void testDeleteProductCategory()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );

    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( productCategory.getId() ) ).will( returnValue( productCategory ) );

    PropertySetItem pcProperty = new PropertySetItem();
    pcProperty.setBooleanVal( Boolean.TRUE );
    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.INSTALL_PRODUCTCLAIMS ) ).will( returnValue( pcProperty ) );

    mockPromotionPayoutDAO.expects( once() ).method( "isProductCategoryAssignedToPayout" ).with( same( productCategory.getId() ) ).will( returnValue( false ) );

    mockProductCategoryDAO.expects( once() ).method( "deleteProductCategory" ).with( same( productCategory ) );

    try
    {
      productCategoryService.deleteProductCategory( productCategory.getId() );
      mockProductCategoryDAO.verify();
    }
    catch( ServiceErrorException e )
    {
      fail( "Delete of ProductCategory failed." );
    }
  }

  /**
   * Test delete the product category from database
   */
  public void testDeleteProductCategoryWithSubcategory()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    this.addSubProductCategory( "5", productCategory );

    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( productCategory.getId() ) ).will( returnValue( productCategory ) );

    try
    {
      productCategoryService.deleteProductCategory( productCategory.getId() );
      fail( "Delete DeleteProductCategoryWithSubcategory failed." );
    }
    catch( ServiceErrorException e )
    {
      // test successfull
    }
    mockProductCategoryDAO.verify();
  }

  /**
   * Test delete the product category from database
   */
  public void testDeleteProductCategoryWithProduct()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    Set productSet = new HashSet();
    Product product = getProductDomainObject( "2" );
    productSet.add( product );
    productCategory.setProducts( productSet );

    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( productCategory.getId() ) ).will( returnValue( productCategory ) );

    try
    {
      productCategoryService.deleteProductCategory( productCategory.getId() );
      fail( "Delete of ProductCategoryWithProduct failed." );
    }
    catch( ServiceErrorException e )
    {
      // test successfull
    }

    mockProductCategoryDAO.verify();
  }

  /**
   * Test delete the product category from database
   */
  public void testDeleteProductCategoryBelongsToPromotionPayout()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );

    // ArrayList promotionPayoutList = (ArrayList)this.getPromotionPayoutList( "2", productCategory
    // );

    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( productCategory.getId() ) ).will( returnValue( productCategory ) );
    PropertySetItem pcProperty = new PropertySetItem();
    pcProperty.setBooleanVal( Boolean.TRUE );
    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.INSTALL_PRODUCTCLAIMS ) ).will( returnValue( pcProperty ) );
    mockPromotionPayoutDAO.expects( once() ).method( "isProductCategoryAssignedToPayout" ).with( same( productCategory.getId() ) ).will( returnValue( true ) );

    try
    {
      productCategoryService.deleteProductCategory( productCategory.getId() );
      fail( "Delete of ProductCategoryBelongsToPromotionPayout." );
    }
    catch( ServiceErrorException e )
    {
      // test successfull
    }

    mockProductCategoryDAO.verify();
  }

  /**
   * get the product category by its id
   */
  public void testGetProductCategoryById()
  {
    ProductCategory rootCategory = getRootProductCategoryDomainObject( "1" );
    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( rootCategory.getId() ) ).will( returnValue( rootCategory ) );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    productCategoryService.getProductCategoryById( rootCategory.getId(), associationRequestCollection );
    mockProductCategoryDAO.verify();
  }

  /**
   * Get list of all master (root) product categories
   */
  public void testGetAllMasterCategories()
  {
    List rootCatList = new ArrayList( 3 );
    rootCatList.add( getRootProductCategoryDomainObject( "1" ) );
    rootCatList.add( getRootProductCategoryDomainObject( "2" ) );
    rootCatList.add( getRootProductCategoryDomainObject( "3" ) );
    mockProductCategoryDAO.expects( once() ).method( "getAllMasterCategories" ).will( returnValue( rootCatList ) );
    productCategoryService.getAllMasterCategories();
    mockProductCategoryDAO.verify();
  }

  private ProductCategory getRootProductCategoryDomainObject( String suffix )
  {
    ProductCategory productCategory = new ProductCategory();
    productCategory.setId( new Long( suffix ) );
    productCategory.setName( "testRootCategory" + suffix );
    productCategory.setDescription( "testRootCatgoryDescription" + suffix );
    productCategory.setParentProductCategory( null );
    return productCategory;
  }

  private void addSubProductCategory( String suffix, ProductCategory parentProductCategory )
  {
    Set subProductCategorySet = new HashSet();
    ProductCategory subProductCategory = new ProductCategory();
    subProductCategory.setId( new Long( 5 ) );
    subProductCategorySet.add( subProductCategory );
    parentProductCategory.setProducts( subProductCategorySet );
  }

  private ProductCategory getChildProductCategoryDomainObject( String suffix, ProductCategory parentProductCategory )
  {
    ProductCategory productCategory = new ProductCategory();
    productCategory.setName( "testCategory" + suffix );
    productCategory.setDescription( "testCatgoryDescription" + suffix );
    productCategory.setParentProductCategory( parentProductCategory );
    return productCategory;
  }

  /*
   * private List getChildCategoryList( ProductCategory parentProductCategory ) { List childList =
   * new ArrayList(); ProductCategory childCategory = getChildProductCategoryDomainObject( "1",
   * parentProductCategory ); childList.add( childCategory );
   * childCategory.setParentProductCategory( parentProductCategory ); childCategory =
   * getChildProductCategoryDomainObject( "2", parentProductCategory ); childList.add( childCategory
   * ); childCategory.setParentProductCategory( parentProductCategory ); childCategory =
   * getChildProductCategoryDomainObject( "3", parentProductCategory ); childList.add( childCategory
   * ); childCategory.setParentProductCategory( parentProductCategory ); return childList; }
   */

  private Product getProductDomainObject( String suffix )
  {
    Product product = new Product();
    product.setId( new Long( 1 ) );
    return product;
  }

  /*
   * private List getPromotionPayoutList( String suffix, ProductCategory productCategory ) { //
   * build the PromotionPayout PromotionPayout promotionPayout = new PromotionPayout();
   * promotionPayout.setId( new Long( suffix ) ); promotionPayout.setProductCategory(
   * productCategory ); List promotionPayoutList = new ArrayList(); promotionPayoutList.add(
   * promotionPayout ); return promotionPayoutList; }
   */
}
