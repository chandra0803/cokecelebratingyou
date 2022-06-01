/**
 * 
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.QuizReportsDAO;
import com.biperf.core.service.reports.QuizReportsService;

/**
 * @author poddutur
 *
 */
public class QuizReportsServiceImpl implements QuizReportsService
{
  private QuizReportsDAO quizReportsDAO;

  @Override
  public Map<String, Object> getQuizActivityForOrgBarResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizActivityForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizStatusPercentForOrgBarResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizStatusPercentForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizActivitySummaryResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizActivitySummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizActivityDetailOneResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizActivityDetailOneResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizActivityDetailTwoResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizActivityDetailTwoResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizQuestionAnalysisBarResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizQuestionAnalysisBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAttemptsPercentForOrgBarResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizAttemptsPercentForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAttemptStatusForOrgBarResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizAttemptStatusForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAnalysisSummaryResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizAnalysisSummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAnalysisDetailOneResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizAnalysisDetailOneResults( reportParameters );
  }

  @Override
  public Map<String, Object> getQuizAnalysisDetailTwoResults( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizAnalysisDetailTwoResults( reportParameters );
  }

  @Override
  public Map getQuizActivityReportExtract( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizActivityReportExtract( reportParameters );
  }

  @Override
  public Map getQuizAnalysisReportExtract( Map<String, Object> reportParameters )
  {
    return quizReportsDAO.getQuizAnalysisReportExtract( reportParameters );
  }

  public void setQuizReportsDAO( QuizReportsDAO quizReportsDAO )
  {
    this.quizReportsDAO = quizReportsDAO;
  }

}
