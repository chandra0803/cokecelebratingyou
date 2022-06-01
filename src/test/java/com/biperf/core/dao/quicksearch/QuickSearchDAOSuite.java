/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.quicksearch;

import com.biperf.core.dao.quicksearch.hibernate.QuickSearchDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * QuickSearchDAOSuite is a container for running all QuickSearch DAO tests out of container.
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
public class QuickSearchDAOSuite extends TestSuite
{

  /**
   * suite to run all Message DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.quicksearch.QuickSearchDAOSuite" );

    suite.addTestSuite( QuickSearchDAOImplTest.class );

    return suite;
  }

}
