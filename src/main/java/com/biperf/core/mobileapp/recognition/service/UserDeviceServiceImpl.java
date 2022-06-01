
package com.biperf.core.mobileapp.recognition.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.mobileapp.recognition.dao.UserDeviceDAO;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;

public class UserDeviceServiceImpl implements UserDeviceService
{

  private UserDeviceDAO userDeviceDao;

  @Override
  public Map<Long, Set<UserDevice>> getUniqueUserDevicesForAll( List<Long> userIds )
  {
    Map<Long, Set<UserDevice>> deviceMap = new HashMap<>();
    List<UserDevice> allDevices = userDeviceDao.findUserDevicesForAll( userIds );

    if ( allDevices == null || allDevices.isEmpty() )
    {
      return deviceMap;
    }

    Iterator<UserDevice> allDeviceIterator = allDevices.iterator();
    while ( allDeviceIterator.hasNext() )
    {
      UserDevice userDevice = allDeviceIterator.next();

      // Input userId may not exist in the map. Only add it once we know it is a result.
      if ( !deviceMap.containsKey( userDevice.getUser().getId() ) )
      {
        Set<UserDevice> deviceSet = new HashSet<>();
        deviceSet.add( userDevice );
        deviceMap.put( userDevice.getUser().getId(), deviceSet );
      }
      else
      {
        deviceMap.get( userDevice.getUser().getId() ).add( userDevice );
      }
    }

    return deviceMap;
  }

  public List<UserDevice> findUserDevicedForUser( Long userId )
  {
    List<UserDevice> allDevices = userDeviceDao.findUserDevicesFor( userId );
    return allDevices;
  }

  public void setUserDeviceDao( UserDeviceDAO userDeviceDao )
  {
    this.userDeviceDao = userDeviceDao;
  }

  public UserDevice createUserDevice( UserDevice userNotifcation )
  {
    return userDeviceDao.create( userNotifcation );
  }

  public void deleteDeviceByUserIdAndDeviceId( Long userId, Long deviceId )
  {
    userDeviceDao.deleteDeviceByUserIdAndDeviceId( userId, deviceId );
  }

  @Override
  public UserDevice findUserDeviceByDeviceId( Long deviceId )
  {
    return userDeviceDao.findUserDeviceByDeviceId( deviceId );
  }

  public UserDevice updateUserDevice( UserDevice userDevice )
  {
    return userDeviceDao.updateUserDevice( userDevice );
  }

  @Override
  public UserDevice findUserDeviceByRosterDeviceId( UUID rosterDeviceId )
  {
    return userDeviceDao.findUserDeviceByRosterDeviceId( rosterDeviceId );
  }

  @Override
  public Long findUserDeviceIdByRosterDeviceId( UUID rosterDeviceId )
  {
    return userDeviceDao.findUserDeviceIdByRosterDeviceId( rosterDeviceId );
  }
}
