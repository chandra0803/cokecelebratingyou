
package com.biperf.core.dao.purl;

import com.biperf.core.dao.purl.hibernate.PurlContributorDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PurlContributorDAOSuite
{
  /**
   * suite to run all PURL Contributor DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.purl.hibernate.PurlContributorDAOSuite" );

    suite.addTestSuite( PurlContributorDAOImplTest.class );

    return suite;
  }
}
