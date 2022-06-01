/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.ActivityLoader;
import com.biperf.core.service.promotion.engine.ActivityLoaderFactory;

/**
 * ActivityLoaderFactory.
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
 * <td>wadzinsk</td>
 * <td>Aug 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ActivityLoaderFactoryImpl implements ActivityLoaderFactory
{
  /** Injected */
  private Map entries;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.ActivityLoaderFactory#getActivityLoader(java.lang.String)
   * @param payoutType
   * @return ActivityLoader
   */
  public ActivityLoader getActivityLoader( String payoutType )
  {
    ActivityLoader activityLoader = (ActivityLoader)entries.get( payoutType );
    if ( activityLoader == null )
    {
      throw new BeaconRuntimeException( "Unknown promotionPayoutTypeCode: " + payoutType );
    }

    return activityLoader;
  }

  /**
   * @return value of entries property
   */
  public Map getEntries()
  {
    return entries;
  }

  /**
   * @param entries value for entries property
   */
  public void setEntries( Map entries )
  {
    this.entries = entries;
  }
}
