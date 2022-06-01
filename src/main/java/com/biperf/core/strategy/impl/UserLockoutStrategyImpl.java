/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/impl/UserLockoutStrategyImpl.java,v $
 */

package com.biperf.core.strategy.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.UserLockoutStrategy;
import com.biperf.core.ui.user.LockoutInfo;

/**
 * UserLockoutStrategyImpl is based on the number of consecutive failed attempts by a user. If count
 * of cosecutive failed attempts exceeds a predefined number (a system propert) then user is
 * considered locked.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sharma</td>
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserLockoutStrategyImpl extends BaseStrategy implements UserLockoutStrategy
{
  /**
   * System variable name that specifies the login failure count for User Lockout
   */
  public static final String LOCKOUT_FAILURE_COUNT = SystemVariableService.LOCKOUT_FAILURE_COUNT;
  private static final String CM_KEY_PREFIX = "user.profile.";

  /**
   * Handles the Authentication for the LockoutStrategy
   * 
   * @param user
   * @param isAuthenticationValid
   */
  @Override
  public void handleAuthentication( User user, boolean isAuthenticationValid )
  {
    if ( user == null )
    {
      return;
    }

    if ( isAuthenticationValid )
    {
      handleSuccessfulAuthentication( user );
    }
    else
    {
      handleFailedAuthentication( user );
    }
  }

  /**
   * Increase the login failure count for the user
   * 
   * @param user
   */
  private void handleFailedAuthentication( User user )
  {
    // increase the failed count of the user
    int currentFailureCount = 0;
    if ( user.getLoginFailuresCount() != null )
    {
      currentFailureCount = user.getLoginFailuresCount().intValue();
    }
    boolean locked = ( currentFailureCount + 1 ) >= getSystemVariableService().getPropertyByName( LOCKOUT_FAILURE_COUNT ).getIntVal();
    if ( locked && user.getLockTimeoutExpireDate() == null )
    {
      user.setLockTimeoutExpireDate( buildLockTimeout() );
    }
    user.setLoginFailuresCount( new Integer( currentFailureCount + 1 ) );
  }

  /**
   * Reset the login failure count for the user, to zero
   * 
   * @param user
   */
  private void handleSuccessfulAuthentication( User user )
  {
    clearUserLockout( user );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.strategy.UserLockoutStrategy#isUserLockedout(User)
   * @return boolean
   * @param user
   */
  @Override
  public boolean isUserLockedout( User user )
  {
    if ( user != null )
    {
      // check hard lock first
      if ( user.isAccountLocked() )
      {
        return true;
      }

      int currentFailureCount = 0;
      if ( user.getLoginFailuresCount() != null )
      {
        currentFailureCount = user.getLoginFailuresCount().intValue();
      }

      // now check the soft lock and date
      boolean locked = currentFailureCount >= getSystemVariableService().getPropertyByName( LOCKOUT_FAILURE_COUNT ).getIntVal();
      if ( locked && user.getLockTimeoutExpireDate() == null )
      {
        user.setLockTimeoutExpireDate( buildLockTimeout() );
      }

      if ( locked )
      {
        Instant now = Instant.now();
        if ( !Objects.isNull( user.getLockTimeoutExpireDate() ) && now.isBefore( user.getLockTimeoutExpireDate().toInstant() ) )
        {
          return true;
        }
        else if ( !Objects.isNull( user.getLockTimeoutExpireDate() ) && now.isAfter( user.getLockTimeoutExpireDate().toInstant() ) )
        {
          clearUserLockout( user );// time has elapsed..reset.
          return false;
        }
      }
    }

    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.strategy.UserLockoutStrategy#isUserLockedOutInfo(User)
   * @return boolean
   * @param user
   */
  @Override
  public LockoutInfo getUserLockOutInfo( User user )
  {
    LockoutInfo lockoutInfo = new LockoutInfo();
    int currentFailureCount = 0;
    if ( user.getLoginFailuresCount() != null )
    {
      currentFailureCount = user.getLoginFailuresCount().intValue();
    }
    boolean locked = currentFailureCount >= getAllowableFailedLoginAttempts() && !Objects.isNull( user.getLockTimeoutExpireDate() ) && Instant.now().isBefore( user.getLockTimeoutExpireDate().toInstant() );

    if ( user != null )
    {
      if ( user.isAccountLocked() )
      {
        lockoutInfo.setHardLocked( user.isAccountLocked() );
      }
      else if ( locked )
      {
        lockoutInfo.setSoftLocked( locked );
      }
      else if ( user.isAccountLocked() && locked )
      {
        lockoutInfo.setLockedForBoth( Boolean.TRUE );
      }

    }
    return lockoutInfo;
  }

  private Date buildLockTimeout()
  {
    long minutes = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_LOCKOUT_DURATION_MINUTES ).getLongVal();
    Instant lockedUntil = Instant.now().plus( minutes, ChronoUnit.MINUTES );
    return Date.from( lockedUntil );
  }

  /**
   * Clear/Reset the user lockout status by resetting the login failur count
   * 
   * @param user
   */
  private void clearUserLockout( User user )
  {
    if ( user != null )
    {
      user.setLoginFailuresCount( new Integer( 0 ) );
      user.setLockTimeoutExpireDate( null );
    }
  }

  @Override
  public int getAllowableFailedLoginAttempts()
  {
    int allowableAttempts = -1;
    PropertySetItem ps = getSystemVariableService().getPropertyByName( LOCKOUT_FAILURE_COUNT );
    if ( ps != null )
    {
      allowableAttempts = ps.getIntVal();
    }
    return allowableAttempts;
  }
}
