/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/NominationReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.NominationReportsDAO;
import com.biperf.core.service.reports.NominationReportsService;

/**
 * NominationReportsServiceImpl.
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
 * <td>drahn</td>
 * <td>Aug 3, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class NominationReportsServiceImpl implements NominationReportsService
{
  private NominationReportsDAO nominationReportsDAO;

  @Override
  public Map<String, Object> getTotalNominationsChartResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getTotalNominationsChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getTopNominatorsChartResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getTopNominatorsChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationReceiverTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    return nominationReportsDAO.getNominationReceiverTabularResults( reportParameters, nodeAndBelow );
  }

  @Override
  public Map<String, Object> getNominationTopNomineeChartResult( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationTopNomineeChartResult( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationGiverNominatorsListTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    return nominationReportsDAO.getNominationGiverNominatorsListResults( reportParameters, nodeAndBelow );
  }

  @Override
  public Map<String, Object> getNominationGiverNominationsListTabularResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationGiverNominationsListResults( reportParameters );
  }

  @Override
  public Map getNominationExtractResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationExtractResults( reportParameters );
  }

  @Override
  public Map getNominationSummaryExtractResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationSummaryExtractResults( reportParameters );
  }

  // =======================================
  // NOMINATION GIVER BY ORG REPORT
  // =======================================

  @Override
  public Map<String, Object> getNominationsGivenByOrgSummaryTabularResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationsGivenByOrgSummaryTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getNominatorsByOrgChartResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominatorsByOrgChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getNomineesByOrgChartResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNomineesByOrgChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationsByMonthChartResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationsByMonthChartResults( reportParameters );
  }

  public void setNominationReportsDAO( NominationReportsDAO nominationReportsDAO )
  {
    this.nominationReportsDAO = nominationReportsDAO;
  }

  @Override
  public Map<String, Object> getNominationNomineeTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    return nominationReportsDAO.getNominationNomineeTabularResults( reportParameters, nodeAndBelow );
  }

  // =======================================
  // NOMINATION AGING REPORT
  // =======================================

  @Override
  public Map<String, Object> getNominationAgingTabularResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationAgingTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationAgingBarchartResults( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationAgingBarchartResults( reportParameters );
  }

  @Override
  public Map getNominationAgingReportDetailExtract( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationAgingReportDetailExtract( reportParameters );
  }

  @Override
  public Map getNominationAgingReportSummaryExtract( Map<String, Object> reportParameters )
  {
    return nominationReportsDAO.getNominationAgingReportSummaryExtract( reportParameters );
  }

  @Override
  public Map<String, Object> getTotalUniqueNominatorsByMonthChartResults( Map<String, Object> reportParameters )
  {
    return null;
  }

  @Override
  public Map<String, Object> getPointsIssuedByPromotionChartResults( Map<String, Object> reportParameters )
  {
    return null;
  }

  @Override
  public Map<String, Object> getTotalNominationsByPromotionChartResults( Map<String, Object> reportParameters )
  {
    return null;
  }
}
