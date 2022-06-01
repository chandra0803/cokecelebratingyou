
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * EnrollmentReportsDAO.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>poddutur</td>
 * <td>JULY 17, 2012</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */

public interface EnrollmentReportsDAO extends DAO
{
  public static final String BEAN_NAME = "enrollmentReportsDAO";

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
