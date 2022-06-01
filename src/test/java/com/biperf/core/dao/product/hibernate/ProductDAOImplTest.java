/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/product/hibernate/ProductDAOImplTest.java,v $
 */

package com.biperf.core.dao.product.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.claim.hibernate.ClaimFormDAOImplTest;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hibernate.CharacteristicDAOImplTest;
import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromoMgrPayoutFreqType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.product.ProductCharacteristic;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * ProductDAOImplTest.
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
 * <td>Sathish</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductDAOImplTest extends BaseDAOTest
{

  /**
   * Uses the ApplicationContextFactory to look up the ProductDAO implementation.
   * 
   * @return ProductDAO
   */
  private ProductDAO getProductDAO()
  {
    return (ProductDAO)ApplicationContextFactory.getApplicationContext().getBean( "productDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ProductCategoryDAO implementation.
   * 
   * @return ProductCategoryDAO
   */
  private ProductCategoryDAO getProductCategoryDAO()
  {
    return (ProductCategoryDAO)ApplicationContextFactory.getApplicationContext().getBean( "productCategoryDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return PromotionDAO
   */
  private PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }

  /**
   * Test getting a list of products by the promotion parameter. TODO complete this test once
   * Promotions are completed public void testGetProductListByPromotion() { String testSuffix =
   * String.valueOf(System.currentTimeMillis() % 9); // Create a product Product product1 =
   * ProductDAOImplTest.buildStaticProductDomainObject(testSuffix,
   * ProductDAOImplTest.getProductCategoryDomainObject(testSuffix)); String testSuffix2 =
   * String.valueOf(System.currentTimeMillis() % 6); // Create a product Product product2 =
   * ProductDAOImplTest.buildStaticProductDomainObject(testSuffix2,
   * ProductDAOImplTest.getProductCategoryDomainObject(testSuffix2)); int productCount = 0;
   * PromotionPayoutGroup promotionPayoutGroup =
   * PromotionPayoutDAOImplTest.buildPromotionPayoutGroup(); Promotion promotion1 =
   * PromotionDAOImplTest.buildProductClaimPromotion( testSuffix );
   * getPromotionDAO().save(promotion1); PromotionPayout promotionPayout = new PromotionPayout();
   * promotionPayout.setPromotionPayoutGroup( promotionPayoutGroup ); promotionPayout.setProduct(
   * product1 ); productCount++; getPromotionPayoutDAO().save( promotionPayout ); PromotionPayout
   * promotionPayout2 = new PromotionPayout(); promotionPayout2.setPromotionPayoutGroup(
   * promotionPayoutGroup ); ProductCategory productCategory = getProductCategoryDomainObject(
   * testSuffix ); productCategory.addProduct(product2); productCount++; // This is a duplicate, but
   * it should only show up once productCategory.addProduct(product1);
   * promotionPayout2.setProductCategory( productCategory ); getPromotionPayoutDAO().save(
   * promotionPayout2 ); // Get the actualList from the database. List actualList =
   * getProductDAO().getProductsByPromotion(promotion1); // Assert the tests were correct.
   * assertEquals(productCount, actualList.size()); }
   */
  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testSaveAndGetById()
  {
    // create a new product
    ProductDAO productDAO = getProductDAO();

    Product expectedProduct = ProductDAOImplTest.buildStaticProductDomainObject( "55", getProductCategoryDomainObject( "100" ) );
    productDAO.save( expectedProduct );

    assertEquals( "Actual product doesn't match with expected", expectedProduct, productDAO.getProductById( expectedProduct.getId() ) );

    // do an update on the saved product
    expectedProduct.setName( "test product Name-UPDATED" );
    productDAO.save( expectedProduct );

    flushAndClearSession();

    // retrieve the product
    Product actualProduct = productDAO.getProductById( expectedProduct.getId() );
    assertDomainObjectEquals( "Actual product doesn't match with expected", expectedProduct, actualProduct );

    // Confirm product characteristics are retrieved
    Set actualProductCharacteristics = actualProduct.getProductCharacteristics();

    assertEquals( 2, actualProductCharacteristics.size() );
    assertTrue( actualProductCharacteristics.contains( expectedProduct.getProductCharacteristics().iterator().next() ) );

  }

  /**
   * Test getting all products in the database.
   */
  public void testGetAllProducts()
  {
    ProductDAO productDAO = getProductDAO();

    List expectedList = new ArrayList();

    ProductCategory category = getProductCategoryDomainObject( "1" );

    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "1", category );
    productDAO.save( product1 );

    Product product2 = ProductDAOImplTest.buildStaticProductDomainObject( "2", category );
    productDAO.save( product2 );

    Product product3 = ProductDAOImplTest.buildStaticProductDomainObject( "3", category );
    productDAO.save( product3 );

    expectedList.add( product1 );
    expectedList.add( product2 );
    expectedList.add( product3 );

    flushAndClearSession();

    List actualList = productDAO.getAll();

    assertTrue( "The list of products from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );

  }

  /**
   * Test getting the products by promotion.
   */
  public void testGetProductsByPromotion()
  {

    String testString = "TEST" + ( System.currentTimeMillis() % 2039421 );
    ProductCategoryDAO productCategoryDAO = getProductCategoryDAO();
    ProductDAO productDAO = getProductDAO();
    PromotionDAO promotionDAO = getPromotionDAO();

    // Build a productCategory
    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( testString );

    // Save the productCategory
    productCategoryDAO.saveProductCategory( productCategory );

    // Build a list of products
    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "1" + testString, productCategory );
    Product product2 = ProductDAOImplTest.buildStaticProductDomainObject( "2" + testString, productCategory );
    Product product3 = ProductDAOImplTest.buildStaticProductDomainObject( "3" + testString, productCategory );

    // Save the list of products
    productDAO.save( product3 );
    productDAO.save( product2 );
    productDAO.save( product1 );

    // Build the expected List of products
    List expectedProductList = new ArrayList();
    expectedProductList.add( product1 );
    expectedProductList.add( product2 );
    expectedProductList.add( product3 );

    // Build a promotion
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( testString );

    // Build a promotionPayoutGroup
    PromotionPayoutGroup promotionPayoutGroup = PromotionPayoutDAOImplTest.buildPromotionPayoutGroup();

    // Assign the promotion to the promotionPayoutGroup
    promotionPayoutGroup.setPromotion( promotion );

    // Build a list of promotionPayouts with the products
    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    PromotionPayout promotionPayout3 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );

    // Assign the promotionPayouts to the promotionPayoutGroup
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    // Save the promotion
    promotionDAO.save( promotion );

    // Get the actual list of products by promotion
    List actualProductList = productDAO.getProductsByPromotion( promotion.getId() );

    // Compare it to the original list of products
    assertTrue( "Actual list didn't contain the expected products", actualProductList.containsAll( expectedProductList ) );

  }

  /**
   * Test getting a product by name and category.
   */
  public void testGetProductByNameAndCategoryId()
  {
    // create a new product
    ProductDAO productDAO = getProductDAO();
    ProductCategory category = getProductCategoryDomainObject( "100" );
    ProductCategory dbCategory = getProductCategoryDAO().saveProductCategory( category );
    Product expectedProduct = ProductDAOImplTest.buildStaticProductDomainObject( "13", dbCategory );
    productDAO.save( expectedProduct );

    Product retrievedProduct = productDAO.getProductByNameAndCategoryId( "testProduct13", dbCategory.getId() );
    assertEquals( "Actual product doesn't match with expected", expectedProduct, retrievedProduct );
  }

  /**
   * creates a product domain object
   * 
   * @param suffix
   * @param productCategory
   * @return Product
   */
  public static Product buildStaticProductDomainObject( String suffix, ProductCategory productCategory )
  {
    Product product = new Product();

    product.setName( "testProduct" + suffix + String.valueOf( System.currentTimeMillis() % 58765186 ) );
    product.setDescription( "testProductDescription" + suffix );
    product.setProductCategory( productCategory );
    product.addProductCharacteristic( getTestProductCharacteristic() );
    product.addProductCharacteristic( getTestProductCharacteristic() );
    return product;
  }

  /**
   * Get a productCharacteristicType.
   * 
   * @return ProductCharacteristicType
   */
  public static ProductCharacteristic getTestProductCharacteristic()
  {
    ProductCharacteristic productCharacteristic = new ProductCharacteristic();

    ProductCharacteristicType expectedChar = new ProductCharacteristicType();
    CharacteristicDAOImplTest.loadCharacteristicValues( expectedChar );

    productCharacteristic.setProductCharacteristicType( expectedChar );

    return productCharacteristic;
  }

  /**
   * creates a product category domain object
   * 
   * @param suffix
   * @return ProductCategory
   */
  public static ProductCategory getProductCategoryDomainObject( String suffix )
  {
    ProductCategory productCategory = new ProductCategory();

    productCategory.setName( "testRootCategory" + suffix );
    productCategory.setDescription( "testRootCatgoryDescription" + suffix );
    productCategory.setParentProductCategory( null );

    return productCategory;
  }

  /**
   * creates a promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static Promotion getProductClaimPromotionDomainObject( String suffix )
  {
    ProductClaimPromotion promotion = new ProductClaimPromotion();

    // Required fields
    promotion.setName( "testPromotion" + suffix );

    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    promotion.setPromotionProcessingMode( PromotionProcessingModeType.lookup( PromotionProcessingModeType.REAL_TIME ) );
    promotion.setPayoutManagerPeriod( PromoMgrPayoutFreqType.lookup( PromoMgrPayoutFreqType.MONTHLY ) );
    promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );

    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    return promotion;
  }

  /**
   * testIsProductNameUnique
   */
  public void testIsProductNameUnique()
  {
    String suffix = String.valueOf( System.currentTimeMillis() % 200299343 );

    // Build a productCategory
    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( suffix );

    // Build a list of products
    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "uniqueProduct" + suffix, productCategory );

    getProductDAO().save( product1 );
    flushAndClearSession();

    // NOTE: The query called by the DAO looks for a lowercase reprensentation of the String passed
    // in.

    // Different name, same product id, same category id
    assertTrue( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( "Some--Unique--N-a-m-e" + suffix, product1.getId(), product1.getProductCategory().getId() ) );

    // Same name, different product id, different category id
    assertTrue( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( product1.getName(), new Long( 34 ), new Long( 99 ) ) );

    // Lowercase (same) name, same product id, same category id
    assertTrue( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( product1.getName().toLowerCase(), product1.getId(), product1.getProductCategory().getId() ) );

    // Uppercase (same) name, same product id, same category id
    assertTrue( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( product1.getName().toUpperCase(), product1.getId(), product1.getProductCategory().getId() ) );

    // Same name, no product id, same category id
    assertFalse( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( product1.getName(), null, product1.getProductCategory().getId() ) );

    // Uppercase (same) name, different product id, same category id
    assertFalse( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( product1.getName().toUpperCase(), new Long( 34 ), product1.getProductCategory().getId() ) );

    // Lowercase (same) name, different product id, same category id
    assertFalse( "The product name is not unique (but should be)", getProductDAO().isProductNameUnique( product1.getName().toLowerCase(), new Long( 34 ), product1.getProductCategory().getId() ) );

  }

  /**
   * Test getting the products by promotion.
   */
  public void testGetProductsByPromotionAndDateRange()
  {

    String testString = "TEST" + ( System.currentTimeMillis() % 2039421 );
    ProductCategoryDAO productCategoryDAO = getProductCategoryDAO();
    ProductDAO productDAO = getProductDAO();
    PromotionDAO promotionDAO = getPromotionDAO();

    // Build a productCategory
    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( testString );

    // Save the productCategory
    productCategoryDAO.saveProductCategory( productCategory );

    // Build a list of products
    Product product1 = ProductDAOImplTest.buildStaticProductDomainObject( "1" + testString, productCategory );
    Product product2 = ProductDAOImplTest.buildStaticProductDomainObject( "2" + testString, productCategory );
    Product product3 = ProductDAOImplTest.buildStaticProductDomainObject( "3" + testString, productCategory );

    // Save the list of products
    productDAO.save( product3 );
    productDAO.save( product2 );
    productDAO.save( product1 );

    // Build the expected List of products
    List expectedProductList = new ArrayList();
    expectedProductList.add( product1 );
    expectedProductList.add( product2 );
    expectedProductList.add( product3 );

    // Build a promotion
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( testString );

    // Build a promotionPayoutGroup
    PromotionPayoutGroup promotionPayoutGroup = PromotionPayoutDAOImplTest.buildPromotionPayoutGroup();

    // Assign the promotion to the promotionPayoutGroup
    promotionPayoutGroup.setPromotion( promotion );

    // Build a list of promotionPayouts with the products
    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    PromotionPayout promotionPayout3 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );
    // added payout dates for product range check

    // Assign the promotionPayouts to the promotionPayoutGroup
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    // Save the promotion
    promotionDAO.save( promotion );

    // Get the actual list of products by promotion
    List actualProductList = productDAO.getProductsByPromotionAndDateRange( promotion.getId() );

    // Compare it to the original list of products
    assertTrue( "Actual list didn't contain the expected products", actualProductList.containsAll( expectedProductList ) );

  }
}
