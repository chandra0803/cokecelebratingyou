/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/journal/JournalDAOSuite.java,v $
 */

package com.biperf.core.dao.journal;

import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * JournalDAOSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 14, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class JournalDAOSuite extends TestSuite
{
  /**
   * suite to run all Journal DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.journal.hibernate.JournalDAOSuite" );

    suite.addTestSuite( JournalDAOImplTest.class );

    return suite;
  }

}
