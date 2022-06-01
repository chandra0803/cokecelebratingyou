/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.QuizReportsDAO;

/**
 * @author poddutur
 *
 */
public class QuizReportsDAOImpl extends BaseReportsDAO implements QuizReportsDAO
{
  private static final Log log = LogFactory.getLog( QuizReportsDAOImpl.class );

  private DataSource dataSource;

  /* QUIZ ACTIVITY */

  @Override
  public Map<String, Object> getQuizActivityForOrgBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYFORORGBAR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcQuizActivity procedure = new CallPrcQuizActivity( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizStatusPercentForOrgBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZSTATUSPERCENTFORORB";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcQuizActivity procedure = new CallPrcQuizActivity( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizActivitySummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYSUMMARY";
    String sortColName = "Org_Name";
    String[] sortColNames = { "Org_Name",
                              "",
                              "Eligible_Quizzes",
                              "Quiz_Attempts",
                              "Attempts_In_Progress",
                              "Attempts_Failed",
                              "Attempts_Passed",
                              "Eligible_Passed_Pct",
                              "Eligible_Failed_Pct",
                              "Points",
                              "sweepstakes_won_cnt" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcQuizActivity procedure = new CallPrcQuizActivity( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizActivityDetailOneResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYDETAILONE";
    String sortColName = "participant_name";
    String[] sortColNames = { "participant_name",
                              "",
                              "country",
                              "eligible_quizzes_cnt",
                              "quiz_attempts_cnt",
                              "attempts_in_progress_cnt",
                              "attempts_failed_cnt",
                              "attempts_passed_cnt",
                              "points_cnt",
                              "sweepstakes_won_cnt" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcQuizActivity procedure = new CallPrcQuizActivity( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizActivityDetailTwoResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ACTIVITY.PRC_GETQUIZACTIVITYDETAILTWO";
    String sortColName = "completed_by";
    String[] sortColNames = { "q_date", "", "completed_by", "org_name", "promotion", "score_passing", "results", "points_cnt", "sweepstakes_won_cnt", "certificate" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcQuizActivity procedure = new CallPrcQuizActivity( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* QUIZ ANALYSIS */

  @Override
  public Map<String, Object> getQuizQuestionAnalysisBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZQUESTIONANALYSISBAR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcQuizAnalysis procedure = new CallPrcQuizAnalysis( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAttemptsPercentForOrgBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZATTEMPTPERCENTFOROB";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcQuizAnalysis procedure = new CallPrcQuizAnalysis( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAttemptStatusForOrgBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZATTEMPTSTATUSFOROB";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcQuizAnalysis procedure = new CallPrcQuizAnalysis( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAnalysisSummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZANALYSISSUMMARY";
    String sortColName = "quiz_name";
    String[] sortColNames = { "quiz_name",
                              "",
                              "quiz_type",
                              "ques_in_pool",
                              "number_to_ask",
                              "req_to_pass",
                              "quiz_attempts",
                              "quiz_attempts_percent",
                              "quiz_attempts_passed",
                              "quiz_attempts_pass_perc",
                              "quiz_attempts_failed",
                              "quiz_attempts_fail_perc",
                              "quiz_attempts_incomplete",
                              "quiz_attempts_incompl_perc",
                              "max_attempts_allowed_per_pax" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcQuizAnalysis procedure = new CallPrcQuizAnalysis( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAnalysisDetailOneResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZANALYSISDETAILONE";
    String sortColName = "Question";
    String[] sortColNames = { "Question", "", "nbr_of_times_asked", "nbr_correct_responses", "perc_correct_responses", "nbr_incorrect_responses", "perc_incorrect_responses" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcQuizAnalysis procedure = new CallPrcQuizAnalysis( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAnalysisDetailTwoResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_QUIZ_ANALYSIS.PRC_GETQUIZANALYSISDETAILTWO";
    String sortColName = "qqa_answer";
    String[] sortColNames = { "qqa_answer", "is_correct", "number_of_times_selected", "perc_of_times_selected" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcQuizAnalysis procedure = new CallPrcQuizAnalysis( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getQuizActivityReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcQuizActivityExtract procedure = new CallPrcQuizActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getQuizAnalysisReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcQuizAnalysisExtract procedure = new CallPrcQuizAnalysisExtract( dataSource );
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
