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

import com.biperf.core.value.recognition.RecognitionGivenByTimeValue;
import com.biperf.core.value.recognition.RecognitionGivenDetailTotalsValue;
import com.biperf.core.value.recognition.RecognitionGivenDetailValue;
import com.biperf.core.value.recognition.RecognitionGivenParticipationRateBarValue;
import com.biperf.core.value.recognition.RecognitionGivenParticipationRatePieValue;
import com.biperf.core.value.recognition.RecognitionPointsGivenByTimeValue;
import com.biperf.core.value.recognition.RecognitionPointsGivenValue;
import com.biperf.core.value.recognition.RecognitionSummaryValue;
import com.biperf.core.value.recognition.RecognitionValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcRecognitionGivenByOrgReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_BYORGSUMMARYRESULTS";
  private static final String PROC2 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGDETAILRESULTS";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGGIVENPIERESULTS";
  private static final String PROC4 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGGIVENTIMERESULTS";
  private static final String PROC5 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGRATEBARRESULTS";
  private static final String PROC6 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGRATEPIERESULTS";
  private static final String PROC7 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGPOINTSBARRESULTS";
  private static final String PROC8 = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGPOINTSTIMERESULTS";

  public CallPrcRecognitionGivenByOrgReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobposition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionstatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenSummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenSummaryTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenDetailMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenDetailTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenPieMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenByTimeBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenParticipationRateBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionGivenParticipationRatePieMapper() ) );
        break;
      case PROC7:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionPointsGivenBarMapper() ) );
        break;
      case PROC8:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionGivenByOrgReport.BasicRecognitionPointsGivenByTimeBarMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_jobposition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_promotionstatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
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

  /* TABLES */

  private class BasicRecognitionGivenSummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionSummaryValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionSummaryValue> reportData = new ArrayList<RecognitionSummaryValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionSummaryValue( rs.getString( "org_name" ),
                                                     rs.getLong( "node_id" ),
                                                     rs.getLong( "eligible_cnt" ),
                                                     rs.getLong( "actual_cnt" ),
                                                     rs.getBigDecimal( "per_gave_receive" ),
                                                     rs.getLong( "total_recognition_cnt" ),
                                                     rs.getLong( "points" ),
                                                     rs.getLong( "plateau_earned" ),
                                                     rs.getLong( "sweepstakes_won" ),
                                                     rs.getInt( "total_records" ) ) );
      }

      return reportData;
    }
  }

  private class BasicRecognitionGivenSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionSummaryValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionSummaryValue( rs.getLong( "eligible_cnt" ),
                                            rs.getLong( "actual_cnt" ),
                                            rs.getBigDecimal( "per_gave_receive" ),
                                            rs.getLong( "total_recognition_cnt" ),
                                            rs.getLong( "points" ),
                                            rs.getLong( "plateau_earned" ),
                                            rs.getLong( "sweepstakes_won" ) );
      }
      return null;
    }
  }

  private class BasicRecognitionGivenDetailMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionGivenDetailValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionGivenDetailValue> reportData = new ArrayList<RecognitionGivenDetailValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionGivenDetailValue( rs.getLong( "giver_user_id" ),
                                                         rs.getString( "giver_name" ),
                                                         rs.getLong( "recognitions_cnt" ),
                                                         rs.getLong( "recognition_points" ),
                                                         rs.getLong( "plateau_earned_count" ),
                                                         rs.getLong( "sweepstakes_won_count" ),
                                                         rs.getInt( "total_records" ) ) );
      }

      return reportData;
    }
  }

  private class BasicRecognitionGivenDetailTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionGivenDetailTotalsValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionGivenDetailTotalsValue( rs.getLong( "recognitions_cnt" ),
                                                      rs.getLong( "recognition_points" ),
                                                      rs.getLong( "plateau_earned_count" ),
                                                      rs.getLong( "sweepstakes_won_count" ) );
      }
      return null;
    }
  }

  /* CHARTS */

  private class BasicRecognitionGivenPieMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionValue> reportData = new ArrayList<RecognitionValue>();
      while ( rs.next() )
      {
        RecognitionValue reportValue = new RecognitionValue();

        reportValue.setNodeName( rs.getString( "node_name" ) );
        reportValue.setRecognitionCnt( rs.getLong( "recognition_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionGivenByTimeBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionGivenByTimeValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionGivenByTimeValue> reportData = new ArrayList<RecognitionGivenByTimeValue>();
      while ( rs.next() )
      {
        RecognitionGivenByTimeValue reportValue = new RecognitionGivenByTimeValue();

        reportValue.setMonthName( rs.getString( "month_name" ) );
        reportValue.setRecognitionCnt( rs.getLong( "recognition_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionGivenParticipationRateBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionGivenParticipationRateBarValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionGivenParticipationRateBarValue> reportData = new ArrayList<RecognitionGivenParticipationRateBarValue>();

      while ( rs.next() )
      {
        RecognitionGivenParticipationRateBarValue reportValue = new RecognitionGivenParticipationRateBarValue();

        reportValue.setHaveGivenCnt( rs.getLong( "have_given_cnt" ) );
        reportValue.setHaveNotGivenCnt( rs.getLong( "havenot_given_cnt" ) );
        reportValue.setNodeName( rs.getString( "org_name" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionGivenParticipationRatePieMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionGivenParticipationRatePieValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionGivenParticipationRatePieValue> reportData = new ArrayList<RecognitionGivenParticipationRatePieValue>();

      while ( rs.next() )
      {
        RecognitionGivenParticipationRatePieValue reportValue = new RecognitionGivenParticipationRatePieValue();

        reportValue.setHaveGivenPct( rs.getDouble( "eligible_pct" ) );
        reportValue.setHaveNotGivenPct( rs.getDouble( "not_eligible_pct" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsGivenBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsGivenValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsGivenValue> reportData = new ArrayList<RecognitionPointsGivenValue>();

      while ( rs.next() )
      {
        RecognitionPointsGivenValue reportValue = new RecognitionPointsGivenValue();

        reportValue.setNodeName( rs.getString( "node_name" ) );
        reportValue.setRecognitionPointCnt( rs.getLong( "points" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsGivenByTimeBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsGivenByTimeValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsGivenByTimeValue> reportData = new ArrayList<RecognitionPointsGivenByTimeValue>();

      while ( rs.next() )
      {
        RecognitionPointsGivenByTimeValue reportValue = new RecognitionPointsGivenByTimeValue();

        reportValue.setMonthName( rs.getString( "MONTH_NAME" ) );
        reportValue.setRecognitionPointCnt( rs.getLong( "POINTS" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
