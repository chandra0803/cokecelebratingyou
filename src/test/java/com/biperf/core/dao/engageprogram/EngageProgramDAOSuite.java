
package com.biperf.core.dao.engageprogram;

import com.biperf.core.dao.engageprogram.hibernate.EngageProgramDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class EngageProgramDAOSuite extends TestSuite
{
  /**
   * suite to run all EngageProgram DAO Tests.
   *
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.serviceanniversary.EngageProgramDAOSuite" );

    suite.addTestSuite( EngageProgramDAOImplTest.class );

    return suite;

  }
}
