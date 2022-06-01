
package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * 
 * EngagementReportsService.
 * 
 * @author kandhi
 * @since Aug 21, 2014
 * @version 1.0
 */
public interface EngagementReportsService extends SAO
{
  public static String BEAN_NAME = "engagementReportsService";

  public Map<String, Object> getParticipationScoreReportExtract( Map<String, Object> reportParameters );
}
