/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/multimedia/MultimediaDAOSuite.java,v $
 */

package com.biperf.core.dao.multimedia;

import com.biperf.core.dao.multimedia.hibernate.MultimediaDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * MultimediaTestSuite.
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
 * <td>zahler</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MultimediaDAOSuite extends TestSuite
{
  /**
   * suite to run all Multimedia DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.multimedia.hibernate.MultimediaDAOImplTest" );

    suite.addTestSuite( MultimediaDAOImplTest.class );

    return suite;
  }
}
