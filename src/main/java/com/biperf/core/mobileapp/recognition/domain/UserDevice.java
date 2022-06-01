
package com.biperf.core.mobileapp.recognition.domain;

import java.util.Objects;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

public class UserDevice extends BaseDomain
{
  private User user;
  private DeviceType deviceType;
  private String registrationId;
  private int notificationCount;
  private boolean debug;
  private UUID rosterDeviceId;

  public UserDevice( User user, DeviceType deviceType, String registrationId, boolean debug )
  {
    this.user = user;
    this.deviceType = deviceType;
    this.registrationId = registrationId;
    this.debug = debug;
  }

  private UserDevice()
  {
  }

  public User getUser()
  {
    return user;
  }

  private void setUser( User user )
  {
    this.user = user;
  }

  public DeviceType getDeviceType()
  {
    return deviceType;
  }

  public void setDeviceType( DeviceType deviceType )
  {
    this.deviceType = deviceType;
  }

  public String getRegistrationId()
  {
    return registrationId;
  }

  public int getNotificationCount()
  {
    return notificationCount;
  }

  protected void setNotificationCount( int notificationCount )
  {
    this.notificationCount = notificationCount;
  }

  public void resetNotificationCount()
  {
    this.notificationCount = 0;
  }

  public void setRegistrationId( String registrationId )
  {
    this.registrationId = registrationId;
  }

  public boolean isDebug()
  {
    return debug;
  }

  private void setDebug( boolean debug )
  {
    this.debug = debug;
  }
  
  public UUID getRosterDeviceId()
  {
    return rosterDeviceId;
  }

  public void setRosterDeviceId( UUID rosterDeviceId )
  {
    this.rosterDeviceId = rosterDeviceId;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode( this.user.getId() );
    hash = 89 * hash + Objects.hashCode( this.deviceType );
    hash = 89 * hash + Objects.hashCode( this.registrationId );
    return hash;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    final UserDevice other = (UserDevice)obj;
    if ( !Objects.equals( this.user.getId(), other.user.getId() ) )
    {
      return false;
    }
    if ( this.deviceType != other.deviceType )
    {
      return false;
    }
    if ( !Objects.equals( this.registrationId, other.registrationId ) )
    {
      return false;
    }
    return true;
  }

}
