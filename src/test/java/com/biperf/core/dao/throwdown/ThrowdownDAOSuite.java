/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/ThrowdownDAOSuite.java,v $
 */

package com.biperf.core.dao.throwdown;

import com.biperf.core.dao.throwdown.hibernate.DivisionDAOImplTest;
import com.biperf.core.dao.throwdown.hibernate.MatchDAOImplTest;
import com.biperf.core.dao.throwdown.hibernate.MatchTeamOutcomeDAOImplTest;
import com.biperf.core.dao.throwdown.hibernate.MatchTeamProgressDAOImplTest;
import com.biperf.core.dao.throwdown.hibernate.RoundDAOImplTest;
import com.biperf.core.dao.throwdown.hibernate.SmackTalkDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * BudgetDAOSuite is a container for running all Budget DAO tests out of container.
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
public class ThrowdownDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.throwdown.hibernate.ThrowdownDAOSuite" );

    suite.addTestSuite( DivisionDAOImplTest.class );
    suite.addTestSuite( RoundDAOImplTest.class );
    suite.addTestSuite( MatchDAOImplTest.class );
    suite.addTestSuite( MatchTeamOutcomeDAOImplTest.class );
    suite.addTestSuite( MatchTeamProgressDAOImplTest.class );
    suite.addTestSuite( SmackTalkDAOImplTest.class );

    return suite;
  }

}
