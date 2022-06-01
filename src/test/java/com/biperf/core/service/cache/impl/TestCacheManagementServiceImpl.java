
package com.biperf.core.service.cache.impl;

import java.util.List;

import com.biperf.core.service.cache.CacheManagementBean;
import com.biperf.core.service.cache.CacheManagementService;

public class TestCacheManagementServiceImpl implements CacheManagementService
{
  @Override
  public String getCacheManagerName()
  {
    return null;
  }

  @Override
  public void clearCache( String cacheRegionName )
  {
  }

  @Override
  public void clearCacheStatistics( String cacheRegionName )
  {
  }

  @Override
  public List<String> getCacheRegionNames()
  {
    return null;
  }

  @Override
  public CacheManagementBean getCacheRegionBean( String cacehRegionName )
  {
    return null;
  }

  @Override
  public List<CacheManagementBean> getCacheRegionBeans()
  {
    return null;
  }

  @Override
  public void setCacheManagerName( String cacheManagerName )
  {
  }

  @Override
  public void updateCacheSettings( CacheManagementBean cacheManagementBean )
  {
  }

  @Override
  public void shutdown()
  {
  }
}
