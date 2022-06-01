/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

/**
 * ActivityLoaderFactoryImpl.
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
 * <td>Aug 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ActivityLoaderFactory
{

  /**
   * Construct an ActivityLoader instance. The type of template is determined by the payout type.
   * 
   * @param payoutType
   * @return ActivityLoader
   */
  public ActivityLoader getActivityLoader( String payoutType );

}
