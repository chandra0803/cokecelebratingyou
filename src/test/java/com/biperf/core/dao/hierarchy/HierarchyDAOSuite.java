/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/hierarchy/HierarchyDAOSuite.java,v $
 */

package com.biperf.core.dao.hierarchy;

import com.biperf.core.dao.hierarchy.hibernate.HierarchyDAOImplTest;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.hierarchy.hibernate.NodeTypeDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * HierarchyDAOSuite is a container for running all Hierarchy DAO tests out of container.
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
public class HierarchyDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.core.dao.hierarchy.HierarchyDAOSuite" );

    suite.addTestSuite( HierarchyDAOImplTest.class );
    suite.addTestSuite( NodeDAOImplTest.class );
    suite.addTestSuite( NodeTypeDAOImplTest.class );

    return suite;
  }

}
