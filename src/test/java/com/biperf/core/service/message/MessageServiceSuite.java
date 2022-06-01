/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.message;

import com.biperf.core.service.message.impl.MessageServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * MessageService test suite for running all message service tests out of container.
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
 * <td>tcheng</td>
 * <td>September 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.message.MessageServiceSuite" );

    suite.addTestSuite( MessageServiceImplTest.class );

    return suite;
  }

}
