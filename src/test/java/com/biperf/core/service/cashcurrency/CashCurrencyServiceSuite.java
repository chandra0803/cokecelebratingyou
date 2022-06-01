/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.cashcurrency;

import com.biperf.core.service.cashcurrency.impl.CashCurrencyServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author poddutur
 * @since Mar 31, 2016
 */
public class CashCurrencyServiceSuite extends TestSuite
{
  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.cashcurrency.CashCurrencyServiceSuite" );

    suite.addTestSuite( CashCurrencyServiceImplTest.class );

    return suite;
  }

}
