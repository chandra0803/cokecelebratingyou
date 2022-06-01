/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/activity/MerchandiseActivity.java,v $
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.merchandise.MerchOrder;

/**
 * MerchandiseActivity.
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
 * <td>meadows</td>
 * <td>Jan 14, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface MerchandiseActivity
{
  public MerchOrder getMerchOrder();

  public void setMerchOrder( MerchOrder merchOrder );

}
