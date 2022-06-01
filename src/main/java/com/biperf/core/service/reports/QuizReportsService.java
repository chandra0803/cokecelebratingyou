/**
 * 
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * @author poddutur
 *
 */
public interface QuizReportsService extends SAO
{
  /**
     * BEAN_NAME is for applicationContext.xml reference
     */
  public static String BEAN_NAME = "quizReportsService";

  public Map<String, Object> getQuizActivityForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizStatusPercentForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizActivitySummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizActivityDetailOneResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizActivityDetailTwoResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizQuestionAnalysisBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAttemptsPercentForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAttemptStatusForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAnalysisSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAnalysisDetailOneResults( Map<String, Object> reportParameters );

  public Map<String, Object> getQuizAnalysisDetailTwoResults( Map<String, Object> reportParameters );

  public Map getQuizActivityReportExtract( Map<String, Object> reportParameters );

  public Map getQuizAnalysisReportExtract( Map<String, Object> reportParameters );

}
