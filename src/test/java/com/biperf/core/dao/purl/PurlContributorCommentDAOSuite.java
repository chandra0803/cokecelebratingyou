
package com.biperf.core.dao.purl;

import com.biperf.core.dao.purl.hibernate.PurlContributorCommentDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PurlContributorCommentDAOSuite extends TestSuite
{

  /**
   * suite to run all PurlContributorComment DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.purl.hibernate.PurlContributorCommentDAOSuite" );

    suite.addTestSuite( PurlContributorCommentDAOImplTest.class );

    return suite;
  }
}
