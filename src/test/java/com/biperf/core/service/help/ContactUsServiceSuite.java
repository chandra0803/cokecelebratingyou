/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/help/ContactUsServiceSuite.java,v $
 */

package com.biperf.core.service.help;

import com.biperf.core.service.help.impl.ContactUsServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * ContactUsServiceSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 3, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ContactUsServiceSuite extends TestSuite
{
  /**
   * Returns a suite of contact us service tests.
   * 
   * @return a test suite that contains all the contact us service tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.help.ContactUsServiceSuite" );

    suite.addTestSuite( ContactUsServiceImplTest.class );

    return suite;
  }
}
