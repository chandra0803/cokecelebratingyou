/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/cache/EhCacheBasedUserCache.java,v $
 */

package com.biperf.core.security.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataRetrievalFailureException;

import com.biperf.core.domain.user.User;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

/**
 * Caches <code>User</code> objects using a Spring IoC defined <A
 * HREF="http://ehcache.sourceforge.net">EHCACHE</a>.
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
public class EhCacheBasedUserCache implements UserCache, InitializingBean
{
  private static final Log logger = LogFactory.getLog( EhCacheBasedUserCache.class );

  private Cache cache;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.cache.UserCache#getUserFromCache(java.lang.String)
   * @param username
   * @return <code>User</code>
   */
  public User getUserFromCache( String username )
  {
    Element element = null;

    try
    {
      element = cache.get( username );
    }
    catch( CacheException cacheException )
    {
      throw new DataRetrievalFailureException( "Cache failure: " + cacheException.getMessage(), cacheException );
    }

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Cache hit: " + ( element != null ) + "; username: " + username );
    }

    if ( element == null )
    {
      return null;
    }

    return (User)element.getValue();
  }

  /**
   * Overridden from
   * 
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   * @throws Exception
   */
  public void afterPropertiesSet() throws Exception
  {
    if ( cache == null )
    {
      throw new IllegalArgumentException( "cache mandatory" );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.cache.UserCache#putUserInCache(com.biperf.core.domain.user.User)
   * @param user
   */
  public void putUserInCache( User user )
  {
    Element element = new Element( user.getUserName(), user );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Cache put: " + element.getKey() );
    }

    cache.put( element );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.cache.UserCache#removeUserFromCache(java.lang.String)
   * @param username
   */
  public void removeUserFromCache( String username )
  {
    cache.remove( username );
  }

  /**
   * @return value of cache property
   */
  public Cache getCache()
  {
    return cache;
  }

  /**
   * @param cache value for cache property
   */
  public void setCache( Cache cache )
  {
    this.cache = cache;
  }

}
