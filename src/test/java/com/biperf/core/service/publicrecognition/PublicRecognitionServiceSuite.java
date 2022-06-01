/**
 * 
 */

package com.biperf.core.service.publicrecognition;

import com.biperf.core.service.publicrecognition.impl.PublicRecognitionServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author poddutur
 *
 */
public class PublicRecognitionServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.publicrecognition.PublicRecognitionServiceSuite" );

    suite.addTestSuite( PublicRecognitionServiceImplTest.class );

    return suite;
  }
}
