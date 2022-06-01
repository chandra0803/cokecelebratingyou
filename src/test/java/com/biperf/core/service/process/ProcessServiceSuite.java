/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.process;

import com.biperf.core.service.process.impl.ProcessInvocationServiceImplTest;
import com.biperf.core.service.process.impl.ProcessServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Participant Service test suite for running all participant service tests out of container.
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
 * <td>Sathish</td>
 * <td>May 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.participant.ProcessServiceSuite" );

    suite.addTestSuite( ProcessServiceImplTest.class );
    suite.addTestSuite( ProcessInvocationServiceImplTest.class );

    return suite;
  }

}
