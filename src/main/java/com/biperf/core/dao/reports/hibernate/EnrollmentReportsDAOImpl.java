/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.EnrollmentReportsDAO;

/**
 * @author poddutur
 *
 */
public class EnrollmentReportsDAOImpl extends BaseReportsDAO implements EnrollmentReportsDAO
{

  private static final Log log = LogFactory.getLog( EnrollmentReportsDAOImpl.class );

  private DataSource dataSource;

  @Override
  public Map<String, Object> getEnrollmentSummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLMENTSUMMARY";
    String sortColName = "hierarchy_node_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "hierarchy_node_name";
          break;
        case 3:
          sortColName = "active_cnt";
          break;
        case 4:
          sortColName = "inactive_cnt";
          break;
        case 5:
          sortColName = "total_cnt";
          break;
        default:
          sortColName = "hierarchy_node_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcEnrollmentReport procedure = new CallPrcEnrollmentReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getEnrollmentDetailsResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLMENTDETAILS";
    String sortColName = "pax_name";
    String[] sortColNames = { "pax_name", "enrollment_date", "status", "job_position", "role", "department", "node_name", "country" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcEnrollmentReport procedure = new CallPrcEnrollmentReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getTotalEnrollmentsBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_ENROLL_REPORTS.PRC_GETTOTALENROLLMENTBAR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcEnrollmentReport procedure = new CallPrcEnrollmentReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getEnrollmentStatusPercentageBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLSTATUSPERCENTBAR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcEnrollmentReport procedure = new CallPrcEnrollmentReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getEnrollmentActiveInactiveBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLACTIVEINACTIVEBAR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcEnrollmentReport procedure = new CallPrcEnrollmentReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPieResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_ENROLL_REPORTS.PRC_GETPIERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcEnrollmentReport procedure = new CallPrcEnrollmentReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ============================
  // ENROLLMENT EXTRACT REPORT
  // ============================

  @Override
  public Map getEnrollmentExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcEnrollmentExtract procedure = new CallPrcEnrollmentExtract( dataSource );
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
