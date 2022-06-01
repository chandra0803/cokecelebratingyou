/**
 * 
 */

package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * @author poddutur
 *
 */
public interface QuizReportsDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "quizReportsDAO";

  public Map<String, Object> getQuizActivityForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizStatusPercentForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizQuestionAnalysisBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAttemptsPercentForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAttemptStatusForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizActivitySummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizActivityDetailOneResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizActivityDetailTwoResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAnalysisSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAnalysisDetailOneResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAnalysisDetailTwoResults( Map<String, Object> reportParameters );

  public Map getQuizActivityReportExtract( Map<String, Object> reportParameters );

  public Map getQuizAnalysisReportExtract( Map<String, Object> reportParameters );

}
