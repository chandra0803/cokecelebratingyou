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

import com.biperf.core.value.quizactivityreport.QuizActivityDetailOneReportValue;
import com.biperf.core.value.quizactivityreport.QuizActivityDetailTwoReportValue;
import com.biperf.core.value.quizactivityreport.QuizActivityForOrgReportValue;
import com.biperf.core.value.quizactivityreport.QuizActivitySummaryReportValue;
import com.biperf.core.value.quizactivityreport.QuizStatusPercentForOrgReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcQuizActivity extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYSUMMARY";
  private static final String PROC2 = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYDETAILONE";
  private static final String PROC3 = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYDETAILTWO";

  /* Charts */
  private static final String PROC4 = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYFORORGBAR";
  private static final String PROC5 = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZSTATUSPERCENTFORORB";

  public CallPrcQuizActivity( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_diyQuizId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_result", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_paxId", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivitySummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivitySummaryTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivityDetailOneMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivityDetailOneTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivityDetailTwoMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivityDetailTwoTotalsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizActivityForOrgBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizActivity.BasicQuizStatusPercentForOrgBarMapper() ) );
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
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
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
    inParams.put( "p_in_paxId", reportParameters.get( "paxId" ) );
    inParams.put( "p_in_result", reportParameters.get( "result" ) );
    inParams.put( "p_in_diyQuizId", reportParameters.get( "diyQuizId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class BasicQuizActivitySummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizActivitySummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizActivitySummaryReportValue> reportData = new ArrayList<QuizActivitySummaryReportValue>();

      while ( rs.next() )
      {
        QuizActivitySummaryReportValue reportValue = new QuizActivitySummaryReportValue();

        reportValue.setOrgName( rs.getString( "Org_Name" ) );
        reportValue.setEligibleQuizzesCnt( rs.getLong( "Eligible_Quizzes" ) );
        reportValue.setQuizAttemptsCnt( rs.getLong( "Quiz_Attempts" ) );
        reportValue.setQuizAttemptsInProgressCnt( rs.getLong( "Attempts_In_Progress" ) );
        reportValue.setAttemptsFailedCnt( rs.getLong( "Attempts_Failed" ) );
        reportValue.setAttemptsPassedCnt( rs.getLong( "Attempts_Passed" ) );
        reportValue.setEligiblePassedPct( rs.getDouble( "Eligible_Passed_Pct" ) );
        reportValue.setEligibleFailedPct( rs.getDouble( "Eligible_Failed_Pct" ) );
        reportValue.setPointsCnt( rs.getLong( "Points" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won_cnt" ) );
        reportValue.setNodeId( rs.getLong( "Node_Id" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicQuizActivitySummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public QuizActivitySummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      QuizActivitySummaryReportValue reportValue = new QuizActivitySummaryReportValue();

      while ( rs.next() )
      {
        reportValue.setEligibleQuizzesCnt( rs.getLong( "Eligible_Quizzes" ) );
        reportValue.setQuizAttemptsCnt( rs.getLong( "Quiz_Attempts" ) );
        reportValue.setQuizAttemptsInProgressCnt( rs.getLong( "Attempts_In_Progress" ) );
        reportValue.setAttemptsFailedCnt( rs.getLong( "Attempts_Failed" ) );
        reportValue.setAttemptsPassedCnt( rs.getLong( "Attempts_Passed" ) );
        reportValue.setEligiblePassedPct( rs.getDouble( "Eligible_Passed_Pct" ) );
        reportValue.setEligibleFailedPct( rs.getDouble( "Eligible_Failed_Pct" ) );
        reportValue.setPointsCnt( rs.getLong( "Points" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won_cnt" ) );
      }

      return reportValue;
    }

  }

  private class BasicQuizActivityDetailOneMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizActivityDetailOneReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizActivityDetailOneReportValue> reportData = new ArrayList<QuizActivityDetailOneReportValue>();

      while ( rs.next() )
      {
        QuizActivityDetailOneReportValue reportValue = new QuizActivityDetailOneReportValue();

        reportValue.setPaxName( rs.getString( "participant_name" ) );
        reportValue.setCountry( rs.getString( "COUNTRY" ) );
        reportValue.setEligibleQuizzesCnt( rs.getLong( "eligible_quizzes_cnt" ) );
        reportValue.setQuizAttemptsCnt( rs.getLong( "quiz_attempts_cnt" ) );
        reportValue.setQuizAttemptsInProgressCnt( rs.getLong( "attempts_in_progress_cnt" ) );
        reportValue.setAttemptsFailedCnt( rs.getLong( "attempts_failed_cnt" ) );
        reportValue.setAttemptsPassedCnt( rs.getLong( "attempts_passed_cnt" ) );
        reportValue.setPointsCnt( rs.getLong( "points_cnt" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won_cnt" ) );
        reportValue.setParticipantId( rs.getLong( "participant_id" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicQuizActivityDetailOneTotalsMapper implements ResultSetExtractor
  {
    @Override
    public QuizActivityDetailOneReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      QuizActivityDetailOneReportValue reportValue = new QuizActivityDetailOneReportValue();

      while ( rs.next() )
      {
        reportValue.setEligibleQuizzesCnt( rs.getLong( "eligible_quizzes_cnt" ) );
        reportValue.setQuizAttemptsCnt( rs.getLong( "quiz_attempts_cnt" ) );
        reportValue.setQuizAttemptsInProgressCnt( rs.getLong( "quiz_attempts_in_progress_cnt" ) );
        reportValue.setAttemptsFailedCnt( rs.getLong( "attempts_failed_cnt" ) );
        reportValue.setAttemptsPassedCnt( rs.getLong( "attempts_passed_cnt" ) );
        reportValue.setPointsCnt( rs.getLong( "points_cnt" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won_cnt" ) );
      }
      return reportValue;
    }

  }

  private class BasicQuizActivityDetailTwoMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizActivityDetailTwoReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizActivityDetailTwoReportValue> reportData = new ArrayList<QuizActivityDetailTwoReportValue>();

      while ( rs.next() )
      {
        QuizActivityDetailTwoReportValue reportValue = new QuizActivityDetailTwoReportValue();

        reportValue.setQuizDate( rs.getDate( "q_date" ) );
        reportValue.setCompletedByName( rs.getString( "completed_by" ) );
        reportValue.setOrgName( rs.getString( "org_name" ) );
        reportValue.setPromotionName( rs.getString( "promotion" ) );
        reportValue.setScorePassing( rs.getString( "score_passing" ) );
        reportValue.setQuizResult( rs.getString( "results" ) );
        reportValue.setPointsCnt( rs.getLong( "points_cnt" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won_cnt" ) );
        reportValue.setCertificate( rs.getLong( "certificate" ) );
        reportValue.setClaimId( rs.getLong( "claim_id" ) );
        reportValue.setParticipantId( rs.getLong( "participant_id" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  private class BasicQuizActivityDetailTwoTotalsMapper implements ResultSetExtractor
  {
    @Override
    public QuizActivityDetailTwoReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      QuizActivityDetailTwoReportValue reportValue = new QuizActivityDetailTwoReportValue();
      while ( rs.next() )
      {
        reportValue.setPointsCnt( rs.getLong( "points_cnt" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won_cnt" ) );
        reportValue.setCertificate( rs.getLong( "certificate_cnt" ) );
      }
      return reportValue;
    }

  }

  private class BasicQuizActivityForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizActivityForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizActivityForOrgReportValue> reportData = new ArrayList<QuizActivityForOrgReportValue>();

      while ( rs.next() )
      {
        QuizActivityForOrgReportValue reportValue = new QuizActivityForOrgReportValue();

        reportValue.setOrgName( rs.getString( "Org_Name" ) );
        reportValue.setNodeId( rs.getLong( "Node_Id" ) );
        reportValue.setAttemptsFailed( rs.getLong( "Attempts_Failed" ) );
        reportValue.setAttemptsPassed( rs.getLong( "Attempts_Passed" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicQuizStatusPercentForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizStatusPercentForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizStatusPercentForOrgReportValue> reportData = new ArrayList<QuizStatusPercentForOrgReportValue>();

      while ( rs.next() )
      {
        QuizStatusPercentForOrgReportValue reportValue = new QuizStatusPercentForOrgReportValue();

        reportValue.setOrgName( rs.getString( "Org_Name" ) );
        reportValue.setNodeId( rs.getLong( "Node_Id" ) );
        reportValue.setFailedPct( rs.getLong( "Failed_Pct" ) );
        reportValue.setPassedPct( rs.getLong( "Passed_Pct" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

}
