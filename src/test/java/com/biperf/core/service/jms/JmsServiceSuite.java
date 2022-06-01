
package com.biperf.core.service.jms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.biperf.core.service.jms.impl.GJavaMessageServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith( Suite.class )
@SuiteClasses( { GJavaMessageServiceImplTest.class } )
public class JmsServiceSuite extends TestSuite
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.integration.IntegrationServiceSuite" );

    suite.addTestSuite( GJavaMessageServiceImplTest.class );

    return suite;
  }

}
