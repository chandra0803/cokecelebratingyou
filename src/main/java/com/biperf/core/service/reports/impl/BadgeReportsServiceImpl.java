
package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.BadgeReportsDAO;
import com.biperf.core.service.reports.BadgeReportsService;

public class BadgeReportsServiceImpl implements BadgeReportsService
{

  private BadgeReportsDAO badgeReportsDAO;

  @Override
  public Map<String, Object> getBadgeActivityByOrgSummaryTabularResults( Map<String, Object> reportParameters )
  {
    return badgeReportsDAO.getBadgeActivityByOrgSummaryTabularResults( reportParameters );
  }

  @Override
  public Map getBadgeActivityExtractResults( Map<String, Object> reportParameters )
  {
    return badgeReportsDAO.getBadgeActivityExtractResults( reportParameters );
  }

  @Override
  public Map<String, Object> getBadgesEarnedBarChartResults( Map<String, Object> reportParameters )
  {
    return badgeReportsDAO.getBadgesEarnedBarChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getBadgeActivityTeamLevelTabularResults( Map<String, Object> reportParameters )
  {
    return badgeReportsDAO.getBadgeActivityTeamLevelTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getBadgeActivityParticipantLevelTabularResults( Map<String, Object> reportParameters )
  {
    return badgeReportsDAO.getBadgeActivityParticipantLevelTabularResults( reportParameters );
  }

  public void setBadgeReportsDAO( BadgeReportsDAO badgeReportsDAO )
  {
    this.badgeReportsDAO = badgeReportsDAO;
  }

}
