/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/hibernate/GenericPostUpdateEventListener.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.AbstractEvent;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.cache.CacheManagementService;

/**
 * GenericPostUpdateEventListener.
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
 * <td>sedey</td>
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public abstract class AbstractEventListener implements Serializable
{
  private static final Log logger = LogFactory.getLog( AbstractEventListener.class );

  private String promotionQueryCacheName = null;
  private CacheManagementService cacheManagementService = null;

  protected void processPromotionChangeEvent( AbstractEvent event, Object entity )
  {
    if ( entity instanceof Promotion )
    {
      if ( logger.isWarnEnabled() )
      {
        logger.warn( "processPromotionChangeEvent " + event.getClass().getSimpleName() + " on Promotion: " + ( null != entity ? ( (Promotion)entity ).getId() : entity ) );
      }
      getCacheManagementService().clearCache( promotionQueryCacheName );
    }
  }

  public CacheManagementService getCacheManagementService()
  {
    return cacheManagementService;
  }

  public void setCacheManagementService( CacheManagementService cacheManagementService )
  {
    this.cacheManagementService = cacheManagementService;
  }

  public String getPromotionQueryCacheName()
  {
    return promotionQueryCacheName;
  }

  public void setPromotionQueryCacheName( String promotionQueryCacheName )
  {
    this.promotionQueryCacheName = promotionQueryCacheName;
  }
}
