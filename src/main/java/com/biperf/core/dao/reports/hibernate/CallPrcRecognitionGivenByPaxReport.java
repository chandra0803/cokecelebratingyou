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

import com.biperf.core.value.recognitionbypax.RecognitionPointsGivenByParticipantValue;
import com.biperf.core.value.recognitionbypax.RecognitionPointsGivenByPromotionValue;
import com.biperf.core.value.recognitionbypax.RecognitionSummaryGivenByParticipantValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenByParticipantValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenByPromotionValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenMetricsValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenParticipationRateByPaxValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcRecognitionGivenByPaxReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETSUMMARYGIVENBYPAX";
  private static final String PROC2 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETACTIVITYGIVENBYPAX";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETRECOGSGIVENBYPAX";
  private static final String PROC4 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETPOINTSGIVENBYPAX";
  private static final String PROC5 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETRECOGSGIVENBYPROMO";
  private static final String PROC6 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETPOINTSGIVENBYPROMO";
  private static final String PROC7 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETMETRICS";
  private static final String PROC8 = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETPARTICIPATIONRATEBYPAX";

  public CallPrcRecognitionGivenByPaxReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.

    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_giverReceiver", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_userId", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionSummaryGivenByParticipantMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionSummaryGivenByParticipantTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionActivityGivenByParticipantMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionActivityGivenByParticipantTotalMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionsGivenByParticipantBarMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionPointsGivenByParticipantBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionsGivenByPromotionBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionPointsGivenByPromotionBarMapper() ) );
        break;
      case PROC7:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionsGivenMetricsBarMapper() ) );
        break;
      case PROC8:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByPaxReport.BasicRecognitionsGivenParticipationRateByPaxPieMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_giverReceiver", reportParameters.get( "giverReceiver" ) );
    inParams.put( "p_in_userId", reportParameters.get( "userId" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class BasicRecognitionSummaryGivenByParticipantMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionSummaryGivenByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionSummaryGivenByParticipantValue> reportData = new ArrayList<RecognitionSummaryGivenByParticipantValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionSummaryGivenByParticipantValue( rs.getLong( "giver_user_id" ),
                                                                       rs.getString( "giver_name" ),
                                                                       rs.getString( "giver_country" ),
                                                                       rs.getString( "giver_node" ),
                                                                       rs.getLong( "organization_id" ),
                                                                       rs.getString( "giver_department" ),
                                                                       rs.getString( "giver_job_position" ),
                                                                       rs.getLong( "recog_count" ),
                                                                       rs.getLong( "recog_points" ),
                                                                       rs.getLong( "plateau_earned_count" ),
                                                                       rs.getInt( "total_records" ) ) );
      }
      return reportData;
    }
  }

  private class BasicRecognitionSummaryGivenByParticipantTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionSummaryGivenByParticipantValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionSummaryGivenByParticipantValue( rs.getLong( "recog_count" ), rs.getLong( "recog_points" ), rs.getLong( "plateau_earned_count" ) );
      }
      return null;
    }
  }

  private class BasicRecognitionActivityGivenByParticipantMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionSummaryGivenByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionSummaryGivenByParticipantValue> reportData = new ArrayList<RecognitionSummaryGivenByParticipantValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionSummaryGivenByParticipantValue( rs.getString( "giver_name" ),
                                                                       rs.getString( "giver_country" ),
                                                                       rs.getString( "giver_node_name" ),
                                                                       rs.getString( "department" ),
                                                                       rs.getString( "job_position" ),
                                                                       rs.getLong( "recognition_points" ),
                                                                       rs.getLong( "plateau_earned_count" ),
                                                                       rs.getInt( "total_records" ),
                                                                       rs.getDate( "date_approved" ),
                                                                       rs.getString( "promotion_name" ),
                                                                       rs.getLong( "claim_id" ) ) );
      }
      return reportData;
    }
  }

  private class BasicRecognitionActivityGivenByParticipantTotalMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionSummaryGivenByParticipantValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionSummaryGivenByParticipantValue( rs.getLong( "recognition_points" ), rs.getLong( "plateau_earned_count" ) );
      }
      return null;
    }
  }

  private class BasicRecognitionsGivenByParticipantBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsGivenByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsGivenByParticipantValue> reportData = new ArrayList<RecognitionsGivenByParticipantValue>();

      while ( rs.next() )
      {
        RecognitionsGivenByParticipantValue reportValue = new RecognitionsGivenByParticipantValue();

        reportValue.setPaxName( rs.getString( "GIVER_NAME" ) );
        reportValue.setRecognitionCnt( rs.getLong( "RECOGNITIONS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsGivenByParticipantBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsGivenByParticipantValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsGivenByParticipantValue> reportData = new ArrayList<RecognitionPointsGivenByParticipantValue>();

      while ( rs.next() )
      {
        RecognitionPointsGivenByParticipantValue reportValue = new RecognitionPointsGivenByParticipantValue();

        reportValue.setPaxName( rs.getString( "GIVER_NAME" ) );
        reportValue.setRecognitionPointsCnt( rs.getLong( "RECOGNITION_POINTS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsGivenByPromotionBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsGivenByPromotionValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsGivenByPromotionValue> reportData = new ArrayList<RecognitionsGivenByPromotionValue>();

      while ( rs.next() )
      {
        RecognitionsGivenByPromotionValue reportValue = new RecognitionsGivenByPromotionValue();

        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setRecognitionCnt( rs.getLong( "RECOGNITIONS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsGivenByPromotionBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsGivenByPromotionValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsGivenByPromotionValue> reportData = new ArrayList<RecognitionPointsGivenByPromotionValue>();

      while ( rs.next() )
      {
        RecognitionPointsGivenByPromotionValue reportValue = new RecognitionPointsGivenByPromotionValue();

        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setRecognitionPointsCnt( rs.getLong( "RECOGNITION_POINTS_CNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsGivenMetricsBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsGivenMetricsValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsGivenMetricsValue> reportData = new ArrayList<RecognitionsGivenMetricsValue>();

      while ( rs.next() )
      {
        RecognitionsGivenMetricsValue reportValue = new RecognitionsGivenMetricsValue();

        reportValue.setTopCnt( rs.getLong( "TOP_VALUE" ) );
        reportValue.setTotalCnt( rs.getLong( "TOTAL" ) );
        reportValue.setOverallOrgAvgCnt( rs.getDouble( "OVERALL_ORG_AVG" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionsGivenParticipationRateByPaxPieMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionsGivenParticipationRateByPaxValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionsGivenParticipationRateByPaxValue> reportData = new ArrayList<RecognitionsGivenParticipationRateByPaxValue>();

      while ( rs.next() )
      {
        RecognitionsGivenParticipationRateByPaxValue reportValue = new RecognitionsGivenParticipationRateByPaxValue();

        reportValue.setHaveGivenUniquePct( rs.getDouble( "eligible_pct" ) );
        reportValue.setHaveNotGivenUniquePct( rs.getDouble( "not_eligible_pct" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
