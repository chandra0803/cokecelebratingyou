
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * EngagementReportsDAO.
 * 
 * @author kandhi
 * @since Aug 21, 2014
 * @version 1.0
 */
public interface EngagementReportsDAO extends DAO
{
  public static final String BEAN_NAME = "engagementReportsDAO";

  public Map<String, Object> getParticipationScoreReportExtract( Map<String, Object> reportParameters );

}
