/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/sysadmin/CacheStatsController.java,v $
 */

package com.biperf.core.service.cache;

/**
 * CacheStatsController gets the cache bean.
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
 * <td>Jun 12, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class CacheManagementBean implements java.io.Serializable
{
  private long timeToIdleSeconds;
  private long timeToLiveSeconds;
  private long maxBytesLocalHeap;
  private long maxBytesLocalDisk;

  private String cacheName = null;
  private long calculatedMemorySize = 0;
  private long calculatedOffHeapSize = 0;
  private long calculatedOnDiskSize = 0;

  private float averageGetTime = 0;
  private float averageSearchTime = 0;

  private long memoryStoreSize = 0;
  private long offHeapStoreSize = 0;
  private int diskStoreSize = 0;

  // come from the Statistics object
  private float statsAverageGetTime = 0;
  private long statsAverageSearchTime = 0;
  private long cacheHits = 0;
  private long cacheMisses = 0;
  private long diskStoreObjectCount = 0;
  private long evictionCount = 0;

  private long inMemoryHits = 0;
  private long inMemoryMisses = 0;
  private long memoryStorageObjectCount = 0;
  private long objectCount = 0;
  private long offHeapHits = 0;
  private long offHeapMisses = 0;
  private long offHeapStorabeObjectCount = 0;
  private long onDiskHits = 0;
  private long searchesPerSecond = 0;
  private int statisticsAccuracy = 0;
  private String statisticsAccuracyDescription = null;
  private long writerQueueSize = 0;

  public String getCacheName()
  {
    return cacheName;
  }

  public void setCacheName( String cacheName )
  {
    this.cacheName = cacheName;
  }

  public long getCalculatedMemorySize()
  {
    return calculatedMemorySize;
  }

  public void setCalculatedMemorySize( long calculatedMemorySize )
  {
    this.calculatedMemorySize = calculatedMemorySize;
  }

  public long getCalculatedOffHeapSize()
  {
    return calculatedOffHeapSize;
  }

  public void setCalculatedOffHeapSize( long calculatedOffHeapSize )
  {
    this.calculatedOffHeapSize = calculatedOffHeapSize;
  }

  public long getCalculatedOnDiskSize()
  {
    return calculatedOnDiskSize;
  }

  public void setCalculatedOnDiskSize( long calculatedOnDiskSize )
  {
    this.calculatedOnDiskSize = calculatedOnDiskSize;
  }

  public float getAverageGetTime()
  {
    return averageGetTime;
  }

  public void setAverageGetTime( float averageGetTime )
  {
    this.averageGetTime = averageGetTime;
  }

  public float getAverageSearchTime()
  {
    return averageSearchTime;
  }

  public void setAverageSearchTime( float averageSearchTime )
  {
    this.averageSearchTime = averageSearchTime;
  }

  public long getMemoryStoreSize()
  {
    return memoryStoreSize;
  }

  public void setMemoryStoreSize( long memoryStoreSize )
  {
    this.memoryStoreSize = memoryStoreSize;
  }

  public long getOffHeapStoreSize()
  {
    return offHeapStoreSize;
  }

  public void setOffHeapStoreSize( long offHeapStoreSize )
  {
    this.offHeapStoreSize = offHeapStoreSize;
  }

  public int getDiskStoreSize()
  {
    return diskStoreSize;
  }

  public void setDiskStoreSize( int diskStoreSize )
  {
    this.diskStoreSize = diskStoreSize;
  }

  public float getStatsAverageGetTime()
  {
    return statsAverageGetTime;
  }

  public void setStatsAverageGetTime( float statsAverageGetTime )
  {
    this.statsAverageGetTime = statsAverageGetTime;
  }

  public long getStatsAverageSearchTime()
  {
    return statsAverageSearchTime;
  }

  public void setStatsAverageSearchTime( long statsAverageSearchTime )
  {
    this.statsAverageSearchTime = statsAverageSearchTime;
  }

  public long getCacheHits()
  {
    return cacheHits;
  }

  public void setCacheHits( long cacheHits )
  {
    this.cacheHits = cacheHits;
  }

  public long getCacheMisses()
  {
    return cacheMisses;
  }

  public void setCacheMisses( long cacheMisses )
  {
    this.cacheMisses = cacheMisses;
  }

  public long getDiskStoreObjectCount()
  {
    return diskStoreObjectCount;
  }

  public void setDiskStoreObjectCount( long diskStoreObjectCount )
  {
    this.diskStoreObjectCount = diskStoreObjectCount;
  }

  public long getEvictionCount()
  {
    return evictionCount;
  }

  public void setEvictionCount( long evictionCount )
  {
    this.evictionCount = evictionCount;
  }

  public long getInMemoryHits()
  {
    return inMemoryHits;
  }

  public void setInMemoryHits( long inMemoryHits )
  {
    this.inMemoryHits = inMemoryHits;
  }

  public long getInMemoryMisses()
  {
    return inMemoryMisses;
  }

  public void setInMemoryMisses( long inMemoryMisses )
  {
    this.inMemoryMisses = inMemoryMisses;
  }

  public long getMemoryStorageObjectCount()
  {
    return memoryStorageObjectCount;
  }

  public void setMemoryStorageObjectCount( long memoryStorageObjectCount )
  {
    this.memoryStorageObjectCount = memoryStorageObjectCount;
  }

  public long getObjectCount()
  {
    return objectCount;
  }

  public void setObjectCount( long objectCount )
  {
    this.objectCount = objectCount;
  }

  public long getOffHeapHits()
  {
    return offHeapHits;
  }

  public void setOffHeapHits( long offHeapHits )
  {
    this.offHeapHits = offHeapHits;
  }

  public long getOffHeapMisses()
  {
    return offHeapMisses;
  }

  public void setOffHeapMisses( long offHeapMisses )
  {
    this.offHeapMisses = offHeapMisses;
  }

  public long getOffHeapStorabeObjectCount()
  {
    return offHeapStorabeObjectCount;
  }

  public void setOffHeapStorabeObjectCount( long offHeapStorabeObjectCount )
  {
    this.offHeapStorabeObjectCount = offHeapStorabeObjectCount;
  }

  public long getOnDiskHits()
  {
    return onDiskHits;
  }

  public void setOnDiskHits( long onDiskHits )
  {
    this.onDiskHits = onDiskHits;
  }

  public long getSearchesPerSecond()
  {
    return searchesPerSecond;
  }

  public void setSearchesPerSecond( long searchesPerSecond )
  {
    this.searchesPerSecond = searchesPerSecond;
  }

  public int getStatisticsAccuracy()
  {
    return statisticsAccuracy;
  }

  public void setStatisticsAccuracy( int statisticsAccuracy )
  {
    this.statisticsAccuracy = statisticsAccuracy;
  }

  public String getStatisticsAccuracyDescription()
  {
    return statisticsAccuracyDescription;
  }

  public void setStatisticsAccuracyDescription( String statisticsAccuracyDescription )
  {
    this.statisticsAccuracyDescription = statisticsAccuracyDescription;
  }

  public long getWriterQueueSize()
  {
    return writerQueueSize;
  }

  public void setWriterQueueSize( long writerQueueSize )
  {
    this.writerQueueSize = writerQueueSize;
  }

  public long getTimeToIdleSeconds()
  {
    return timeToIdleSeconds;
  }

  public void setTimeToIdleSeconds( long timeToIdleSeconds )
  {
    this.timeToIdleSeconds = timeToIdleSeconds;
  }

  public long getTimeToLiveSeconds()
  {
    return timeToLiveSeconds;
  }

  public void setTimeToLiveSeconds( long timeToLiveSeconds )
  {
    this.timeToLiveSeconds = timeToLiveSeconds;
  }

  public long getMaxBytesLocalHeap()
  {
    return maxBytesLocalHeap;
  }

  public void setMaxBytesLocalHeap( long maxBytesLocalHeap )
  {
    this.maxBytesLocalHeap = maxBytesLocalHeap;
  }

  public long getMaxBytesLocalDisk()
  {
    return maxBytesLocalDisk;
  }

  public void setMaxBytesLocalDisk( long maxBytesLocalDisk )
  {
    this.maxBytesLocalDisk = maxBytesLocalDisk;
  }
}
