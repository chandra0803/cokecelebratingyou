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
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
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
 * TieredPayoutStrategyTest.
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
public class TieredPayoutStrategyTest extends BaseServiceTest
{
  Product product1;

  Product product2;

  Product product3;

  Product product4;

  ProductCategory productCategory3;

  int payoutGroup1SubmitterPayout;
  int payoutGroup1QuantityRequired;

  int payoutGroup2SubmitterPayout;
  int payoutGroup2QuantityRequired;

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
    ProductCategory productCategory1And2 = ProductCategoryDAOImplTest.buildProductCategory( "pc1and2" );
    product1 = ProductDAOImplTest.buildStaticProductDomainObject( "p1", productCategory1And2 );

    product2 = ProductDAOImplTest.buildStaticProductDomainObject( "p2", productCategory1And2 );

    productCategory3 = ProductCategoryDAOImplTest.buildProductCategory( "pc3" );
    product3 = ProductDAOImplTest.buildStaticProductDomainObject( "p3", productCategory3 );

    ProductCategory productSubcategory1OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "ps1ofc3" );
    productCategory3.addSubcategory( productSubcategory1OfCat3 );
    ProductCategory productSubcategory2OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "ps2ofc3" );
    productCategory3.addSubcategory( productSubcategory2OfCat3 );

    ProductCategory productCategory4 = ProductCategoryDAOImplTest.buildProductCategory( "pc4" );
    product4 = ProductDAOImplTest.buildStaticProductDomainObject( "p4", productCategory4 );

    payoutGroup1SubmitterPayout = 5;
    payoutGroup1QuantityRequired = 3;

    payoutGroup2SubmitterPayout = 25;
    payoutGroup2QuantityRequired = 10;

  }

  public void testBasicSuccess()
  {
    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    Set qualifyingActivities = buildQualifyingActivitiesForTiered( promotion );
    Set nonQualifyingActivities = buildNonQualifyingActivitiesForTiered( promotion, null );

    // For master and child promotions, some products may not quality for certain groups,
    // Strategy take care of that.

    Set activities = new LinkedHashSet();
    activities.addAll( qualifyingActivities );
    activities.addAll( nonQualifyingActivities );

    // add additional non-qualifying activity
    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should qualify once
    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    // Expected One PayoutResult because there was one group.
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( qualifyingActivities.size(), payoutCalculationResult.getContributingActivities().size() );
    assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );

    // Test with child promo - should qualify twice
    promotion.setParentPromotion( new ProductClaimPromotion() );
    payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( qualifyingActivities.size(), payoutCalculationResult.getContributingActivities().size() );
    assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );

  }

  public void testBasicMultiGroupSuccess()
  {
    ProductClaimPromotion promotion = buildTieredMultiGroupPromotionProductsOnly();

    Set qualifyingActivities = buildQualifyingActivitiesForTiered( promotion );
    Set nonQualifyingActivities = buildNonQualifyingActivitiesForTiered( promotion, null );

    // For master and child promotions, some products may not quality for certain groups,
    // Strategy take care of that.

    Set activities = new LinkedHashSet();
    activities.addAll( qualifyingActivities );
    activities.addAll( nonQualifyingActivities );

    // add additional non-qualifying activity
    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should qualify once
    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    // Expect 2 results, one for each group.
    assertEquals( payoutCalculationResults.size(), 2 );

    long totalPayout = 0;
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      if ( payoutCalculationResult.getCalculatedPayout() != null )
      {
        totalPayout += payoutCalculationResult.getCalculatedPayout().longValue();

        assertEquals( qualifyingActivities.size(), payoutCalculationResult.getContributingActivities().size() );

        assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );
      }
      else
      {
        // Neither should generate carryover
        assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );
      }
    }
    assertEquals( payoutGroup1SubmitterPayout, totalPayout );

  }

  public void testBasicSuccessWithOutputCarryover()
  {
    Promotion promotion = buildTieredPromotionProductsOnly();
    Set activities = buildQualifyingActivitiesForTiered( promotion );
    // Add additional sales activity to create carryover
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( 2 ) );
    activities.add( salesActivity1 );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

    // should be two carryover activities, one for each product
    assertEquals( 2, payoutCalculationResult.getGeneratedActivities().size() );
    SalesActivity carryoverActivity = (SalesActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();
    assertTrue( carryoverActivity.isCarryover() );
    assertEquals( 1, carryoverActivity.getQuantity().longValue() );

  }

  public void testBasicMultiGroupSuccessWithOutputCarryOverOnEachGroup()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.TIERED ) );
    promotion.setPayoutCarryOver( true );

    PromotionPayoutGroup promotionPayoutGroup1 = new PromotionPayoutGroup();

    // group1 - 1 required, includes prod1 and prod2
    promotionPayoutGroup1.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup1.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup1.setQuantity( 2 );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    promotionPayoutGroup1.addPromotionPayout( promotionPayout1 );

    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    promotionPayoutGroup1.addPromotionPayout( promotionPayout2 );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup1 );

    // group1 - 1 required, includes prod3
    PromotionPayoutGroup promotionPayoutGroup2 = new PromotionPayoutGroup();

    promotionPayoutGroup2.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup2.setSubmitterPayout( payoutGroup2SubmitterPayout );
    promotionPayoutGroup2.setQuantity( 2 );

    PromotionPayout promotionPayout3 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );
    promotionPayoutGroup2.addPromotionPayout( promotionPayout3 );

    promotion.addPromotionPayoutGroup( promotionPayoutGroup2 );

    // Create activies so two will be used for group one (leaving carryover of 1) and
    // two will be used for group 2 (carryover of one)
    Set activities = buildQualifyingActivitiesForTiered( promotion );
    for ( Iterator iter = activities.iterator(); iter.hasNext(); )
    {
      SalesActivity activity = (SalesActivity)iter.next();

      if ( activity.getProduct().equals( product1 ) )
      {
        // Adjust the quantity to cause carryover of one on 2nd group.
        activity.setQuantity( new Long( 2 ) );
      }

      if ( activity.getProduct().equals( product3 ) )
      {
        // Adjust the quantity to cause carryover of one on 2nd group.
        activity.setQuantity( new Long( 3 ) );
      }

    }

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should qualify once
    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    // Expect 2 results, one for each group.
    assertEquals( payoutCalculationResults.size(), 2 );

    long totalPayout = 0;
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
      totalPayout += payoutCalculationResult.getCalculatedPayout().longValue();

      // group 2 has higer payout so would have been considered first, so carryover will all be in
      // group 1 since
      // carryover gets pushed to the last group processed.
      if ( payoutCalculationResult.getPromotionPayoutGroup().equals( promotionPayoutGroup1 ) )
      {

        assertEquals( 2, payoutCalculationResult.getContributingActivities().size() );

        assertEquals( 2, payoutCalculationResult.getGeneratedActivities().size() );

        assertContains( product2, payoutCalculationResult.getGeneratedActivities(), "product" );
        assertContains( product3, payoutCalculationResult.getGeneratedActivities(), "product" );

      }

      if ( payoutCalculationResult.getPromotionPayoutGroup().equals( promotionPayoutGroup2 ) )
      {
        assertEquals( 1, payoutCalculationResult.getContributingActivities().size() );

        assertEquals( 0, payoutCalculationResult.getGeneratedActivities().size() );

      }
    }
    assertEquals( payoutGroup1SubmitterPayout + payoutGroup2SubmitterPayout, totalPayout );

  }

  public void testBasicMultiGroupSuccessWithOutputCarryOver()
  {
    ProductClaimPromotion promotion = buildTieredMultiGroupPromotionProductsOnly();

    Set activities = buildQualifyingActivitiesForTiered( promotion );

    // Adjust the quantity of the first activity to cause carryover of one.
    SalesActivity activity = (SalesActivity)activities.iterator().next();
    activity.setQuantity( new Long( 2 ) );

    // add additional non-qualifying activity
    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should qualify once
    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    // Expect 2 results, one for each group.
    assertEquals( payoutCalculationResults.size(), 2 );

    long totalPayout = 0;
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      if ( payoutCalculationResult.getCalculatedPayout() != null )
      {
        totalPayout += payoutCalculationResult.getCalculatedPayout().longValue();

        // All activities contributed in payout.
        assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

        // One carryover should result and it should be product3
        assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );

        SalesActivity carryOverActivity = (SalesActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();

        assertEquals( product3.getName(), carryOverActivity.getProduct().getName() );
      }
    }
    assertEquals( payoutGroup1SubmitterPayout, totalPayout );

  }

  public void testBasicMultiGroupSuccessWithOutputCarryOverRetroOff()
  {
    Integer expectedMinQualifier = new Integer( 10 );
    ProductClaimPromotion promotion = buildTieredMultiGroupPromotionProductsOnly();

    Set activities = buildQualifyingActivitiesForTiered( promotion );
    for ( Iterator iter = promotion.getPromotionPayoutGroups().iterator(); iter.hasNext(); )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      promotionPayoutGroup.setMinimumQualifier( expectedMinQualifier );
      promotionPayoutGroup.setRetroPayout( Boolean.FALSE );
    }
    // Adjust the quantity of the first activity to cause carryover of one + enough for min
    // qualifier.
    SalesActivity activity = (SalesActivity)activities.iterator().next();
    activity.setQuantity( new Long( 2 + expectedMinQualifier.longValue() ) );

    // add additional non-qualifying activity
    SalesFacts salesFacts = new SalesFacts( null, null, null, new LinkedHashMap() );
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should qualify once
    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    // Expect 2 results, one for each group.
    assertEquals( payoutCalculationResults.size(), 2 );

    long totalPayout = 0;
    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      if ( payoutCalculationResult.getCalculatedPayout() != null )
      {
        totalPayout += payoutCalculationResult.getCalculatedPayout().longValue();

        // All activities contributed in payout.
        assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

        // One carryover should result and it should be product3
        assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );

        SalesActivity carryOverActivity = (SalesActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();

        assertEquals( product3.getName(), carryOverActivity.getProduct().getName() );
        MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
        assertNotNull( minimumQualifierStatus );
        assertTrue( minimumQualifierStatus.isMinQualifierMet() );
      }
    }
    assertEquals( payoutGroup1SubmitterPayout, totalPayout );

  }

  public void testBasicMultiGroupSuccessWithCarryOverOffRetroOff()
  {
    Integer expectedMinQualifier = new Integer( 10 );
    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    promotion.setPayoutCarryOver( false );

    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );
    Set activities = new LinkedHashSet();

    // Add salesActivity1. Set the quantity of the first activity so less than min qualifier,
    // shouldn't payout or
    // cause any min qualiifer accumulation.
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( expectedMinQualifier.longValue() - 1 ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setClaim( null );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    for ( Iterator iter = promotion.getPromotionPayoutGroups().iterator(); iter.hasNext(); )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      promotionPayoutGroup.setMinimumQualifier( expectedMinQualifier );
      promotionPayoutGroup.setRetroPayout( Boolean.TRUE );
    }

    SalesFacts salesFacts = new SalesFacts( null, null, null, new LinkedHashMap() );
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should not qualify
    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MIN_QUALIFIER_NOT_EXCEEDED_CARRYOVER_OFF_FAILURE );

    MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus );
    assertFalse( minimumQualifierStatus.isMinQualifierMet() );
    assertEquals( 0, minimumQualifierStatus.getCompletedQuantity() );

    // Now run again with min qual + payoutGroup1QuantityRequired to receive award
    activities.clear();
    salesActivity1 = new SalesActivity( product1, new Long( expectedMinQualifier.longValue() + payoutGroup1QuantityRequired ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setClaim( null );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    for ( Iterator iter = promotion.getPromotionPayoutGroups().iterator(); iter.hasNext(); )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      promotionPayoutGroup.setMinimumQualifier( expectedMinQualifier );
      promotionPayoutGroup.setRetroPayout( Boolean.FALSE );
    }

    salesFacts = new SalesFacts( null, null, null, new LinkedHashMap() );
    tieredPayoutStrategy = new TieredPayoutStrategy();

    // Test with master promo - should not qualify
    payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );

    minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
    assertNotNull( minimumQualifierStatus );
    assertTrue( minimumQualifierStatus.isMinQualifierMet() );

    // long totalPayout = 0;
    // for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    // {
    // PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
    //
    // if ( payoutCalculationResult.getCalculatedPayout() != null )
    // {
    // totalPayout += payoutCalculationResult.getCalculatedPayout().longValue();
    //
    // // All activities contributed in payout.
    // assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size()
    // );
    //
    // // One carryover should result and it should be product3
    // assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );
    //
    // SalesActivity carryOverActivity = (SalesActivity)payoutCalculationResult
    // .getGeneratedActivities().iterator().next();
    //
    // assertEquals( product3.getName(), carryOverActivity.getProduct().getName() );
    // MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult
    // .getMinimumQualifierStatus();
    // assertNotNull( minimumQualifierStatus );
    // assertTrue( minimumQualifierStatus.isMinQualifierMet() );
    // }
    // }
    // assertEquals( payoutGroup1SubmitterPayout, totalPayout );

  }

  public void testBasicSuccessWithOutputCarryoverButCarryoverDisabled()
  {
    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    promotion.setPayoutCarryOver( false );

    Set activities = buildQualifyingActivitiesForTiered( promotion );
    // Add additional sales activity to create carryover
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( 2 ) );
    activities.add( salesActivity1 );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

    // should be no carryover activity, or any other activity either
    assertEquals( 0, payoutCalculationResult.getGeneratedActivities().size() );

  }

  public void testBasicSuccessWithManagerOverride()
  {
    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    promotion.setPayoutManager( true );
    Set activities = buildQualifyingActivitiesForTiered( promotion );

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts = new SalesFacts( null, null, expectedManager, null );
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

    // should be one manager override activity
    assertEquals( 1, payoutCalculationResult.getGeneratedActivities().size() );
    ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();
    assertEquals( payoutGroup1SubmitterPayout, managerOverrideActivity.getSubmitterPayout().longValue() );
    assertTrue( expectedManager == managerOverrideActivity.getParticipant() );
    assertNull( managerOverrideActivity.getMinimumQualifierStatus() );

  }

  public void testBasicSuccessWithOutputCarryoverAndManagerOverride()
  {
    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    promotion.setPayoutManager( true );
    Set activities = buildQualifyingActivitiesForTiered( promotion );

    Participant expectedManager = ParticipantDAOImplTest.buildUniqueParticipant( "manager" );
    SalesFacts salesFacts = new SalesFacts( null, null, expectedManager, null );

    // Add additional sales activity to create carryover
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( 2 ) );
    activities.add( salesActivity1 );

    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

    // should be three activities, two carryover and one manager override
    assertEquals( 3, payoutCalculationResult.getGeneratedActivities().size() );
    boolean salesActivityFound = false;
    boolean managerOverrideActivityFound = false;
    for ( Iterator iter = payoutCalculationResult.getGeneratedActivities().iterator(); iter.hasNext(); )
    {
      Activity generatedActivity = (Activity)iter.next();
      if ( generatedActivity instanceof SalesActivity )
      {
        SalesActivity carryoverActivity = (SalesActivity)generatedActivity;
        assertTrue( carryoverActivity.isCarryover() );
        assertEquals( 1, carryoverActivity.getQuantity().longValue() );
        salesActivityFound = true;
      }
      else if ( generatedActivity instanceof ManagerOverrideActivity )
      {
        ManagerOverrideActivity managerOverrideActivity = (ManagerOverrideActivity)generatedActivity;
        assertEquals( payoutGroup1SubmitterPayout, managerOverrideActivity.getSubmitterPayout().longValue() );
        assertTrue( expectedManager == managerOverrideActivity.getParticipant() );
        managerOverrideActivityFound = true;
      }
      else
      {
        fail( "unknown activity class in generated activities: " + generatedActivity.getClass().getName() );
      }
    }

    if ( !salesActivityFound )
    {
      fail( "Should have been sales activity in generated activities, none found" );
    }
    if ( !managerOverrideActivityFound )
    {
      fail( "Should have been managerOverride activity in generated activities, none found" );
    }

  }

  public void testFailureInsufficientQuantity()
  {
    Promotion promotion = buildTieredPromotionProductsOnly();
    Set activities = buildQualifyingActivitiesForTiered( promotion );

    SalesActivity activity = (SalesActivity)activities.iterator().next();
    long insufficientQuantity = activity.getQuantity().longValue() - 1;
    activity.setQuantity( new Long( insufficientQuantity ) );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.TIERED_INSUFFICIENT_QUANTITY );
  }

  public void testFailureInsufficientQuantityWithCarryover()
  {
    Claim inputCarryoverClaim1 = ClaimDAOImplTest.buildStaticProductClaim();
    Claim inputCarryoverClaim2 = ClaimDAOImplTest.buildStaticProductClaim();
    Claim actualClaim = ClaimDAOImplTest.buildStaticProductClaim();

    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)promotion.getPromotionPayoutGroups().iterator().next();
    // Put required quantity beyond what is claimed
    promotionPayoutGroup.setQuantity( 20 );

    Set activities = buildQualifyingActivitiesForTiered( promotion, actualClaim );

    // Add input carryover activity
    SalesActivity inputCarryoverActivity1 = new SalesActivity( product1, new Long( 2 ) );
    inputCarryoverActivity1.setCarryover( true );
    inputCarryoverActivity1.setClaim( inputCarryoverClaim1 );
    activities.add( inputCarryoverActivity1 );

    SalesActivity inputCarryoverActivity2 = new SalesActivity( product1, new Long( 2 ) );
    inputCarryoverActivity2.setCarryover( true );
    inputCarryoverActivity2.setClaim( inputCarryoverClaim2 );
    activities.add( inputCarryoverActivity2 );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.TIERED_INSUFFICIENT_QUANTITY );

    // should be five carryover activities
    assertEquals( 5, payoutCalculationResult.getGeneratedActivities().size() );
    boolean activityWithActualClaimFound = false;
    boolean activityWithInputCarryoverActivity1Found = false;
    boolean activityWithInputCarryoverActivity2Found = false;
    for ( Iterator iter = payoutCalculationResult.getGeneratedActivities().iterator(); iter.hasNext(); )
    {
      // Confirm each carryover activity appears for each of the three claims
      SalesActivity carryoverActivity = (SalesActivity)iter.next();
      assertTrue( carryoverActivity.isCarryover() );
      assertNotNull( "carryoverActivity must have a claim associated with it, but was null", carryoverActivity.getClaim() );
      if ( carryoverActivity.getClaim() == actualClaim )
      {
        activityWithActualClaimFound = true;
        assertEquals( 1, carryoverActivity.getQuantity().longValue() );
      }
      else if ( carryoverActivity.getClaim() == inputCarryoverActivity1.getClaim() )
      {
        activityWithInputCarryoverActivity1Found = true;
        assertEquals( inputCarryoverActivity1.getQuantity(), carryoverActivity.getQuantity() );
      }
      else if ( carryoverActivity.getClaim() == inputCarryoverActivity2.getClaim() )
      {
        activityWithInputCarryoverActivity2Found = true;
        assertEquals( inputCarryoverActivity2.getQuantity(), carryoverActivity.getQuantity() );
      }
      else
      {
        fail( "Unknown output carryover claim: " + carryoverActivity.getClaim() );
      }
    }
    assertTrue( activityWithActualClaimFound );
    assertTrue( activityWithInputCarryoverActivity1Found );
    assertTrue( activityWithInputCarryoverActivity2Found );
  }

  public void testInputCarryoverWithOutputCarryoverSuccess()
  {
    Claim inputCarryoverClaim = ClaimDAOImplTest.buildStaticProductClaim();
    Claim actualClaim = ClaimDAOImplTest.buildStaticProductClaim();

    Promotion promotion = buildTieredPromotionProductsOnly();
    Set activities = buildQualifyingActivitiesForTiered( promotion, actualClaim );
    // Add input carryover activity
    SalesActivity inputCarryoverActivity = new SalesActivity( product1, new Long( 2 ) );
    inputCarryoverActivity.setCarryover( true );
    inputCarryoverActivity.setClaim( inputCarryoverClaim );
    activities.add( inputCarryoverActivity );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );

    // should be two carryover activity
    assertEquals( 2, payoutCalculationResult.getGeneratedActivities().size() );
    SalesActivity carryoverActivity = (SalesActivity)payoutCalculationResult.getGeneratedActivities().iterator().next();
    assertTrue( carryoverActivity.isCarryover() );
    assertEquals( 1, carryoverActivity.getQuantity().longValue() );

    assertTrue( "output carryover activity claim must be same as submitted claim when payout occurs", actualClaim == carryoverActivity.getClaim() );

  }

  public void testtestSameProductTwiceInClaimSuccess()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    Promotion promotion = buildTieredPromotionProductsOnly();
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( 1 ) );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product1, new Long( 2 ) );// total sold needs
    // to be 3
    salesActivity2.setPromotion( promotion );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );
    assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );

  }

  public void testNoInputActivities()
  {

    Promotion promotion = buildTieredPromotionProductsOnly();

    // Add a sales activity for something other than that which qualifies.
    Set activities = new LinkedHashSet();

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    try
    {
      tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
      fail( "Should have received BeaconRuntimeException" );
    }
    catch( BeaconRuntimeException e )
    {
      // expected
    }

  }

  public void testDuplicateProductDefinitionSuccess()
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );

    // Create promototion with a product and a product category promotion payout objects that each
    // apply to product3
    Promotion promotion = buildTieredPromotionProductAndProductCategory();
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product3, new Long( 3 ) );// total sold needs
    // to be 3
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Expected payout, but no payout occurred. failure reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_SUCCESS );

    // Confirm calculated payouts
    Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( payoutGroup1SubmitterPayout, calculatedPayout.longValue() );

    assertEquals( activities.size(), payoutCalculationResult.getContributingActivities().size() );
    assertTrue( payoutCalculationResult.getGeneratedActivities().isEmpty() );
  }

  public void testZeroPayoutFailure()
  {
    ProductClaimPromotion promotion = buildTieredPromotionProductsOnly();
    PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)promotion.getPromotionPayoutGroups().iterator().next();
    promotionPayoutGroup.setSubmitterPayout( 0 );

    Set activities = buildQualifyingActivitiesForTiered( promotion );

    SalesFacts salesFacts = null;
    TieredPayoutStrategy tieredPayoutStrategy = new TieredPayoutStrategy();

    Set payoutCalculationResults = tieredPayoutStrategy.processActivitiesInternal( activities, promotion, salesFacts );
    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertFalse( "Expected zero payout, but payout occurred. success reason: " + payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(),
                 payoutCalculationResult.isCalculationSuccessful() );

    assertEquals( payoutCalculationResult.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.SALES_ZERO_PAYOUT_GROUP_PAYOUT );
  }

  private ProductClaimPromotion buildTieredPromotionProductsOnly()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.TIERED ) );
    promotion.setPayoutCarryOver( true );

    PromotionPayoutGroup promotionPayoutGroup1 = buildPromotionPayoutGroup1ProductsOnly();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup1 );

    return promotion;
  }

  private ProductClaimPromotion buildTieredMultiGroupPromotionProductsOnly()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.TIERED ) );
    promotion.setPayoutCarryOver( true );

    PromotionPayoutGroup promotionPayoutGroup1 = buildPromotionPayoutGroup1ProductsOnly();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup1 );

    PromotionPayoutGroup promotionPayoutGroup2 = buildPromotionPayoutGroup2ProductsOnly();
    promotion.addPromotionPayoutGroup( promotionPayoutGroup2 );

    return promotion;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroup1ProductsOnly()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setQuantity( payoutGroup1QuantityRequired );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );

    PromotionPayout promotionPayout3 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout3 );

    return promotionPayoutGroup;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroup2ProductsOnly()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup2SubmitterPayout );
    promotionPayoutGroup.setQuantity( payoutGroup2QuantityRequired );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product2 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );

    return promotionPayoutGroup;
  }

  private Promotion buildTieredPromotionProductAndProductCategory()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "" );

    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.TIERED ) );
    promotion.setPayoutCarryOver( true );

    PromotionPayoutGroup promotionPayoutGroup = buildPromotionPayoutGroupProductAndProductCategory();

    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  private PromotionPayoutGroup buildPromotionPayoutGroupProductAndProductCategory()
  {
    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();

    promotionPayoutGroup.setGuid( GuidUtils.generateGuid() );
    promotionPayoutGroup.setSubmitterPayout( payoutGroup1SubmitterPayout );
    promotionPayoutGroup.setQuantity( payoutGroup1QuantityRequired );

    PromotionPayout promotionPayout1 = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategory3 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout1 );

    PromotionPayout promotionPayout2 = PromotionPayoutDAOImplTest.buildPromotionPayout( product3 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout2 );

    return promotionPayoutGroup;
  }

  private Set buildQualifyingActivitiesForTiered( Promotion promotion )
  {
    return buildQualifyingActivitiesForTiered( promotion, null );
  }

  private Set buildQualifyingActivitiesForTiered( Promotion promotion, Claim claim )
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product1, new Long( 1 ) );
    salesActivity1.setSubmitter( true );
    salesActivity1.setClaim( claim );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product2, new Long( 1 ) );
    salesActivity2.setSubmitter( true );
    salesActivity2.setClaim( claim );
    salesActivity2.setPromotion( promotion );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    // Add salesActivity3
    SalesActivity salesActivity3 = new SalesActivity( product3, new Long( 1 ) );
    salesActivity3.setSubmitter( true );
    salesActivity3.setClaim( claim );
    salesActivity3.setPromotion( promotion );
    salesActivity3.setParticipant( uniqueParticipant );
    activities.add( salesActivity3 );

    return activities;
  }

  private Set buildNonQualifyingActivitiesForTiered( Promotion promotion, Claim claim )
  {
    Participant uniqueParticipant = ParticipantDAOImplTest.buildUniqueParticipant( ParticipantDAOImplTest.buildUniqueString() );
    Set activities = new LinkedHashSet();

    // Add salesActivity1
    SalesActivity salesActivity1 = new SalesActivity( product4, new Long( 1 ) );
    salesActivity1.setClaim( claim );
    salesActivity1.setPromotion( promotion );
    salesActivity1.setParticipant( uniqueParticipant );
    activities.add( salesActivity1 );

    // Add salesActivity2
    SalesActivity salesActivity2 = new SalesActivity( product4, new Long( 1 ) );
    salesActivity2.setClaim( claim );
    salesActivity2.setPromotion( promotion );
    salesActivity2.setParticipant( uniqueParticipant );
    activities.add( salesActivity2 );

    // Add salesActivity3
    SalesActivity salesActivity3 = new SalesActivity( product4, new Long( 1 ) );
    salesActivity3.setClaim( claim );
    salesActivity3.setPromotion( promotion );
    salesActivity3.setParticipant( uniqueParticipant );
    activities.add( salesActivity3 );

    return activities;
  }

}
