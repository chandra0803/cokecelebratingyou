/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reporttablerefresh/ReportTableRefreshDAO.java,v $
 */

package com.biperf.core.dao.reporttablerefresh;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * ClaimDAO will handle processing a claimForm submission.
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
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ReportTableRefreshDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "reportTableRefreshDAO";

  /**
   * Calls the stored procedure for syncing app users
   */
  public Map reportTableCoreRefresh( Long userId );

}
