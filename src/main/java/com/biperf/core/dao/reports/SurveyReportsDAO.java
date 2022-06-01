
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

public interface SurveyReportsDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "surveyReportsDAO";

  public Map<String, Object> getSurveyStandardDeviationBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyTotalResponsePercentBarResults( Map<String, Object> reportParameters );

  public Map getSurveyAnalysisOptionsReportExtract( Map<String, Object> reportParameters );

  public Map getSurveyAnalysisOpenEndedReportExtract( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyAnalysisByOrgResults( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyAnalysisByQuestionsResults( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyResponseList( Map<String, Object> reportParameters );

  public boolean getSurveyResponseType( String promotionId );

}
