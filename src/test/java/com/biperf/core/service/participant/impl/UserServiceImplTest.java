/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/participant/impl/UserServiceImplTest.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.jmock.Mock;

import com.biperf.core.dao.country.hibernate.CountryDAOImplTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.Acl;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.impl.MailingServiceImpl;
import com.biperf.core.service.system.SystemVariableService;

/**
 * UserServiceImplTest.
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
 * <td>sharma</td>
 * <td>Apr 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserServiceImplTest extends BaseServiceTest
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public UserServiceImplTest( String test )
  {
    super( test );
  }

  /** userServiceImplementation */
  private UserServiceImpl userService = new UserServiceImpl();
  // private SecurityPwdRequestDAOImpl securityPwdRequestDAOImpl = new SecurityPwdRequestDAOImpl();
  private MailingServiceImpl mailingService = new MailingServiceImpl();
  /** mocks */
  private Mock mockUserDAO = null;
  private Mock mockCharacteristicDAO = null;
  private Mock mockNodeDAO = null;
  private Mock mockParticipantDAO = null;
  private Mock mockawardBanQServiceFactory = null;
  private Mock mockAwardBanQService = null;
  private Mock mockMailingService = null;
  private Mock mockSystemVariableService = null;
  private Mock mockAclDAO = null;
  private AssociationRequest mockAssociationRequest = new AssociationRequest()
  {
    public void execute( Object domainObject )
    {
    }
  };

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockUserDAO = new Mock( UserDAO.class );
    userService.setUserDAO( (UserDAO)mockUserDAO.proxy() );

    mockCharacteristicDAO = new Mock( CharacteristicDAO.class );
    userService.setCharacteristicDAO( (CharacteristicDAO)mockCharacteristicDAO.proxy() );

    mockNodeDAO = new Mock( NodeDAO.class );
    userService.setNodeDAO( (NodeDAO)mockNodeDAO.proxy() );

    mockParticipantDAO = new Mock( ParticipantDAO.class );
    userService.setParticipantDAO( (ParticipantDAO)mockParticipantDAO.proxy() );

    mockawardBanQServiceFactory = new Mock( AwardBanQServiceFactory.class );
    userService.setAwardBanQServiceFactory( (AwardBanQServiceFactory)mockawardBanQServiceFactory.proxy() );

    mockAwardBanQService = new Mock( AwardBanQService.class );

    mockMailingService = new Mock( MailingService.class );

    mockSystemVariableService = new Mock( SystemVariableService.class );
    userService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );

    mockAclDAO = new Mock( AclDAO.class );
    userService.setAclDAO( (AclDAO)mockAclDAO.proxy() );
  }

  /**
   * Test getting the User by id.
   */
  public void testGetUserById()
  {
    User user = buildUser();
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    userService.getUserById( user.getId() );
    mockUserDAO.verify();
  }

  /**
   * Test getting the User by id.
   */
  public void testGetUserByIdWithAssociations()
  {

    // Get the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );

    // UserDAO expected to call getUserId once with the UserId which will return the User expected
    mockUserDAO.expects( once() ).method( "getUserByIdWithAssociations" ).with( same( user.getId() ), eq( associationRequestCollection ) ).will( returnValue( user ) );

    userService.getUserByIdWithAssociations( user.getId(), associationRequestCollection );

    mockUserDAO.verify();
  }

  /**
   * Test updating the User through the service.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public void testSaveUserUpdate() throws UniqueConstraintViolationException, ServiceErrorException
  {

    // Create the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    User dbUser = new User();
    dbUser.setId( new Long( 1 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // update the user

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( dbUser ) );

    // test the UserService.addUser
    userService.saveUser( user );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();
  }

  /**
   * Test updating the User through the service.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public void testSaveUserUpdateAsPax() throws UniqueConstraintViolationException, ServiceErrorException
  {

    // Create the test User.
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    Participant dbUser = new Participant();
    dbUser.setId( new Long( 1 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    participant2.setAwardBanqNumber( "1234" );
    participant2.setUserName( dbUser.getUserName() );
    participant2.setFirstName( dbUser.getFirstName() );
    participant2.setLastName( dbUser.getLastName() );
    participant2.setId( dbUser.getId() );

    // update the user

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( dbUser ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    // test the UserService.addUser
    userService.saveUser( user );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();
  }

  /**
   * Test adding the User through the service.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public void testSaveUserInsert() throws UniqueConstraintViolationException, ServiceErrorException
  {
    // Create the test User.
    User user = new User();
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    User savedUser = new User();
    savedUser.setId( new Long( 999 ) );
    savedUser.setUserName( "ServiceTestUserName" );
    savedUser.setFirstName( "ServiceTestFirstName" );
    savedUser.setLastName( "ServiceTestLastName" );
    savedUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    savedUser.setPassword( "ServiceTestPassword" );
    savedUser.setActive( Boolean.TRUE );

    // insert a new user
    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( savedUser ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( savedUser ) ).will( returnValue( savedUser ) );

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( null ) );

    // test the UserService.addUser
    userService.saveUser( user );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();
  }

  /**
   * Test adding the User through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveUserInsertConstraintViolation() throws ServiceErrorException
  {
    // Create the test User.
    User user = new User();
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    // Create the test User to return.
    User dbUser = new User();
    dbUser.setId( new Long( 1 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // insert a new user
    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    try
    {
      // test the UserService.addUser
      userService.saveUser( user );
    }
    catch( UniqueConstraintViolationException e )
    {
      return;
    }

    fail( "Should have thrown UniqueConstraintViolationException" );
  }

  /**
   * Test adding the User through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveUserUpdateConstraintViolation() throws ServiceErrorException
  {
    // Create the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    // Create the test User to return.
    User dbUser = new User();
    dbUser.setId( new Long( 2 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // insert a new user
    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    try
    {
      // test the UserService.addUser
      userService.saveUser( user );
    }
    catch( UniqueConstraintViolationException e )
    {
      return;
    }

    fail( "Should have thrown UniqueConstraintViolationException" );
  }

  /**
   * Test getting the User by Name.
   */
  public void testGetUserByUserName()
  {
    // Get the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    // UserDAO expected to call getUserId once with the UserId which will return the User expected
    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( user ) );

    User userResult = userService.getUserByUserName( user.getUserName() );
    mockUserDAO.verify();
    assertEquals( userResult.getUserName(), "UNITTEST" );
  }

  /**
   * Test get all Users returns at least 1 User
   */
  public void testGetAllUsers()
  {
    List users = new ArrayList();
    users.add( new User() );
    mockUserDAO.expects( once() ).method( "getAll" ).will( returnValue( users ) );

    List returnedUsers = userService.getAllUsers();
    assertTrue( returnedUsers.size() > 0 );

  }

  public void testGetRAWelcomeAllUsers()
  {
    List users = new ArrayList();
    users.add( new User() );
    mockUserDAO.expects( once() ).method( "getAllUsersForRAWelcomeMail" ).will( returnValue( users ) );

    List returnedUsers = userService.getAllUsersForRAWelcomeMail();
    assertTrue( returnedUsers.size() > 0 );

  }

  /**
   * Test getting a list of users through search.
   */
  public void testSearchUser()
  {

    List expectedList = new ArrayList();

    User expectedUser1 = new User();
    expectedUser1.setFirstName( "TestFIRSTNAME-ABC" );
    expectedUser1.setLastName( "TestLASTNAME-ABC" );
    expectedUser1.setMasterUserId( new Long( 1 ) );
    expectedUser1.setPassword( "testPASSWORD" );
    expectedUser1.setActive( Boolean.TRUE );
    // expectedUser1.setStatusType( mockTrueStatusType );
    expectedUser1.setUserName( "testUSERNAME-ABC" );
    expectedUser1.setLoginFailuresCount( new Integer( 0 ) );
    expectedList.add( expectedUser1 );

    User expectedUser2 = new User();
    expectedUser2.setFirstName( "TestFIRSTNAME-XYZ" );
    expectedUser2.setLastName( "TestLASTNAME-XYZ" );
    expectedUser2.setMasterUserId( new Long( 1 ) );
    expectedUser2.setPassword( "testPASSWORD" );
    expectedUser2.setActive( Boolean.TRUE );
    // expectedUser2.setStatusType( mockTrueStatusType );
    expectedUser2.setUserName( "testUSERNAME-XYZ" );
    expectedUser2.setLoginFailuresCount( new Integer( 0 ) );
    expectedList.add( expectedUser2 );

    mockUserDAO.expects( once() ).method( "searchUser" ).will( returnValue( expectedList ) );

    userService.searchUser( "firstname", "lastname", "username" );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();

  }

  /**
   * Tests getting a list of Acls which were assigned and saved with the user Id.
   */
  public void testGetAssignedAclsByUserId()
  {

    // Get the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    Acl acl = new Acl();
    acl.setId( new Long( 1 ) );
    acl.setCode( "testNAME" );
    acl.setHelpText( "testHELPTEXT" );
    acl.setClassName( "testCLASSNAME" );

    UserAcl userAcl2 = new UserAcl( user, acl );
    userAcl2.setTarget( "TARGET" );
    userAcl2.setPermission( "PERMISSION" );
    userAcl2.setGuid( "GUID" );
    user.addUserAcl( userAcl2 );

    Acl acl1 = new Acl();
    acl.setId( new Long( 2 ) );
    acl1.setCode( "testNAME1" );
    acl1.setHelpText( "testHELPTEXT1" );
    acl1.setClassName( "testCLASSNAME1" );

    UserAcl userAcl1 = new UserAcl( user, acl1 );
    userAcl1.setTarget( "TARGET" );
    userAcl1.setPermission( "PERMISSION" );
    userAcl1.setGuid( "GUID" );
    user.addUserAcl( userAcl1 );

    List aclList = new ArrayList();
    aclList.add( acl );
    aclList.add( acl1 );

    mockUserDAO.expects( once() ).method( "getAvailableAcls" ).with( same( user.getId() ) ).will( returnValue( aclList ) );

    List actualList = userService.getAvailableAclsByUserId( user.getId() );

    assertEquals( "Actual acls List wasn't what was expected", aclList, actualList );

    // Verify the mockDAO returns the same thing the service is attempting to get.
    mockUserDAO.verify();

  }

  /**
   * Tests getting a set of Roles which are available for the user Id.
   */
  public void testGetAvailableRoles()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    Set availableRoles = new HashSet();
    availableRoles.add( new Role() );
    mockUserDAO.expects( once() ).method( "getAvailableRoles" ).with( same( user.getId() ) ).will( returnValue( availableRoles ) );

    Set returnedRoles = userService.getAvailableRolesByUserId( user.getId() );
    assertTrue( returnedRoles.size() > 0 );
  }

  /**
   * Test get all of the userCharacteristic records
   */
  public void testGetUserCharacteristics()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    user.getUserCharacteristics().add( userCharacteristic );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    Set userCharacteristics = userService.getUserCharacteristics( user.getId() );

    assertTrue( userCharacteristics.size() == 1 );
  }

  /**
   * Test adding a user characteristic
   */
  public void testAddUserCharacteristic()
  {
    String uniqueName = String.valueOf( Math.random() % 29930297 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    userService.addUserCharacteristic( user.getId(), userCharacteristic );

    assertTrue( user.getUserCharacteristics().size() == 1 );

    mockUserDAO.verify();
  }

  /**
   * Test update user characteristic
   */
  public void testUpdateUserCharacteristic()
  {
    String uniqueName = String.valueOf( Math.random() % 29930293 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    UserCharacteristic userCharacteristicUpdated = new UserCharacteristic();
    userCharacteristicUpdated.setCharacteristicValue( "RED" );
    userCharacteristicUpdated.setUserCharacteristicType( characteristic );
    userCharacteristicUpdated.setUser( user );

    user.getUserCharacteristics().add( userCharacteristic );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserCharacteristic" ).with( same( userCharacteristicUpdated ) ).will( returnValue( userCharacteristicUpdated ) );

    userService.updateUserCharacteristic( user.getId(), userCharacteristicUpdated );

    mockUserDAO.verify();

  } // testupdateUserCharacteristic

  /**
   * Test update user characteristic
   */
  public void testUpdateUserCharacteristics()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setId( new Long( 1 ) );
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristicType characteristic2 = new UserCharacteristicType();
    characteristic2.setId( new Long( 1 ) );
    characteristic2.setCharacteristicName( "testCharacteristic2" );
    characteristic2.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    UserCharacteristic userCharacteristic2 = new UserCharacteristic();
    userCharacteristic2.setCharacteristicValue( "BROWN" );
    userCharacteristic2.setUserCharacteristicType( characteristic2 );
    userCharacteristic2.setUser( user );

    UserCharacteristic userCharacteristicUpdated = new UserCharacteristic();
    userCharacteristicUpdated.setCharacteristicValue( "RED" );
    userCharacteristicUpdated.setUserCharacteristicType( characteristic );
    userCharacteristicUpdated.setUser( user );

    UserCharacteristic userCharacteristicUpdated2 = new UserCharacteristic();
    userCharacteristicUpdated2.setCharacteristicValue( "BLUE" );
    userCharacteristicUpdated2.setUserCharacteristicType( characteristic2 );
    userCharacteristicUpdated2.setUser( user );

    user.getUserCharacteristics().add( userCharacteristic );
    user.getUserCharacteristics().add( userCharacteristic2 );

    List userCharacteristicsUpdated = new ArrayList();

    userCharacteristicsUpdated.add( userCharacteristicUpdated );
    userCharacteristicsUpdated.add( userCharacteristicUpdated2 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    mockCharacteristicDAO.expects( atLeastOnce() ).method( "getCharacteristicById" ).withAnyArguments().will( onConsecutiveCalls( returnValue( characteristic ), returnValue( characteristic2 ) ) );

    mockUserDAO.expects( once() ).method( "updateUserCharacteristics" ).with( same( userCharacteristicsUpdated ) );

    userService.updateUserCharacteristics( user.getId(), userCharacteristicsUpdated );

    mockUserDAO.verify();

  } // testupdateUserCharacteristic

  /**
   * Tests get the set of email addresses for a user.
   */
  public void testGetUserEmailAddresses()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    Set<UserEmailAddress> userEmailAddresses = new HashSet<UserEmailAddress>();
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "test@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddresses.add( userEmailAddress );

    user.setUserEmailAddresses( userEmailAddresses );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    Set returnedUserEmailAddresses = userService.getUserEmailAddresses( user.getId() );
    assertTrue( returnedUserEmailAddresses.size() == 1 );
    mockUserDAO.verify();
  }

  /**
   * Tests updating a UserEmailAddress
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserEmailAddress() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "test@test.com" );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserEmailAddress" ).with( same( userEmailAddress ) );
    userService.updateUserEmailAddress( user.getId(), userEmailAddress );

    mockUserDAO.verify();
  }

  /**
   * Tests updating a UserEmailAddress
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserEmailAddressWithPrimary() throws ServiceErrorException
  {
    Participant paxuser = new Participant();
    paxuser.setId( new Long( 1 ) );
    paxuser.setUserName( "unittest" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserEmailAddress origUserEmailAddress = new UserEmailAddress();
    origUserEmailAddress.setEmailAddr( "test@test.com" );
    origUserEmailAddress.setEmailType( EmailAddressType.lookup( "hom" ) );
    origUserEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserEmailAddress.setIsPrimary( Boolean.TRUE );
    origUserEmailAddress.setUser( paxuser );

    Set userEmailList = new LinkedHashSet();
    userEmailList.add( origUserEmailAddress );
    participant.setUserPhones( userEmailList );

    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserEmailAddress origUserEmailAddress2 = new UserEmailAddress();
    origUserEmailAddress2.setEmailAddr( "test2@test.com" );
    origUserEmailAddress2.setIsPrimary( Boolean.TRUE );
    origUserEmailAddress2.setEmailType( EmailAddressType.lookup( "hom" ) );
    origUserEmailAddress2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserEmailAddress2.setUser( paxuser );

    Set userEmailList2 = new LinkedHashSet();
    userEmailList2.add( origUserEmailAddress2 );
    participant2.setUserPhones( userEmailList2 );

    participant2.setAwardBanqNumber( "1234" );
    // ----------------

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "test@test.com" );
    userEmailAddress.setIsPrimary( Boolean.TRUE );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "hom" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );

    paxuser.getUserEmailAddresses().add( userEmailAddress );

    UserEmailAddress updatedUserEmailAddress2 = new UserEmailAddress();
    updatedUserEmailAddress2.setEmailAddr( "test2@test.com" );
    updatedUserEmailAddress2.setIsPrimary( Boolean.TRUE );
    updatedUserEmailAddress2.setEmailType( EmailAddressType.lookup( "hom" ) );
    updatedUserEmailAddress2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( paxuser.getId() ) ).will( returnValue( paxuser ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( paxuser.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockUserDAO.expects( once() ).method( "updateUserEmailAddress" ).with( same( updatedUserEmailAddress2 ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.updateUserEmailAddress( paxuser.getId(), updatedUserEmailAddress2 );
    mockUserDAO.verify();
  }

  /**
   * Tests updating a UserEmailAddress
   * 
   * @throws ServiceErrorException
   */
  public void testUpdatePrimaryUserEmailAddress() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserEmailAddress origUserEmailAddress = new UserEmailAddress();
    origUserEmailAddress.setEmailAddr( "test@test.com" );
    origUserEmailAddress.setEmailType( EmailAddressType.lookup( "hom" ) );
    origUserEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserEmailAddress.setIsPrimary( Boolean.TRUE );
    origUserEmailAddress.setUser( user );

    Set userEmailList = new LinkedHashSet();
    userEmailList.add( origUserEmailAddress );
    participant.setUserPhones( userEmailList );

    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserEmailAddress origUserEmailAddress2 = new UserEmailAddress();
    origUserEmailAddress2.setEmailAddr( "test2@test.com" );
    origUserEmailAddress2.setIsPrimary( Boolean.TRUE );
    origUserEmailAddress2.setEmailType( EmailAddressType.lookup( "bus" ) );
    origUserEmailAddress2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserEmailAddress2.setUser( user );

    Set userEmailList2 = new LinkedHashSet();
    userEmailList2.add( origUserEmailAddress2 );
    participant2.setUserPhones( userEmailList2 );

    participant2.setAwardBanqNumber( "1234" );
    // ----------------

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "test@test.com" );
    userEmailAddress.setIsPrimary( Boolean.TRUE );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "hom" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );

    UserEmailAddress updatedUserEmailAddress2 = new UserEmailAddress();
    updatedUserEmailAddress2.setEmailAddr( "test2@test.com" );
    updatedUserEmailAddress2.setIsPrimary( Boolean.FALSE );
    updatedUserEmailAddress2.setEmailType( EmailAddressType.lookup( "bus" ) );
    updatedUserEmailAddress2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );

    user.getUserEmailAddresses().add( userEmailAddress );
    user.getUserEmailAddresses().add( updatedUserEmailAddress2 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.setPrimaryUserEmailAddress( user.getId(), updatedUserEmailAddress2.getEmailType() );
    mockUserDAO.verify();
  }

  /**
   * Tests deleting a user email address bt email address type
   */
  public void testDeleteUserPrimaryEmailAddressbyType()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setId( new Long( 1 ) );
    userEmailAddress.setEmailAddr( "test@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( Boolean.TRUE );
    user.getUserEmailAddresses().add( userEmailAddress );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    try
    {
      userService.deleteUserEmailAddressbyType( user.getId(), EmailAddressType.lookup( "home" ) );
      fail( "Should have thrown Exception deleting Primary EmailAddr" );
    }
    catch( ServiceErrorException e )
    {
      // the test passed as primary email can NOT be deleted
    }

    mockUserDAO.verify();
  }

  /**
   * Tests deleting a user email address bt email address type
   * 
   * @throws Exception
   */
  public void testDeleteUserEmailAddressbyType() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setId( new Long( 1 ) );
    userEmailAddress.setEmailAddr( "test@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( Boolean.FALSE );
    user.getUserEmailAddresses().add( userEmailAddress );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    userService.deleteUserEmailAddressbyType( user.getId(), EmailAddressType.lookup( "home" ) );

    assertTrue( user.getUserEmailAddresses().size() == 0 );
    mockUserDAO.verify();
  }

  /**
   * Tests adding a new UserEmailAddress for a user
   * 
   * @throws ServiceErrorException
   */
  public void testAddUserEmailAddress() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    // has no emails
    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserEmailAddress userEmailAddress2 = new UserEmailAddress();
    userEmailAddress2.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress2.setEmailAddr( "test@test.com" );
    userEmailAddress2.setIsPrimary( Boolean.TRUE );

    Set userEmailAddressList = new LinkedHashSet();
    userEmailAddressList.add( userEmailAddress2 );
    participant2.setUserEmailAddresses( userEmailAddressList );
    participant2.setAwardBanqNumber( "1234" );
    // -------------------

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setEmailAddr( "test@test.com" );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.addUserEmailAddress( user.getId(), userEmailAddress );
    assertTrue( user.getUserEmailAddresses().size() == 1 );
    mockUserDAO.verify();
  }

  /**
   * Tests the ftech of primary email address for user
   */
  public void testGetUserPrimaryEmailAddress()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress nonPrimaryUserEmailAddress = new UserEmailAddress();
    nonPrimaryUserEmailAddress.setId( new Long( 1 ) );
    nonPrimaryUserEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    nonPrimaryUserEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    nonPrimaryUserEmailAddress.setEmailAddr( "test1@test.com" );
    nonPrimaryUserEmailAddress.setIsPrimary( Boolean.FALSE );

    user.getUserEmailAddresses().add( nonPrimaryUserEmailAddress );

    UserEmailAddress primaryUserEmailAddress = new UserEmailAddress();
    primaryUserEmailAddress.setId( new Long( 2 ) );
    primaryUserEmailAddress.setEmailType( EmailAddressType.lookup( "work" ) );
    primaryUserEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    primaryUserEmailAddress.setEmailAddr( "test2@test.com" );
    primaryUserEmailAddress.setIsPrimary( Boolean.TRUE );
    user.getUserEmailAddresses().add( primaryUserEmailAddress );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    UserEmailAddress expectedUserEmailAddress = userService.getPrimaryUserEmailAddress( user.getId() );
    assertEquals( "Failed to get Primary UserEmailAddress", expectedUserEmailAddress, primaryUserEmailAddress );
    mockUserDAO.verify();
  }

  /**
   * Test adding a user phone
   * 
   * @throws ServiceErrorException
   */
  public void testAddUserPhone() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    // has no phone
    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserPhone userPhone2 = new UserPhone();
    userPhone2.setPhoneType( PhoneType.lookup( "bus" ) );
    userPhone2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhone2.setPhoneNbr( "9119119111" );
    userPhone2.setPhoneExt( "1234" );
    userPhone2.setIsPrimary( Boolean.TRUE );

    Set userPhoneList = new LinkedHashSet();
    userPhoneList.add( userPhone2 );
    participant2.setUserPhones( userPhoneList );
    participant2.setAwardBanqNumber( "1234" );
    // -------------------

    UserPhone userPhone = new UserPhone();
    userPhone.setPhoneType( PhoneType.lookup( "bus" ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhone.setPhoneNbr( "9119119111" );
    userPhone.setPhoneExt( "1234" );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.addUserPhone( user.getId(), userPhone );

    assertTrue( user.getUserPhones().size() == 1 );

    // make sure phone added is primary (since it's the only one)
    assertTrue( "Phone Record should have been set to primary", userPhone.isPrimary() );

    mockUserDAO.verify();
  }

  /**
   * Test Deleting User Phone
   */
  public void testDeleteUserPhone()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhone = new UserPhone();
    userPhone.setPhoneType( PhoneType.lookup( "home" ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhone.setPhoneNbr( "9119119111" );
    userPhone.setPhoneExt( "1234" );

    user.getUserPhones().add( userPhone );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    userService.deleteUserPhone( user.getId(), "home" );
    assertTrue( user.getUserPhones().size() == 0 );

    mockUserDAO.verify();
  }

  /**
   * Test get a user phone by type
   */
  public void testGetUserPhone()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );

    UserPhone userPhoneBus = new UserPhone();
    userPhoneBus.setPhoneType( PhoneType.lookup( "bus" ) );
    userPhoneBus.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneBus.setPhoneNbr( "1112223333" );
    userPhoneBus.setPhoneExt( "1234" );

    user.getUserPhones().add( userPhoneHome );
    user.getUserPhones().add( userPhoneBus );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    UserPhone returnedUserPhone = userService.getUserPhone( user.getId(), userPhoneHome.getPhoneType().getCode() );

    assertEquals( returnedUserPhone, userPhoneHome );
  }

  /**
   * Test get all of the userphone records
   */
  public void testGetUserPhones()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );

    UserPhone userPhoneBus = new UserPhone();
    userPhoneBus.setPhoneType( PhoneType.lookup( "bus" ) );
    userPhoneBus.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneBus.setPhoneNbr( "1112223333" );
    userPhoneBus.setPhoneExt( "1234" );

    user.getUserPhones().add( userPhoneHome );
    user.getUserPhones().add( userPhoneBus );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    Set userPhones = userService.getUserPhones( user.getId() );

    assertTrue( userPhones.size() == 2 );
  }

  /**
   * Test updating the primary phone
   * 
   * @throws ServiceErrorException
   */
  public void testUpdatePrimaryPhone() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );
    userPhoneHome.setIsPrimary( Boolean.valueOf( true ) );

    UserPhone userPhoneBus = new UserPhone();
    userPhoneBus.setPhoneType( PhoneType.lookup( "bus" ) );
    userPhoneBus.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneBus.setPhoneNbr( "1112223333" );
    userPhoneBus.setPhoneExt( "1234" );
    userPhoneHome.setIsPrimary( Boolean.valueOf( false ) );

    user.getUserPhones().add( userPhoneHome );
    user.getUserPhones().add( userPhoneBus );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserPhone origUserPhone = new UserPhone();
    origUserPhone.setPhoneType( PhoneType.lookup( "home" ) );
    origUserPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserPhone.setPhoneNbr( "9119119111" );
    origUserPhone.setPhoneExt( "1234" );
    origUserPhone.setIsPrimary( Boolean.TRUE );
    origUserPhone.setUser( user );

    Set userPhoneList = new LinkedHashSet();
    userPhoneList.add( origUserPhone );
    participant.setUserPhones( userPhoneList );

    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserPhone origUserPhone2 = new UserPhone();
    origUserPhone2.setPhoneType( PhoneType.lookup( "bus" ) );
    origUserPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserPhone2.setPhoneNbr( "1112223333" );
    origUserPhone2.setPhoneExt( "1234" );
    origUserPhone2.setIsPrimary( Boolean.TRUE );
    origUserPhone2.setUser( user );

    Set userPhoneList2 = new LinkedHashSet();
    userPhoneList2.add( origUserPhone2 );
    participant2.setUserPhones( userPhoneList2 );

    participant2.setAwardBanqNumber( "1234" );
    // ----------------

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.updatePrimaryPhone( user.getId(), userPhoneBus.getPhoneType().getCode() );
    assertEquals( userPhoneBus.isPrimary(), true );
  }

  /**
   * Test update user phone
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserPhone() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );

    UserPhone userPhoneUpdated = new UserPhone();
    userPhoneUpdated.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneUpdated.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneUpdated.setPhoneNbr( "1112223333" );
    userPhoneUpdated.setPhoneExt( "1234" );

    user.getUserPhones().add( userPhoneHome );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserPhone" ).with( same( userPhoneUpdated ) ).will( returnValue( userPhoneUpdated ) );

    userService.updateUserPhone( user.getId(), userPhoneUpdated );

    assertEquals( userPhoneUpdated.getPhoneType(), userPhoneHome.getPhoneType() );

  } // testupdateUserPhone

  /**
   * Test update user phone
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserPhoneWithPrimary() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserPhone origUserPhone = new UserPhone();
    origUserPhone.setPhoneType( PhoneType.lookup( "home" ) );
    origUserPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserPhone.setPhoneNbr( "9119119111" );
    origUserPhone.setPhoneExt( "1234" );
    origUserPhone.setIsPrimary( Boolean.TRUE );
    origUserPhone.setUser( user );

    Set userPhoneList = new LinkedHashSet();
    userPhoneList.add( origUserPhone );
    participant.setUserPhones( userPhoneList );

    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserPhone origUserPhone2 = new UserPhone();
    origUserPhone2.setPhoneType( PhoneType.lookup( "home" ) );
    origUserPhone2.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    origUserPhone2.setPhoneNbr( "1112223333" );
    origUserPhone2.setPhoneExt( "1234" );
    origUserPhone2.setIsPrimary( Boolean.TRUE );
    origUserPhone2.setUser( user );

    Set userPhoneList2 = new LinkedHashSet();
    userPhoneList2.add( origUserPhone2 );
    participant2.setUserPhones( userPhoneList2 );

    participant2.setAwardBanqNumber( "1234" );
    // ----------------

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );

    UserPhone userPhoneUpdated = new UserPhone();
    userPhoneUpdated.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneUpdated.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneUpdated.setPhoneNbr( "1112223333" );
    userPhoneUpdated.setPhoneExt( "1234" );
    userPhoneUpdated.setIsPrimary( Boolean.TRUE );

    user.getUserPhones().add( userPhoneHome );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockUserDAO.expects( once() ).method( "updateUserPhone" ).with( same( userPhoneUpdated ) ).will( returnValue( userPhoneUpdated ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.updateUserPhone( user.getId(), userPhoneUpdated );

    assertEquals( userPhoneUpdated.getPhoneType(), userPhoneHome.getPhoneType() );

  } // testupdateUserPhone

  /**
   * Builds a test address;
   * 
   * @return Address
   */
  private Address buildTestAddress()
  {
    Address address = new Address();

    address.setCountry( CountryDAOImplTest.buildUnitedStates() );
    address.setAddr1( "123 Main Street" );
    address.setAddr2( "Suite 33" );
    address.setAddr3( "Office 9" );

    address.setCity( "Anytown" );
    address.setStateType( StateType.lookup( "mn" ) );
    address.setPostalCode( "52901" );

    assertNotNull( "Country should not be null", address.getCountry() );
    assertNotNull( "StateType 'mn' should not be null", address.getStateType() );

    return address;
  }

  /**
   * Builds a test address;
   * 
   * @return Address
   */
  private Address buildTestAddress2()
  {
    Address address = new Address();
    address.setCountry( CountryDAOImplTest.buildUnitedStates() );
    address.setAddr1( "5678 Main Street" );
    address.setAddr2( "Suite 5" );
    address.setAddr3( "Office 9" );
    address.setCity( "Sometown" );
    address.setStateType( StateType.lookup( "mn" ) );
    address.setPostalCode( "55555" );

    assertNotNull( "Country should not be null", address.getCountry() );
    assertNotNull( "StateType 'mn' should not be null", address.getStateType() );

    return address;
  }

  /**
   * Builds a test address;
   * 
   * @return Address
   */
  private Address buildTestForeignAddress()
  {
    Address address = new Address();
    address.setCountry( CountryDAOImplTest.buildFrance() );
    address.setAddr1( "5678 Main Street" );
    address.setAddr2( "Suite 5" );
    address.setAddr3( "Office 9" );

    assertNotNull( "Country should not be null", address.getCountry() );

    return address;
  }

  /**
   * Test adding a user address
   * 
   * @throws ServiceErrorException
   */
  public void testAddUserAddress() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    // has no addresses
    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress updAddress1 = new UserAddress();
    updAddress1.setAddress( buildTestAddress() );
    updAddress1.setAddressType( AddressType.lookup( "hom" ) );
    updAddress1.setIsPrimary( Boolean.TRUE );

    Set userAddressList = new LinkedHashSet();
    userAddressList.add( updAddress1 );
    participant2.setUserAddresses( userAddressList );
    participant2.setAwardBanqNumber( "1234" );
    // -------------------

    // Note: since getUserById is called 3 times (once for each add), using the "onConsecutiveCalls"
    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ), returnValue( user ) ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );

    UserAddress newAddress1 = userService.addUserAddress( user.getId(), userAddress1 );
    assertTrue( newAddress1.isPrimary() );
    assertTrue( user.getUserAddresses().size() == 1 );

    // add a 2nd address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );

    UserAddress newAddress2 = userService.addUserAddress( user.getId(), userAddress2 );
    assertFalse( newAddress2.isPrimary() );
    assertTrue( user.getUserAddresses().size() == 2 );

    // add a 3rd address
    UserAddress userAddress3 = new UserAddress();
    userAddress3.setAddress( buildTestAddress() );
    userAddress3.setAddressType( AddressType.lookup( "shp" ) );

    UserAddress newAddress3 = userService.addUserAddress( user.getId(), userAddress3 );
    assertFalse( newAddress3.isPrimary() );
    assertTrue( user.getUserAddresses().size() == 3 );

    mockUserDAO.verify();
  } // end testAddUserAddress

  /**
   * Test Deleting User Address
   * 
   * @throws ServiceErrorException
   */
  public void testDeleteUserAddress() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.FALSE );

    user.getUserAddresses().add( userAddress1 );

    assertTrue( user.getUserAddresses().size() == 1 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    userService.deleteUserAddress( user.getId(), "hom" );

    assertTrue( user.getUserAddresses().size() == 0 );

    mockUserDAO.verify();
  } // end testDeleteUserAddress

  /**
   * Test Deleting User Primary Address
   */
  public void testDeletePrimaryUserAddress()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );

    user.getUserAddresses().add( userAddress1 );

    assertTrue( user.getUserAddresses().size() == 1 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    try
    {
      userService.deleteUserAddress( user.getId(), "hom" );
      fail( "Should never get here. Cannot delete the primary address.  It should have gone into ServiceErrorException" );
    }
    catch( ServiceErrorException se )
    {
      // good - want to go into exception if deleting primary
    }

    assertTrue( user.getUserAddresses().size() == 1 );

    mockUserDAO.verify();
  } // end testDeleteUserAddress

  /**
   * Test get a user address by type
   */
  public void testGetUserAddress()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    user.getUserAddresses().add( userAddress1 );

    // add a 2nd address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    user.getUserAddresses().add( userAddress2 );

    assertTrue( user.getUserAddresses().size() == 2 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    // Get the specific user address
    UserAddress returnedUserAddress = userService.getUserAddress( user.getId(), userAddress1.getAddressType().getCode() );

    assertEquals( returnedUserAddress, userAddress1 );
  } // end testGetUserAddress

  /**
   * Test get all of the user address records
   */
  public void testGetUserAddresses()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    user.getUserAddresses().add( userAddress1 );

    // add a 2nd address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    user.getUserAddresses().add( userAddress2 );

    assertTrue( user.getUserAddresses().size() == 2 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    Set userAddresses = userService.getUserAddresses( user.getId() );

    assertTrue( userAddresses.size() == 2 );

  } // end testGetUserAddresses

  /**
   * Test updating the primary address
   * 
   * @throws ServiceErrorException
   */
  public void testUpdatePrimaryAddress() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress origUserAddress = new UserAddress();
    origUserAddress.setAddress( buildTestAddress() );
    origUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    origUserAddress.setIsPrimary( Boolean.TRUE );
    origUserAddress.setUser( user );

    UserAddress origUserAddress2 = new UserAddress();
    origUserAddress2.setAddress( buildTestAddress2() );
    origUserAddress2.setAddressType( AddressType.lookup( "bus" ) );
    origUserAddress2.setIsPrimary( Boolean.FALSE );
    origUserAddress2.setUser( user );

    Set userAddressList = new LinkedHashSet();
    userAddressList.add( origUserAddress );
    userAddressList.add( origUserAddress2 );
    participant.setUserAddresses( userAddressList );

    participant.setAwardBanqNumber( "1234" );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress updUserAddress = new UserAddress();
    updUserAddress.setAddress( buildTestAddress() );
    updUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    updUserAddress.setIsPrimary( Boolean.FALSE );
    updUserAddress.setUser( user );

    UserAddress updUserAddress2 = new UserAddress();
    updUserAddress2.setAddress( buildTestAddress2() );
    updUserAddress2.setAddressType( AddressType.lookup( "bus" ) );
    updUserAddress2.setIsPrimary( Boolean.TRUE );
    updUserAddress2.setUser( user );

    Set userAddressList2 = new LinkedHashSet();
    userAddressList2.add( updUserAddress );
    userAddressList2.add( updUserAddress2 );

    participant2.setUserAddresses( userAddressList2 );
    participant2.setAwardBanqNumber( "1234" );
    // -------------------

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    user.getUserAddresses().add( userAddress1 );

    // add a 2nd address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress2() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    user.getUserAddresses().add( userAddress2 );

    assertTrue( user.getUserAddresses().size() == 2 );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );

    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.updatePrimaryAddress( user.getId(), userAddress2.getAddressType().getCode() );

    assertEquals( userAddress2.isPrimary(), true );
    assertEquals( userAddress1.isPrimary(), false );

  } // end testUpdatePrimaryAddress

  /**
   * Test updating the primary address with a different country
   */
  public void testUpdatePrimaryAddressWithDifCountry()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    user.getUserAddresses().add( userAddress1 );

    // add a 2nd address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestForeignAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    user.getUserAddresses().add( userAddress2 );

    assertTrue( user.getUserAddresses().size() == 2 );
    assertEquals( userAddress1.isPrimary(), true );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );

    try
    {
      // attempt to now change the primary address to one of a different country
      userService.updatePrimaryAddress( user.getId(), userAddress2.getAddressType().getCode() );
      fail( "Should never get here.  An exception should be thrown when trying to change the primary address to an address with a different country" );
    }
    catch( ServiceErrorException se )
    {
      // good - should get here
    }

  } // end testUpdatePrimaryAddress

  /**
   * Tests updating the First User Address
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateFirstUserAddress() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress origUserAddress = new UserAddress();
    origUserAddress.setAddress( buildTestAddress2() );
    origUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    origUserAddress.setIsPrimary( Boolean.TRUE );
    origUserAddress.setUser( user );

    Set userAddressList = new LinkedHashSet();
    userAddressList.add( origUserAddress );
    participant.setUserAddresses( userAddressList );

    participant.setAwardBanqNumber( "1234" );

    // updated address
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestAddress2() );
    updatedUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    updatedUserAddress.setIsPrimary( Boolean.TRUE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );

    mockParticipantDAO.expects( once() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) ).will( returnValue( participant ) );

    mockUserDAO.expects( once() ).method( "updateUserAddress" ).with( same( updatedUserAddress ) ).will( returnValue( updatedUserAddress ) );

    userService.updateUserAddress( user.getId(), updatedUserAddress );

    assertEquals( updatedUserAddress.getAddress().getCity(), "Sometown" );

    // ------------------
    // update a 2nd time
    // ------------------
    UserAddress updatedUserAddress2 = new UserAddress();
    Address addr = buildTestAddress2();
    addr.setCity( "RRcity" );
    updatedUserAddress2.setAddress( addr );
    updatedUserAddress2.setAddressType( AddressType.lookup( "hom" ) );
    updatedUserAddress2.setIsPrimary( Boolean.TRUE );
    updatedUserAddress2.setUser( user );

    // build participant2
    Participant participant2 = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress userAddress2 = new UserAddress();
    Address addr2 = buildTestAddress2();
    addr2.setCity( "RRcity" );
    userAddress2.setAddress( addr2 );
    userAddress2.setAddressType( AddressType.lookup( "hom" ) );
    userAddress2.setIsPrimary( Boolean.TRUE );
    userAddress2.setUser( user );

    Set userAddressList2 = new LinkedHashSet();
    userAddressList2.add( userAddress2 );
    participant2.setUserAddresses( userAddressList2 );
    participant2.setAwardBanqNumber( "1234" );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );

    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( participant ), returnValue( participant2 ) ) );

    mockUserDAO.expects( once() ).method( "updateUserAddress" ).with( same( updatedUserAddress2 ) ).will( returnValue( updatedUserAddress2 ) );

    mockawardBanQServiceFactory.expects( once() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( once() ).method( "updateParticipantInAwardBanQWebService" ).with( same( participant2 ) );

    userService.updateUserAddress( user.getId(), updatedUserAddress2 );

    assertEquals( updatedUserAddress2.getAddress().getCity(), "RRcity" );

  } // end testUpdateFirstUserAddress

  /**
   * Test update user address, trying to update the attributes of the primary address, keeping same
   * country
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserAddressPrimary() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress origUserAddress = new UserAddress();
    origUserAddress.setAddress( buildTestAddress() );
    origUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    origUserAddress.setIsPrimary( Boolean.TRUE );
    origUserAddress.setUser( user );

    Set userAddressList = new LinkedHashSet();
    userAddressList.add( origUserAddress );
    participant.setUserAddresses( userAddressList );
    participant.setAwardBanqNumber( "" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );

    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // updated address
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestAddress2() );
    updatedUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    updatedUserAddress.setIsPrimary( Boolean.TRUE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );
    mockParticipantDAO.expects( once() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) ).will( returnValue( participant ) );
    mockUserDAO.expects( once() ).method( "updateUserAddress" ).with( same( updatedUserAddress ) ).will( returnValue( updatedUserAddress ) );

    userService.updateUserAddress( user.getId(), updatedUserAddress );

    assertEquals( updatedUserAddress.getAddressType(), userAddress1.getAddressType() );
    assertEquals( updatedUserAddress.getAddress().getCity(), "Sometown" );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    // Get the specific user address
    UserAddress returnedUserAddress = userService.getUserAddress( user.getId(), updatedUserAddress.getAddressType().getCode() );

    assertEquals( returnedUserAddress, updatedUserAddress );

  } // end testUpdateUserAddressPrimary

  /**
   * Test update user address, trying to update the attributes of the primary address to a different
   * country - should throw an exception
   */
  public void testUpdateUserAddressPrimaryWithDifCountry()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );

    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // updated address
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestForeignAddress() );
    updatedUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    updatedUserAddress.setIsPrimary( Boolean.TRUE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );

    try
    {
      userService.updateUserAddress( user.getId(), updatedUserAddress );
      fail( "Should not get here. Cannot update primary address with a different country" );
    }
    catch( ServiceErrorException se )
    {
      // Success - should throw and error when updating a primary address with a different country.
    }

  } // end testUpdateUserAddressPrimaryWithDifCountry

  /**
   * Test update user primary address from primary to non primary - trying to set the primary field
   * from true to false - should get exception
   */
  public void testUpdateUserAddressFromPrimaryToNonPrimary()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );

    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // updated address
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestAddress2() );
    updatedUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    updatedUserAddress.setIsPrimary( Boolean.FALSE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );

    try
    {
      userService.updateUserAddress( user.getId(), updatedUserAddress );
      fail( "Should not have gotten here - a Service Exception should have been thrown." );
    }
    catch( ServiceErrorException se )
    {
      // good should get here. An exception should be thrown trying to update the primary value to
      // false.
    }

  } // end testUpdateUserAddressFromPrimaryToNonPrimary

  /**
   * Test update user address, trying to update the attributes of a non primary address
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserAddressNonPrimary() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );
    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // 2nd Address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    userAddress2.setUser( user );
    // add the 2nd address
    user.getUserAddresses().add( userAddress2 );

    // update the 2nd address, and not try to change primary value
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestAddress2() );
    updatedUserAddress.setAddressType( AddressType.lookup( "bus" ) );
    updatedUserAddress.setIsPrimary( Boolean.FALSE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );
    mockUserDAO.expects( once() ).method( "updateUserAddress" ).with( same( updatedUserAddress ) ).will( returnValue( updatedUserAddress ) );

    userService.updateUserAddress( user.getId(), updatedUserAddress );

    assertEquals( updatedUserAddress.getAddressType(), userAddress2.getAddressType() );
    assertEquals( updatedUserAddress.getAddress().getCity(), "Sometown" );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    // Get the specific user address
    UserAddress returnedUserAddress = userService.getUserAddress( user.getId(), updatedUserAddress.getAddressType().getCode() );

    assertEquals( returnedUserAddress, updatedUserAddress );

  } // end testUpdateUserAddressNonPrimary

  /**
   * Test update user address, trying to update the attributes of a non primary address Changing it
   * to be the primary address of the same country
   * 
   * @throws ServiceErrorException
   */
  public void testUpdateUserAddressNonPrimaryToPrimaryOfSameCountry() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );
    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress origUserAddress = new UserAddress();
    origUserAddress.setAddress( buildTestAddress() );
    origUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    origUserAddress.setIsPrimary( Boolean.TRUE );
    origUserAddress.setUser( user );

    Set userAddressList = new LinkedHashSet();
    userAddressList.add( origUserAddress );
    participant.setUserAddresses( userAddressList );
    participant.setAwardBanqNumber( "" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );
    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // 2nd Address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    userAddress2.setUser( user );
    // add the 2nd address
    user.getUserAddresses().add( userAddress2 );

    // update the 2nd address, and not try to change primary value, but have the same country
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestAddress2() );
    updatedUserAddress.setAddressType( AddressType.lookup( "bus" ) );
    updatedUserAddress.setIsPrimary( Boolean.TRUE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( user ), returnValue( user ), returnValue( user ), returnValue( user ) ) );
    mockParticipantDAO.expects( once() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) ).will( returnValue( participant ) );

    mockUserDAO.expects( once() ).method( "updateUserAddress" ).with( same( updatedUserAddress ) ).will( returnValue( updatedUserAddress ) );

    userService.updateUserAddress( user.getId(), updatedUserAddress );

    assertEquals( updatedUserAddress.getAddressType(), userAddress2.getAddressType() );
    assertEquals( updatedUserAddress.getAddress().getCity(), "Sometown" );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    // Get the specific user address
    UserAddress returnedUserAddress = userService.getUserAddress( user.getId(), updatedUserAddress.getAddressType().getCode() );

    assertEquals( returnedUserAddress, updatedUserAddress );

  } // end testUpdateUserAddressNonPrimaryToPrimaryOfSameCountry

  /**
   * Test update user address, trying to update the attributes of a non primary address Changing it
   * to be the primary address of the same country
   */
  public void testUpdateUserAddressNonPrimaryToPrimaryOfDifCountry()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );
    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // 2nd Address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    userAddress2.setUser( user );
    // add the 2nd address
    user.getUserAddresses().add( userAddress2 );

    // update the 2nd address, and not try to change primary value, but have the same country
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestForeignAddress() );
    updatedUserAddress.setAddressType( AddressType.lookup( "bus" ) );
    updatedUserAddress.setIsPrimary( Boolean.TRUE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) )
        .will( onConsecutiveCalls( returnValue( user ), returnValue( user ), returnValue( user ), returnValue( user ) ) );

    try
    {
      userService.updateUserAddress( user.getId(), updatedUserAddress );
      fail( "Should not get here. An exception should be thrown if trying to update primary address to one of a different country." );
    }
    catch( ServiceErrorException se )
    {
      // Success - An exception should be thrown if trying to update primary address to one of a
      // different country
    }

  } // end testUpdateUserAddressNonPrimaryToPrimaryOfDifCountry

  /**
   * Test addUserNode adds the Node to the user
   */
  public void testAddUserNode()
  {

    Node node = new Node();
    node.setId( new Long( 1 ) );
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserNode userNode = createUserNode( node, user );

    mockNodeDAO.expects( atLeastOnce() ).method( "getNodeById" ).with( same( node.getId() ) ).will( returnValue( node ) );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    userService.addUserNode( user.getId(), userNode );

    // mockNodeDAO.verify();
    mockUserDAO.verify();

  }

  /**
   * Test that getting the Primary Hierarchy UserNode.
   */
  public void testGetUserNodesEmpty()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    Set userNodes = Collections.EMPTY_SET;

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    Set recievedUserNodes = userService.getUserNodes( user.getId() );

    assertEquals( "User nodes set did not match.", userNodes, recievedUserNodes );
    mockUserDAO.verify();
  }

  /**
   * Tests the service pass through to the DAO to get a list of Nodes assigned to the user.
   */
  public void testGetAssignedNodes()
  {

    User user = new User();
    user.setId( new Long( 1 ) );

    Node node1 = new Node();
    node1.setId( new Long( 34 ) );

    Node node2 = new Node();
    node2.setId( new Long( 512 ) );

    UserNode userNode1 = createUserNode( node1, user );

    UserNode userNode2 = createUserNode( node2, user );

    user.addUserNode( userNode1 );
    user.addUserNode( userNode2 );

    List assignedNodes = new ArrayList();
    assignedNodes.add( node1 );
    assignedNodes.add( node2 );

    mockUserDAO.expects( once() ).method( "getAssignedNodes" ).with( same( user.getId() ) ).will( returnValue( assignedNodes ) );

    List actualAssignedNodes = userService.getAssignedNodes( user.getId() );

    assertEquals( "Assigned node list wasn't what was expected.", actualAssignedNodes, assignedNodes );

    mockUserDAO.verify();

  }

  /**
   * Test the generatePassword method
   */
  // Comment out below
  // See test in PasswordPolicyStrategyImplTest
  // public void testGeneratePassword()
  // {
  // String password = userService.generatePassword();
  //
  // assertTrue( "Password was null", password != null );
  // }
  /**
   * Test <code>UserNodes</code>'s on a user are updated when a user is moved from one
   * <code>node</code> to another <code>node</code>.
   */
  public void testUpdateUserNodeChangeNode()
  {
    User user = new User();
    user.setId( new Long( 1 ) );

    Node node1 = new Node();
    node1.setId( new Long( 2 ) );

    Node node2 = new Node();
    node2.setId( new Long( 3 ) );

    UserNode userNode1 = createUserNode( node1, user );

    createUserNode( node2, user );

    Set userNodes = new LinkedHashSet();
    userNodes.add( userNode1 );
    node1.setUserNodes( userNodes );
    user.addUserNode( userNode1 );
    // user.addUserNode( userNode2 );

    node2.setUserNodes( Collections.EMPTY_SET );

    List expectedUserList = new ArrayList();
    expectedUserList.add( user );

    // Mock a nodeDAO to return a node
    mockNodeDAO.expects( atLeastOnce() ).method( "getNodeById" ).will( onConsecutiveCalls( returnValue( node2 ), returnValue( node2 ) ) );

    mockUserDAO.expects( once() ).method( "getAllUsersOnNode" ).will( returnValue( expectedUserList ) );

    // call the update method to move a user from one node to another and check they were moved.
    userService.updateUserNodeChangeNode( node1.getId(), node2.getId() );

    mockUserDAO.verify();
    mockNodeDAO.verify();
    assertEquals( "Move failed", node2, user.getUserNodeByNodeId( node2.getId() ).getNode() );
  }

  /**
   * testMoveUserToNode
   */
  public void testMoveUserToNode()
  {
    Node oldNode = new Node();
    oldNode.setId( new Long( 1 ) );

    Node newNode = new Node();
    newNode.setId( new Long( 2 ) );

    User user1 = new User();
    user1.setId( new Long( 1 ) );

    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setUser( user1 );
    userNode1.setNode( oldNode );
    userNode1.setActive( Boolean.valueOf( true ) );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( "own" ) );

    Set userNodes1 = new LinkedHashSet();
    userNodes1.add( userNode1 );

    user1.setUserNodes( userNodes1 );
    userService.moveUserToNode( oldNode.getId(), newNode, user1 );
    assertEquals( "Failed to reset owner", true, user1.getUserNodeByNodeId( newNode.getId() ).getHierarchyRoleType().isOwner() );

  }

  /**
   * testMoveUserToNodeWithCurrentOwner
   */
  public void testMoveUserToNodeWithCurrentOwner()
  {
    Node oldNode = new Node();
    oldNode.setId( new Long( 1 ) );

    Node newNode = new Node();
    newNode.setId( new Long( 2 ) );

    User user1 = new User();
    user1.setId( new Long( 1 ) );

    User user2 = new User();
    user2.setId( new Long( 2 ) );

    UserNode userNode1 = new UserNode();
    userNode1.setIsPrimary( Boolean.TRUE );
    userNode1.setUser( user1 );
    userNode1.setNode( oldNode );
    userNode1.setActive( Boolean.valueOf( true ) );
    userNode1.setHierarchyRoleType( HierarchyRoleType.lookup( "own" ) );

    createUserNode( newNode, user2 );

    Set userNodes1 = new LinkedHashSet();
    userNodes1.add( userNode1 );

    Set userNodes2 = new LinkedHashSet();
    userNodes2.add( userNode1 );
    newNode.setUserNodes( userNodes2 );

    user1.setUserNodes( userNodes1 );

    userService.moveUserToNode( oldNode.getId(), newNode, user1 );

    assertEquals( "Failed to reset owner", false, user1.getUserNodeByNodeId( newNode.getId() ).getHierarchyRoleType().isOwner() );

  }

  /**
   * Creates a userNode for testing.
   * 
   * @param node1
   * @param user1
   * @return UserNode
   */
  private UserNode createUserNode( Node node1, User user1 )
  {
    UserNode userNode = new UserNode();
    userNode.setIsPrimary( Boolean.TRUE );
    userNode.setUser( user1 );
    userNode.setNode( node1 );
    userNode.setActive( Boolean.valueOf( true ) );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( "w" ) );
    return userNode;
  }

  /**
   * Test getting the User by Email Address.
   */

  public void testGetUserByEmailAddress()
  {
    String emailAddress = "emailaddress@domain.com";
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( emailAddress );

    User user = new User();
    user.addUserEmailAddress( userEmailAddress );

    mockUserDAO.expects( once() ).method( "getUserByEmailAddr" ).with( same( emailAddress ) ).will( returnValue( user ) );

    User userResult = userService.getUserByEmailAddr( emailAddress );
    assertTrue( !userResult.getUserEmailAddresses().isEmpty() );

    mockUserDAO.verify();
  }

  public void testGetBudgetMediaValueForUser()
  {
    Long userId = 1L;

    Country country = new Country();
    country.setCountryCode( "US" );
    country.setBudgetMediaValue( BigDecimal.valueOf( .42 ) );
    Address address = new Address();
    address.setCountry( country );
    UserAddress userAddress = new UserAddress();
    userAddress.setAddress( address );
    userAddress.setIsPrimary( true );
    User user = new User();
    user.addUserAddress( userAddress );

    mockUserDAO.expects( once() ).method( "getPrimaryUserAddressCountry" ).with( same( userId ) ).will( returnValue( country ) );

    BigDecimal budgetMediaValue = userService.getBudgetMediaValueForUser( userId );
    assertEquals( BigDecimal.valueOf( .42 ), budgetMediaValue );

    mockUserDAO.verify();
  }

  /**
   * Test getting the User by Email Address.
   */
  public void testGetUserDetailsByEmailAddrs()
  {
    String emailAddress = "emailaddress@domain.com";
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( emailAddress );

    User user = new User();
    user.addUserEmailAddress( userEmailAddress );

    mockUserDAO.expects( once() ).method( "getUserByEmailAddr" ).with( same( emailAddress ) ).will( returnValue( user ) );

    User userResult = userService.getUserByEmailAddr( emailAddress );
    assertTrue( !userResult.getUserEmailAddresses().isEmpty() );

    mockUserDAO.verify();
  }

  public void testGetUserEmailAddressById()
  {
    mockUserDAO.expects( once() ).method( "getUserEmailAddressById" ).with( same( 1L ) ).will( returnValue( buildUserEmailAddress() ) );
    UserEmailAddress userEmailAddress = userService.getUserEmailAddressById( 1L );
    assertTrue( "User Email by id does not match", userEmailAddress.getEmailAddr().equals( "someemail@email.com" ) );
  }

  public void testGetUserPhoneById()
  {
    mockUserDAO.expects( once() ).method( "getUserPhoneById" ).with( same( 1L ) ).will( returnValue( buildUserPhone() ) );
    UserPhone userPhone = userService.getUserPhoneById( 1L );
    assertTrue( "User Phone by id does not match", userPhone.getPhoneNbr().equals( "123-456-7890" ) );
  }

  public void testGetUserDetailsByEmailAddr()
  {
    mockUserDAO.expects( once() ).method( "getUserIdsByEmail" ).with( same( "someemail@email.com" ) ).will( returnValue( new HashSet<>( Arrays.asList( 1L, 2L ) ) ) );
    Set<Long> users = userService.getUserIdsByEmailOrPhone( "someemail@email.com" );
    assertTrue( "Users by email address returned null", !CollectionUtils.isEmpty( users ) );
  }

  private UserEmailAddress buildUserEmailAddress()
  {
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setId( 1L );
    userEmailAddress.setEmailAddr( "someemail@email.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.BUSINESS ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( true );
    userEmailAddress.setUser( buildUser() );
    return userEmailAddress;
  }

  private UserPhone buildUserPhone()
  {
    UserPhone userPhone = new UserPhone();
    userPhone.setId( 1L );
    userPhone.setIsPrimary( true );
    userPhone.setPhoneNbr( "123-456-7890" );
    userPhone.setUser( buildUser() );
    return userPhone;
  }

  private User buildUser()
  {
    // Get the test User.
    User user = new User();
    user.setId( 1L );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    return user;
  }

  public void testIsUniqueEmailAddr()
  {
    mockUserDAO.expects( once() ).method( "isUniqueEmail" ).with( same( "someemail12345@email.com" ) ).will( returnValue( true ) );
    boolean unique = userService.isUniqueEmail( "someemail12345@email.com" );
    assertTrue( unique );
  }

  public void testGetAllStrongMailUsers()
  {
    List<StrongMailUser> strongMailUsers = new ArrayList();
    mockUserDAO.expects( once() ).method( "getAllStrongMailUsers" ).will( returnValue( strongMailUsers ) );
    strongMailUsers = userService.getAllStrongMailUsers();
    assertTrue( strongMailUsers.size() == 0 );
  }

  /**
   * Test updating the User through the service.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public void testUpdatePerson() throws UniqueConstraintViolationException, ServiceErrorException
  {

    // Create the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    User dbUser = new User();
    dbUser.setId( new Long( 1 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // update the user

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( dbUser ) );

    // test the UserService.addUser
    userService.updateUser( user );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();
  }

  public void testupdateEmailAddressForPerson() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "test@test.com" );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserEmailAddress" ).with( same( userEmailAddress ) );
    userService.updateUserEmailAddress( user.getId(), userEmailAddress );

    mockUserDAO.verify();
  }

  /**
   * Test update user phone
   * 
   * @throws ServiceErrorException
   */
  public void test() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );

    UserPhone userPhoneUpdated = new UserPhone();
    userPhoneUpdated.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneUpdated.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneUpdated.setPhoneNbr( "1112223333" );
    userPhoneUpdated.setPhoneExt( "1234" );

    user.getUserPhones().add( userPhoneHome );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserPhone" ).with( same( userPhoneUpdated ) ).will( returnValue( userPhoneUpdated ) );

    userService.updateUserPhone( user.getId(), userPhoneUpdated );

    assertEquals( userPhoneUpdated.getPhoneType(), userPhoneHome.getPhoneType() );

  }

  /**
   * Test get all of the user address records
   */
  public void testupdateAddressForPerson()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // add 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    user.getUserAddresses().add( userAddress1 );

    // add a 2nd address
    UserAddress userAddress2 = new UserAddress();
    userAddress2.setAddress( buildTestAddress() );
    userAddress2.setAddressType( AddressType.lookup( "bus" ) );
    userAddress2.setIsPrimary( Boolean.FALSE );
    user.getUserAddresses().add( userAddress2 );

    assertTrue( user.getUserAddresses().size() == 2 );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    Set userAddresses = userService.getUserAddresses( user.getId() );

    assertTrue( userAddresses.size() == 2 );

  }

  public void testUpdateCharacteristicForUser()
  {
    String uniqueName = String.valueOf( Math.random() % 29930293 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    UserCharacteristic userCharacteristicUpdated = new UserCharacteristic();
    userCharacteristicUpdated.setCharacteristicValue( "RED" );
    userCharacteristicUpdated.setUserCharacteristicType( characteristic );
    userCharacteristicUpdated.setUser( user );

    user.getUserCharacteristics().add( userCharacteristic );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserCharacteristic" ).with( same( userCharacteristicUpdated ) ).will( returnValue( userCharacteristicUpdated ) );

    userService.updateCharacteristicForUser( user.getId(), userCharacteristicUpdated );

    mockUserDAO.verify();

  } // testupdateUserCharacteristic

  public void testDeleteEmailAddressForPerson()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setId( new Long( 1 ) );
    userEmailAddress.setEmailAddr( "test@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "home" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( Boolean.FALSE );
    user.getUserEmailAddresses().add( userEmailAddress );
    mockUserDAO.expects( once() ).method( "deleteEmailAddressForPerson" ).isVoid();

    userService.deleteEmailAddressForUser( user.getId(), userEmailAddress.getId() );

    mockUserDAO.verify();
  }

  public void testdeletePhoneNumberForUser()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserPhone userPhone = new UserPhone();
    userPhone.setId( new Long( 1 ) );
    userPhone.setPhoneNbr( "8681809143" );
    userPhone.setCountryPhoneCode( "us" );
    userPhone.setPhoneType( PhoneType.lookup( "home" ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    user.getUserPhones().add( userPhone );
    mockUserDAO.expects( once() ).method( "deletePhoneNumberForPerson" ).isVoid();

    userService.deletePhoneNumberForUser( user.getId(), userPhone.getId() );

    mockUserDAO.verify();
  }

  public void testdeleteAddressForUser()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserAddress updAddress = new UserAddress();
    updAddress.setAddress( buildTestAddress() );
    updAddress.setAddressType( AddressType.lookup( "hom" ) );
    updAddress.setIsPrimary( Boolean.TRUE );

    user.getUserAddresses().add( updAddress );
    mockUserDAO.expects( once() ).method( "deleteAddressForPerson" ).isVoid();

    userService.deleteAddressForUser( user.getId(), updAddress.getId() );

    mockUserDAO.verify();
  }

  public void testGetUserAddressById()
  {

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserAddress updAddress = new UserAddress();
    updAddress.setAddress( buildTestAddress() );
    updAddress.setAddressType( AddressType.lookup( "hom" ) );
    updAddress.setIsPrimary( Boolean.TRUE );
    user.getUserAddresses().add( updAddress );

    mockUserDAO.expects( once() ).method( "getUserAddressById" ).with( same( 1L ) ).will( returnValue( updAddress ) );
    UserAddress userAddress = userService.getUserAddressById( 1L );
    assertNotNull( userAddress );
    mockUserDAO.verify();

  }

  public void testupdateUserEmailAddress() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );

    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "test@test.com" );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateEmailAddressForPerson" ).with( same( userEmailAddress ) );
    userService.updateEmailAddressForUser( user.getId(), userEmailAddress );

    mockUserDAO.verify();
  }

  public void testupdatePhoneNumberForUser() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserPhone userPhoneHome = new UserPhone();
    userPhoneHome.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneHome.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneHome.setPhoneNbr( "9119119111" );
    userPhoneHome.setPhoneExt( "1234" );

    UserPhone userPhoneUpdated = new UserPhone();
    userPhoneUpdated.setPhoneType( PhoneType.lookup( "home" ) );
    userPhoneUpdated.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhoneUpdated.setPhoneNbr( "1112223333" );
    userPhoneUpdated.setPhoneExt( "1234" );

    user.getUserPhones().add( userPhoneHome );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserPhone" ).with( same( userPhoneUpdated ) ).will( returnValue( userPhoneUpdated ) );

    userService.updatePhoneNumberForUser( user.getId(), userPhoneUpdated );

    assertEquals( userPhoneUpdated.getPhoneType(), userPhoneHome.getPhoneType() );

  }

  public void testupdateAddressForUser() throws ServiceErrorException
  {
    Participant user = new Participant();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    // build participant
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    UserAddress origUserAddress = new UserAddress();
    origUserAddress.setAddress( buildTestAddress() );
    origUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    origUserAddress.setIsPrimary( Boolean.TRUE );
    origUserAddress.setUser( user );

    Set userAddressList = new LinkedHashSet();
    userAddressList.add( origUserAddress );
    participant.setUserAddresses( userAddressList );
    participant.setAwardBanqNumber( "" );

    // 1st address
    UserAddress userAddress1 = new UserAddress();
    userAddress1.setAddress( buildTestAddress() );
    userAddress1.setAddressType( AddressType.lookup( "hom" ) );
    userAddress1.setIsPrimary( Boolean.TRUE );
    userAddress1.setUser( user );

    // add the first address
    user.getUserAddresses().add( userAddress1 );

    // updated address
    UserAddress updatedUserAddress = new UserAddress();
    updatedUserAddress.setAddress( buildTestAddress2() );
    updatedUserAddress.setAddressType( AddressType.lookup( "hom" ) );
    updatedUserAddress.setIsPrimary( Boolean.TRUE );
    updatedUserAddress.setUser( user );

    mockUserDAO.expects( atLeastOnce() ).method( "getUserById" ).with( same( user.getId() ) ).will( onConsecutiveCalls( returnValue( user ), returnValue( user ) ) );
    mockParticipantDAO.expects( once() ).method( "getParticipantOverviewById" ).with( same( user.getId() ) ).will( returnValue( participant ) );
    mockUserDAO.expects( once() ).method( "updateUserAddress" ).with( same( updatedUserAddress ) ).will( returnValue( updatedUserAddress ) );

    userService.updateAddressForUser( user.getId(), updatedUserAddress );

    assertEquals( updatedUserAddress.getAddressType(), userAddress1.getAddressType() );
    assertEquals( updatedUserAddress.getAddress().getCity(), "Sometown" );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );
    // Get the specific user address
    UserAddress returnedUserAddress = userService.getUserAddress( user.getId(), updatedUserAddress.getAddressType().getCode() );

    assertEquals( returnedUserAddress, updatedUserAddress );

  }

  public void testUpdateUser() throws UniqueConstraintViolationException, ServiceErrorException
  {

    // Create the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    User dbUser = new User();
    dbUser.setId( new Long( 1 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // update the user

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( dbUser ) );

    // test the UserService.addUser
    userService.updateUser( user );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();
  }

  public void testDeleteCharacteristicForUser()
  {
    String uniqueName = String.valueOf( Math.random() % 29930293 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setId( 1L );
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    UserCharacteristic userCharacteristicUpdated = new UserCharacteristic();
    userCharacteristicUpdated.setCharacteristicValue( "RED" );
    userCharacteristicUpdated.setUserCharacteristicType( characteristic );
    userCharacteristicUpdated.setUser( user );

    user.getUserCharacteristics().add( userCharacteristic );

    mockUserDAO.expects( once() ).method( "deleteAttributeForPerson" ).isVoid();

    userService.deleteCharacteristicForUser( user.getId(), userCharacteristic.getId() );

    mockUserDAO.verify();

  }

  public void testGetUserCharacteristicById()
  {
    String uniqueName = String.valueOf( Math.random() % 29930297 );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setId( 1L );
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    mockUserDAO.expects( once() ).method( "getUserCharacteristicById" ).will( returnValue( userCharacteristic ) );
    userService.getUserCharacteristicById( userCharacteristic.getId(), user.getId() );

    mockUserDAO.verify();
  }

  public void testUpdateAttributeForUser()
  {
    String uniqueName = String.valueOf( Math.random() % 29930297 );
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setId( 1L );
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    mockUserDAO.expects( once() ).method( "getUserById" ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateUserCharacteristic" ).will( returnValue( userCharacteristic ) );

    userService.updateCharacteristicForUser( userCharacteristic.getId(), userCharacteristic );

    mockUserDAO.verify();

  }

  public void testGetUserCharacteristicsByUserId()
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setCharacteristicName( "testCharacteristic" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "BROWN" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user );

    List<UserCharacteristic> userCharList = new ArrayList<UserCharacteristic>();
    userCharList.add( userCharacteristic );
    user.getUserCharacteristics().add( userCharacteristic );

    mockUserDAO.expects( once() ).method( "getUserCharacteristicsByUserId" ).with( same( user.getId() ) ).will( returnValue( userCharList ) );

    List userCharacteristics = userService.getUserCharacteristicsByUserId( user.getId() );

    assertTrue( userCharacteristics.size() == 1 );
    mockUserDAO.verify();
  }
  
  public void testDeleteUser() throws UniqueConstraintViolationException, ServiceErrorException
  {

    // Create the test User.
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );
    // user.setStatusType( mockTrueStatusType ); // not sure about its business use

    User dbUser = new User();
    dbUser.setId( new Long( 1 ) );
    dbUser.setUserName( "ServiceTestUserName" );
    dbUser.setFirstName( "ServiceTestFirstName" );
    dbUser.setLastName( "ServiceTestLastName" );
    dbUser.setMasterUserId( new Long( 1 ) ); // not clear on its business use
    dbUser.setPassword( "ServiceTestPassword" );
    dbUser.setActive( Boolean.TRUE );
    // dbUser.setStatusType( mockTrueStatusType ); // not sure about its business use

    // update the user

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( same( user.getUserName() ) ).will( returnValue( dbUser ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) ).will( returnValue( dbUser ) );

    // test the UserService.addUser
    userService.deleteUser( user );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockUserDAO.verify();
  }

}
