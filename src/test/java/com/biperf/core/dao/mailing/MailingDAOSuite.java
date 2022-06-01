/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/mailing/MailingDAOSuite.java,v $
 */

package com.biperf.core.dao.mailing;

import com.biperf.core.dao.mailing.hibernate.MailingDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * MailingDAOSuite.
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
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MailingDAOSuite extends TestSuite
{
  /**
   * suite to run all Journal DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.mailing.hibernate.MailingDAOSuite" );

    suite.addTestSuite( MailingDAOImplTest.class );

    return suite;
  }
}
