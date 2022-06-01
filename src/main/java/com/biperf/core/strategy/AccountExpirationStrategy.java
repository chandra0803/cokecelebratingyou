/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/AccountExpirationStrategy.java,v $
 */

package com.biperf.core.strategy;

import com.biperf.core.domain.user.User;

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
public interface AccountExpirationStrategy extends Strategy
{
  /**
   * @param user
   * @return boolean isAccountExpired
   */
  public boolean isAccountExpired( User user );
}
