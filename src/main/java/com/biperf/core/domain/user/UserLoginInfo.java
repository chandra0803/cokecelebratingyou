
package com.biperf.core.domain.user;

import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;

public class UserLoginInfo extends BaseDomain
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Long userId;

  private Timestamp userLoggedInTime;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Timestamp getUserLoggedInTime()
  {
    return userLoggedInTime;
  }

  public void setUserLoggedInTime( Timestamp userLoggedInTime )
  {
    this.userLoggedInTime = userLoggedInTime;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    else
    {
      if ( object instanceof UserLoginInfo )
      {
        UserLoginInfo castObj = (UserLoginInfo)object;
        if ( userId.equals( castObj.getUserId() ) && userLoggedInTime.equals( castObj.getUserLoggedInTime() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public int hashCode()
  {
    return 0;
  }

}
