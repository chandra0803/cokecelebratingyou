/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/country/CountryDAOSuite.java,v $
 */

package com.biperf.core.dao.country;

import com.biperf.core.dao.country.hibernate.CountryDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * CountryDAOSuite is a container for running all Country DAO tests out of container.
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
public class CountryDAOSuite extends TestSuite
{

  /**
   * suite to run all Country DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.country.hibernate.CountryDAOSuite" );

    suite.addTestSuite( CountryDAOImplTest.class );

    return suite;
  }

}
