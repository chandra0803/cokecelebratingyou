
package com.biperf.core.domain.ssi;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * SSISuite.
 * 
 * @author kandhi
 * @since Oct 22, 2014
 * @version 1.0
 */
public class SSISuite extends TestSuite
{

  /**
   * SSI Domain Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.domain.ssi.SSISuite" );

    suite.addTestSuite( SSIPromotionTest.class );

    return suite;
  } // end suite

}
