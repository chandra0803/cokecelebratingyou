/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/audit/AuditDAOSuite.java,v $
 */

package com.biperf.core.dao.audit;

import com.biperf.core.dao.audit.hibernate.PayoutCalculationAuditDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * AuditDAOSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct 3, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class AuditDAOSuite extends TestSuite
{
  /**
   * Returns a suite of audit DAO tests.
   * 
   * @return a test suite that contains all the audit DAO tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.audit.AuditDAOSuite" );

    suite.addTestSuite( PayoutCalculationAuditDAOImplTest.class );

    return suite;
  }
}
