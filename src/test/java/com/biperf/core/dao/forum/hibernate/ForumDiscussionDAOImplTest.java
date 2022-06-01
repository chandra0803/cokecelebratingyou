/**
 * 
 */

package com.biperf.core.dao.forum.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.forum.ForumDiscussionDAO;
import com.biperf.core.dao.forum.ForumTopicDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.UserDAOImplTest;
import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionReply;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * @author poddutur
 * 
 */
public class ForumDiscussionDAOImplTest extends BaseDAOTest
{
  /**
   * Uses the ApplicationContextFactory to look up the ForumDiscussionDAO
   * implementation.
   * 
   * @return ForumDiscussionDAO
   */
  private ForumDiscussionDAO getForumDiscussionDAO()
  {
    return (ForumDiscussionDAO)ApplicationContextFactory.getApplicationContext().getBean( "forumDiscussionDAO" );
  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ForumTopicDAO
   * implementation.
   * 
   * @return ForumTopicDAO
   */
  private static ForumTopicDAO getForumTopicDAO()
  {
    return (ForumTopicDAO)ApplicationContextFactory.getApplicationContext().getBean( "forumTopicDAO" );
  }

  /**
   * Test getting all forum Discussions in the database.
   */
  @SuppressWarnings( "unchecked" )
  public void testGetAllForumDiscussions()
  {
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();

    ForumDiscussionDAO forumDiscussionDAO = getForumDiscussionDAO();

    ForumTopic forumTopic1 = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "1" );
    forumTopicDAO.save( forumTopic1 );

    List expectedList = new ArrayList();

    final User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );
    flushAndClearSession();

    ForumDiscussion forumDiscussion1 = new ForumDiscussion();// ForumDiscussionDAOImplTest.buildStaticForumDiscussionDomainObject("1");
    forumDiscussion1.setForumTopic( forumTopic1 );
    forumDiscussion1.setUser( user );
    forumDiscussionDAO.save( forumDiscussion1 );

    ForumDiscussion forumDiscussion2 = new ForumDiscussion(); // ForumDiscussionDAOImplTest.buildStaticForumDiscussionDomainObject("2");
    forumDiscussion2.setForumTopic( forumTopic1 );
    forumDiscussion2.setUser( user );
    forumDiscussionDAO.save( forumDiscussion2 );

    expectedList.add( forumDiscussion1 );
    expectedList.add( forumDiscussion2 );

    flushAndClearSession();

    List actualList = forumDiscussionDAO.getAll();

    assertTrue( "The list of forum Discussions from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );
  }

  /**
   * Tests create, save, update and selecting the forum Discussion by the Id.
   */
  public void testSaveAndGetById()
  {
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();

    ForumDiscussionDAO forumDiscussionDAO = getForumDiscussionDAO();

    ForumTopic forumTopic1 = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "1" );
    forumTopicDAO.save( forumTopic1 );

    ForumDiscussion expectedForumDiscussion = new ForumDiscussion();// ForumDiscussionDAOImplTest.buildStaticForumDiscussionDomainObject("1");
    expectedForumDiscussion.setForumTopic( forumTopic1 );

    final User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );
    flushAndClearSession();
    expectedForumDiscussion.setUser( user );

    forumDiscussionDAO.save( expectedForumDiscussion );

    assertEquals( "Actual forum discussion doesn't match with expected", expectedForumDiscussion, forumDiscussionDAO.getDiscussionById( expectedForumDiscussion.getId() ) );

    // do an update on the saved forum Discussion
    expectedForumDiscussion.setDiscussionTitle( "test forum discussion Name-UPDATED" );
    forumDiscussionDAO.save( expectedForumDiscussion );

    flushAndClearSession();

    // retrieve the forum Discussion
    ForumDiscussion actualForumDiscussion = forumDiscussionDAO.getDiscussionById( expectedForumDiscussion.getId() );
    assertDomainObjectEquals( "Actual forum discussion doesn't match with expected", expectedForumDiscussion, actualForumDiscussion );
  }

  /**
   * Test for delete the forum discussion from the database.
   */
  public void testDelete()
  {
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();

    ForumDiscussionDAO forumDiscussionDAO = getForumDiscussionDAO();

    ForumTopic forumTopic1 = ForumTopicDAOImplTest.buildStaticForumTopicDomainObject( "1" );
    forumTopicDAO.save( forumTopic1 );

    ForumDiscussion forumDiscussion = new ForumDiscussion(); // ForumDiscussionDAOImplTest.buildStaticForumDiscussionDomainObject("1");
    forumDiscussion.setForumTopic( forumTopic1 );
    final User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );
    flushAndClearSession();
    forumDiscussion.setUser( user );

    forumDiscussionDAO.save( forumDiscussion );
    flushAndClearSession();

    // Ensure the forum Discussion has been saved to the database.
    ForumDiscussion forumDiscussionById = forumDiscussionDAO.getDiscussionById( forumDiscussion.getId() );
    assertEquals( "Forum Discussion was not saved to the database as expected", forumDiscussion, forumDiscussionById );
    flushAndClearSession();
    Long forumDiscussionId = forumDiscussionById.getId();
    forumDiscussionDAO.deleteDiscussion( forumDiscussion, new ArrayList<ForumDiscussionReply>() );

    flushAndClearSession();

    // check if the forum Discussion got deleted
    assertTrue( "Forum Discussion was not deleted from the database.", forumDiscussionDAO.getDiscussionById( forumDiscussionId ) == null );
  }

  /**
   * test to get the list of sorted discussions by topicId from the database.
   * 
   */
  public void testGetSortedDiscussionList()
  {
    Long topicId = (long)1;
    ForumDiscussionDAO forumDiscussionDAO = getForumDiscussionDAO();

    List expectedList = new ArrayList();

    ForumDiscussion discussion1 = buildStaticForumDiscussionDomainObject( "1" );
    final User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );
    flushAndClearSession();
    discussion1.setUser( user );
    forumDiscussionDAO.save( discussion1 );

    ForumDiscussion discussion2 = buildStaticForumDiscussionDomainObject( "2" );
    discussion2.setUser( user );
    forumDiscussionDAO.save( discussion2 );

    expectedList.add( discussion1 );
    expectedList.add( discussion2 );

    flushAndClearSession();

    List actualList = forumDiscussionDAO.getSortedDiscussionList( topicId );

    assertTrue( "The list of sorted forum discussions from the database doesn't contain the expected set", expectedList.containsAll( actualList ) );
  }

  /**
   * creates a forum discussion domain object
   * 
   * @param suffix
   * @return ForumDiscussion
   */
  private static ForumDiscussion buildStaticForumDiscussionDomainObject( String string )
  {
    ForumDiscussion forumDiscussion = new ForumDiscussion();
    forumDiscussion.setForumTopic( buildStaticForumTopicDomainObject( string ) );
    forumDiscussion.setDiscussionTitle( "Discussion Name" + string );
    forumDiscussion.setDiscussionBody( "Discussion Body" + string );
    return forumDiscussion;
  }

  /**
   * creates a forum topic domain object
   * 
   * @param suffix
   * @return ForumDiscussion
   */
  private static ForumTopic buildStaticForumTopicDomainObject( String string )
  {
    ForumTopic forumTopic = new ForumTopic();
    forumTopic.setId( new Long( 1 ) );
    forumTopic.setTopicCmAssetCode( "Topic Name" + string );
    forumTopic.setAudienceType( "Audience type" + string );
    ForumTopicDAO forumTopicDAO = getForumTopicDAO();
    forumTopicDAO.save( forumTopic );
    return forumTopic;
  }

}
