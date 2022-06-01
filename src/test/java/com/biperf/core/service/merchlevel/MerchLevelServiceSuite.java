/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/merchlevel/MerchLevelServiceSuite.java,v $
 */

package com.biperf.core.service.merchlevel;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * MerchLevelServiceSuite  
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>babu</td>
 * <td>Jul 13, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */

public class MerchLevelServiceSuite extends TestSuite
{
  /**
   * Returns a suite of Merchlinq Level service tests.
   * 
   * @return a test suite that contains all the Merchlinq Level service tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.merchlevel.MerchLevelServiceSuite" );

    // suite.addTestSuite( MerchLevelServiceImplTest.class );

    return suite;
  }
}
