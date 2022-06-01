
package com.biperf.core.mobileapp.recognition.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.service.SAO;

public interface UserDeviceService extends SAO
{

  public static final String BEAN_NAME = "userDeviceService";

  /**
   * Returns a map from User ID to a set of devices for that user.
   * A user ID from the input parameter may not be represented in the map.
   * The map will not have null values (i.e. if there is a user ID in the map, the set of devices will not be null)
   */
  public Map<Long, Set<UserDevice>> getUniqueUserDevicesForAll( List<Long> userIds );

  public List<UserDevice> findUserDevicedForUser( Long userId );

  public UserDevice createUserDevice( UserDevice userNotifcation );

  public void deleteDeviceByUserIdAndDeviceId( Long userId, Long deviceId );

  public UserDevice findUserDeviceByDeviceId( Long deviceId );

  public UserDevice updateUserDevice( UserDevice userDevice );

  public UserDevice findUserDeviceByRosterDeviceId( UUID rosterDeviceId );

  public Long findUserDeviceIdByRosterDeviceId( UUID rosterDeviceId );

}
