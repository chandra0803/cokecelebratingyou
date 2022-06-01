/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise;

import com.biperf.core.dao.merchandise.hibernate.MerchOrderDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MerchOrderDAOSuite extends TestSuite
{

  /**
   * suite to run all MerchOrder DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.merchandise.hibernate.MerchOrderDAOSuite" );

    suite.addTestSuite( MerchOrderDAOImplTest.class );

    return suite;
  }

}
