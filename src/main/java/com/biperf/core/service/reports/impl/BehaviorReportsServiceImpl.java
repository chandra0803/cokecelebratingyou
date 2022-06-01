
package com.biperf.core.service.reports.impl;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.reports.BehaviorReportsDAO;
import com.biperf.core.service.reports.BehaviorReportsService;

/**
 * Hibernate implementation of BehaviorReportsService interface
 */
public class BehaviorReportsServiceImpl implements BehaviorReportsService
{
  private BehaviorReportsDAO behaviorReportsDAO;

  @Override
  public Map<String, Object> getBehaviorsTabularResults( Map<String, Object> reportParameters )
  {
    return behaviorReportsDAO.getBehaviorsTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getBehaviorsBarchartResults( Map<String, Object> reportParameters )
  {
    return behaviorReportsDAO.getBehaviorsBarchartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getBehaviorsPiechartResults( Map<String, Object> reportParameters )
  {
    return behaviorReportsDAO.getBehaviorsPiechartResults( reportParameters );
  }

  @Override
  public List<String> getBehaviorsByPromo( String promotionId, String locale )
  {
    return behaviorReportsDAO.getBehaviorsByPromo( promotionId, locale );
  }

  @Override
  public Map getBehaviorReportExtract( Map<String, Object> reportParameters )
  {
    return behaviorReportsDAO.getBehaviorReportExtract( reportParameters );
  }

  public void setBehaviorReportsDAO( BehaviorReportsDAO behaviorReportsDAO )
  {
    this.behaviorReportsDAO = behaviorReportsDAO;
  }

}
