/**
 * 
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * @author poddutur
 *
 */
public interface AwardsReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "awardsReportsService";

  public Map<String, Object> getTotalPointsIssuedForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getOtherAwardsIssuedForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getReceivedNotReceivedAwardsPieResults( Map<String, Object> reportParameters );

  public Map<String, Object> getReceivedNotReceivedAwardsForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPersonsReceivingAwardsForOrgBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getAwardsSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getAwardsFirstDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getAwardsSecondDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTotalPointsIssuedByPaxBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getReceivedNotReceivedAwardsPaxPieResults( Map<String, Object> reportParameters );

  public Map<String, Object> getAwardsSummaryByPaxResults( Map<String, Object> reportParameters );

  public Map<String, Object> getAwardsDetailByPaxResults( Map<String, Object> reportParameters );

  public Map getAwardsActivityReportExtract( Map<String, Object> reportParameters );

}
