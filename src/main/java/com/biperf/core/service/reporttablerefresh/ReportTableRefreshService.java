/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reporttablerefresh/ReportTableRefreshService.java,v $
 */

package com.biperf.core.service.reporttablerefresh;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * QuizService.
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
 * <td>crosenquest</td>
 * <td>Oct 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ReportTableRefreshService extends SAO
{

  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "reportTableRefreshService";

  /**
   * Calls the stored procedure for syncing app users
   */
  public Map reportTableCoreRefresh( Long userId );

}
