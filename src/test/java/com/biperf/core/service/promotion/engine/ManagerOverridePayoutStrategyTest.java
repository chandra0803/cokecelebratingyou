/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/promotion/engine/ManagerOverridePayoutStrategyTest.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.utils.GuidUtils;

/*
 * ManagerOverridePayoutStrategyTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 29, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ManagerOverridePayoutStrategyTest extends BaseServiceTest
{

  ProductClaimPromotion promo1;
  Date startDate;
  Date endDate;
  Participant manager;

  ManagerOverrideFacts facts;

  ManagerOverrideActivity activity1;
  ManagerOverrideActivity activity2;
  ManagerOverrideActivity activity3;
  ManagerOverrideActivity activity4;
  ManagerOverrideActivity activity5;

  protected void setUp() throws Exception
  {
    super.setUp();

    // Build Activities for manager override processing.
    promo1 = PromotionDAOImplTest.buildProductClaimPromotion( "TestPromo" );
    promo1.setPayoutManager( true );
    promo1.setPayoutManagerPercent( new Integer( 10 ) );

    Calendar cal = Calendar.getInstance();
    cal.add( Calendar.MONTH, -1 );
    startDate = cal.getTime();
    cal.add( Calendar.MONTH, 2 );
    endDate = cal.getTime();

    manager = ParticipantDAOImplTest.buildUniqueParticipant( "TestPax" );

    facts = new ManagerOverrideFacts( manager, startDate, endDate );

    activity1 = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    activity1.setParticipant( manager );
    activity1.setPosted( false );
    activity1.setPromotion( promo1 );
    activity1.setSubmissionDate( Calendar.getInstance().getTime() );
    activity1.setSubmitterPayout( new Long( 20 ) );

    activity2 = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    activity2.setParticipant( manager );
    activity2.setPosted( false );
    activity2.setPromotion( promo1 );
    activity2.setSubmissionDate( Calendar.getInstance().getTime() );
    activity2.setSubmitterPayout( new Long( 30 ) );

    activity3 = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    activity3.setParticipant( manager );
    activity3.setPosted( false );
    activity3.setPromotion( promo1 );
    activity3.setSubmissionDate( Calendar.getInstance().getTime() );
    activity3.setSubmitterPayout( new Long( 22 ) );

    activity4 = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    activity4.setParticipant( manager );
    activity4.setPosted( false );
    activity4.setPromotion( promo1 );
    activity4.setSubmissionDate( Calendar.getInstance().getTime() );
    activity4.setSubmitterPayout( new Long( 44 ) );

    activity5 = new ManagerOverrideActivity( GuidUtils.generateGuid() );
    activity5.setParticipant( manager );
    activity5.setPosted( false );
    activity5.setPromotion( promo1 );
    activity5.setSubmissionDate( Calendar.getInstance().getTime() );
    activity5.setSubmitterPayout( new Long( 33 ) );

  }

  public void testBasicSubmitterSuccess()
  {
    Set activities = new LinkedHashSet();
    activities.add( activity1 );
    activities.add( activity2 );

    ManagerOverridePayoutStrategy strategy = new ManagerOverridePayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promo1, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Manager Override Payout successful with a payout: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MANAGER_OVERRIDE );

    // Confirm calculated payouts
    Long calculatedPayout = result.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( 5, calculatedPayout.longValue() );
    assertEquals( 2, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

  }

  public void testSubmitterSuccessRoundingLow()
  {
    Set activities = new LinkedHashSet();
    activities.add( activity3 );

    ManagerOverridePayoutStrategy strategy = new ManagerOverridePayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promo1, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Manager Override Payout successful with a payout: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MANAGER_OVERRIDE );

    // Confirm calculated payouts
    Long calculatedPayout = result.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( 3, calculatedPayout.longValue() );
    assertEquals( 1, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

  }

  public void testSubmitterSuccessRoundingHigh()
  {
    Set activities = new LinkedHashSet();
    activities.add( activity3 );
    activities.add( activity4 );

    ManagerOverridePayoutStrategy strategy = new ManagerOverridePayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promo1, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Manager Override Payout successful with a payout: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MANAGER_OVERRIDE );

    // Confirm calculated payouts
    Long calculatedPayout = result.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( 7, calculatedPayout.longValue() );
    assertEquals( 2, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

  }

  public void testSubmitterSuccessRoundingMiddle()
  {
    Set activities = new LinkedHashSet();
    activities.add( activity3 );
    activities.add( activity5 );

    ManagerOverridePayoutStrategy strategy = new ManagerOverridePayoutStrategy();

    Set payoutCalculationResults = strategy.processActivities( activities, promo1, facts );

    assertEquals( payoutCalculationResults.size(), 1 );

    PayoutCalculationResult result = (PayoutCalculationResult)payoutCalculationResults.iterator().next();

    assertTrue( "Manager Override Payout successful with a payout: " + result.getPayoutCalculationAudit().getReasonType().getCode(), result.isCalculationSuccessful() );

    assertEquals( result.getPayoutCalculationAudit().getReasonType().getCode(), PayoutCalculationAuditReasonType.MANAGER_OVERRIDE );

    // Confirm calculated payouts
    Long calculatedPayout = result.getCalculatedPayout();
    assertNotNull( calculatedPayout );
    assertEquals( 6, calculatedPayout.longValue() );
    assertEquals( 2, result.getContributingActivities().size() );
    assertEquals( 0, result.getGeneratedActivities().size() );

  }

}
