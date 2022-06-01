
package com.biperf.core.process;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ProcessSuit extends TestSuite
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite( ProcessSuit.class.getName() );

    suite.addTestSuite( RefreshPendingNominationApproverProcessTest.class );

    return suite;
  }

  public static void main( String[] args )
  {
    System.out.println( ProcessSuit.class.getName() );
  }

}
