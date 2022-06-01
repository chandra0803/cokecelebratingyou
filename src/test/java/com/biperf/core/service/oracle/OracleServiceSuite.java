
package com.biperf.core.service.oracle;

import com.biperf.core.service.oracle.impl.OracleServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * OracleServiceSuite.
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
 * <td>Tammy Cheng</td>
 * <td>Dec 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OracleServiceSuite extends TestSuite
{

  /**
   * Claim Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.oracle.OracleServiceSuite" );

    suite.addTestSuite( OracleServiceImplTest.class );
    return suite;
  }

}
