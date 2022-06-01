
package com.biperf.core.service.gamification;

import com.biperf.core.service.gamification.impl.GamificationServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GamificationServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.gamification.GamificationServiceSuite" );

    suite.addTestSuite( GamificationServiceImplTest.class );

    return suite;
  }
}
