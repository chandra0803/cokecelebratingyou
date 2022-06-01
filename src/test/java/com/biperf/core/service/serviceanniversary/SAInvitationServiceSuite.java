
package com.biperf.core.service.serviceanniversary;

import com.biperf.core.service.serviceanniversary.impl.SAInvitationServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SAInvitationServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.serviceanniversary.SAProgramServiceSuite" );

    suite.addTestSuite( SAInvitationServiceImplTest.class );

    return suite;
  }
}
