
package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface EnrollmentReportsService extends SAO
{
  public static String BEAN_NAME = "enrollmentReportsService";

  // =======================================
  // ENROLLMENT ACTIVITY REPORT
  // =======================================

  public Map<String, Object> getEnrollmentSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getEnrollmentDetailsResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTotalEnrollmentsBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getEnrollmentStatusPercentageBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getEnrollmentActiveInactiveBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPieResults( Map<String, Object> reportParameters );

  // =======================================
  // ENROLLMENT EXTRACT REPORT
  // =======================================

  public Map getEnrollmentExtractResults( Map<String, Object> reportParameters );
}
