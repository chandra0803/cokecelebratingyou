
package com.biperf.core.dao.ssi;

import com.biperf.core.dao.ssi.hibernate.SSIContestDAOImplTest;
import com.biperf.core.dao.ssi.hibernate.SSIPromotionDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SSIDAOSuite extends TestSuite
{
  /**
   * suite to run all SSI DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.ssi.SSIDAOSuite" );

    suite.addTestSuite( SSIPromotionDAOImplTest.class );
    suite.addTestSuite( SSIContestDAOImplTest.class );

    return suite;
  }
}
