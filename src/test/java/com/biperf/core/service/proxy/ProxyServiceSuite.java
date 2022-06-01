/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/proxy/ProxyServiceSuite.java,v $
 */

package com.biperf.core.service.proxy;

import com.biperf.core.service.proxy.impl.ProxyServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Proxy Service test suite for running all proxy service tests out of container.
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
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyServiceSuite extends TestSuite
{

  /**
   * Proxy Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.proxy.ProxyServiceSuite" );

    suite.addTestSuite( ProxyServiceImplTest.class );

    return suite;
  }

}
