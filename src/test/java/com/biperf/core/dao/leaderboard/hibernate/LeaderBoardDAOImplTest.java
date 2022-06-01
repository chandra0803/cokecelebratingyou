
package com.biperf.core.dao.leaderboard.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.leaderboard.LeaderBoardDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardPaxActivity;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.ApplicationContextFactory;

public class LeaderBoardDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns an {@link LeaderBoardDAO} object.
   * 
   * @return an {@link LeaderBoardDAO} object.
   */
  private static LeaderBoardDAO getLeaderBoardDAO()
  {
    return (LeaderBoardDAO)ApplicationContextFactory.getApplicationContext().getBean( "leaderBoardDAO" );
  }

  /**
   * Get the LeaderBoardDAO.
   * 
   * @return LeaderBoardDAO
   */
  private static LeaderBoardDAO getLeaderBoardParticipantDAO()
  {
    return (LeaderBoardDAO)ApplicationContextFactory.getApplicationContext().getBean( "leaderBoardDAO" );
  }

  /**
   * Get the ParticipantDAO
   * 
   * @return ParticipantDAO
   */
  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  /**
   * Get the roleDAO from the beanFactory.
   * 
   * @return RoleDAO
   */
  protected RoleDAO getRoleDAO()
  {
    return (RoleDAO)ApplicationContextFactory.getApplicationContext().getBean( "roleDAO" );
  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)getDAO( "userDAO" );
  }

  /**
   * Get the AclDAO from the beanFactory.
   * 
   * @return AclDAO
   */
  protected AclDAO getAclDAO()
  {
    return (AclDAO)ApplicationContextFactory.getApplicationContext().getBean( "aclDAO" );
  }

  /**
   * Get the LeaderBoardDAO.
   * 
   * @return LeaderBoardDAO
   */
  private static LeaderBoardDAO getLeaderBoardPaxActivityDAO()
  {
    return (LeaderBoardDAO)ApplicationContextFactory.getApplicationContext().getBean( "leaderBoardDAO" );
  }

  // LeaderBoard methods
  public void testSaveLeaderBoardAndGetLeaderBoardById()
  {

    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();

    LeaderBoard expectedLeaderBoard = new LeaderBoard();

    UserDAO userDAO = getUserDAO();
    // User user = UserDAOImplTest.buildStaticUser( "JunitTestUser1", "first1", "last1" );
    User user = userDAO.getUserById( 5581L );
    expectedLeaderBoard.setName( "Test LeaderBoard" );
    expectedLeaderBoard.setStatus( "A" );
    expectedLeaderBoard.setUser( user );
    leaderBoardDAO.saveLeaderBoard( expectedLeaderBoard );
    flushAndClearSession();

    LeaderBoard actualLeaderBoard = leaderBoardDAO.getLeaderBoardById( expectedLeaderBoard.getId() );
    assertEquals( "LeaderBoard not equal", expectedLeaderBoard, actualLeaderBoard );

  }

  public void testGetLeaderBoardsByPaxAndOwnerUserId()
  {
    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    Set<LeaderBoardParticipant> paxSet1 = new HashSet<LeaderBoardParticipant>();
    Set<LeaderBoardParticipant> paxSet2 = new HashSet<LeaderBoardParticipant>();
    Set<LeaderBoardParticipant> paxSet3 = new HashSet<LeaderBoardParticipant>();

    LeaderBoardParticipant leaderPax1 = new LeaderBoardParticipant();
    LeaderBoardParticipant leaderPax2 = new LeaderBoardParticipant();
    LeaderBoardParticipant leaderPax3 = new LeaderBoardParticipant();

    UserDAO userDAO = getUserDAO();

    LeaderBoard leaderBoard1 = new LeaderBoard();
    leaderBoard1.setName( "LB1" );
    leaderBoard1.setStatus( "C" );
    leaderBoard1.setStartDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 5 ) ) );
    leaderBoard1.setEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 50 ) ) );
    leaderBoard1.setDisplayEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 60 ) ) );

    User user1 = new User();
    user1 = userDAO.getUserById( 5583L );
    leaderBoard1.setUser( user1 );

    leaderPax1.setActive( true );
    leaderPax1.setAsOfDate( new Date( new Date().getTime() ) );
    // leaderPax.setId( 250L );
    leaderPax1.setLeaderboard( leaderBoard1 );
    leaderPax1.setParticipantRank( 1 );
    leaderPax1.setScore( "12" );
    leaderPax1.setUser( (Participant)user1 );
    paxSet1.add( leaderPax1 );
    leaderBoard1.setParticipants( paxSet1 );
    leaderBoardDAO.saveLeaderBoard( leaderBoard1 );

    LeaderBoard leaderBoard2 = new LeaderBoard();
    leaderBoard2.setName( "LB2" );
    leaderBoard2.setStatus( "C" );
    leaderBoard2.setStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 5 ) ) );
    leaderBoard2.setEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 50 ) ) );
    leaderBoard2.setDisplayEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 60 ) ) );
    leaderBoard2.setUser( user1 );

    leaderPax2.setActive( true );
    leaderPax2.setAsOfDate( new Date( new Date().getTime() ) );
    // leaderPax.setId( 251L );
    leaderPax2.setLeaderboard( leaderBoard2 );
    leaderPax2.setParticipantRank( 1 );
    leaderPax2.setScore( "12" );
    leaderPax2.setUser( (Participant)user1 );
    paxSet2.add( leaderPax2 );

    leaderBoard2.setParticipants( paxSet2 );

    leaderBoardDAO.saveLeaderBoard( leaderBoard2 );

    LeaderBoard leaderBoard3 = new LeaderBoard();
    leaderBoard3.setName( "LB3" );
    leaderBoard3.setStatus( "U" );
    leaderBoard3.setStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 50 ) ) );
    leaderBoard3.setEndDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 5 ) ) );
    leaderBoard3.setDisplayEndDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 60 ) ) );
    // leaderPax.setId( 252L );
    leaderPax3.setActive( true );
    leaderPax3.setAsOfDate( new Date( new Date().getTime() ) );
    leaderPax3.setLeaderboard( leaderBoard3 );
    leaderPax3.setParticipantRank( 1 );
    leaderPax3.setScore( "12" );
    leaderPax3.setUser( (Participant)user1 );
    paxSet3.add( leaderPax3 );
    leaderBoard3.setParticipants( paxSet3 );
    leaderBoard3.setUser( user1 );
    leaderBoardDAO.saveLeaderBoard( leaderBoard3 );
    flushAndClearSession();

    List<LeaderBoard> resultWithoutDetail = leaderBoardDAO.getLeaderBoardsForTile( 5583L );
    assertTrue( "LB2 will come under this criteria, So should be greathan or equal to 1", resultWithoutDetail.size() >= 1 );
    assertTrue( "LB2 id is not null", leaderBoard2.getId() != null );
    List<LeaderBoard> resultWithDetail = leaderBoardDAO.getLeaderBoardsForTile( 5583L );
    assertTrue( "All LBs will be fetched. So, size should be 1 or more", resultWithDetail.size() >= 1 );
    assertTrue( "Checking for ids", leaderBoard3.getId() != null );
  }

  public void testGetLeaderBoardsByUserIdAndStatus()
  {
    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    UserDAO userDAO = getUserDAO();

    User user1 = new User();
    user1 = userDAO.getUserById( 5581L );
    userDAO.saveUser( user1 );

    LeaderBoard leaderBoard1 = new LeaderBoard();
    leaderBoard1.setUser( user1 );
    leaderBoard1.setStatus( "C" );
    leaderBoard1.setName( "Test LeaderBoard1" );
    leaderBoardDAO.saveLeaderBoard( leaderBoard1 );

    User user2 = new User();
    user2 = userDAO.getUserById( 5581L );
    userDAO.saveUser( user2 );

    LeaderBoard leaderBoard2 = new LeaderBoard();
    leaderBoard2.setUser( user2 );
    leaderBoard2.setStatus( "C" );
    leaderBoard2.setName( "Test LeaderBoard2" );
    leaderBoardDAO.saveLeaderBoard( leaderBoard2 );

    List leaderBoardsByUserOne = leaderBoardDAO.getLeaderBoardsByOwnerUserIdAndStatus( user1.getId(), "C" );
    assertTrue( "Size should be greater then are equal to two", leaderBoardsByUserOne.size() >= 2 );

    List leaderBoardsByUserTwo = leaderBoardDAO.getLeaderBoardsByOwnerUserIdAndStatus( user2.getId(), "C" );
    assertTrue( "Size should be greater then are equal to two", leaderBoardsByUserTwo.size() >= 2 );
  }

  public void testGetLeaderBoardsByStatus()
  {
    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    UserDAO userDAO = getUserDAO();

    LeaderBoard leaderBoard1 = new LeaderBoard();
    leaderBoard1.setName( "LB1" );
    leaderBoard1.setStatus( "C" );
    leaderBoard1.setStartDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 5 ) ) );
    leaderBoard1.setEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 50 ) ) );
    leaderBoard1.setDisplayEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 60 ) ) );

    User user1 = new User();
    user1 = userDAO.getUserById( 5581L );
    leaderBoard1.setUser( user1 );
    leaderBoardDAO.saveLeaderBoard( leaderBoard1 );

    LeaderBoard leaderBoard2 = new LeaderBoard();
    leaderBoard2.setName( "LB2" );
    leaderBoard2.setStatus( "C" );
    leaderBoard2.setStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 5 ) ) );
    leaderBoard2.setEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 50 ) ) );
    leaderBoard2.setDisplayEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 60 ) ) );

    leaderBoard2.setUser( user1 );
    leaderBoardDAO.saveLeaderBoard( leaderBoard2 );

    LeaderBoard leaderBoard3 = new LeaderBoard();
    leaderBoard3.setName( "LB3" );
    leaderBoard3.setStatus( "D" );
    leaderBoard3.setStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 50 ) ) );
    leaderBoard3.setEndDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 5 ) ) );
    leaderBoard3.setDisplayEndDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 30 ) ) );

    leaderBoard3.setUser( user1 );
    leaderBoardDAO.saveLeaderBoard( leaderBoard3 );
    flushAndClearSession();

    List<LeaderBoard> completedLeaderBoards = leaderBoardDAO.getLeaderBoardByStatus( LeaderBoard.COMPLETED );
    List<LeaderBoard> liveLeaderBoards = leaderBoardDAO.getLeaderBoardByStatus( LeaderBoard.LIVE );
    List<LeaderBoard> expiredLeaderBoards = leaderBoardDAO.getLeaderBoardByStatus( LeaderBoard.EXPIRED );

    assertTrue( "completedLeaderBoards should be atleast one", completedLeaderBoards.size() >= 1 );
    assertTrue( "liveLeaderBoards should be atleast one", liveLeaderBoards.size() >= 1 );
    assertTrue( "expiredLeaderBoards should be atleast one", expiredLeaderBoards.size() >= 1 );
  }

  public void testSaveLeaderBoardParticipantAndGetById()
  {
    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    UserDAO userDAO = getUserDAO();
    ParticipantDAO participantDAO = getParticipantDAO();

    LeaderBoard leaderBoard = new LeaderBoard();

    User leaderBoardOwner = userDAO.getUserById( 5581L );
    leaderBoard.setUser( leaderBoardOwner );
    leaderBoard.setStatus( "A" );
    leaderBoard.setName( "Test Leader Board" );

    leaderBoardDAO.saveLeaderBoard( leaderBoard );

    LeaderBoardParticipant leaderBoardParticipant = new LeaderBoardParticipant();

    leaderBoardParticipant.setLeaderboard( leaderBoard );

    Participant leaderBoardParticipantDetails = participantDAO.getParticipantById( 5582L );
    leaderBoardParticipant.setUser( leaderBoardParticipantDetails );
    leaderBoardParticipant.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant );

    flushAndClearSession();
    LeaderBoardParticipant actualLeaderBoardParticipant = leaderBoardDAO.getLeaderBoardParticipantById( leaderBoardParticipant.getId() );
    assertEquals( "LeaderBoard not equal", leaderBoardParticipant, actualLeaderBoardParticipant );
  }

  public void testSaveLeaderBoardParticipantsList()
  {
    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    ParticipantDAO participantDAO = getParticipantDAO();

    LeaderBoard leaderBoard = new LeaderBoard();

    Participant leaderBoardOwner = participantDAO.getParticipantById( 5581L );
    leaderBoard.setUser( leaderBoardOwner );
    leaderBoard.setStatus( "A" );
    leaderBoard.setName( "Test Leader Board" );

    leaderBoardDAO.saveLeaderBoard( leaderBoard );

    LeaderBoardParticipant leaderBoardParticipant1 = new LeaderBoardParticipant();
    LeaderBoardParticipant leaderBoardParticipant2 = new LeaderBoardParticipant();
    LeaderBoardParticipant leaderBoardParticipant3 = new LeaderBoardParticipant();

    leaderBoardParticipant1.setLeaderboard( leaderBoard );
    leaderBoardParticipant2.setLeaderboard( leaderBoard );
    leaderBoardParticipant3.setLeaderboard( leaderBoard );

    Participant leaderBoardParticipantDetails1 = participantDAO.getParticipantById( 5582L );
    leaderBoardParticipant1.setUser( leaderBoardParticipantDetails1 );
    leaderBoardParticipant1.setScore( "100" );
    Participant leaderBoardParticipantDetails2 = participantDAO.getParticipantById( 5583L );
    leaderBoardParticipant2.setUser( leaderBoardParticipantDetails2 );
    leaderBoardParticipant2.setScore( "100" );
    Participant leaderBoardParticipantDetails3 = participantDAO.getParticipantById( 5584L );
    leaderBoardParticipant3.setUser( leaderBoardParticipantDetails3 );
    leaderBoardParticipant3.setScore( "100" );

    List<LeaderBoardParticipant> list = new ArrayList<LeaderBoardParticipant>();

    list.add( leaderBoardParticipant1 );
    list.add( leaderBoardParticipant2 );
    list.add( leaderBoardParticipant3 );

    leaderBoardDAO.saveLeaderBoardParticipantsList( list );
    flushAndClearSession();
    for ( LeaderBoardParticipant lbp : list )
    {
      assertTrue( lbp.getId() != null );
    }

  }

  public void testGetLeaderBoardParticipantsByLeaderBoardId()
  {
    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();

    ParticipantDAO participantDAO = getParticipantDAO();

    LeaderBoard leaderBoard = new LeaderBoard();

    Participant leaderBoardOwner = participantDAO.getParticipantById( 5581L );
    leaderBoard.setUser( leaderBoardOwner );
    leaderBoard.setStatus( "A" );
    leaderBoard.setName( "Test Leader Board" );

    leaderBoardDAO.saveLeaderBoard( leaderBoard );

    LeaderBoardParticipant leaderBoardParticipant1 = new LeaderBoardParticipant();
    leaderBoardParticipant1.setLeaderboard( leaderBoard );

    Participant leaderBoardParticipant1Details = participantDAO.getParticipantById( 5582L );
    leaderBoardParticipant1.setUser( leaderBoardParticipant1Details );
    leaderBoardParticipant1.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant1 );

    LeaderBoardParticipant leaderBoardParticipant2 = new LeaderBoardParticipant();
    leaderBoardParticipant2.setLeaderboard( leaderBoard );

    Participant leaderBoardParticipant2Details = participantDAO.getParticipantById( 5583L );
    leaderBoardParticipant2.setUser( leaderBoardParticipant2Details );
    leaderBoardParticipant2.setScore( "500" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant2 );

    List leaderBoardParticipantsList = leaderBoardDAO.getLeaderBoardParticipantsByLeaderBoardId( leaderBoard.getId(), LeaderBoardParticipant.ALL_LB_PAX );
    assertTrue( "Size of LeaderBoardParticipants Should Be two or more.", leaderBoardParticipantsList.size() >= 2 );
  }

  public void testSaveLeaderBoardPaxActivityAndGetById()
  {

    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();

    ParticipantDAO participantDAO = getParticipantDAO();

    LeaderBoard leaderBoard1 = new LeaderBoard();

    Participant leaderBoardOwner1 = participantDAO.getParticipantById( 5581L );
    leaderBoard1.setUser( leaderBoardOwner1 );
    leaderBoard1.setStatus( "A" );
    leaderBoard1.setName( "Test Leader Board1" );
    leaderBoard1.setStartDate( new Date() );

    leaderBoardDAO.saveLeaderBoard( leaderBoard1 );

    LeaderBoard leaderBoard2 = new LeaderBoard();

    Participant leaderBoardOwner2 = participantDAO.getParticipantById( 5584L );
    leaderBoard2.setUser( leaderBoardOwner2 );
    leaderBoard2.setStatus( "A" );
    leaderBoard2.setName( "Test Leader Board2" );
    leaderBoard2.setStartDate( new Date() );

    leaderBoardDAO.saveLeaderBoard( leaderBoard2 );

    LeaderBoardParticipant leaderBoardParticipant1 = new LeaderBoardParticipant();
    leaderBoardParticipant1.setLeaderboard( leaderBoard1 );
    Participant participant = participantDAO.getParticipantById( 5582L );
    leaderBoardParticipant1.setUser( participant );
    leaderBoardParticipant1.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant1 );

    LeaderBoardPaxActivity leaderBoardPaxActivity = new LeaderBoardPaxActivity();

    leaderBoardPaxActivity.setLeaderBoard( leaderBoard1 );
    leaderBoardPaxActivity.setUser( participant );

    leaderBoardPaxActivity.setSubmissionDate( new Date() );
    leaderBoardPaxActivity.setType( "Test Type" );
    leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivity );

    LeaderBoardPaxActivity lbpActivity = leaderBoardDAO.getLeaderBoardPaxActivityById( leaderBoardPaxActivity.getId() );

    assertEquals( leaderBoardPaxActivity, lbpActivity );
  }

  public void testGetLeaderBoardPaxActivitiesByUserId()
  {

    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    ParticipantDAO participantDAO = getParticipantDAO();

    LeaderBoard leaderBoard1 = new LeaderBoard();

    Participant leaderBoardOwner1 = participantDAO.getParticipantById( 5581L );
    leaderBoard1.setUser( leaderBoardOwner1 );
    leaderBoard1.setStatus( "A" );
    leaderBoard1.setName( "Test Leader Board1" );
    leaderBoard1.setStartDate( new Date() );

    leaderBoardDAO.saveLeaderBoard( leaderBoard1 );

    LeaderBoard leaderBoard2 = new LeaderBoard();

    Participant leaderBoardOwner2 = participantDAO.getParticipantById( 5584L );
    leaderBoard2.setUser( leaderBoardOwner2 );
    leaderBoard2.setStatus( "A" );
    leaderBoard2.setName( "Test Leader Board2" );
    leaderBoard2.setStartDate( new Date() );

    leaderBoardDAO.saveLeaderBoard( leaderBoard2 );

    LeaderBoardParticipant leaderBoardParticipant1 = new LeaderBoardParticipant();
    leaderBoardParticipant1.setLeaderboard( leaderBoard1 );
    Participant participant = participantDAO.getParticipantById( 5582L );
    leaderBoardParticipant1.setUser( participant );
    leaderBoardParticipant1.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant1 );

    LeaderBoardParticipant leaderBoardParticipant2 = new LeaderBoardParticipant();
    leaderBoardParticipant2.setLeaderboard( leaderBoard2 );
    leaderBoardParticipant2.setUser( participant );
    leaderBoardParticipant2.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant2 );

    LeaderBoardPaxActivity leaderBoardPaxActivity1 = new LeaderBoardPaxActivity();

    leaderBoardPaxActivity1.setLeaderBoard( leaderBoard1 );
    leaderBoardPaxActivity1.setUser( participant );
    leaderBoardPaxActivity1.setSubmissionDate( new Date() );
    leaderBoardPaxActivity1.setType( "Test Type" );
    leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivity1 );

    LeaderBoardPaxActivity leaderBoardPaxActivity2 = new LeaderBoardPaxActivity();

    leaderBoardPaxActivity2.setLeaderBoard( leaderBoard2 );
    leaderBoardPaxActivity2.setUser( participant );
    leaderBoardPaxActivity2.setSubmissionDate( new Date() );
    leaderBoardPaxActivity2.setType( "Test Type" );
    leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivity2 );

    List<LeaderBoardPaxActivity> activities = leaderBoardDAO.getLeaderBoardPaxActivityByUserId( participant.getId() );

    assertTrue( "List should be greater then are equal to 2", activities.size() >= 2 );

  }

  public void testGetLeaderBoardPaxActivitiesByLeaderBoardId()
  {

    LeaderBoardDAO leaderBoardDAO = getLeaderBoardDAO();
    ParticipantDAO participantDAO = getParticipantDAO();

    LeaderBoard leaderBoard = new LeaderBoard();

    Participant leaderBoardOwner = participantDAO.getParticipantById( 5581L );
    leaderBoard.setUser( leaderBoardOwner );
    leaderBoard.setStatus( "A" );
    leaderBoard.setName( "Test Leader Board" );
    leaderBoard.setStartDate( new Date() );

    leaderBoardDAO.saveLeaderBoard( leaderBoard );

    LeaderBoardParticipant leaderBoardParticipant1 = new LeaderBoardParticipant();
    leaderBoardParticipant1.setLeaderboard( leaderBoard );
    Participant participant1 = participantDAO.getParticipantById( 5582L );
    leaderBoardParticipant1.setUser( participant1 );
    leaderBoardParticipant1.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant1 );

    LeaderBoardParticipant leaderBoardParticipant2 = new LeaderBoardParticipant();
    leaderBoardParticipant2.setLeaderboard( leaderBoard );
    Participant participant2 = participantDAO.getParticipantById( 5585L );
    leaderBoardParticipant2.setUser( participant2 );
    leaderBoardParticipant2.setScore( "100" );

    leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipant2 );

    LeaderBoardPaxActivity leaderBoardPaxActivity1 = new LeaderBoardPaxActivity();

    leaderBoardPaxActivity1.setLeaderBoard( leaderBoard );
    leaderBoardPaxActivity1.setUser( participant1 );
    leaderBoardPaxActivity1.setSubmissionDate( new Date() );
    leaderBoardPaxActivity1.setType( "Test Type" );
    leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivity1 );

    LeaderBoardPaxActivity leaderBoardPaxActivity2 = new LeaderBoardPaxActivity();

    leaderBoardPaxActivity2.setLeaderBoard( leaderBoard );
    leaderBoardPaxActivity2.setUser( participant2 );
    leaderBoardPaxActivity2.setSubmissionDate( new Date() );
    leaderBoardPaxActivity2.setType( "Test Type" );
    leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivity2 );

    List<LeaderBoardPaxActivity> activities = leaderBoardDAO.getLeaderBoardPaxActivityByLeaderBoardId( leaderBoard.getId() );

    assertTrue( "List should be greater then are equal to 2", activities.size() >= 2 );

  }

}
