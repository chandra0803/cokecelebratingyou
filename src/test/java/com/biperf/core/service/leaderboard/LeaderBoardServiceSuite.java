
package com.biperf.core.service.leaderboard;

import com.biperf.core.service.leaderboard.impl.LeaderBoardServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class LeaderBoardServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.leaderboard.LeaderBoardServiceSuite" );

    suite.addTestSuite( LeaderBoardServiceImplTest.class );

    return suite;
  }
}
