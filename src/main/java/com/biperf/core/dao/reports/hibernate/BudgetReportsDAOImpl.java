/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/BudgetReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.BudgetReportsDAO;
import com.biperf.core.utils.UserManager;

/**
 * 
 * BudgetReportsDAOImpl.
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
public class BudgetReportsDAOImpl extends BaseReportsDAO implements BudgetReportsDAO
{
  private DataSource dataSource;

  // =======================================
  // POINTS BUDGET BALANCE REPORT
  // =======================================

  @Override
  public Map<String, Object> getPointsBudgetBalanceTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "PROMO_NAME";
          break;
        case 3:
          sortColName = "PROMO_DATE";
          break;
        case 4:
          sortColName = "BUDGET_MASTER_NAME";
          break;
        case 6:
          sortColName = "ORIGINAL_BUDGET";
          break;
        case 7:
          sortColName = "BUDGET_ADJUSTMENTS";
          break;
        case 8:
          sortColName = "AWARDED";
          break;
        case 9:
          sortColName = "AVAILABLE_BALANCE";
          break;
        default:
          sortColName = "PROMO_NAME";
          break;
      }
    }
    String procName = "pkg_query_point_budget_balance.prc_getBalanceTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callPointsProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getPointsBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "BUDGET_OWNER";
          break;
        case 2:
          sortColName = "BUDGET_MASTER_NAME";
          break;
        case 3:
          sortColName = "BUDGET_PERIOD";
          break;
        case 4:
          sortColName = "ORIGINAL_BALANCE";
          break;
        case 5:
          sortColName = "BUDGET_ADJUSTMENTS";
          break;
        case 6:
          sortColName = "AWARDED";
          break;
        case 7:
          sortColName = "AVAILABLE_BALANCE";
          break;
        default:
          sortColName = "BUDGET_OWNER";
          break;
      }
    }

    String procName = "pkg_query_point_budget_balance.prc_getBalanceByPromoTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callPointsProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getPointsBudgetUtilizationLevelChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_point_budget_balance.prc_getUseInPointsChart";
    return callPointsProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getPointsBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_point_budget_balance.prc_getUseByPercentageChart";
    return callPointsProc( procName, reportParameters );
  }

  // =======================================
  // CASH BUDGET BALANCE REPORT
  // =======================================

  @Override
  public Map<String, Object> getCashBudgetBalanceTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "PROMO_NAME";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "PROMO_NAME";
          break;
        case 2:
            sortColName = "PROMO_DATE";
            break;          
        case 3:
          sortColName = "BUDGET_MASTER_NAME";
          break;
        case 4:
          sortColName = "BUDGET_PERIOD";
          break;
        case 5:
          sortColName = "ORIGINAL_BUDGET";
          break;
        case 6:
          sortColName = "BUDGET_ADJUSTMENTS";
          break;
        case 7:
          sortColName = "AWARDED";
          break;
        case 8:
          sortColName = "AVAILABLE_BALANCE";
          break;
        default:
          sortColName = "PROMO_NAME";
          break;
      }
    }
    String procName = "pkg_query_cash_budget_balance.prc_getBalanceTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callCashProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getCashBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "BUDGET_OWNER";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "BUDGET_OWNER";
          break;
        case 2:
          sortColName = "BUDGET_MASTER_NAME";
          break;
        case 3:
          sortColName = "BUDGET_PERIOD";
          break;
        case 4:
          sortColName = "ORIGINAL_BUDGET";
          break;
        case 5:
          sortColName = "BUDGET_ADJUSTMENTS";
          break;
        case 6:
          sortColName = "AWARDED";
          break;
        case 7:
          sortColName = "AVAILABLE_BALANCE";
          break;
        default:
          sortColName = "BUDGET_OWNER";
          break;
      }
    }

    String procName = "pkg_query_cash_budget_balance.prc_getBalanceByPromoTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callCashProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getCashBudgetUtilizationInPointsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_cash_budget_balance.prc_getUseInPointsChart";
    return callCashProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getCashBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_cash_budget_balance.prc_getUseByPercentageChart";
    return callCashProc( procName, reportParameters );
  }
  @Override
  public Map<String, Object> getBudgetUtilizationByPercentageChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_budget_balance.prc_getUseByPercentageChart";
    return callProc(procName, reportParameters);
  }
  

  // =======================================
  // BUDGET BALANCE REPORT
  // =======================================

  @Override
  public Map<String, Object> getBudgetBalanceTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "PROMO_NAME";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "PROMO_NAME";
          break;
        case 3:
          sortColName = "PROMO_DATE";
          break;
        case 4:
          sortColName = "BUDGET_NAME";
          break;
        case 5:
          sortColName = "BUDGET_PERIOD";
          break;
        case 6:
          sortColName = "CURRENT_DEPOSITED";
          break;
        case 7:
          sortColName = "CURRENT_NET_TRANSFERS";
          break;
        case 8:
          sortColName = "CURRENT_TOTAL_BUDGET";
          break;
        case 9:
          sortColName = "CURRENT_ISSUED";
          break;
        case 10:
          sortColName = "CURRENT_BUDGET_REMAINING";
          break;
        case 11:
          sortColName = "CURRENT_BUDGET_UTILIZATION";
          break;
        case 12:
          sortColName = "PRIOR_PERIOD_DEPOSITED";
          break;
        case 13:
          sortColName = "PRIOR_PERIOD_NETTRANSFERS";
          break;
        case 14:
          sortColName = "PRIOR_PERIOD_TOTAL_BUDGET";
          break;
        case 15:
          sortColName = "PRIOR_PERIOD_ISSUED";
          break;
        case 16:
          sortColName = "PRIOR_PERIOD_BUDGET_REMAINING";
          break;
        case 17:
          sortColName = "PRIOR_PERIOD_USE";
          break;
        default:
          sortColName = "PROMO_NAME";
          break;
      }
    }
    String procName = "pkg_query_budget_balance.prc_getBalanceTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callProc(procName, reportParameters);
  } 

  @Override
  public Map<String, Object> getBudgetBalanceByPromotionTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "BUDGET_OWNER";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "BUDGET_OWNER";
          break;
        case 2:
          sortColName = "BUDGET_NAME";
          break;
        case 3:
          sortColName = "BUDGET_PERIOD";
          break;
        case 4:
          sortColName = "CURRENT_DEPOSITED";
          break;
        case 5:
          sortColName = "CURRENT_NET_TRANSFERS";
          break;
        case 6:
          sortColName = "CURRENT_TOTAL_BUDGET";
          break;
        case 7:
          sortColName = "CURRENT_ISSUED";
          break;
        case 8:
          sortColName = "CURRENT_BUDGET_REMAINING";
          break;
        case 9:
          sortColName = "CURRENT_BUDGET_UTILIZATION";
          break;
        case 10:
          sortColName = "PRIOR_PERIOD_DEPOSITED";
          break;
        case 11:
          sortColName = "PRIOR_PERIOD_NETTRANSFERS";
          break;
        case 12:
          sortColName = "PRIOR_PERIOD_TOTAL_BUDGET";
          break;
        case 13:
          sortColName = "PRIOR_PERIOD_ISSUED";
          break;
        case 14:
          sortColName = "PRIOR_PERIOD_BUDGET_REMAINING";
          break;
        case 15:
          sortColName = "PRIOR_PERIOD_USE";
          break;
        default:
          sortColName = "BUDGET_OWNER";
          break;
      }
    }

    String procName = "pkg_query_budget_balance.prc_getBalanceByPromoTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callProc(procName, reportParameters);
  }

  
  @Override
  public Map<String, Object> getBudgetDepositedDetailTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "BUDGET_OWNER";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "TRANS_DATE";
          break;
        case 2:
          sortColName = "TRANS_AMOUNT";
          break;
        default:
          sortColName = "TRANS_DATE";
          break;
      }
    }

    String procName = "pkg_query_budget_balance.prc_getDepositedDetailTabRes";
    reportParameters.put( "sortedOn", sortColName );
    return callProc(procName, reportParameters);
  }
  // =======================================
  // BUDGET EXTRACT REPORT
  // =======================================

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getPointsBudgetExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcPointsBudgetExtract procedure = new CallPrcPointsBudgetExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getPointsBudgetIssuanceExtractResults( Map<String, Object> reportParameters )
  {
    CallPointsPrcBudgetIssuanceExtract procedure = new CallPointsPrcBudgetIssuanceExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getPointsBudgetSecondLevelExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcPointsBudgetSecondLevelExtract procedure = new CallPrcPointsBudgetSecondLevelExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getCashBudgetExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcCashBudgetExtract procedure = new CallPrcCashBudgetExtract( dataSource );
    reportParameters.put( "userId", UserManager.getUserId() );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getCashBudgetSecondLevelExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcCashBudgetSecondLevelExtract procedure = new CallPrcCashBudgetSecondLevelExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getCashBudgetIssuanceExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcCashBudgetIssuanceExtract procedure = new CallPrcCashBudgetIssuanceExtract( dataSource );
    reportParameters.put( "userId", UserManager.getUserId() );
    return procedure.executeProcedure( reportParameters );
  }

  private Map<String, Object> callPointsProc( String procName, Map<String, Object> reportParameters )
  {
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcPointsBudgetBalance procedure = new CallPrcPointsBudgetBalance( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  private Map<String, Object> callCashProc( String procName, Map<String, Object> reportParameters )
  {
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcCashBudgetBalance procedure = new CallPrcCashBudgetBalance( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }
  private Map<String, Object> callProc(String procName, Map<String, Object> reportParameters)
{
  setLocaleDatePattern( reportParameters );
  setFilterReportParameters( reportParameters );
  CallPrcBudgetBalance procedure = new CallPrcBudgetBalance( dataSource, procName );
  return procedure.executeProcedure( reportParameters );
}
  //-------------------------------
  // Client customization for WIP #27192 start
  //-------------------------------    
  public int getActiveBudgetForNodebyId (Long nodeId)
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getActiveBudgetForNodebyId" );
    query.setParameter( "nodeId", nodeId );

    return (Integer)query.uniqueResult(); 
  }
  //-------------------------------
  // Client customization for WIP #27192 end
  //-------------------------------    
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
