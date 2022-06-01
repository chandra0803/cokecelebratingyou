/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/challengepoint/ChallengepointServiceSuite.java,v $
 */

package com.biperf.core.service.challengepoint;

import com.biperf.core.service.challengepoint.impl.ChallengepointProgressServiceImplTest;
import com.biperf.core.service.challengepoint.impl.ChallengepointServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AwardsBanqService test suite for running all awards banq service tests out of container.
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
 * <td>Sathish</td>
 * <td>May 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengepointServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.challengepoint.ChallengepointServiceSuite" );

    suite.addTestSuite( ChallengepointProgressServiceImplTest.class );
    suite.addTestSuite( ChallengepointServiceImplTest.class );
    return suite;
  }

}
