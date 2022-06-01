/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/LoginReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.LoginReportsDAO;
import com.biperf.core.service.reports.LoginReportsService;

/**
 * LoginReportsServiceImpl.
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
 * <td>sedey</td>
 * <td>June 27, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class LoginReportsServiceImpl implements LoginReportsService
{
  private LoginReportsDAO loginReportsDAO;

  public void setLoginReportsDAO( LoginReportsDAO loginReportsDAO )
  {
    this.loginReportsDAO = loginReportsDAO;
  }

  @Override
  public Map<String, Object> getMonthlyLoginBarResults( Map<String, Object> reportParameters, boolean uniqueValues )
  {
    return loginReportsDAO.getMonthlyLoginBarResults( reportParameters, uniqueValues );
  }

  @Override
  public Map<String, Object> getLoginStatusChartResults( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getLoginStatusChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getOrganizationBarResults( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getOrganizationBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getLoginPercentageBarResults( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getLoginPercentageBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getTopPaxLoginsByName( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getTopPaxLoginsByName( reportParameters );
  }

  @Override
  public Map<String, Object> getLoginListOfParticipantsResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    return loginReportsDAO.getLoginListOfParticipantsResults( reportParameters, nodeAndBelow );
  }

  @Override
  public Map<String, Object> getOrgLoginActivityTopLevelResults( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getOrgLoginActivityTopLevelResults( reportParameters );
  }

  @Override
  public Map<String, Object> getParticipantLogonActivityResults( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getParticipantLogonActivityResults( reportParameters );
  }

  // =======================================
  // LOGIN EXTRACT REPORT
  // =======================================
  @Override
  public Map getLoginExtractResults( Map<String, Object> reportParameters )
  {
    return loginReportsDAO.getLoginExtractResults( reportParameters );
  }

}
