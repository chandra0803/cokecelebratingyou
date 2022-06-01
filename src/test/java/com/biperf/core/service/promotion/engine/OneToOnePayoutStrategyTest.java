/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductCategoryDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.claim.ProductClaim;
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
 * OneToOnePayoutStrategyTest.
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
public class OneToOnePayoutStrategyTest extends BaseServiceTest
{
  Product product1;
  int product1QuantitySold;

  Product product2;
  int product2QuantitySold;

  ProductCategory productCategoryProd1AndProd2;

  Product product3;
  int product3QuantitySold;

  ProductCategory productCategory3;

  Product product4;
  int product4QuantitySold;

  int payoutGroup1SubmitterPayout;
  int payoutGroup2SubmitterPayout;

  Integer payoutGroup1TeamMemberPayout;
  Integer payoutGroup2TeamMemberPayout;

  private Product productS2OfC3;

  private Product productS1OfC3;

  private int productS1OfC3QuantitySold;

  private int productS2OfC3QuantitySold;

  private ProductCategory productSubcategory1OfCat3;
  private Integer minQualifier1;

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
    product1QuantitySold = 10;

    product2 = ProductDAOImplTest.buildStaticProductDomainObject( "p2", productCategoryProd1AndProd2 );
    product2QuantitySold = 20;

    productCategory3 = ProductCategoryDAOImplTest.buildProductCategory( "pc3" );
    product3 = ProductDAOImplTest.buildStaticProductDomainObject( "p3", productCategory3 );
    product3QuantitySold = 30;

    productSubcategory1OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "pcs1ofc3" );
    productCategory3.addSubcategory( productSubcategory1OfCat3 );
    productS1OfC3 = ProductDAOImplTest.buildStaticProductDomainObject( "ps1ofc3", productSubcategory1OfCat3 );
    productS1OfC3QuantitySold = 50;

    ProductCategory productSubcategory2OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "pcs2ofc3" );
    productCategory3.addSubcategory( productSubcategory2OfCat3 );
    productS2OfC3 = ProductDAOImplTest.buildStaticProductDomainObject( "ps2ofc3", productSubcategory2OfCat3 );
    productS2OfC3QuantitySold = 60;

    ProductCategory productCategory4 = ProductCategoryDAOImplTest.buildProductCategory( "pc4" );
    product4 = ProductDAOImplTest.buildStaticProductDomainObject( "p4", productCategory4 );
    product4QuantitySold = 40;

    payoutGroup1SubmitterPayout = 5;
    payoutGroup2SubmitterPayout = 6;
    payoutGroup1TeamMemberPayout = new Integer( 7 );
    payoutGroup2TeamMemberPayout = new Integer( 8 );

    minQualifier1 = new Integer( 5 );

  }

  public void testBasicSubmitterSuccess()
  {
    Promotion promotion = buildOneToOnePromotionWithOnePayoutGroup();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();
    Set activities = buildQualifyingActivitiesForOneToOne( true, promotion, claim );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, calculatedPayout.longValue() );

    assertEquals( 1, payoutCalculationResult.getContributingActivities().size() );

    assertEquals( 0, payoutCalculationResult.getGeneratedActivities().size() );

  }

  public void testBasicTeamMemberSuccess()
  {
    Promotion promotion = buildOneToOnePromotionWithOnePayoutGroup();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();
    Set activities = buildQualifyingActivitiesForOneToOne( false, promotion, claim );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1TeamMemberPayout.intValue() * product1QuantitySold, calculatedPayout.longValue() );
  }

  public void testSubmitterMinQualifierRetroOnSuccessOnOneClaim()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    promotion.setPayoutManager( true );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup1();
    promotionPayoutGroup.setRetroPayout( Boolean.TRUE );
    promotionPayoutGroup.setMinimumQualifier( minQualifier1 );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();
    Set activities = buildQualifyingActivitiesForOneToOne( true, promotion, claim );

    LinkedHashMap minQualifierStatusByPromoPayoutGroup = new LinkedHashMap();

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts = new SalesFacts( null, null, expectedManager, minQualifierStatusByPromoPayoutGroup );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, calculatedPayout.longValue() );

    assertEquals( 1, payoutCalculationResult.getContributingActivities().size() );

    MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus );

    assertTrue( minimumQualifierStatus.isMinQualifierMet() );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, managerOverrideActivity.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity.getParticipant() );
    assertNotNull( managerOverrideActivity.getMinimumQualifierStatus() );
  }

  public void testSubmitterMinQualifierRetroOnSuccessOnSecondClaim()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    promotion.setPayoutManager( true );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup1();
    promotionPayoutGroup.setRetroPayout( Boolean.TRUE );
    promotionPayoutGroup.setMinimumQualifier( new Integer( minQualifier1.intValue() * 4 ) );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    // Claim 1 - should fail (since 0 quantity available after min qualifier quantity removed), but
    // min qualifier should be met.
    Claim claim1 = ClaimDAOImplTest.buildStaticProductClaim();

    Set activitiesClaim1 = buildQualifyingActivitiesForOneToOne( true, promotion, claim1 );

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts1 = new SalesFacts( null, null, expectedManager, new LinkedHashMap() );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResultsClaim1 = payoutStrategy.processActivitiesInternal( activitiesClaim1, promotion, salesFacts1 );
    assertEquals( payoutCalculationResultsClaim1.size(), 1 );

    PayoutCalculationResult payoutCalculationResult1 = (PayoutCalculationResult)payoutCalculationResultsClaim1.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult1.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult1.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult1.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult1.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, calculatedPayout.longValue() );

    assertEquals( 1, payoutCalculationResult1.getContributingActivities().size() );

    MinimumQualifierStatus minimumQualifierStatus1 = payoutCalculationResult1.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus1 );

    assertFalse( minimumQualifierStatus1.isMinQualifierMet() );
    assertEquals( product1QuantitySold, minimumQualifierStatus1.getCompletedQuantity() );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult1.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)payoutCalculationResult1.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, managerOverrideActivity.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity.getParticipant() );
    assertNotNull( managerOverrideActivity.getMinimumQualifierStatus() );

    // Claim 2
    Claim claim2 = ClaimDAOImplTest.buildStaticProductClaim();

    Set activitiesClaim2 = buildQualifyingActivitiesForOneToOne( true, promotion, claim2 );

    LinkedHashMap minQualifierStatusByPromoPayoutGroup = new LinkedHashMap();
    minQualifierStatusByPromoPayoutGroup.put( promotionPayoutGroup, minimumQualifierStatus1 );

    SalesFacts salesFacts2 = new SalesFacts( null, null, expectedManager, minQualifierStatusByPromoPayoutGroup );

    Set payoutCalculationResults2 = payoutStrategy.processActivitiesInternal( activitiesClaim2, promotion, salesFacts2 );
    assertEquals( payoutCalculationResults2.size(), 1 );

    PayoutCalculationResult payoutCalculationResult2 = (PayoutCalculationResult)payoutCalculationResults2.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult2.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult2.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult2.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout2 = payoutCalculationResult2.getCalculatedPayout();
    assertNotNull( calculatedPayout2 );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, calculatedPayout2.longValue() );

    assertEquals( 1, payoutCalculationResult2.getContributingActivities().size() );

    MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult2.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus );

    assertTrue( minimumQualifierStatus.isMinQualifierMet() );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult2.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity2 = (ManagerOverrideActivity)payoutCalculationResult2.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, managerOverrideActivity2.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity2.getParticipant() );
    assertNotNull( managerOverrideActivity2.getMinimumQualifierStatus() );
  }

  public void testSubmitterMinQualifierRetroOffQualifierExceededOnSecondClaim()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    promotion.setPayoutManager( true );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup1();
    promotionPayoutGroup.setRetroPayout( Boolean.FALSE );
    promotionPayoutGroup.setMinimumQualifier( new Integer( minQualifier1.intValue() * 2 ) );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    Claim claim1 = ClaimDAOImplTest.buildStaticProductClaim();

    Set activitiesClaim1 = buildQualifyingActivitiesForOneToOne( true, promotion, claim1 );

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts1 = new SalesFacts( null, null, expectedManager, new LinkedHashMap() );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResultsClaim1 = payoutStrategy.processActivitiesInternal( activitiesClaim1, promotion, salesFacts1 );
    assertEquals( payoutCalculationResultsClaim1.size(), 1 );

    PayoutCalculationResult payoutCalculationResult1 = (PayoutCalculationResult)payoutCalculationResultsClaim1.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult1.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult1.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult1.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MIN_QUALIFIER_NOT_EXCEEDED_RETRO_OFF_FAILURE );

    assertEquals( 1, payoutCalculationResult1.getContributingActivities().size() );

    assertEquals( 0, payoutCalculationResult1.getGeneratedActivities().size() );

    MinimumQualifierStatus minimumQualifierStatus1 = payoutCalculationResult1.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus1 );

    assertTrue( minimumQualifierStatus1.isMinQualifierMet() );
    assertEquals( product1QuantitySold, minimumQualifierStatus1.getCompletedQuantity() );

    // Claim 2
    Claim claim2 = ClaimDAOImplTest.buildStaticProductClaim();

    Set activitiesClaim2 = buildQualifyingActivitiesForOneToOne( true, promotion, claim2 );

    LinkedHashMap minQualifierStatusByPromoPayoutGroup = new LinkedHashMap();
    minQualifierStatusByPromoPayoutGroup.put( promotionPayoutGroup, minimumQualifierStatus1 );

    SalesFacts salesFacts2 = new SalesFacts( null, null, expectedManager, minQualifierStatusByPromoPayoutGroup );

    Set payoutCalculationResults2 = payoutStrategy.processActivitiesInternal( activitiesClaim2, promotion, salesFacts2 );
    assertEquals( payoutCalculationResults2.size(), 1 );

    PayoutCalculationResult payoutCalculationResult2 = (PayoutCalculationResult)payoutCalculationResults2.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult2.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult2.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult2.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout2 = payoutCalculationResult2.getCalculatedPayout();
    assertNotNull( calculatedPayout2 );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, calculatedPayout2.longValue() );

    assertEquals( 1, payoutCalculationResult2.getContributingActivities().size() );

    MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult2.getMinimumQualifierStatus();
    assertNull( minimumQualifierStatus );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult2.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity2 = (ManagerOverrideActivity)payoutCalculationResult2.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, managerOverrideActivity2.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity2.getParticipant() );
    assertNull( managerOverrideActivity2.getMinimumQualifierStatus() );

  }

  public void testTeamMemberMinQualifierRetroOffFailure()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );
    promotion.setPayoutManager( true );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup1();
    promotionPayoutGroup.setRetroPayout( Boolean.FALSE );
    promotionPayoutGroup.setMinimumQualifier( minQualifier1 );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    ProductClaim claim = ClaimDAOImplTest.buildStaticProductClaim();
    claim.setMinimumQualifierMet( Boolean.FALSE );
    Set activities = buildQualifyingActivitiesForOneToOne( false, promotion, claim );

    LinkedHashMap minQualifierStatusByPromoPayoutGroup = new LinkedHashMap();

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts = new SalesFacts( null, null, expectedManager, minQualifierStatusByPromoPayoutGroup );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MIN_QUALIFIER_NOT_EXCEEDED_RETRO_OFF_TEAM_MEMBER_FAILURE );

    assertEquals( 1, payoutCalculationResult.getContributingActivities().size() );

    MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus );
    assertFalse( minimumQualifierStatus.isMinQualifierMet() );

  }

  public void testBasicSubmitterSuccessWithManagerOverrride()
  {
    ProductClaimPromotion promotion = buildOneToOnePromotionWithOnePayoutGroup();
    promotion.setPayoutManager( true );

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts = new SalesFacts( null, null, expectedManager, null );

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();
    Set activities = buildQualifyingActivitiesForOneToOne( true, promotion, claim );

    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, calculatedPayout.longValue() );

    assertEquals( 1, payoutCalculationResult.getContributingActivities().size() );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold, managerOverrideActivity.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity.getParticipant() );
    assertNull( managerOverrideActivity.getMinimumQualifierStatus() );
  }

  // TODO: may areuse for min quantity type
  // public void testFailureInsufficientQuantity()
  // {
  // Promotion promotion = buildOneToOnePromotionWithOnePayoutGroup();
  // Set activities = buildQualifyingActivitiesForOneToOne( true, promotion );
  //
  // SalesActivity activity = (SalesActivity)activities.iterator().next();
  // long insufficientQuantity = activity.getQuantity().longValue() - 1;
  // activity.setQuantity( new Long( insufficientQuantity ) );
  //
  // SalesFacts salesFacts = new SalesFacts( null, null, null );
  // OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();
  //
  // Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities,
  // promotion,
  // salesFacts );
  // assertEquals( payoutCalculationResults.size(), 1 );
  //
  // PayoutCalculationResult payoutCalculationResult =
  // (PayoutCalculationResult)payoutCalculationResults
  // .iterator().next();
  //
  // assertFalse( "Expected zero payout, but payout occurred. success reason: "
  // + payoutCalculationResult.getPayoutCalculationAudit().getReasonType()
  // .getCode(),
  // payoutCalculationResult.isCalculationSuccessful() );
  //
  // assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
  // PayoutCalculationAuditReasonType.CROSS_SELL_INSUFFICIENT_QUANTITY);
  // }

  public void testNoInputActivities()
  {

    Promotion promotion = buildOneToOnePromotionWithOnePayoutGroup();

    // Add a sales activity for something other than that which qualifies.
    Set activities = new LinkedHashSet();

    SalesFacts salesFacts = null;
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    try
    {
      payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
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
    Promotion promotion = buildOneToOnePromotionWithOnePayoutGroup();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();

    // Add a sales activity for something other than that which qualifies.
    Set activities = new LinkedHashSet();
    // Add salesActivity4
    SalesActivity salesActivity4 = new SalesActivity( product4, new Long( product4QuantitySold ) );
    salesActivity4.setSubmitter( true );
    salesActivity4.setParticipant( uniqueParticipant );
    salesActivity4.setClaim( claim );

    activities.add( salesActivity4 );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.CROSS_SELL_UNMATCHED_PRODUCT );
  }

  public void testSubmitterTwoPayoutGroupsSuccess()
  {
    Promotion promotion = buildCrossSellPromotionWithTwoPayoutGroups();
    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();

    Set activities = buildQualifyingActivitiesForOneToOne( true, promotion, claim );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
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
    assertEquals( ( payoutGroup1SubmitterPayout * product1QuantitySold ) + ( payoutGroup2SubmitterPayout * product3QuantitySold ), totalCalculatedPayout );

  }

  public void testSameProductTwiceInClaimSuccess()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Promotion promotion = buildCrossSellPromotionWithOnePayoutGroupWithOneProduct();
    Set activities = new LinkedHashSet();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( product1QuantitySold ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setParticipant( uniqueParticipant );
    salesActivity1.setClaim( claim );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product1, new Long( product1QuantitySold ) );
    salesActivity2.setSubmitter( true );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout * product1QuantitySold * 2, calculatedPayout.longValue() );
  }

  public void testSoldProductsMatchProductCatandProdSubcatSuccess()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Promotion promotion = buildOneToOnePromotionWithProductCatAndSubcatPayoutGroups();
    Set activities = new LinkedHashSet();

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( productS1OfC3, new Long( productS1OfC3QuantitySold ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setParticipant( uniqueParticipant );
    salesActivity1.setClaim( claim );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( productS2OfC3, new Long( productS2OfC3QuantitySold ) );
    salesActivity2.setSubmitter( true );
    salesActivity2.setParticipant( uniqueParticipant );
    salesActivity1.setClaim( claim );
    activities.add( salesActivity2 );

    // TODO: add a product from subcat 1 and one from subcat 2.
    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 2 );

    // Confirm calculated payouts
    long totalCalculatedPayout = 0;
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                  payoutCalculationResult.isCalculationSuccessful() );

      assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

      totalCalculatedPayout += payoutCalculationResult.getCalculatedPayout().longValue();
    }
    int actualGroup1TotalPayout = ( productS1OfC3QuantitySold + productS2OfC3QuantitySold ) * payoutGroup1SubmitterPayout;
    int actualGroup2TotalPayout = productS1OfC3QuantitySold * payoutGroup2SubmitterPayout;
    assertEquals( actualGroup1TotalPayout + actualGroup2TotalPayout, totalCalculatedPayout );

  }

  public void testZeroPayoutFailure()
  {
    ProductClaimPromotion promotion = buildOneToOnePromotionWithOnePayoutGroup();
    PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)promotion.getPromotionPayoutGroups().iterator().next();
    promotionPayoutGroup.setSubmitterPayout( 0 );

    Claim claim = ClaimDAOImplTest.buildStaticProductClaim();

    Set activities = buildQualifyingActivitiesForOneToOne( true, promotion, claim );

    SalesFacts salesFacts = new SalesFacts( null, null, null, null );
    OneToOnePayoutStrategy payoutStrategy = new OneToOnePayoutStrategy();

    Set payoutCalculationResults = payoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_ZERO_PAYOUT_GROUP_PAYOUT );
  }

  private ProductClaimPromotion buildOneToOnePromotionWithOnePayoutGroup()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroup1();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private ProductClaimPromotion buildCrossSellPromotionWithOnePayoutGroupWithOneProduct()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroupWithOneProduct();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private ProductClaimPromotion buildOneToOnePromotionWithProductCatAndSubcatPayoutGroups()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );

    PromotionPayoutGroup promotionPayoutGroup1 = buildCategoryPayoutGroup();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup1 );

    PromotionPayoutGroup promotionPayoutGroup2 = buildSubcategoryPayoutGroup();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup2 );

    return promotion;
  }

  private ProductClaimPromotion buildCrossSellPromotionWithTwoPayoutGroups()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.ONE_TO_ONE ) );

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
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroupWithOneProduct()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup1TeamMemberPayout );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
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
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildCategoryPayoutGroup()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup1TeamMemberPayout );

    PromotionPayout promotionPayoutWithProductCategory = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategory3 );
    promotionPayoutGroup.addPromotionPayout( promotionPayoutWithProductCategory );

    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildSubcategoryPayoutGroup()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup2SubmitterPayout );
    promotionPayoutGroup.setTeamMemberPayout( payoutGroup2TeamMemberPayout );

    PromotionPayout promotionPayoutWithSubcategory = PromotionPayoutDAOImplTest.buildPromotionPayout( productSubcategory1OfCat3 );
    promotionPayoutGroup.addPromotionPayout( promotionPayoutWithSubcategory );

    return promotionPayoutGroup;
  }

  private Set buildQualifyingActivitiesForOneToOne( boolean isSubmitter, Promotion promotion, Claim claim )
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( product1QuantitySold ) );
    salesActivity1.setSubmitter( isSubmitter );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    salesActivity1.setClaim( claim );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product2, new Long( product2QuantitySold ) );
    salesActivity2.setSubmitter( isSubmitter );
    salesActivity2.setPromotion( promotion );
    salesActivity2.setParticipant( uniqueParticipant );
    salesActivity2.setClaim( claim );

    activities.add( salesActivity2 );

    // Add salesActivity3
    SalesActivity salesActivity3 = new SalesActivity( product3, new Long( product3QuantitySold ) );
    salesActivity3.setSubmitter( isSubmitter );
    salesActivity3.setPromotion( promotion );
    salesActivity3.setParticipant( uniqueParticipant );
    salesActivity3.setClaim( claim );
    activities.add( salesActivity3 );

    // Add salesActivity4
    SalesActivity salesActivity4 = new SalesActivity( product4, new Long( product4QuantitySold ) );
    salesActivity4.setSubmitter( isSubmitter );
    salesActivity4.setPromotion( promotion );
    salesActivity4.setParticipant( uniqueParticipant );
    salesActivity4.setClaim( claim );
    activities.add( salesActivity4 );

    return activities;
  }

}
