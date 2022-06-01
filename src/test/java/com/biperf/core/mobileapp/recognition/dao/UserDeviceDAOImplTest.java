
package com.biperf.core.mobileapp.recognition.dao;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.user.User;
import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;

public class UserDeviceDAOImplTest extends BaseDAOTest
{

  public void testCreate()
  {
    User user = getUserDAO().getUserById( new Long( 1 ) );

    UserDevice userDevice = new UserDevice( user, DeviceType.ANDROID, "1234", true );

    userDevice = getUserDeviceDAO().create( userDevice );

    assertNotNull( userDevice.getId() );
    assertNotNull( userDevice.getAuditCreateInfo() );
  }

  public void testCreate_NullParameter()
  {
    UserDevice userDevice = getUserDeviceDAO().create( null );
    assertNull( userDevice );
  }

  public void testDelete()
  {
    final Long USER_ID = new Long( 1 );
    final String REGISRATION_ID = "563094grgtn485gnvvastadsdf43ndvd3";

    int deleted = getUserDeviceDAO().delete( USER_ID, REGISRATION_ID );
    assertEquals( 0, deleted );
  }

  public void testFindUserDevicesFor()
  {
    final Long USER_ID = new Long( 1 );
    List<UserDevice> devices = getUserDeviceDAO().findUserDevicesFor( USER_ID );

    assertNotNull( devices );
    assertEquals( 0, devices.size() );
  }

  private UserDeviceDAO getUserDeviceDAO()
  {
    return (UserDeviceDAO)getDAO( "userDeviceDAO" );
  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)getDAO( "userDAO" );
  }
}
