/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/employer/EmployerServiceSuite.java,v $
 */

package com.biperf.core.service.employer;

import com.biperf.core.service.employer.impl.EmployerServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * IntegrationService test suite for running all external integration service tests out of
 * container.
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
public class EmployerServiceSuite extends TestSuite
{

  /**
   * Employer Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.employer.EmployerServiceSuite" );

    suite.addTestSuite( EmployerServiceImplTest.class );

    return suite;
  }

}