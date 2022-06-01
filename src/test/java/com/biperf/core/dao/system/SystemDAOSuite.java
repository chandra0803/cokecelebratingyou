/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/system/SystemDAOSuite.java,v $
 */

package com.biperf.core.dao.system;

import com.biperf.core.dao.system.hibernate.SystemVariableDAOTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SystemDAOSuite is a container for running all System DAO tests out of container.
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
public class SystemDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.system.SystemDAOSuite" );

    suite.addTestSuite( SystemVariableDAOTest.class );

    return suite;
  }

}
