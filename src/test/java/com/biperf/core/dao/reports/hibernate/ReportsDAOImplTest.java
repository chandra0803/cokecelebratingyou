/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/reports/hibernate/ReportsDAOImplTest.java,v $
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Date;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.reports.ReportsDAO;
import com.biperf.core.domain.enums.ReportCategoryType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.domain.report.ReportParameter;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * ReportsDAOImplTest.
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
 * <td>Dec 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportsDAOImplTest extends BaseDAOTest
{

  public void testGetReportDate()
  {

    Date coreRefreshDate = getReportsDAO().getReportDate( ReportCategoryType.CORE );

    assertTrue( "Date wasn't null", coreRefreshDate != null );

  }

  /**
   * Get the ReportsDAO.
   * 
   * @return ReportsDAO
   */
  private ReportsDAO getReportsDAO()
  {
    return (ReportsDAO)ApplicationContextFactory.getApplicationContext().getBean( "reportsDAO" );
  }

  private void testGetAwardType()
  {
    Report report = new Report();
    report.setId( 1000L );
    ReportParameter reportParameter = new ReportParameter();
    reportParameter.setReport( report );
    reportParameter.setParameterName( "AWARDTYPE" );
    reportParameter.setId( 5000L );
    ReportDashboardItem reportDashBoardItem = new ReportDashboardItem();
    reportDashBoardItem.setId( 2000L );
    ReportDashboardItemParam reportDashBoardItemParam = new ReportDashboardItemParam();
    reportDashBoardItemParam.setReportDashboardItem( reportDashBoardItem );
    reportDashBoardItemParam.setValue( "points" );
    reportDashBoardItemParam.setReportParameter( reportParameter );

    ReportsDAO reportsDao = getReportsDAO();
    String val = reportsDao.getAwardType( reportDashBoardItem.getId(), report.getId() );
    assertTrue( val.equals( "points" ) );

  }

}