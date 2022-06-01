
package com.biperf.core.service.facebook;

import com.biperf.core.service.facebook.impl.FacebookServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class FacebookServiceSuite extends TestSuite
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.facebook.FacebookServiceSuite" );

    suite.addTestSuite( FacebookServiceImplTest.class );

    return suite;
  }
}
