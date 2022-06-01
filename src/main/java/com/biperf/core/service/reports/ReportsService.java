/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/ReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.service.SAO;

/**
 * ReportsService <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public interface ReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "reportsService";

  public static final int NUMBER_OF_DASHBOARD_REPORT = 6;

  /**
   * Get the current users top level node. The current user is obtained from the UserManager.
   * 
   * @return List.
   */
  public List getUsersTopLevelNodes();

  /**
   * Get node by the given node id
   * 
   * @return Node.
   */
  public Node getNodeById( Long nodeId );

  /**
   * Retrieve the user characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportUserCharacteristics();

  /**
   * Retreive the node type characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportNodeTypeCharacteristics();

  /**
   * Gets the last date report was run.
   * 
   * @param reportName
   * @param reportType
   * @return Date
   */
  public Date getReportDate( String reportCategory );

  /**
   * Gets promotion by the given id
   * 
   * @return Promotion
   */
  public Promotion getPromotionById( Long promotionId );

  /**
   * Determine whether or not the currently logged in user can view the particular node
   * 
   * @param node
   * @return boolean
   */
  public boolean canUserViewNode( Node node );

  /**
   * Saves the Report Dashboard information to the database.
   * 
   * @param reportDashboard
   * @return ReportDashboard
   */
  public ReportDashboard saveReportDashboard( ReportDashboard reportDashboard );

  /**
   * Gets the Report Dashboard information from the database.
   * 
   * @param userId
   * @return ReportDashboard
   */
  public ReportDashboard getUserDashboardById( Long userId );

  /**
   * Gets a report object 
   * @param reportId
   * @return report
   */
  public Report getReport( Long reportId );

  /**
   * Gets all reports object 
   * @return List
   */
  public List getAllReports();

  /**
   * Gets a report object 
   * @param reportCode
   * @return report
   */
  public Report getReportByCode( String reportCode );

  public List getValuesFromNamedQuery( String listDefinition, Object param );

  /**
   * Update the Report to the database.
   * 
   * @param Report
   */
  public void updateReports( Report report );

  public String getAwardType( Long dashboardItemId, Long reportId );

}
