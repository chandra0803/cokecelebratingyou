/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/budget/BudgetServiceSuite.java,v $
 */

package com.biperf.core.service.budget;

import com.biperf.core.service.budget.impl.BudgetMasterServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * BudgetService test suite for running all budget service tests out of container.
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
public class BudgetServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.budget.BudgetServiceSuite" );

    suite.addTestSuite( BudgetMasterServiceImplTest.class );

    return suite;
  }

}
