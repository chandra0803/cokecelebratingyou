/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.email;

import com.biperf.core.service.email.impl.MailingServiceImplTest;
import com.biperf.core.service.email.impl.PersonalizationServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * MailingServiceSuite
 * 
 *
 */
public class MailingServiceSuite extends TestSuite
{
  /**
   * Returns a suite of email notification service tests.
   * 
   * @return a test suite that contains all the email notification service tests.
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.email.MailingServiceSuite" );

    suite.addTestSuite( MailingServiceImplTest.class );
    suite.addTestSuite( PersonalizationServiceImplTest.class );

    return suite;
  }
}
