
package com.biperf.core.dao.awardgenerator;

import com.biperf.core.dao.awardgenerator.hibernate.AwardGenBatchDAOImplTest;
import com.biperf.core.dao.awardgenerator.hibernate.AwardGeneratorDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AwardGenBatchDAOSuit extends TestSuite
{
  /**
   * Returns a suite of audit DAO tests.
   * 
   * @return a test suite that contains all the audit DAO tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.awardgenerator.AwardGenBatchDAOSuit" );

    suite.addTestSuite( AwardGenBatchDAOImplTest.class );
    suite.addTestSuite( AwardGeneratorDAOImplTest.class );

    return suite;
  }
}
