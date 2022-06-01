/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/activity/ActivityDAOSuite.java,v $
 */

package com.biperf.core.dao.activity;

import com.biperf.core.dao.activity.hibernate.ActivityDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * ActivityDAOSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 13, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ActivityDAOSuite extends TestSuite
{
  /**
   * suite to run all SalesActivity DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.activity.hibernate.ActivityDAOSuite" );

    suite.addTestSuite( ActivityDAOImplTest.class );

    return suite;
  }

}
