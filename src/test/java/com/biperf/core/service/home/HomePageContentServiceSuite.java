/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/home/HomePageContentServiceSuite.java,v $
 */

package com.biperf.core.service.home;

import com.biperf.core.service.home.impl.HomePageContentServiceTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * HomePageContentServiceSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 3, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class HomePageContentServiceSuite extends TestSuite
{
  /**
   * Returns a suite of home page content service tests.
   * 
   * @return a test suite that contains all the home page content service tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.home.HomePageContentServiceSuite" );

    suite.addTestSuite( HomePageContentServiceTest.class );

    return suite;
  }
}
