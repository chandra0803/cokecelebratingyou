
package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.EngagementReportsDAO;
import com.biperf.core.service.reports.EngagementReportsService;

/**
 * 
 * EngagementReportsServiceImpl.
 * 
 * @author kandhi
 * @since Aug 21, 2014
 * @version 1.0
 */
public class EngagementReportsServiceImpl implements EngagementReportsService
{
  private EngagementReportsDAO engagementReportsDAO;

  public void setEngagementReportsDAO( EngagementReportsDAO engagementReportsDAO )
  {
    this.engagementReportsDAO = engagementReportsDAO;
  }

  @Override
  public Map<String, Object> getParticipationScoreReportExtract( Map<String, Object> reportParameters )
  {
    return engagementReportsDAO.getParticipationScoreReportExtract( reportParameters );
  }

}
