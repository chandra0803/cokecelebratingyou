
package com.biperf.core.service.throwdown;

import com.biperf.core.service.throwdown.impl.SmackTalkServiceImplTest;
import com.biperf.core.service.throwdown.impl.StackStandingParticipantServiceImplTest;
import com.biperf.core.service.throwdown.impl.StackStandingServiceImplTest;
import com.biperf.core.service.throwdown.impl.TeamServiceImplTest;
import com.biperf.core.service.throwdown.impl.ThrowdownServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ThrowdownServiceSuite extends TestSuite
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.throwdown.ThrowdownServiceSuite" );

    suite.addTestSuite( ThrowdownServiceImplTest.class );
    suite.addTestSuite( TeamServiceImplTest.class );
    suite.addTestSuite( StackStandingServiceImplTest.class );
    suite.addTestSuite( StackStandingParticipantServiceImplTest.class );
    suite.addTestSuite( SmackTalkServiceImplTest.class );

    return suite;
  }
}