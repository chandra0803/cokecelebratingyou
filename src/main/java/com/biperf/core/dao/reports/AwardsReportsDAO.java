/**
 * 
 */

package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * @author poddutur
 *
 */
public interface AwardsReportsDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "awardsReportsDAO";

  public Map<String, Object> getTotalPointsIssuedForOrgBarResults( Map<String, Object> reportParameters );

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
