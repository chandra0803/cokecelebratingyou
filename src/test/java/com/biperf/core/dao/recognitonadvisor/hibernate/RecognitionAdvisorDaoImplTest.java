/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/quiz/hibernate/QuizDAOImplTest.java,v $
 */

package com.biperf.core.dao.recognitonadvisor.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.recognitionadvisor.RecognitionAdvisorDao;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

/**
 * RecognitionAdvisorDaoImplTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ramesh J</td>
 * <td>Dec 11, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionAdvisorDaoImplTest extends BaseDAOTest
{

  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   *
   * @return RecognitionAdvisorDao
   */
  private RecognitionAdvisorDao getRecognitionAdvisorDao()
  {
    return (RecognitionAdvisorDao)getDAO( "recognitionAdvisorDao" );
  }

  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)getDAO( "userDAO" );
  }

  private NodeDAO getNodeDAO()
  {
    return (NodeDAO)getDAO( "nodeDAO" );
  }

  public void testShowRAReminderPaxData()
  {
    User savedUser = buildAndSaveParticipant();
    List<RecognitionAdvisorValueBean> raReminderPaxData = getRecognitionAdvisorDao().showRAReminderPaxData( savedUser.getId(), 0L, 8L, "NO_VALUE", "", 3L, "1", 1L );
    assertNotNull( raReminderPaxData );

  }

  private Participant buildAndSaveParticipant()
  {
    Participant participant = buildStaticParticipant();

    Participant savedParticipant = getParticipantDAO().saveParticipant( participant );
    flushAndClearSession();
    return savedParticipant;
  }

  /**
   * Build a static participant for testing.
   * 
   * @return Participant
   */
  public Participant buildStaticParticipant()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );
    Participant participant = buildStaticParticipant( "testPAXUSERNAME" + uniqueName, "testFIRSTNAME", "testLASTNAME" );

    return participant;
  }

  /**
   * Builds a participant with the unique params.
   * 
   * @param username
   * @param firstname
   * @param lastname
   * @return Participant
   */
  private Participant buildStaticParticipant( String username, String firstname, String lastname )
  {
    Participant participant = new Participant();

    // Set the param data onto the participant
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    participant.setFirstName( firstname );
    participant.setLastName( lastname );
    participant.setUserName( username );

    // Add the remaining bits onto the participant
    completeParticipant( participant );

    return participant;

  }

  /**
   * Attaches the remaining bits onto the participant. This is seperate to avoid needing to repeat
   * it each time when building a Participant.
   * 
   * @param participant
   */
  private void completeParticipant( Participant participant )
  {

    participant.setPassword( "testPASSWORD" );

    participant.setActive( Boolean.TRUE );
    participant.setWelcomeEmailSent( Boolean.FALSE );
    participant.setMasterUserId( new Long( 1 ) );
    participant.setLoginFailuresCount( new Integer( 0 ) );
    participant.setLastResetDate( new Date() );

    // from Participant
    participant.setAwardBanqExtractDate( new Date() );
    participant.setAwardBanqNumber( "testABN" );
    participant.setCentraxId( "testCXID" );
    participant.setRelationshipType( ParticipantRelationshipType.getDefaultItem() );
    participant.setSuspensionStatus( ParticipantSuspensionStatus.getDefaultItem() );
    participant.setStatus( ParticipantStatus.getDefaultItem() );
    participant.setStatusChangeDate( new Date() );
    participant.setPassword( "testpassword" );

    ParticipantDAO paxDAO = getParticipantDAO();
    paxDAO.saveParticipant( participant );

  }

  public void testShowRAReminderNewHirePaxData()
  {
    User savedUser = buildAndSaveParticipant();
    List<RecognitionAdvisorValueBean> raReminderNewHirePaxData = getRecognitionAdvisorDao().getRANewHireForEmail( savedUser.getId() );
    assertNotNull( raReminderNewHirePaxData );

  }

  public void testShowRAReminderOverDuePaxData()
  {
    User savedUser = buildAndSaveParticipant();
    List<RecognitionAdvisorValueBean> raReminderOverDuePaxData = getRecognitionAdvisorDao().getRATeamMemberReminderData( savedUser.getId() );
    assertNotNull( raReminderOverDuePaxData );

  }

  public void testGetLongOverDueNewHireForManager()
  {
    User savedUser = buildAndSaveParticipant();

    // -Ve Flow. Logged in user Id is null. Proc returns bad request code

    Long id = getRecognitionAdvisorDao().getLongOverDueNewHireForManager( null );
    assertEquals( null, id );
    // + ve flow. Manager has a reportee (overdue/new hire).

    Participant pax = getNodeDetailsAndReportingPaxDetails( savedUser );

    id = getRecognitionAdvisorDao().getLongOverDueNewHireForManager( savedUser.getId() );

    assertNotNull( id );
    assertEquals( id, pax.getId() );

  }

  public void testCheckNewEmployeeAndTeamMemberExist()
  {

    User user = buildAndSaveParticipant();
    List<AlertsValueBean> alertsValueBean = getRecognitionAdvisorDao().checkNewEmployeeAndTeamMemberExist( user.getId() );
    assertNotNull( alertsValueBean );

  }

  private Participant getNodeDetailsAndReportingPaxDetails( User loggedInUser )
  {
    NodeDAO nodeDAO = getNodeDAO();
    UserDAO userDAO = getUserDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setName( "Test NodeHierarchy2" );
    hierarchy1.setDescription( "description goes here" );
    hierarchy1.setPrimary( false );
    hierarchy1.setActive( true );
    hierarchy1.setCmAssetCode( "CM name ASSET" );
    hierarchy1.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    NodeType nodeType1 = new NodeType();
    nodeType1.setName( "testNodeType" );
    nodeType1.setCmAssetCode( "test.asset" );
    nodeType1.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );

    Node node1 = new Node();
    node1.setName( "Test NodeHierarchy1" );
    node1.setDescription( "description goes here" );
    node1.setParentNode( parentNode );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy1 );
    node1.setPath( parentNode.getPath() + node1.getName() );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "Test NodeHierarchy2" );
    node2.setDescription( "description goes here" );
    node2.setParentNode( node1 );
    node2.setNodeType( nodeType1 );
    node2.setPath( node1.getPath() + node2.getName() );
    node2.setHierarchy( hierarchy1 );
    nodeDAO.saveNode( node2 );

    UserNode userNode = new UserNode();
    userNode.setNode( node1 );
    userNode.setIsPrimary( true );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );
    loggedInUser.addUserNode( userNode );
    userDAO.saveUser( loggedInUser );
    User loggedInUser1 = buildStaticParticipant();
    Participant participant = (Participant)loggedInUser1;

    userNode.setNode( node1 );
    userNode.setIsPrimary( true );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    loggedInUser1.addUserNode( userNode );

    userDAO.saveUser( loggedInUser1 );
    return participant;

  }

}
