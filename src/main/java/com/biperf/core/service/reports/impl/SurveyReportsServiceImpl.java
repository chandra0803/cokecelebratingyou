
package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.SurveyReportsDAO;
import com.biperf.core.service.reports.SurveyReportsService;

public class SurveyReportsServiceImpl implements SurveyReportsService
{
  private SurveyReportsDAO surveyReportsDAO;

  @Override
  public Map<String, Object> getSurveyStandardDeviationBarResults( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyStandardDeviationBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyTotalResponsePercentBarResults( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyTotalResponsePercentBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyAnalysisByOrgResults( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyAnalysisByOrgResults( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyAnalysisByQuestionsResults( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyAnalysisByQuestionsResults( reportParameters );
  }

  public void setSurveyReportsDAO( SurveyReportsDAO surveyReportsDAO )
  {
    this.surveyReportsDAO = surveyReportsDAO;
  }

  @Override
  public Map<String, Object> getSurveyResponseList( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyResponseList( reportParameters );
  }

  @Override
  public Map getSurveyAnalysisOptionsReportExtract( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyAnalysisOptionsReportExtract( reportParameters );
  }

  @Override
  public Map getSurveyAnalysisOpenEndedReportExtract( Map<String, Object> reportParameters )
  {
    return surveyReportsDAO.getSurveyAnalysisOpenEndedReportExtract( reportParameters );
  }

  @Override
  public boolean getSurveyResponseType( String promotionId )
  {
    return surveyReportsDAO.getSurveyResponseType( promotionId );
  }
}
