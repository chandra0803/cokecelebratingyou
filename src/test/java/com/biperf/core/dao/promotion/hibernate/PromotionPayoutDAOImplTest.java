/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/promotion/hibernate/PromotionPayoutDAOImplTest.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.product.hibernate.ProductCategoryDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.GuidUtils;

/**
 * PromotionPayoutDAOImplTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>leep</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutDAOImplTest extends BaseDAOTest
{
  private PromotionDAO promotionDAO;
  private PromotionPayoutDAO promotionPayoutDAO;

  /**
   * Overridden from
   * 
   * @see junit.framework.TestCase#setUp() Sets up the fixture, for example, open a network
   *      connection. This method is called before a test is executed.
   * @throws Exception
   */
  public void setUp() throws Exception
  {
    super.setUp();

    promotionDAO = getPromotionDAO();
    promotionPayoutDAO = getPromotionPayoutDAO();
  }

  /**
   * Uses the ApplicationContextFactory to look up the ProductDAO implementation.
   * 
   * @return ProductDAO
   */
  private PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ProductDAO implementation.
   * 
   * @return ProductDAO
   */
  private PromotionPayoutDAO getPromotionPayoutDAO()
  {
    return (PromotionPayoutDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionPayoutDAO" );
  }

  /**
   * Test saving and getting a promotionPayoutGroup by the Id.
   */
  public void testSaveGetPromotionPayoutGroupByIdThenDelete()
  {

    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    // Create and save the group
    PromotionPayoutGroup expectedGroup = buildPromotionPayoutGroup();
    promotion.addPromotionPayoutGroup( expectedGroup );
    promotionPayoutDAO.saveGroup( expectedGroup );

    // Clear the session
    BaseDAOTest.flushAndClearSession();

    // Get the group by Id
    PromotionPayoutGroup actualGroup = promotionPayoutDAO.getGroupById( expectedGroup.getId() );

    assertEquals( "Expected promotionPayoutGroup wasn't equal to actual", expectedGroup, actualGroup );

    // Clear the session
    BaseDAOTest.flushAndClearSession();

    // Get the ID from the group
    Long expectedGroupId = expectedGroup.getId();

    // Delete the group
    promotionPayoutDAO.deleteGroup( expectedGroup );

    // Clear the session
    BaseDAOTest.flushAndClearSession();

    PromotionPayoutGroup deletedGroup = promotionPayoutDAO.getGroupById( expectedGroupId );

    assertTrue( "The deleted group has been deleted", deletedGroup == null );

  }

  /**
   * Test saving and getting a Child promotionPayoutGroup by the Id.
   */
  public void testSaveGetPromotionPayoutGroupWithParentByIdThenDelete()
  {

    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    // Create and save the parent group
    PromotionPayoutGroup expectedParentGroup = buildPromotionPayoutGroup();
    promotion.addPromotionPayoutGroup( expectedParentGroup );
    promotionPayoutDAO.saveGroup( expectedParentGroup );

    // create the child group and add the parent group to it
    PromotionPayoutGroup expectedGroup = buildPromotionPayoutGroup();
    expectedGroup.setParentPromotionPayoutGroup( expectedParentGroup );
    promotion.addPromotionPayoutGroup( expectedGroup );
    promotionPayoutDAO.saveGroup( expectedGroup );

    // Clear the session
    BaseDAOTest.flushAndClearSession();

    // Get the group by Id
    PromotionPayoutGroup actualGroup = promotionPayoutDAO.getGroupById( expectedGroup.getId() );

    assertEquals( "Expected promotionPayoutGroup wasn't equal to actual", expectedGroup, actualGroup );
    assertEquals( "Expected Parent Payout Group wasn't equal to actual", expectedGroup.getParentPromotionPayoutGroup(), actualGroup.getParentPromotionPayoutGroup() );

    // Clear the session
    BaseDAOTest.flushAndClearSession();

    // Get the ID from the group
    Long expectedGroupId = expectedGroup.getId();

    // Delete the group
    promotionPayoutDAO.deleteGroup( expectedGroup );

    // Clear the session
    BaseDAOTest.flushAndClearSession();

    PromotionPayoutGroup deletedGroup = promotionPayoutDAO.getGroupById( expectedGroupId );

    assertTrue( "The deleted group has been deleted", deletedGroup == null );

  }

  /**
   * Test getting a list of promotionPayouts assigned to a promtionPayoutGroup.
   */
  public void testGetPromotionPayoutsByPromotionPayoutGroupId()
  {

    String testString = "TEST" + ( System.currentTimeMillis() % 3243242 );

    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( testString );

    Product product = ProductDAOImplTest.buildStaticProductDomainObject( testString, productCategory );

    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    // Build a PromotionPayoutGroup and add promotionPayouts
    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    promotionPayoutGroup.addPromotionPayout( buildPromotionPayout( product ) );
    promotionPayoutGroup.addPromotionPayout( buildPromotionPayout( product ) );
    promotionPayoutGroup.addPromotionPayout( buildPromotionPayout( product ) );

    promotionPayoutDAO.saveGroup( promotionPayoutGroup );

    List actualPromotionPayouts = promotionPayoutDAO.getPromotionPayoutsByGroupId( promotionPayoutGroup.getId() );

    assertEquals( "Expected PromotionProduct Set was not equals to actual", promotionPayoutGroup.getPromotionPayouts(), actualPromotionPayouts );

  }

  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testSaveAndGetById()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    // create a new product
    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();

    PromotionPayout expectedPromotionPayout = buildPromotionPayout();
    expectedPromotionPayout.setProductCategory( ProductCategoryDAOImplTest.buildProductCategory( "52" ) );
    expectedPromotionPayout.setProduct( ProductDAOImplTest.buildStaticProductDomainObject( "53", expectedPromotionPayout.getProductCategory() ) );

    promotionPayoutGroup.addPromotionPayout( expectedPromotionPayout );

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    promotionPayoutDAO.saveGroup( promotionPayoutGroup );

    assertEquals( "Actual expectedPromotionPayout doesn't match with expected", expectedPromotionPayout, promotionPayoutDAO.getPromotionPayoutById( expectedPromotionPayout.getId() ) );

    // do an update on the saved promotion payout group
    promotionPayoutGroup.setQuantity( 150 );
    promotionPayoutDAO.saveGroup( promotionPayoutGroup );

    flushAndClearSession();

    // retrieve the product
    PromotionPayout actualPromotionPayout = promotionPayoutDAO.getPromotionPayoutById( expectedPromotionPayout.getId() );

    assertDomainObjectEquals( "Actual expectedPromotionPayout doesn't match with expected.", expectedPromotionPayout, actualPromotionPayout );

    // Confirm objects are retrieved
    assertEquals( "Actual promotionPayoutGroup doesn't match with expected.", expectedPromotionPayout.getPromotionPayoutGroup(), actualPromotionPayout.getPromotionPayoutGroup() );

    assertEquals( "Actual productCategory doesn't match with expected.", expectedPromotionPayout.getProductCategory(), actualPromotionPayout.getProductCategory() );

    assertEquals( "Actual product doesn't match with expected.", expectedPromotionPayout.getProduct(), actualPromotionPayout.getProduct() );

  }

  /**
   * Test for delete the product from the database.
   */
  public void testDelete()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    PromotionPayout promotionPayout = new PromotionPayout();
    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    promotionPayoutDAO.saveGroup( promotionPayoutGroup );

    promotionPayout.setProductCategory( ProductCategoryDAOImplTest.buildProductCategory( "52" ) );
    promotionPayout.setProduct( ProductDAOImplTest.buildStaticProductDomainObject( "53", ProductCategoryDAOImplTest.buildProductCategory( "52" ) ) );
    Calendar calendar = Calendar.getInstance();
    calendar.add( Calendar.YEAR, -1 );
    promotionPayout.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout.setProductOrCategoryEndDate( new Date() );

    promotionPayoutGroup.addPromotionPayout( promotionPayout );

    promotionPayoutDAO.save( promotionPayout );

    flushAndClearSession();

    // Ensure the product has been saved to the database.
    PromotionPayout promotionPayoutFromDB = promotionPayoutDAO.getPromotionPayoutById( promotionPayout.getId() );

    assertEquals( "promotionPayout was not saved to the database as expected", promotionPayout, promotionPayoutFromDB );

    flushAndClearSession();

    Long promotionPayoutId = promotionPayoutFromDB.getId();
    promotionPayoutDAO.delete( promotionPayoutFromDB );

    flushAndClearSession();

    // check if the promotionPayout got deleted
    assertTrue( "promotionPayout was not deleted from the database.", promotionPayoutDAO.getPromotionPayoutById( promotionPayoutId ) == null );
  }

  /**
   * Test getting all products in the database.
   */
  public void testGetAllProducts()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( getUniqueString() );
    promotionDAO.save( promotion );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    promotionPayoutDAO.saveGroup( promotionPayoutGroup );

    List expectedList = new ArrayList();

    ProductCategory productCategory = ProductCategoryDAOImplTest.buildProductCategory( getUniqueString() );
    Product product = ProductDAOImplTest.buildStaticProductDomainObject( getUniqueString(), productCategory );

    PromotionPayout promotionPayout1 = new PromotionPayout();
    promotionPayout1.setPromotionPayoutGroup( promotionPayoutGroup );
    promotionPayout1.setProductCategory( productCategory );
    promotionPayout1.setProduct( product );
    Calendar calendar = Calendar.getInstance();
    calendar.add( Calendar.YEAR, -1 );
    promotionPayout1.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout1.setProductOrCategoryEndDate( new Date() );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );
    promotionPayoutDAO.save( promotionPayout1 );

    PromotionPayout promotionPayout2 = new PromotionPayout();
    promotionPayout2.setPromotionPayoutGroup( promotionPayoutGroup );
    promotionPayout2.setProductCategory( productCategory );
    promotionPayout2.setProduct( product );
    calendar.add( Calendar.YEAR, -1 );
    promotionPayout2.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout2.setProductOrCategoryEndDate( new Date() );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    promotionPayoutDAO.save( promotionPayout2 );

    PromotionPayout promotionPayout3 = new PromotionPayout();
    promotionPayout3.setPromotionPayoutGroup( promotionPayoutGroup );
    promotionPayout3.setProductCategory( productCategory );
    promotionPayout3.setProduct( product );
    calendar.add( Calendar.YEAR, -1 );
    promotionPayout3.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout3.setProductOrCategoryEndDate( new Date() );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );
    promotionPayoutDAO.save( promotionPayout3 );

    expectedList.add( promotionPayout1 );
    expectedList.add( promotionPayout2 );
    expectedList.add( promotionPayout3 );

    flushAndClearSession();

    List actualList = promotionPayoutDAO.getAllPromotionPayouts();

    assertTrue( "The list of products from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );

  }

  /**
   * Creates a product category domain object
   * 
   * @return ProductCategory
   */
  public static PromotionPayout buildPromotionPayout()
  {

    PromotionPayout promotionPayout = new PromotionPayout();
    Calendar calendar = Calendar.getInstance();
    calendar.add( Calendar.YEAR, -1 );

    promotionPayout.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout.setProductOrCategoryEndDate( new Date() );
    return promotionPayout;

  }

  /**
   * Builds a promotionPayout with a specific product.
   * 
   * @param product
   * @return PromotionPayout
   */
  public static PromotionPayout buildPromotionPayout( Product product )
  {

    PromotionPayout promotionPayout = buildPromotionPayout();
    promotionPayout.setProduct( product );

    Calendar calendar = Calendar.getInstance();
    calendar.add( Calendar.YEAR, -1 );
    promotionPayout.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout.setProductOrCategoryEndDate( new Date() );

    return promotionPayout;
  }

  /**
   * Builds a promotionPayoutGroup from scratch.
   * 
   * @return PromotionPayoutGroup
   */
  public static PromotionPayoutGroup buildPromotionPayoutGroup()
  {
    return PromotionPayoutDAOImplTest.buildPromotionPayoutGroup( true );
  }

  /**
   * Builds a promotionPayoutGroup from scratch.
   * 
   * @param withPromotion
   * @return PromotionPayoutGroup
   */
  public static PromotionPayoutGroup buildPromotionPayoutGroup( boolean withPromotion )
  {
    String uniqueString = "Test" + ( System.currentTimeMillis() % 3122131 );

    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();
    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setQuantity( 293 );
    promotionPayoutGroup.setSubmitterPayout( 2 );
    promotionPayoutGroup.setTeamMemberPayout( new Integer( 62 ) );
    promotionPayoutGroup.setRetroPayout( new Boolean( false ) );

    if ( withPromotion )
    {
      // Assign the promotion.
      promotionPayoutGroup.setPromotion( PromotionDAOImplTest.buildProductClaimPromotion( uniqueString ) );
    }

    return promotionPayoutGroup;
  }

  /**
   * @param productCategory
   * @return PromotionPayout
   */
  public static PromotionPayout buildPromotionPayout( ProductCategory productCategory )
  {
    PromotionPayout promotionPayout = buildPromotionPayout();
    promotionPayout.setProductCategory( productCategory );

    Calendar calendar = Calendar.getInstance();
    calendar.add( Calendar.YEAR, -1 );
    promotionPayout.setProductOrCategoryStartDate( calendar.getTime() );
    promotionPayout.setProductOrCategoryEndDate( new Date() );

    return promotionPayout;
  }

}
