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

import com.biperf.core.value.recognitionreceivedbypax.RecognitionPointsReceivedByParticipantValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionPointsReceivedByPromotionValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionSummaryReceivedByParticipantValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedByParticipantValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedByPromotionValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedMetricsValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedParticipationRateByPaxValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedScatterPlotValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcRecognitionReceivedByPaxReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETSUMMARYRECEIVEDBYPAX";
  private static final String PROC2 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETACTIVITYRECEIVEDBYPAX";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDBYPAX";
  private static final String PROC4 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETPOINTSRECEIVEDBYPAX";
  private static final String PROC5 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDBYPROMO";
  private static final String PROC6 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETPOINTSRECEIVEDBYPROMO";
  private static final String PROC7 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDMETRICS";
  private static final String PROC8 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETPARTICIPATIONRATEBYPAX";
  private static final String PROC9 = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDSCATTER";

  public CallPrcRecognitionReceivedByPaxReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_giverReceiver", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_userId", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionSummaryReceivedByParticipantMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.RecognitionReceivedByParticipantTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionActivityReceivedByParticipantMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionActivityReceivedByParticipantTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionsReceivedByParticipantBarMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionPointsReceivedByParticipantBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionsReceivedByPromotionBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionPointsReceivedByPromotionBarMapper() ) );
        break;
      case PROC7:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionsReceivedMetricsBarMapper() ) );
        break;
      case PROC8:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionsReceivedParticipationRateByPaxPieMapper() ) );
        break;
      case PROC9:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByPaxReport.BasicRecognitionsReceivedScatterPlotMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_giverReceiver", reportParameters.get( "giverReceiver" ) );
    inParams.put( "p_in_userId", reportParameters.get( "userId" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /* TABLES */

  private class BasicRecognitionSummaryReceivedByParticipantMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionSummaryReceivedByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionSummaryReceivedByParticipantValue> reportData = new ArrayList<RecognitionSummaryReceivedByParticipantValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionSummaryReceivedByParticipantValue( rs.getLong( "recvr_user_id" ),
                                                                          rs.getString( "receiver_name" ),
                                                                          rs.getString( "receiver_country" ),
                                                                          rs.getString( "receiver_node" ),
                                                                          rs.getLong( "organization_id" ),
                                                                          rs.getString( "recvr_department" ),
                                                                          rs.getString( "recvr_job_position" ),
                                                                          rs.getLong( "recog_count" ),
                                                                          rs.getLong( "recog_points" ),
                                                                          rs.getLong( "plateau_earned_count" ),
                                                                          rs.getInt( "total_records" ) ) );
      }
      return reportData;
    }
  }

  private class RecognitionReceivedByParticipantTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionSummaryReceivedByParticipantValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionSummaryReceivedByParticipantValue( rs.getLong( "recog_count" ), rs.getLong( "recog_points" ), rs.getLong( "plateau_earned_count" ) );
      }
      return null;
    }
  }

  private class BasicRecognitionActivityReceivedByParticipantMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionSummaryReceivedByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionSummaryReceivedByParticipantValue> reportData = new ArrayList<RecognitionSummaryReceivedByParticipantValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionSummaryReceivedByParticipantValue( rs.getString( "receiver_name" ),
                                                                          rs.getString( "receiver_country" ),
                                                                          rs.getString( "recvr_node_name" ),
                                                                          rs.getString( "department" ),
                                                                          rs.getString( "job_position" ),
                                                                          rs.getLong( "recognition_points" ),
                                                                          rs.getLong( "plateau_earned_count" ),
                                                                          rs.getInt( "total_records" ),
                                                                          rs.getDate( "date_approved" ),
                                                                          rs.getString( "promotion_name" ),
                                                                          rs.getLong( "claim_id" ),
                                                                          rs.getString( "sender_name" ) ) );
      }
      return reportData;
    }
  }

  private class BasicRecognitionActivityReceivedByParticipantTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionSummaryReceivedByParticipantValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionSummaryReceivedByParticipantValue( rs.getLong( "recognition_points" ), rs.getLong( "plateau_earned_count" ) );
      }
      return null;
    }
  }

  /* CHARTS */

  private class BasicRecognitionsReceivedByParticipantBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsReceivedByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsReceivedByParticipantValue> reportData = new ArrayList<RecognitionsReceivedByParticipantValue>();

      while ( rs.next() )
      {
        RecognitionsReceivedByParticipantValue reportValue = new RecognitionsReceivedByParticipantValue();

        reportValue.setPaxName( rs.getString( "RECEIVER_NAME" ) );
        reportValue.setRecognitionCnt( rs.getLong( "RECOGNITIONS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsReceivedByParticipantBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsReceivedByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsReceivedByParticipantValue> reportData = new ArrayList<RecognitionPointsReceivedByParticipantValue>();
      while ( rs.next() )
      {
        RecognitionPointsReceivedByParticipantValue reportValue = new RecognitionPointsReceivedByParticipantValue();

        reportValue.setPaxName( rs.getString( "RECEIVER_NAME" ) );
        reportValue.setRecognitionPointsCnt( rs.getLong( "RECOGNITION_POINTS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsReceivedByPromotionBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsReceivedByPromotionValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsReceivedByPromotionValue> reportData = new ArrayList<RecognitionsReceivedByPromotionValue>();

      while ( rs.next() )
      {
        RecognitionsReceivedByPromotionValue reportValue = new RecognitionsReceivedByPromotionValue();

        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setRecognitionCnt( rs.getLong( "RECOGNITIONS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsReceivedByPromotionBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsReceivedByPromotionValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsReceivedByPromotionValue> reportData = new ArrayList<RecognitionPointsReceivedByPromotionValue>();

      while ( rs.next() )
      {
        RecognitionPointsReceivedByPromotionValue reportValue = new RecognitionPointsReceivedByPromotionValue();

        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setRecognitionPointsCnt( rs.getLong( "RECOGNITION_POINTS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsReceivedMetricsBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsReceivedMetricsValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsReceivedMetricsValue> reportData = new ArrayList<RecognitionsReceivedMetricsValue>();

      while ( rs.next() )
      {
        RecognitionsReceivedMetricsValue reportValue = new RecognitionsReceivedMetricsValue();

        reportValue.setTopCnt( rs.getLong( "TOP_VALUE" ) );
        reportValue.setTotalCnt( rs.getLong( "TOTAL" ) );
        reportValue.setOverallOrgAvgCnt( rs.getDouble( "OVERALL_ORG_AVG" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsReceivedParticipationRateByPaxPieMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsReceivedParticipationRateByPaxValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsReceivedParticipationRateByPaxValue> reportData = new ArrayList<RecognitionsReceivedParticipationRateByPaxValue>();

      while ( rs.next() )
      {
        RecognitionsReceivedParticipationRateByPaxValue reportValue = new RecognitionsReceivedParticipationRateByPaxValue();

        reportValue.setHaveReceivedPct( rs.getDouble( "eligible_pct" ) );
        reportValue.setHaveNotReceivedPct( rs.getDouble( "not_eligible_pct" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsReceivedScatterPlotMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsReceivedScatterPlotValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsReceivedScatterPlotValue> reportData = new ArrayList<RecognitionsReceivedScatterPlotValue>();

      while ( rs.next() )
      {
        RecognitionsReceivedScatterPlotValue reportValue = new RecognitionsReceivedScatterPlotValue();

        reportValue.setParticipantName( rs.getString( "RECEIVER_NAME" ) );
        reportValue.setRecognitionCount( rs.getLong( "RECOGNITIONS_CNT" ) );
        reportValue.setDaysSinceLastRec( rs.getLong( "DAYS_SINCE_LAST_REC" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
