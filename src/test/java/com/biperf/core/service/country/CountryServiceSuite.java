/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/country/CountryServiceSuite.java,v $
 */

package com.biperf.core.service.country;

import com.biperf.core.service.country.impl.CountryServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * CountryService test suite for running all country service tests out of container.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.country.CountryServiceSuite" );

    suite.addTestSuite( CountryServiceImplTest.class );

    return suite;
  }

}
