
package com.biperf.core.mobileapp.recognition.service;

import static com.biperf.core.mobileapp.recognition.domain.DeviceType.ANDROID;
import static com.biperf.core.mobileapp.recognition.domain.DeviceType.IOS;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.google.android.gcm.server.Message;
import com.biperf.core.google.android.gcm.server.Result;
import com.biperf.core.mobileapp.recognition.dao.UserDeviceDAO;
import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.Environment;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.notification.Payload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;

public class MobileNotificationServiceImplTest
{
  private MobileNotificationServiceImpl service;

  private UserDeviceDAO mockUserDeviceDao;
  private UserDAO mockUserDao;
  private ClaimDAO mockClaimDao;
  private GoogleCloudMessagingService mockGcmService;
  private SystemVariableService mockSystemVariableService;
  private CMAssetService mockCmAssetService;
  private NodeService mockNodeService;

  @Before
  public void setUp()
  {
    PickListItem.setPickListFactory( new MockPickListFactory() );

    service = new MobileNotificationServiceImpl();

    mockUserDeviceDao = createMock( UserDeviceDAO.class );
    service.setUserDeviceDao( mockUserDeviceDao );

    mockUserDao = createMock( UserDAO.class );
    service.setUserDao( mockUserDao );

    mockClaimDao = createMock( ClaimDAO.class );
    service.setClaimDao( mockClaimDao );

    mockGcmService = createMock( GoogleCloudMessagingService.class );
    service.setGcmService( mockGcmService );

    mockSystemVariableService = createMock( SystemVariableService.class );
    service.setSystemVariableService( mockSystemVariableService );

    mockCmAssetService = createMock( CMAssetService.class );
    service.setCmAssetService( mockCmAssetService );

    mockNodeService = createMock( NodeService.class );
    service.setNodeService( mockNodeService );

  }

  @Test
  public void testCreateUserDeviceFor()
  {
    final Long USER_ID = new Long( 23452 );
    final DeviceType DEVICE_TYPE = DeviceType.ANDROID;
    final String REG_ID = "asdf34rtsdfer34r43r3rfqsdvbt";
    final int NOTIFICATION_COUNT = 8;
    final boolean DEBUG = true;

    // set up the call to mockUserDao
    User user = new User();
    user.setId( USER_ID );
    expect( mockUserDao.getUserById( USER_ID ) ).andReturn( user );

    // set up the UserDevice that is created by the call to mockUserDeviceDao
    TestableUserDevice mockCreated = new TestableUserDevice( user, DEVICE_TYPE, REG_ID, DEBUG );
    mockCreated.setNotificationCount( NOTIFICATION_COUNT );

    // set up the call to mockUserDeviceDao
    expect( mockUserDeviceDao.create( (UserDevice)anyObject() ) ).andReturn( mockCreated );

    // replay the mocks....
    replayAll();

    // run the test!
    UserDevice created = service.createUserDeviceFor( USER_ID, DEVICE_TYPE, REG_ID, DEBUG );

    // verify...
    verifyAll();

    // asserts
    assertNotNull( created );
    assertEquals( USER_ID, created.getUser().getId() );
    assertEquals( DEVICE_TYPE, created.getDeviceType() );
    assertEquals( REG_ID, created.getRegistrationId() );
    assertEquals( DEBUG, created.isDebug() );
    assertEquals( 0, created.getNotificationCount() );
  }

  @Test
  public void testDeleteUserDevice()
  {
    final Long USER_ID = new Long( 65542 );
    final String REGISTRATION_ID = "68gno39ruwmcwoir3jfsoiu";

    // set up the call to mockUserDeviceDao
    final int DELETED_COUNT = 0;
    expect( mockUserDeviceDao.delete( USER_ID, REGISTRATION_ID ) ).andReturn( DELETED_COUNT );

    // replay the mocks....
    replayAll();

    // do the test!
    int deletedCount = service.deleteUserDevice( USER_ID, REGISTRATION_ID );

    // verify the mocks
    verifyAll();

    // asserts
    assertEquals( DELETED_COUNT, deletedCount );
  }

  @Test
  public void testOnRecognitionClaimSent() throws IOException, CommunicationException, CommunicationException, CommunicationException, CommunicationException, KeystoreException, Exception
  {
    // build the Node for the recipient and the recipient's manager
    final Long NODE_ID = new Long( 3867 );
    Node node = new Node();
    node.setId( NODE_ID );

    // build the manager of the recipient
    final Long MANAGER_USER_ID = new Long( 679490358 );
    Participant manager = new Participant();
    manager.setId( MANAGER_USER_ID );
    manager.setActive( Boolean.TRUE );

    // build the manager's UserNode
    UserNode managerUserNode = new UserNode();
    managerUserNode.setIsPrimary( Boolean.TRUE );
    managerUserNode.setUser( manager );
    managerUserNode.setNode( node );
    managerUserNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );

    // add the manager's UserNode to the manager
    manager.addUserNode( managerUserNode );

    // add the manager's UserNode to the Node
    node.addUserNode( managerUserNode );

    // build the recipient
    final Long RECIPIENT_USER_ID = new Long( 345345 );
    Participant recipient = new Participant();
    recipient.setId( RECIPIENT_USER_ID );
    recipient.setActive( Boolean.TRUE );
    recipient.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );

    // build the recipient's UserNode
    UserNode recipientUserNode = new UserNode();
    recipientUserNode.setIsPrimary( Boolean.TRUE );
    recipientUserNode.setUser( recipient );
    recipientUserNode.setNode( node );
    recipientUserNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );

    // add the recipient's UserNode to the recipient
    recipient.addUserNode( recipientUserNode );

    // add the recipient's UserNode to the Node
    node.addUserNode( recipientUserNode );

    // build the RecognitionClaim
    final Long RECOGNITION_CLAIM_ID = new Long( 311485 );
    RecognitionClaim recognition = new RecognitionClaim();
    recognition.setId( NODE_ID );

    // build the ClaimRecipient
    final Long CLAIM_RECIPIENT_ID = new Long( 25653 );
    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setId( CLAIM_RECIPIENT_ID );
    claimRecipient.setClaim( recognition );
    claimRecipient.setRecipient( recipient );

    // add the ClaimRecipient to the RecognitionClaim
    recognition.addClaimRecipient( claimRecipient );

    // create the RecognitionPromotion
    RecognitionPromotion rp = new RecognitionPromotion();
    rp.setAllowPublicRecognitionPoints( true );

    // add the RecognitionPromotion to the RecognitionClaim
    recognition.setPromotion( rp );

    // set up the call to the mockClaimDao to get the RecognitionClaim
    expect( mockClaimDao.getClaimById( RECOGNITION_CLAIM_ID ) ).andReturn( recognition );

    // set up two devices for the recipient: one Android, one iOS
    final Long RECIPIENT_ANDROID_USER_DEVICE_ID = new Long( 3574 );
    final String RECIPIENT_ANDROID_REG_ID = "recipient-android";
    UserDevice recipientAndroid = new UserDevice( recipient, ANDROID, RECIPIENT_ANDROID_REG_ID, false );
    recipientAndroid.setId( RECIPIENT_ANDROID_USER_DEVICE_ID );

    final Long RECIPIENT_IOS_USER_DEVICE_ID = new Long( 1683574 );
    final String RECIPIENT_IOS_REG_ID = "recipient-ios";
    UserDevice recipientIOS = new UserDevice( recipient, IOS, RECIPIENT_IOS_REG_ID, false );
    recipientIOS.setId( RECIPIENT_IOS_USER_DEVICE_ID );

    List<UserDevice> recipientDevices = new ArrayList();
    recipientDevices.add( recipientAndroid );
    recipientDevices.add( recipientIOS );

    // set up the call to mockUserDeviceDao to get the recipient's devices
    expect( mockUserDeviceDao.findUserDevicesFor( RECIPIENT_USER_ID ) ).andReturn( recipientDevices );

    // set up two devices for the manager: one Android, one iOS
    final Long MANAGER_ANDROID_USER_DEVICE_ID = new Long( 3585374 );
    final String MANAGER_ANDROID_REG_ID = "recipient-android";
    UserDevice managerAndroid = new UserDevice( recipient, ANDROID, MANAGER_ANDROID_REG_ID, false );
    managerAndroid.setId( MANAGER_ANDROID_USER_DEVICE_ID );

    final Long MANAGER_IOS_USER_DEVICE_ID = new Long( 135687374 );
    final String MANAGER_IOS_REG_ID = "recipient-ios";
    UserDevice managerIOS = new UserDevice( recipient, IOS, MANAGER_IOS_REG_ID, false );
    managerIOS.setId( MANAGER_IOS_USER_DEVICE_ID );

    List<UserDevice> managerDevices = new ArrayList();
    managerDevices.add( managerAndroid );
    managerDevices.add( managerIOS );

    // set up the call to mockUserDeviceDao to get the manager's devices
    expect( mockUserDeviceDao.findUserDevicesFor( MANAGER_USER_ID ) ).andReturn( managerDevices );

    // set up the calls to the mockSystemVariableService (called twice, once for recipient, once for
    // manager)
    PropertySetItem psi = new PropertySetItem();
    psi.setStringVal( LanguageType.ENGLISH );
    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ) ).andReturn( psi );
    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ) ).andReturn( psi );

    // set up the calls to mockCmAssetService for the message text
    final String RECIPIENT_MESSAGE_ASSET = "recognition.detail";
    final String RECIPIENT_MESSAGE_KEY = "MOBILE_NOTIFICATION_RECIPIENT";
    final String MANAGER_MESSAGE_ASSET = "recognition.detail";
    final String MANAGER_MESSAGE_KEY = "MOBILE_NOTIFICATION_MANAGER";
    final Locale locale = Locale.US;
    expect( mockCmAssetService.getString( RECIPIENT_MESSAGE_ASSET, RECIPIENT_MESSAGE_KEY, locale, true ) ).andReturn( "{0} was recognized" );
    expect( mockCmAssetService.getString( MANAGER_MESSAGE_ASSET, MANAGER_MESSAGE_KEY, locale, true ) ).andReturn( "{0} was recognized" );

    // set up the call to mockGcmService for actually sending the message to the Android device
    // for the recipient
    Result result = new Result.Builder().build();
    expect( mockGcmService.sendNoRetry( (Message)anyObject(), eq( recipientAndroid ) ) ).andReturn( result );

    // set up the call to mockGcmService for actually sending the message to the Android device
    // for the manager
    result = new Result.Builder().build();
    expect( mockGcmService.sendNoRetry( (Message)anyObject(), eq( managerAndroid ) ) ).andReturn( result );

    // set up the system properties that are read by the javapns library
    System.setProperty( Environment.PROXY_HOST, "8080" );
    System.setProperty( Environment.PROXY_PORT, Environment.DEFAULT_PROXY_PORT );

    // set up the call to the mockIosNotificationService for actually sending the message to the iOS
    // device
    // for the recipient
    PushedNotifications recipientIOSNotifications = new PushedNotifications();
    PushedNotification recipientIOSNotification = new MockSuccessfulPushedNotification( null, null, null );
    recipientIOSNotifications.add( recipientIOSNotification );

    // set up the call to the mockIosNotificationService for actually sending the message to the iOS
    // device
    // for the manager
    PushedNotifications managerIOSNotifications = new PushedNotifications();
    PushedNotification managerIOSNotification = new MockSuccessfulPushedNotification( null, null, null );
    managerIOSNotifications.add( managerIOSNotification );

    // set up the call that gets the recipient's manager(s)
    final Set<User> managerSet = new HashSet<>();
    managerSet.add( manager );
    expect( mockNodeService.getNodeManagersForUser( anyObject(), anyObject() ) ).andReturn( managerSet );

    // replay the mocks....
    replayAll();

    // do the test!!!
    service.onRecognitionClaimSent( RECOGNITION_CLAIM_ID );

    // verify the mocks...
    verifyAll();
  }

  @Test
  public void testHandleGoogleCloudMessagingResult_Success()
  {
    // create the UserDevice
    final String REG_ID = "regId";
    final Long USER_ID = new Long( 37567772 );
    User user = new User();
    user.setId( USER_ID );
    UserDevice userDevice = new UserDevice( user, ANDROID, REG_ID, false );

    // create the successful result
    final String MESSAGE_ID = "messageId";
    Result result = new Result.Builder().messageId( MESSAGE_ID ).build();

    final Long RECOGNITION_ID = new Long( 6845868 );
    RecipientRecognitionNotification notification = new RecipientRecognitionNotification( RECOGNITION_ID, LandingScreen.RECOGNITION_DETAIL, true, true, "hello" );

    // replay the mocks...
    replayAll();

    // run the test!
    service.handleGoogleCloudMessagingResult( notification, userDevice, result );

    // verify the mocks...
    verifyAll();

    // asserts
    assertEquals( REG_ID, userDevice.getRegistrationId() );
  }

  @Test
  public void testHandleGoogleCloudMessagingResult_NewCanonicalRegistrationId()
  {
    // create the UserDevice
    final String REG_ID = "regId";
    final Long USER_ID = new Long( 37567772 );
    User user = new User();
    user.setId( USER_ID );
    UserDevice userDevice = new UserDevice( user, ANDROID, REG_ID, false );

    // create the successful result
    final String MESSAGE_ID = "messageId";
    final String CANONICAL_REG_ID = "canonicalRegistrationId";
    Result result = new Result.Builder().messageId( MESSAGE_ID ).canonicalRegistrationId( CANONICAL_REG_ID ).build();

    final Long RECOGNITION_ID = new Long( 6845868 );
    RecipientRecognitionNotification notification = new RecipientRecognitionNotification( RECOGNITION_ID, LandingScreen.RECOGNITION_DETAIL, true, true, "hello" );

    // replay the mocks...
    replayAll();

    // run the test!
    service.handleGoogleCloudMessagingResult( notification, userDevice, result );

    // verify the mocks...
    verifyAll();

    // asserts
    assertEquals( CANONICAL_REG_ID, userDevice.getRegistrationId() );
  }

  @Test
  public void testHandleGoogleCloudMessagingResult_Fail()
  {
    // create the User
    final Long USER_ID = new Long( 780376 );
    User user = new User();
    user.setId( USER_ID );

    // create the UserDevice
    final String REG_ID = "regId";
    UserDevice userDevice = new UserDevice( user, ANDROID, REG_ID, false );

    // create the successful result
    final String ERROR_CODE = "someErrorCode";
    Result result = new Result.Builder().errorCode( ERROR_CODE ).build();

    final Long RECOGNITION_ID = new Long( 6845868 );
    RecipientRecognitionNotification notification = new RecipientRecognitionNotification( RECOGNITION_ID, LandingScreen.RECOGNITION_DETAIL, true, true, "hello" );

    // replay the mocks...
    replayAll();

    // run the test!
    service.handleGoogleCloudMessagingResult( notification, userDevice, result );

    // verify the mocks...
    verifyAll();
  }

  public void test_filterDuplicates()
  {
    final Long USER_ID = new Long( 2677438 );
    final String ANDROID_DEVICE_ID = "androidDeviceId";
    final String IOS_DEVICE_ID = "iOSDeviceId";

    User user;
    UserDevice userDevice;

    List<UserDevice> devices = new ArrayList<>();

    // create the two identical Android UserDevice instances
    user = new User();
    user.setId( USER_ID );
    userDevice = new UserDevice( user, ANDROID, ANDROID_DEVICE_ID, false );
    devices.add( userDevice );

    user = new User();
    user.setId( USER_ID );
    userDevice = new UserDevice( user, ANDROID, ANDROID_DEVICE_ID, false );
    devices.add( userDevice );

    // create the two identical iOS userDevice instances
    user = new User();
    user.setId( USER_ID );
    userDevice = new UserDevice( user, IOS, IOS_DEVICE_ID, false );
    devices.add( userDevice );

    user = new User();
    user.setId( USER_ID );
    userDevice = new UserDevice( user, IOS, IOS_DEVICE_ID, false );
    devices.add( userDevice );

    // do the test!!!
    Set<UserDevice> filtered = service.filterDuplicates( devices );

    // asserts
    assertNotNull( filtered );
    assertEquals( 2, filtered.size() );
  }

  private static class TestableUserDevice extends UserDevice
  {
    public TestableUserDevice( User user, DeviceType deviceType, String registrationId, boolean debug )
    {
      super( user, deviceType, registrationId, debug );
    }

    @Override
    public void setNotificationCount( int notificationCount )
    {
      super.setNotificationCount( notificationCount );
    }
  }

  private static class MockSuccessfulPushedNotification extends PushedNotification
  {
    public MockSuccessfulPushedNotification( Device device, Payload payload, Exception exception )
    {
      super( device, payload, exception );
    }

    @Override
    public boolean isSuccessful()
    {
      return true;
    }
  }

  public void replayAll()
  {
    replay( mockUserDeviceDao, mockUserDao, mockClaimDao, mockGcmService, mockSystemVariableService, mockCmAssetService, mockNodeService );
  }

  public void verifyAll()
  {
    verify( mockUserDeviceDao, mockUserDao, mockClaimDao, mockGcmService, mockSystemVariableService, mockCmAssetService, mockNodeService );
  }
}
