
package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.ThrowdownActivityReportsDAO;
import com.biperf.core.service.reports.ThrowdownActivityReportsService;

/**
 * 
 * ThrowdownActivityReportsServiceImpl.
 * 
 * @author kandhi
 * @since Oct 21, 2013
 * @version 1.0
 */
public class ThrowdownActivityReportsServiceImpl implements ThrowdownActivityReportsService
{

  private ThrowdownActivityReportsDAO throwdownActivityReportsDAO;

  // *************************************************************//
  // ********** START THROWDOWN ACTIVITY BY PAX REPORT ***********//
  // *************************************************************//
  @Override
  public Map<String, Object> getThrowdownActivityByPaxSummaryTabularResults( Map<String, Object> reportParameters )
  {
    return throwdownActivityReportsDAO.getThrowdownActivityByPaxSummaryTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getThrowdownActivityByPaxDetailTabularResults( Map<String, Object> reportParameters )
  {
    return throwdownActivityReportsDAO.getThrowdownActivityByPaxDetailTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getThrowdownTotalActivityChartResults( Map<String, Object> reportParameters )
  {
    return throwdownActivityReportsDAO.getThrowdownTotalActivityChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getThrowdownActivityByRoundChartResults( Map<String, Object> reportParameters )
  {
    return throwdownActivityReportsDAO.getThrowdownActivityByRoundChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPointsEarnedInThrowdownChartResults( Map<String, Object> reportParameters )
  {
    return throwdownActivityReportsDAO.getPointsEarnedInThrowdownChartResults( reportParameters );
  }

  @Override
  public Map getThrowdownActivityByPaxReportExtract( Map<String, Object> reportParameters )
  {
    return throwdownActivityReportsDAO.getThrowdownActivityByPaxReportExtract( reportParameters );
  }

  // *************************************************************//
  // *********** END THROWDOWN ACTIVITY BY PAX REPORT ************//
  // *************************************************************//

  public void setThrowdownActivityReportsDAO( ThrowdownActivityReportsDAO throwdownActivityReportsDAO )
  {
    this.throwdownActivityReportsDAO = throwdownActivityReportsDAO;
  }
}
