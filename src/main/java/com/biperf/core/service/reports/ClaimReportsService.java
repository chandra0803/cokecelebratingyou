/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/ClaimReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * ClaimReportsService.
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
 * <td>Aug 17, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ClaimReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "claimReportsService";

  // =======================================
  // CLAIM BY ORG REPORT
  // =======================================

  public Map<String, Object> getClaimByOrgTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByOrgSubmissionStatusChart( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByOrgMonthlyChart( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByOrgParticipationRateChart( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByOrgParticipationLevelChart( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByOrgItemStatusChart( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByOrgTotalsChart( Map<String, Object> reportParameters );

  // =======================================
  // CLAIM BY PAX REPORT
  // =======================================

  public Map<String, Object> getClaimByPaxTabularResults( Map<String, Object> reportParameters, boolean includeChildNodes );

  public Map<String, Object> getClaimByPaxClaimListTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByPaxSubmittedClaims( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByPaxClaimStatusChart( Map<String, Object> reportParameters );

  public Map<String, Object> getClaimByPaxItemStatusChart( Map<String, Object> reportParameters );

  // =======================================
  // CLAIM EXTRACT REPORT
  // =======================================

  public Map getClaimExtractResults( Map<String, Object> reportParameters );

  @SuppressWarnings( "rawtypes" )
  public Map getItemsClaimExtractResults( Map<String, Object> reportParameters );

  // =======================================
  // CLAIM TOTALS
  // =======================================
}
