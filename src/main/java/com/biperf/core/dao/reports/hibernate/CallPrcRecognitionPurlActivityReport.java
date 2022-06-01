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

import com.biperf.core.value.recognitionpurlactivity.RecognitionPurlActivityReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcRecognitionPurlActivityReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETSUMMARYTABULARRESULTS";
  private static final String PROC2 = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETPAXTABULARRESULTS";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETACTIVITYCHARTRESULTS";
  private static final String PROC4 = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETCONTRIBCHARTRESULTS";
  private static final String PROC5 = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETRECIPCHARTRESULTS";

  public CallPrcRecognitionPurlActivityReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlActivitySummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlSummaryTabularResultsTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlActivityParticipantMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlparticipantTabularResultsTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlActivityChartRowMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlActivityOverallContributorsChartRowMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionPurlActivityReport.RecognitionPurlRecipientsChartRowMapper() ) );
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
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_department", (String)reportParameters.get( "department" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_parentNodeId", (String)reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_promotionId", (String)reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortColName" ) );
    if ( ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  public class RecognitionPurlActivitySummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPurlActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPurlActivityReportValue> reportData = new ArrayList<RecognitionPurlActivityReportValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionPurlActivityReportValue( rs.getString( "NODE_NAME" ),
                                                                rs.getLong( "NODE_ID" ),
                                                                rs.getLong( "RECIPIENT_COUNT" ),
                                                                rs.getLong( "CONTRIBUTORS_INVITED" ),
                                                                rs.getLong( "CONTRIBUTED" ),
                                                                rs.getLong( "CONTRIBUTIONS_POSTED" ),
                                                                rs.getBigDecimal( "PERCENT_CONTRIBUTED" ),
                                                                rs.getInt( "TOTAL_RECORDS" ) ) );
      }
      return reportData;
    }
  }

  public class RecognitionPurlSummaryTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionPurlActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionPurlActivityReportValue( rs.getLong( "RECIPIENT_COUNT" ),
                                                       rs.getLong( "CONTRIBUTORS_INVITED" ),
                                                       rs.getLong( "CONTRIBUTED" ),
                                                       rs.getLong( "CONTRIBUTIONS_POSTED" ),
                                                       rs.getBigDecimal( "PERCENT_CONTRIBUTED" ) );
      }
      return null;
    }
  }

  public class RecognitionPurlActivityParticipantMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPurlActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPurlActivityReportValue> reportData = new ArrayList<RecognitionPurlActivityReportValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionPurlActivityReportValue( rs.getLong( "PURL_RECIPIENT_ID" ),
                                                                rs.getLong( "CONTRIBUTORS_INVITED" ),
                                                                rs.getLong( "ACTUAL_CONTRIBUTORS" ),
                                                                rs.getLong( "CONTRIBUTIONS_POSTED" ),
                                                                rs.getBigDecimal( "CONTRIBUTIONS_PERCENTAGE" ),
                                                                rs.getString( "PROMOTION_NAME" ),
                                                                rs.getString( "RECIPIENT_COUNTRY" ),
                                                                rs.getDate( "AWARD_DATE" ),
                                                                rs.getString( "PURL_STATUS" ),
                                                                rs.getString( "AWARD" ),
                                                                rs.getString( "RECIPIENT_NAME" ),
                                                                rs.getString( "MANAGER_NAME" ),
                                                                rs.getInt( "TOTAL_RECORDS" ) ) );
      }
      return reportData;
    }
  }

  public class RecognitionPurlparticipantTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionPurlActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionPurlActivityReportValue( rs.getLong( "CONTRIBUTORS_INVITED" ),
                                                       rs.getLong( "ACTUAL_CONTRIBUTORS" ),
                                                       rs.getLong( "CONTRIBUTIONS_POSTED" ),
                                                       rs.getBigDecimal( "CONTRIBUTIONS_PERCENTAGE" ) );
      }
      return null;
    }
  }

  public class RecognitionPurlActivityChartRowMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPurlActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPurlActivityReportValue> reportData = new ArrayList<RecognitionPurlActivityReportValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionPurlActivityReportValue( rs.getString( "NODE_NAME" ), rs.getLong( "CONTRIBUTORS_INVITED" ), rs.getLong( "CONTRIBUTED" ), rs.getLong( "NOT_CONTRIBUTED" ) ) );
      }
      return reportData;
    }
  }

  public class RecognitionPurlActivityOverallContributorsChartRowMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPurlActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPurlActivityReportValue> reportData = new ArrayList<RecognitionPurlActivityReportValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionPurlActivityReportValue( rs.getLong( "CONTRIBUTED" ), rs.getLong( "NOT_CONTRIBUTED" ) ) );
      }
      return reportData;
    }
  }

  public class RecognitionPurlRecipientsChartRowMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPurlActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPurlActivityReportValue> reportData = new ArrayList<RecognitionPurlActivityReportValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionPurlActivityReportValue( rs.getString( "NODE_NAME" ), rs.getLong( "RECIPIENT_COUNT" ) ) );
      }
      return reportData;
    }
  }

}
