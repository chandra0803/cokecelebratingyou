/**
 * 
 */

package com.biperf.core.service.forum;

import com.biperf.core.service.forum.impl.ForumTopicServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author poddutur
 *
 */
public class ForumTopicServiceSuite extends TestSuite
{
  /**
     * Service Test Suite
     * 
     * @return Test
     */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.forum.ForumTopicServiceSuite" );

    suite.addTestSuite( ForumTopicServiceImplTest.class );

    return suite;
  }

}
