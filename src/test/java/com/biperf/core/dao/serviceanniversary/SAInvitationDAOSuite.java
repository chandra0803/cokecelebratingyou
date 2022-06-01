
package com.biperf.core.dao.serviceanniversary;

import com.biperf.core.dao.serviceanniversary.hibernate.SAInvitationDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SAInvitationDAOSuite extends TestSuite
{
  /**
   * suite to run all SAProgram DAO Tests.
   *
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.serviceanniversary.SAInvitationDAOSuite" );

    suite.addTestSuite( SAInvitationDAOImplTest.class );

    return suite;

  }
}
