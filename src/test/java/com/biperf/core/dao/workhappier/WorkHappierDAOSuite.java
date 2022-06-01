
package com.biperf.core.dao.workhappier;

import com.biperf.core.dao.workhappier.hibernate.WorkHappierDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WorkHappierDAOSuite extends TestSuite
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.workhappier.WorkHappierDAOSuite" );

    suite.addTestSuite( WorkHappierDAOImplTest.class );
    return suite;
  }
}
