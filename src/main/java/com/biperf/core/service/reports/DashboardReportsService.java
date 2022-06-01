/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/DashboardReportsService.java,v $
 */

package com.biperf.core.service.reports;

import java.util.List;
import java.util.Locale;

import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.service.SAO;

/**
 * DashboardReportsService.
 * 
 * @author kandhi
 * @since Aug 15, 2012
 * @version 1.0
 */
public interface DashboardReportsService extends SAO
{

  public static String BEAN_NAME = "dashboardReportsService";

  public ReportDashboard getUserDashboard( Long paxId );

  public void deleteUserDashboard( Long userId );

  public ReportDashboard removeChart( Long reportDashboardId, Long reportDashboardItemId );

  public ReportDashboard reOrder( Long reportDashboardId, Long reportDashBoardItemId, int newIndex );

  public ReportDashboardItem getUserDashboardItem( Long reportDashboardId, int seqNum );

  public ReportDashboardItem getUserDashboardItemById( Long reportDashboardItemId );

  public ReportDashboard saveReportDashBoardItem( Long chartId, Long paxId, List<ReportDashboardItemParam> reportDashboardItemParams, Boolean nodeAndBelow );

  void saveUserDashboard( Long paxId, String chartIds, Locale locale );

  public boolean dashboardExistsForUser( Long userId );

}
