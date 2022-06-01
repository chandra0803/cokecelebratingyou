/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/commlog/CommLogDAOSuite.java,v $
 *
 */

package com.biperf.core.dao.commlog;

import com.biperf.core.dao.commlog.hibernate.CommLogDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * CommLogDAOSuite <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLogDAOSuite extends TestSuite
{
  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.commlog.hibernate.CommLogDAOSuite" );

    suite.addTestSuite( CommLogDAOImplTest.class );

    return suite;
  }
}