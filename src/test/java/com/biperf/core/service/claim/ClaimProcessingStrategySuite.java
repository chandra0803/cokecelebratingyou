/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/claim/ClaimProcessingStrategySuite.java,v $
 */

package com.biperf.core.service.claim;

import com.biperf.core.service.claim.impl.ClaimProcessingStrategyTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ClaimProcessingStrategySuite.
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
 * <td>zahler</td>
 * <td>Oct 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimProcessingStrategySuite extends TestSuite
{
  /**
   * Claim Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.claim.ClaimProcessingStrategySuite" );

    suite.addTestSuite( ClaimProcessingStrategyTest.class );
    return suite;
  }
}
