/*
\ * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/LoginReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.LoginReportsDAO;

/**
 * 
 * LoginReportsDAOImpl.
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
public class LoginReportsDAOImpl extends BaseReportsDAO implements LoginReportsDAO
{
  private DataSource dataSource;

  @Override
  public Map<String, Object> getMonthlyLoginBarResults( Map<String, Object> reportParameters, boolean uniqueValues )
  {
    String procName = "pkg_query_login_activity.prc_getMonthlyLoginBarResults";
    reportParameters.put( "uniqueValues", uniqueValues );
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getTopPaxLoginsByName( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_login_activity.prc_getTopPaxLoginsByName";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getLoginStatusChartResults( Map<String, Object> reportParameters )
  {
    boolean nodeAndBelow = false;

    if ( reportParameters.get( "nodeAndBelow" ).equals( true ) )
    {
      nodeAndBelow = true;
    }
    reportParameters.put( "nodeAndBelow", nodeAndBelow );

    String procName = "pkg_query_login_activity.prc_getLoginStatusChartResults";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getLoginPercentageBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "pkg_query_login_activity.prc_getPercentageBarResults";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcLoginActivity procedure = new CallPrcLoginActivity( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getOrganizationBarResults( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_login_activity.prc_getOrganizationBarResults";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getParticipantLogonActivityResults( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_login_activity.prc_getPaxResults";
    String sortColName = "PAX_NAME";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "PAX_NAME";
          break;
        case 2:
          sortColName = "PAX_POSITION";
          break;
        case 3:
          sortColName = "PAX_DEPARTMENT";
          break;
        case 4:
          sortColName = "PAX_COUNTRY";
          break;
        case 5:
          sortColName = "ORGANIZATION_NAME";
          break;
        case 6:
          sortColName = "LOGIN_DATE";
          break;
        default:
          break;
      }
    }

    reportParameters.put( "sortedOn", sortColName );
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getLoginListOfParticipantsResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    String sortColName = "ORG";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "ORG";
          break;
        case 2:
          sortColName = "DEPARTMENT";
          break;
        case 3:
          sortColName = "POSITION";
          break;
        case 4:
          sortColName = "NAME";
          break;
        case 6:
          sortColName = "LAST_LOGIN";
          break;
        case 7:
          sortColName = "TOTAL_VISITS";
          break;
        default:
          break;
      }
    }

    reportParameters.put( "sortedOn", sortColName );
    String procName = "pkg_query_login_activity.prc_getListOfPaxResults";
    return callProc( procName, reportParameters );
  }

  /**
   * Get the top level organization log on activity by parent node id.
   * @param reportParameters
   * @return Map
   */
  @Override
  public Map<String, Object> getOrgLoginActivityTopLevelResults( Map<String, Object> reportParameters )
  {
    String sortColName = "NAME";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "NAME";
          break;
        case 3:
          sortColName = "ELIGIBLE_PAX";
          break;
        case 4:
          sortColName = "LOGGED_IN_CNT";
          break;
        case 5:
          sortColName = "PERCENT_LOGIN";
          break;
        case 6:
          sortColName = "NOT_LOGGED_IN_CNT";
          break;
        case 7:
          sortColName = "PERCENT_NOT_LOGIN";
          break;
        case 8:
          sortColName = "TOTAL_VISITS";
          break;
        default:
          break;
      }
    }

    reportParameters.put( "sortedOn", sortColName );
    String procName = "pkg_query_login_activity.prc_getOrgTopLevelResults";
    return callProc( procName, reportParameters );
  }

  private Map<String, Object> callProc( String procName, Map<String, Object> reportParameters )
  {
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcLoginActivity procedure = new CallPrcLoginActivity( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // LOGIN EXTRACT REPORT
  // =======================================
  @SuppressWarnings( { "rawtypes" } )
  @Override
  public Map getLoginExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcLoginExtract procedure = new CallPrcLoginExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
