/**
 * 
 */

package com.biperf.core.dao.forum;

import com.biperf.core.dao.forum.hibernate.ForumTopicDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author poddutur
 *
 */
public class ForumTopicDAOSuite extends TestSuite
{
  /**
     * suite to run all ForumTopic DAO Tests.
     * 
     * @return Test
     */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.forum.ForumDAOSuite" );

    suite.addTestSuite( ForumTopicDAOImplTest.class );

    return suite;
  }

}
