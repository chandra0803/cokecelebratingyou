/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/BudgetReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.BudgetReportsDAO;
import com.biperf.core.service.reports.BudgetReportsService;

/**
 * BudgetReportsServiceImpl.
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
public class BudgetReportsServiceImpl implements BudgetReportsService
{
  private BudgetReportsDAO budgetReportsDAO;

  public void setBudgetReportsDAO( BudgetReportsDAO budgetReportsDAO )
  {
    this.budgetReportsDAO = budgetReportsDAO;
  }

  // =======================================
  // POINTS BUDGET BALANCE REPORT
  // =======================================

  @Override
  public Map<String, Object> getPointsBudgetBalanceTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetBalanceTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPointsBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetBalanceByPromotionTabularResults( reportParameters );
  }

  // =======================================
  // CASH BUDGET BALANCE REPORT
  // =======================================

  @Override
  public Map<String, Object> getCashBudgetBalanceTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetBalanceTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getCashBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetBalanceByPromotionTabularResults( reportParameters );
  }

  // =======================================
  // BUDGET EXTRACT REPORT
  // =======================================

  @Override
  public Map getPointsBudgetExtractResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetExtractResults( reportParameters );
  }

  @Override
  public Map getPointsBudgetIssuanceExtractResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetIssuanceExtractResults( reportParameters );
  }

  @Override
  public Map getPointsBudgetSecondLevelExtractResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetSecondLevelExtractResults( reportParameters );
  }

  @Override
  public Map getCashBudgetExtractResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetExtractResults( reportParameters );
  }

  @Override
  public Map getCashBudgetSecondLevelExtractResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetSecondLevelExtractResults( reportParameters );
  }

  @Override
  public Map getCashBudgetIssuanceExtractResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetIssuanceExtractResults( reportParameters );
  }

  // ==============
  // BUDGET TOTALS
  // ==============
  @Override
  public Map<String, Object> getPointsBudgetUtilizationLevelChart( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetUtilizationLevelChart( reportParameters );
  }

  @Override
  public Map<String, Object> getPointsBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getPointsBudgetUtilizationByPercentageChart( reportParameters );
  }

  @Override
  public Map<String, Object> getCashBudgetUtilizationInPointsChart( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetUtilizationInPointsChart( reportParameters );
  }

  @Override
  public Map<String, Object> getCashBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getCashBudgetUtilizationByPercentageChart( reportParameters );
  }
  @Override
  public Map<String, Object> getBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getBudgetUtilizationByPercentageChart( reportParameters );
  }
  
  @Override
  public Map<String, Object> getBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getBudgetBalanceByPromotionTabularResults( reportParameters );
  }


  @Override
  public Map<String, Object> getBudgetBalanceTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getBudgetBalanceTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getBudgetDepositedDetailTabularResults( Map<String, Object> reportParameters )
  {
    return budgetReportsDAO.getBudgetDepositedDetailTabularResults( reportParameters );
  }
  //-------------------------------
  // Client customization for WIP #27192 start
  //-------------------------------   
  @Override
  public int getActiveBudgetForNodebyId (Long nodeId)
  {
    return budgetReportsDAO.getActiveBudgetForNodebyId( nodeId );
  }
  //-------------------------------
  // Client customization for WIP #27192 end
  //-------------------------------  

@Override
public Map<String, Object> getBudgetUtilizationInPointsChart(Map<String, Object> reportParameters) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Map<String, Object> getBudgetIssuedDetailTabularResults(Map<String, Object> reportParameters) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Map<String, Object> getBudgetAllocatedDetailTabularResults(Map<String, Object> reportParameters) {
	// TODO Auto-generated method stub
	return null;
}
}
