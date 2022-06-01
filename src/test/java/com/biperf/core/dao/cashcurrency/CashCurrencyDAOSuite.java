/**
 * 
 */

package com.biperf.core.dao.cashcurrency;

import com.biperf.core.dao.cashcurrency.hibernate.CashCurrencyDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author poddutur
 *
 */
public class CashCurrencyDAOSuite extends TestSuite
{
  /**
     * suite to run all ForumTopic DAO Tests.
     * 
     * @return Test
     */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.cashcurrency.CashCurrencyDAOSuite" );

    suite.addTestSuite( CashCurrencyDAOImplTest.class );

    return suite;
  }

}
