/**
 * 
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.AwardsReportsDAO;
import com.biperf.core.service.reports.AwardsReportsService;

/**
 * @author poddutur
 *
 */
public class AwardsReportsServiceImpl implements AwardsReportsService
{
  private AwardsReportsDAO awardsReportsDAO;

  @Override
  public Map<String, Object> getTotalPointsIssuedForOrgBarResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getTotalPointsIssuedForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getReceivedNotReceivedAwardsForOrgBarResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getReceivedNotReceivedAwardsForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPersonsReceivingAwardsForOrgBarResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getPersonsReceivingAwardsForOrgBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsSummaryResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getAwardsSummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsFirstDetailResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getAwardsFirstDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsSecondDetailResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getAwardsSecondDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getTotalPointsIssuedByPaxBarResults( Map<String, Object> reportParameters )// 1
  {
    return awardsReportsDAO.getTotalPointsIssuedByPaxBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getReceivedNotReceivedAwardsPaxPieResults( Map<String, Object> reportParameters )// 2
  {
    return awardsReportsDAO.getReceivedNotReceivedAwardsPaxPieResults( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsSummaryByPaxResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getAwardsSummaryByPaxResults( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsDetailByPaxResults( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getAwardsDetailByPaxResults( reportParameters );
  }

  @Override
  public Map getAwardsActivityReportExtract( Map<String, Object> reportParameters )
  {
    return awardsReportsDAO.getAwardsActivityReportExtract( reportParameters );
  }

  public void setAwardsReportsDAO( AwardsReportsDAO awardsReportsDAO )
  {
    this.awardsReportsDAO = awardsReportsDAO;
  }

  @Override
  public Map<String, Object> getOtherAwardsIssuedForOrgBarResults( Map<String, Object> reportParameters )
  {
    return null;
  }

  @Override
  public Map<String, Object> getReceivedNotReceivedAwardsPieResults( Map<String, Object> reportParameters )
  {
    return null;
  }

}
