/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.workhappier;

import com.biperf.core.service.workhappier.impl.WorkHappierServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author poddutur
 * @since Nov 30, 2015
 */
public class WorkHappierServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.workhappier.WorkHappierServiceSuite" );

    suite.addTestSuite( WorkHappierServiceImplTest.class );

    return suite;
  }

}
