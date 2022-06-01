/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/claim/ClaimDAOSuite.java,v $
 */

package com.biperf.core.dao.claim;

import com.biperf.core.dao.claim.hibernate.ClaimApproverSnapshotDAOImplTest;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.claim.hibernate.ClaimFormDAOImplTest;
import com.biperf.core.dao.claim.hibernate.ClaimGroupDAOImplTest;
import com.biperf.core.dao.claim.hibernate.JournalClaimQueryConstraintTest;
import com.biperf.core.dao.claim.hibernate.MinimumQualifierStatusDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ClaimDAOSuite.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimDAOSuite extends TestSuite
{
  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.claim.hibernate.ClaimDAOSuite" );

    suite.addTestSuite( ClaimApproverSnapshotDAOImplTest.class );
    suite.addTestSuite( ClaimFormDAOImplTest.class );
    suite.addTestSuite( ClaimDAOImplTest.class );
    suite.addTestSuite( ClaimGroupDAOImplTest.class );
    suite.addTestSuite( JournalClaimQueryConstraintTest.class );
    suite.addTestSuite( MinimumQualifierStatusDAOImplTest.class );

    return suite;
  }
}
