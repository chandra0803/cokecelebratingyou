
package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface SurveyReportsService extends SAO
{
  public static String BEAN_NAME = "surveyReportsService";

  public Map<String, Object> getSurveyStandardDeviationBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyTotalResponsePercentBarResults( Map<String, Object> reportParameters );

  public Map getSurveyAnalysisOptionsReportExtract( Map<String, Object> reportParameters );

  public Map getSurveyAnalysisOpenEndedReportExtract( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyAnalysisByOrgResults( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyAnalysisByQuestionsResults( Map<String, Object> reportParameters );

  public Map<String, Object> getSurveyResponseList( Map<String, Object> reportParameters );

  public boolean getSurveyResponseType( String promotionId );

}
