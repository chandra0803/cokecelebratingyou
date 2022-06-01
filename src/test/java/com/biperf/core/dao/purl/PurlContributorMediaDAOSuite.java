
package com.biperf.core.dao.purl;

import com.biperf.core.dao.purl.hibernate.PurlContributorMediaDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PurlContributorMediaDAOSuite extends TestSuite
{
  /**
   * suite to run all PurlContributorMedia DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.purl.hibernate.PurlContributorMediaDAOSuite" );

    suite.addTestSuite( PurlContributorMediaDAOImplTest.class );

    return suite;
  }
}
