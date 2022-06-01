/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/value/BudgetMeterTest.java,v $
 */

package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.BudgetType;
import com.objectpartners.cms.util.DateUtils;

import junit.framework.TestCase;

public class BudgetMeterTest extends TestCase
{
  private static Long PROMOTION_ID = 500L;

  public void testContainsMultipleNodes()
  {
    BudgetMeter budgetMeter = new BudgetMeter();
    assertFalse( "empty budget meter should not have multiple nodes", budgetMeter.containsMultipleNodes() );

    budgetMeter.getBudgetMeterDetails().add( buildPaxBudgetMeterDetailBean( 1L, 5 ) );
    assertFalse( "pax budget meter should not have multiple nodes", budgetMeter.containsMultipleNodes() );

    budgetMeter.getBudgetMeterDetails().add( buildNodeBudgetMeterDetailBean( 2L, 4L, 5 ) );
    assertFalse( "one node budget meter should not have multiple nodes", budgetMeter.containsMultipleNodes() );

    budgetMeter.getBudgetMeterDetails().add( buildNodeBudgetMeterDetailBean( 3L, 4L, 5 ) );
    assertFalse( "same node budget meter should not have multiple nodes", budgetMeter.containsMultipleNodes() );

    budgetMeter.getBudgetMeterDetails().add( buildNodeBudgetMeterDetailBean( 4L, 5L, 5 ) );
    assertTrue( "different node budget meter should have multiple nodes", budgetMeter.containsMultipleNodes() );
  }

  public void testGetBudgetMeterDetailForBudgetId()
  {
    BudgetMeter budgetMeter = new BudgetMeter();
    assertNull( "empty budget meter should not find budget meter detail", budgetMeter.getBudgetMeterDetailForBudgetId( 1L ) );

    budgetMeter.getBudgetMeterDetails().add( buildPaxBudgetMeterDetailBean( 2L, 5 ) );
    assertNull( "non-matched budget id should not find budget meter detail", budgetMeter.getBudgetMeterDetailForBudgetId( 1L ) );

    budgetMeter.getBudgetMeterDetails().add( buildPaxBudgetMeterDetailBean( 1L, 5 ) );
    BudgetMeterDetailBean foundBudgetMeterDetail = budgetMeter.getBudgetMeterDetailForBudgetId( 1L );
    assertNotNull( "matched budget id should  find budget meter detail", foundBudgetMeterDetail );
    assertEquals( foundBudgetMeterDetail.getBudgetId(), Long.valueOf( 1 ) );
  }

  public void testSortAndPopulateDisplayNames()
  {
    BudgetMeter budgetMeter = new BudgetMeter();
    budgetMeter.sortAndPopulateDisplayNames();
    assertTrue( budgetMeter.getBudgetMeterDetails().isEmpty() );

    BudgetMeterDetailBean bean1 = buildNodeBudgetMeterDetailBean( 2L, 1L, 5 ); // shared node (1)
    budgetMeter.getBudgetMeterDetails().add( bean1 );
    budgetMeter.sortAndPopulateDisplayNames();
    assertEquals( bean1, budgetMeter.getBudgetMeterDetails().get( 0 ) );

    BudgetMeterDetailBean bean2 = buildPaxBudgetMeterDetailBean( 1L, 5 ); // shared pax
    budgetMeter.getBudgetMeterDetails().add( bean2 );
    budgetMeter.sortAndPopulateDisplayNames();
    assertEquals( bean1, budgetMeter.getBudgetMeterDetails().get( 0 ) );
    assertEquals( bean2, budgetMeter.getBudgetMeterDetails().get( 1 ) );

    BudgetMeterDetailBean bean3 = buildNodeBudgetMeterDetailBean( 3L, 3L, 5 ); // shared node (3)
    BudgetMeterDetailBean bean4 = buildNodeBudgetMeterDetailBean( 4L, 3L, 5 ); // shared node (3)
    BudgetMeterDetailBean bean5 = buildNodeBudgetMeterDetailBean( 5L, 2L, 5 ); // shared node (2)
    BudgetMeterDetailBean bean6 = buildNodeBudgetMeterDetailBean( 6L, 2L, 5 ); // shared node (2)
    BudgetMeterDetailBean bean7 = buildNodeBudgetMeterDetailBean( 7L, 2L, 1 ); // non-shared node
                                                                               // (2)
    BudgetMeterDetailBean bean8 = buildPaxBudgetMeterDetailBean( 8L, 5 ); // shared pax
    BudgetMeterDetailBean bean9 = buildPaxBudgetMeterDetailBean( 9L, 1 ); // non-shared pax
    budgetMeter.getBudgetMeterDetails().add( bean3 );
    budgetMeter.getBudgetMeterDetails().add( bean4 );
    budgetMeter.getBudgetMeterDetails().add( bean5 );
    budgetMeter.getBudgetMeterDetails().add( bean6 );
    budgetMeter.getBudgetMeterDetails().add( bean7 );
    budgetMeter.getBudgetMeterDetails().add( bean8 );
    budgetMeter.getBudgetMeterDetails().add( bean9 );
    budgetMeter.sortAndPopulateDisplayNames();
    assertEquals( bean9, budgetMeter.getBudgetMeterDetails().get( 0 ) );
    assertEquals( bean2, budgetMeter.getBudgetMeterDetails().get( 1 ) );
    assertEquals( bean8, budgetMeter.getBudgetMeterDetails().get( 2 ) );
    assertEquals( bean1, budgetMeter.getBudgetMeterDetails().get( 3 ) );
    assertEquals( bean7, budgetMeter.getBudgetMeterDetails().get( 4 ) );
    assertEquals( bean5, budgetMeter.getBudgetMeterDetails().get( 5 ) );
    assertEquals( bean6, budgetMeter.getBudgetMeterDetails().get( 6 ) );
    assertEquals( bean3, budgetMeter.getBudgetMeterDetails().get( 7 ) );
    assertEquals( bean4, budgetMeter.getBudgetMeterDetails().get( 8 ) );

    assertEquals( null, bean9.getBudgetDisplayName() );
    assertEquals( null, bean2.getBudgetDisplayName() );
    assertEquals( null, bean8.getBudgetDisplayName() );
    assertEquals( "nodeName1", bean1.getBudgetDisplayName() );
    assertEquals( "nodeName2", bean7.getBudgetDisplayName() );
    assertEquals( null, bean5.getBudgetDisplayName() );
    assertEquals( null, bean6.getBudgetDisplayName() );
    assertEquals( "nodeName3", bean3.getBudgetDisplayName() );
    assertEquals( null, bean4.getBudgetDisplayName() );
  }

  private static final BudgetMeterDetailBean buildPaxBudgetMeterDetailBean( Long budgetId, Integer promoListSize )
  {
    BudgetMeterDetailBean bean = new BudgetMeterDetailBean();

    bean.setBudgetId( budgetId );
    bean.setBudgetType( BudgetType.PAX_BUDGET_TYPE );
    bean.setBudgetMasterId( 100L );
    bean.setStartDate( DateUtils.toDate( "01/01/2012" ) );
    bean.setEndDate( DateUtils.toDate( "01/30/2012" ) );
    bean.setNodeName( null );
    bean.setNodeId( null );
    bean.setIsPrimaryNode( false );
    bean.setBudgetDisplayName( null );
    // Bug 67645 start
    bean.setUsedBudget( 50 );
    bean.setTotalBudget( 1000 );
    bean.setRemainingBudget( 950 );
    // Bug 67645 end

    List<BudgetMeterDetailPromoBean> promoList = new ArrayList<BudgetMeterDetailPromoBean>();
    for ( int i = 0; i < promoListSize; i++ )
    {
      promoList.add( buildBudgetMeterDetailPromoBean() );
    }
    bean.setPromoList( promoList );

    return bean;
  }

  private static final BudgetMeterDetailBean buildNodeBudgetMeterDetailBean( Long budgetId, Long nodeId, Integer promoListSize )
  {
    BudgetMeterDetailBean bean = new BudgetMeterDetailBean();

    bean.setBudgetId( budgetId );
    bean.setBudgetType( BudgetType.NODE_BUDGET_TYPE );
    bean.setBudgetMasterId( 100L );
    bean.setStartDate( DateUtils.toDate( "01/01/2012" ) );
    bean.setEndDate( DateUtils.toDate( "01/30/2012" ) );
    bean.setNodeName( "nodeName" + nodeId );
    bean.setNodeId( nodeId );
    bean.setIsPrimaryNode( false );
    bean.setBudgetDisplayName( null );
    bean.setUsedBudget( 50 );
    bean.setTotalBudget( 1000 );

    List<BudgetMeterDetailPromoBean> promoList = new ArrayList<BudgetMeterDetailPromoBean>();
    for ( int i = 0; i < promoListSize; i++ )
    {
      promoList.add( buildBudgetMeterDetailPromoBean() );
    }
    bean.setPromoList( promoList );

    return bean;
  }

  private static final BudgetMeterDetailPromoBean buildBudgetMeterDetailPromoBean()
  {
    BudgetMeterDetailPromoBean bean = new BudgetMeterDetailPromoBean();

    bean.setPromoId( PROMOTION_ID );
    bean.setPromoName( "promoName" + PROMOTION_ID );
    bean.setPromoStartDate( DateUtils.toDate( "01/01/2012" ) );
    bean.setPromoEndDate( null );
    PROMOTION_ID++;

    return bean;
  }

}
