/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/promotion/engine/CrossSellPayoutStrategyTest.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductCategoryDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.utils.GuidUtils;

/**
 * CrossSellPayoutStrategyTest.
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
 * <td>wadzinsk</td>
 * <td>Aug 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CrossSellPayoutStrategyTest extends BaseServiceTest
{
  Product product1;
  int product1QuantityRequired;

  Product product2;
  int product2QuantityRequired;

  ProductCategory productCategoryProd1AndProd2;

  Product product3;
  int product3QuantityRequired;

  ProductCategory productCategory3;

  Product product4;
  int product4QuantityRequired;

  int payoutGroup1SubmitterPayout;
  int payoutGroup2SubmitterPayout;

  Integer payoutGroup1TeamMemberPayout;
  Integer payoutGroup2TeamMemberPayout;

  private Product productS2OfC3;

  private Product productS1OfC3;

  private int productS1OfC3QuantityRequired;

  private int productS2OfC3QuantityRequired;

  private ProductCategory productSubcategory1OfCat3;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.BaseServiceTest#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    // Build products for promotion and claim
    productCategoryProd1AndProd2 = ProductCategoryDAOImplTest.buildProductCategory( "pc1and2" );
    product1 = ProductDAOImplTest.buildStaticProductDomainObject( "p1", productCategoryProd1AndProd2 );
    product1QuantityRequired = 10;

    product2 = ProductDAOImplTest.buildStaticProductDomainObject( "p2", productCategoryProd1AndProd2 );
    product2QuantityRequired = 20;

    productCategory3 = ProductCategoryDAOImplTest.buildProductCategory( "pc3" );
    product3 = ProductDAOImplTest.buildStaticProductDomainObject( "p3", productCategory3 );
    product3QuantityRequired = 30;

    productSubcategory1OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "pcs1ofc3" );
    productCategory3.addSubcategory( productSubcategory1OfCat3 );
    productS1OfC3 = ProductDAOImplTest.buildStaticProductDomainObject( "ps1ofc3", productSubcategory1OfCat3 );
    productS1OfC3QuantityRequired = 50;

    ProductCategory productSubcategory2OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "pcs2ofc3" );
    productCategory3.addSubcategory( productSubcategory2OfCat3 );
    productS2OfC3 = ProductDAOImplTest.buildStaticProductDomainObject( "ps2ofc3", productSubcategory2OfCat3 );
    productS2OfC3QuantityRequired = 60;

    ProductCategory productCategory4 = ProductCategoryDAOImplTest.buildProductCategory( "pc4" );
    product4 = ProductDAOImplTest.buildStaticProductDomainObject( "p4", productCategory4 );
    product4QuantityRequired = 40;

    payoutGroup1SubmitterPayout = 5;
    payoutGroup2SubmitterPayout = 6;
    payoutGroup1TeamMemberPayout = new Integer( 7 );
    payoutGroup2TeamMemberPayout = new Integer( 8 );

  }

  public void testBasicSubmitterSuccess()
  {
    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( 2, payoutCalculationResult.getContributingActivities().size() );

    assertEquals( 0, payoutCalculationResult.getGeneratedActivities().size() );

  }

  public void testBasicTeamMemberSuccess()
  {
    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    Set activities = buildQualifyingActivitiesForCrossSell( false, promotion );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1TeamMemberPayout.longValue(), calculatedPayout.longValue() );
  }

  public void testBasicSubmitterSuccessWithManagerOverrride()
  {
    ProductClaimPromotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    promotion.setPayoutManager( true );

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts = new SalesFacts( null, null, expectedManager, null );

    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );

    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( 2, payoutCalculationResult.getContributingActivities().size() );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout, managerOverrideActivity.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity.getParticipant() );

  }

  public void testFailureInsufficientQuantity()
  {
    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );

    SalesActivity activity = (SalesActivity)activities.iterator().next();
    long insufficientQuantity = activity.getQuantity().longValue() - 1;
    activity.setQuantity( new Long( insufficientQuantity ) );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );
  }

  public void testNoInputActivities()
  {

    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();

    // Add a sales activity for something other than that which qualifies.
    Set activities = new LinkedHashSet();

    SalesFacts salesFacts = null;
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    try
    {
      crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
      fail( "Should have received BeaconRuntimeException" );
    }
    catch( BeaconRuntimeException e )
    {
      // expected
    }

  }

  public void testFailureClaimIsMissingARequiredProduct()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );
    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();

    // Add a sales activity for something other than that which qualifies.
    Set activities = new LinkedHashSet();
    // Add salesActivity4
    SalesActivity salesActivity4 = new SalesActivity( product4, new Long( product4QuantityRequired ) );
    salesActivity4.setSubmitter( true );
    salesActivity4.setParticipant( uniqueParticipant );

    activities.add( salesActivity4 );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.CROSS_SELL_UNMATCHED_PRODUCT );
  }

  public void testSubmitterPayoutWithRequirementsFilledTwiceOnBoth()
  {
    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );
    for ( Iterator iter = activities.iterator(); iter.hasNext(); )
    {
      SalesActivity activity = (SalesActivity)iter.next();
      long newQuantity = activity.getQuantity().longValue() * 2;
      activity.setQuantity( new Long( newQuantity ) );

    }

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * 2, calculatedPayout.longValue() );
  }

  public void testSubmitterPayoutWithRequirementsFilledThreeTimesOnOneTwiceOnOther()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( product1QuantityRequired ) );
    salesActivity1.setQuantity( new Long( salesActivity1.getQuantity().longValue() * 3 ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product2, new Long( product2QuantityRequired ) );
    salesActivity2.setQuantity( new Long( salesActivity2.getQuantity().longValue() * 2 ) );
    salesActivity2.setSubmitter( true );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * 2, calculatedPayout.longValue() );
  }

  public void testSubmitterTwoPayoutGroupsSuccess()
  {
    Promotion promotion = buildCrossSellPromotionWithTwoPayoutGroups();
    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 2 );

    long totalCalculatedPayout = 0;
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                  payoutCalculationResult.isCalculationSuccessful() );

      assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

      totalCalculatedPayout += payoutCalculationResult.getCalculatedPayout().longValue();
    }

    // Confirm total calculated payout
    assertEquals( payoutGroup1SubmitterPayout + payoutGroup2SubmitterPayout, totalCalculatedPayout );

  }

  public void testSameProductTwiceInClaimSuccess()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroupWithOneProduct();
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( product1QuantityRequired ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product1, new Long( product1QuantityRequired ) );
    salesActivity2.setSubmitter( true );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * 2, calculatedPayout.longValue() );
  }

  /**
   * product that matches a product promotion payout and a product category promotion payout should
   * only get applied to the product count.
   */
  public void testSoldProductMatchesProductandProdCatEntriesInPromotionGroupSuccess()
  {
    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroupProductAndCategory();
    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

  }

  public void testSoldProductMatchesProductCatandProdSubcatEntriesInPromotionGroupSuccess()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroupProductCatAndSubcat();
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( productS1OfC3, new Long( productS1OfC3QuantityRequired ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( productS2OfC3, new Long( productS2OfC3QuantityRequired ) );
    salesActivity2.setSubmitter( true );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    // TODO: add a product from subcat 1 and one from subcat 2.
    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

  }

  public void testZeroPayoutFailure()
  {
    ProductClaimPromotion promotion = buildCrossSellPromotionWithOnePayoutGroup();
    PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)promotion.getPromotionPayoutGroups().iterator().next();
    promotionPayoutGroup.setSubmitterPayout( 0 );

    Set activities = buildQualifyingActivitiesForCrossSell( true, promotion );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    CrossSellPayoutStrategy crossSellPayoutStrategy = new CrossSellPayoutStrategy();

    Set payoutCalculationResults = crossSellPayoutStrategy.processActivities( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_ZERO_PAYOUT_GROUP_PAYOUT );
  }

  private ProductClaimPromotion buildCrossSellPromotionWithOnePayoutGroup()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.CROSS_SELL ) );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup1();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private ProductClaimPromotion buildCrossSellPromotionWithOnePayoutGroupWithOneProduct()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.CROSS_SELL ) );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroupWithOneProduct();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private ProductClaimPromotion buildCrossSellPromotionWithOnePayoutGroupProductAndCategory()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.CROSS_SELL ) );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroupProductAndCategory();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private ProductClaimPromotion buildCrossSellPromotionWithOnePayoutGroupProductCatAndSubcat()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.CROSS_SELL ) );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroupCategoryAndSubcategory();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private ProductClaimPromotion buildCrossSellPromotionWithTwoPayoutGroups()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.CROSS_SELL ) );

    PromotionPayoutGroup promotionPayoutGroup1 = buildPromotionPayoutGroup1();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup1 );

    PromotionPayoutGroup promotionPayoutGroup2 = buildPromotionPayoutGroup2();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup2 );

    return promotion;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroup1()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup1TeamMemberPayout );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    promotionPayout1.setQuantity( product1QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    promotionPayout2.setQuantity( product2QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroupWithOneProduct()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup1TeamMemberPayout );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    promotionPayout1.setQuantity( product1QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroup2()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup2SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup2TeamMemberPayout );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );
    promotionPayout1.setQuantity( product3QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product4 );
    promotionPayout2.setQuantity( product4QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );
    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroupProductAndCategory()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup1TeamMemberPayout );

    PromotionPayout promotionPayoutWithProductCategory = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategoryProd1AndProd2 );
    promotionPayoutWithProductCategory.setQuantity( product1QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayoutWithProductCategory );

    PromotionPayout promotionPayoutWithProduct = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    promotionPayoutWithProduct.setQuantity( product2QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayoutWithProduct );

    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroupCategoryAndSubcategory()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup1TeamMemberPayout );

    PromotionPayout promotionPayoutWithProductCategory = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategory3 );
    promotionPayoutWithProductCategory.setQuantity( productS2OfC3QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayoutWithProductCategory );

    PromotionPayout promotionPayoutWithSubcategory = PromotionPayoutDAOImplTest.buildPromotionPayout( productSubcategory1OfCat3 );
    promotionPayoutWithSubcategory.setQuantity( productS1OfC3QuantityRequired );
    promotionPayoutGroup.addPromotionPayout( promotionPayoutWithSubcategory );

    return promotionPayoutGroup;
  }

  private Set buildQualifyingActivitiesForCrossSell( boolean isSubmitter, Promotion promotion )
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( product1QuantityRequired ) );
    salesActivity1.setSubmitter( isSubmitter );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product2, new Long( product2QuantityRequired ) );
    salesActivity2.setSubmitter( isSubmitter );
    salesActivity2.setPromotion( promotion );
    salesActivity2.setParticipant( uniqueParticipant );

    activities.add( salesActivity2 );

    // Add salesActivity3
    SalesActivity salesActivity3 = new SalesActivity( product3, new Long( product3QuantityRequired ) );
    salesActivity3.setSubmitter( isSubmitter );
    salesActivity3.setPromotion( promotion );
    salesActivity3.setParticipant( uniqueParticipant );
    activities.add( salesActivity3 );

    // Add salesActivity4
    SalesActivity salesActivity4 = new SalesActivity( product4, new Long( product4QuantityRequired ) );
    salesActivity4.setSubmitter( isSubmitter );
    salesActivity4.setPromotion( promotion );
    salesActivity4.setParticipant( uniqueParticipant );
    activities.add( salesActivity4 );

    return activities;
  }

}
