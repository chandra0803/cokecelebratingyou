
package com.biperf.core.mobileapp.recognition.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class UserDeviceDAOImpl extends BaseDAO implements UserDeviceDAO
{
  @Override
  public UserDevice create( UserDevice userDevice )
  {
    if ( userDevice == null )
    {
      return null;
    }

    // first delete all existing UserDevice objects that match the existing one
    List<UserDevice> existingDevices = findUserDevicesFor( userDevice.getUser().getId() );
    if ( existingDevices != null )
    {
      for ( UserDevice existingUserDevice : existingDevices )
      {
        if ( existingUserDevice.equals( userDevice ) )
        {
          // delete it
          getSession().delete( existingUserDevice );
        }
      }
    }

    // create the new one
    UserDevice device = (UserDevice)HibernateUtil.saveOrUpdateOrShallowMerge( userDevice );

    return device;
  }

  @Override
  public int deleteAllDevicesFor( Long userId )
  {
    int count = 0;

    // first delete all existing UserDevice objects that match the existing one
    List<UserDevice> existingDevices = findUserDevicesFor( userId );
    if ( existingDevices != null )
    {
      for ( UserDevice existingUserDevice : existingDevices )
      {
        // delete it
        getSession().delete( existingUserDevice );
        count++;
      }
    }

    return count;
  }

  @Override
  public int delete( Long userId, String registrationId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.mobileapp.recognition.domain.UserDevice.Delete" );
    query.setLong( "userId", userId );
    query.setString( "registrationId", registrationId );

    return query.executeUpdate();
  }

  @Override
  public List<UserDevice> findUserDevicesFor( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.mobileapp.recognition.domain.UserDevice.FindByUserId" );
    query.setLong( "userId", userId.longValue() );

    return query.list();
  }

  @Override
  public List<UserDevice> findUserDevicesForAll( List<Long> userIds )
  {
    List<UserDevice> devices = new LinkedList<>();
    Session session = HibernateSessionManager.getSession();

    // Fun fact - can only send a list of 1,000 elements to oracle at a time
    final int SLICE_SIZE = 1000;
    int fromIndex = 0;
    int toIndex = SLICE_SIZE;
    while ( fromIndex < userIds.size() )
    {
      if ( toIndex > userIds.size() )
      {
        toIndex = userIds.size();
      }

      Criteria criteria = session.createCriteria( UserDevice.class, "device" );
      criteria.createAlias( "device.user", "user" ).add( Restrictions.in( "user.id", userIds.subList( fromIndex, toIndex ) ) );
      devices.addAll( criteria.list() );

      fromIndex = toIndex;
      toIndex += SLICE_SIZE;
    }

    return devices;
  }

  @Override
  public UserDevice findUserDeviceByDeviceId( Long deviceId )
  {
    Session session = HibernateSessionManager.getSession();
    UserDevice device = (UserDevice)session.get( UserDevice.class, deviceId );

    return device;
  }

  public void deleteDeviceByUserIdAndDeviceId( Long userId, Long deviceId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( UserDevice.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "id", deviceId ) );
    UserDevice userDevice = (UserDevice)criteria.uniqueResult();
    getSession().delete( userDevice );
    getSession().flush();
  }

  public UserDevice updateUserDevice( UserDevice userDevice )
  {
    Session session = HibernateSessionManager.getSession();
    session.update( userDevice );
    return userDevice;
  }

  @Override
  public UserDevice findUserDeviceByRosterDeviceId( UUID rosterDeviceId )
  {
    Criteria searchCriteria = getSession().createCriteria( UserDevice.class );
    searchCriteria.add( Restrictions.eq( "rosterDeviceId", rosterDeviceId ) );
    return (UserDevice)searchCriteria.uniqueResult();

  }

  @Override
  public Long findUserDeviceIdByRosterDeviceId( UUID rosterDeviceId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.mobileapp.recognition.domain.UserDevice.findByUserId" );
    query.setString( "rosterDeviceId", rosterDeviceId.toString() );
    return (Long)query.uniqueResult();

  }
}
