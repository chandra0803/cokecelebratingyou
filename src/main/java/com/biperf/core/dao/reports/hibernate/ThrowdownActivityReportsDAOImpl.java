
package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.ThrowdownActivityReportsDAO;

/**
 * 
 * ThrowdownActivityReportsDAOImpl.
 * 
 * @author kandhi
 * @since Oct 21, 2013
 * @version 1.0
 */
public class ThrowdownActivityReportsDAOImpl extends BaseReportsDAO implements ThrowdownActivityReportsDAO
{

  private static final Log log = LogFactory.getLog( ThrowdownActivityReportsDAOImpl.class );

  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  // ********************************************************************//
  // ********** THROWDOWN ACTIVITY BY PAX SUMMARY FIRST LEVEL ***********//
  // ********************************************************************//

  @Override
  public Map<String, Object> getThrowdownActivityByPaxSummaryTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETBYPAXSUMMARY";
    String sortColName = "PARTICIPANT_NAME";
    String[] sortColNames = { "PARTICIPANT_NAME",
                              "",
                              "LOGIN_ID",
                              "COUNTRY",
                              "USER_STATUS",
                              "NODE_NAME",
                              "JOB_POSITION",
                              "DEPARTMENT",
                              "PROMO_NAME",
                              "WINS",
                              "LOSSES",
                              "TIES",
                              "ACTIVITY",
                              "RANK",
                              "PAYOUT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcThrowdownActivityByPaxReport procedure = new CallPrcThrowdownActivityByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getThrowdownActivityByPaxDetailTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETBYPAXDETAIL";
    String sortColName = "ROUND_NUMBER";
    String[] sortColNames = { "PARTICIPANT_NAME",
                              "LOGIN_ID",
                              "COUNTRY",
                              "USER_STATUS",
                              "NODE_NAME",
                              "JOB_POSITION",
                              "DEPARTMENT",
                              "PROMOTION_NAME",
                              "ROUND_NUMBER",
                              "ROUND_START_DATE",
                              "ROUND_END_DATE",
                              "WINS",
                              "LOSSES",
                              "TIES",
                              "ACTIVITY",
                              "RANK",
                              "PAYOUT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcThrowdownActivityByPaxReport procedure = new CallPrcThrowdownActivityByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ********************************************************************//
  // ********** THROWDOWN ACTIVITY BY PAX TOTAL ACTIVITY CHART **********//
  // ********************************************************************//

  @Override
  public Map<String, Object> getThrowdownTotalActivityChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETTOTALACTIVITYCHART";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcThrowdownActivityByPaxReport procedure = new CallPrcThrowdownActivityByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ********************************************************************//
  // ********** THROWDOWN ACTIVITY BY PAX ACTIVITY BY ROUND CHART *******//
  // ********************************************************************//

  @Override
  public Map<String, Object> getThrowdownActivityByRoundChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETBYROUNDCHART";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcThrowdownActivityByPaxReport procedure = new CallPrcThrowdownActivityByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ********************************************************************//
  // ********** THROWDOWN ACTIVITY BY PAX POINTS EARNED CHART ***********//
  // ********************************************************************//

  @Override
  public Map<String, Object> getPointsEarnedInThrowdownChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETPOINTSEARNED";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcThrowdownActivityByPaxReport procedure = new CallPrcThrowdownActivityByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ***************************************************************//
  // ********** THROWDOWN ACTIVITY BY PAX REPORT EXTRACT ***********//
  // ***************************************************************//

  @Override
  public Map getThrowdownActivityByPaxReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcThrowdownExtract procedure = new CallPrcThrowdownExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

}
