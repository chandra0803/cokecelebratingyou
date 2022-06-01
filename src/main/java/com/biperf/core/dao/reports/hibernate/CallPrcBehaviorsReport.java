
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

import com.biperf.core.value.behavior.BehaviorsReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author corneliu
 */
public class CallPrcBehaviorsReport extends StoredProcedure
{
  private static final String TAB_RESULTS_PROC = "PKG_QUERY_BEHAVIORS_REPORT.PRC_GETTABULARRESULTS";

  /* Charts */
  private static final String BARCHART_RESULTS_PROC = "PKG_QUERY_BEHAVIORS_REPORT.PRC_GETBARCHARTRESULTS";
  private static final String PIECHART_RESULTS_PROC = "PKG_QUERY_BEHAVIORS_REPORT.PRC_GETPIECHARTRESULTS";

  public CallPrcBehaviorsReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_giverReceiver", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case TAB_RESULTS_PROC:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcBehaviorsReport.BehaviorsReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcBehaviorsReport.BehaviorsReportSummaryTotalsMapper() ) );
        break;
      case BARCHART_RESULTS_PROC:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcBehaviorsReport.BehaviorsBarchartReportMapper() ) );
        break;
      case PIECHART_RESULTS_PROC:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcBehaviorsReport.BehaviorsPiechartReportMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_giverReceiver", reportParameters.get( "giverReceiver" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /* TABLES */

  private class BehaviorsReportMapper implements ResultSetExtractor
  {
    @Override
    public List<BehaviorsReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BehaviorsReportValue> reportData = new ArrayList<BehaviorsReportValue>();

      while ( rs.next() )
      {
        BehaviorsReportValue reportValue = new BehaviorsReportValue();

        reportValue.setBehavior( rs.getString( "behavior" ) );
        reportValue.setbCnt( rs.getLong( "b_cnt" ) );
        reportValue.setIsLeaf( rs.getBoolean( "is_leaf" ) );
        reportValue.setDetailNodeId( rs.getLong( "detail_node_id" ) );
        reportValue.setNodeName( rs.getString( "node_name" ) );
        reportValue.setTotalRecords( rs.getLong( "total_records" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BehaviorsReportSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public List<BehaviorsReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BehaviorsReportValue> reportData = new ArrayList<BehaviorsReportValue>();

      while ( rs.next() )
      {
        BehaviorsReportValue reportValue = new BehaviorsReportValue();
        reportValue.setBehavior( rs.getString( "behavior" ) );
        reportValue.setbCnt( rs.getLong( "b_cnt" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  /* CHARTS */

  private class BehaviorsBarchartReportMapper implements ResultSetExtractor
  {
    @Override
    public List<BehaviorsReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BehaviorsReportValue> reportData = new ArrayList<BehaviorsReportValue>();

      while ( rs.next() )
      {
        BehaviorsReportValue reportValue = new BehaviorsReportValue();

        reportValue.setBehavior( rs.getString( "behavior" ) );
        reportValue.setbCnt( rs.getLong( "b_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BehaviorsPiechartReportMapper implements ResultSetExtractor
  {
    @Override
    public List<BehaviorsReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BehaviorsReportValue> reportData = new ArrayList<BehaviorsReportValue>();

      while ( rs.next() )
      {
        BehaviorsReportValue reportValue = new BehaviorsReportValue();

        reportValue.setBehavior( rs.getString( "behavior" ) );
        reportValue.setbCnt( rs.getLong( "b_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
