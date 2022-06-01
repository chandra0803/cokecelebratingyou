/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/sysadmin/PerformanceStats.java,v $
 */

package com.biperf.core.ui.sysadmin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.jamonapi.MonitorFactory;

/**
 * PerformanceStats is a holder of the JAMon data that works better with displaytag.
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
 * <td>Sep 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PerformanceStats
{

  private String[] data;

  public static List getData()
  {
    List data = new ArrayList();
    String[][] raw = MonitorFactory.getRootMonitor().getData();
    for ( int i = 0; i < raw.length; i++ )
    {
      try
      {
        data.add( new PerformanceStats( raw[i] ) );
      }
      catch( Exception ignore )
      {
        // ignore
      }
    }
    return data;
  }

  private PerformanceStats( String[] data )
  {
    if ( data == null || data.length != 26 )
    {
      throw new IllegalArgumentException( "data must have 26 elements" );
    }
    this.data = data;
  }

  public String getLabel()
  {
    return data[0];
  }

  public int getHits()
  {
    return NumberUtils.toInt( StringUtils.remove( data[1], "," ) );
  }

  public int getAvgDuration()
  {
    return NumberUtils.toInt( StringUtils.remove( data[2], "," ) );
  }

  public int getTotalDuration()
  {
    return NumberUtils.toInt( StringUtils.remove( data[3], "," ) );
  }

  public int getStdDevDuration()
  {
    return NumberUtils.toInt( StringUtils.remove( data[4], "," ) );
  }

  public int getMinDuration()
  {
    return NumberUtils.toInt( StringUtils.remove( data[5], "," ) );
  }

  public int getMaxDuration()
  {
    return NumberUtils.toInt( StringUtils.remove( data[6], "," ) );
  }

  public int getActiveCount()
  {
    return NumberUtils.toInt( StringUtils.remove( data[7], "," ) );
  }

  public int getAvgActiveCount()
  {
    return NumberUtils.toInt( StringUtils.remove( data[8], "," ) );
  }

  public int getMaxActiveCount()
  {
    return NumberUtils.toInt( StringUtils.remove( data[9], "," ) );
  }

  public String getFirstAccessTime()
  {
    return data[10];
  }

  public String getLastAccessTime()
  {
    return data[11];
  }

  public String getPrimary()
  {
    return data[12];
  }

  public String[] getBuckets()
  {
    String[] result = new String[13];
    for ( int i = 0; i < 13; i++ )
    {
      result[i] = data[13 + i];
    }
    return result;
  }

}
