
package com.biperf.core.service.cache;

import java.util.List;

import com.biperf.core.service.SAO;

public interface CacheManagementService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "cacheManagementService";

  public String getCacheManagerName();

  public void clearCache( String cacheRegionName );

  public void clearCacheStatistics( String cacheRegionName );

  public List<String> getCacheRegionNames();

  public CacheManagementBean getCacheRegionBean( String cacehRegionName );

  public List<CacheManagementBean> getCacheRegionBeans();

  public void setCacheManagerName( String cacheManagerName );

  public void updateCacheSettings( CacheManagementBean cacheManagementBean );
  
  public void shutdown();
}
