
package com.biperf.core.dao.goalquest;

import com.biperf.core.dao.goalquest.hibernate.GoalLevelDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GoalLevelDAOSuite extends TestSuite
{
  /**
   * Returns the goal level DAO test suite.
   * 
   * @return the goal level DAO test suite.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.goalquest.GoalLevelDAOSuite" );
    suite.addTestSuite( GoalLevelDAOImplTest.class );

    return suite;
  }
}
