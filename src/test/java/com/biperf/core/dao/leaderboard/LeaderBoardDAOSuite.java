
package com.biperf.core.dao.leaderboard;

import com.biperf.core.dao.leaderboard.hibernate.LeaderBoardDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class LeaderBoardDAOSuite extends TestSuite
{
  /**
   * suite to run all LeaderBoard DAO Tests.
   *
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.leaderboard.hibernate.LeaderBoardDAOSuite" );

    suite.addTestSuite( LeaderBoardDAOImplTest.class );

    return suite;

  }
}
