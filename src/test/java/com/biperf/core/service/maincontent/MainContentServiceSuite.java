/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/maincontent/MainContentServiceSuite.java,v $
 */

package com.biperf.core.service.maincontent;

import com.biperf.core.service.maincontent.impl.MainContentServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * MainContentServiceSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 3, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class MainContentServiceSuite extends TestSuite
{
  /**
   * Returns a suite of main content service tests.
   * 
   * @return a test suite that contains all the main content service tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.maincontent.MainContentServiceSuite" );

    suite.addTestSuite( MainContentServiceImplTest.class );

    return suite;
  }
}
