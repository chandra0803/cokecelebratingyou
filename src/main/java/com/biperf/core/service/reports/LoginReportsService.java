/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/LoginReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * LoginReportsService.
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
 * <td>simhadri</td>
 * <td>Mar 31, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface LoginReportsService extends SAO
{
  public static String BEAN_NAME = "loginReportsService";

  public Map<String, Object> getMonthlyLoginBarResults( Map<String, Object> reportParameters, boolean uniqueValues );

  public Map<String, Object> getLoginStatusChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getOrganizationBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getLoginPercentageBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTopPaxLoginsByName( Map<String, Object> reportParameters );

  public Map<String, Object> getLoginListOfParticipantsResults( Map<String, Object> reportParameters, boolean nodeAndBelow );

  public Map<String, Object> getOrgLoginActivityTopLevelResults( Map<String, Object> reportParameters );

  public Map<String, Object> getParticipantLogonActivityResults( Map<String, Object> reportParameters );

  // =======================================
  // LOGIN EXTRACT REPORT
  // =======================================
  public Map getLoginExtractResults( Map<String, Object> reportParameters );

}
