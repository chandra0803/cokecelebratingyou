/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/journal/JournalServiceSuite.java,v $
 */

package com.biperf.core.service.journal;

import com.biperf.core.service.journal.impl.JournalServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JournalServiceSuite.
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
 * <td>robinsra</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class JournalServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.journal.JournalServiceSuite" );

    suite.addTestSuite( JournalServiceImplTest.class );

    return suite;
  }

}
