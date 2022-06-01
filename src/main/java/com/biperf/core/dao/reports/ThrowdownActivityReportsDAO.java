
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * ThrowdownActivityReportsDAO.
 * 
 * @author kandhi
 * @since Oct 21, 2013
 * @version 1.0
 */
public interface ThrowdownActivityReportsDAO extends DAO
{

  public static final String BEAN_NAME = "throwdownActivityReportsDAO";

  public Map<String, Object> getThrowdownActivityByPaxSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getThrowdownActivityByPaxDetailTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getThrowdownTotalActivityChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getThrowdownActivityByRoundChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPointsEarnedInThrowdownChartResults( Map<String, Object> reportParameters );

  public Map getThrowdownActivityByPaxReportExtract( Map<String, Object> reportParameters );

}
