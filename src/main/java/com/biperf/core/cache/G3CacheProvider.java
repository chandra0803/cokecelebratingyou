/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/cache/G3CacheProvider.java,v $
 */

package com.biperf.core.cache;

import com.biperf.cache.CacheFactory;
import com.biperf.cache.hibernate.AbstractCacheFactoryCacheProvider;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * G3CacheProvider.
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
 * <td>Brian Repko</td>
 * <td>Aug 29, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class G3CacheProvider extends AbstractCacheFactoryCacheProvider
{

  /**
   * Overridden from
   * 
   * @see com.biperf.cache.hibernate.AbstractCacheFactoryCacheProvider#getCacheFactory()
   * @return CacheFactory that was looked up in ApplicationContextFactory
   */
  protected CacheFactory getCacheFactory()
  {
    CacheFactory result = null;
    result = (CacheFactory)ApplicationContextFactory.getApplicationContext().getBean( "cacheFactory" );
    return result;
  }

}
