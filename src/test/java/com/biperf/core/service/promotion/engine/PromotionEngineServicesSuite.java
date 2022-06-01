/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/promotion/engine/PromotionEngineServicesSuite.java,v $
 */

package com.biperf.core.service.promotion.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
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
 * <td>Aug 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionEngineServicesSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.promotion.engine.PromotionEngineServicesSuite" );

    suite.addTestSuite( CrossSellPayoutStrategyTest.class );
    suite.addTestSuite( OneToOnePayoutStrategyTest.class );
    suite.addTestSuite( TieredPayoutStrategyTest.class );
    suite.addTestSuite( ManagerOverridePayoutStrategyTest.class );
    suite.addTestSuite( SalesPayoutStrategyUtilTest.class );
    suite.addTestSuite( QuizPayoutStrategyTest.class );
    suite.addTestSuite( SweepstakesPayoutStrategyTest.class );
    return suite;
  }

}
