/**
 * 
 */

package com.biperf.core.service.forum.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.forum.ForumTopicDAO;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 * 
 */
public class ForumTopicServiceImplTest extends BaseServiceTest
{
  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ForumTopicServiceImplTest( String test )
  {
    super( test );
  }

  /** ForumTopicService */
  private ForumTopicServiceImpl forumTopicServiceImpl = new ForumTopicServiceImpl();

  /** mockForumTopicDAO */
  private Mock mockForumTopicDAO = null;

  /**
   * Setup the test. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockForumTopicDAO = new Mock( ForumTopicDAO.class );
    forumTopicServiceImpl.setForumTopicDAO( (ForumTopicDAO)mockForumTopicDAO.proxy() );
  }

  /**
   * Test get sorted topic list.
   */

  // Commenting this code, as forumTopicServiceImpl.getSortedTopicList( userId, count) referring
  // ForumTopic instead of ForumTopicValueBean
  // This needs to be fixed in service itself (NOTE: This method is not called anywhere)

  /*
   * public void testGetSortedTopicList() { Long userId = (long) 60020; int count = 3; List
   * expectedList = getSortedTopicList( userId );
   * mockForumTopicDAO.expects(once()).method("getSortedTopicList").will(returnValue(expectedList));
   * List actualList = forumTopicServiceImpl.getSortedTopicList( userId, count);
   * assertTrue("Actual set didn't contain expected set for getSortedTopicList.",
   * actualList.containsAll(expectedList)); mockForumTopicDAO.verify(); }
   */

  public void testGetSortedTopicList()
  {
    Long userId = (long)60020;

    List<ForumTopicValueBean> expectedList = getSortedTopicList( userId );

    mockForumTopicDAO.expects( once() ).method( "getAllSortedTopicList" ).will( returnValue( expectedList ) );

    List<ForumTopicValueBean> actualList = forumTopicServiceImpl.getAllSortedTopicList();

    assertTrue( "Actual set didn't contain expected set for getSortedTopicList.", actualList.containsAll( expectedList ) );

    mockForumTopicDAO.verify();
  }

  private List<ForumTopicValueBean> getSortedTopicList( Long userId )
  {
    List<ForumTopicValueBean> getSortedTopicList = new ArrayList<ForumTopicValueBean>();

    ForumTopicValueBean valueBean = new ForumTopicValueBean();
    valueBean.setId( new Long( 1 ) );
    valueBean.setLastPostUserId( userId );
    valueBean.setStatus( "Active" );
    valueBean.setRepliesCount( new Long( 3 ) );
    valueBean.setDiscussionCount( new Long( 5 ) );

    getSortedTopicList.add( valueBean );

    ForumTopicValueBean valueBean2 = new ForumTopicValueBean();
    valueBean2.setId( new Long( 1 ) );
    valueBean2.setLastPostUserId( userId );
    valueBean2.setStatus( "Active" );
    valueBean2.setRepliesCount( new Long( 3 ) );
    valueBean2.setDiscussionCount( new Long( 5 ) );

    getSortedTopicList.add( valueBean2 );

    ForumTopicValueBean valueBean3 = new ForumTopicValueBean();
    valueBean3.setId( new Long( 1 ) );
    valueBean3.setLastPostUserId( userId );
    valueBean3.setStatus( "Active" );
    valueBean3.setRepliesCount( new Long( 3 ) );
    valueBean3.setDiscussionCount( new Long( 5 ) );

    getSortedTopicList.add( valueBean3 );

    return getSortedTopicList;
  }

}
