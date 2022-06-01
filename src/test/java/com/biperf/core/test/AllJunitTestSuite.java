/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/test/AllJunitTestSuite.java,v $
 */

package com.biperf.core.test;

import com.biperf.core.dao.DAOSuite;
import com.biperf.core.domain.DomainSuite;
import com.biperf.core.service.ServiceSuite;
import com.biperf.core.strategy.StrategySuite;
import com.biperf.core.ui.UIControllerSuite;
import com.biperf.core.utils.UtilsSuite;
import com.biperf.core.value.ValueSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllJunitTestSuite is a test suite for running all junit tests out of container. Application Test
 * Suite.
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
 * <td>waldal</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AllJunitTestSuite /* extends TestSuite */
{
  /**
   * Runs all test suites.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.test.AllJunitTestSuite" );

    suite.addTest( ValueSuite.suite() );
    suite.addTest( DomainSuite.suite() );
    suite.addTest( StrategySuite.suite() );
    suite.addTest( UtilsSuite.suite() );
    suite.addTest( ServiceSuite.suite() );
    suite.addTest( DAOSuite.suite() );
    suite.addTest( UIControllerSuite.suite() );
    return suite;
  }

}
