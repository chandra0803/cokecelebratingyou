
package com.biperf.core.service.leaderboard.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.leaderboard.LeaderBoardDAO;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardPaxActivity;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.leaderboard.LeaderBoardParticipantAssociationRequest;

public class LeaderBoardServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public LeaderBoardServiceImplTest( String test )
  {
    super( test );
  }

  /** LeaderBoardServiceImpl */
  private LeaderBoardServiceImpl leaderboardService = new LeaderBoardServiceImpl();

  /** mockLeaderBoardDAO */
  private Mock mockLeaderBoardDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockLeaderBoardDAO = new Mock( LeaderBoardDAO.class );
    leaderboardService.setLeaderBoardDAO( (LeaderBoardDAO)mockLeaderBoardDAO.proxy() );

  }
  /* 
  *//**
    * Test getting an LeaderBoard by Date
    * @throws ServiceErrorException 
    */
  /*
   * public void testGetAllLeaderBoardsByDate() { // Get the test LeaderBoard. LeaderBoard
   * leaderboard = new LeaderBoard(); leaderboard.setId( new Long( 101 ) );
   * leaderboard.setStartDate( DateUtils.getFirstDayOfThisYear() ); leaderboard.setEndDate( new
   * Date()); leaderboard.setStatus( "A" ); mockLeaderBoardDAO.expects( once() ).method(
   * "getLeaderBoardById" ).with( same( new Long(101) ) ).will( returnValue( leaderboard ) );
   * mockLeaderBoardDAO.expects( once() ).method( "getAllLeaderBoardsByDate" ).with( same( new
   * Date(10-10-2011) ), same( new Date(10-10-2012)) ).will( returnValue( leaderboard ) );
   * AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
   * associationRequestCollection.add( new
   * LeaderBoardParticipantAssociationRequest(LeaderBoardParticipantAssociationRequest.EMPLOYEE));
   * leaderboardService.getLeaderBoardById( new Long(101)); mockLeaderBoardDAO.verify(); }
   *//**
    * Test getting an LeaderBoard by Date and Status
    *//*
       * public void testGetAllLeaderBoardsByDateAndStatus() { // Create the test LeaderBoard By
       * Date and Status. LeaderBoard leaderboard = new LeaderBoard(); leaderboard.setStartDate( new
       * Date(10-10-2011 ) ); leaderboard.setEndDate( new Date(10-10-2012 ) );
       * leaderboard.setStatus( "A" ); leaderboard.setId( new Long(101) );
       * mockLeaderBoardDAO.expects( once() ).method( "getAllLeaderBoardsByDateAndStatus" ).with(
       * same( new Date(10-10-2011) ), same(new Date(10-10-2012)), same("Acvtive") ).will(
       * returnValue( leaderboard ) ); mockLeaderBoardDAO.expects( once() ).method(
       * "getLeaderBoardById" ).with( same( new Long(101) ) ).will( returnValue( leaderboard ) );
       * AssociationRequestCollection associationRequestCollection = new
       * AssociationRequestCollection(); associationRequestCollection.add( new
       * LeaderBoardParticipantAssociationRequest(LeaderBoardParticipantAssociationRequest.EMPLOYEE)
       * ); leaderboardService.getLeaderBoardById( new Long(101)); mockLeaderBoardDAO.verify(); }
       */

  /**
   * Test getting the LeaderBoard by id.
   */
  public void testGetLeaderBoardById()
  {
    // Get the test LeaderBoard.
    LeaderBoard leaderboard = new LeaderBoard();
    leaderboard.setId( new Long( 101 ) );
    leaderboard.setName( "TestServiceMessage" );
    leaderboard.setActivityTitle( "TestServiceMessageTitle" );

    User user = new User();
    user.setId( new Long( 1 ) );
    leaderboard.setUser( user );

    // LeaderBoardDAO expected to call getLeaderBoardById once with the LeaderBoarId which will
    // return the LeaderBoar expected
    mockLeaderBoardDAO.expects( once() ).method( "getLeaderBoardById" ).with( same( leaderboard.getId() ) ).will( returnValue( leaderboard ) );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new LeaderBoardParticipantAssociationRequest( LeaderBoardParticipantAssociationRequest.EMPLOYEE ) );
    LeaderBoard actualLeaderboard = leaderboardService.getLeaderBoardById( leaderboard.getId() );

    assertEquals( "Actual leaderboard does not match to what was expected", leaderboard, actualLeaderboard );

    mockLeaderBoardDAO.verify();
  }

  /**
   * Test saving the LeaderBoard through the service.
   * 
   * @throws ServiceErrorException
   * @throws UniqueConstraintViolationException 
   */
  public void testsaveLeaderBoard()
      // throws UniqueConstraintViolationException,
      throws ServiceErrorException, UniqueConstraintViolationException
  {

    LeaderBoard leaderboard = new LeaderBoard();
    leaderboard.setId( new Long( 101 ) );

    User user = new User();
    user.setId( new Long( 1 ) );
    leaderboard.setUser( user );

    LeaderBoard dbLeaderBoard = new LeaderBoard();
    dbLeaderBoard.setId( new Long( 101 ) );

    // save the user
    mockLeaderBoardDAO.expects( once() ).method( "saveLeaderBoard" ).with( same( leaderboard ) ).will( returnValue( leaderboard ) );

    // test the leaderboardService.saveLeaderBoard
    leaderboardService.saveLeaderBoard( leaderboard );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockLeaderBoardDAO.verify();
  }

  /**
   * Test saving  the leaderBoardParticipant.
   * @throws ServiceErrorException 
   */
  public void testSaveLeaderBoardParticipant() throws ServiceErrorException
  {

    // Create the test LeaderBoardParticipant.
    LeaderBoardParticipant leaderBoardParticipant = new LeaderBoardParticipant();
    leaderBoardParticipant.setUser( new Participant() );

    mockLeaderBoardDAO.expects( once() ).method( "saveLeaderBoardParticipant" ).with( same( leaderBoardParticipant ) ).will( returnValue( leaderBoardParticipant ) );

    LeaderBoardParticipant actualLeaderBoardParticipant = this.leaderboardService.saveLeaderBoardParticipant( leaderBoardParticipant );

    assertEquals( "Actual saved leaderBoardParticipant wasn't equal to what was expected", leaderBoardParticipant, actualLeaderBoardParticipant );

    mockLeaderBoardDAO.verify();
  }

  /**
   * Test getting the leaderBoardParticipant by the id.
   * @throws ServiceErrorException 
   */
  public void testGetLeaderBoardParticipantById() throws ServiceErrorException
  {

    LeaderBoardParticipant leaderBoardParticipant = new LeaderBoardParticipant();
    leaderBoardParticipant.setId( new Long( 1 ) );
    Participant user = new Participant();

    user.setId( new Long( 1 ) );
    leaderBoardParticipant.setUser( user );

    mockLeaderBoardDAO.expects( once() ).method( "getLeaderBoardParticipantById" ).with( same( leaderBoardParticipant.getId() ) ).will( returnValue( leaderBoardParticipant ) );

    LeaderBoardParticipant actualLeaderBoardParticipant = leaderboardService.getLeaderBoardParticipantById( leaderBoardParticipant.getId() );

    assertEquals( "Actual leaderBoardParticipant wasn't equal to what was expected", leaderBoardParticipant, actualLeaderBoardParticipant );

    mockLeaderBoardDAO.verify();

  }

  /**
   * Test getting the leaderBoardPaxActivity by the id.
   */
  public void testGetLeaderBoardPaxActivityById()
  {

    LeaderBoardPaxActivity leaderBoardPaxActivity = new LeaderBoardPaxActivity();
    leaderBoardPaxActivity.setId( new Long( 11 ) );

    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    leaderBoardPaxActivity.setUser( new User() );

    mockLeaderBoardDAO.expects( once() ).method( "getLeaderBoardPaxActivityById" ).with( same( leaderBoardPaxActivity.getId() ) ).will( returnValue( leaderBoardPaxActivity ) );

    LeaderBoardPaxActivity actualLeaderBoardPaxActivity = this.leaderboardService.getLeaderBoardPaxActivityById( leaderBoardPaxActivity.getId() );

    assertEquals( "Actual leaderBoardPaxActivity wasn't equal to what was expected", leaderBoardPaxActivity, actualLeaderBoardPaxActivity );

    mockLeaderBoardDAO.verify();

  }

  /**
   * Test saving  the leaderBoardPaxActivity.
   * @throws ServiceErrorException 
   */
  public void testSaveLeaderBoardPaxActivity() throws ServiceErrorException
  {

    // Create the test LeaderBoardPaxActivity.
    LeaderBoardPaxActivity leaderBoardPaxActivity = new LeaderBoardPaxActivity();
    leaderBoardPaxActivity.setId( new Long( 11 ) );

    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    leaderBoardPaxActivity.setUser( new User() );

    mockLeaderBoardDAO.expects( once() ).method( "saveLeaderBoardPaxActivity" ).with( same( leaderBoardPaxActivity ) ).will( returnValue( leaderBoardPaxActivity ) );

    LeaderBoardPaxActivity actualLeaderBoardPaxActivity = this.leaderboardService.saveLeaderBoardPaxActivity( leaderBoardPaxActivity );

    assertEquals( "Actual saved leaderBoardPaxActivity wasn't equal to what was expected", leaderBoardPaxActivity, actualLeaderBoardPaxActivity );

    mockLeaderBoardDAO.verify();
  }

}
