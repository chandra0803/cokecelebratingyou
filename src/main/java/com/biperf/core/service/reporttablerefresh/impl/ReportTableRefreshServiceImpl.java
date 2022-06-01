/*
 * (c) 2005 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/quiz/impl/QuizServiceImpl.java,v $
 */

package com.biperf.core.service.reporttablerefresh.impl;

import java.util.Map;

import com.biperf.core.dao.reporttablerefresh.ReportTableRefreshDAO;
import com.biperf.core.service.reporttablerefresh.ReportTableRefreshService;

/**
 * QuizServiceImpl.
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
public class ReportTableRefreshServiceImpl implements ReportTableRefreshService
{

  /** ReportTableRefreshDAO */
  private ReportTableRefreshDAO reportTableRefreshDAO;

  public Map reportTableCoreRefresh( Long userId )
  {
    return reportTableRefreshDAO.reportTableCoreRefresh( userId );
  }

  public ReportTableRefreshDAO getReportTableRefreshDAO()
  {
    return reportTableRefreshDAO;
  }

  public void setReportTableRefreshDAO( ReportTableRefreshDAO reportTableRefreshDAO )
  {
    this.reportTableRefreshDAO = reportTableRefreshDAO;
  }
}
