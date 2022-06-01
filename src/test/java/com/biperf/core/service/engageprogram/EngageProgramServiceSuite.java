
package com.biperf.core.service.engageprogram;

import com.biperf.core.service.engageprogram.impl.EngageProgramServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class EngageProgramServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.serviceanniversary.EngageProgramServiceSuite" );

    suite.addTestSuite( EngageProgramServiceImplTest.class );

    return suite;
  }
}
