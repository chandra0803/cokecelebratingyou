
package com.biperf.core.service.publicrecognitionwall;

import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.publicrecognitionwall.impl.PublicRecognitionWallServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PublicRecognitionWallServiceSuite extends BaseServiceTest
{

  /**
     * Service Test Suite
     * 
     * @return Test
     */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.publicrecognitionwall.PublicRecognitionWallServiceSuite" );

    suite.addTestSuite( PublicRecognitionWallServiceImplTest.class );

    return suite;
  }

}
