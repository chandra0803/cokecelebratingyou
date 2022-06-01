/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/audit/AuditServiceSuite.java,v $
 */

package com.biperf.core.service.audit;

import com.biperf.core.service.audit.impl.AuditServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * ActivityServiceSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class AuditServiceSuite extends TestSuite
{

  /**
   * Claim Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.audit.AuditServiceSuite" );

    suite.addTestSuite( AuditServiceImplTest.class );
    return suite;
  }
}
