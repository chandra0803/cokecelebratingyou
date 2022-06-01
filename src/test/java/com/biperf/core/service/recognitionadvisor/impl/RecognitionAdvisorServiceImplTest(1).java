
package com.biperf.core.service.recognitionadvisor.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.recognitionadvisor.RecognitionAdvisorDao;
import com.biperf.core.domain.enums.ActivityType;
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
import com.biperf.core.ui.UnitTest;
import com.biperf.core.ui.recognitionadvisor.RecognitionAdvisorView;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

/**
 * Used for the RecognitonAdvisor Algorithm screen(s).
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

// TODO Must remember to refactor the class.

@RunWith( MockitoJUnitRunner.class )
public class RecognitionAdvisorServiceImplTest extends UnitTest
{
  public static final Long TEST_USER_ID = 100000L;

  @Mock
  RecognitionAdvisorDao recognitionAdvisorDao;

  @InjectMocks
  private RecognitionAdvisorServiceImpl recogAdvisorTest = new RecognitionAdvisorServiceImpl();

  @Test
  public void testshowRAReminderPaxData()
  {

    when( recognitionAdvisorDao.showRAReminderPaxData( buildRAPax().getId(), 0L, 8L, "NO_VALUE", "", 0L, "1", 1L ) ).thenReturn( buildRApaxRemiderList() );
    RecognitionAdvisorView actualResults = recogAdvisorTest.showRAReminderPaxData( buildRAPax().getId(), 0L, 8L, "NO_VALUE", "", 0L, "1", "1", 1L );
    assertNotNull( actualResults );
  }

  public RecognitionAdvisorValueBean buildRAPax()
  {
    RecognitionAdvisorValueBean recogAdvisorValueBean = new RecognitionAdvisorValueBean();
    recogAdvisorValueBean.setAvatarUrl( "Avatar Url" );
    recogAdvisorValueBean.setFirstName( "First Name" );
    recogAdvisorValueBean.setLastName( "Last Name" );
    recogAdvisorValueBean.setParticipantGroupType( 1 );
    recogAdvisorValueBean.setId( TEST_USER_ID );
    return recogAdvisorValueBean;
  }

  public List<RecognitionAdvisorValueBean> buildRApaxRemiderList()
  {
    RecognitionAdvisorValueBean recognitionAdvisorValueBean = new RecognitionAdvisorValueBean();
    recognitionAdvisorValueBean.setFirstName( buildRAPax().getFirstName() );
    recognitionAdvisorValueBean.setLastName( buildRAPax().getLastName() );
    recognitionAdvisorValueBean.setParticipantGroupType( buildRAPax().getParticipantGroupType() );
    recognitionAdvisorValueBean.setId( buildRAPax().getId() );
    List<RecognitionAdvisorValueBean> raValueBeanlist = new ArrayList<RecognitionAdvisorValueBean>();
    raValueBeanlist.add( recognitionAdvisorValueBean );

    return raValueBeanlist;
  }

  @Test
  public void getRAReminderOverDueEmailPaxData()
  {
    when( recognitionAdvisorDao.showRAReminderPaxData( buildRAPax().getId(), 0L, 8L, "NO_VALUE", "", 0L, "1", 1L ) ).thenReturn( buildRApaxRemiderList() );
    List<RecognitionAdvisorValueBean> lstRAOverDueValueBean = recogAdvisorTest.getRATeamMemberReminderData( 1050L );
    assertNotNull( lstRAOverDueValueBean );
  }

  @Test
  public void showRAReminderNewHireEmailPaxData()
  {
    when( recognitionAdvisorDao.getRANewHireForEmail( any() ) ).thenReturn( buildRANewHirePaxData() );
    List<RecognitionAdvisorValueBean> lstRANewHireValueBean = recogAdvisorTest.showRAReminderNewHireEmailPaxData( 1050L );
    assertNotNull( lstRANewHireValueBean );
  }

  @Test
  public void testCheckNewEmployeeAndTeamMemberExist()
  {
    String nhAndTmEmpCheckFlag = "Y"; // -ve Flow. Incorrect Pattern
    String[] a = nhAndTmEmpCheckFlag.split( "|" );
    List<AlertsValueBean> raAlertList = buildAlertValueBean( a );
    when( recognitionAdvisorDao.checkNewEmployeeAndTeamMemberExist( any() ) ).thenReturn( raAlertList );
    List<AlertsValueBean> raAlertLisrActual = recogAdvisorTest.checkNewEmployeeAndTeamMemberExist( 1050L );
    assertEquals( raAlertLisrActual, raAlertList );

    nhAndTmEmpCheckFlag = "Y|Y"; // +ve Flow.Same is applicable for Y|N
    a = nhAndTmEmpCheckFlag.split( "|" );
    raAlertList = buildAlertValueBean( a );
    when( recognitionAdvisorDao.checkNewEmployeeAndTeamMemberExist( any() ) ).thenReturn( raAlertList );
    raAlertLisrActual = recogAdvisorTest.checkNewEmployeeAndTeamMemberExist( 1050L );
    assertEquals( raAlertLisrActual.size(), 2 );
    assertEquals( raAlertLisrActual, raAlertList );

  }

  private Participant buildStaticParticipant()
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

  @Test
  public void testGetLongOverDueNewHireForManager()
  {
    User user = buildStaticParticipant();
    Participant pax = getNodeDetailsAndReportingPaxDetails( user );
    when( recognitionAdvisorDao.getLongOverDueNewHireForManager( user.getId() ) ).thenReturn( pax.getId() );
    recogAdvisorTest.getLongOverDueNewHireForManager( user.getId() );

  }

  private List<AlertsValueBean> buildAlertValueBean( String[] a )
  {
    List<AlertsValueBean> raAlertList = new ArrayList<AlertsValueBean>();
    if ( a != null && a[0] != null )
    {
      AlertsValueBean newEmpAlterBean = new AlertsValueBean();
      newEmpAlterBean = buildAlertsValueBeanForNewHire( a, newEmpAlterBean );

      raAlertList.add( newEmpAlterBean );
    }
    if ( a.length == 3 && a[2] != null )
    {
      AlertsValueBean oDEmpAlterBean = new AlertsValueBean();
      oDEmpAlterBean = buildAlertsValueBeanForOverDue( a, oDEmpAlterBean );

      raAlertList.add( oDEmpAlterBean );
    }
    return raAlertList;

  }

  private List<RecognitionAdvisorValueBean> buildRANewHirePaxData()
  {

    List<RecognitionAdvisorValueBean> raNewHireList = new ArrayList<RecognitionAdvisorValueBean>();

    RecognitionAdvisorValueBean raNewHireEmailValueBean = new RecognitionAdvisorValueBean();

    raNewHireEmailValueBean.setAvatarUrl( "Avatar Url1" );
    raNewHireEmailValueBean.setFirstName( "shan" );
    raNewHireEmailValueBean.setLastName( "mugam" );
    raNewHireEmailValueBean.setPositionType( "Developer" );

    RecognitionAdvisorValueBean raNewHireEmailValueBeanOne = new RecognitionAdvisorValueBean();
    raNewHireEmailValueBeanOne.setAvatarUrl( "Avatar Url2" );
    raNewHireEmailValueBeanOne.setFirstName( "Brian" );
    raNewHireEmailValueBeanOne.setLastName( "Ackerman" );
    raNewHireEmailValueBeanOne.setPositionType( "Emp" );

    RecognitionAdvisorValueBean raNewHireEmailValueBeanTwo = new RecognitionAdvisorValueBean();
    raNewHireEmailValueBeanTwo.setAvatarUrl( "Avatar Url" );
    raNewHireEmailValueBeanTwo.setFirstName( "Adetoye" );
    raNewHireEmailValueBeanTwo.setLastName( "Adekanmbi" );
    raNewHireEmailValueBeanTwo.setPositionType( "Manager" );

    raNewHireList.add( raNewHireEmailValueBean );
    raNewHireList.add( raNewHireEmailValueBeanOne );
    raNewHireList.add( raNewHireEmailValueBeanTwo );

    return raNewHireList;
  }

  private AlertsValueBean buildAlertsValueBeanForNewHire( String[] a, AlertsValueBean newEmpAlterBean )
  {
    newEmpAlterBean.setActivityType( ActivityType.RA_ALERT_NEW_HIRE );
    newEmpAlterBean.setNewHireAlert( a[0].toUpperCase().equals( "Y" ) ? true : false );
    return newEmpAlterBean;
  }

  private AlertsValueBean buildAlertsValueBeanForOverDue( String[] a, AlertsValueBean oDEmpAlterBean )
  {
    oDEmpAlterBean.setActivityType( ActivityType.RA_ALERT_OVER_DUE );
    oDEmpAlterBean.setOverDueAlert( a[2].toUpperCase().equals( "Y" ) ? true : false );
    return oDEmpAlterBean;
  }

  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( UserDAO.BEAN_NAME );
  }

  private NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( NodeDAO.BEAN_NAME );
  }

}
