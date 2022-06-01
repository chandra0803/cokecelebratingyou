/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/sysadmin/CacheStatsForm.java,v $
 */

package com.biperf.core.ui.sysadmin;

import org.apache.struts.action.ActionForm;

/**
 * CacheStatsForm.
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
 * <td>Jun 13, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class CacheManagementForm extends ActionForm
{
  private String cacheName;
  private String method;
  private long maxBytesLocalHeap;
  private long maxBytesLocalDisk;
  private long timeToLiveSeconds;
  private long timeToIdleSeconds;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getCacheName()
  {
    return cacheName;
  }

  public void setCacheName( String cacheName )
  {
    this.cacheName = cacheName;
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

  public long getTimeToLiveSeconds()
  {
    return timeToLiveSeconds;
  }

  public void setTimeToLiveSeconds( long timeToLiveSeconds )
  {
    this.timeToLiveSeconds = timeToLiveSeconds;
  }

  public long getTimeToIdleSeconds()
  {
    return timeToIdleSeconds;
  }

  public void setTimeToIdleSeconds( long timeToIdleSeconds )
  {
    this.timeToIdleSeconds = timeToIdleSeconds;
  }
}
