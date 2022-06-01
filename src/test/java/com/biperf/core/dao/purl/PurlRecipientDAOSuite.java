
package com.biperf.core.dao.purl;

import com.biperf.core.dao.purl.hibernate.PurlRecipientDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PurlRecipientDAOSuite extends TestSuite
{

  /**
   * suite to run all PurlRecipientDAO DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.purl.hibernate.PurlRecipientDAOSuite" );

    suite.addTestSuite( PurlRecipientDAOImplTest.class );

    return suite;
  }
}
