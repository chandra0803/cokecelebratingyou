/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/UtilsSuite.java,v $
 */

package com.biperf.core.utils;

import com.biperf.core.service.util.QuartzProcessUtilTest;
import com.biperf.core.utils.crypto.CryptoSuite;
import com.biperf.core.utils.fedresources.FEDResourceLocatorFactoryImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Utils test suite for running all utility tests out of container.
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
 * <td>Adam</td>
 * <td>Feb 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UtilsSuite extends TestSuite
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.utils.UtilsSuite" );

    suite.addTest( CryptoSuite.suite() );
    // suite.addTest( DateUtilsTest.class );
    suite.addTestSuite( QuartzProcessUtilTest.class );
    suite.addTestSuite( FEDResourceLocatorFactoryImplTest.class );

    return suite;
  }

}
