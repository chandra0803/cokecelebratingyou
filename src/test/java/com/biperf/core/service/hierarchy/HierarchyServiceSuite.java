/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/hierarchy/HierarchyServiceSuite.java,v $
 */

package com.biperf.core.service.hierarchy;

import com.biperf.core.service.hierarchy.impl.HierarchyServiceImplTest;
import com.biperf.core.service.hierarchy.impl.NodeServiceImplTest;
import com.biperf.core.service.hierarchy.impl.NodeTypeServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Hierarchy Service test suite for running all hierarchy service tests out of container.
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
public class HierarchyServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.hierarchy.HierarchyServiceSuite" );

    suite.addTestSuite( HierarchyServiceImplTest.class );
    suite.addTestSuite( NodeServiceImplTest.class );
    suite.addTestSuite( NodeTypeServiceImplTest.class );

    return suite;
  }

}
