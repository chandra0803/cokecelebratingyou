/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.enrollment.EnrollmentActivityReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcEnrollmentReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLMENTSUMMARY";
  private static final String PROC2 = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLMENTDETAILS";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_ENROLL_REPORTS.PRC_GETTOTALENROLLMENTBAR";
  private static final String PROC4 = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLSTATUSPERCENTBAR";
  private static final String PROC5 = "PKG_QUERY_ENROLL_REPORTS.PRC_GETENROLLACTIVEINACTIVEBAR";
  private static final String PROC6 = "PKG_QUERY_ENROLL_REPORTS.PRC_GETPIERESULTS";

  public CallPrcEnrollmentReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_role", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentSummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentSummaryTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentDetailsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentBarMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentStatusPercentageMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentActiveInactiveBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcEnrollmentReport.BasicEnrollmentPieMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    if ( ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_role", reportParameters.get( "role" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class BasicEnrollmentSummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<EnrollmentActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EnrollmentActivityReportValue> reportData = new ArrayList<EnrollmentActivityReportValue>();

      while ( rs.next() )
      {
        EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();
        reportValue.setHierarchyNodeName( rs.getString( "hierarchy_node_name" ) );
        reportValue.setHierarchyNodeId( rs.getLong( "hierarchy_node_id" ) );
        reportValue.setActiveCnt( rs.getLong( "active_cnt" ) );
        reportValue.setInactiveCnt( rs.getLong( "inactive_cnt" ) );
        reportValue.setTotalCnt( rs.getLong( "total_cnt" ) );
        reportValue.setIsLeaf( rs.getBoolean( "is_leaf" ) );
        reportValue.setIsTeam( rs.getBoolean( "is_team" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicEnrollmentSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public EnrollmentActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setActiveCnt( rs.getLong( "active_cnt" ) );
        reportValue.setInactiveCnt( rs.getLong( "inactive_cnt" ) );
        reportValue.setTotalCnt( rs.getLong( "total_cnt" ) );
      }
      return reportValue;
    }
  }

  private class BasicEnrollmentDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<EnrollmentActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EnrollmentActivityReportValue> reportData = new ArrayList<EnrollmentActivityReportValue>();

      while ( rs.next() )
      {
        EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();

        reportValue.setPaxName( rs.getString( "pax_name" ) );
        reportValue.setEnrollmentDate( rs.getString( "enrollment_date" ) );
        reportValue.setStatus( rs.getString( "status" ) );
        reportValue.setJobPosition( rs.getString( "job_position" ) );
        reportValue.setRole( rs.getString( "role" ) );
        reportValue.setDepartment( rs.getString( "department" ) );
        reportValue.setNodeName( rs.getString( "node_name" ) );
        reportValue.setCountry( rs.getString( "country" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicEnrollmentBarMapper implements ResultSetExtractor
  {
    @Override
    public List<EnrollmentActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EnrollmentActivityReportValue> reportData = new ArrayList<EnrollmentActivityReportValue>();

      while ( rs.next() )
      {
        EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();

        reportValue.setMonth( rs.getString( "month_name" ) );
        reportValue.setStatusCnt( rs.getLong( "total_enrollment_count" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicEnrollmentStatusPercentageMapper implements ResultSetExtractor
  {
    @Override
    public List<EnrollmentActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EnrollmentActivityReportValue> reportData = new ArrayList<EnrollmentActivityReportValue>();

      while ( rs.next() )
      {
        EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();

        reportValue.setHierarchyNodeName( rs.getString( "hierarchy_node_name" ) );
        reportValue.setActiveCnt( rs.getLong( "active_cnt" ) );
        reportValue.setInactiveCnt( rs.getLong( "inactive_cnt" ) );
        reportValue.setTotalCnt( rs.getLong( "total_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicEnrollmentActiveInactiveBarMapper implements ResultSetExtractor
  {
    @Override
    public List<EnrollmentActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EnrollmentActivityReportValue> reportData = new ArrayList<EnrollmentActivityReportValue>();

      while ( rs.next() )
      {
        EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();

        reportValue.setActivePct( rs.getBigDecimal( "ACTIVE_COUNT" ) );
        reportValue.setInActivePct( rs.getBigDecimal( "INACTIVE_COUNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicEnrollmentPieMapper implements ResultSetExtractor
  {
    @Override
    public List<EnrollmentActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EnrollmentActivityReportValue> reportData = new ArrayList<EnrollmentActivityReportValue>();

      while ( rs.next() )
      {
        EnrollmentActivityReportValue reportValue = new EnrollmentActivityReportValue();
        reportValue.setHierarchyNodeName( rs.getString( "hierarchy_node_name" ) );
        reportValue.setStatusCnt( rs.getLong( "active_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
