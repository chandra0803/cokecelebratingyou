/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/promotion/impl/PromotionPayoutServiceImplTest.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.utils.GuidUtils;

/**
 * PromotionPayoutServiceImplTest.
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
public class PromotionPayoutServiceImplTest extends MockObjectTestCase
{
  private PromotionPayoutServiceImpl promotionPayoutServiceImpl = new PromotionPayoutServiceImpl();

  private Mock mockPromotionPayoutDAO = null;

  private Mock mockPromotionDAO = null;

  private Mock mockProductCategoryDAO = null;

  private Mock mockProductDAO = null;

  // Members for EasyMock

  private PromotionPayoutServiceImpl promotionPayoutServiceImplUnderTest;

  private ProductDAO productDAOMock;
  private PromotionDAO promotionDAOMock;
  private ProductCategoryDAO productCategoryDAOMock;
  private PromotionPayoutDAO promotionPayoutDAOMock;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PromotionPayoutServiceImplTest( String test )
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
    mockPromotionPayoutDAO = new Mock( PromotionPayoutDAO.class );
    promotionPayoutServiceImpl.setPromotionPayoutDAO( (PromotionPayoutDAO)mockPromotionPayoutDAO.proxy() );

    mockPromotionDAO = new Mock( PromotionDAO.class );
    promotionPayoutServiceImpl.setPromotionDAO( (PromotionDAO)mockPromotionDAO.proxy() );

    mockProductCategoryDAO = new Mock( ProductCategoryDAO.class );
    promotionPayoutServiceImpl.setProductCategoryDAO( (ProductCategoryDAO)mockProductCategoryDAO.proxy() );

    mockProductDAO = new Mock( ProductDAO.class );
    promotionPayoutServiceImpl.setProductDAO( (ProductDAO)mockProductDAO.proxy() );

    // EasyMock Setup
    promotionPayoutServiceImplUnderTest = new PromotionPayoutServiceImpl();

    promotionPayoutDAOMock = EasyMock.createMock( PromotionPayoutDAO.class );
    promotionPayoutServiceImplUnderTest.setPromotionPayoutDAO( promotionPayoutDAOMock );

    productDAOMock = EasyMock.createMock( ProductDAO.class );
    promotionPayoutServiceImplUnderTest.setProductDAO( productDAOMock );

    promotionDAOMock = EasyMock.createMock( PromotionDAO.class );
    promotionPayoutServiceImplUnderTest.setPromotionDAO( promotionDAOMock );

    productCategoryDAOMock = EasyMock.createMock( ProductCategoryDAO.class );
    promotionPayoutServiceImplUnderTest.setProductCategoryDAO( productCategoryDAOMock );

  }

  /**
   * Test to save a product.
   */
  public void testSave()
  {
    // create a product object
    PromotionPayout expectedPromotionPayout = buildPromotionPayout( null );
    expectedPromotionPayout.setId( new Long( 1 ) );

    // create a product object
    Promotion expectedPromotion = new ProductClaimPromotion();
    expectedPromotion.setId( new Long( 2 ) );

    // create a product object
    ProductCategory expectedProductCategory = new ProductCategory();
    expectedProductCategory.setId( new Long( 10 ) );

    // create a product object
    Product expectedProduct = new Product();
    expectedProduct.setId( new Long( 3 ) );

    // ProductDAO expected to call save once with the Product
    // which will return the Product expected
    // mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( expectedPromotion
    // .getId() ) ).will( returnValue( expectedPromotion ) );

    // ProductDAO expected to call save once with the Product
    // which will return the Product expected
    mockProductCategoryDAO.expects( once() ).method( "getProductCategoryById" ).with( same( expectedProductCategory.getId() ) ).will( returnValue( expectedProductCategory ) );

    // ProductDAO expected to call save once with the Product
    // which will return the Product expected
    mockProductDAO.expects( once() ).method( "getProductById" ).with( same( expectedProduct.getId() ) ).will( returnValue( expectedProduct ) );

    // ProductDAO expected to call save once with the Product
    // which will return the Product expected
    mockPromotionPayoutDAO.expects( once() ).method( "save" ).with( same( expectedPromotionPayout ) ).will( returnValue( expectedPromotionPayout ) );

    // make the service call
    PromotionPayout actualPromotionPayout = promotionPayoutServiceImpl.save( expectedPromotion.getId(), expectedProductCategory.getId(), expectedProduct.getId(), expectedPromotionPayout );

    assertEquals( "Actual PromotionPayout didn't match with what is expected", expectedPromotionPayout, actualPromotionPayout );

    mockPromotionPayoutDAO.verify();
  }

  /**
   * Tests getting the node from the service through the DAO.
   */
  public void testGetById()
  {
    PromotionPayout expectedPromotionPayout = buildPromotionPayout( null );
    expectedPromotionPayout.setId( new Long( 1 ) );

    mockPromotionPayoutDAO.expects( once() ).method( "getPromotionPayoutById" ).with( same( expectedPromotionPayout.getId() ) ).will( returnValue( expectedPromotionPayout ) );

    PromotionPayout actualPromotionPayout = promotionPayoutServiceImpl.getPromotionPayoutById( expectedPromotionPayout.getId() );

    assertEquals( "Actual PromotionPayout does not match to what was expected", expectedPromotionPayout, actualPromotionPayout );

    mockPromotionPayoutDAO.verify();
  }

  /**
   * Tests deleting the hierachy from the database through the service.
   * 
   * @throws Exception
   */
  public void testDelete() throws Exception
  {
    PromotionPayout expectedPromotionPayout = buildPromotionPayout( null );
    expectedPromotionPayout.setId( new Long( 1 ) );

    mockPromotionPayoutDAO.expects( once() ).method( "getPromotionPayoutById" ).with( same( expectedPromotionPayout.getId() ) ).will( returnValue( expectedPromotionPayout ) );

    mockPromotionPayoutDAO.expects( once() ).method( "delete" ).with( same( expectedPromotionPayout ) );

    promotionPayoutServiceImpl.delete( expectedPromotionPayout.getId() );

    mockPromotionPayoutDAO.verify();
  }

  /**
   * Test getting all nodes from the database through the service.
   */
  public void testGetAllPromotionPayouts()
  {
    List<PromotionPayout> expectedList = getPromotionPayoutList();

    mockPromotionPayoutDAO.expects( once() ).method( "getAllPromotionPayouts" ).will( returnValue( expectedList ) );

    List actualList = promotionPayoutServiceImpl.getAllPromotionPayouts();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockPromotionPayoutDAO.verify();
  }

  /**
   * Test deleting a promotionPayoutGroup.
   */
  public void testDeletePromotionPayoutGroup()
  {
    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();

    EasyMock.expect( promotionPayoutDAOMock.getGroupById( promotionPayoutGroup.getId() ) ).andReturn( promotionPayoutGroup );

    promotionPayoutDAOMock.deleteGroup( promotionPayoutGroup );
    EasyMock.expectLastCall().once();

    // TODO need to determine how to Mock a delete call.
    EasyMock.replay( promotionPayoutDAOMock );

    promotionPayoutServiceImplUnderTest.deletePromotionPayoutGroup( promotionPayoutGroup.getId() );

    EasyMock.verify( promotionPayoutDAOMock );

  }

  /**
   * Test saving the promotionPayoutGroup.
   */
  public void testSavePromotionPayoutGroup()
  {

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();

    EasyMock.expect( promotionPayoutDAOMock.saveGroup( promotionPayoutGroup ) ).andReturn( promotionPayoutGroup );

    EasyMock.replay( promotionPayoutDAOMock );

    PromotionPayoutGroup actualPromotionPayoutGroup = promotionPayoutServiceImplUnderTest.savePromotionPayoutGroup( promotionPayoutGroup );

    assertEquals( "Expected promotionPayoutGroup wasn't equals to actual", promotionPayoutGroup, actualPromotionPayoutGroup );

    EasyMock.verify( promotionPayoutDAOMock );

  }

  /**
   * Test saving the promotionPayoutGroup.
   */
  public void testGetPromotionPayoutsByPromotionPayoutGroupId()
  {

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();

    buildPromotionPayoutList( promotionPayoutGroup );

    EasyMock.expect( promotionPayoutDAOMock.getPromotionPayoutsByGroupId( promotionPayoutGroup.getId() ) ).andReturn( promotionPayoutGroup.getPromotionPayouts() );

    EasyMock.replay( promotionPayoutDAOMock );

    List actualPromotionPayouts = promotionPayoutServiceImplUnderTest.getPromotionPayoutsByPromotionPayoutGroupId( promotionPayoutGroup.getId() );

    assertEquals( "Expected promotionPayout list wasn't equals to actual", promotionPayoutGroup.getPromotionPayouts(), actualPromotionPayouts );

    EasyMock.verify( promotionPayoutDAOMock );

  }

  /**
   * Test getting the promotionPayoutGroup by id.
   */
  public void testGetPromotionPayoutGroupById()
  {

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();

    EasyMock.expect( promotionPayoutDAOMock.getGroupById( promotionPayoutGroup.getId() ) ).andReturn( promotionPayoutGroup );

    EasyMock.replay( promotionPayoutDAOMock );

    PromotionPayoutGroup actualPromotionPayoutGroup = promotionPayoutServiceImplUnderTest.getPromotionPayoutGroupById( promotionPayoutGroup.getId() );

    assertEquals( "Expected promotionPayoutGroup wasn't equals to actual", promotionPayoutGroup, actualPromotionPayoutGroup );

    EasyMock.verify( promotionPayoutDAOMock );

  }

  /**
   * Build a list of product domain objects for testing.
   * 
   * @return List
   */
  private List<PromotionPayout> getPromotionPayoutList()
  {
    List<PromotionPayout> promotionPayoutList = new ArrayList<>();

    PromotionPayout promotionPayout1 = buildPromotionPayout( null );
    promotionPayout1.setId( new Long( 1 ) );

    PromotionPayout promotionPayout2 = buildPromotionPayout( null );
    promotionPayout2.setId( new Long( 2 ) );

    PromotionPayout promotionPayout3 = buildPromotionPayout( null );
    promotionPayout3.setId( new Long( 3 ) );

    promotionPayoutList.add( promotionPayout1 );
    promotionPayoutList.add( promotionPayout2 );
    promotionPayoutList.add( promotionPayout3 );

    return promotionPayoutList;

  }

  /**
   * Build a list of promotionPayouts that are associated to the promotionPayoutGroup.
   * 
   * @param promotionPayoutGroup
   * @return List
   */
  private List<PromotionPayout> buildPromotionPayoutList( PromotionPayoutGroup promotionPayoutGroup )
  {
    List<PromotionPayout> promotionPayoutList = new ArrayList<>();

    PromotionPayout promotionPayout1 = buildPromotionPayout( null );
    promotionPayout1.setId( new Long( 1 ) );

    PromotionPayout promotionPayout2 = buildPromotionPayout( null );
    promotionPayout2.setId( new Long( 2 ) );

    PromotionPayout promotionPayout3 = buildPromotionPayout( null );
    promotionPayout3.setId( new Long( 3 ) );

    promotionPayoutList.add( promotionPayout1 );
    promotionPayoutList.add( promotionPayout2 );
    promotionPayoutList.add( promotionPayout3 );

    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );

    return promotionPayoutList;

  }

  /**
   * creates a product domain object
   * 
   * @param productCategory
   * @return promotionPayout
   */
  public static PromotionPayout buildPromotionPayout( ProductCategory productCategory )
  {
    PromotionPayout promotionPayout = new PromotionPayout();
    promotionPayout.setProductCategory( productCategory );

    return promotionPayout;
  }

  /**
   * Builds a promotionPayoutGroup domain object for testing.
   * 
   * @return PromotionPayoutGroup
   */
  private PromotionPayoutGroup buildPromotionPayoutGroup()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setId( new Long( 34 ) );
    promotionPayoutGroup.setQuantity( 12 );
    promotionPayoutGroup.setSubmitterPayout( 4 );
    promotionPayoutGroup.setTeamMemberPayout( new Integer( 432 ) );

    promotionPayoutGroup.setPromotion( PromotionPayoutServiceImplTest.buildProductClaimPromotion( "testPROMOTION" ) );

    // promotionPayoutGroup.addPromotionPayout(PromotionPayoutServiceImplTest.buildPromotionPayout());
    return promotionPayoutGroup;
  }

  /**
   * creates a product domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ProductClaimPromotion buildProductClaimPromotion( String suffix )
  {
    ProductClaimPromotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 323 ) );
    promotion.setName( suffix );
    return promotion;
  }

  /**
   * creates a product domain object
   * 
   * @param suffix
   * @param productCategory
   * @return Product private Product buildProduct( String suffix, ProductCategory productCategory ) {
   *         Product product = new Product(); product.setName( "testProduct" + suffix );
   *         product.setDescription( "testProductDescription" + suffix );
   *         product.setProductCategory( productCategory ); return product; }
   */

  /**
   * creates a product category domain object
   * 
   * @param suffix
   * @return ProductCategory private ProductCategory buildProductCategory( String suffix ) {
   *         ProductCategory productCategory = new ProductCategory(); productCategory.setName(
   *         "testRootCategory" + suffix ); productCategory.setDescription(
   *         "testRootCatgoryDescription" + suffix ); productCategory.setParentProductCategory( null );
   *         return productCategory; }
   */

}
