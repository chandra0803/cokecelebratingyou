/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/claim/ClaimServiceSuite.java,v $
 */

package com.biperf.core.service.claim;

import com.biperf.core.service.claim.impl.ClaimFormDefinitionServiceImplTest;
import com.biperf.core.service.claim.impl.ClaimServiceImplTest;
import com.biperf.core.service.claim.impl.NominationClaimServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ClaimService test suite for running all claim service tests out of container.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimServiceSuite extends TestSuite
{

  /**
   * Claim Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.claim.ClaimServiceSuite" );

    suite.addTestSuite( ClaimFormDefinitionServiceImplTest.class );
    suite.addTestSuite( ClaimServiceImplTest.class );
    suite.addTestSuite( NominationClaimServiceImplTest.class );
    return suite;
  }

}
