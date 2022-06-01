/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/calculator/CalculatorDAOSuite.java,v $
 */

package com.biperf.core.dao.calculator;

import com.biperf.core.dao.calculator.hibernate.CalculatorDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * CalculatorDAOSuite.
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
 * <td>May 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorDAOSuite extends TestSuite
{

  /**
   * suite to run all Calculator DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.calculator.CalculatorDAOSuite" );

    suite.addTestSuite( CalculatorDAOImplTest.class );

    return suite;
  }

}
