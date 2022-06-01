/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/BudgetReportsDAO.java,v $
 *
 */

package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * BudgetReportsDAO.
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
 * <td>Aug 29, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface BudgetReportsDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "budgetReportsDAO";

  // =======================================
  // POINTS BUDGET BALANCE REPORT
  // =======================================

  public Map<String, Object> getPointsBudgetBalanceTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPointsBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters );

  // =======================================
  // CASH BUDGET BALANCE REPORT
  // =======================================

  public Map<String, Object> getCashBudgetBalanceTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getCashBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters );

  // =======================================
  // BUDGET EXTRACT REPORT
  // =======================================

  public Map getPointsBudgetExtractResults( Map<String, Object> reportParameters );

  public Map getPointsBudgetIssuanceExtractResults( Map<String, Object> reportParameters );

  public Map getPointsBudgetSecondLevelExtractResults( Map<String, Object> reportParameters );

  public Map getCashBudgetExtractResults( Map<String, Object> reportParameters );

  public Map getCashBudgetSecondLevelExtractResults( Map<String, Object> reportParameters );

  public Map getCashBudgetIssuanceExtractResults( Map<String, Object> reportParameters );

  // =======================================
  // BUDGET CHARTS
  // =======================================

  public Map<String, Object> getPointsBudgetUtilizationLevelChart( Map<String, Object> reportParameters );

  public Map<String, Object> getPointsBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters );

  public Map<String, Object> getCashBudgetUtilizationInPointsChart( Map<String, Object> reportParameters );
  
  public Map<String, Object> getBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters );
  
  public Map<String, Object> getCashBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters );

  public Map<String, Object> getBudgetDepositedDetailTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters );
 
  public Map<String, Object> getBudgetBalanceTabularResults( Map<String, Object> reportParameters );

  //-------------------------------
  // Client customization for WIP #27192 start
  //-------------------------------    
  public int getActiveBudgetForNodebyId (Long nodeId);
  //-------------------------------
  // Client customization for WIP #27192 end
}
