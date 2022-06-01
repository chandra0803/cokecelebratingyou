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

import com.biperf.core.value.quizanalysisreport.QuizAnalysisDetailOneReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAnalysisDetailTwoReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAnalysisSummaryReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAttemptStatusForOrgReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAttemptsPercentForOrgReportValue;
import com.biperf.core.value.quizanalysisreport.QuizQuestionAnalysisReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcQuizAnalysis extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZANALYSISSUMMARY";
  private static final String PROC2 = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZANALYSISDETAILONE";
  private static final String PROC3 = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZANALYSISDETAILTWO";

  /* Charts */
  private static final String PROC4 = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZQUESTIONANALYSISBAR";
  private static final String PROC5 = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZATTEMPTPERCENTFOROB";
  private static final String PROC6 = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZATTEMPTSTATUSFOROB";

  public CallPrcQuizAnalysis( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_diyQuizId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_qqId", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAnalysisSummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAnalysisSummaryTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAnalysisDetailOneMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAnalysisDetailOneTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAnalysisDetailTwoMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAnalysisDetailTwoTotalsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizQuestionAnalysisBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAttemptsPercentForOrgBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcQuizAnalysis.BasicQuizAttemptStatusForOrgBarMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_diyQuizId", reportParameters.get( "diyQuizId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    inParams.put( "p_in_qqId", reportParameters.get( "qqId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class BasicQuizAnalysisSummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizAnalysisSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizAnalysisSummaryReportValue> reportData = new ArrayList<QuizAnalysisSummaryReportValue>();

      while ( rs.next() )
      {
        QuizAnalysisSummaryReportValue reportValue = new QuizAnalysisSummaryReportValue();

        reportValue.setQuizId( rs.getLong( "quiz_id" ) );
        reportValue.setQuizName( rs.getString( "quiz_name" ) );
        reportValue.setQuizType( rs.getString( "quiz_type" ) );
        reportValue.setQuestionsInPoolCnt( rs.getLong( "ques_in_pool" ) );
        reportValue.setQuesToAskPerAttemptCnt( rs.getLong( "number_to_ask" ) );
        reportValue.setReqToPassCnt( rs.getLong( "req_to_pass" ) );
        reportValue.setQuizAttemptsCnt( rs.getLong( "quiz_attempts" ) );
        reportValue.setQuizAttemptsPct( rs.getDouble( "quiz_attempts_percent" ) );
        reportValue.setPassedAttemptsCnt( rs.getLong( "quiz_attempts_passed" ) );
        reportValue.setPassedAttemptsPct( rs.getDouble( "quiz_attempts_pass_perc" ) );
        reportValue.setFailedAttemptsCnt( rs.getLong( "quiz_attempts_failed" ) );
        reportValue.setFailedAttemptsPct( rs.getDouble( "quiz_attempts_fail_perc" ) );
        reportValue.setIncompleteAttemptsCnt( rs.getLong( "quiz_attempts_incomplete" ) );
        reportValue.setIncompleteAttemptsPct( rs.getDouble( "quiz_attempts_incompl_perc" ) );
        reportValue.setMaxAttemptsAllowedPerPaxCnt( rs.getString( "max_attempts_allowed_per_pax" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicQuizAnalysisSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public QuizAnalysisSummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      QuizAnalysisSummaryReportValue reportValue = new QuizAnalysisSummaryReportValue();
      while ( rs.next() )
      {
        reportValue.setQuestionsInPoolCnt( rs.getLong( "ques_in_pool" ) );
        reportValue.setQuesToAskPerAttemptCnt( rs.getLong( "number_to_ask" ) );
        reportValue.setReqToPassCnt( rs.getLong( "req_to_pass" ) );
        reportValue.setQuizAttemptsCnt( rs.getLong( "quiz_attempts" ) );
        reportValue.setQuizAttemptsPct( rs.getDouble( "quiz_attempts_percent" ) );
        reportValue.setPassedAttemptsCnt( rs.getLong( "quiz_attempts_passed" ) );
        reportValue.setPassedAttemptsPct( rs.getDouble( "quiz_attempts_pass_perc" ) );
        reportValue.setFailedAttemptsCnt( rs.getLong( "quiz_attempts_failed" ) );
        reportValue.setFailedAttemptsPct( rs.getDouble( "quiz_attempts_fail_perc" ) );
        reportValue.setIncompleteAttemptsCnt( rs.getLong( "quiz_attempts_incomplete" ) );
        reportValue.setIncompleteAttemptsPct( rs.getDouble( "quiz_attempts_incompl_perc" ) );
        reportValue.setMaxAttemptsAllowedPerPaxCnt( rs.getString( "max_attempts_allowed_per_pax" ) );
      }
      return reportValue;
    }

  }

  private class BasicQuizAnalysisDetailOneMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizAnalysisDetailOneReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizAnalysisDetailOneReportValue> reportData = new ArrayList<QuizAnalysisDetailOneReportValue>();

      while ( rs.next() )
      {
        QuizAnalysisDetailOneReportValue reportValue = new QuizAnalysisDetailOneReportValue();

        reportValue.setQqId( rs.getLong( "qq_id" ) );
        reportValue.setQuestion( rs.getString( "Question" ) );
        reportValue.setNbrOfTimesAsked( rs.getLong( "nbr_of_times_asked" ) );
        reportValue.setNbrCorrectResponses( rs.getLong( "nbr_correct_responses" ) );
        reportValue.setCorrectResponsesPct( rs.getDouble( "perc_correct_responses" ) );
        reportValue.setNbrIncorrectResponses( rs.getLong( "nbr_incorrect_responses" ) );
        reportValue.setIncorrectResponsesPct( rs.getDouble( "perc_incorrect_responses" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicQuizAnalysisDetailOneTotalsMapper implements ResultSetExtractor
  {
    @Override
    public QuizAnalysisDetailOneReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      QuizAnalysisDetailOneReportValue reportValue = new QuizAnalysisDetailOneReportValue();

      while ( rs.next() )
      {
        reportValue.setNbrOfTimesAsked( rs.getLong( "nbr_of_times_asked" ) );
        reportValue.setNbrCorrectResponses( rs.getLong( "nbr_correct_responses" ) );
        reportValue.setCorrectResponsesPct( rs.getDouble( "perc_correct_responses" ) );
        reportValue.setNbrIncorrectResponses( rs.getLong( "nbr_incorrect_responses" ) );
        reportValue.setIncorrectResponsesPct( rs.getDouble( "perc_incorrect_responses" ) );
      }
      return reportValue;
    }
  }

  private class BasicQuizAnalysisDetailTwoMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizAnalysisDetailTwoReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizAnalysisDetailTwoReportValue> reportData = new ArrayList<QuizAnalysisDetailTwoReportValue>();

      while ( rs.next() )
      {
        QuizAnalysisDetailTwoReportValue reportValue = new QuizAnalysisDetailTwoReportValue();
        reportValue.setResponse( rs.getString( "qqa_answer" ) );
        reportValue.setRightResponse( rs.getString( "is_correct" ) );
        reportValue.setTimesSelectedCnt( rs.getLong( "number_of_times_selected" ) );
        reportValue.setTimesSelectedPct( rs.getDouble( "perc_of_times_selected" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }
  }

  private class BasicQuizAnalysisDetailTwoTotalsMapper implements ResultSetExtractor
  {
    @Override
    public QuizAnalysisDetailTwoReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      QuizAnalysisDetailTwoReportValue reportValue = new QuizAnalysisDetailTwoReportValue();

      while ( rs.next() )
      {
        reportValue.setTimesSelectedCnt( rs.getLong( "number_of_times_selected" ) );
        reportValue.setTimesSelectedPct( rs.getDouble( "perc_of_times_selected" ) );
      }
      return reportValue;
    }
  }

  private class BasicQuizQuestionAnalysisBarMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizQuestionAnalysisReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizQuestionAnalysisReportValue> reportData = new ArrayList<QuizQuestionAnalysisReportValue>();

      while ( rs.next() )
      {
        QuizQuestionAnalysisReportValue reportValue = new QuizQuestionAnalysisReportValue();

        reportValue.setQuestion( rs.getString( "Question" ) );
        reportValue.setCorrectResponsesPct( rs.getDouble( "perc_correct_responses" ) );
        reportValue.setIncorrectResponsesPct( rs.getDouble( "perc_incorrect_responses" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicQuizAttemptsPercentForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizAttemptsPercentForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizAttemptsPercentForOrgReportValue> reportData = new ArrayList<QuizAttemptsPercentForOrgReportValue>();

      while ( rs.next() )
      {
        QuizAttemptsPercentForOrgReportValue reportValue = new QuizAttemptsPercentForOrgReportValue();

        reportValue.setQuizName( rs.getString( "quiz_name" ) );
        reportValue.setPassedAttemptsPct( rs.getDouble( "quiz_attempts_pass_perc" ) );
        reportValue.setFailedAttemptsPct( rs.getDouble( "quiz_attempts_fail_perc" ) );
        reportValue.setIncompleteAttemptsPct( rs.getDouble( "quiz_attempts_incompl_perc" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicQuizAttemptStatusForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<QuizAttemptStatusForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<QuizAttemptStatusForOrgReportValue> reportData = new ArrayList<QuizAttemptStatusForOrgReportValue>();

      while ( rs.next() )
      {
        QuizAttemptStatusForOrgReportValue reportValue = new QuizAttemptStatusForOrgReportValue();

        reportValue.setQuizName( rs.getString( "quiz_name" ) );
        reportValue.setPassedAttemptsCnt( rs.getLong( "quiz_attempts_passed" ) );
        reportValue.setFailedAttemptsCnt( rs.getLong( "quiz_attempts_failed" ) );
        reportValue.setIncompleteAttemptsCnt( rs.getLong( "quiz_attempts_incomplete" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

}
