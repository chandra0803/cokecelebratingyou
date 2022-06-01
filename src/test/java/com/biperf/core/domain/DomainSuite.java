/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/DomainSuite.java,v $
 */

package com.biperf.core.domain;

import com.biperf.core.domain.claim.ClaimSuite;
import com.biperf.core.domain.enums.PickListSuite;
import com.biperf.core.domain.type.TypeSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Domain test suite for running all domain tests out of container.
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
public class DomainSuite extends TestSuite
{

  /**
   * Domain Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.domain.DomainSuite" );

    suite.addTest( PickListSuite.suite() );
    suite.addTest( TypeSuite.suite() );
    suite.addTest( ClaimSuite.suite() );
    suite.addTestSuite( BaseDomainTest.class );

    return suite;
  }

}
