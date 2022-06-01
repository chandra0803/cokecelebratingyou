/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/ClaimReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.ClaimReportsDAO;
import com.biperf.core.service.reports.ClaimReportsService;

/**
 * ClaimReportsServiceImpl.
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
public class ClaimReportsServiceImpl implements ClaimReportsService
{
  private ClaimReportsDAO claimReportsDAO;

  public void setClaimReportsDAO( ClaimReportsDAO claimReportsDAO )
  {
    this.claimReportsDAO = claimReportsDAO;
  }

  // =======================================
  // CLAIM BY ORG REPORT
  // =======================================

  @Override
  public Map<String, Object> getClaimByOrgTabularResults( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgSubmissionStatusChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgSubmissionStatusChart( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgMonthlyChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgMonthlyChart( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgParticipationRateChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgParticipationRateChart( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgParticipationLevelChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgParticipationLevelChart( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgItemStatusChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgItemStatusChart( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgTotalsChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByOrgTotalsChart( reportParameters );
  }

  // =======================================
  // CLAIM BY PAX REPORT
  // =======================================

  @Override
  public Map<String, Object> getClaimByPaxTabularResults( Map<String, Object> reportParameters, boolean includeChildNodes )
  {
    return claimReportsDAO.getClaimByPaxTabularResults( reportParameters, includeChildNodes );
  }

  @Override
  public Map<String, Object> getClaimByPaxClaimListTabularResults( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByPaxClaimListTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxSubmittedClaims( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByPaxSubmittedClaims( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxClaimStatusChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByPaxClaimStatusChart( reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxItemStatusChart( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimByPaxItemStatusChart( reportParameters );
  }

  // =======================================
  // CLAIM EXTRACT REPORT
  // =======================================

  @Override
  public Map getClaimExtractResults( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getClaimExtractResults( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getItemsClaimExtractResults( Map<String, Object> reportParameters )
  {
    return claimReportsDAO.getItemsClaimExtractResults( reportParameters );
  }
}
