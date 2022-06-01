/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/email/EmailNotificationServiceSuite.java,v $
 */

package com.biperf.core.service.email;

import com.biperf.core.service.email.impl.EmailNotificationServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * EmailNotificationServiceSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 3, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class EmailNotificationServiceSuite extends TestSuite
{
  /**
   * Returns a suite of email notification service tests.
   * 
   * @return a test suite that contains all the email notification service tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.email.EmailNotificationServiceSuite" );

    suite.addTestSuite( EmailNotificationServiceImplTest.class );

    return suite;
  }
}
