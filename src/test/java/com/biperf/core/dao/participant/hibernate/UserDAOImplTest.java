/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/participant/hibernate/UserDAOImplTest.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.Acl;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserLoginInfo;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * Exercises DAO implementations through JUnit.
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
 * <td>crosenquest</td>
 * <td>Apr 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserDAOImplTest extends BaseDAOTest
{
  /**
   * userDAO
   */
  private static UserDAO userDAO;

  /**
   * Get the userDAO from the beanFactory.
   * 
   * @return UserDAO
   */
  static
  {
    userDAO = (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
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

  /**
   * Get the ParticipantDAO from the beanFactory.
   * 
   * @return ParticipantDAO
   */
  protected ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "participantDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   * 
   * @return NodeTypeDAO
   */
  protected NodeTypeDAO getNodeTypeDAO()
  {
    return (NodeTypeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeTypeDAO" );
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
   * Get the NodeDAO from the beanFactory.
   * 
   * @return NodeDAO
   */
  protected NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

  /**
   * Get the HierarchyDAO from the beanFactory.
   * 
   * @return HierarchyDAO
   */
  protected HierarchyDAO getHierarchyDAO()
  {
    return (HierarchyDAO)ApplicationContextFactory.getApplicationContext().getBean( "hierarchyDAO" );
  }

  /**
   * Tests getting User with associations
   */
  public void testGetUserByIdWithAssociations()
  {
    User user = buildStaticUser();

    Role role1 = new Role();
    role1.setCode( "testCode" );
    role1.setHelpText( "testHELPTEXT" );
    role1.setName( "testNAME" );
    role1.setActive( Boolean.valueOf( true ) );
    getRoleDAO().saveRole( role1 );
    user.addRole( role1 );

    Role role2 = new Role();
    role2.setCode( "testCode2" );
    role2.setHelpText( "testHELPTEXT2" );
    role2.setName( "testNAME2" );
    role2.setActive( Boolean.valueOf( true ) );
    getRoleDAO().saveRole( role2 );
    user.addRole( role2 );

    User savedUser = userDAO.saveUser( user );
    flushAndClearSession();

    AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
    userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ROLE ) );

    User actualUser = userDAO.getUserByIdWithAssociations( savedUser.getId(), userAssociationRequestCollection );

    assertEquals( "Users not equal", user, actualUser );
  }

  /**
   * Tests getting User with associations
   */
  public void testGetAllUsersForWelcomeMail()
  {
    userDAO.getAllUsersForWelcomeMail();
  }
  
  public void testAllUsersForRAWelcomeMail()
  {
    userDAO.getAllUsersForRAWelcomeMail();
  }

  /**
   * Tests saving or updating the user information to the database and fetching the information.
   */
  public void testSaveAndGetUserById()
  {

    RoleDAO roleDAO = getRoleDAO();
    AclDAO aclDAO = getAclDAO();

    User expectedUser = new User();
    expectedUser.setUserType( UserType.lookup( UserType.BI ) );
    expectedUser.setFirstName( "TestFIRSTNAME" );
    expectedUser.setLastName( "TestLASTNAME" );
    expectedUser.setMasterUserId( new Long( 1 ) );
    expectedUser.setPassword( "testPASSWORD" );
    expectedUser.setActive( Boolean.TRUE );
    expectedUser.setWelcomeEmailSent( Boolean.FALSE );
    expectedUser.setUserName( "testUSERNAME" );
    expectedUser.setLoginFailuresCount( new Integer( 0 ) );
    expectedUser.setLastResetDate( new Date() );

    Role role1 = new Role();
    role1.setCode( "testCODE" );
    role1.setHelpText( "testHELPTEXT" );
    role1.setName( "testName" );
    role1.setActive( Boolean.valueOf( true ) );
    roleDAO.saveRole( role1 );
    expectedUser.addRole( role1 );

    Role role2 = new Role();
    role2.setCode( "testCODE2" );
    role2.setHelpText( "testHELPTEXT2" );
    role2.setName( "testNAME2" );
    role2.setActive( Boolean.valueOf( true ) );
    roleDAO.saveRole( role2 );
    expectedUser.addRole( role2 );

    Role role3 = new Role();
    role3.setCode( "testCODE3" );
    role3.setHelpText( "testHELPTEXT3" );
    role3.setName( "testNAME3" );
    role3.setActive( Boolean.valueOf( true ) );
    roleDAO.saveRole( role3 );
    expectedUser.addRole( role3 );

    Acl acl1 = new Acl();
    acl1.setCode( "testAclCODE" );
    acl1.setName( "testAclNAME" );
    acl1.setHelpText( "testAclHELPTEXT" );
    acl1.setClassName( "testAclCLASSNAME" );
    aclDAO.saveAcl( acl1 );

    UserAcl userAcl1 = new UserAcl( expectedUser, acl1 );
    userAcl1.setTarget( "TARGET1" );
    userAcl1.setPermission( "PERMISSION1" );
    userAcl1.setGuid( "GUID1" );

    expectedUser.addUserAcl( userAcl1 );

    Acl acl2 = new Acl();
    acl2.setCode( "testAclCODE2" );
    acl2.setName( "testAclNAME2" );
    acl2.setHelpText( "testAclHELPTEXT2" );
    acl2.setClassName( "testAclCLASSNAME2" );
    aclDAO.saveAcl( acl2 );

    UserAcl userAcl2 = new UserAcl( expectedUser, acl2 );
    userAcl2.setTarget( "TARGET2" );
    userAcl2.setPermission( "PERMISSION2" );
    userAcl2.setGuid( "GUID2" );

    expectedUser.addUserAcl( userAcl2 );

    Acl acl3 = new Acl();
    acl3.setCode( "testAclCODE3" );
    acl3.setName( "testAclNAME3" );
    acl3.setHelpText( "testAclHELPTEXT3" );
    acl3.setClassName( "testAclCLASSNAME3" );
    aclDAO.saveAcl( acl3 );

    UserAcl userAcl3 = new UserAcl( expectedUser, acl3 );
    userAcl3.setTarget( "TARGET3" );
    userAcl3.setPermission( "PERMISSION3" );
    userAcl3.setGuid( "GUID3" );

    expectedUser.addUserAcl( userAcl3 );

    userDAO.saveUser( expectedUser );

    flushAndClearSession();

    User actualUser = userDAO.getUserById( expectedUser.getId() );

    assertEquals( "Users not equals", expectedUser, actualUser );

    acl3.setCode( "updatedNAME" );
    role3.setHelpText( "updatedHELPTEXT" );

    userDAO.saveUser( expectedUser );
    flushAndClearSession();

    actualUser = userDAO.getUserById( expectedUser.getId() );

    assertEquals( "Updated Users not equals", expectedUser, actualUser );

  }

  /**
   * Tests changing the user's password.
   */
  public void testChangePassword()

  {
    User savedUser = buildAndSaveParticipant();

    User user = userDAO.getUserByUserName( savedUser.getUserName() );
    user.setPassword( "newPasswordsdlxvcxc" );
    userDAO.saveUser( user );
    flushAndClearSession();

    savedUser = userDAO.getUserByUserName( savedUser.getUserName() );
    assertEquals( "{MD5}33ED0B0F37FFDF28567CF8C35A68CFE1", savedUser.getPassword() );

  }

  /**
   * Test getting user by UserName
   */
  public void testGetUserByUserName()
  {
    User testUser = buildAndSaveParticipant();
    User user = userDAO.getUserByUserName( testUser.getUserName() );
    assertNotNull( user );
    assertNotNull( user.getId() );
    assertEquals( testUser.getId(), user.getId() );
    assertEquals( testUser.getFirstName(), user.getFirstName() );
    assertEquals( testUser.getLastName(), user.getLastName() );
    assertEquals( testUser.getSecretAnswer(), user.getSecretAnswer() );
    assertEquals( testUser.getSecretQuestionType(), user.getSecretQuestionType() );
    org.hibernate.Hibernate.initialize( user.getUserRoles() );
  }

  /**
   * Test getting user by UserName
   */
  @SuppressWarnings( "unchecked" )
  public void testGetUserByUserNameWithAssociations()
  {
    AssociationRequestCollection associations = new AssociationRequestCollection();
    associations.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    User testUser = buildAndSaveParticipantWithEmail();
    User user = userDAO.getUserByUserNameWithAssociations( testUser.getUserName(), associations );
    assertNotNull( user );
    assertNotNull( user.getId() );
    assertEquals( testUser.getId(), user.getId() );
    assertEquals( testUser.getFirstName(), user.getFirstName() );
    assertEquals( testUser.getLastName(), user.getLastName() );
    assertTrue( !user.getUserEmailAddresses().isEmpty() );
  }

  /**
   * Test isPasswordValid method.
   */
  public void testIsPasswordValid()
  {
    User testUser = buildAndSaveParticipant();
    boolean passwordValid = userDAO.isPasswordValid( testUser, "testpassword" );
    assertEquals( true, passwordValid );
  }

  /**
   * Test get all Users.
   */
  public void testGetAllUsers()
  {

    List users = userDAO.getAll();

    System.out.println( "Number of Users = " + users.size() );
    assertEquals( true, userDAO.getAll().size() > 0 );
  }

  /**
   * Test search users with given criteria.
   */
  public void testSearchUser()
  {

    User expectedUser1 = new User();
    expectedUser1.setUserType( UserType.lookup( UserType.BI ) );
    expectedUser1.setFirstName( "TestFIRSTNAME-ABC" );
    expectedUser1.setLastName( "TestLASTNAME-ABC" );
    expectedUser1.setMasterUserId( new Long( 1 ) );
    expectedUser1.setPassword( "testPASSWORD" );
    expectedUser1.setActive( Boolean.TRUE );
    expectedUser1.setWelcomeEmailSent( Boolean.FALSE );
    // expectedUser1.setStatusType( mockTrueStatusType );
    expectedUser1.setUserName( "testUSERNAME-ABC" );
    expectedUser1.setLoginFailuresCount( new Integer( 0 ) );
    expectedUser1.setLastResetDate( new Date() );
    expectedUser1.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );
    userDAO.saveUser( expectedUser1 );

    User expectedUser2 = new User();
    expectedUser2.setUserType( UserType.lookup( UserType.BI ) );
    expectedUser2.setFirstName( "TestFIRSTNAME-XYZ" );
    expectedUser2.setLastName( "TestLASTNAME-XYZ" );
    expectedUser2.setMasterUserId( new Long( 1 ) );
    expectedUser2.setPassword( "testPASSWORD" );
    expectedUser2.setActive( Boolean.TRUE );
    expectedUser2.setWelcomeEmailSent( Boolean.FALSE );
    // expectedUser2.setStatusType( mockTrueStatusType );
    expectedUser2.setUserName( "testUSERNAME-XYZ" );
    expectedUser2.setLoginFailuresCount( new Integer( 0 ) );
    expectedUser2.setLastResetDate( new Date() );
    expectedUser2.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );
    userDAO.saveUser( expectedUser2 );

    User expectedUser3 = new User();
    expectedUser3.setUserType( UserType.lookup( UserType.BI ) );
    expectedUser3.setFirstName( "TestFIRSTNAME-KYC" );
    expectedUser3.setLastName( "TestLASTNAME-KYC" );
    expectedUser3.setMasterUserId( new Long( 1 ) );
    expectedUser3.setPassword( "testPASSWORD" );
    expectedUser3.setActive( Boolean.TRUE );
    expectedUser3.setWelcomeEmailSent( Boolean.FALSE );
    // expectedUser3.setStatusType( mockFalseStatusType );
    expectedUser3.setUserName( "testUSERNAME-KYC" );
    expectedUser3.setLoginFailuresCount( new Integer( 0 ) );
    expectedUser3.setLastResetDate( new Date() );
    expectedUser3.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );
    userDAO.saveUser( expectedUser3 );

    List expectedList = new ArrayList();
    expectedList.add( expectedUser1 );
    expectedList.add( expectedUser3 );
    expectedList.add( expectedUser2 );

    assertTrue( "ExpectedList for search wasn't equal to actualList", userDAO.searchUser( "firstname", "lastname", "username" ).containsAll( expectedList ) );

    expectedList.clear();

    expectedList.add( expectedUser1 );

    assertTrue( "ExpectedList for search wasn't equal to actualList", userDAO.searchUser( "", "", "ABC" ).containsAll( expectedList ) );

    expectedList.clear();

    expectedList.add( expectedUser3 );
    expectedList.add( expectedUser2 );

    assertTrue( "ExpectedList for search wasn't equal to actualList", userDAO.searchUser( "", "", "Y" ).containsAll( expectedList ) );

  }

  /**
   * Test get available Roles. First create a user, then create a role, then verify that the
   * available role set is > than 0 for the newly created user.
   */
  public void testGetAvailableRoles()
  {
    RoleDAO roleDAO = getRoleDAO();

    User expectedUser = new User();
    expectedUser.setUserType( UserType.lookup( UserType.BI ) );
    expectedUser.setFirstName( "TestFIRSTNAME" );
    expectedUser.setLastName( "TestLASTNAME" );
    expectedUser.setMasterUserId( new Long( 1 ) );
    expectedUser.setPassword( "testPASSWORD" );
    expectedUser.setActive( Boolean.TRUE );
    expectedUser.setWelcomeEmailSent( Boolean.FALSE );
    // expectedUser.setStatusType( mockTrueStatusType );
    expectedUser.setUserName( "testUSERNAME" );
    expectedUser.setLoginFailuresCount( new Integer( 0 ) );
    expectedUser.setLastResetDate( new Date() );
    expectedUser.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );
    userDAO.saveUser( expectedUser );

    User actualUser = userDAO.getUserById( expectedUser.getId() );

    assertEquals( "Users not equals", expectedUser, actualUser );

    Role expectedRole = new Role();
    expectedRole.setCode( "TESTCode" );
    expectedRole.setHelpText( "TestHELPTEXT" );
    expectedRole.setName( "TestRoleNAME" );
    expectedRole.setActive( Boolean.valueOf( true ) );
    roleDAO.saveRole( expectedRole );

    Role actualRole = roleDAO.getRoleById( expectedRole.getId() );

    assertEquals( "Users not equals", expectedRole, actualRole );

    Set availableRoles = userDAO.getAvailableRoles( expectedUser.getId() );

    System.out.println( "Number of Roles = " + availableRoles.size() );
    assertEquals( true, availableRoles.size() > 0 );
  }

  /**
   * Tests searching the database for a list of Nodes not assigned to the user.
   */
  public void testGetUnassignedNodes()
  {

    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    // Build and save a hierarchy.
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    getHierarchyDAO().save( hierarchy );

    // Build and save a node type.
    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );
    getNodeTypeDAO().saveNodeType( nodeType );

    HibernateSessionManager.getSession().flush();

    // Build and save the first node.
    Node node1 = new Node();
    node1.setName( "testNAME-ABC" + uniqueName );
    node1.setDescription( "testDESCRIPTION-ABC" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testPATH" );
    nodeDAO.saveNode( node1 );

    // Build and save a user.
    User user = buildStaticUser();
    userDAO.saveUser( user );

    // Associate the first node and the user.
    UserNode userNode = new UserNode();
    userNode.setNode( node1 );
    userNode.setActive( Boolean.TRUE );
    userNode.setIsPrimary( Boolean.TRUE );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode.setIsPrimary( false );
    user.addUserNode( userNode );
    userDAO.saveUser( user );

    // Build and save the second node.
    Node node2 = new Node();
    node2.setName( "testNAME-XYZ" + uniqueName );
    node2.setDescription( "testDESCRIPTION-XYZ" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    nodeDAO.saveNode( node2 );

    // Associate the second node and the user.
    UserNode userNode2 = new UserNode();
    userNode2.setNode( node2 );
    userNode2.setIsPrimary( Boolean.TRUE );
    userNode2.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode2.setIsPrimary( false );
    userNode2.setActive( Boolean.TRUE );
    user.addUserNode( userNode2 );
    userDAO.saveUser( user );

    // Build and save the third node.
    Node node3 = new Node();
    node3.setName( "testNAME-AZK" + uniqueName );
    node3.setDescription( "testDESCRIPTION-AZK" );
    node3.setNodeType( nodeType );
    node3.setHierarchy( hierarchy );
    node3.setPath( node2.getPath() + node3.getName() );
    nodeDAO.saveNode( node3 );

    List expectedList = new ArrayList();
    expectedList.add( node3 );

    flushAndClearSession();

    // Search for all records which aren't assigned to the user.
    List results = userDAO.getUnassignedNodes( user.getId() );
    assertTrue( "Actual list of unassigned nodes doesn't contain expected node", results.contains( node3 ) );
  }

  /**
   * Test getting a list of all Acls assigned to a user.
   */
  public void testGetAssignedAclsByUserId()
  {

    User user = buildStaticUser();
    userDAO.saveUser( user );

    AclDAO aclDAO = getAclDAO();

    Acl acl1 = new Acl();
    acl1.setCode( "testCODE1" );
    acl1.setName( "testNAME1" );
    acl1.setHelpText( "testHELPTEXT1" );
    acl1.setClassName( "testCLASSNAME1" );
    aclDAO.saveAcl( acl1 );

    UserAcl userAcl1 = new UserAcl( user, acl1 );
    userAcl1.setTarget( "TARGET1" );
    userAcl1.setPermission( "PERMISSION1" );
    userAcl1.setGuid( "GUID1" );

    user.addUserAcl( userAcl1 );

    Acl acl2 = new Acl();
    acl2.setCode( "testCODE2" );
    acl2.setName( "testNAME2" );
    acl2.setHelpText( "testHELPTEXT2" );
    acl2.setClassName( "testCLASSNAME2" );
    aclDAO.saveAcl( acl2 );

    UserAcl userAcl2 = new UserAcl( user, acl2 );
    userAcl2.setTarget( "TARGET2" );
    userAcl2.setPermission( "PERMISSION2" );
    userAcl2.setGuid( "GUID2" );
    user.addUserAcl( userAcl2 );

    userDAO.saveUser( user );

    List aclList = userDAO.getAvailableAcls( user.getId() );
    assertTrue( "Assigned acls list wasn't what was expected", aclList.size() >= 2 );

  }

  /**
   * Test add Node to user adds a UserNode and you can get a userNode back from the user.
   */
  public void testUserNode()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + uniqueName );
    nodeType.setNameCmKey( "testkey" + uniqueName );
    getNodeTypeDAO().saveNodeType( nodeType );

    Node node1 = new Node();
    node1.setName( "HierTestNode110" + uniqueName );
    node1.setDescription( "testDESCRIPTIONNode1" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testNodePath" );
    getNodeDAO().saveNode( node1 );

    User user = buildStaticUser();
    userDAO.saveUser( user );

    UserNode userNode = new UserNode();
    userNode.setIsPrimary( Boolean.TRUE );
    userNode.setUser( user );
    userNode.setNode( node1 );
    userNode.setActive( Boolean.valueOf( true ) );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode.setIsPrimary( false );

    user.addUserNode( userNode );
    Set userNodes = userDAO.getUserById( user.getId() ).getUserNodes();
    System.out.println( "UserNodes is " + userNodes.size() );
    assertTrue( "UserNodes set wasn't what was expected", userNodes.size() >= 1 );
  }

  /**
   * Test getting a list of all Nodes assigned to a user.
   */
  public void testGetAssignedNodes()
  {

    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );
    NodeDAO nodeDAO = getNodeDAO();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );
    getNodeTypeDAO().saveNodeType( nodeType );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "testNAME" + uniqueName );
    hierarchy.setCmAssetCode( "testASSET" );
    hierarchy.setNameCmKey( "testKEY" );

    getHierarchyDAO().save( hierarchy );

    Node node1 = new Node();
    node1.setName( "testNODENAME1" + uniqueName );
    node1.setDescription( "testNODEDESCRIPTION1" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testNodePath" );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNODENAME2" + uniqueName );
    node2.setDescription( "testNODEDESCRIPTION2" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    nodeDAO.saveNode( node2 );

    User user = buildStaticUser();
    userDAO.saveUser( user );

    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setUser( user );
    userNode1.setNode( node1 );
    userNode1.setActive( Boolean.valueOf( true ) );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode1.setIsPrimary( false );

    UserNode userNode2 = new UserNode();
    userNode2.setIsPrimary( Boolean.TRUE );
    userNode2.setUser( user );
    userNode2.setNode( node2 );
    userNode2.setActive( Boolean.valueOf( true ) );
    userNode2.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode2.setIsPrimary( false );

    user.addUserNode( userNode1 );
    user.addUserNode( userNode2 );

    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    List nodeList = userDAO.getAssignedNodes( user.getId() );
    assertTrue( "Assigned ndoe list wasn't what was expected", nodeList.size() >= 1 );

  }

  /**
   * Tests getting all non participant users
   */
  public void testGetAllUsersNonParticipant()
  {
    User user = buildStaticUser();

    userDAO.saveUser( user );

    Participant participant = buildStaticParticipant();
    getParticipantDAO().saveParticipant( participant );

    List nonParticipantUsers = userDAO.getAllUsersNonParticipant();

    assertFalse( "Participant Should not have been returned", nonParticipantUsers.contains( participant ) );
  }

  /**
   * Test the getAllUsersOnNode methos
   */
  public void testGetAllUsersOnNode()
  {
    String uniqueName = getUniqueString();

    NodeDAO nodeDAO = getNodeDAO();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );
    getNodeTypeDAO().saveNodeType( nodeType );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "testNAME" + uniqueName );
    hierarchy.setCmAssetCode( "testASSET" );
    hierarchy.setNameCmKey( "testKEY" );

    getHierarchyDAO().save( hierarchy );

    Node node1 = new Node();
    node1.setName( "testNODENAME1" + uniqueName );
    node1.setDescription( "testNODEDESCRIPTION1" );
    node1.setNodeType( nodeType );
    node1.setHierarchy( hierarchy );
    node1.setPath( "testNodePath" );
    nodeDAO.saveNode( node1 );

    Node node2 = new Node();
    node2.setName( "testNODENAME2" + uniqueName );
    node2.setDescription( "testNODEDESCRIPTION2" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    node2.setPath( node1.getPath() + node2.getName() );
    nodeDAO.saveNode( node2 );

    User user = buildStaticUser();
    userDAO.saveUser( user );

    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setUser( user );
    userNode1.setNode( node1 );
    userNode1.setActive( Boolean.valueOf( true ) );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode1.setIsPrimary( false );

    UserNode userNode2 = new UserNode();
    userNode2.setIsPrimary( Boolean.TRUE );
    userNode2.setUser( user );
    userNode2.setNode( node2 );
    userNode2.setActive( Boolean.valueOf( true ) );
    userNode2.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    userNode2.setIsPrimary( false );

    user.addUserNode( userNode1 );
    user.addUserNode( userNode2 );

    flushAndClearSession();

    List userList = userDAO.getAllUsersOnNode( node1.getId() );

    for ( Iterator iter = userList.iterator(); iter.hasNext(); )
    {
      User savedUser = (User)iter.next();

      assertEquals( user, savedUser );
    }

    assertTrue( "users on node was not 1", userList.size() == 1 );
  }

  public void testGetUserByEmailAddr()
  {
    String emailAddress = "emailaddress@domain.com";
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setEmailAddr( emailAddress );

    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.addUserEmailAddress( userEmailAddress );
    user.setFirstName( "TestFIRSTNAME" );
    user.setLastName( "TestLASTNAME" );
    user.setMasterUserId( new Long( 1 ) );
    user.setPassword( "testPASSWORD" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );
    // expectedUser.setStatusType( mockTrueStatusType );
    user.setUserName( "testUSERNAME" );
    user.setLoginFailuresCount( new Integer( 0 ) );
    user.setLastResetDate( new Date() );
    user.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );
    userDAO.saveUser( user );
    getUserDAO().saveUser( user );

    User userExists = getUserDAO().getUserByEmailAddr( emailAddress );
    assertTrue( null != userExists );

    String emailAddressNotExists = "address154@biworldwide.com";
    User userNotExists = userDAO.getUserByEmailAddr( emailAddressNotExists );
    assertTrue( null == userNotExists );
  }

  /**
   * Tests saving login info
   */
  public void testSaveLoginInfo()
  {
    User savedUser = buildAndSaveParticipant();

    int countBeforeInsert = 0;
    int countAfterInsert = 0;

    countBeforeInsert = getUserDAO().getAllLoginActivityCount( savedUser.getId() );

    Timestamp timestamp = new Timestamp( System.currentTimeMillis() );
    UserLoginInfo userLoginInfo = new UserLoginInfo();
    userLoginInfo.setUserId( savedUser.getId() );
    userLoginInfo.setUserLoggedInTime( timestamp );
    getUserDAO().saveLoginInfo( userLoginInfo );

    flushAndClearSession();

    countAfterInsert = getUserDAO().getAllLoginActivityCount( savedUser.getId() );

    assertEquals( "Login_Activity was not saved", countBeforeInsert + 1, countAfterInsert );

    flushAndClearSession();
  }

  public void testgetUserEmailAddressById()
  {
    User user = buildStaticUser();
    UserEmailAddress email = buildUserEmailAddress();
    email.setUser( user );
    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();

    UserEmailAddress userEmailAddressById = userDAO.getUserEmailAddressById( email.getId() );
    assertNotNull( userEmailAddressById );
  }

  public void testgetUserPhoneById()
  {
    User user = buildStaticUser();
    UserPhone expectedPhone = buildUserPhone();
    expectedPhone.setUser( user );
    user.setUserPhones( new HashSet<>( Arrays.asList( expectedPhone ) ) );
    userDAO.saveUser( user );
    flushAndClearSession();

    UserPhone actualPhone = userDAO.getUserPhoneById( expectedPhone.getId() );
    assertNotNull( "User Phone is null", actualPhone );
    assertEquals( "Phone Number doesn't match", expectedPhone.getPhoneNbr(), actualPhone.getPhoneNbr() );
  }

  public void testgetUserDetailsByEmailAddr()
  {
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setUser( user );
    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();

    Set<Long> userDetailsByEmailAddr = userDAO.getUserIdsByEmail( email.getEmailAddr() );
    assertNotNull( "User by email address is null", userDetailsByEmailAddr );
    assertTrue( "User by email address not found", CollectionUtils.isNotEmpty( userDetailsByEmailAddr ) );
  }

  public void testgetUserDetailsByEmailAddrWithProjections()
  {
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setUser( user );
    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();

    Set<Long> userIdsByEmailAddr = userDAO.getUserIdsByEmail( email.getEmailAddr() );
    assertNotNull( "User by email address is null", userIdsByEmailAddr );
    assertTrue( "User by email address not found", CollectionUtils.isNotEmpty( userIdsByEmailAddr ) );
  }

  public void testGetPrimaryEmailCountByEmailAddressFindUnique()
  {
    String emailAddress = "i-am-a-unique@email.address.com";
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setEmailAddr( emailAddress );
    email.setUser( user );

    UserEmailAddress email2 = buildUserEmailAddress();
    email2.setIsPrimary( false ); // make this non-primary to test constraint
    email2.setEmailAddr( emailAddress );
    email2.setUser( user );

    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();
    int count = userDAO.getPrimaryEmailCountByEmailAddress( emailAddress );
    assertTrue( "Count by primary email address failed to return 1", count == 1 );
  }

  public void testGetPrimaryEmailCountByEmailAddressFindMultiples()
  {
    String emailAddress = "i-am-not-unique@email.address.com";
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setEmailAddr( emailAddress );
    email.setUser( user );
    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    User user2 = buildStaticParticipant();
    user2.setUserName( user2.getUserName() + "123" );
    UserEmailAddress email2 = buildUserEmailAddress();
    email2.setEmailAddr( emailAddress );
    email2.setUser( user2 );
    user2.setUserEmailAddresses( new HashSet<>( Arrays.asList( email2 ) ) );

    userDAO.saveUser( user );
    userDAO.saveUser( user2 );

    flushAndClearSession();
    int count = userDAO.getPrimaryEmailCountByEmailAddress( emailAddress );
    assertTrue( "Count by primary email address failed to return 2", count == 2 );
  }

  /**
   * Builds a static user.
   * 
   * @return User
   */
  public static User buildStaticUser()
  {
    return buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
  }

  /**
   * Builds a static user with the params.
   * 
   * @param userName
   * @param firstName
   * @param lastName
   * @return User
   */
  public static Participant buildStaticUser( String userName, String firstName, String lastName )
  {

    Participant user = new Participant();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setUserName( userName );
    user.setFirstName( firstName );
    user.setLastName( lastName );
    user.setPassword( "testPASSWORD" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );

    return user;
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
   * Tests update email information
   */
  public void testupdateEmail()
  {
    Long userId = new Long( 6070 );
    String emailAddress = "bi@gmail.com";

    boolean isEmailUpdated = getUserDAO().updateRecoveryEmail( userId, emailAddress );

    assertTrue( isEmailUpdated );

  }

  /**
   * Tests update Phone information
   */
  public void testupdatePhone()
  {
    Long userId = new Long( 6070 );
    String phone = "123456789";
    String countryPhoneCode = "us";

    boolean isPhoneUpdated = getUserDAO().updateRecoveryPhone( userId, phone, countryPhoneCode );

    assertTrue( isPhoneUpdated );
  }

  private Participant buildAndSaveParticipant()
  {
    Participant participant = buildStaticParticipant();

    Participant savedParticipant = getParticipantDAO().saveParticipant( participant );
    flushAndClearSession();
    return savedParticipant;
  }

  private Participant buildAndSaveParticipant( Date reissuedPasswordExpiryDate )
  {
    Participant participant = buildStaticParticipant();
    // participant.setReissuedPasswordExpireDate( reissuedPasswordExpiryDate );
    Participant savedParticipant = getParticipantDAO().saveParticipant( participant );
    flushAndClearSession();
    return savedParticipant;
  }

  @SuppressWarnings( "unchecked" )
  private Participant buildAndSaveParticipantWithEmail()
  {
    Participant participant = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setUser( participant );
    participant.getUserEmailAddresses().add( email );
    Participant savedParticipant = getParticipantDAO().saveParticipant( participant );
    flushAndClearSession();
    return savedParticipant;
  }

  /**
   * Returns user dao
   * 
   * @return UserDAO
   */
  protected UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  private UserEmailAddress buildUserEmailAddress()
  {
    UserEmailAddress email = new UserEmailAddress();
    email.setEmailAddr( "email@email.com" );
    email.setEmailType( EmailAddressType.lookup( EmailAddressType.BUSINESS ) );
    email.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    email.setIsPrimary( true );
    return email;
  }

  private UserPhone buildUserPhone()
  {
    UserPhone phone = new UserPhone();
    phone.setCountryPhoneCode( "001" );
    phone.setIsPrimary( true );
    phone.setPhoneType( PhoneType.lookup( PhoneType.BUSINESS ) );
    phone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    phone.setPhoneNbr( "9119119111" );
    return phone;
  }

  public void testIsUniqueEmailFindUnique()
  {
    String emailAddress = "i-am-a-unique123@email.address.com";
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setEmailAddr( emailAddress );
    email.setUser( user );

    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();
    boolean isUnique = userDAO.isUniqueEmail( emailAddress );
    assertTrue( isUnique );
  }

  public void testIsUniqueEmailFindMultiples()
  {
    String emailAddress = "i-am-not-unique@email.address.com";
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setEmailAddr( emailAddress );
    email.setUser( user );
    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email ) ) );

    User user2 = buildStaticParticipant();
    user2.setUserName( user2.getUserName() + "123" );
    UserEmailAddress email2 = buildUserEmailAddress();
    email2.setEmailAddr( emailAddress );
    email2.setUser( user2 );
    user2.setUserEmailAddresses( new HashSet<>( Arrays.asList( email2 ) ) );

    userDAO.saveUser( user );
    userDAO.saveUser( user2 );

    flushAndClearSession();
    boolean isUnique = userDAO.isUniqueEmail( emailAddress );
    assertFalse( isUnique );
  }

  public void testIsUniqueEmailFindUniqueMultipleTypes()
  {
    String emailAddress = "i-am-a-unique123@email.address.com";
    User user = buildStaticParticipant();
    UserEmailAddress email = buildUserEmailAddress();
    email.setEmailAddr( emailAddress );
    email.setUser( user );
    UserEmailAddress email2 = buildUserEmailAddress();
    email2.setEmailAddr( emailAddress );
    email2.setIsPrimary( false );
    email2.setEmailType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
    email2.setUser( user );
    user.setUserEmailAddresses( new HashSet<>( Arrays.asList( email, email2 ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();
    boolean isUnique = userDAO.isUniqueEmail( emailAddress );
    assertTrue( isUnique );
  }

  public void testIsUniquePhoneFindUnique()
  {
    String phoneNumber = "123-456-1111";
    User user = buildStaticParticipant();
    UserPhone userPhone = buildUserPhone();
    userPhone.setPhoneNbr( phoneNumber );
    userPhone.setUser( user );
    user.setUserPhones( new HashSet<>( Arrays.asList( userPhone ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();

    boolean isUnique = userDAO.isUniquePhoneNumber( phoneNumber );
    assertTrue( isUnique );
  }

  public void testIsUniquePhoneFindUniqueMultipleTypes()
  {
    String phoneNumber = "123-456-1111";
    User user = buildStaticParticipant();
    UserPhone userPhone = buildUserPhone();
    userPhone.setPhoneNbr( phoneNumber );
    userPhone.setUser( user );
    UserPhone userPhone2 = buildUserPhone();
    userPhone2.setPhoneNbr( phoneNumber );
    userPhone2.setIsPrimary( false );
    userPhone2.setPhoneType( PhoneType.lookup( PhoneType.MOBILE ) );
    userPhone2.setUser( user );
    user.setUserPhones( new HashSet<>( Arrays.asList( userPhone, userPhone2 ) ) );

    userDAO.saveUser( user );
    flushAndClearSession();

    boolean isUnique = userDAO.isUniquePhoneNumber( phoneNumber );
    assertTrue( isUnique );
  }

  public void testIsUniquePhoneFindMultiples()
  {
    String phoneNumber = "123-456-1111";

    User user1 = buildStaticParticipant();
    UserPhone userPhone = buildUserPhone();
    userPhone.setPhoneNbr( phoneNumber );
    userPhone.setUser( user1 );
    user1.setUserPhones( new HashSet<>( Arrays.asList( userPhone ) ) );

    User user2 = buildStaticParticipant();
    UserPhone userPhone2 = buildUserPhone();
    userPhone2.setPhoneNbr( phoneNumber );
    userPhone2.setUser( user2 );
    user2.setUserPhones( new HashSet<>( Arrays.asList( userPhone2 ) ) );

    userDAO.saveUser( user1 );
    userDAO.saveUser( user2 );
    flushAndClearSession();

    boolean isUnique = userDAO.isUniquePhoneNumber( phoneNumber );
    assertFalse( isUnique );
  }

  public void testGetAllStrongMailUsers()
  {
    List<StrongMailUser> strongMailUsers = new ArrayList();
    flushAndClearSession();

    assertTrue( userDAO.getAllStrongMailUsers().size() == 0 );
  }

}