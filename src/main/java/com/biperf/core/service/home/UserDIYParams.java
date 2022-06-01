
package com.biperf.core.service.home;

public class UserDIYParams
{
  private Long userId;
  private boolean isManager;
  private boolean isOwner;
  private boolean isDisplayManagerSendAlert;

  public UserDIYParams( Long userId, boolean isManager, boolean isOwner, boolean isDisplayManagerSendAlert )
  {
    this.userId = userId;
    this.isManager = isManager;
    this.isOwner = isOwner;
    this.isDisplayManagerSendAlert = isDisplayManagerSendAlert;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public boolean isManager()
  {
    return isManager;
  }

  public void setManager( boolean isManager )
  {
    this.isManager = isManager;
  }

  public boolean isOwner()
  {
    return isOwner;
  }

  public void setOwner( boolean isOwner )
  {
    this.isOwner = isOwner;
  }

  public boolean isDisplayManagerSendAlert()
  {
    return isDisplayManagerSendAlert;
  }

  public void setDisplayManagerSendAlert( boolean isDisplayManagerSendAlert )
  {
    this.isDisplayManagerSendAlert = isDisplayManagerSendAlert;
  }
}
