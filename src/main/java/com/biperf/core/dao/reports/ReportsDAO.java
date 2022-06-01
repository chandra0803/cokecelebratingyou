/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/ReportsDAO.java,v $
 *
 */

package com.biperf.core.dao.reports;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboard;

/**
 * ReportsDAO <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public interface ReportsDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "reportsDAO";

  /**
   * Gets the last date report was run.
   * 
   * @param reportName
   * @param reportType
   * @return Date
   */
  public Date getReportDate( String reportCategory );

  /**
   * Retrieve the user characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportUserCharacteristics();

  /**
   * Retrieve the node type characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportNodeTypeCharacteristics();

  /**
   * Retrieve the User Dashboard along with its items
   * 
   * @return Report Dashboard.
   */
  public ReportDashboard getUserDashboard( Long id );

  /**
   * Retrieve the Reports
   * 
   * @return Reports.
   */
  public List getReports();

  /**
   * Retrieve All Reports
   * 
   * @return Reports.
   */
  public List getAllReports();

  /**
   * Saves the Report Dashboard information to the database.
   * 
   * @param reportDashboard
   * @return ReportDashboard
   */
  public ReportDashboard saveReportDashboard( ReportDashboard reportDashboard );

  /**
   * Gets a report object
   * @param reportId
   * @return
   */
  public Report getReport( Long reportId );

  /**
   * Gets a report object
   * @param reportCode
   * @return
   */
  public Report getReportByCode( String reportCode );

  /**
   * Gets a report parameter drop down list using named query 
   * @param namedQuery
   * @return
   */
  public List getReportParameters( String namedQuery, Object param );

  /**
   * Update the report
   * 
   * @param report
   */
  public void updateReports( Report report );

  public String getAwardType( Long dashboardItemId, Long reportId );
}
