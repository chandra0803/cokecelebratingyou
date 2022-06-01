/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/security/SecurityDAOSuite.java,v $
 */

package com.biperf.core.dao.security;

import com.biperf.core.dao.security.hibernate.AclDAOImplTest;
import com.biperf.core.dao.security.hibernate.RoleDAOImplTest;

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
public class SecurityDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.security.SecurityDAOSuite" );

    suite.addTestSuite( AclDAOImplTest.class );
    suite.addTestSuite( RoleDAOImplTest.class );

    return suite;
  }

}
