
package com.biperf.core.service.purl;

import com.biperf.core.service.purl.impl.PurlServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PurlServiceSuite extends TestSuite
{
  /**
    * Service Test Suite
    * 
    * @return Test
    */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.purl.PurlServiceSuite" );

    suite.addTestSuite( PurlServiceImplTest.class );

    return suite;
  }

}
