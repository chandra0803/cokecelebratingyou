/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/impl/AccountExpirationStrategyImpl.java,v $
 */

package com.biperf.core.strategy.impl;

import com.biperf.core.domain.user.User;
import com.biperf.core.strategy.AccountExpirationStrategy;
import com.biperf.core.strategy.BaseStrategy;

/**
 * Strategy for handling account expiration.
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
 * <td>zahler</td>
 * <td>Apr 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AccountExpirationStrategyImpl extends BaseStrategy implements AccountExpirationStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.strategy.AccountExpirationStrategy#isAccountExpired(com.biperf.core.domain.user.User)
   * @param user
   * @return boolean isAccountExpired
   */
  public boolean isAccountExpired( User user )
  {
    // It appears that this strategy is not used. See PasswordPolicyStrategy.isPasswordExpired()
    return false;
  }

}
