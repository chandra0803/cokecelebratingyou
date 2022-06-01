/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/cache/UserCache.java,v $
 */

package com.biperf.core.security.cache;

import com.biperf.core.domain.user.User;

/**
 * Provides a cache of {@link User} objects.
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
 * <td>Apr 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface UserCache
{

  /**
   * Obtains a {@link User} from the cache.
   * 
   * @param username the {@link User} used to place the user in the cache
   * @return the populated <code>UserDetails</code> or <code>null</code> if the user could not
   *         be found or if the cache entry has expired
   */
  public User getUserFromCache( String username );

  /**
   * Places a {@link User} in the cache. The <code>username</code> is the key used to subsequently
   * retrieve the <code>UserDetails</code>.
   * 
   * @param user the fully populated <code>User</code> to place in the cache
   */
  public void putUserInCache( User user );

  /**
   * Removes the specified user from the cache. The <code>username</code> is the key used to
   * remove the user. If the user is not found, the method should simply return (not thrown an
   * exception).
   * <P>
   * Some cache implementations may not support eviction from the cache, in which case they should
   * provide appropriate behaviour to alter the user in either its documentation, via an exception,
   * or through a log message.
   * </p>
   * 
   * @param username to be evicted from the cache
   */
  public void removeUserFromCache( String username );
}
