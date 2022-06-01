
package com.biperf.core.mobileapp.recognition.service;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.domain.user.User;
import com.biperf.core.mobileapp.recognition.dao.UserDeviceDAO;
import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.service.BaseServiceTest;

public class UserDeviceServiceImplTest extends BaseServiceTest
{

  /** userServiceImplementation */
  private UserDeviceServiceImpl userService = new UserDeviceServiceImpl();
  /** mocks */
  private Mock mockUserDeviceDAO = null;

  public UserDeviceServiceImplTest( String test )
  {
    super( test );
  }

  protected void setUp() throws Exception
  {
    super.setUp();

    mockUserDeviceDAO = new Mock( UserDeviceDAO.class );
    userService.setUserDeviceDao( (UserDeviceDAO)mockUserDeviceDAO.proxy() );
  }

  public void testFindUserDevicedForUser()
  {

    User user = buildUser();
    List<UserDevice> devices = new ArrayList<UserDevice>();
    devices.add( new UserDevice( user, DeviceType.ANDROID, "abdxcvsdfsadfsadfsaddfsdf&@$^@$*@#$^@#*$@#$werwerwer", Boolean.FALSE ) );
    mockUserDeviceDAO.expects( once() ).method( "findUserDevicesFor" ).with( same( user.getId() ) ).will( returnValue( devices ) );
    userService.findUserDevicedForUser( user.getId() );
    assertNotNull( devices );
    mockUserDeviceDAO.verify();
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

  public void testCreateUserDevice()
  {
    User user = buildUser();
    UserDevice userDevice = new UserDevice( user, DeviceType.ANDROID, "abdxcvsdfsadfsadfsaddfsdf&@$^@$*@#$^@#*$@#$werwerwer", Boolean.FALSE );
    mockUserDeviceDAO.expects( once() ).method( "create" ).will( returnValue( userDevice ) );
    UserDevice device = userService.createUserDevice( userDevice );
    assertNotNull( device );
    mockUserDeviceDAO.verify();
  }

  public void testDeleteDeviceByUserIdAndDeviceId()
  {
    User user = buildUser();

    UserDevice userDevice = new UserDevice( user, DeviceType.ANDROID, "abdxcvsdfsadfsadfsaddfsdf&@$^@$*@#$^@#*$@#$werwerwer", Boolean.FALSE );
    userDevice.setId( 200L );
    mockUserDeviceDAO.expects( once() ).method( "deleteDeviceByUserIdAndDeviceId" ).isVoid();
    userService.deleteDeviceByUserIdAndDeviceId( user.getId(), userDevice.getId() );
    mockUserDeviceDAO.verify();
  }

  public void testFindUserDeviceByDeviceId()
  {

    User user = buildUser();
    UserDevice ud = new UserDevice( user, DeviceType.ANDROID, "abdxcvsdfsadfsadfsaddfsdf&@$^@$*@#$^@#*$@#$werwerwer", Boolean.FALSE );
    ud.setId( 1L );
    mockUserDeviceDAO.expects( once() ).method( "findUserDeviceByDeviceId" ).with( same( user.getId() ) ).will( returnValue( ud ) );
    UserDevice device = userService.findUserDeviceByDeviceId( ud.getId() );
    assertNotNull( device );
    mockUserDeviceDAO.verify();
  }

  public void testUpdateUserDevice()
  {
    User user = buildUser();
    UserDevice userDevice = new UserDevice( user, DeviceType.ANDROID, "abdxcvsdfsadfsadfsaddfsdf&@$^@$*@#$^@#*$@#$werwerwer", Boolean.FALSE );
    userDevice.setDeviceType( DeviceType.IOS );
    mockUserDeviceDAO.expects( once() ).method( "updateUserDevice" ).will( returnValue( userDevice ) );
    UserDevice device = userService.updateUserDevice( userDevice );
    assertNotNull( device );
    mockUserDeviceDAO.verify();
  }
}
