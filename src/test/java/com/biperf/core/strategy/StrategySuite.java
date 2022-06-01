/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/strategy/StrategySuite.java,v $
 */

package com.biperf.core.strategy;

import com.biperf.core.strategy.impl.PasswordPolicyStrategyImplTest;
import com.biperf.core.strategy.impl.RecognitionWinnersListStrategyTest;
import com.biperf.core.strategy.impl.UserLockoutStrategyImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * StrategySuite.
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
 * <td>tennant</td>
 * <td>Apr 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StrategySuite extends TestSuite
{

  /**
   * Strategy Test suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.strategy.StrategySuite" );

    // Strategy Tests- maybe these need their own suite
    suite.addTestSuite( UserLockoutStrategyImplTest.class );
    suite.addTestSuite( PasswordPolicyStrategyImplTest.class );
    suite.addTestSuite( RecognitionWinnersListStrategyTest.class );

    return suite;
  }

}
