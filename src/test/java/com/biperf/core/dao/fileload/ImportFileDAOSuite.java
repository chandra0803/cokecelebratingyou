/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/fileload/ImportFileDAOSuite.java,v $
 */

package com.biperf.core.dao.fileload;

import com.biperf.core.dao.fileload.hibernate.ImportFileDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * ImportFileDAOSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 19, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportFileDAOSuite extends TestSuite
{
  /**
   * Returns the import file DAO test suite.
   * 
   * @return the import file DAO test suite.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.fileload.ImportFileDAOSuite" );
    suite.addTestSuite( ImportFileDAOImplTest.class );

    return suite;
  }
}
