/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/reports/ReportsDAOSuite.java,v $
 */

package com.biperf.core.dao.reports;

import com.biperf.core.dao.reports.hibernate.ReportsDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SecurityDAOSuite is a container for running all Security DAO tests out of container.
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
public class ReportsDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.reports.ReportsDAOSuite" );

    suite.addTestSuite( ReportsDAOImplTest.class );

    return suite;
  }

}
