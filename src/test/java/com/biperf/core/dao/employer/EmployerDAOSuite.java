/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/employer/EmployerDAOSuite.java,v $
 */

package com.biperf.core.dao.employer;

import com.biperf.core.dao.employer.hibernate.EmployerDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * EmployerDAOSuite is a container for running all employer DAO tests out of container.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerDAOSuite extends TestSuite
{

  /**
   * suite to run all employer DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.employer.EmployerDAOSuite" );

    suite.addTestSuite( EmployerDAOImplTest.class );

    return suite;
  }

}
