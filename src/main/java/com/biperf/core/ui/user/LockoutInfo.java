
package com.biperf.core.ui.user;

public class LockoutInfo
{
  private boolean isHardLocked;

  public boolean isHardLocked()
  {
    return isHardLocked;
  }

  public void setHardLocked( boolean isHardLocked )
  {
    this.isHardLocked = isHardLocked;
  }

  public boolean isSoftLocked()
  {
    return isSoftLocked;
  }

  public void setSoftLocked( boolean isSoftLocked )
  {
    this.isSoftLocked = isSoftLocked;
  }

  public void setLockedForBoth( boolean isLockedForBoth )
  {
    this.isLockedForBoth = isLockedForBoth;
  }

  private boolean isSoftLocked;
  private boolean isLockedForBoth;

  public boolean isLockedForBoth()
  {
    return isLockedForBoth;
  }

}
