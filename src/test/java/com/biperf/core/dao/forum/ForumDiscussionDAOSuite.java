
package com.biperf.core.dao.forum;

import com.biperf.core.dao.forum.hibernate.ForumDiscussionDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ForumDiscussionDAOSuite extends TestSuite
{
  /**
     * suite to run all ForumDiscussion DAO Tests.
     * 
     * @return Test
     */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.forum.ForumDiscussionDAOSuite" );

    suite.addTestSuite( ForumDiscussionDAOImplTest.class );

    return suite;
  }

}
