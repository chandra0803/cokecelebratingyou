
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

public interface WorkHappierReportsDAO extends DAO
{
  public static final String BEAN_NAME = "workHappierReportsDAO";

  public Map<String, Object> getConfidentialFeedbackReportExtract( Map<String, Object> reportParameters );

  public Map<String, Object> getHappinessPulseReportExtract( Map<String, Object> reportParameters );

  public Map<String, Object> getWorkHappierReportExtract( Map<String, Object> extractParameters );
}
