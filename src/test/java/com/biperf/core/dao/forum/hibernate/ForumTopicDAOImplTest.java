/**
 * 
 */

package com.biperf.core.dao.forum.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.forum.ForumTopicDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * @author poddutur
 * 
 */
public class ForumTopicDAOImplTest extends BaseDAOTest
{
  /**
   * Uses the ApplicationContextFactory to look up the ForumTopicDAO
   * implementation.
   * 
   * @return ForumTopicDAO
   */
  private ForumTopicDAO getForumTopicDAO()
  {
    return (ForumTopicDAO)ApplicationContextFactory.getApplicationContext().getBean( "forumTopicDAO" );
  }

  /**
   * Test getting all forum topics in the database.
   */
  @SuppressWarnings( "unchecked" )
  public void testGetAllForumTopics()
  {
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();

    List expectedList = new ArrayList();

    ForumTopic forumTopic1 = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "1" );
    forumTopicDAO.save( forumTopic1 );

    ForumTopic forumTopic2 = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "2" );
    forumTopicDAO.save( forumTopic2 );

    expectedList.add( forumTopic1 );
    expectedList.add( forumTopic2 );

    flushAndClearSession();

    List actualList = forumTopicDAO.getAll();

    assertTrue( "The list of forum topics from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );

  }

  /**
   * Tests create, save, update and selecting the forum topic by the Id.
   */
  public void testSaveAndGetById()
  {
    // create a new forum Topic
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();

    ForumTopic expectedForumTopic = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "1" );
    forumTopicDAO.save( expectedForumTopic );

    assertEquals( "Actual forum topic doesn't match with expected", expectedForumTopic, forumTopicDAO.getTopicById( expectedForumTopic.getId() ) );

    // do an update on the saved forum Topic
    expectedForumTopic.setTopicCmAssetCode( "test forum topic Name-UPDATED" );
    forumTopicDAO.save( expectedForumTopic );

    flushAndClearSession();

    // retrieve the forum Topic
    ForumTopic actualForumTopic = forumTopicDAO.getTopicById( expectedForumTopic.getId() );
    assertDomainObjectEquals( "Actual forum topic doesn't match with expected", expectedForumTopic, actualForumTopic );
  }

  /**
     * Test for delete the forum topic from the database.
     */
  public void testDelete()
  {
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();

    ForumTopic forumTopic = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "1" );
    forumTopicDAO.save( forumTopic );

    flushAndClearSession();

    // Ensure the forum topic has been saved to the database.
    ForumTopic forumTopicById = forumTopicDAO.getTopicById( forumTopic.getId() );
    assertEquals( "Forum Topic was not saved to the database as expected", forumTopic, forumTopicById );

    Long forumTopicId = forumTopicById.getId();
    forumTopicDAO.deleteTopic( forumTopicById );

    flushAndClearSession();

    // check if the forum topic got deleted
    assertTrue( "Forum Topic was not deleted from the database.", forumTopicDAO.getTopicById( forumTopicId ) == null );
  }

  /**
   * creates a forum topic domain object
   * 
   * @param suffix
   * @return ForumTopic
   */
  public static ForumTopic buildStaticForumTopicDomainObject( String string )
  {
    ForumTopic forumTopic = new ForumTopic();
    forumTopic.setTopicCmAssetCode( "Topic Name" + string );
    forumTopic.setAudienceType( "Audience type" + string );
    return forumTopic;
  }

}
