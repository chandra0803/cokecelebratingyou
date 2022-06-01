/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/commlog/CommLogServiceSuite.java,v $
 */

package com.biperf.core.service.commlog;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * CommLogService test suite for running all CommLogService tests out of container.
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
 * <td>jenniget</td>
 * <td>Nov 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogServiceSuite extends TestSuite
{
  /**
   * CommLogService Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.commlog.CommLogServiceSuite" );

    suite.addTestSuite( CommLogServiceImplTest.class );
    return suite;
  }
}
