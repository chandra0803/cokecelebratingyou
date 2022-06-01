/**
 * 
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.EnrollmentReportsDAO;
import com.biperf.core.service.reports.EnrollmentReportsService;

/**
 * @author poddutur
 *
 */
public class EnrollmentReportsServiceImpl implements EnrollmentReportsService
{

  private EnrollmentReportsDAO enrollmentReportsDAO;

  // =======================================
  // ENROLLMENT ACTIVITY REPORT
  // =======================================

  public void setEnrollmentReportsDAO( EnrollmentReportsDAO enrollmentReportsDAO )
  {
    this.enrollmentReportsDAO = enrollmentReportsDAO;
  }

  @Override
  public Map<String, Object> getEnrollmentSummaryResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getEnrollmentSummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getEnrollmentDetailsResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getEnrollmentDetailsResults( reportParameters );
  }

  @Override
  public Map<String, Object> getTotalEnrollmentsBarResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getTotalEnrollmentsBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getEnrollmentStatusPercentageBarResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getEnrollmentStatusPercentageBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getEnrollmentActiveInactiveBarResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getEnrollmentActiveInactiveBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPieResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getPieResults( reportParameters );
  }

  // =======================================
  // ENROLLMENT EXTRACT REPORT
  // =======================================

  @Override
  public Map getEnrollmentExtractResults( Map<String, Object> reportParameters )
  {
    return enrollmentReportsDAO.getEnrollmentExtractResults( reportParameters );
  }
}
