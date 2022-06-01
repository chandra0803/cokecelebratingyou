/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/proxy/ProxyDAOSuite.java,v $
 */

package com.biperf.core.dao.proxy;

import com.biperf.core.dao.proxy.hibernate.ProxyDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ProxyDAOSuite is a container for running all Proxy DAO tests out of container.
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
 * <td>sedey</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyDAOSuite extends TestSuite
{

  /**
   * suite to run all Proxy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.proxy.ProxyDAOSuite" );

    suite.addTestSuite( ProxyDAOImplTest.class );

    return suite;
  }

}
