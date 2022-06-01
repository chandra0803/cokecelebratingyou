/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportDashboardController.java,v $
 *
 */

package com.biperf.core.ui.reports;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.ui.BaseController;

/**
 * ReportDashboardController.
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
 * <td>arasi</td>
 * <td>May 19, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReportDashboardController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( ReportDashboardController.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    final String METHOD_NAME = "execute";

    LOG.info( ">>> " + METHOD_NAME );
    /*
     * ReportsService reportsService = getReportsService(); ReportDashboard reportDashboard =
     * reportsService.getUserDashboardById( UserManager.getUserId() ); ArrayList itemList = new
     * ArrayList(); if ( reportDashboard != null ) { ReportDashboardItem.removePdfExportFileNames(
     * reportDashboard.getId() ); for ( int i = 1; i <= ReportsService.NUMBER_OF_DASHBOARD_REPORT;
     * i++ ) { ReportDashboardItem item = reportDashboard.getReportDashboardItem( i ); if ( item ==
     * null ) { item = new ReportDashboardItem(); item.setReportType( null );
     * item.setPositionNumber( new Integer( i ) ); } else { item.setPdfExportRequestId(
     * item.getReportDashboard().getId() ); ReportDashboardItem.add( item ); } itemList.add( item );
     * } reportDashboard.setReportDashboardItems( itemList ); } request.setAttribute(
     * "reportDashboard", reportDashboard );
     */
    LOG.info( "<<< " + METHOD_NAME );
  }

  private ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
  }

}
