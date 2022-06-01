
package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface WorkHappierReportsService extends SAO
{
  public static String BEAN_NAME = "workHappierReportsService";

  public Map<String, Object> getConfidentialFeedbackReportExtract( Map<String, Object> reportParameters );

  public Map<String, Object> getHappinessPulseReportExtract( Map<String, Object> reportParameters );

  public void generateAndSendEmailWorkHappierReportExtract( Map<String, Object> extractParameters ) throws Exception;

}
