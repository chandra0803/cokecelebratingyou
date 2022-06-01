/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/UserLockoutStrategy.java,v $
 */

package com.biperf.core.strategy;

import com.biperf.core.domain.user.User;
import com.biperf.core.ui.user.LockoutInfo;

/**
 * UserLockoutStrategy.
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
public interface UserLockoutStrategy extends Strategy
{
  /**
   * Handles the Authentication for the LockoutStrategy
   * 
   * @param user
   * @param isAuthenticationValid
   */
  public void handleAuthentication( User user, boolean isAuthenticationValid );

  /**
   * This method tells wthether the user is currently locked or not. The user id needs to be passed
   * as user may have not been authenticated yet and no user id is available on thread local.
   * 
   * @param user
   * @return boolean
   */
  public boolean isUserLockedout( User user );

  public LockoutInfo getUserLockOutInfo( User user );

  /*
   * The number of allowable failed attempts to login before locking the user out.
   */
  public int getAllowableFailedLoginAttempts();
}
