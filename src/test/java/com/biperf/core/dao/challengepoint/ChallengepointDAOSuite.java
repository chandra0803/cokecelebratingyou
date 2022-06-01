/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/challengepoint/ChallengepointDAOSuite.java,v $
 */

package com.biperf.core.dao.challengepoint;

import com.biperf.core.dao.challengepoint.hibernate.ChallengepointAwardDAOImplTest;
import com.biperf.core.dao.challengepoint.hibernate.ChallengepointPaxGoalDAOImplTest;
import com.biperf.core.dao.challengepoint.hibernate.ChallengepointProgressDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * GoalQuestDAOSuite <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan
 * 01, 2007</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ChallengepointDAOSuite extends TestSuite
{
  /**
   * Returns the import file DAO test suite.
   * 
   * @return the import file DAO test suite.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAOSuite" );
    suite.addTestSuite( ChallengepointAwardDAOImplTest.class );
    suite.addTestSuite( ChallengepointPaxGoalDAOImplTest.class );
    suite.addTestSuite( ChallengepointProgressDAOImplTest.class );

    return suite;
  }
}
