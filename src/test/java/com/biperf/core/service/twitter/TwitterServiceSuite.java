
package com.biperf.core.service.twitter;

import com.biperf.core.service.twitter.impl.TwitterServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TwitterServiceSuite extends TestSuite
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.twitter.TwitterServiceSuite" );

    suite.addTestSuite( TwitterServiceImplTest.class );

    return suite;
  }
}
