
package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * 
 * ThrowdownActivityReportsService.
 * 
 * @author kandhi
 * @since Oct 21, 2013
 * @version 1.0
 */
public interface ThrowdownActivityReportsService extends SAO
{
  public static String BEAN_NAME = "throwdownActivityReportsService";

  public Map<String, Object> getThrowdownActivityByPaxSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getThrowdownActivityByPaxDetailTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getThrowdownTotalActivityChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getThrowdownActivityByRoundChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPointsEarnedInThrowdownChartResults( Map<String, Object> reportParameters );

  public Map getThrowdownActivityByPaxReportExtract( Map<String, Object> reportParameters );
}
