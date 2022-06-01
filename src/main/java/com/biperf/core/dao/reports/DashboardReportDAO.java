/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/DashboardReportDAO.java,v $
 */

package com.biperf.core.dao.reports;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.report.ReportChart;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportParameter;

/**
 * DashboardReportDAO.
 * 
 * @author kandhi
 * @since Aug 15, 2012
 * @version 1.0
 */
public interface DashboardReportDAO extends DAO
{
  public static final String BEAN_NAME = "dashboardReportDAO";

  public ReportDashboard getUserDashboard( Long id );

  public void deleteUserDashboard( ReportDashboard reportDashboard );

  public ReportDashboard getReportDashBoardById( Long reportDashboardId );

  public ReportDashboardItem getReportDashBoardItemById( Long reportDashboardItemId );

  public ReportChart getReportChartById( Long chartId );

  public ReportParameter getReportParameterById( Long id );

  public boolean dashboardExistsForUser( Long userId );

}
