/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/activity/ActivityServiceSuite.java,v $
 */

package com.biperf.core.service.activity;

import com.biperf.core.service.activity.impl.ActivityServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * ActivityServiceSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ActivityServiceSuite extends TestSuite
{

  /**
   * Claim Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.activity.ActivityServiceSuite" );

    suite.addTestSuite( ActivityServiceImplTest.class );
    return suite;
  }
}
