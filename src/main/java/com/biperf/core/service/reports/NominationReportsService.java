/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/NominationReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * NominationReportsService.
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
public interface NominationReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "nominationReportsService";

  public Map<String, Object> getTotalNominationsChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTopNominatorsChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTotalUniqueNominatorsByMonthChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPointsIssuedByPromotionChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTotalNominationsByPromotionChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getNominationReceiverTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow );

  public Map<String, Object> getNominationTopNomineeChartResult( Map<String, Object> reportParameters );

  public Map<String, Object> getNominationGiverNominatorsListTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow );

  public Map<String, Object> getNominationGiverNominationsListTabularResults( Map<String, Object> reportParameters );

  public Map getNominationExtractResults( Map<String, Object> reportParameters );

  public Map getNominationSummaryExtractResults( Map<String, Object> reportParameters );

  // =======================================
  // NOMINATIONS GIVEN BY ORG REPORT
  // =======================================

  public Map<String, Object> getNominationsGivenByOrgSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getNominatorsByOrgChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getNomineesByOrgChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getNominationsByMonthChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getNominationNomineeTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow );

  // =======================================
  // NOMINATION AGING REPORT
  // =======================================

  public Map<String, Object> getNominationAgingTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getNominationAgingBarchartResults( Map<String, Object> reportParameters );

  public Map getNominationAgingReportDetailExtract( Map<String, Object> reportParameters );

  public Map getNominationAgingReportSummaryExtract( Map<String, Object> reportParameters );

}
