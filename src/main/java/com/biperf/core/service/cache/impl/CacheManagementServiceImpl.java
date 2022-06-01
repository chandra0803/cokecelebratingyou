
package com.biperf.core.service.cache.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.biperf.core.service.cache.CacheManagementBean;
import com.biperf.core.service.cache.CacheManagementService;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;

public class CacheManagementServiceImpl implements CacheManagementService
{
  private String cacheManagerName = null;

  @Override
  public String getCacheManagerName()
  {
    return cacheManagerName;
  }

  @Override
  public void clearCache( String cacheRegionName )
  {
    getCacheManager().getEhcache( cacheRegionName ).removeAll();
  }

  @Override
  public void clearCacheStatistics( String cacheRegionName )
  {
    getCacheManager().getEhcache( cacheRegionName ).clearStatistics();
  }

  @Override
  public List<String> getCacheRegionNames()
  {
    List<String> cacheNames = Arrays.asList( getCacheManager().getCacheNames() );
    Collections.sort( cacheNames );
    return cacheNames;
  }

  @Override
  public CacheManagementBean getCacheRegionBean( String cacehRegionName )
  {
    CacheManagementBean bean = new CacheManagementBean();

    Ehcache cache = getCacheManager().getEhcache( cacehRegionName );
    bean.setCacheName( cacehRegionName );
    bean.setCalculatedMemorySize( cache.calculateInMemorySize() );
    bean.setCalculatedOffHeapSize( cache.calculateOffHeapSize() );
    bean.setCalculatedOnDiskSize( cache.calculateOnDiskSize() );
    bean.setAverageGetTime( cache.getAverageGetTime() );
    bean.setAverageSearchTime( cache.getAverageSearchTime() );
    bean.setMemoryStoreSize( cache.getMemoryStoreSize() );
    bean.setOffHeapStoreSize( cache.getOffHeapStoreSize() );
    bean.setDiskStoreSize( cache.getDiskStoreSize() );
    // come from the Statistics object
    Statistics stats = cache.getStatistics();
    bean.setStatsAverageGetTime( stats.getAverageGetTime() );
    bean.setStatsAverageSearchTime( stats.getAverageSearchTime() );
    bean.setCacheHits( stats.getCacheHits() );
    bean.setCacheMisses( stats.getCacheMisses() );
    bean.setDiskStoreObjectCount( stats.getDiskStoreObjectCount() );
    bean.setEvictionCount( stats.getEvictionCount() );
    bean.setInMemoryHits( stats.getInMemoryHits() );
    bean.setInMemoryMisses( stats.getInMemoryMisses() );
    bean.setMemoryStorageObjectCount( stats.getMemoryStoreObjectCount() );
    bean.setObjectCount( stats.getObjectCount() );
    bean.setOffHeapHits( stats.getOffHeapHits() );
    bean.setOffHeapMisses( stats.getOffHeapMisses() );
    bean.setOffHeapStorabeObjectCount( stats.getOffHeapStoreObjectCount() );
    bean.setOnDiskHits( stats.getOnDiskHits() );
    bean.setSearchesPerSecond( stats.getSearchesPerSecond() );
    bean.setStatisticsAccuracy( stats.getStatisticsAccuracy() );
    bean.setStatisticsAccuracyDescription( stats.getStatisticsAccuracyDescription() );
    bean.setWriterQueueSize( stats.getWriterQueueSize() );
    // set the current settings
    net.sf.ehcache.config.CacheConfiguration config = cache.getCacheConfiguration();
    bean.setMaxBytesLocalDisk( config.getMaxBytesLocalDisk() );
    bean.setMaxBytesLocalHeap( config.getMaxBytesLocalHeap() );
    bean.setTimeToIdleSeconds( config.getTimeToIdleSeconds() );
    bean.setTimeToLiveSeconds( config.getTimeToLiveSeconds() );

    return bean;
  }

  @Override
  public void updateCacheSettings( CacheManagementBean cacheManagementBean )
  {
    Ehcache cache = getCacheManager().getEhcache( cacheManagementBean.getCacheName() );
    // alter the mutable changes..
    net.sf.ehcache.config.CacheConfiguration config = cache.getCacheConfiguration();
    config.setTimeToIdleSeconds( cacheManagementBean.getTimeToIdleSeconds() );
    config.setTimeToLiveSeconds( cacheManagementBean.getTimeToLiveSeconds() );
    // config.setMaxBytesLocalDisk( cacheManagementBean.getMaxBytesLocalDisk() ) ;
    config.setMaxBytesLocalHeap( cacheManagementBean.getMaxBytesLocalHeap() );
  }

  @Override
  public List<CacheManagementBean> getCacheRegionBeans()
  {
    List<CacheManagementBean> caches = new ArrayList<CacheManagementBean>();
    for ( String cacheName : getCacheRegionNames() )
    {
      caches.add( getCacheRegionBean( cacheName ) );
    }
    return caches;
  }

  @Override
  public void shutdown()
  {
    getCacheManager().shutdown();
  }
  
  @Override
  public void setCacheManagerName( String cacheManagerName )
  {
    this.cacheManagerName = cacheManagerName;
  }

  private CacheManager getCacheManager()
  {
    return CacheManager.getCacheManager( cacheManagerName );
  }
}
