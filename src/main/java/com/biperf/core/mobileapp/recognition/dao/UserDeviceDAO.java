
package com.biperf.core.mobileapp.recognition.dao;

import java.util.List;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;

public interface UserDeviceDAO extends DAO
{
  public UserDevice create( UserDevice userDevice );

  public int delete( Long userId, String registrationId );

  public int deleteAllDevicesFor( Long userId );

  public List<UserDevice> findUserDevicesFor( Long userId );

  public List<UserDevice> findUserDevicesForAll( List<Long> userIds );

  public UserDevice findUserDeviceByDeviceId( Long deviceId );

  public void deleteDeviceByUserIdAndDeviceId( Long userId, Long deviceId );

  public UserDevice updateUserDevice( UserDevice userDevice );

  public UserDevice findUserDeviceByRosterDeviceId( UUID rosterDeviceId );

  public Long findUserDeviceIdByRosterDeviceId( UUID rosterDeviceId );

}
