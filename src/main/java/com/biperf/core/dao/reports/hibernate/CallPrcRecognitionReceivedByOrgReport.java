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

import com.biperf.core.value.recognition.RecognitionPointsReceivedByTimeValue;
import com.biperf.core.value.recognition.RecognitionPointsReceivedValue;
import com.biperf.core.value.recognition.RecognitionReceivedByTimeValue;
import com.biperf.core.value.recognition.RecognitionReceivedDetailTotalsValue;
import com.biperf.core.value.recognition.RecognitionReceivedDetailValue;
import com.biperf.core.value.recognition.RecognitionReceivedParticipationRateBarValue;
import com.biperf.core.value.recognition.RecognitionReceivedParticipationRatePieValue;
import com.biperf.core.value.recognition.RecognitionReceivedSummaryValue;
import com.biperf.core.value.recognition.RecognitionReceivedValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcRecognitionReceivedByOrgReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_BYORGSUMMARYRESULTS";
  private static final String PROC2 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGDETAILRESULTS";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRECVDPIERESULTS";
  private static final String PROC4 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRECVDTIMERESULTS";
  private static final String PROC5 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRATEBARRESULTS";
  private static final String PROC6 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRATEPIERESULTS";
  private static final String PROC7 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGPOINTSBARRESULTS";
  private static final String PROC8 = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGPOINTSTIMERESULTS";

  public CallPrcRecognitionReceivedByOrgReport( DataSource ds, String STORED_PROC_NAME )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedSummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedSummaryTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedDetailMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedDetailTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedPieMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedByTimeBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedParticipationRateBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionReceivedParticipationRatePieMapper() ) );
        break;
      case PROC7:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionPointsReceivedBarMapper() ) );
        break;
      case PROC8:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcRecognitionReceivedByOrgReport.BasicRecognitionPointsReceivedByTimeBarMapper() ) );
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

  private class BasicRecognitionReceivedSummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionReceivedSummaryValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionReceivedSummaryValue> reportData = new ArrayList<RecognitionReceivedSummaryValue>();

      while ( rs.next() )
      {
        reportData.add( new RecognitionReceivedSummaryValue( rs.getString( "org_name" ),
                                                             rs.getLong( "node_id" ),
                                                             rs.getLong( "eligible_cnt" ),
                                                             rs.getLong( "actual_cnt" ),
                                                             rs.getDouble( "per_gave_receive" ),
                                                             rs.getLong( "total_recognition_cnt" ),
                                                             rs.getLong( "points" ),
                                                             rs.getLong( "plateau_earned" ),
                                                             rs.getLong( "sweepstakes_won" ),
                                                             rs.getInt( "total_records" ) ) );
      }

      return reportData;
    }
  }

  private class BasicRecognitionReceivedSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionReceivedSummaryValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      RecognitionReceivedSummaryValue reportValue = new RecognitionReceivedSummaryValue();

      while ( rs.next() )
      {
        reportValue.setEligibleCnt( rs.getLong( "eligible_cnt" ) );
        reportValue.setActualCnt( rs.getLong( "actual_cnt" ) );
        reportValue.setEligiblePct( rs.getDouble( "per_gave_receive" ) );
        reportValue.setTotalRecognition( rs.getLong( "total_recognition_cnt" ) );
        reportValue.setPoints( rs.getLong( "points" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "plateau_earned" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won" ) );
      }

      return reportValue;
    }
  }

  private class BasicRecognitionReceivedDetailMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionReceivedDetailValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionReceivedDetailValue> reportData = new ArrayList<RecognitionReceivedDetailValue>();
      while ( rs.next() )
      {
        reportData.add( new RecognitionReceivedDetailValue( rs.getLong( "recvr_user_id" ),
                                                            rs.getString( "receiver_name" ),
                                                            rs.getLong( "recognitions_cnt" ),
                                                            rs.getLong( "recognition_points" ),
                                                            rs.getLong( "plateau_earned_count" ),
                                                            rs.getLong( "sweepstakes_won_count" ),
                                                            rs.getInt( "total_records" ) ) );
      }
      return reportData;
    }
  }

  private class BasicRecognitionReceivedDetailTotalsMapper implements ResultSetExtractor
  {
    @Override
    public RecognitionReceivedDetailTotalsValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        return new RecognitionReceivedDetailTotalsValue( rs.getLong( "recognitions_cnt" ),
                                                         rs.getLong( "recognition_points" ),
                                                         rs.getLong( "plateau_earned_count" ),
                                                         rs.getLong( "sweepstakes_won_count" ) );
      }
      return null;
    }
  }

  private class BasicRecognitionReceivedPieMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionReceivedValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionReceivedValue> reportData = new ArrayList<RecognitionReceivedValue>();

      while ( rs.next() )
      {
        RecognitionReceivedValue reportValue = new RecognitionReceivedValue();

        reportValue.setNodeName( rs.getString( "node_name" ) );
        reportValue.setRecognitionCnt( rs.getLong( "recognition_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionReceivedByTimeBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionReceivedByTimeValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionReceivedByTimeValue> reportData = new ArrayList<RecognitionReceivedByTimeValue>();

      while ( rs.next() )
      {
        RecognitionReceivedByTimeValue reportValue = new RecognitionReceivedByTimeValue();

        reportValue.setMonthName( rs.getString( "month_name" ) );
        reportValue.setRecognitionCnt( rs.getLong( "recognition_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionReceivedParticipationRateBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionReceivedParticipationRateBarValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionReceivedParticipationRateBarValue> reportData = new ArrayList<RecognitionReceivedParticipationRateBarValue>();

      while ( rs.next() )
      {
        RecognitionReceivedParticipationRateBarValue reportValue = new RecognitionReceivedParticipationRateBarValue();

        reportValue.setHaveReceivedCnt( rs.getLong( "have_given_cnt" ) );
        reportValue.setHaveNotReceivedCnt( rs.getLong( "havenot_given_cnt" ) );
        reportValue.setNodeName( rs.getString( "org_name" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionReceivedParticipationRatePieMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionReceivedParticipationRatePieValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionReceivedParticipationRatePieValue> reportData = new ArrayList<RecognitionReceivedParticipationRatePieValue>();

      while ( rs.next() )
      {
        RecognitionReceivedParticipationRatePieValue reportValue = new RecognitionReceivedParticipationRatePieValue();

        reportValue.setHaveReceivedPct( rs.getDouble( "eligible_pct" ) );
        reportValue.setHaveNotReceivedPct( rs.getDouble( "not_eligible_pct" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsReceivedBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsReceivedValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsReceivedValue> reportData = new ArrayList<RecognitionPointsReceivedValue>();

      while ( rs.next() )
      {
        RecognitionPointsReceivedValue reportValue = new RecognitionPointsReceivedValue();

        reportValue.setNodeName( rs.getString( "node_name" ) );
        reportValue.setRecognitionPointCnt( rs.getLong( "points" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicRecognitionPointsReceivedByTimeBarMapper implements ResultSetExtractor
  {
    @Override
    public List<RecognitionPointsReceivedByTimeValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionPointsReceivedByTimeValue> reportData = new ArrayList<RecognitionPointsReceivedByTimeValue>();

      while ( rs.next() )
      {
        RecognitionPointsReceivedByTimeValue reportValue = new RecognitionPointsReceivedByTimeValue();

        reportValue.setMonthName( rs.getString( "MONTH_NAME" ) );
        reportValue.setRecognitionPointCnt( rs.getLong( "POINTS" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
