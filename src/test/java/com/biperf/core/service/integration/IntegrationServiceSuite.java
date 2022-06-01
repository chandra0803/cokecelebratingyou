/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/integration/IntegrationServiceSuite.java,v $
 */

package com.biperf.core.service.integration;

import com.biperf.core.service.integration.impl.EmailServiceImplTest;
import com.biperf.core.service.integration.impl.SupplierServiceImplTest;

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
public class IntegrationServiceSuite extends TestSuite
{

  /**
   * Integration Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.integration.IntegrationServiceSuite" );

    suite.addTestSuite( EmailServiceImplTest.class );
    suite.addTestSuite( SupplierServiceImplTest.class );

    return suite;
  }

}
